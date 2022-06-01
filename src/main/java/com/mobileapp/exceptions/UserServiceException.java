package com.mobileapp.exceptions;

public class UserServiceException extends RuntimeException{
	
	private static final long serialVersionUID = -2292705512716703442L;

	public UserServiceException(String message) 
	{
		super(message);
	}

}
