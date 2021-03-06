package com.featureuseraccess.service;

/**
 * Exception to signify that an update operation has failed.
 * 
 * @author Fletcher Sarip
 *
 */
public class UpdateFailedException extends Exception {

	private static final long serialVersionUID = -800890650489537904L;

	public UpdateFailedException() {
		super();
	}

	public UpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateFailedException(String message) {
		super(message);
	}

	public UpdateFailedException(Throwable cause) {
		super(cause);
	}
	
}
