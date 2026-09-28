package com.example.spring_security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring_security_jwt.model.ERole;
import com.example.spring_security_jwt.model.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}