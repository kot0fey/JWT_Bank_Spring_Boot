package com.kotofey.jwt_spring_boot.repository;

import com.kotofey.jwt_spring_boot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByLogin(String login);

    @Query(value = """
            SELECT u FROM User u
            WHERE (:phone_number IS NULL OR u.phoneNumber = :phone_number)
            AND (:email IS NULL OR u.email = :email)
            AND (:first_name IS NULL OR u.firstName LIKE CONCAT(:first_name, '%'))
            AND (:last_name IS NULL OR u.lastName LIKE CONCAT(:last_name, '%'))
            AND (:middle_name IS NULL OR u.middleName LIKE CONCAT(:middle_name, '%'))
            AND (u.dateOfBirth > :dateOfBirth)
            """)
    Page<User> findByFilter(
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("phone_number") String phoneNumber,
            @Param("first_name") String firstName,
            @Param("last_name") String lastName,
            @Param("middle_name") String middleName,
            @Param("email") String email,
            Pageable pageable
    );

    @Transactional
    @Modifying
    @Query("""
            update User u
            set u.balance = (u.balance * 1.05)\s
            where u.balance < (u.deposit * 2.07)
            """)
    void scheduledBalanceIncrease();

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
