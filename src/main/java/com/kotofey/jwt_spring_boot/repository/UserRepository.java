package com.kotofey.jwt_spring_boot.repository;

import com.kotofey.jwt_spring_boot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByLogin(String login);

    default Optional<User> findByUsername(String username) {
        Optional<User> user = findByEmail(username);
        if (user.isPresent()) {
            return user;
        }
        user = findByPhoneNumber(username);
        if (user.isPresent()) {
            return user;
        }
        user = findByLogin(username);
        return user;
    }
}