package com.pockettracker.user.repository;

import com.pockettracker.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserCredentialsEmail(String email);

}
