package com.example.seller.dto.response;

import com.example.seller.domain.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class AllOrdersResponse
{
    private ResponseStatus status;
    private List<Order> orders;
}
