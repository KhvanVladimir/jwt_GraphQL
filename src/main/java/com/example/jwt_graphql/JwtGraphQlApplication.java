package com.example.jwt_graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtGraphQlApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtGraphQlApplication.class, args);
    }

}
