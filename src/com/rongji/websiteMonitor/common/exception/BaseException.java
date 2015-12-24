package com.rongji.websiteMonitor.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * 
 * 基类异常
 * 
 * @author  HQJ
 * @version 1.0
 * @since   1.0.0 HQJ 2011-08-17
 */
public class BaseException extends RuntimeException {
	/** Serial version UID required for safe serialization. */
	private static final long serialVersionUID = -2253849926349514915L;

	protected int errNum = 0; // 错误编号
	
	protected Throwable cause = null;
	
	public BaseException() {
		super();
	}
	
	public BaseException(int errNum) {
		this.errNum = errNum;
	}
	
	public BaseException(String message) {
		super(message);
	}

	public BaseException(int errNum, String message) {
		super(message);
		this.errNum = errNum;
	}

	public BaseException(int errNum, String message, Throwable cause) {
		super(message, cause);
		this.errNum = errNum;
		this.cause = cause;
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
		this.cause = cause;
	}

	public int getErrNum() {
		return this.errNum;
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getErrNumMsg() {
		return ExceptionNumber.getErrNumMsg(this.errNum);
	}

	public String getMyMessage() {
		return super.getMessage();
	}

	public String toString() {
		String message = "[ERR-" + errNum + "] " + getMyMessage();
		return message;
	}

	public String getMessage() {
		String sMessage = toString();
		if (cause != null)
			sMessage = sMessage + "\r\n<-- " + cause.toString();
		return sMessage;
	}

	public String getLocalizedMessage() {
		return getMessage();
	}

	public void printStackTrace(PrintStream ps) {
		if (this.cause == null) {
			//printStackTrace(ps);
		} else {
			Throwable root = this.cause;
			synchronized (ps) {
				ps.println(toString());
				Throwable temp = null;
				while (root instanceof BaseException) {
					ps.println("<-- " + root.toString());
					temp = root;
					root = ((BaseException) root).getCause();
					if (root == null) {
						temp.printStackTrace(ps);
						break;
					}
				}
				if (root != null) {
					ps.print("<-- ");
					root.printStackTrace(ps);
				}
			}
		}
	}

	public void printStackTrace(PrintWriter pw) {
		if (this.cause == null) {
			//printStackTrace(pw);
		} else {
			Throwable root = this.cause;
			synchronized (pw) {
				pw.println(toString());
				Throwable preRoot = null;
				while (root instanceof BaseException) {
					pw.print("<-- ");
					preRoot = root;
					root = ((BaseException) root).getCause();
					if (root == null) {
						preRoot.printStackTrace(pw);
						break;
					}
					pw.println(preRoot.toString());
				}
				if (root != null) {
					pw.print("<-- ");
					root.printStackTrace(pw);
				}
			}
		}
	}

	public String getStackTraceText() {
		return getStackTraceText(this.cause);
	}

	public static String getStackTraceText(Throwable ex) {
		StringWriter stringWriter = null;
		PrintWriter printWriter = null;
		Exception ex2;
		String s = "";
		if (ex != null) {
			try {
				stringWriter = new StringWriter();
				printWriter = new PrintWriter(stringWriter);
				ex.printStackTrace(printWriter);
				printWriter.flush();
				s = stringWriter.toString();
			} finally {
				if (stringWriter != null)
					try {
						stringWriter.close();
					} catch (Exception e) {
					}
				if (printWriter != null)
					try {
						printWriter.close();
					} catch (Exception e) {
					}
			}
		} 
		return s;
	}

	public static final void main(String args[]) throws CmsException {
		throw new CmsException(10,  "获取ID为的栏目失败,请刷新站点栏目树！");
//		BaseException fire0 = new BaseException(20, "当前文档为空，无法删除！");
////	    BaseException fire = new BaseException(1, "my exception 1", fire0);
////		BaseException fire2 = new BaseException(10, "my exception 2", fire);
//		fire0.printStackTrace(System.out);
//		System.out.println("-------------------");
//		System.out.println(fire0.getMessage());
//		System.out.println("-------------------");
//		System.out.println(fire0.getStackTraceText());
////		try {
//			int a = 0;
//			int b = 1 / a;
////		System.out.println(b);
//			Long.parseLong("0.0");
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//			System.out.println("-------------------");
//			System.out.println(getStackTraceText(ex));
//			System.out.println("-------------------");
//			ex.printStackTrace();
////			
//		}
	}

}
