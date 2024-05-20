package com.kotofey.jwt_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@SpringBootApplication
public class JwtSpringBootApplication {

    public static void main(String[] args){
        SpringApplication.run(JwtSpringBootApplication.class, args);
    }

}
