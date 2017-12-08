/**
 *
 */
package com.privemanagers.tts.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mangesh K
 *
 */
public enum TTSLanguageEnum {

	ENG("en", "en"), CAN("zh-HK", "zh_TW"), MAN("zh-CN", "zh_TW");

	private final String langValue;
	private final String locale;

	private TTSLanguageEnum(final String langValue, String locale) {
		this.langValue = langValue;
		this.locale = locale;
	}

	public String value() {
		return this.langValue;
	}

	public String getLocale() {
		return this.locale;
	}

	public static String getLocaleByLangValue(String langValue) {
		for (TTSLanguageEnum language : TTSLanguageEnum.values()) {
			if (language.value().equals(langValue)) {
				return language.getLocale();
			}
		}
		return null;
	}

	public static Set<String> getLocales() {
		Set<String> locales = new HashSet<String>();

		for (TTSLanguageEnum language : TTSLanguageEnum.values()) {
			locales.add(language.getLocale());
		}

		return locales;
	}

	public static List<String> getLangListByLocale(String locale) {
		List<String> langList = new ArrayList<String>();

		for (TTSLanguageEnum language : TTSLanguageEnum.values()) {

			if (language.getLocale().equals(locale)) {
				langList.add(language.value());
			}
		}

		return langList;
	}

}
