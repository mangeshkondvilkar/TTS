/**
 * 
 */
package com.privemanagers.tts.util;

/**
 * @author Mangesh K
 *
 */
public enum MyMonthsEnum {
	Jan(1, "January"), Feb(2, "February"), Mar(3, "March"), Apr(4, "April"), May(5, "May"), Jun(6, "June"), Jul(7,
			"July"), Aug(8,"August"), Sep(9, "September"), Oct(10, "October"), Nov(11, "November"), Dec(12, "December");

	private int monthNo;
	private String month;

	MyMonthsEnum(int monthNo, String month) {
		this.monthNo = monthNo;
		this.month = month;
	}

	public int getMonthNo() {
		return this.monthNo;
	}

	public String getMonth() {
		return this.month;
	}

}

