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
	private TwilioSayTag say;

	public TwilioSayTag getSay() {
		return say;
	}

	public void setSay(TwilioSayTag say) {
		this.say = say;
	}

}
