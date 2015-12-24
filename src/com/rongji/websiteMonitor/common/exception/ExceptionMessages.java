package com.rongji.websiteMonitor.common.exception;

import java.util.MissingResourceException;

public class ExceptionMessages {
	private static final String BUNDLE_NAME = "exception_messages";
	//private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("exception_messages");

	public static String getString(int errorNumber) {
		String key = "ExNum." + String.valueOf(errorNumber);
		try {
//			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
		}
		return '!' + key + '!';
	}
}
