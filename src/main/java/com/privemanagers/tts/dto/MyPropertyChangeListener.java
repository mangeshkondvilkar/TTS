/**
 * 
 */
package com.privemanagers.tts.dto;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

/**
 * @author Mangesh K
 *
 */
public class MyPropertyChangeListener implements PropertyChangeListener {

	private boolean isPinFlushed = false;
	
	private static final Logger LOGGER = Logger.getLogger(MyPropertyChangeListener.class);
	
	public MyPropertyChangeListener() {
		super();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

		LOGGER.info("MyPropertyChangeListener is called....");
		LOGGER.info("OldPin Value: " + event.getOldValue());
		LOGGER.info("NewPin Value: " + event.getNewValue());

		if (Integer.valueOf(String.valueOf(event.getNewValue())) == 0) {
			this.isPinFlushed = true;
		}
	}

	public boolean isPinFlushed() {
		return this.isPinFlushed;
	}
}
