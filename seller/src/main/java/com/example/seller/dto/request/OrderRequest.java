package com.example.seller.dto.request;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest
{
    private OrderDTO[] order;
}
