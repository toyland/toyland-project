package com.toyland.user.model.repository;

import com.toyland.user.infrastructure.JpaUserRepositoryCustom;
import com.toyland.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaUserRepositoryCustom {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
