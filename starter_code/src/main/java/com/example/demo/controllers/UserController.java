package com.example.demo.controllers;


import com.example.demo.service.UserService;
import com.example.demo.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;

import javax.validation.Valid;

@RestController
@RequestMapping(Constant.APIUri.USER_API)
public class UserController {

	Logger logger = LogManager.getLogger(this.getClass());

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(Constant.APIUri.USER_API_FIND_BY_ID)
	public ResponseEntity<User> findById(@PathVariable Long id) {
		User user = userService.findById(id);
		if (user == null){
			logger.error("findById: User not found.");
			return ResponseEntity.notFound().build();
		} else {
			logger.error("findById: Retrieved user data successfully.");
			return ResponseEntity.ok(user);
		}
	}
	
	@GetMapping(Constant.APIUri.USER_API_FIND_BY_USERNAME)
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userService.findByUserName(username);
		if (user == null){
			logger.error("findByUserName: User not found.");
			return ResponseEntity.notFound().build();
		} else {
			logger.error("findByUserName: Retrieved user data successfully.");
			return ResponseEntity.ok(user);
		}
	}
	
	@PostMapping(Constant.APIUri.USER_API_CREATE)
	public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) throws Exception {
		if (!createUserRequest.getPassword().equals(createUserRequest.getConfPassword())){
			logger.error("createUser: " + Constant.Message.PASSWORDS_DO_NOT_MATCH);
			throw new Exception(Constant.Message.PASSWORDS_DO_NOT_MATCH);
		}
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(createUserRequest.getPassword());
		user = userService.createUser(user);
		logger.info("createUser: The user has been created successfully");
		return ResponseEntity.ok(user);
	}
	
}
