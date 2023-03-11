package com.rohan.lms.gateway.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ErrorResponseDto implements Serializable {

	private Date timestamp;
	private String error;
	private List<String> details;
	private String path;

	public ErrorResponseDto() {
		super();
	}

	public ErrorResponseDto(Date timestamp, String error, List<String> details, String path) {
		super();
		this.timestamp = timestamp;
		this.error = error;
		this.details = details;
		this.path = path;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
