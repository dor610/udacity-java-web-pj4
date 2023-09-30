package com.example.demo.util;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Common {
    public static CreateUserRequest createUserRequestObject() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(TestConstant.User.USERNAME + System.currentTimeMillis());
        createUserRequest.setPassword(TestConstant.User.PASSWORD);
        createUserRequest.setConfPassword(TestConstant.User.PASSWORD);
        return createUserRequest;
    }

    public static  User createUser(TestRestTemplate testRestTemplate, int port) {
        ResponseEntity<User> response = testRestTemplate.exchange(
                TestConstant.URL.BASE + port + Constant.APIUri.USER_API + Constant.APIUri.USER_API_CREATE,
                HttpMethod.POST,
                new HttpEntity<>(Common.createUserRequestObject()),
                User.class);
        return response.getBody();
    }

    public static String login(String userName, TestRestTemplate testRestTemplate, int port) {
        Map<String, String> input = new HashMap<>();
        input.put("username", userName);
        input.put("password", TestConstant.User.PASSWORD);
        ResponseEntity<Object> response = testRestTemplate.exchange(
                TestConstant.URL.BASE + port + Constant.Security.LOGIN_URL,
                HttpMethod.POST,
                new HttpEntity<>(input),
                Object.class);
        String token = Objects.requireNonNull(response.getHeaders().get("Authorization")).get(0);
        return token != null? token : "";
    }
}
