/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;

/**
 * @author Mangesh K
 *
 */
public class TwilioDaoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String isin;
	private String strategyKey;
	private int sessionPin;
	private String language;
	private String ttsText;
	private String callerMobileNo;

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getStrategyKey() {
		return strategyKey;
	}

	public void setStrategyKey(String strategyKey) {
		this.strategyKey = strategyKey;
	}

	public int getSessionPin() {
		return sessionPin;
	}

	public void setSessionPin(int sessionPin) {
		this.sessionPin = sessionPin;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTtsText() {
		return ttsText;
	}

	public void setTtsText(String ttsText) {
		this.ttsText = ttsText;
	}

	public String getCallerMobileNo() {
		return callerMobileNo;
	}

	public void setCallerMobileNo(String callerMobileNo) {
		this.callerMobileNo = callerMobileNo;
	}

}
