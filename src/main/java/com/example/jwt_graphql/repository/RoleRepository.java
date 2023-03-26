package com.example.jwt_graphql.repository;

import com.example.jwt_graphql.entity.Role;
import com.example.jwt_graphql.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(RoleName roleName);
}
