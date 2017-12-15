/**
 * 
 */
package com.privemanagers.tts.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author Mangesh K
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ TwilioSayTag.class, TwilioPauseTag.class })
public class TwilioResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlAnyElement(lax = true)
	private List<Object> object;

	public List<Object> getObject() {
		return object;
	}

	public void setObject(List<Object> object) {
		this.object = object;
	}

}
