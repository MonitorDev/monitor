/*
 * PermitionAccessException.java
 * 
 * Copyright 2009 Rongji Enterprise, Inc. All rights reserved.
 * 
 * Rongji PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 */
package com.rongji.websiteMonitor.common.exception;
/**
 * 权限访问异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class PermitionAccessException extends Exception{
	/**  */
	private static final long serialVersionUID = 7683519914088699237L;

	public PermitionAccessException() {
		super();
	}

	public PermitionAccessException(String message) {
		super(message);
	}

	public PermitionAccessException(Throwable cause) {
		super(cause);
	}

	public PermitionAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
