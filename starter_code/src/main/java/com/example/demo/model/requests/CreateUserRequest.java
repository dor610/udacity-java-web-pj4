package com.example.demo.model.requests;

import com.example.demo.util.Constant;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequest {

	@JsonProperty()
	@NotNull
	private String username;

	@JsonProperty()
	@NotNull
	@Size(min = 8, message = Constant.Message.PASSWORD_TOO_SHORT)
	private String password;

	@JsonProperty()
	@NotNull
	private String confPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfPassword() {
		return confPassword;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
}
