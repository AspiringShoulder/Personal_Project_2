package com.example.seller.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Products")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "wholesale_price", nullable = false)
    private double wholesalePrice;

    @Column(name = "retail_price", nullable = false)
    private double retailPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
