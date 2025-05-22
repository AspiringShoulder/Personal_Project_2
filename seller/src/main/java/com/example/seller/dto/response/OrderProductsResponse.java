package com.example.seller.dto.response;

import com.example.seller.domain.OrderProduct;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderProductsResponse
{
    private ResponseStatus status;
    private List<OrderProduct> products;
}
