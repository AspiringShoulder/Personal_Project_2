package com.example.seller.service;

import com.example.seller.dao.UserDAO;
import com.example.seller.domain.User;
import com.example.seller.exception.InvalidCredentialsException;
import com.example.seller.security.AuthUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO)
    {
        LOGGER.info("userService created");
        this.userDAO = userDAO;
    }

    public void addUser(String username, String password, String email)
    {
        User newUser = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .admin(false)
                .build();

        userDAO.addUser(newUser);
    }

    public boolean getUserByUsername(String username)
    {
        return userDAO.getUserByUsername(username).isPresent();
    }

    public boolean getUserByPassword(String password)
    {
        return userDAO.getUserByPassword(password).isPresent();
    }

    private List<GrantedAuthority> getAuthoritiesFromUser(User user)
    {
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        if (user.getAdmin())
        {
            userAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        else
        {
            userAuthorities.add(new SimpleGrantedAuthority("nonAdmin"));
        }

        return userAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws InvalidCredentialsException
    {
        Optional<User> userOptional = userDAO.getUserByUsername(username);

        if (!userOptional.isPresent())
        {
            throw new InvalidCredentialsException();
        }

        User user = userOptional.get(); // database user

        return AuthUserDetails.builder()
                .user(user)// spring security's userDetail
                .username(user.getUsername())
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .authorities(getAuthoritiesFromUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }
}
