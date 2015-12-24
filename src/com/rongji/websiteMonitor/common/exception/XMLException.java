package com.rongji.websiteMonitor.common.exception;

/**
 * XML操作异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class XMLException extends Exception{
	/**  */
	private static final long serialVersionUID = -5053145782509243469L;

	public XMLException() {
		super();
	}

	public XMLException(String message) {
		super(message);
	}

	public XMLException(Throwable cause) {
		super(cause);
	}

	public XMLException(String message, Throwable cause) {
		super(message, cause);
	}
}
