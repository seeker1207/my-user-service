package com.assignment.userservice.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    static public LocalDate getBirthDateFromCitizenNumber(String citizenNumber) {
        if (citizenNumber == null) return null;

        String birthDate = citizenNumber.substring(0, 6);
        char genderCode = citizenNumber.charAt(6);

        String centuryPrefix = switch (genderCode) {
            case '1', '2', '5', '6' -> "19";
            case '3', '4', '7', '8' -> "20";
            default -> throw new IllegalArgumentException("유효하지 않은 주민등록번호 입니다.");
        };

        String fullBirthDate = centuryPrefix + birthDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(fullBirthDate, formatter);
    }
}
