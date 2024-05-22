package com.kotofey.jwt_spring_boot;

import com.kotofey.jwt_spring_boot.model.User;
import com.kotofey.jwt_spring_boot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class JwtSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtSpringBootApplication.class, args);
    }

}
