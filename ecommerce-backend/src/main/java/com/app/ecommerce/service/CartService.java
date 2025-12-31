package com.app.ecommerce.service;

import com.app.ecommerce.entity.CartItem;
import com.app.ecommerce.entity.Product;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.repository.CartItemRepository;
import com.app.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartRepo;
    private final ProductRepository productRepo;

    public CartService(CartItemRepository cartRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    public List<CartItem> getUserCart(User user) {
        return cartRepo.findByUser(user);
    }

    public CartItem addToCart(User user, Long productId, int qty) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(qty);

        return cartRepo.save(item);
    }

    public void removeItem(Long id) {
        cartRepo.deleteById(id);
    }
}
