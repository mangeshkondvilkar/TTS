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
@XmlRootElement(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwilioErrorResponse {

	@XmlElement(name="Say")
	private TwilioSayResponse say;

	public TwilioSayResponse getSay() {
		return say;
	}

	public void setSay(TwilioSayResponse say) {
		this.say = say;
	}

}
