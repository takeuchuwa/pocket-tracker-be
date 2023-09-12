package com.pockettracker.auth.user.repository;

import com.pockettracker.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserCredentialsEmail(String email);

}
