package org.example.pos_back_end.controller;

import lombok.RequiredArgsConstructor;
import org.example.pos_back_end.dto.OrderDTO;
import org.example.pos_back_end.dto.OrderDetailDTO;
import org.example.pos_back_end.service.custom.OrderService;
import org.example.pos_back_end.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<APIResponse<OrderDTO>> placeOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO savedOrder = orderService.placeOrder(orderDTO); // get DTO with orderId
            return new ResponseEntity<>(
                    new APIResponse<>(201, "Order Placed Successfully", savedOrder),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new APIResponse<>(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<OrderDTO>>> getAllOrders() {
        try {
            List<OrderDTO> orders = orderService.getAllOrders();
            return new ResponseEntity<>(
                    new APIResponse<>(200, "All Orders Retrieved", orders),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(500, "Error retrieving orders: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<APIResponse<OrderDTO>> getOrder(@PathVariable String orderId) {
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return new ResponseEntity<>(
                    new APIResponse<>(200, "Order Found", order),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(404, e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(500, "Error retrieving order: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<APIResponse<List<OrderDTO>>> getOrdersByCustomer(@PathVariable String customerId) {
        try {
            List<OrderDTO> orders = orderService.getOrdersByCustomer(customerId);
            return new ResponseEntity<>(
                    new APIResponse<>(200, "Orders Retrieved for Customer", orders),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(500, "Error retrieving orders: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<APIResponse<String>> cancelOrder(@PathVariable String orderId) {
        try {
            orderService.cancelOrder(orderId);
            return new ResponseEntity<>(
                    new APIResponse<>(200, "Order Cancelled Successfully", null),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new APIResponse<>(404, e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(500, "Error cancelling order: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}