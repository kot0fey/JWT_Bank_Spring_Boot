package com.kotofey.jwt_spring_boot.controller;

import com.kotofey.jwt_spring_boot.domain.request.SendMoneyRequest;
import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.domain.response.UserDto;
import com.kotofey.jwt_spring_boot.service.ControllerService;
import com.kotofey.jwt_spring_boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ControllerService controllerService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

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
    ) {
        logger.info("Request: /api/v1/user/filter");
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
        logger.info("Request: /api/v1/user/update");
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        return ResponseEntity.ok(userService.update(updateRequest, token));
    }

    @PostMapping("delete/phoneNumber")
    public ResponseEntity<AuthenticationResponse> deletePhoneNumber(
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        logger.info("Request: /api/v1/user/delete/phoneNumber");
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        return ResponseEntity.ok(userService.deletePhoneNumber(token));
    }

    @PostMapping("delete/email")
    public ResponseEntity<AuthenticationResponse> deleteEmail(
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        logger.info("Request: /api/v1/user/delete/email");
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        return ResponseEntity.ok(userService.deleteEmail(token));
    }

    @PostMapping("/sendMoney")
    public ResponseEntity<String> sendMoney(
            @RequestBody SendMoneyRequest request,
            HttpServletRequest httpServletRequest
    ) {
        logger.info("Request: /api/v1/user/sendMoney");
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        userService.sendMoney(token, request);
        return ResponseEntity.ok("OK");
    }
}
