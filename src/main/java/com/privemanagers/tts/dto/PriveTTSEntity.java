/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;

/**
 * @author Mangesh K
 *
 */
public class PriveTTSEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String language;
	private String ttsFileURL;
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getTtsFileURL() {
		return ttsFileURL;
	}
	public void setTtsFileURL(String ttsFileURL) {
		this.ttsFileURL = ttsFileURL;
	}

}
