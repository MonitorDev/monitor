/*
 * TemplateParseException.java
 * 
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 * 
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 模板解析异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class ParseException extends Exception{
	/**  */
	private static final long serialVersionUID=-2668203556855647886L;

	public ParseException(){
		super();
	}
	
	public ParseException(String message){
		super(message);
	}
	
	public ParseException(Throwable cause){
		super(cause);
	}
	
	public ParseException(String message,Throwable cause){
		super(message,cause);
	}

}
