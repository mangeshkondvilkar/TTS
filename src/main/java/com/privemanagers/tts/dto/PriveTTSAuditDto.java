/**
 *
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mangesh K
 *
 */
public class PriveTTSAuditDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String isin;
	private String sessionPin;
	private Date createdDate;
	private Date lastUpdatedDate;
	private String callerMobileNo;
	private String language;
	private String fundCode;
	private String fundName;

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getSessionPin() {
		return sessionPin;
	}

	public void setSessionPin(String sessionPin) {
		this.sessionPin = sessionPin;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCallerMobileNo() {
		return callerMobileNo;
	}

	public void setCallerMobileNo(String callerMobileNo) {
		this.callerMobileNo = callerMobileNo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
}
