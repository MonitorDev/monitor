/*
 * OperateDateException.java	1.0.0
 *
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.<br>
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 操作数据错误
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class OperateDateException extends Exception{
	/**  */
	private static final long serialVersionUID=6189255299890714300L;

	public OperateDateException(){
		super();
	}
	
	public OperateDateException(String message){
		super(message);
	}
	
	public OperateDateException(Throwable cause){
		super(cause);
	}
	
	public OperateDateException(String message,Throwable cause){
		super(message,cause);
	}
}
