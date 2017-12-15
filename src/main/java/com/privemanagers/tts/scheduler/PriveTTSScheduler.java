/**
 * 
 */
package com.privemanagers.tts.scheduler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.privemanagers.tts.dto.PriveTTSAuditDto;
import com.privemanagers.tts.service.IPriveTTSService;
import com.privemanagers.tts.util.PDFScraperWithPDFBox;
import com.privemanagers.tts.util.TTSConstants;
import com.privemanagers.tts.util.TTSLanguageEnum;
import com.privemanagers.tts.util.UtilsServer;

/**
 * @author Mangesh K
 *
 */
@Configuration
@EnableScheduling
public class PriveTTSScheduler {

	@Autowired
	private IPriveTTSService citiTTSService;

	private static final Logger LOGGER = Logger.getLogger(PriveTTSScheduler.class);

	/**
	 * A scheduled method to extract the Audit log from DB and export it into
	 * Excel on a weekly basis.
	 */
	@Scheduled(cron = "0 59 23 * * SAT")
	public void auditLogExportBatchJob() {

		try {
			Calendar calendar = Calendar.getInstance();
			int currentMonth = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

			if (calendar.get(Calendar.MONTH) != currentMonth) {
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);

				Calendar toCalendar = Calendar.getInstance();
				toCalendar.set(Calendar.MONTH, currentMonth - 1);
				toCalendar.set(Calendar.DAY_OF_MONTH, toCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				toCalendar.set(Calendar.HOUR_OF_DAY, 23);
				toCalendar.set(Calendar.MINUTE, 59);
				toCalendar.set(Calendar.SECOND, 59);

				extractAuditLogByDates(calendar.getTime(), toCalendar.getTime());
			}

			Calendar currntMonthCalendar = Calendar.getInstance();
			currntMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
			currntMonthCalendar.set(Calendar.HOUR_OF_DAY, 0);
			currntMonthCalendar.set(Calendar.MINUTE, 0);
			currntMonthCalendar.set(Calendar.SECOND, 0);

			extractAuditLogByDates(currntMonthCalendar.getTime(), new Date());

		} catch (Exception e) {
			LOGGER.error("Error while extracting Audit data from prive DB...");
			LOGGER.error(e);
		}
	}

	private void extractAuditLogByDates(Date fromDate, Date toDate) {
		try {
			String fromDateString = UtilsServer.dateFormat.format(fromDate);
			String toDateString = UtilsServer.dateFormat.format(toDate);

			List<PriveTTSAuditDto> priveTTSAuditDtos = citiTTSService.fetchTTSAuditLog(fromDateString, toDateString);
			String fileName = UtilsServer.EXPORT_FOLDER + "TTSAuditLog/TTSAuditLog." + fromDateString + "_"
					+ toDateString + ".CSV";
			UtilsServer.extractLogToXLS(priveTTSAuditDtos, fileName);

			LOGGER.info("Extracted log from :" + fromDateString + " to " + toDateString + " in file " + fileName);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	/**
	 * @author Mangesh K. Purpose of this scheduler is to read the KFS PDFs,
	 *         extract the key risks and save/override them to prive DB on a
	 *         daily basis. Any change in TTS content will be logged for audit
	 *         purpose.
	 */
	@Scheduled(cron = "0 59 6 * * *")
	//@Scheduled(cron = "0 38 16 * * FRI")
	public void pdfScrapperBatchJob() {

		LOGGER.info("pdfScrapperBatchJob scheduler has been started...");
		try {
			// get all keys from DB and iterate below for each key
			// List<String> keys = citiTTSService.fetchAllKeys();
			List<Map<String, Object>> keysList = citiTTSService.fetchAllKeysWithName();
			LOGGER.info("keys.size ======++> " + keysList.size());

			int count = 1;
			Set<String> locales = TTSLanguageEnum.getLocales();

			// For testing
			// String locale = "en";
			for (Map<String, Object> map : keysList) {
				// iterate for each locale for current key
				String currentKey = String.valueOf(map.get("STRATEGY_KEY"));
				String currentFundName = String.valueOf(map.get("CITI_FUND_NAME"));
				LOGGER.info("locales from ENUM=========> " + locales + ", processing key: " + currentKey
						+ " with FundName: " + currentFundName);

				for (String locale : locales) {
					try {
						// 1. Download PDF and save it in temp folder
						String kfsFileUrl = TTSConstants.PRODUCTION ? TTSConstants.DOWNLOAD_URL_PROD
								: (TTSConstants.UAT ? TTSConstants.DOWNLOAD_URL_UAT : TTSConstants.DOWNLOAD_URL_DEV);
						// String kfsFileUrl = TTSConstants.DOWNLOAD_URL_PROD;
						kfsFileUrl = kfsFileUrl.replace(TTSConstants.TAG_STRATEGY_KEY, currentKey);
						kfsFileUrl = kfsFileUrl.replace(TTSConstants.TAG_SELECTED_LOCALE, locale);

						URL url = new URL(kfsFileUrl);
						String fileName = "/tmp/export/Download_from_url_" + count + ".pdf";
						File file = new File(fileName);
						downloadAndSaveToPdf(url, file);

						// 2. Extract TTS content from PDF
						String keyRisks = null;
						String ttsText = null;
						keyRisks = PDFScraperWithPDFBox.processPdf(file, locale, currentFundName);

						if (null != keyRisks) {
							if ("en".equalsIgnoreCase(locale)) {
								ttsText = "Below are the major risk factors in the Product Key Facts dated "
										+ PDFScraperWithPDFBox.extractDate(file) + " of the " + currentFundName + ".\n"
										+ keyRisks + TTSConstants.TTS_END_OF_ANNOUNCEMENT_EN;

							} else {
								ttsText = "下列為 " + currentFundName + " " + PDFScraperWithPDFBox.extractDateChinese(file)
										+ " 產品資料概要中的主要風險因素披露。" + "\n" + keyRisks
										+ TTSConstants.TTS_END_OF_ANNOUNCEMENT_CH;
							}
							// 3. Save to DB
							citiTTSService.saveTTSText(currentKey, ttsText, locale);

						} else {
							LOGGER.error("Failed to read KEY-RISKS from KFS PDF for key => " + currentKey
									+ "; locale => " + locale);
						}
						// delete temp PDF file
						FileUtils.deleteQuietly(file);
						count++;
					} catch (Exception e) {
						LOGGER.error("Failed for key =========> " + currentKey + " and locale ========> " + locale);
						LOGGER.error(e.getMessage());
						e.printStackTrace();
					}
				}
				// Below used for local testing
				// if (count > 4) {
				// break;
				// }
			}

			LOGGER.info("end pdfScrapperBatchJob...");
		} catch (Exception e) {
			LOGGER.error("Exception in PDF Scrapper batch processing...Please contact support! " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void downloadAndSaveToPdf(URL url, File file) throws Exception {
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			byte[] ba1 = new byte[1024];
			int baLength;
			fos = new FileOutputStream(file);
			// Contacting the URL
			LOGGER.info("Connecting to " + url.toString() + " ... ");
			// URLConnection urlConn = url.openConnection();
			// Checking whether the URL contains a PDF

			/*
			 * if (!("application/pdf".equals(urlConn.getContentType()))) {
			 * LOGGER.error("FAILED...[Sorry. This is not a PDF.]\n"); } else {
			 */
			try {
				// Read the PDF from the URL and save to a local
				// file
				is = url.openStream();
				while ((baLength = is.read(ba1)) != -1) {
					fos.write(ba1, 0, baLength);
				}
				fos.flush();
				LOGGER.info("DONE...Processing the PDF ...\n");
			} catch (ConnectException ce) {
				LOGGER.error("FAILED...ConnectException [" + ce.getMessage() + "]\n");
				throw ce;
			}
			// }
		} catch (Exception e) {
			LOGGER.error("FAILED...Exception in downloading PDF [" + e.getMessage() + "]\n");
			throw e;
		} finally {
			if (null != fos) {
				fos.close();
			}
			if (null != is) {
				is.close();
			}
		}
	}

}
