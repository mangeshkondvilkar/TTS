/**
 * 
 */
package com.privemanagers.tts.dto;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.privemanagers.tts.controller.PriveTTSController;

/**
 * @author Mangesh K
 *
 */
public class PriveTTSDaoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String key;
	private String isin;
	private String strategyKey;

	private String language;
	private int sessionPin;
	private String ttsText;

	// For propertyChange event handling
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public PriveTTSDaoDto() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStrategyKey() {
		return strategyKey;
	}

	public void setStrategyKey(String strategyKey) {
		this.strategyKey = strategyKey;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getSessionPin() {
		return sessionPin;
	}

	public void setSessionPin(int sessionPin) {
		propertyChangeSupport.firePropertyChange("sessionPin", this.sessionPin, sessionPin);
		this.sessionPin = sessionPin;
	}

	public String getTtsText() {
		return ttsText;
	}

	public void setTtsText(String ttsText) {
		this.ttsText = ttsText;
	}

	/**
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

}
