/**
 *
 */
package com.privemanagers.tts.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.privemanagers.tts.dto.PriveFundListTTS;
import com.privemanagers.tts.dto.PriveTTSAuditDto;
import com.privemanagers.tts.dto.PriveTTSDaoDto;
import com.privemanagers.tts.dto.TwilioDaoDto;

/**
 * @author Mangesh K
 *
 */
@Repository
public interface IPriveTTSDao {

	public void saveTTSData(PriveTTSDaoDto citiDaoDto) throws Exception;

	public List<PriveFundListTTS> fetchTTSData();

	public String fetchTwilioNumber() throws Exception;

	public String fetchKFSFileURL(int pin) throws Exception;

	public void removeSessionPin(String pin) throws Exception;

	public TwilioDaoDto fetchDataForTwilio(int pin);

	public void saveTTSAuditLog(PriveTTSDaoDto daoDto) throws Exception;

	public void updateAuditLog(TwilioDaoDto twilioDaoDto) throws Exception;

	public List<PriveTTSAuditDto> fetchTTSAuditLog(String fromDate, String toDate) throws Exception;

	public void saveTTSText(String key, String ttsText, String locale) throws Exception;

	public List<String> fetchAllKeys() throws Exception;

	public List<Map<String, Object>> fetchAllKeysWithName() throws Exception;
}
