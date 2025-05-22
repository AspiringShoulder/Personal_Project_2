package com.example.seller.dto.response;

import com.example.seller.domain.Product;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse
{
    private ResponseStatus status;
    private Product product;
}
