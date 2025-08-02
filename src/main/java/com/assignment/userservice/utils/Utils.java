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

    public static DateRange getBirthDateRangeForAge(int age, LocalDate baseDate) {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 0보다 작을 수 없습니다.");
        }

        // 만 나이 기준으로 계산
        // 예: 현재 2024-01-01, 나이 20살
        // 시작일: 2003-01-02 (만 나이 20살인 사람 중 가장 빠른 생일)
        // 종료일: 2004-01-01 (만 나이 20살인 사람 중 가장 늦은 생일)

        LocalDate endDate = baseDate.minusYears(age);
        LocalDate startDate = endDate.minusYears(age + 1).plusDays(1);

        return new DateRange(startDate, endDate);
    }

}
