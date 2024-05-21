package com.kotofey.jwt_spring_boot.service;

import com.kotofey.jwt_spring_boot.config.JwtService;
import com.kotofey.jwt_spring_boot.domain.request.AuthenticationRequest;
import com.kotofey.jwt_spring_boot.domain.request.RegisterRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public String register(RegisterRequest request) throws BadRequestException {
        if (
                (
                        request.getPhoneNumber().isEmpty() &&
                        request.getEmail().isEmpty()
                ) ||
                request.getPassword().isEmpty() ||
                request.getDeposit() < 0F ||
                request.getDateOfBirth().isEmpty() ||
                request.getFirstName().isEmpty() ||
                request.getLastName().isEmpty() ||
                request.getMiddleName().isEmpty()
        ) {
            throw new BadRequestException("Bad request");
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
            User user = User.builder()
                    .login(request.getLogin())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .deposit(request.getDeposit())
                    .balance(request.getDeposit())
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .dateOfBirth(sf.parse(request.getDateOfBirth()))
                    .build();
            userRepository.save(user);
            Map<String, Object> extraClaims = jwtService.generateExtraClaims(user);
            String jwtToken = jwtService.generateToken(extraClaims, user);
            return jwtToken;
        } catch (ParseException e) {
            throw new BadRequestException("Wrong date of birth format");
        }
    }

    public String authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Map<String, Object> extraClaims = jwtService.generateExtraClaims(user);
        String jwtToken = jwtService.generateToken(extraClaims, user);
        return jwtToken;
    }
}
