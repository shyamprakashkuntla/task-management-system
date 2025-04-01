package com.ve.task_management.constants;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
	 private int statusCode;
	    private String message;
	    private boolean success;
	    private T data;

	    // Constructors
	    public ResponseWrapper() {
	    }

	    // Constructor for responses with data
	    public ResponseWrapper(HttpStatus status, String message, boolean success, T data) {
	        this.statusCode = status.value();
	        this.message = message;
	        this.success = success;
	        this.data = data;
	    }
 
	    // Constructor for responses without data
	    public ResponseWrapper(HttpStatus status, String message, boolean success) {
	        this.statusCode = status.value();
	        this.message = message;
	        this.success = success;
	    }

	    public ResponseWrapper(int statusCode, String message, T data) {
//			super();
			this.statusCode = statusCode;
			this.message = message;
			this.data = data;
		}
	    
	    public ResponseWrapper(int statusCode, String message, boolean success) {
	        this.statusCode = statusCode;
	        this.message = message;
	        this.success = success;
	        this.data = null;   // No data for this constructor
	    }
	    
	    public ResponseWrapper(int statusCode, String message, boolean success,T data) {
	        this.statusCode = statusCode;
	        this.message = message;
	        this.success = success;
	        this.data = data;   // No data for this constructor
	    }

		// Getters and Setters
	    public int getStatusCode() {
	        return statusCode;
	    }

	    public void setStatusCode(int statusCode) {
	        this.statusCode = statusCode;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public boolean isSuccess() {
	        return success;
	    }

	    public void setSuccess(boolean success) {
	        this.success = success;
	    }

	    public T getData() {
	        return data;
	    }

	    public void setData(T data) {
	        this.data = data;
	    }

	    @Override
	    public String toString() {
	        return "ResponseWrapper{" +
	               "statusCode=" + statusCode +
	               ", message='" + message + '\'' +
	               ", success=" + success +
	               ", data=" + data +
	               '}';
	    }

}
