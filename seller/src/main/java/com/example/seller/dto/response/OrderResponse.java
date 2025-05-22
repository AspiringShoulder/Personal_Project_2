package com.example.seller.dto.response;

import com.example.seller.domain.Order;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse
{
    private ResponseStatus status;
    private Order order;
}
