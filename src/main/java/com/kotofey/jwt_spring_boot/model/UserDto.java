package com.kotofey.jwt_spring_boot.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String login;
    private String email;
    private String phoneNumber;
    private Float deposit;
    private Float balance;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate dateOfBirth;
}
