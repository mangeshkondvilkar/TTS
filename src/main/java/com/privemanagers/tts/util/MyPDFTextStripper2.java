package com.privemanagers.tts.util;

import java.io.IOException;

import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

/** Override class to extract location of each character on page. */
public class MyPDFTextStripper2 extends PDFTextStripper {

	public MyPDFTextStripper2() throws IOException {
		super();
	}

	public MyPDFTextStripper2(String encoding) throws IOException {
		super(encoding);
	}

	@Override
	protected void processTextPosition(TextPosition textPosition) {
		// TODO Auto-generated method stub
		String fontName = "";
		if (null!=textPosition.getFont().getFontDescriptor()){
			/*System.out.println(textPosition.getFont().getFontDescriptor().getFontWeight() + " || "
					+ textPosition.getFont().getFontDescriptor().getFontName());*/
			fontName = textPosition.getFont().getFontDescriptor().getFontName();
		}
		//System.out.println(textPosition.getFont().getBaseFont());
		if (textPosition.getFont().getBaseFont().contains("Bold")
				|| textPosition.getFont().getBaseFont().contains("Bol")
				|| textPosition.getFont().getBaseFont().contains("bold")
				|| textPosition.getFont().getBaseFont().contains("bol")
				|| textPosition.getFont().getBaseFont().contains("Pro-Regular")
				|| textPosition.getFont().getBaseFont().contains("Amplitude-Medium")
				|| textPosition.getFont().getBaseFont().contains("PMingLiU")) {
			super.processTextPosition(textPosition);
		} else {
			// System.out.println(textPosition.getFont().getBaseFont());
			// super.processTextPosition(textPosition);
			return;
		}
	}

}
