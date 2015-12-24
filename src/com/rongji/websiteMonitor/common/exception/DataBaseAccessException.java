package com.rongji.websiteMonitor.common.exception;

/**
 * 数据库访问异常
 * 
 * @author	RJ-CMS7 Team
 * @version	1.0.0
 * @since	1.0.0	HQJ		2009-10-23
 */
public class DataBaseAccessException extends Exception{
	/**  */
	private static final long serialVersionUID=-665887495598710428L;

	public DataBaseAccessException(){
		super();
	}
	
	public DataBaseAccessException(String message){
		super(message);
	}
	
	public DataBaseAccessException(Throwable cause){
		super(cause);
	}
	
	public DataBaseAccessException(String message,Throwable cause){
		super(message,cause);
	}
}
