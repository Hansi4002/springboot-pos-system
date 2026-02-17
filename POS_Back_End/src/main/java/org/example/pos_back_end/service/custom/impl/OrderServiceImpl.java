package org.example.pos_back_end.service.custom.impl;

import lombok.RequiredArgsConstructor;
import org.example.pos_back_end.dto.OrderDTO;
import org.example.pos_back_end.dto.OrderDetailDTO;
import org.example.pos_back_end.entity.Customer;
import org.example.pos_back_end.entity.Item;
import org.example.pos_back_end.entity.Order;
import org.example.pos_back_end.entity.OrderDetail;
import org.example.pos_back_end.repository.CustomerRepository;
import org.example.pos_back_end.repository.ItemRepository;
import org.example.pos_back_end.repository.OrderRepository;
import org.example.pos_back_end.service.custom.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {

        if (orderDTO == null || orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("OrderDTO or items cannot be null/empty");
        }

        // 1️⃣ Validate Customer
        Customer customer = customerRepository.findById(orderDTO.getCId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 2️⃣ Generate Order ID
        String orderId = generateOrderId();

        // 3️⃣ Create Order
        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomer(customer);
        order.setDate(orderDTO.getDate() != null ? orderDTO.getDate() : new Date());

        double total = 0.0;

        // 4️⃣ Process Items
        for (OrderDetailDTO detailDTO : orderDTO.getItems()) {

            if (detailDTO.getItemCode() == null) {
                throw new IllegalArgumentException("Item code cannot be null");
            }

            Item item = itemRepository.findById(detailDTO.getItemCode())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + detailDTO.getItemCode()));

            // Check stock
            if (item.getQtyOnHand() < detailDTO.getQty()) {
                throw new RuntimeException("Insufficient stock for item: " + item.getDescription());
            }

            // Reduce stock
            item.setQtyOnHand(item.getQtyOnHand() - detailDTO.getQty());
            itemRepository.save(item);

            // Create OrderDetail
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setItem(item);
            detail.setQty(detailDTO.getQty());
            detail.setUnitPrice(detailDTO.getUnitPrice());

            // Add to order
            order.getOrderDetails().add(detail);

            // Calculate total
            total += detailDTO.getQty() * detailDTO.getUnitPrice();
        }

        order.setTotal(total);

        // Save order (cascade saves orderDetails)
        orderRepository.save(order);

        // ★★★ IMPORTANT: Set the generated orderId in the DTO before returning ★★★
        orderDTO.setOrderId(orderId);  // ← ADD THIS LINE!

        return orderDTO;
    }

    private String generateOrderId() {
        // Get all existing order IDs
        List<Order> allOrders = orderRepository.findAll();

        if (allOrders.isEmpty()) {
            return "OD001"; // First order
        }

        // Find the highest number
        int maxNumber = 0;
        for (Order order : allOrders) {
            String orderId = order.getOrderId();
            if (orderId != null && orderId.startsWith("OD")) {
                try {
                    // Extract the number part (after "OD")
                    String numberPart = orderId.substring(2);
                    int num = Integer.parseInt(numberPart);
                    if (num > maxNumber) {
                        maxNumber = num;
                    }
                } catch (NumberFormatException e) {
                    // Skip if not a number format
                }
            }
        }

        // Increment and format with leading zeros
        int nextNumber = maxNumber + 1;
        return String.format("OD%03d", nextNumber); // OD001, OD002, ..., OD999
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByCustomer(String customerId) {
        List<Order> orders = orderRepository.findByCustomerCId(customerId);
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Restore stock for each orderDetail
        for (OrderDetail detail : order.getOrderDetails()) {
            Item item = detail.getItem();
            item.setQtyOnHand(item.getQtyOnHand() + detail.getQty());
            itemRepository.save(item);
        }

        orderRepository.delete(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCId(order.getCustomer().getCId());
        dto.setDate(order.getDate());
        dto.setTotal(order.getTotal());

        List<OrderDetailDTO> items = order.getOrderDetails()
                .stream()
                .map(detail -> new OrderDetailDTO(
                        detail.getItem().getCode(),
                        detail.getQty(),
                        detail.getUnitPrice()
                ))
                .collect(Collectors.toList());

        dto.setItems(items);

        return dto;
    }
}
