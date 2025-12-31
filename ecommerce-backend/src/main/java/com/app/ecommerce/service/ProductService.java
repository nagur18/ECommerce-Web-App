package com.app.ecommerce.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.app.ecommerce.entity.Product;
import com.app.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    // ADMIN
    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    public Product updateProduct(Long id, Product newData) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setTitle(newData.getTitle());
        p.setDescription(newData.getDescription());
        p.setPrice(newData.getPrice());
        p.setCategory(newData.getCategory());
        p.setStock(newData.getStock());
        p.setImageUrl(newData.getImageUrl());

        return productRepo.save(p);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    // USER
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
