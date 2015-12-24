/*
 * InternalErrorException.java
 * 
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 * 
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 内部错误 
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class InternalException extends Exception{

	private static final long serialVersionUID=-7602780450333802613L;
	
	/**
	 * 内部错误
	 *
	 */
	public InternalException(){
		super();
	}
	
	/**
	 * 内部错误
	 * 
	 * @param strErr
	 * 					异常信息
	 */
	public InternalException(String strErr){
		super(strErr);
	}
}
