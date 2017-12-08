/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mangesh K
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwilioResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*
	@XmlElement(name = "Pin", required = true, nillable = true)
	private int id;
*/
	@XmlElement(name = "Say", required = true, nillable = true)
	private TwilioSayResponse say;

/*	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
*/
	public TwilioSayResponse getSay() {
		return say;
	}

	public void setSay(TwilioSayResponse say) {
		this.say = say;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
