package com.app.ecommerce.controller;

import org.springframework.web.bind.annotation.*;
import com.app.ecommerce.entity.Order;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.repository.OrderRepository;
import com.app.ecommerce.security.JwtUtil;
import com.app.ecommerce.service.OrderService;
import com.app.ecommerce.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // ✅ Constructor for Dependency Injection
    public OrderController(OrderService orderService, OrderRepository orderRepository, 
                          UserService userService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ===== PLACE ORDER (Checkout) =====
    @PostMapping
    public Order checkout(@RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        return orderService.placeOrder(user);
    }

    // ===== GET USER'S ORDER HISTORY =====
    @GetMapping("/my")
    public List<Order> getMyOrders(@RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        return orderRepository.findByUser(user);
    }

    // ===== HELPER: Extract Current User from JWT =====
    private User getCurrentUser(String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractEmail(token);
        return userService.findByEmail(email);
    }
}