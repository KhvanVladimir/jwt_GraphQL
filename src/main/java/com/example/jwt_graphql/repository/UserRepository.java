package com.example.jwt_graphql.repository;

import com.example.jwt_graphql.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    @EntityGraph(value = "user.roles")
    Optional<User> findUserByUsername(String username);

    @EntityGraph(value = "user.roles")
    @Query(value = "SELECT user FROM User user JOIN user.roles role WHERE role.roleName = :roleName")
    Page<User> finsUsersByRole(String roleName, Pageable pageable);

    @EntityGraph(value = "user.roles")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(value = "user.roles")
    List<User> findAll();
}
