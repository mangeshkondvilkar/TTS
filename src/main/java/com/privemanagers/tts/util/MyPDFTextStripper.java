package com.privemanagers.tts.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

/** Override class to to process only bold text. */
public class MyPDFTextStripper extends PDFTextStripper {

	ArrayList<List<TextPosition>> pageText = new ArrayList<List<TextPosition>>();

	public MyPDFTextStripper() throws IOException {
		super();
	}

	public MyPDFTextStripper(String encoding) throws IOException {
		super(encoding);
	}

	@Override
	public void writePage() throws IOException {
		super.writePage();
		pageText.add(getCharactersByArticle().get(0));
	}

	public ArrayList<List<TextPosition>> getPageText() {
		return pageText;
	}

	@Override
	protected void processTextPosition(TextPosition textPosition) {
		// TODO Auto-generated method stub
		// System.out.println(textPosition.getFont().getBaseFont());
		// System.out.println(textPosition.getFont().getBaseFont());
		if (textPosition.getFont().getBaseFont().contains("Bold")
				|| textPosition.getFont().getBaseFont().contains("Bol")
				|| textPosition.getFont().getBaseFont().contains("bold")
				|| textPosition.getFont().getBaseFont().contains("bol")
				|| textPosition.getFont().getBaseFont().contains("Pro-Regular")
				|| textPosition.getFont().getBaseFont().contains("Amplitude-Medium")) {
			super.processTextPosition(textPosition);
		} else {
			// System.out.println(textPosition.getFont().getBaseFont());
			// super.processTextPosition(textPosition);
			return;
		}
	}

}
