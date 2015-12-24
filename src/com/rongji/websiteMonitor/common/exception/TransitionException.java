/*
 * TransitionException.java	1.0.0
 *
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.<br>
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 数据类型转换
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class TransitionException extends Exception{
	/**  */
	private static final long serialVersionUID = -2136747088822401883L;

	public TransitionException() {
		super();
	}

	public TransitionException(String message) {
		super(message);
	}

	public TransitionException(Throwable cause) {
		super(cause);
	}

	public TransitionException(String message, Throwable cause) {
		super(message, cause);
	}
}
