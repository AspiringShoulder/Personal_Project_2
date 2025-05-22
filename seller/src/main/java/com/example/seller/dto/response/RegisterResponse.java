package com.example.seller.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponse
{
    private ResponseStatus status;
}
