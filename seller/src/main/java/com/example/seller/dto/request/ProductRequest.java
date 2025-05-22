package com.example.seller.dto.request;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest
{
    private String name;
    private String description;
    private Double wholesalePrice;
    private Double retailPrice;
    private Integer quantity;
}
