package com.kotofey.jwt_spring_boot.controller;

import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.service.ControllerService;
import com.kotofey.jwt_spring_boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ControllerService controllerService;
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("popa");
    }

    @PostMapping("/update")
    public ResponseEntity<AuthenticationResponse> update(
            @RequestBody UpdateRequest updateRequest,
            HttpServletRequest httpServletRequest
    ){
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
