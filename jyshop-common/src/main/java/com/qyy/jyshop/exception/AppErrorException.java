package com.qyy.jyshop.exception;

@SuppressWarnings("serial")
public class AppErrorException extends RuntimeException {
	private int key;
	private String message;

	public AppErrorException() {
	}

	public AppErrorException(String message) {
		this.message = message;
	}

	public AppErrorException(int key, String message) {
		this.key = key;
		this.message = message;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
