package com.kotofey.jwt_spring_boot.domain.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String login;
    private String phoneNumber;
    private String password;
    private Float deposit;
    private String lastName;
    private String firstName;
    private String middleName;
    private String dateOfBirth;
}
