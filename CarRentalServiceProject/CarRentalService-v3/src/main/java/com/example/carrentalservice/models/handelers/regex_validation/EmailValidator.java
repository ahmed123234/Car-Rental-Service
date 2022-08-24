package com.example.carrentalservice.models.handelers.regex_validation;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {
    private static final  String PATTERN ="^[_A-Za-z\\d-+]+(\\.[_A-Za-z\\d-]+)*@[A-Za-z\\d-]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$";

    private final Pattern pattern = Pattern.compile(PATTERN);

    @Override
    public boolean test(String email) {

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}