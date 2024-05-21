package com.kotofey.jwt_spring_boot.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class ControllerService {
    public void setAccessTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("access_token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public String getTokenFromAuthorizedRequest(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String token = null;
        //todo test without cookies
        for (Cookie cookie: cookies){
            if(cookie.getName().equals("access_token")){
                token = cookie.getValue();
                break;
            }
        }
        return token;
    }
}
