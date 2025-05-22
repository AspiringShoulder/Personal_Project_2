package com.example.seller.controller;

import com.example.seller.dto.request.RegisterRequest;
import com.example.seller.dto.response.LoginResponse;
import com.example.seller.dto.request.LoginRequest;
import com.example.seller.dto.response.RegisterResponse;
import com.example.seller.exception.InvalidCredentialsException;
import com.example.seller.security.AuthUserDetails;
import com.example.seller.security.JWTProvider;
import com.example.seller.dto.response.ResponseStatus;
import com.example.seller.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("origins = http://localhost:4200")
public class LoginController
{
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTProvider jwtProvider;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager, JWTProvider jwtProvider)
    {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request)
    {
        Authentication authentication;

        try
        {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }

        catch (AuthenticationException e)
        {
            throw new InvalidCredentialsException();
        }

        AuthUserDetails authUserDetails = (AuthUserDetails) authentication.getPrincipal();
        String token = jwtProvider.createToken(authUserDetails);
        LoginResponse response = LoginResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Login successful.")
                                .build()
                )
                .token(token)
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request)
    {
        if (userService.getUserByUsername(request.getUsername()) || userService.getUserByPassword(request.getPassword()))
        {
            throw new InvalidCredentialsException();
        }

        userService.addUser(request.getUsername(), request.getPassword(), request.getEmail());
        RegisterResponse response = RegisterResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Creating account...")
                                .build()
                )
                .build();

        return ResponseEntity.status(200).body(response);
    }
}
