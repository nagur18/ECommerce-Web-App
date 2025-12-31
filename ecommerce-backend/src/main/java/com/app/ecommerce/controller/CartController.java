package com.app.ecommerce.controller;

import org.springframework.web.bind.annotation.*;
import com.app.ecommerce.entity.CartItem;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.security.JwtUtil;
import com.app.ecommerce.service.CartService;
import com.app.ecommerce.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // ✅ Constructor for Dependency Injection
    public CartController(CartService cartService, UserService userService, JwtUtil jwtUtil) {
        this.cartService = cartService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ===== ADD TO CART =====
    @PostMapping
    public CartItem addToCart(
            @RequestParam Long productId, 
            @RequestParam int qty,
            @RequestHeader("Authorization") String authHeader) {
        
        User user = getCurrentUser(authHeader);
        return cartService.addToCart(user, productId, qty);
    }

    // ===== GET USER CART =====
    @GetMapping
    public List<CartItem> getUserCart(@RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        return cartService.getUserCart(user);
    }

    // ===== REMOVE ITEM FROM CART =====
    @DeleteMapping("/{id}")
    public String removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return "Item removed from cart";
    }

    // ===== HELPER: Extract Current User from JWT =====
    private User getCurrentUser(String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractEmail(token);
        return userService.findByEmail(email);
    }
}