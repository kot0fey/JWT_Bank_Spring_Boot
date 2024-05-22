package com.kotofey.jwt_spring_boot.domain.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
