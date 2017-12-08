/**
 * 
 */
package com.privemanagers.tts.exception;

/**
 * @author Mangesh K
 *
 */
public class AjaxCallException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public AjaxCallException(String message){
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
