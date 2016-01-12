package com.nixsolutions.ponarin.utils;

import java.util.Arrays;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class UserFormUtils {

    public MultiValueMap<String, String> createParamsUserForm() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.put("login", Arrays.asList("Login"));
        map.put("password", Arrays.asList("Q!q1qq"));
        map.put("matchingPassword", Arrays.asList("Q!q1qq"));
        map.put("firstName", Arrays.asList("FirstName"));
        map.put("lastName", Arrays.asList("LastName"));
        map.put("email", Arrays.asList("test@mail.ru"));
        map.put("birthDay", Arrays.asList("25-12-1990"));
        map.put("role", Arrays.asList("User"));

        return map;
    }
}
