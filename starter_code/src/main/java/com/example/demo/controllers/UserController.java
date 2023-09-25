package com.example.demo.controllers;


import com.example.demo.service.UserService;
import com.example.demo.util.Constant;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
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
import java.util.Objects;

@RestController
@RequestMapping(Constant.APIUri.USER_API)
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(Constant.APIUri.USER_API_FIND_BY_ID)
	@JsonView(User.class)
	public ResponseEntity<User> findById(@PathVariable Long id) {
		User user = userService.findById(id);
		return Objects.isNull(user) ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND) : new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping(Constant.APIUri.USER_API_FIND_BY_USERNAME)
	@JsonView(User.class)
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userService.findByUserName(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping(Constant.APIUri.USER_API_CREATE)
	@JsonView(User.class)
	public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) throws Exception {
		if (!createUserRequest.getPassword().equals(createUserRequest.getConfPassword()))
			throw new Exception(Constant.Message.PASSWORDS_DO_NOT_MATCH);
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		user.setPassword(createUserRequest.getPassword());
		return ResponseEntity.ok(userService.createUser(user));
	}
	
}
