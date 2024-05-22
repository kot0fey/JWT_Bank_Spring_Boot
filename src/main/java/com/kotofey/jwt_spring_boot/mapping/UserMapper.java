package com.kotofey.jwt_spring_boot.mapping;

import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.domain.response.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto mapToDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .deposit(user.getDeposit())
                .balance(user.getBalance())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
}
