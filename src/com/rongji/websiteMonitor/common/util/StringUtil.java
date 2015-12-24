package com.rongji.websiteMonitor.common.util;

public class StringUtil {
	public static String toHtml(String content) {
		return toHtml(content, true);
	}
	
	/**
	 * 
	 * @param content
	 * @param changeBlank 是否替换空格符
	 * @return
	 */
	public static String toHtml(String content, boolean changeBlank) {
		if (content == null) {
			return "";
		}
		char[] srcBuff = content.toCharArray();
		int nSrcLen = srcBuff.length;
		StringBuffer retBuff = new StringBuffer(nSrcLen * 2);
		for (int i = 0; i < nSrcLen; ++i) {
			char cTemp = srcBuff[i];
			switch (cTemp) {
			case ' ': //空格符
				retBuff.append((changeBlank) ? "&nbsp;" : " ");
				break;
			case '<':
				retBuff.append("&lt;");
				break;
			case '>':
				retBuff.append("&gt;");
				break;
			case '\n': //换行符
				retBuff.append("<br>");
				break;
			case '\r': //回车符
				retBuff.append("<br/>");
			    if ((i + 1 < srcBuff.length) && (srcBuff[(i + 1)] == '\n')) {
			    	++i;
			    }
			    break;
			case '"': //引号符
				retBuff.append("&quot;");
				break;
			case '\'':
				retBuff.append("&#39;");
			    break;
			case '&':
				boolean bUnicode = false;
				for (int j = i + 1; (j < nSrcLen) && (!bUnicode); ++j) {
					cTemp = srcBuff[j];
					if ((cTemp == '#') || (cTemp == ';')) {
						retBuff.append("&");
						bUnicode = true;
					}
				}
				if (!bUnicode)
					retBuff.append("&amp;");
				break;
			case '\t': //制表符
				retBuff.append((changeBlank) ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ");
				break;
			default:
				retBuff.append(cTemp);
			}
		}
		return retBuff.toString();
	}
}
