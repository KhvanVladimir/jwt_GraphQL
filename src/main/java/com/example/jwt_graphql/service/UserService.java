package com.example.jwt_graphql.service;

import com.example.jwt_graphql.entity.User;
import com.example.jwt_graphql.jwt.JWTUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String userName);
    String login();
    List<User> getAllUsers();
    JWTUserDetails loadUserByToken(String token);
}
