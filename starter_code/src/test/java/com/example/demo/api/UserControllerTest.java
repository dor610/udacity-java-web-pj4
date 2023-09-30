package com.example.demo.api;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.util.Common;
import com.example.demo.util.Constant;
import com.example.demo.util.TestConstant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate;

    @Autowired
    public void setTestRestTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }
    @Test
    public void testCreateUser() {
        CreateUserRequest createUserObject = Common.createUserRequestObject();
        ResponseEntity<User> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.USER_API + Constant.APIUri.USER_API_CREATE,
                        HttpMethod.POST,
                        new HttpEntity<>(createUserObject),
                        User.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(createUserObject.getUsername(), Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    public void testLogin() {
        User user = Common.createUser(testRestTemplate, port);
        Map<String, String> input = new HashMap<>();
        input.put("username", user.getUsername());
        input.put("password", TestConstant.User.PASSWORD);
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.Security.LOGIN_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(input),
                        Object.class);

        String token = Objects.requireNonNull(response.getHeaders().get("Authorization")).get(0);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertTrue(token.contains(Constant.Security.TOKEN_PREFIX));
    }

    @Test
    public void testFindUserById() {
        User user = Common.createUser(testRestTemplate, port);
        String token = Common.login(user.getUsername(), testRestTemplate, port);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", token);
        ResponseEntity<User> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.USER_API + "/id/" + user.getId(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        User.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user.getUsername(), Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    public void testFindUserByIdNotFoundCase() {
        User user = Common.createUser(testRestTemplate, port);
        String token = Common.login(user.getUsername(), testRestTemplate, port);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", token);
        ResponseEntity<User> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.USER_API + "/id/10000",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        User.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindUserByUserName() {
        User user = Common.createUser(testRestTemplate, port);
        String token = Common.login(user.getUsername(), testRestTemplate, port);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", token);
        ResponseEntity<User> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.USER_API + "/" + user.getUsername(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        User.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(user.getUsername(), Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    public void testFindUserByUserNameNotFoundCase() {
        User user = Common.createUser(testRestTemplate, port);
        String token = Common.login(user.getUsername(), testRestTemplate, port);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", token);
        ResponseEntity<User> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.USER_API + "/userName",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        User.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
