package com.rongji.websiteMonitor.common.exception;

import org.apache.log4j.Logger;

/**
 * 
 * 系统内部错误异常
 * 
 * @author  HQJ
 * @version 1.0
 * @since   1.0.0 HQJ 2011-08-17
 */
public class CmsException extends BaseException {
	/** Serial version UID required for safe serialization. */
	private static final long serialVersionUID = -3387516993124229948L;

	private static final Logger logger = Logger.getLogger(CmsException.class);

	public CmsException() {
		super();
		this.errNum = ExceptionNumber.ERR_UNKNOWN; // 未知;
	}

	public CmsException(int errNum) {
		super(errNum);
	}

	public CmsException(String message) {
		super(message);
		this.errNum = 1000;
	}

	public CmsException(int errNum, String message) {
		super(errNum, message);
	}

	public CmsException(String message, Throwable cause) {
		super(message, cause);
		this.errNum = 1000;
	}

	public CmsException(int errNum, String message, Throwable cause) {
		super(errNum, message, cause);
	}

	public static void catchException(String message, Exception ex, boolean severity) throws CmsException {
		logger.error(message, ex);
		if (severity)
			throw new CmsException(1000, message, ex);
	}

}
