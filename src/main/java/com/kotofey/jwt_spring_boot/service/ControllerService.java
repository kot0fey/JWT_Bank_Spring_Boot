package com.kotofey.jwt_spring_boot.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class ControllerService {

    public String getTokenFromAuthorizedRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if ((authHeader == null) || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7); //7 -> "Bearer "
    }
}
