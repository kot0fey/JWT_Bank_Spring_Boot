package com.kotofey.jwt_spring_boot.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DateUtil {
    public LocalDate stringToLocalDate(String string) throws ParseException {
        if (string == null){
            return LocalDate.of(-4712, 1, 1);
        }
        try {
            //"dd.MM.yyyy"
            List<Integer> dateList = Arrays.stream(string.split("\\."))
                    .map(s -> Integer.parseInt(s))
                    .toList();
            return LocalDate.of(
                    dateList.get(2),
                    dateList.get(1),
                    dateList.get(0)
            );
        }catch (Exception e){
            throw new ParseException("Invalid date format", 18);
        }
    }
}
