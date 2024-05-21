package com.kotofey.jwt_spring_boot.service;

import com.kotofey.jwt_spring_boot.config.JwtService;
import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public void update(UpdateRequest request, String token) {
        String username = jwtService.getUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
    }
}
