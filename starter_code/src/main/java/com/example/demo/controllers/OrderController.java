package com.example.demo.controllers;

import java.util.List;

import com.example.demo.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping(Constant.APIUri.ORDER_API)
public class OrderController {

	Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping(Constant.APIUri.ORDER_API_SUBMIT)
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("submit: User not found.");
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		logger.info("submit: The order has been created successfully.");
		return ResponseEntity.ok(order);
	}
	
	@GetMapping(Constant.APIUri.ORDER_API_GET_ORDER_FOR_USER)
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("getOrdersForUser: User not found.");
			return ResponseEntity.notFound().build();
		}
		logger.info("getOrdersForUser: Retrieved orders successfully.");
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
