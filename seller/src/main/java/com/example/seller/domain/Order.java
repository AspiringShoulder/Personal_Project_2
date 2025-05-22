package com.example.seller.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "time_created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated;
}
