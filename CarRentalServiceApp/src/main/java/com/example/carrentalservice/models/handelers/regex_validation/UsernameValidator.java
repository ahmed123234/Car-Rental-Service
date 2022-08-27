package com.example.carrentalservice.models.handelers.regex_validation;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class UsernameValidator implements Predicate<String> {

    private static final  String PATTERN = "^[a-zA-Z\\d_-]{8,15}$";

    private final Pattern pattern = Pattern.compile(PATTERN);

    @Override
    public boolean test(String userName) {

        Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }
}
