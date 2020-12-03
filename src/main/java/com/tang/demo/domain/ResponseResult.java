package com.tang.demo.domain;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {

	private static final long serialVersionUID = -6964481356983562044L;
	public static final Integer SUCCESS_STATUS = 0;
	public static final String SUCCESS_MESSAGE = "success";

	private Integer status;
	private String message;
	private T data;

	public ResponseResult(String message, Integer code) {
		this.message = message;
		this.status = code;
	}

	public ResponseResult(String message, Integer code, T data) {
		this.message = message;
		this.status = code;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	/////////////////////////////////////////////////////////////////

	public static <T>  ResponseResult<T> success() {
		return new ResponseResult<T>(SUCCESS_MESSAGE, SUCCESS_STATUS, null);
	}
	
	public static <T> ResponseResult<T> success(T data) {
		return new ResponseResult<T>(SUCCESS_MESSAGE, SUCCESS_STATUS, data);
	}

	public static <T> ResponseResult<T> failed(Integer errorCode, String message) {
		return new ResponseResult<T>(message, errorCode);
	}
}