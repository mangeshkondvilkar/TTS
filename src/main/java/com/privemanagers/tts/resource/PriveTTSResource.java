/**
 * 
 */
package com.privemanagers.tts.resource;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.privemanagers.tts.controller.PriveTTSController;
import com.privemanagers.tts.dto.TwilioDaoDto;
import com.privemanagers.tts.dto.TwilioErrorResponse;
import com.privemanagers.tts.dto.TwilioPauseTag;
import com.privemanagers.tts.dto.TwilioResponse;
import com.privemanagers.tts.dto.TwilioSayTag;
import com.privemanagers.tts.dto.XmlObjectFactory;
import com.privemanagers.tts.service.IPriveTTSService;

/**
 * @author Mangesh K
 *
 */
@RestController
@RequestMapping("/twilio")
public class PriveTTSResource {

	@Autowired
	private IPriveTTSService citiTTSService;

	@Autowired
	private XmlObjectFactory xmlObjectFactory;
	
	@Autowired
	private PriveTTSController priveTTSController;

	private static final Logger LOGGER = Logger.getLogger(PriveTTSController.class);

	/**
	 * This Resource is meant to be called from Prive Twilio App in oder to get
	 * TTS file content in TwilML response format to be recited over phone.
	 * 
	 * @param request
	 *            - HttpRequest to capture request attributes received from
	 *            twilio request
	 * @param pin
	 * @return - Returns a TwiML response for twilio to process
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTtsResponse", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody TwilioResponse handleTwilioRequest(HttpServletRequest request, ModelMap twilioModelMap)
			throws Exception {

		LOGGER.info("Twilio request is received for PIN => " + request.getParameter("Digits"));

		final String pin = request.getParameter("Digits");
		TwilioDaoDto twilioDaoDto = null;

		try {
			twilioDaoDto = citiTTSService.fetchDataForTwilio(Integer.parseInt(pin));

		} catch (NumberFormatException e) {
			LOGGER.error("NumberFormatException in handleTwilioRequest...");

			final List<Object> xmlElementList = new ArrayList<Object>();

			final TwilioSayTag sayTag = new TwilioSayTag();
			sayTag.setVoice("alice");
			sayTag.setLanguage("en");
			sayTag.setContent("Please enter correct PIN");
			xmlElementList.add(xmlObjectFactory.createSayTag(sayTag));

			final TwilioPauseTag pauseTag = new TwilioPauseTag();
			pauseTag.setLength("2");
			xmlElementList.add(xmlObjectFactory.createPauseTag(pauseTag));

			final TwilioResponse twilioResponse = new TwilioResponse();
			twilioResponse.setObject(xmlElementList);

			return twilioResponse;
		} catch (EmptyResultDataAccessException e) {
			LOGGER.error("EmptyResultDataAccessException in handleTwilioRequest...");

			final List<Object> xmlElementList = new ArrayList<Object>();

			final TwilioSayTag sayTag = new TwilioSayTag();
			sayTag.setVoice("alice");
			sayTag.setLanguage("en");
			sayTag.setContent(
					"Your PIN has not been found in Prive system. Please either enter a correct PIN or generate a new PIN and try again.");
			xmlElementList.add(xmlObjectFactory.createSayTag(sayTag));

			final TwilioPauseTag pauseTag = new TwilioPauseTag();
			pauseTag.setLength("2");
			xmlElementList.add(xmlObjectFactory.createPauseTag(pauseTag));

			final TwilioResponse twilioResponse = new TwilioResponse();
			twilioResponse.setObject(xmlElementList);

			return twilioResponse;
		}

		twilioDaoDto.setSessionPin(Integer.parseInt(pin));
		twilioDaoDto.setCallerMobileNo(request.getParameter("Caller"));

		final TwilioResponse twilioResponse = new TwilioResponse();
		final List<Object> xmlElementList = new ArrayList<Object>();

		String content = twilioDaoDto.getTtsText();

		if (null != content && !"".equalsIgnoreCase(content)) {
			content = content.replaceAll("\\.", "....");
			// Read line by line --> form a <Say> tag for each line --> add a
			// pause.
			BufferedReader br = new BufferedReader(new StringReader(content));
			String line = null;

			while (null != (line = br.readLine())) {
				if (line.length() > 0) {

					final TwilioSayTag say = new TwilioSayTag();
					say.setVoice("alice");
					say.setLanguage(twilioDaoDto.getLanguage());
					say.setContent(line);
					xmlElementList.add(xmlObjectFactory.createSayTag(say));

					// Add pause of 1 seconds
					final TwilioPauseTag pause = new TwilioPauseTag();
					pause.setLength("2");
					xmlElementList.add(xmlObjectFactory.createPauseTag(pause));
				}
			}

			twilioResponse.setObject(xmlElementList);
		} else {
			LOGGER.info("Sorry no information found in Prive system related to your fund in selected language....");

			final List<Object> xmlElementListError = new ArrayList<Object>();
			final TwilioSayTag say = new TwilioSayTag();
			say.setVoice("alice");
			say.setLanguage("en");
			say.setContent("Sorry no information found in Prive system related to your fund in selected language.");
			xmlElementListError.add(xmlObjectFactory.createSayTag(say));

			// Add pause of 1 seconds
			final TwilioPauseTag pause = new TwilioPauseTag();
			pause.setLength("2");
			xmlElementListError.add(xmlObjectFactory.createPauseTag(pause));

			twilioResponse.setObject(xmlElementListError);
		}

		// Flush the corresponding PIN from DB.
		citiTTSService.removeSessionPin(pin);
		//
		priveTTSController.getPriveTTSDaoDto().setSessionPin(0);

		// Update Audit log table
		citiTTSService.updateAuditLog(twilioDaoDto);

		return twilioResponse;
	}

	/**
	 * Purpose of this method is to handle all uncaught Exceptions during twilio
	 * request processing
	 * 
	 * @param request
	 *            - To capture request attributes such as URL for logging
	 *            purpose
	 * @param ex
	 *            - To capture the error cause/trace for logging purpose
	 * @return - Returns a proper HTTP status code and message
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<TwilioErrorResponse> handleTwilioError(HttpServletRequest request, Exception ex) {

		LOGGER.error("Error Occurred in Twilio request processing for URL: " + request.getRequestURL(), ex);

		TwilioErrorResponse errorResponse = new TwilioErrorResponse();
		TwilioSayTag sayResponse = new TwilioSayTag();
		// sayResponse.setLanguage("US_en");
		sayResponse.setVoice("alice");
		sayResponse.setContent("There is an error processing your request. Please try again. " + ex.getMessage());
		errorResponse.setSay(sayResponse);

		return new ResponseEntity<TwilioErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
