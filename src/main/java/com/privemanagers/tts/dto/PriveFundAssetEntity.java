/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;

/**
 * @author Mangesh K
 *
 */
public class PriveFundAssetEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String isin;
	private String citiFundCodde;
	
	public String getIsin() {
		return isin;
	}
	public void setIsin(String isin) {
		this.isin = isin;
	}
	public String getCitiFundCodde() {
		return citiFundCodde;
	}
	public void setCitiFundCodde(String citiFundCodde) {
		this.citiFundCodde = citiFundCodde;
	}
	
}
