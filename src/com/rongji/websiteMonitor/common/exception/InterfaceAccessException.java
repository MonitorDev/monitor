/*
 * InterfaceAccessException.java	1.0.0
 *
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.<br>
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 接口访问异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class InterfaceAccessException extends Exception{
	/**  */
	private static final long serialVersionUID=-4666321476276689093L;

	public InterfaceAccessException(){
		super();
	}
	
	public InterfaceAccessException(String message){
		super(message);
	}
	
	public InterfaceAccessException(Throwable cause){
		super(cause);
	}
	
	public InterfaceAccessException(String message,Throwable cause){
		super(message,cause);
	}
}
