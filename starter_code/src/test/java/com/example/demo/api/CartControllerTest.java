package com.example.demo.api;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerTest {
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
    public void testAddToCart(){
        ModifyCartRequest requestObject = createModifyCartRequestObject();
        ResponseEntity<Cart> response = addItemToCart(requestObject);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertFalse(response.getBody().getItems().isEmpty());
    }

    @Test
    public void testRemoveFromCart(){
        ModifyCartRequest requestObject = createModifyCartRequestObject();
        addItemToCart(requestObject);
        ResponseEntity<Cart> response = testRestTemplate.exchange(
                TestConstant.URL.BASE + this.port + Constant.APIUri.CART_API + Constant.APIUri.CART_API_REMOVE_FROM_CART,
                HttpMethod.POST,
                new HttpEntity<>(requestObject, headers),
                Cart.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(response.getBody().getItems().isEmpty());
    }

    private ModifyCartRequest createModifyCartRequestObject() {
        ModifyCartRequest object = new ModifyCartRequest();
        object.setUsername(user.getUsername());
        object.setItemId((int) Math.floor((Math.random() * 2) + 1));
        object.setQuantity((int) Math.floor((Math.random() * 10)));
        return object;
    }

    private ResponseEntity<Cart> addItemToCart(ModifyCartRequest requestObject) {
         return
                testRestTemplate.exchange(
                        TestConstant.URL.BASE + this.port + Constant.APIUri.CART_API + Constant.APIUri.CART_API_ADD_TO_CART,
                        HttpMethod.POST,
                        new HttpEntity<>(requestObject, headers),
                        Cart.class);
    }
}
