package com.kotofey.jwt_spring_boot.service;

import com.kotofey.jwt_spring_boot.config.JwtService;
import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public AuthenticationResponse update(UpdateRequest request, String token) {
        User user = getUserByToken(token);
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        String newToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(newToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse deletePhoneNumber(String token) throws BadRequestException {
        User user = getUserByToken(token);
        if (user.getEmail().isEmpty()) {
            throw new BadRequestException("No email for user");
        }
        user.setPhoneNumber("");
        userRepository.save(user);
        String newToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(newToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse deleteEmail(String token) throws BadRequestException {
        User user = getUserByToken(token);
        if (user.getPhoneNumber().isEmpty()) {
            throw new BadRequestException("No phone number for user");
        }
        user.setEmail("");
        userRepository.save(user);
        String newToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(newToken)
                .build();
    }

    private User getUserByToken(String token) {
        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }


}
