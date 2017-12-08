/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Mangesh K
 *
 */
@Component
@Scope("prototype")
public class PriveTTSDaoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String key; 
	private String isin;
	private String strategyKey;

	private String language;
	private int sessionPin;
	private String ttsText;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStrategyKey() {
		return strategyKey;
	}

	public void setStrategyKey(String strategyKey) {
		this.strategyKey = strategyKey;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getSessionPin() {
		return sessionPin;
	}

	public void setSessionPin(int sessionPin) {
		this.sessionPin = sessionPin;
	}

	public String getTtsText() {
		return ttsText;
	}

	public void setTtsText(String ttsText) {
		this.ttsText = ttsText;
	}

}
