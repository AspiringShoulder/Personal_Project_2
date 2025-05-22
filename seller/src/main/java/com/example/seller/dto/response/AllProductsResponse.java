package com.example.seller.dto.response;

import com.example.seller.domain.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AllProductsResponse
{
    private ResponseStatus status;
    private List<Product> products;
}
