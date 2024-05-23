package com.kotofey.jwt_spring_boot.service;

import com.kotofey.jwt_spring_boot.config.JwtService;
import com.kotofey.jwt_spring_boot.domain.request.SendMoneyRequest;
import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.mapping.UserMapper;
import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.domain.response.UserDto;
import com.kotofey.jwt_spring_boot.repository.UserRepository;
import com.kotofey.jwt_spring_boot.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.List;


@Service
@RequiredArgsConstructor
@EnableScheduling
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final DateUtil dateUtil;
    private final UserMapper userMapper;

    @Scheduled(fixedRate = 1000 * 60)
    public void scheduledBalanceIncrease() {
        userRepository.scheduledBalanceIncrease();
    }

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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public Page<UserDto> getUsersWithFilter(
            String dateOfBirth,
            String phoneNumber,
            String firstName,
            String lastName,
            String middleName,
            String email,
            Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("id"));
        Page<User> userPage = userRepository.findByFilter(
                dateUtil.stringToLocalDate(dateOfBirth),
                phoneNumber,
                firstName,
                lastName,
                middleName,
                email,
                pageable
        );
        return userPage.map(userMapper::mapToDto);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sendMoney(String token, SendMoneyRequest request) {
        User sender = userRepository.findByUsername(
                jwtService.getUsername(token)
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userRepository.findByUsername(
                request.getUsername()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));
        if (sender.equals(receiver)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender and receiver are equal");
        }
        if (sender.getBalance() < request.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender's balance doesn't contains enough money");
        }
        sender.setBalance(
                sender.getBalance() - request.getAmount()
        );
        receiver.setBalance(
                receiver.getBalance() + request.getAmount()
        );
        userRepository.saveAll(
                List.of(sender, receiver)
        );
    }
}
