/**
 * 
 */
package com.privemanagers.tts.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * @author Mangesh K
 *
 */
public class PDFScraperWithPDFBoxTest {

	private static final Logger LOGGER = Logger.getLogger(PDFScraperWithPDFBoxTest.class);

	/**
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {

		final File folder = new File("/home/mkondvilkar/prive-jira/Citi-New/scrapper/kfs_pdfs/en_pdfs");
		int count = 1;
		if (folder.isDirectory()) {
			List<File> files = Arrays.asList(folder.listFiles());

			for (File file : files) {
				if (file.isFile()) {
					System.out.println(count+" ----------------------------------------------------------------------------------------");
					System.out.println(processPdf(file, "en"));
					count++;
				}
			}
		}
	}

	public static void main1(String[] args) throws Exception {

		URL url1 = new URL(
				"http://192.168.1.107:8080/SlyAWS/DownloadFundFactsheet?locale=en&country=HKG&strategykey=25652&type=10&token=MaVkx3qyCE9uRgqzRp527PsUZhsjdf");

		byte[] ba1 = new byte[1024];
		int baLength;
		FileOutputStream fos1 = new FileOutputStream(
				"/home/mkondvilkar/prive-jira/Citi-New/scrapper/kfs_pdfs/out_1/download_from_url.pdf");

		try {
			// Contacting the URL
			System.out.print("Connecting to " + url1.toString() + " ... ");
			URLConnection urlConn = url1.openConnection();

			// Checking whether the URL contains a PDF
			if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
				System.out.println("FAILED.\n[Sorry. This is not a PDF.]");
			} else {
				try {

					// Read the PDF from the URL and save to a local file
					InputStream is1 = url1.openStream();
					while ((baLength = is1.read(ba1)) != -1) {
						fos1.write(ba1, 0, baLength);
					}
					fos1.flush();
					fos1.close();
					is1.close();

					// Load the PDF document and display its page count
					System.out.print("DONE.\nProcessing the PDF ... ");
					processPdf(
							new File(
									"/home/mkondvilkar/prive-jira/Citi-New/scrapper/kfs_pdfs/out_1/download_from_url.pdf"),
							"/home/mkondvilkar/prive-jira/Citi-New/scrapper/kfs_pdfs/out_1/download_from_url_out.txt");

				} catch (ConnectException ce) {
					System.out.println("FAILED.\n[" + ce.getMessage() + "]\n");
				}
			}

		} catch (NullPointerException npe) {
			System.out.println("FAILED.\n[" + npe.getMessage() + "]\n");
		}
	}

	public static String processPdf(File file, String locale, String... ignoreLines) throws Exception {

		if (locale.equalsIgnoreCase("en")) {
			return processPdfEnglish(file, ignoreLines);
		} else {
			System.out
					.println("Implement new scrapper method for Chinese PDF...Currently returning sample chinese text");
			return processPdfChinese(file, ignoreLines);
		}
	}

	/**
	 * A method to process English PDFs
	 * 
	 * @param file
	 * @param ignoreLines
	 * @return
	 * @throws Exception
	 */
	private static String processPdfEnglish(File file, String... ignoreLines) throws Exception {

		PDDocument doc = null;
		try {
			System.out.println("Inside processPdfEnglish: " + file.getName());
			// load pdf
			doc = PDDocument.load(file);
			//
			MyPDFTextStripper stripper = new MyPDFTextStripper("UTF-8");
			stripper.setStartPage(0);
			stripper.setEndPage(doc.getNumberOfPages());
			byte[] txts = stripper.getText(doc).getBytes("UTF-8");
			String txt = new String(txts, "UTF-8");

			String srcReplace = "(How|how|HOW) (has|Has|HAS) (the|The|THE) (Fund|fund|Sub-Fund|sub-fund|SUB FUND|sub fund|Sub Fund) (performed?|Performed?|PERFORMED?)";
			String destReplace = "How has the Portfolio performed?";
			txt = txt.replaceAll(srcReplace, destReplace);
			txt = txt.replaceAll("(What|WHAT) (are|ARE) (the|THE) (key|KEY) (risks|RISKS)", "What are the key risks");

			txt = txt.substring(txt.indexOf("What are the key risks?") + "What are the key risks?".length(),
					txt.indexOf("How has the Portfolio performed?"));

			BufferedReader reader = new BufferedReader(new StringReader(txt.trim()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			boolean startsWithNumber = false;
			int temp = 0;
			int lineNo = 1;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// Skip the introductory content before risks content starts
				// e.g skip - "Investment involves risks. Please refer to the
				// offering document for details including the risk factors."

				if (!line.matches("[0-9].*") && line.indexOf(".") > 0) {
					continue;
				}

				// If line has no valid content, skip it
				if (!line.matches(".*[A-Za-z0-9].*")) {
					// if no content in line then skip
					continue;
				}

				// If lien contains only a word meaning it is not a part of risk
				// headers, so skip it
				if (!line.contains(" ")) {
					continue;
				}

				// Ignore line if it is the Fund Name
				if ((ignoreLines.length > 0 && ignoreLines[0] != null)
						&& ignoreLines[0].toLowerCase().replaceAll("[^A-Za-z0-9\\. ]", "")
								.contains(line.toLowerCase().replaceAll("[^A-Za-z0-9\\. ]", ""))) {
					System.out.println("Exact Fund name found in script...hence ignoring");
					continue;
				}

				if (temp == 0 && line.matches("[0-9].*")) {
					// headers starts with number
					startsWithNumber = true;
					temp++;
				}

				if (!startsWithNumber) {
					line = String.valueOf(lineNo) + ". " + line;
					lineNo++;
				}

				// if Line starts with numbers, then ignore all subsequent
				// content which does not start with number
				if (startsWithNumber && !line.matches("[0-9].*")) {
					continue;
				}
				// sb.append(line.replaceAll("[^A-Za-z0-9\\. ]", "") + ".\n");
				sb.append(line.replaceAll("[^A-Za-z0-9\\\\/\\-.,()\\“\\” ]", "") + ".\n"); // new
			}

			return sb.toString();
		} catch (Exception e) {
			LOGGER.error("Error in PDFBox processing. Failed for: " + file.getName());
			LOGGER.error(e.getMessage());
			throw e;
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * Method to extract KFS Fund date for English PDFs.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String extractDate(File file) throws Exception {
		PDDocument doc = null;
		try {
			// load pdf
			doc = PDDocument.load(file);
			PDFTextStripper stripper = new PDFTextStripper("UTF-8");
			stripper.setStartPage(0);
			stripper.setEndPage(1);
			byte[] txts = stripper.getText(doc).getBytes("UTF-8");
			String firstPage = new String(txts, "UTF-8");

			BufferedReader reader = new BufferedReader(new StringReader(firstPage.trim()));
			String line = null;
			String date = "";

			while ((line = reader.readLine()) != null) {
				if (line.matches(".*[0-9]{4}.*")) {
					for (MyMonthsEnum month : MyMonthsEnum.values()) {
						if (line.contains(month.getMonth())) {
							int starIndex = line.indexOf(month.getMonth());
							date = line.substring(starIndex, starIndex + (month.getMonth().length() + 5));
							break;
						}
					}
					break;
				}
			}
			return date;
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != doc) {
				doc.close();
			}
		}
	}

	/**
	 * A method to process chinese PDFs
	 * 
	 * @param file
	 * @param ignoreLines
	 * @return
	 * @throws Exception
	 */
	private static String processPdfChinese(File file, String... ignoreLines) throws Exception {
		LOGGER.info("Processing Chinese PDF: " + file.getName());
		PDDocument doc = null;
		try {
			// load pdf
			doc = PDDocument.load(file);
			MyPDFTextStripper2 stripper = new MyPDFTextStripper2("UTF-8");
			stripper.setStartPage(0);
			stripper.setEndPage(doc.getNumberOfPages());
			byte[] txts = stripper.getText(doc).getBytes("UTF-8");
			String txt = new String(txts, "UTF-8");

			String srcReplace = "本附屬基金有哪些主要風險?|本基金有哪些主要風險?|本基金有哪些主要風險?|本基金有哪些主要風險?|有哪些主要風險?";
			String destReplace = "本附屬基金有哪些主要風險?";
			txt = txt.replaceAll(srcReplace, destReplace);

			txt = txt.replaceAll("本附屬基金過往表現如何?|本基金表現如何?|本基金過往的表現如何?|本基金過往的業績表現如何？|本基金表現如何?|本成分基金過往的業績表現如何?|本子基金表現如何?",
					"本附屬基金過往表現如何?");
			try {
				System.out.println(txt.indexOf("本附屬基金有哪些主要風險?"));
				System.out.println(txt.indexOf("本附屬基金過往表現如何?"));
				txt = txt.substring(txt.indexOf("本附屬基金有哪些主要風險?") + "本附屬基金有哪些主要風險?".length(),
						txt.indexOf("本附屬基金過往表現如何?"));
			} catch (Exception e) {
				System.err.println("Failed to find the key risks in this pdf.." + e.getMessage());
				System.out.println("Failed for: " + file.getName());
			}

			BufferedReader reader = new BufferedReader(new StringReader(txt.trim()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			boolean startsWithNumber = false;
			int temp = 0;
			int lineNo = 1;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				// skip the introductory content before risks content starts
				// e.g skip - "Investment involves risks. Please refer to the
				// offering document for details including the risk factors."
				if (line.equals("？") || line.equals("") || line == null || line.contains("，") || line.contains("；")
						|| line.contains("－")) {
					continue;
				}
				if (!line.matches("[0-9].*") && line.indexOf("。") > 0) {
					continue;
				}

				// No valid content
				if (line.length() < 2) {
					continue;
				}

				if (temp == 0 && line.matches("[0-9].*")) {
					// headers starts with number
					startsWithNumber = true;
					temp++;
				}

				if (!startsWithNumber) {
					line = String.valueOf(lineNo) + ". " + line;
					lineNo++;
				}

				// if Line starts with numbers, then ignore all subsequent
				// content which does not start with number
				if (startsWithNumber && !line.matches("[0-9].*")) {
					continue;
				}
				line = line.replace("•", "").trim();
				line = line.replace("·", "").trim();
				line = line.replace("：", "").trim();
				line = line.replace(":", "").trim();
				sb.append(line + ".\n");
				// sb.append(line.replaceAll("[^A-Za-z0-9\\. ]", "") + ".\n");
			}

			return sb.toString();
		} catch (Exception e) {
			LOGGER.error("Error in Chinese PDFBox processing. Failed for: " + file.getName());
			LOGGER.error(e.getMessage());
			throw e;
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	/**
	 * Method to extract KFS Fund Date from Chinese PDFs.
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String extractDateChinese(File file) throws Exception {
		PDDocument doc = null;
		try {
			// load pdf
			doc = PDDocument.load(file);
			PDFTextStripper stripper = new PDFTextStripper("UTF-8");
			stripper.setStartPage(0);
			stripper.setEndPage(1);
			byte[] txts = stripper.getText(doc).getBytes("UTF-8");
			String firstPage = new String(txts, "UTF-8");

			BufferedReader reader = new BufferedReader(new StringReader(firstPage.trim()));
			String line = null;
			String date = null;

			while ((line = reader.readLine()) != null) {
				if (line.matches(".*[0-9]{4}.*") && (line.contains("年") || line.contains("月"))) {
					line = line.replaceAll("\\s+", "");
					date = line.substring(line.indexOf("年") - 4, line.indexOf("月") + 1);
					date = date.substring(0, 4) + " " + date.substring(4);
					break;
				}
				if (date == null) {
					for (MyMonthsEnum month : MyMonthsEnum.values()) {
						if (line.contains(month.getMonth())) {
							// System.out.println(line);
							int starIndex = line.indexOf(month.getMonth());
							date = line.substring(starIndex, starIndex + (month.getMonth().length() + 5));
							System.out.println(date);
							break;
						}
					}
					// check this
					if (date != null) {
						break;
					}
				}
			}
			return date;
		} catch (Exception e) {
			throw e;
		} finally {
			doc.close();
		}
	}
}
