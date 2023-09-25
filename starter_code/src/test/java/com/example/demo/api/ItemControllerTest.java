package com.example.demo.api;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
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
public class ItemControllerTest {
    @LocalServerPort
    private int port;

    private TestRestTemplate testRestTemplate;
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

    @Autowired
    public void setTestRestTemplate(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    @Test
    public void testGetItem() {
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ITEM_API,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody() instanceof List);
        Assert.assertFalse(((List<?>) response.getBody()).isEmpty());
    }

    @Test
    public void testGetItemByIdNotFoundCase() {
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ITEM_API + "/" + 100,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Object.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetItemById() {
        ResponseEntity<Item> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ITEM_API + "/" + 1,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Item.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals("Round Widget", response.getBody().getName());
    }

    @Test
    public void testGetItemsByNameNotFoundCase() {
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ITEM_API + "/name/a_name",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Object.class);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetItemsByName() {
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.ITEM_API + "/name/Round Widget",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody() instanceof List);
        Assert.assertFalse(((List<?>) response.getBody()).isEmpty());
    }
}
