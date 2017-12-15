/**
 *
 */
package com.privemanagers.tts.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.privemanagers.tts.dto.MyPropertyChangeListener;
import com.privemanagers.tts.dto.PriveTTSDaoDto;
import com.privemanagers.tts.exception.AjaxCallException;
import com.privemanagers.tts.exception.NoMappingFoundForISINException;
import com.privemanagers.tts.service.IPriveTTSService;
import com.privemanagers.tts.util.TTSConstants;
import com.privemanagers.tts.util.TTSLanguageEnum;

/**
 * @author Mangesh K.
 *
 *         Purpose: A front controller which will be called first when Citi-User
 *         clicks on the Permalink provided by Prive. It is responsible to
 *         redirect an user to an external window wherein User can select a
 *         language and take further actions.
 */
@Controller
public class PriveTTSController {

	@Autowired
	private IPriveTTSService citiTTSService;

	private static PriveTTSDaoDto daoDto;
	private static MyPropertyChangeListener propertyChangeListener;

	private static final Logger LOGGER = Logger.getLogger(PriveTTSController.class);

	/**
	 * @param isin
	 *            - Unique No. to identify a Citi-Fund
	 * @param modelMap
	 * @return - External window view name to which user will be redirected
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handlePermalinkRequest(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "isin", required = false) String isin, ModelMap modelMap) throws Exception {

		LOGGER.info("Permalink request is received for key: " + key);
		validateRequest(key, isin);

		// Refresh PriveTTSDaoDto object for each request
		daoDto = new PriveTTSDaoDto();

		// Add property event handler related code
		propertyChangeListener = new MyPropertyChangeListener();
		daoDto.addPropertyChangeListener(propertyChangeListener);

		// fetch twilioNo from DB and save it in modelMap to be used in View
		String twilioNo = null;
		twilioNo = citiTTSService.fetchTwilioNumber();
		modelMap.addAttribute("twilioNo", twilioNo);

		// Add request attributes to Model to access them in View if needed
		modelMap.addAttribute("key", key);
		modelMap.addAttribute("isin", isin);

		modelMap.addAttribute("commonDto", daoDto);

		// Set ISIN to DaoDTO in order to save it in DB later
		daoDto.setKey(key);

		return "citi-tts";
	}

	/**
	 *
	 * @param isin
	 * @param strategyKey
	 * @throws Exception
	 */
	private void validateRequest(String key, String isin) throws Exception {

		if (null != key && null != isin) {
			throw new Exception("Invalid permalink Request. Pass exactly one parameter in request.");
		}

		if (null == key && null == isin) {
			throw new Exception("Invalid permalink Request. No parameter received in request.");
		}
	}

	/**
	 *
	 * @param lang
	 *            - Request parameter received as part of an AJAX call
	 * @return pin - 3 digit random pin to be shown on screen as part of a
	 *         language selection tab
	 */
	@RequestMapping(value = "/generateAndSavePin", method = RequestMethod.GET)
	public @ResponseBody String generateAndSaveSessionPIN(@RequestParam("lang") String lang) throws Exception {

		int sessionPin = 0;
		try {
			// Generate a 3 digit PIN
			Random random = new Random();
			sessionPin = 100 + random.nextInt(900);

			// save PIN to DB
			daoDto.setLanguage(lang);
			daoDto.setSessionPin(sessionPin);
			citiTTSService.saveTTSData(daoDto);

			// Update initial Audit data
			citiTTSService.saveTTSAuditLog(daoDto);

			LOGGER.info("Permalink controller PIN generation and Audit Log save is completed...");

		} catch (NoMappingFoundForISINException cusex) {
			LOGGER.error("ISIN not found in DB: " + cusex.getMessage());
			throw new AjaxCallException(cusex.getMessage());
			// return cusex.getMessage();

		} catch (Exception e) {
			throw new AjaxCallException(e.getMessage());
		}

		return String.valueOf(sessionPin);
	}

	/**
	 * The purpose of this method is to allow user to download KFS fund sheet
	 * PDF file from external window.
	 *
	 * @param response
	 *            - Redirect to external KFS URL
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadKFS", method = RequestMethod.GET)
	public @ResponseBody String downloadKeyFactSheetPDF(HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) throws Exception {

		String strategyKey = daoDto.getKey();

		String kfsFileUrl = TTSConstants.PRODUCTION ? TTSConstants.DOWNLOAD_URL_PROD : TTSConstants.DOWNLOAD_URL_DEV;
		kfsFileUrl = kfsFileUrl.replace(TTSConstants.TAG_STRATEGY_KEY, strategyKey);

		String selectedLocale = TTSLanguageEnum.getLocaleByLangValue(daoDto.getLanguage());
		kfsFileUrl = kfsFileUrl.replace(TTSConstants.TAG_SELECTED_LOCALE, selectedLocale);

		LOGGER.info("Downloading KFS request received for PIN:" + daoDto.getSessionPin() + ", KFS URL: " + kfsFileUrl);

		return kfsFileUrl;
	}

	/**
	 * Purpose of this method is to handle all uncaught Exceptions during
	 * request processing
	 *
	 * @param request
	 *            - To capture request attributes such as URL for logging
	 *            purpose
	 * @param ex
	 *            - To capture the error cause/trace for logging purpose
	 * @return - Redirect to error page with proper error message
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest request, Exception ex) {

		LOGGER.error("Error Occurred in PriveTTSController for request URL: " + request.getRequestURL(), ex);

		ModelAndView model = new ModelAndView("error");
		model.addObject("exception", ex);
		model.addObject("url", request.getRequestURL());
		return model;
	}

	@ExceptionHandler(AjaxCallException.class)
	public @ResponseBody String handleAjaxCallError(HttpServletRequest request, Exception ex) {

		LOGGER.error("Error Occurred in PriveTTSController for generateAndSaveSessionPIN Ajax request: "
				+ request.getRequestURL(), ex);

		ModelAndView model = new ModelAndView();
		model.addObject("exception", ex);
		model.addObject("url", request.getRequestURL());
		model.setViewName("error");
		return "Error";
	}

	/**
	 * To be called from Outside
	 * 
	 * @return
	 */
	public PriveTTSDaoDto getPriveTTSDaoDto() {
		return daoDto;
	}

	/**
	 * TO be called via ajax call to know when the PIn has been flushed from DB
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getPinStatus", method = RequestMethod.GET)
	public @ResponseBody String getPinChangeStatus() {

		while (propertyChangeListener != null && !propertyChangeListener.isPinFlushed()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return "OK";
	}
}
