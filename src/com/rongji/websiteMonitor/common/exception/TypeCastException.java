package com.rongji.websiteMonitor.common.exception;

/**
 * 类型转换异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class TypeCastException extends Exception {
	private static final long serialVersionUID = 8466868552524922729L;

	public TypeCastException() {
		super("");
	}

	public TypeCastException(String message) {
		super(message);
	}
}
