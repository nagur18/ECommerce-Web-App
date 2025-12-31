package com.app.ecommerce.controller;

import org.springframework.web.bind.annotation.*;
import com.app.ecommerce.entity.Product;
import com.app.ecommerce.service.ProductService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    // ✅ Constructor-based Dependency Injection (Recommended)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ===== USER ENDPOINTS =====
    
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    
    // ===== ADMIN ENDPOINTS =====
    
    @PostMapping("/admin/products")
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    
    @PutMapping("/admin/products/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
    
    @DeleteMapping("/admin/products/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}