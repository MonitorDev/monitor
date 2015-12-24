package com.rongji.websiteMonitor.common;


public class Constants {

	public static final String OPTION_IS_YES = "1";
	public static final String OPTION_IS_NO = "0";
	public static final int POOL_SIZE = 5;

	/** http类型 */
	public static final String HTTP_TYPE = "http";
	/** ping类型 */
	public static final String PING_TYPE = "ping";
	/** snmp类型*/
	public static final String SNMP_TYPE = "snmp";

	
	/** 可用 */
	public static final String IS_USE = "0";
	/** 不可用 */
	public static final String IS_UNUSE = "1";
	/** 邮件类型 */
	public static final String EMAIL_TYPE = "eMail";

	/** phone类型 */
	public static final String PHONE_TYPE = "phone";

	/** 归档的级别（0：未归档 1：每日归档 2：每月归档 3：每年归档） */
	public static final String ARCHIVE_LEVEL_NONR = "0";
	public static final String ARCHIVE_LEVEL_DAY = "1";
	public static final String ARCHIVE_LEVEL_MONTH = "2";
	public static final String ARCHIVE_LEVEL_YEAR = "3";

	/**
	 * 监测类型
	 * 
	 * @author GYB
	 * 
	 */
	public enum MonitorTYPE {

		HTTP, PING, OTHER;

		public static MonitorTYPE toTYPE(String str) {

			try {

				return valueOf(str);

			} catch (Exception e) {

				return OTHER;

			}

		}

	}

}
