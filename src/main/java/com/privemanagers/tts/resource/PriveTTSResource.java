/**
 * 
 */
package com.privemanagers.tts.resource;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.privemanagers.tts.controller.PriveTTSController;
import com.privemanagers.tts.dto.TwilioDaoDto;
import com.privemanagers.tts.dto.TwilioErrorResponse;
import com.privemanagers.tts.dto.TwilioResponse;
import com.privemanagers.tts.dto.TwilioSayResponse;
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
	public @ResponseBody TwilioResponse handleTwilioRequest(HttpServletRequest request) throws Exception {

		LOGGER.info("Twilio request is received for PIN => " + request.getParameter("Digits"));

		final String pin = request.getParameter("Digits");
		TwilioDaoDto twilioDaoDto = null;

		try {
			twilioDaoDto = citiTTSService.fetchDataForTwilio(Integer.parseInt(pin));

		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException...");
			final TwilioSayResponse sayResponse = new TwilioSayResponse();
			sayResponse.setVoice("alice");
			sayResponse.setLanguage("en");
			sayResponse.setContent("Please enter correct PIN");

			final TwilioResponse twilioResponse = new TwilioResponse();
			// twilioResponse.setId(Integer.parseInt(pin));
			twilioResponse.setSay(sayResponse);
			return twilioResponse;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("EmptyResultDataAccessException...");
			final TwilioSayResponse sayResponse = new TwilioSayResponse();
			sayResponse.setVoice("alice");
			sayResponse.setLanguage("en");
			sayResponse.setContent(
					"Your PIN has not been found in Prive system. Please either enter a correct PIN or generate a new PIN and try again.");

			final TwilioResponse twilioResponse = new TwilioResponse();
			// twilioResponse.setId(Integer.parseInt(pin));
			twilioResponse.setSay(sayResponse);
			return twilioResponse;
		}

		twilioDaoDto.setSessionPin(Integer.parseInt(pin));
		twilioDaoDto.setCallerMobileNo(request.getParameter("Caller"));

		// LOGGER.info("ttsText from DB: => " + twilioDaoDto.getTtsText());

		final TwilioSayResponse sayResponse = new TwilioSayResponse();
		sayResponse.setVoice("alice");
		sayResponse.setLanguage(twilioDaoDto.getLanguage());

		String content = twilioDaoDto.getTtsText();
		if (null != content) {
			sayResponse.setContent(content);
		} else {
			sayResponse.setContent(
					"Sorry no information found in Prive system related to your fund in selected language.");
			sayResponse.setLanguage("en");
		}

		final TwilioResponse twilioResponse = new TwilioResponse();
		twilioResponse.setSay(sayResponse);

		// Flush the corresponding PIN from DB.
		citiTTSService.removeSessionPin(pin);

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
		TwilioSayResponse sayResponse = new TwilioSayResponse();
		// sayResponse.setLanguage("US_en");
		sayResponse.setVoice("alice");
		sayResponse.setContent("There is an error processing your request. Please try again. " + ex.getMessage());
		errorResponse.setSay(sayResponse);

		return new ResponseEntity<TwilioErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
