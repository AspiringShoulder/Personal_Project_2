package com.example.seller.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseStatus
{
    private boolean success;
    private String message;
}
