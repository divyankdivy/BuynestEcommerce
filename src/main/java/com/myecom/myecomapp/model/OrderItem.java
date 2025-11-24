package com.myecom.myecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER) // eager loading for product is okay
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    private int quantity;

    private double price;
}
