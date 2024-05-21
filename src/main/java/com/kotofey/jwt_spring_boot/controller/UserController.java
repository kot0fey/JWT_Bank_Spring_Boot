package com.kotofey.jwt_spring_boot.controller;

import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import com.kotofey.jwt_spring_boot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("popa");
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(
            @RequestBody UpdateRequest request
    ){
        return ResponseEntity.ok(
                userService.update(request)
        );
    }
}
