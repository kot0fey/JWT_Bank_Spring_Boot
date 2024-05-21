package com.kotofey.jwt_spring_boot.auth;

import com.kotofey.jwt_spring_boot.domain.request.AuthenticationRequest;
import com.kotofey.jwt_spring_boot.domain.request.RegisterRequest;
import com.kotofey.jwt_spring_boot.domain.response.AuthenticationResponse;
import com.kotofey.jwt_spring_boot.service.AuthenticationService;
import com.kotofey.jwt_spring_boot.service.ControllerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ControllerService controllerService;
    @PostMapping("/register")
    public ResponseEntity register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) throws BadRequestException {
        controllerService.setAccessTokenCookie(
                response,
                authenticationService.register(request)
        );
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @PostMapping("/authenticate")
    public ResponseEntity authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ){
        controllerService.setAccessTokenCookie(
                response,
                authenticationService.authenticate(request)
        );
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
