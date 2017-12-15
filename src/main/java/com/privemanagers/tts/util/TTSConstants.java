/**
 *
 */
package com.privemanagers.tts.util;

/**
 * @author Mangesh K: An interface to store project specific constants
 */
public interface TTSConstants {

	/**
	 * URL Constants for end-point mappings
	 */
	public static final String PERMALINK_URL_PATH = "/citifundtts";

	/**
	 * SQL Constants for sql queries
	 */

	public static final String FETCH_CITI_TTS_KEY_BASED_ON_ISIN_LANG = "select a.key, b.strategy_key from CITI_TTS a join CITI_FUND_ASSET b on a.citi_fund_asset_key=b.key where b.isin=? and a.language=?";

	public static final String FETCH_CITI_TTS_KEY_BASED_ON_KEY_LANG = "select a.key, b.isin from CITI_TTS a join CITI_FUND_ASSET b on a.citi_fund_asset_key=b.key where b.strategy_key=? and a.language=?";

	public static final String SAVE_CITI_TTS_PIN = "INSERT INTO CITI_TTS_PIN (`SESSION_PIN`,`CREATED_TIMESTAMP`,`LAST_UPDATE_TIMESTAMP`,`CITI_TTS_KEY`) "
			+ "VALUES(?,sysdate(),sysdate(),?)";

	public static final String FETCH_TWILIONO_FROM_CONFIG = "select CONFIG_VALUE from TTS_CONFIG where CONFIG_NAME='TWILIO_MOBILE_NO' and CLIENT_CODE='CITI'";

	public static final String FETCH_KFS_FILE_URL = "select a.KFS_FILE_URL from CITI_TTS a join CITI_TTS_PIN b on a.KEY = b.CITI_TTS_KEY where b.SESSION_PIN=?";

	public static final String FETCH_TTS_DATA_FOR_TWILIO = "select a.ISIN, a.STRATEGY_KEY, b.LANGUAGE, b.TTS_TEXT from CITI_FUND_ASSET a join CITI_TTS b join CITI_TTS_PIN c on a.KEY = b.CITI_FUND_ASSET_KEY and b.KEY=c.CITI_TTS_KEY where c.SESSION_PIN=?";

	public static final String REMOVE_SESSION_PIN = "update CITI_TTS_PIN set SESSION_PIN=null where SESSION_PIN=?";

	public static final String SAVE_AUDIT_LOG = "INSERT INTO CITI_TTS_AUDIT_HIST(`ISIN`, `STRATEGY_KEY`, `LANGUAGE`, `SESSION_PIN`,`CREATED_TIMESTAMP`,`LAST_UPDATE_TIMESTAMP`) VALUES(?,?,?,?,sysdate(),sysdate())";

	public static final String UPDATE_AUDIT_LOG = "UPDATE CITI_TTS_AUDIT_HIST SET `LAST_UPDATE_TIMESTAMP`= sysdate(),`MOBILE_NO`=? WHERE (`ISIN`=? OR `STRATEGY_KEY`=?) AND `SESSION_PIN`=?";

	public static final String FETCH_AUDIT_LOG = "SELECT AUDIT.LAST_UPDATE_TIMESTAMP, AUDIT.LANGUAGE, FUND.CITI_FUND_CODE, FUND.CITI_FUND_NAME, FUND.ISIN FROM CITI_TTS_AUDIT_HIST AUDIT JOIN CITI_FUND_ASSET FUND on AUDIT.STRATEGY_KEY = FUND.STRATEGY_KEY WHERE AUDIT.LAST_UPDATE_TIMESTAMP BETWEEN ? AND ? order by AUDIT.LAST_UPDATE_TIMESTAMP";

	public static final String SAVE_TTS_TEXT = "UPDATE CITI_TTS SET TTS_TEXT=? WHERE (CITI_FUND_ASSET_KEY IN (SELECT A.KEY FROM CITI_FUND_ASSET A WHERE A.STRATEGY_KEY=? OR A.ISIN=?)) AND LANGUAGE=?";

	public static final String FETCH_TTS_TEXT = "SELECT TTS_TEXT FROM CITI_TTS WHERE (CITI_FUND_ASSET_KEY IN (SELECT A.KEY FROM CITI_FUND_ASSET A WHERE A.STRATEGY_KEY=? OR A.ISIN=?)) AND LANGUAGE=?";

	public static final String SAVE_TTS_CHANGE_LOG = "INSERT INTO TTS_TEXT_CHANGE_LOG(`ASSET_KEY`, `LANGUAGE`, `OLD_TTS_TEXT`,`NEW_TTS_TEXT`,`CREATED_TIMESTAMP`) VALUES(?,?,?,?,sysdate())";

	/**
	 * Other Constants
	 */
	public static final String PLATFORM_SPECIFIC_LINE_SEPARATOR = System.getProperty("line.separator");

	public static final String CATALINA_HOME = System.getProperty("catalina.home");

	public static final String TTS_END_OF_ANNOUNCEMENT_EN = "End of an announcement.";
	public static final String TTS_END_OF_ANNOUNCEMENT_CH = "錄音播放完畢.";

	public static final String TAG_STRATEGY_KEY = "[strategy-key]";

	public static final String TAG_SELECTED_LOCALE = "[selected-locale]";

	public final static boolean PRODUCTION = (System.getenv("PRODUCTION") != null
			&& System.getenv("PRODUCTION").equals("true"))
			|| (System.getProperty("PRODUCTION") != null && System.getProperty("PRODUCTION").equals("true"));

	public final static boolean UAT = (System.getenv("UAT") != null && System.getenv("UAT").equals("true"))
			|| (System.getProperty("UAT") != null && System.getProperty("UAT").equals("true"));

	public static final String DOWNLOAD_URL_PROD = "https://www.privemanagers.com/DownloadFundFactsheet?locale=[selected-locale]&country=HKG&strategykey=[strategy-key]&type=10&token=MaVkx3qyCE9uRgqzRp527PsUZhsjdf";

	public static final String DOWNLOAD_URL_UAT = "https://citiuat.privemanagers.com/DownloadFundFactsheet?locale=[selected-locale]&country=HKG&strategykey=[strategy-key]&type=10&token=mX8PSEBg42WR48auyGdTNH38WErbZ3";

	public static final String DOWNLOAD_URL_DEV = "http://192.168.1.107:8080/SlyAWS/DownloadFundFactsheet?locale=[selected-locale]&country=HKG&strategykey=[strategy-key]&type=10&token=MaVkx3qyCE9uRgqzRp527PsUZhsjdf";

}
