package com.example.seller.dto.request;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest
{
    private String username;
    private String password;
}
