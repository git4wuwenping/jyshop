package com.qyy.jyshop.exception;

/**
 * 接口登录异常
 */
@SuppressWarnings("serial")
public class AppLoginException extends RuntimeException {
    
	private int key;
	private String message;

	public AppLoginException() {
	}

	public AppLoginException(String message) {
		this.message = message;
	}

	public AppLoginException(int key, String message) {
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
