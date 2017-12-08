package com.privemanagers.tts.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.privemanagers.tts.dto.PriveTTSAuditDto;

/**
 * @author Sweetnesh Dholariya
 * @date 29 Nov 2017
 * @company Prive Financial
 */
public class UtilsServer {

	public static final String EXPORT_FOLDER = "/tmp/export/";

	private static final String COLUMN_CREATED_DATE = "Created Date";
	private static final String COLUMN_LANGUAGE = "Language";
	private static final String COLUMN_FUND_CODE = "Citi Fund Code";
	private static final String COLUMN_FUND_NAME = "Fund Name";
	private static final String COLUMN_ISIN = "ISIN";
	private static final String comma = ",";

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void extractLogToXLS(List<PriveTTSAuditDto> priveTTSAuditDtos, String fileName) throws IOException {
		File file = new File(fileName);

		StringBuilder contentBuilder = new StringBuilder();
		// Header
		contentBuilder.append(COLUMN_CREATED_DATE + comma);
		contentBuilder.append(COLUMN_FUND_CODE + comma);
		contentBuilder.append(COLUMN_FUND_NAME + comma);
		contentBuilder.append(COLUMN_LANGUAGE + comma);
		contentBuilder.append(COLUMN_ISIN + comma);

		contentBuilder.append("\n");

		for (PriveTTSAuditDto priveTTSAuditDto : priveTTSAuditDtos) {
			String dateAndTime = "";
			String language = "";
			String fundCode = "";
			String fundName = "";
			String isin = "";

			if (priveTTSAuditDto.getLastUpdatedDate() != null) {
				dateAndTime = dateFormat.format(priveTTSAuditDto.getLastUpdatedDate());
			}

			if (priveTTSAuditDto.getLanguage() != null) {
				language = priveTTSAuditDto.getLanguage();
			}

			if (priveTTSAuditDto.getFundCode() != null) {
				fundCode = priveTTSAuditDto.getFundCode();
			}

			if (priveTTSAuditDto.getFundName() != null) {
				fundName = priveTTSAuditDto.getFundName();
			}

			if (priveTTSAuditDto.getIsin() != null) {
				isin = priveTTSAuditDto.getIsin();
			}

			contentBuilder.append(dateAndTime).append(comma);
			contentBuilder.append(fundCode).append(comma);
			contentBuilder.append(fundName).append(comma);
			contentBuilder.append(language).append(comma);
			contentBuilder.append(isin).append(comma);

			contentBuilder.append("\n");
		}

		FileUtils.writeStringToFile(file, contentBuilder.toString());
	}
}
