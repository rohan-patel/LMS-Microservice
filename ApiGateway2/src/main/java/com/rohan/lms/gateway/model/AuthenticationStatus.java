package com.rohan.lms.gateway.model;

public class AuthenticationStatus {

	private Boolean isAuthenticated;
	private String message;

	public AuthenticationStatus() {
	}

	public AuthenticationStatus(Boolean isAuthenticated, String message) {
		super();
		this.isAuthenticated = isAuthenticated;
		this.message = message;
	}

	public Boolean getIsAuthenticated() {
		return isAuthenticated;
	}

	public void setIsAuthenticated(Boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
