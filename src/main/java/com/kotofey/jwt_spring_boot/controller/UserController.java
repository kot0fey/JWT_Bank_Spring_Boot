package com.kotofey.jwt_spring_boot.controller;

import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
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
    public ResponseEntity update(
            @RequestBody UpdateRequest updateRequest,
            HttpServletRequest httpServletRequest
    ){
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        userService.update(updateRequest, token);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("delete/phoneNumber")
    public ResponseEntity deletePhoneNumber(
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        userService.deletePhoneNumber(token);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("delete/email")
    public ResponseEntity deleteEmail(
            HttpServletRequest httpServletRequest
    ) throws BadRequestException {
        String token = controllerService.getTokenFromAuthorizedRequest(httpServletRequest);
        userService.deleteEmail(token);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
