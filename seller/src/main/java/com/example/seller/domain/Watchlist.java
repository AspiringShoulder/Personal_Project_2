package com.example.seller.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Watchlists")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watchlist_id")
    private Integer watchlistId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "in_stock", nullable = false)
    private Boolean inStock;
}
