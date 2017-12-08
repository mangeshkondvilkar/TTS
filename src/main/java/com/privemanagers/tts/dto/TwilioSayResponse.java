/**
 * 
 */
package com.privemanagers.tts.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Mangesh K
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TwilioSayResponse {

	@XmlAttribute
	private String voice;

	@XmlAttribute
	private String language;

	@XmlValue
	private String content;

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
