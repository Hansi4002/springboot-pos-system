package org.example.pos_back_end.service.custom;

import org.example.pos_back_end.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(OrderDTO orderDTO);
    List<OrderDTO> getAllOrders();
    OrderDTO getOrder(String orderId);
    List<OrderDTO> getOrdersByCustomer(String customerId);
    void cancelOrder(String orderId);
}
