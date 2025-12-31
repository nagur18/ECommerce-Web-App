package com.app.ecommerce.service;

import com.app.ecommerce.entity.*;
import com.app.ecommerce.repository.CartItemRepository;
import com.app.ecommerce.repository.OrderItemRepository;
import com.app.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartItemRepository cartRepo;

    public OrderService(OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo,
                        CartItemRepository cartRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.cartRepo = cartRepo;
    }

    public Order placeOrder(User user) {
        List<CartItem> cartItems = cartRepo.findByUser(user);

        double total = 0;
        for (CartItem c : cartItems) {
            total += c.getProduct().getPrice() * c.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setOrderStatus("PENDING");

        Order savedOrder = orderRepo.save(order);

        for (CartItem c : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(savedOrder);
            oi.setProduct(c.getProduct());
            oi.setQuantity(c.getQuantity());
            oi.setPrice(c.getProduct().getPrice());
            orderItemRepo.save(oi);
        }

        cartRepo.deleteAll(cartItems);
        return savedOrder;
    }

    public List<Order> myOrders(User user) {
        return orderRepo.findByUser(user);
    }
}

