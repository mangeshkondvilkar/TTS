/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;

/**
 * @author Mangesh K
 *
 */
public class PriveTTSPinEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sessionPIN;

	public int getSessionPIN() {
		return sessionPIN;
	}

	public void setSessionPIN(int sessionPIN) {
		this.sessionPIN = sessionPIN;
	}
}
