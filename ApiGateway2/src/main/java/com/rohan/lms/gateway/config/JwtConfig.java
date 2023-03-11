package com.rohan.lms.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {

	private String secret;
	private String header;
	private long validity;
	private boolean authDisabled;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public long getValidity() {
		return validity;
	}

	public void setValidity(long validity) {
		this.validity = validity;
	}

	public boolean isAuthDisabled() {
		return authDisabled;
	}

	public void setAuthDisabled(boolean authDisabled) {
		this.authDisabled = authDisabled;
	}

}
