package com.example.seller.dto.request;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO
{
    private int productId;
    private int quantity;
}
