package com.example.jwt_graphql.security;

import com.example.jwt_graphql.jwt.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class GraphQLSecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtFilter, RequestHeaderAuthenticationFilter.class)
                .csrf().disable()
                .cors()
                .and()
                .authorizeHttpRequests((authorization) ->
                        authorization
                                .requestMatchers("/graphql").permitAll()
                                .requestMatchers("/graphiql").permitAll()
                                .anyRequest().authenticated()
                                .and().authenticationProvider(authenticationProvider)
                )
                .httpBasic().disable()
                .sessionManagement().disable()
                .logout().disable();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer customCorsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
