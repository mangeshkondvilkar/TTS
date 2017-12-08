/**
 *
 */
package com.privemanagers.tts.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.privemanagers.tts.dao.IPriveTTSDao;
import com.privemanagers.tts.dto.PriveFundListTTS;
import com.privemanagers.tts.dto.PriveTTSAuditDto;
import com.privemanagers.tts.dto.PriveTTSDaoDto;
import com.privemanagers.tts.dto.TwilioDaoDto;

/**
 * @author Mangesh K
 *
 */
@Service
public class PriveTTSServiceImpl implements IPriveTTSService {

	@Autowired
	IPriveTTSDao citiTTSDao;

	@Override
	@Transactional
	public void saveTTSData(PriveTTSDaoDto citiDaoDto) throws Exception {
		citiTTSDao.saveTTSData(citiDaoDto);
	}

	@Override
	@Transactional
	public String fetchTwilioNumber() throws Exception {
		return citiTTSDao.fetchTwilioNumber();
	}

	@Override
	@Transactional
	public String fetchKFSFileURL(int pin) throws Exception {
		return citiTTSDao.fetchKFSFileURL(pin);
	}

	@Override
	@Transactional
	public void removeSessionPin(String pin) throws Exception {
		citiTTSDao.removeSessionPin(pin);
	}

	@Override
	@Transactional
	public TwilioDaoDto fetchDataForTwilio(int pin) {
		return citiTTSDao.fetchDataForTwilio(pin);
	}

	@Override
	@Transactional
	public void saveTTSAuditLog(PriveTTSDaoDto daoDto) throws Exception {
		citiTTSDao.saveTTSAuditLog(daoDto);
	}

	@Override
	@Transactional
	public void updateAuditLog(TwilioDaoDto twilioDaoDto) throws Exception {
		citiTTSDao.updateAuditLog(twilioDaoDto);
	}

	@Override
	public List<PriveFundListTTS> fetchTTSData() {
		return citiTTSDao.fetchTTSData();
	}

	@Override
	@Transactional
	public List<PriveTTSAuditDto> fetchTTSAuditLog(String fromDate, String toDate) throws Exception {
		return citiTTSDao.fetchTTSAuditLog(fromDate, toDate);
	}

	@Override
	@Transactional
	public void saveTTSText(String key, String ttsText, String locale) throws Exception{
		citiTTSDao.saveTTSText(key, ttsText, locale);
	}

	@Override
	@Transactional
	public List<String> fetchAllKeys() throws Exception {
		return citiTTSDao.fetchAllKeys();
	}

	@Override
	public List<Map<String, Object>> fetchAllKeysWithName() throws Exception {
		return citiTTSDao.fetchAllKeysWithName();
	}

}
