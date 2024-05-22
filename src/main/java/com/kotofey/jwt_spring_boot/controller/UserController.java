package com.kotofey.jwt_spring_boot.controller;

import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.model.UserDto;
import com.kotofey.jwt_spring_boot.repository.UserRepository;
import com.kotofey.jwt_spring_boot.service.ControllerService;
import com.kotofey.jwt_spring_boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ControllerService controllerService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("popa");
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<UserDto>> filter(
            @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "middleName", required = false) String middleName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset
    ) throws ParseException {
       return ResponseEntity.ok(
               userService.getUsersWithFilter(
                       dateOfBirth,
                       phoneNumber,
                       firstName,
                       lastName,
                       middleName,
                       email,
                       limit,
                       offset)
       );
    }

    @PostMapping("/update")
    public ResponseEntity<AuthenticationResponse> update(
            @RequestBody UpdateRequest updateRequest,
            HttpServletRequest httpServletRequest
    ) {
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        return ResponseEntity.ok(userService.update(updateRequest, token));
    }

    @PostMapping("delete/phoneNumber")
    public ResponseEntity<AuthenticationResponse> deletePhoneNumber(
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        return ResponseEntity.ok(userService.deletePhoneNumber(token));
    }

    @PostMapping("delete/email")
    public ResponseEntity<AuthenticationResponse> deleteEmail(
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        return ResponseEntity.ok(userService.deleteEmail(token));
    }
}
