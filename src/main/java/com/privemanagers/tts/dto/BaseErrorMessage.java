/**
 * 
 */
package com.privemanagers.tts.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mangesh K
 *
 */
@XmlRootElement(name="ErrorResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseErrorMessage {

	@XmlElement
	private int statusCode;
	@XmlElement
	private String errorMessage;
	@XmlElement
	private Throwable cause;

	public BaseErrorMessage(){
		super();
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}
	
}
