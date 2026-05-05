package com.nhpdev.backendservice.repository;

import com.nhpdev.backendservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
    Optional<User> findUsersByEmail(String email);
}
