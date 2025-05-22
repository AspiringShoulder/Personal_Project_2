package com.example.seller.dto.request;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest
{
    private String username;
    private String password;
    private String email;
}
