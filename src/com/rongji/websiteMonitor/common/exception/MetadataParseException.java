/*
 * MetadataParseException.java
 * 
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 * 
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 源数据解析异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class MetadataParseException extends Exception{

	/**  */
	private static final long serialVersionUID=564035847402862229L;

	public MetadataParseException(){
		super();
	}
	
	public MetadataParseException(String message){
		super(message);
	}
	
	public MetadataParseException(Throwable cause){
		super(cause);
	}
	
	public MetadataParseException(String message,Throwable cause){
		super(message,cause);
	}
}
