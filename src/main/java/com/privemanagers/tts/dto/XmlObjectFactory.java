/**
 * 
 */
package com.privemanagers.tts.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.springframework.stereotype.Component;

/**
 * @author Mangesh K
 *
 */
@Component
@XmlRegistry
public class XmlObjectFactory {

	@XmlElementDecl(name = "Say")
    public JAXBElement<TwilioSayTag> createSayTag(TwilioSayTag sayTag) {
        return new JAXBElement<TwilioSayTag>(new QName("Say"), TwilioSayTag.class, sayTag);
    }
	

	@XmlElementDecl(name = "Pause")
    public JAXBElement<TwilioPauseTag> createPauseTag(TwilioPauseTag pauseTag) {
        return new JAXBElement<TwilioPauseTag>(new QName("Pause"), TwilioPauseTag.class, pauseTag);
    }
	
}
