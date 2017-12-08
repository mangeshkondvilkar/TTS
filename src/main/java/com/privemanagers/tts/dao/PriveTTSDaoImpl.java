/**
 *
 */
package com.privemanagers.tts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.privemanagers.tts.dto.PriveFundListTTS;
import com.privemanagers.tts.dto.PriveTTSAuditDto;
import com.privemanagers.tts.dto.PriveTTSDaoDto;
import com.privemanagers.tts.dto.TwilioDaoDto;
import com.privemanagers.tts.exception.NoMappingFoundForISINException;
import com.privemanagers.tts.util.TTSConstants;
import com.privemanagers.tts.util.TTSLanguageEnum;

/**
 * @author Mangesh K
 *
 */
@Repository
public class PriveTTSDaoImpl implements IPriveTTSDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = Logger.getLogger(PriveTTSDaoImpl.class);

	@Override
	public void saveTTSData(PriveTTSDaoDto citiDaoDto) throws Exception {

		Map<String, Object> keyIsinTempMap = null;
		try {

			keyIsinTempMap = jdbcTemplate.queryForMap(TTSConstants.FETCH_CITI_TTS_KEY_BASED_ON_KEY_LANG,
					new Object[] { citiDaoDto.getKey(), citiDaoDto.getLanguage() });

			citiDaoDto.setStrategyKey(citiDaoDto.getKey());
			citiDaoDto.setIsin(String.valueOf(keyIsinTempMap.get("isin")));

		} catch (EmptyResultDataAccessException e) {
			// get the data based on ISIN
			LOGGER.info("CITI_TTS.key not found based on KEY. Hence fetching it based on ISIN...");

			try {

				keyIsinTempMap = jdbcTemplate.queryForMap(TTSConstants.FETCH_CITI_TTS_KEY_BASED_ON_ISIN_LANG,
						new Object[] { citiDaoDto.getKey(), citiDaoDto.getLanguage() });

				citiDaoDto.setIsin(citiDaoDto.getKey());
				citiDaoDto.setStrategyKey(String.valueOf(keyIsinTempMap.get("strategy_key")));

			} catch (EmptyResultDataAccessException e2) {
				LOGGER.info("No mapping found for the received KEY in Prive system...");
				throw new NoMappingFoundForISINException(
						"No mapping found in Prive DB for KEY# " + citiDaoDto.getKey());
			}
		}

		jdbcTemplate.update(TTSConstants.SAVE_CITI_TTS_PIN, citiDaoDto.getSessionPin(), keyIsinTempMap.get("key"));

		LOGGER.info("PIN " + citiDaoDto.getSessionPin() + " has been saved to DB for Twilio request handling...");
	}

	@Override
	public String fetchTwilioNumber() throws Exception {

		String mob_no = jdbcTemplate.queryForObject(TTSConstants.FETCH_TWILIONO_FROM_CONFIG, new Object[] {},
				String.class);

		return mob_no;
	}

	@Override
	public String fetchKFSFileURL(int pin) throws Exception {
		String kfsFilePath = jdbcTemplate.queryForObject(TTSConstants.FETCH_KFS_FILE_URL, new Object[] { pin },
				String.class);

		return kfsFilePath;
	}

	@Override
	public void removeSessionPin(String pin) throws Exception {

		jdbcTemplate.update(TTSConstants.REMOVE_SESSION_PIN, pin);

		LOGGER.info("PIN " + pin + " has been flushed from DB.");
	}

	@Override
	public TwilioDaoDto fetchDataForTwilio(int pin) {

		TwilioDaoDto twilioDaoDto = jdbcTemplate.queryForObject(TTSConstants.FETCH_TTS_DATA_FOR_TWILIO,
				new Object[] { pin }, new RowMapper<TwilioDaoDto>() {
					@Override
					public TwilioDaoDto mapRow(ResultSet rs, int rowNumber) throws SQLException {
						TwilioDaoDto twilioDaoDto = new TwilioDaoDto();
						twilioDaoDto.setIsin(rs.getString("ISIN"));
						twilioDaoDto.setStrategyKey(rs.getString("STRATEGY_KEY"));
						twilioDaoDto.setLanguage(rs.getString("LANGUAGE"));
						twilioDaoDto.setTtsText(rs.getString("TTS_TEXT"));
						return twilioDaoDto;
					}
				});

		return twilioDaoDto;
	}

	@Override
	public void saveTTSAuditLog(PriveTTSDaoDto daoDto) throws Exception {

		jdbcTemplate.update(TTSConstants.SAVE_AUDIT_LOG, daoDto.getIsin(), daoDto.getStrategyKey(),
				daoDto.getLanguage(), daoDto.getSessionPin());
		LOGGER.info("Audit Log has been inserted in DB after PIN genration...");
	}

	@Override
	public void updateAuditLog(TwilioDaoDto twilioDaoDto) throws Exception {

		jdbcTemplate.update(TTSConstants.UPDATE_AUDIT_LOG, twilioDaoDto.getCallerMobileNo(), twilioDaoDto.getIsin(),
				twilioDaoDto.getStrategyKey(), twilioDaoDto.getSessionPin());

		LOGGER.info("Audit Log has been updated in DB after Twilio call...");
	}

	@Override
	public List<PriveTTSAuditDto> fetchTTSAuditLog(String fromDate, String toDate) throws Exception {

		List<PriveTTSAuditDto> priveTTSAuditDtos = new ArrayList<PriveTTSAuditDto>();

		try {
			priveTTSAuditDtos = jdbcTemplate.query(TTSConstants.FETCH_AUDIT_LOG, new Object[] { fromDate, toDate },
					new ResultSetExtractor<List<PriveTTSAuditDto>>() {

						@Override
						public List<PriveTTSAuditDto> extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							List<PriveTTSAuditDto> list = new ArrayList<PriveTTSAuditDto>();

							while (rs.next()) {
								PriveTTSAuditDto priveTTSAuditDto = new PriveTTSAuditDto();

								priveTTSAuditDto.setLastUpdatedDate(new Date(rs.getTimestamp(1).getTime()));
								priveTTSAuditDto.setLanguage(rs.getString(2));
								priveTTSAuditDto.setFundCode(rs.getString(3));
								priveTTSAuditDto.setFundName(rs.getString(4));
								priveTTSAuditDto.setIsin(rs.getString(5));

								list.add(priveTTSAuditDto);
							}
							return list;
						}
					});

		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("No Audit data found in prive DB...");
			LOGGER.error(e);
		}

		return priveTTSAuditDtos;
	}

	@Override
	public void saveTTSText(String key, String ttsText, String locale) throws Exception {

		// based on locale get LangList from Enum and then update TTS per key
		// per locale if TTS changed
		List<String> localeList = TTSLanguageEnum.getLangListByLocale(locale);

		for (String locale1 : localeList) {
			LOGGER.info("DAO saveTTSText - Current locale is ==> " + locale1);

			Map<String, Object> fetchTtsTextMap = new HashMap<String, Object>();
			fetchTtsTextMap = jdbcTemplate.queryForMap(TTSConstants.FETCH_TTS_TEXT, key, key, locale1);

			LOGGER.info("ttsText from PDF ===> " + ttsText);
			LOGGER.info("fetchTtsTextMap.get(\"TTS_TEXT\") from DB ===> " + fetchTtsTextMap.get("TTS_TEXT"));

			if (null != ttsText && ttsText.equalsIgnoreCase(String.valueOf(fetchTtsTextMap.get("TTS_TEXT")))) {
				LOGGER.info("New Content is same as existing through fetch and compare...NO NEED TO UPDATE");

			} else {
				LOGGER.info(
						"New Content is either null or different than existing through fetch and compare...UPDATE TTS if not null");

				if (null != ttsText) {
					jdbcTemplate.update(TTSConstants.SAVE_TTS_TEXT, ttsText, key, key, locale1);
					// TODO - log change in DB...
					jdbcTemplate.update(TTSConstants.SAVE_TTS_CHANGE_LOG, key, locale1, fetchTtsTextMap.get("TTS_TEXT"), ttsText);
					LOGGER.info("LOGGED TTS change in DB for key: " + key + " and locale: " + locale1);
				}
			}
		}
	}

	@Override
	public List<String> fetchAllKeys() throws Exception {

		List<String> strategyKeys = new ArrayList<String>();
		List<Map<String, Object>> sqlKeysMap = null;

		sqlKeysMap = jdbcTemplate
				.queryForList("SELECT DISTINCT STRATEGY_KEY FROM CITI_FUND_ASSET WHERE STRATEGY_KEY IS NOT NULL");

		System.out.println("sqlKeysMap size: => " + sqlKeysMap.size());

		for (int i = 0; i < sqlKeysMap.size(); i++) {
			strategyKeys.add(String.valueOf(sqlKeysMap.get(i).get("STRATEGY_KEY")));
		}

		return strategyKeys;
	}

	@Override
	public List<Map<String, Object>> fetchAllKeysWithName() throws Exception {

		List<Map<String, Object>> sqlKeysMap = null;
		sqlKeysMap = jdbcTemplate.queryForList(
				"SELECT DISTINCT STRATEGY_KEY, CITI_FUND_NAME FROM CITI_FUND_ASSET WHERE STRATEGY_KEY IS NOT NULL");

		return sqlKeysMap;
	}

	//
	// used for reference
	@Override
	public List<PriveFundListTTS> fetchTTSData() {

		List<PriveFundListTTS> citiFundListTtsList = new ArrayList<PriveFundListTTS>();

		String sql = "SELECT * FROM dev1.CITIFUNDLIST_TTS";

		citiFundListTtsList = jdbcTemplate.query(sql, new ResultSetExtractor<List<PriveFundListTTS>>() {

			@Override
			public List<PriveFundListTTS> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<PriveFundListTTS> list = new ArrayList<PriveFundListTTS>();

				while (rs.next()) {
					PriveFundListTTS citiFundTts = new PriveFundListTTS();

					citiFundTts.setIsin(rs.getString(1));
					citiFundTts.setFundName(rs.getString(2));
					citiFundTts.setFundCode(rs.getString(3));
					citiFundTts.setSessionPin(rs.getInt(4));
					citiFundTts.setLanguage(rs.getString(5));
					citiFundTts.setTtsFileLocation(rs.getString(6));
					citiFundTts.setCreatedDate(rs.getDate(7));
					citiFundTts.setLastUpdateDate(rs.getDate(8));

					list.add(citiFundTts);
				}
				return list;
			}
		});

		return citiFundListTtsList;
	}

}
