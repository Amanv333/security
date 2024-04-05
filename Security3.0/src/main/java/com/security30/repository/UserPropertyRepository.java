package com.security30.repository;

import com.security30.model.UserProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPropertyRepository extends JpaRepository<UserProperty, Long> {


    Optional<UserProperty> findByUsername(String username);

    Optional<UserProperty> findByEmail(String email);
}