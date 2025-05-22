package com.example.seller.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Order_Products")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Integer orderProductId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order orderId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;

    @Column(name = "retail_price_at_time_of_purchase", nullable = false)
    private Double retailPriceAtTimeOfPurchase;

    @Column(name = "wholesale_price_at_time_of_purchase", nullable = false)
    private Double wholesalePriceAtTimeOfPurchase;

    @Column(name = "quantity", nullable = false)
    private Integer orderQuantity;

    @Column(name = "profit", nullable = false, insertable = false, updatable = false)
    private Double profit;
}
