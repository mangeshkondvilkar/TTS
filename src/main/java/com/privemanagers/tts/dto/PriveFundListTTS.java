/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author Mangesh K
 *
 */
public class PriveFundListTTS implements Serializable {

	public PriveFundListTTS() {
		super();
	}

	private static final long serialVersionUID = 1L;

	private String isin;
	private int sessionPin;
	private String fundName;
	private String fundCode;
	private String language;
	private String ttsFileLocation;
	private Date createdDate;
	private Date lastUpdateDate;

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public int getSessionPin() {
		return sessionPin;
	}

	public void setSessionPin(int sessionPin) {
		this.sessionPin = sessionPin;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTtsFileLocation() {
		return ttsFileLocation;
	}

	public void setTtsFileLocation(String ttsFileLocation) {
		this.ttsFileLocation = ttsFileLocation;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public String toString() {
		return "isin: " + isin + ", sessionPin: " + sessionPin + ", fundName: " + fundName + ", createdDate: " + createdDate;
	}

}
