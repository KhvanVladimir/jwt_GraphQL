package com.example.jwt_graphql.controllers;

import com.example.jwt_graphql.entity.User;
import com.example.jwt_graphql.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserQueryController {
    private final UserService userService;

    @QueryMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
