package com.example.demo.api;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.util.Common;
import com.example.demo.util.Constant;
import com.example.demo.util.TestConstant;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {
    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate;

    @Autowired
    public void setTestRestTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    private User user;
    private String token;
    private HttpHeaders headers;

    @Before
    public void prepareTestData() {
        this.user = Common.createUser(testRestTemplate, port);
        this.token = Common.login(user.getUsername(), testRestTemplate, port);
        this.headers = new HttpHeaders();
        this.headers.add("Content-Type", "application/json");
        this.headers.add("Authorization", token);
    }

    @After
    public void clearTestData() {
        this.user = null;
        this.token = null;
        this.headers = null;
    }


    @Test
    public void testSubmit() {
        ResponseEntity<UserOrder> response = createOrder(user.getUsername());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void testSubmitNotFound() {
        ResponseEntity<UserOrder> response = createOrder("userName");
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrdersForUser() {
        createOrder(user.getUsername());
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ORDER_API + "/history/" + user.getUsername(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody() instanceof List);
        Assert.assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    public void testGetOrdersForUserNotFound() {
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ORDER_API + "/history/" + user.getUsername(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody() instanceof List);
        Assert.assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    private ResponseEntity<UserOrder> createOrder(String userName) {
         return testRestTemplate.exchange(
            TestConstant.URL.BASE + this.port + Constant.APIUri.ORDER_API + "/submit/" + userName,
            HttpMethod.POST,
            new HttpEntity<>(headers),
            UserOrder.class);
    }
}
