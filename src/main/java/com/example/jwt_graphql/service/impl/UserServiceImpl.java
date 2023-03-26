package com.example.jwt_graphql.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwt_graphql.entity.Role;
import com.example.jwt_graphql.entity.User;
import com.example.jwt_graphql.jwt.JWTUserDetails;
import com.example.jwt_graphql.repository.RoleRepository;
import com.example.jwt_graphql.repository.UserRepository;
import com.example.jwt_graphql.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final RoleRepository roleRepository;
    @Value("${jwt.issuer}")
    private String JWT_ISSUER;
    @Override
    public String login() {
        User user = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMap(userRepository::findUserByUsername)
                .orElseThrow(EntityNotFoundException :: new);
        return generateAccessToken(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public JWTUserDetails loadUserByToken(String accessToken) {
        return getDecodedToken(accessToken)
                .map(DecodedJWT::getSubject)
                .flatMap(userRepository::findUserByUsername)
                .map(user -> getUserDetails(user, accessToken))
                .orElse(null);
    }
    private Optional<DecodedJWT> getDecodedToken(String token)  {
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(user -> getUserDetails(user, generateAccessToken(user)))
                .orElseThrow(EntityNotFoundException::new);
    }
    private JWTUserDetails getUserDetails(User user, String accessToken) {
        return JWTUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Optional.ofNullable(user.getRoles())
                        .orElse(new HashSet<>())
                        .stream()
                        .map(Role::getAuthority)
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .accessToken(accessToken)
                .build();
    }
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(Period.ofDays(1));
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withIssuedAt(java.sql.Date.from(now))
                .withExpiresAt(Date.from(expiry))
                .withSubject(user.getUsername())
                .sign(algorithm);
    }
}
