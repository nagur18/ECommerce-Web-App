package com.app.ecommerce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private double totalAmount;

    private String orderStatus;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    
    public Order() {}
    public Order(User user, double totalAmount, String orderStatus) {
		super();
		this.user = user;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
	}
    
    

	public Order(Long id, User user, double totalAmount, String orderStatus, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.user = user;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
	}



	// ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
