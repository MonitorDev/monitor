/*
 * SequenceException.java	1.0.0
 *
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.<br>
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.rongji.websiteMonitor.common.exception;

/**
 * 序列号管理器异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class SequenceException extends Exception{
	/**  */
	private static final long serialVersionUID = -4768395046638538517L;

	public SequenceException() {
		super();
	}

	public SequenceException(String message) {
		super(message);
	}

	public SequenceException(Throwable cause) {
		super(cause);
	}

	public SequenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
