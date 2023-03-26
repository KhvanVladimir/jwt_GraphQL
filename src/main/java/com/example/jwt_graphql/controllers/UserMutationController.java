package com.example.jwt_graphql.controllers;

import com.example.jwt_graphql.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserMutationController {
    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;

    @MutationMapping
    public String login(@Argument String username, @Argument String password) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(username, password);
        try {
            SecurityContextHolder.getContext().setAuthentication(authenticationProvider.authenticate(credentials));
            return userService.login();
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException(username);
        }
    }
}
