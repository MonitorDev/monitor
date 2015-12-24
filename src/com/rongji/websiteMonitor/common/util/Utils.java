package com.rongji.websiteMonitor.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.CommandContainer;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.PanelContainer;
import com.rongji.dfish.engines.xmltmpl.View;
import com.rongji.dfish.engines.xmltmpl.XMLDecorator;
import com.rongji.dfish.engines.xmltmpl.XMLObject;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.CalendarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.ImagePanel;
import com.rongji.dfish.engines.xmltmpl.component.PortalPanel;
import com.rongji.dfish.engines.xmltmpl.component.TreePanel;
import com.rongji.dfish.framework.FrameworkHelper;

/**
 * 长期积累的通用方法类。
 * 
 * @author ITASK-team
 * 
 */
public class Utils {

	public static final String ENCODING = "UTF-8";
	// private static final Logger
	// LOGGER=SystemData.getLogger(CommonMethods.class);
	public static final String ENDLINE = "\r\n";

	public static String URLDecoder(String source) {
		return URLDecoder(source, ENCODING);
	}

	public static String URLEncoder(String source) {
		return URLEncoder(source, ENCODING);
	}

	public static String URLDecoder(String source, String encoding) {
		String res = null;
		try {
			res = java.net.URLDecoder.decode(source, encoding);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return res;
	}

	public static String URLEncoder(String source, String encoding) {
		if(source == null || "".equals(source)) {
			return source;
		}
		String res = null;
		try {
			res = java.net.URLEncoder.encode(source, encoding);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return res;
	}
	
	/**
	 * 输出XML内容 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param xml
	 *            String
	 */
	public static void outPutXML(HttpServletResponse response, XMLObject xo) {
		String xml = xo.asXML();
		HashSet<String> allIds = new HashSet<String>();
		if (xo instanceof View) {// FIXME DEBUG 专用，会影响效率，如果正式系统需要去除
			View view = (View) xo;
			Panel root = view.getRootPanel();
			checkPanel(allIds, root);
		} else if (xo instanceof Command) {
			checkCommand(allIds, (Command) xo);
		}
		if(FrameworkHelper.isDebugOn()) {
			System.out.println(new XMLDecorator(xml, false, false));//调试
		}
		outPutContent("<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>" + ENDLINE + xml, "text/xml", response);
	}

	private static void checkCommand(HashSet<String> allIds, Command p) {
		if (p instanceof CommandContainer) {
			CommandContainer cc = (CommandContainer) p;
			for (Command c : cc.getCommands()) {
				checkCommand(null, c);
			}
		} else if (p instanceof UpdateCommand) {
			UpdateCommand uc = (UpdateCommand) p;
			allIds = new HashSet<String>();
			if (uc.getContent() instanceof View) {
				View view = (View) uc.getContent();
				checkPanel(allIds, view.getRootPanel());
			} else if (UpdateCommand.TYPE_PANEL.equals(uc.getType())) {
				checkPanel(allIds, (Panel) uc.getContent());
			}
		}
	}

	private static void checkPanel(HashSet<String> allIds, Panel p) {
		if (p == null)
			return;
		String id = p.getId();
		if (id != null && !id.equals("")) {
			if (!allIds.add(id))
				throw new RuntimeException("一个view中不可以含有两个或以上相同的ID[" + id + "]");
		} else if (p instanceof FormPanel || p instanceof GridPanel || p instanceof TreePanel
				|| p instanceof PortalPanel || p instanceof ImagePanel || p instanceof CalendarPanel) {
			System.out.println(p);
			throw new RuntimeException("功能面板ID不可以为空");
		}
		if (p instanceof PanelContainer) {
			PanelContainer pc = (PanelContainer) p;
			for (Panel sp : pc.getSubPanels()) {
				checkPanel(allIds, sp);
			}
		}

	}

	/**
	 * 输出HTML内容 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 * @param html
	 */
	public static void outPutHTML(HttpServletResponse response, String html) {
		outPutContent(html, "text/html", response);
	}

	/**
	 * 输出文本内容 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 * @param text
	 */
	public static void outPutTEXT(HttpServletResponse response, String text) {
		outPutContent(text, "text/plain", response);
	}
	/**
	 * 输出文本内容 默认控制这个内容不会被本地缓存。
	 * 
	 * @param response
	 * @param text
	 */
	public static void outPutJson(HttpServletResponse response, String text) {
		outPutContent(text, "text/json", response);
	}
	/**
	 * 输出json内容
	 * @param response
	 * @param obj
	 */
//	public static void outPutJson(HttpServletResponse response, Object obj) {
//		if(obj == null) {
//			obj = "";
//		}
//		outPutJson(response, JsonUtils.toJson(obj, false));
//	}

	/**
	 * 校验一个字符串是否为空判断 改名为notEmpty LinLW // 2005-09-19 原名为validNull容易产生歧义
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 判断一个字符串为空或空串或全部有空格组成
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断一个数组是否非空	LZW 2012-11-02
	 * 
	 * @param l
	 * @return
	 */
	public static boolean notEmpty(Object [] l) {
		return l != null && l.length > 0;
	}
	/**
	 * 判断一个数组是否空 LZW 2012-11-02
	 * 
	 * @param l
	 * @return
	 */
	public static boolean isEmpty(Object [] l) {
		return l == null || l.length <= 0;
	}
	/**
	 * 判断一个Collection是否非空 ZHL 2007-07-28
	 * 
	 * @param l
	 * @return
	 */
	public static boolean notEmpty(Collection<?> l) {
		return l != null && l.size() > 0;
	}
	/**
	 * 判断一个Collection是否空 ZHL 2007-07-28
	 * 
	 * @param l
	 * @return
	 */
	public static boolean isEmpty(Collection<?> l) {
		return l == null || l.size() <= 0;
	}

	/**
	 * 提供XML转义字符
	 * 
	 * @param appendTo
	 *            StringBuffer
	 * @param src
	 *            String
	 */
	public static void escapeXMLword(StringBuilder appendTo, String src) {
		// return title.replaceAll("&", "&amp;")
		// .replaceAll("<", "&lt;")
		// .replaceAll(">", "&gt;")
		// .replaceAll("\"", "&quot;")
		// .replaceAll("'", "&#39;");
		// 不用上述写法是为了效率。
		if (src == null) {
			return;
		}
		char[] ca = src.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			switch (ca[i]) {
			case '&':
				appendTo.append("&amp;");
				break;
			case '<':
				appendTo.append("&lt;");
				break;
			case '>':
				appendTo.append("&gt;");
				break;
			case '\"':
				appendTo.append("&quot;");
				break;
			case '\'':
				appendTo.append("&#39;");
				break;
			case '\r':
				appendTo.append('\r');
				break;
			case '\n':
				appendTo.append('\n');
				break;
			default:
				if (ca[i] < 32 || (ca[i] > 127 && ca[i] < 256)) {
					// appendTo.append("&#").append( (int) ca[i]).append(";");
				} else {
					appendTo.append(ca[i]);
				}
			}
		}
	}

	/**
	 * 对XML字符进行转义
	 * 
	 * @param src
	 *            String
	 * @return String
	 */
	public static String escapeXMLword(String src) {
		StringBuilder sb = new StringBuilder();
		escapeXMLword(sb, src);
		return sb.toString();
	}

	/**
	 * 拷贝属性,只拷贝基础属性 String Date Number等
	 * 
	 * @param to
	 *            Object
	 * @param from
	 *            Object
	 */
	public static void copyPropertiesExact(Object to, Object from) {
		if (from == null || to == null) {
			return;
		}
		Method[] fromM = from.getClass().getMethods();
		for (int i = 0; i < fromM.length; i++) {
			String methodName = fromM[i].getName();
			Class<?> returnType = fromM[i].getReturnType();
			Class<?>[] parapType = fromM[i].getParameterTypes();
			if (methodName.startsWith("get") && CARE_TYPES.contains(returnType)
					&& (parapType == null || parapType.length == 0)) {
				String setterName = "set" + methodName.substring(3);
				try {
					Method setter = to.getClass().getMethod(setterName, new Class[] { returnType });
					if (setter != null) {
						setter.invoke(to, new Object[] { fromM[i].invoke(from, EMPTY_PARAM) });
					}
				} catch (Exception ex) {
				} // ignore
			}
		}
	}

	private static final Object[] EMPTY_PARAM = new Object[0];

	private static final Set<Class<?>> CARE_TYPES = new HashSet<Class<?>>();
	static {
		// 数字
		CARE_TYPES.add(short.class);
		CARE_TYPES.add(Short.class);
		CARE_TYPES.add(int.class);
		CARE_TYPES.add(Integer.class);
		CARE_TYPES.add(long.class);
		CARE_TYPES.add(Long.class);
		CARE_TYPES.add(float.class);
		CARE_TYPES.add(Float.class);
		CARE_TYPES.add(double.class);
		CARE_TYPES.add(Double.class);
		CARE_TYPES.add(BigInteger.class);
		CARE_TYPES.add(BigDecimal.class);
		CARE_TYPES.add(Number.class);
		// 字符
		CARE_TYPES.add(char.class);
		CARE_TYPES.add(String.class);
		// 时间
		CARE_TYPES.add(java.util.Date.class);
		CARE_TYPES.add(java.sql.Date.class);
		CARE_TYPES.add(java.sql.Time.class);
		CARE_TYPES.add(java.sql.Timestamp.class);
	}

	public static String getParameter(HttpServletRequest request, String key) {
		// 处理tomcat下的中文URL的问题 tomcat在GET方法下传递URL
		// encode的数据会出错。它并是不是按照request设置的字符集进行解码的。
		String query = request.getQueryString();
		if (notEmpty(query)) {
			String[] pairs = query.split("[&]");
			for (String string : pairs) {
				String[] pair = string.split("[=]");
				if (pair.length == 2 && key.equals(pair[0]))
					try {
						//if(Utils.notEmpty(pair[1])) {
							return java.net.URLDecoder.decode(pair[1], "UTF-8");
						//}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}

		return request.getParameter(key);
	}

	@SuppressWarnings("deprecation")
	private static void outPutContent(String conent, String contentType, HttpServletResponse response) {
		BufferedOutputStream bos = null;
		try {
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Content-Type", contentType + "; charset=" + ENCODING);
			bos = new BufferedOutputStream(response.getOutputStream());
			// 调试用语句

			// bos.write(("<?xml version=\"1.0\" encoding=\"" + ENCODING
			// + "\"?>" + ENDLINE).getBytes(ENCODING));
			bos.write(conent.getBytes(ENCODING));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}

	/**
	 * 从COOKIE中取得值
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		return null;
	}

	/**
	 * 设置 cookie
	 * 
	 * @param response
	 *            HttpServletResponse 句柄
	 * @param name
	 *            cookie的名称
	 * @param value
	 *            cookie的值
	 */
	public static void setCookieValue(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		if (isEmpty(value)) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(30 * 86400);
		}
		response.addCookie(cookie);
	}

	/**
	 * 设置 cookie
	 * 
	 * @param response
	 *            HttpServletResponse 句柄
	 * @param name
	 *            cookie的名称
	 * @param value
	 *            cookie的值
	 * @param maxAge
	 *            有效时间(秒) 0表示马上清除该cookie, -1表示关闭浏览器时，cookie删除
	 * @param domain
	 *            有效的访问域，默认只有同一个域才能访问。例如"www.163.com"
	 *            如果需要在子网也能访问，如要让mail.163.com也能访问到这个cookie那么这里应该设置为".163.com"
	 * @param Path
	 *            有效的路径
	 *            如网址"http://2008.163.com/08/0807/10/4IO4QADS00742437.html"
	 *            所对应得path为"/08/0807/10/";
	 */
	public static void setCookieValue(HttpServletResponse response, String name, String value, int maxAge,
			String domain, String path) {
		Cookie cookie = new Cookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		if (isEmpty(value)) {
			cookie.setMaxAge(0);
		} else {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}

	/**
	 * 按提供的字符串分隔字符串(带分隔符转换).
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String[] split(String str, String regex) {
		String[] sa = null;
		if (str != null && notEmpty(regex)) {
			sa = str.split(rex4Str(regex));
		} else {
			throw new IllegalArgumentException();
		}

		return sa;
	}

	/**
	 * 正则转义字符和特殊字符串处理
	 * 
	 * @param regex
	 * @return
	 */
	public static String rex4Str(String regex) {
		String rexc = "";

		if (".".equals(regex)) {
			rexc = "\\.";
		} else if ("^".equals(regex)) {
			rexc = "\\^";
		} else if ("$".equals(regex)) {
			rexc = "\\$";
		} else if ("*".equals(regex)) {
			rexc = "\\*";
		} else if ("+".equals(regex)) {
			rexc = "\\+";
		} else if ("|".equals(regex)) {
			rexc = "\\|";
		} else {
			rexc = regex;
		}

		return rexc;
	}

	/**
	 * 把null的对象/字符串变成空字符串
	 * 
	 * @param o
	 * @return
	 */
	public static String null2EmptyString(Object o) {
		return (o == null ? "" : o.toString());
	}
	
	/**
	 * 为空设置默认值
	 * @param <T>
	 * @param o
	 * @param def
	 * @return
	 */
	public static <T> T null2Default(T o,T def) {
		if(o == null) {
			return def;
		}
		return o;
	}
	/**
	 * 重定向printStackTree的输出到字符串
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static String printStackTrace2String(Throwable t) {
		String exceMsg = null;
		StringWriter sw = null;
		PrintWriter ps = null;
		try {
			sw = new StringWriter();
			ps = new PrintWriter(sw);
			t.printStackTrace(ps);
			exceMsg = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			// LOGGER.info("重定向printStackTreec错误.",e,Utils.class);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					// 什么也不做
				}
			}
		}

		return exceMsg;
	}

	private static String POSTFIX_ZIP = ".zip";

	/**
	 * 提取文件名,如果路径没有文件名及后缀,又不想把最后一层的目录名作为文件名,则自动产生一个唯一的ID作为文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromFilePath(String filePath, boolean isPath2Name) {
		String zipFileName = "";
		if (notEmpty(filePath)) {
			String strTempPath = conversionSpecialCharacters(filePath);
			if (hasSubStrNotInitialOrLast(zipFileName, ".")) {
				zipFileName = filePath.substring(strTempPath.indexOf(File.separator));
			} else {
				if (isPath2Name) {
					if (strTempPath.endsWith(File.separator)) {
						String sub = strTempPath.substring(0, strTempPath.length() - 1);
						zipFileName = sub.substring(sub.lastIndexOf(File.separator) + 1, sub.length());
					}
				} else {
					zipFileName = System.currentTimeMillis() + POSTFIX_ZIP;
				}
			}
		}

		return zipFileName;
	}

	/**
	 * 转换目录分隔符
	 * 
	 * @param zipFileRootPath
	 * @return
	 */
	public static String conversionSpecialCharacters(String zipFileRootPath) {
		String strPath = "";
		if (notEmpty(zipFileRootPath)) {
			strPath = zipFileRootPath.replaceAll("[/(\\\\)]", "/");

			if (!strPath.endsWith(File.separator)) {
				strPath = strPath + File.separator;
			}
		}

		return strPath;
	}

	/**
	 * 用于判断子字符串是否出现在首尾(有可能不包涵子字符串)
	 * 
	 * @param subStr
	 *            待判断字符串
	 * @return 返回boolean类型值,当返回“true”表示待判断字符串符合判断规则,否则false
	 */
	public static boolean isSubStrNotInitialOrLast(String str, String subStr) {
		return str != null && !str.startsWith(subStr) && !str.endsWith(subStr);
	}

	/**
	 * 用于判断是否包涵子字符串且该子字符串不出现在首尾
	 * 
	 * @param subStr
	 *            待判断字符串
	 * @return 返回boolean类型值,当返回“true”表示待判断字符串符合判断规则,否则false
	 */
	public static boolean hasSubStrNotInitialOrLast(String str, String subStr) {
		return str != null && str.indexOf(subStr) > 0 && !str.endsWith(subStr);
	}

	/**
	 * 取得唯一的值
	 * 
	 * @return String
	 */
	public static String getUID() {
		return getUID(8);
	}

	private static Random random = new Random(87860988L);

	/**
	 * 取得唯一的值
	 * 
	 * @param byteLength
	 *            相当于多少个字节。由于是16进制数表示。结果是byteLength的2倍
	 * @return String
	 */
	public static String getUID(int byteLength) {
		byte[] temp = new byte[byteLength];
		random.nextBytes(temp);
		return StringCryptor.byte2hex(temp);
	}

	/**
	 * 格式化XML
	 * 
	 * @param doc
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String formatXML(Document doc, String encoding) throws IOException {
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setIndent(" ");
		outputFormat.setIndentSize(1);
		outputFormat.setExpandEmptyElements(false);
		outputFormat.setLineSeparator(System.getProperty("line.separator", "\n"));
		// 设置xml的输出编码
		outputFormat.setEncoding(encoding);
		// 创建输出(目标)
		StringWriter stringWriter = new StringWriter();
		// 创建输出流
		XMLWriter xmlWriter = new XMLWriter(stringWriter, outputFormat);
		// 输出格式化的串到目标中，执行后。格式化后的串保存在stringWriter中。
		try {
			xmlWriter.write(doc);
		} catch (IOException e) {
			throw e;
		} finally {
			if (xmlWriter != null) {
				xmlWriter.close();
			}
		}

		return stringWriter.toString();
	}
	
	public static String getString(HttpServletRequest request, String name, String sDefault) {
		Object objValue = getParameter(request, name);
		if (objValue == null)
			return sDefault;
		return (String) objValue;
	}
	
	public static String getDefaultValue(String obj,String def) {
		if(obj == null || obj.length() == 0) {
			return def;
		}else {
			return obj;
		}
	}
	
	public static <T> T getDefaultValue(T obj,T def) {
		if(obj == null) {
			return def;
		}else {
			return obj;
		}
	}
	
	public static int getInt(HttpServletRequest request, String name, int iDefault) {
		Object oValue = getParameter(request, name);
		if (oValue == null)
			return iDefault;
		try {
			return Integer.parseInt(oValue.toString());
		} catch (Exception ex) {
			// "属性格式不是整型！[" + oValue + "]"
			return iDefault;
		}
	}

	public static long getLong(HttpServletRequest request, String name, long lDefault) {
		Object oValue = getParameter(request, name);
		if (oValue == null)
			return lDefault;
		try {
			return Long.parseLong(oValue.toString());
		} catch (Exception ex) {
			// "属性格式不是长整型！[" + oValue + "]"
			return lDefault;
		}
	}

	public static boolean getBoolean(HttpServletRequest request, String name, boolean bDefault) {
		int nValue = getInt(request, name, (bDefault) ? 1 : 0);
		return nValue != 0;
	}

	/**
	 * 输出语句,相当于System.out.println();便于统一管理,避免开发调试输出语句忘记清除 
	 * @param obj
	 * @author XFB 2010-11-11
	 */
//	public static void print(Object... obj) {
//		if(Constants.IS_DEBUG) {
//			System.out.println(Utils.joinString(obj));
//		}
//	}
	
	/**
	 * 将数组连接成字符串,代替+操作
	 * @param obj
	 * @return
	 * @author XFB 2010-11-22
	 */
	public static String joinString(Object... obj) {
		StringBuilder sb = new StringBuilder();
		if(obj != null) {
			for(int i=0,j=obj.length;i<j;i++) {
				sb.append(obj[i]);
			}
		}
		return sb.toString();
	
	}
	
	/**
	 * 将数组转换成map
	 * @param <T>
	 * @param arr[][2] 
	 * @return
	 */
	public static <T>Map<T,T> getMapByArray(T[][] arr) {
		Map<T,T> m = new HashMap<T,T>();
		for(T[] t : arr) {
			m.put(t[0], t[1]);
		}
		return m;
	}
	
	/**
	 * 判断字符串是否由数字组成
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		for (int i = str.length();--i>=0;){  
		   if (!Character.isDigit(str.charAt(i))){
		       return false;
		   }
		}
		return true;
	}
	
	/**
	 * 判读整数
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str){
		if(Utils.isEmpty(str)) {
			return false;
		}
		if(str.startsWith("-") && str.length() > 1) {
	    	 str = str.substring(1);
		}
	   return isNumeric(str);
	}
    /**
     * 通过反射取的某属性的值
     * 如果有get方法则用get方法取值
     * @param cls
     * @param obj
     * @param atrName
     * @return
     */
	public static Object accessAtrByRef(Class<?> cls,Object obj,String atrName) throws NoSuchFieldException {
		Object o = null;
		try {
			Method m = cls.getMethod(new StringBuilder(atrName.substring(0, 1).toUpperCase())
			  .append(atrName.substring(1)).toString());
			o = m.invoke(obj);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			Field fld = cls.getDeclaredField(atrName);
			fld.setAccessible(true);
			try {
				o = fld.get(obj);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	public static void outPutXmlFile(String xml,HttpServletResponse response){
		outPutContent("<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>" + ENDLINE + xml, "text/xml", response);
	}
	
	/**
	 * html 字符转义
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str) {
		return StringEscapeUtils.escapeHtml(str);
	}
	
	public static String unescapeHtml(String str) {
		return StringEscapeUtils.unescapeHtml(str);
	}
	
	/**
	 * 判断请求是否由DFish框架发出的
	 * @param request
	 * @return
	 */
	public static boolean requestFromDFish(HttpServletRequest request) {
		String h = request.getHeader("x-requested-with");
		return "dfish".equals(h);
	}
	
	public static URLConnection openURLConnection(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(500); // FIXME 连接超时时间
		conn.setReadTimeout(8000); // FIXME 读取超时时间
		return conn;
	}
	
	/**
	 * 转类似" field1:field1value;field2:field2value;field3:field3value; "的字符串 TO Map
	 * @param text
	 * @return
	 */
	public static Map<String, String> string2Map(String text){
		Map<String, String> map = new HashMap<String, String>();
		if(Utils.notEmpty(text)){
			String [] sss = text.split(";");
			if(sss!=null){
				for(String ss:sss){
					String [] s = ss.split(":");
					if(s!=null&&s.length>=2){
						map.put(s[0], s[1]);
					}
				}
			}
		}
		return map;
	}
	/**
	 *  转Map TO 类似" field1:field1value;field2:field2value;field3:field3value; "的字符串  
	 * @param text
	 * @return
	 */
	public static String mapToString(Map map){
		String mapString = new String();
		if(map!=null&&map.size()>0){
			mapString = map.toString().replaceAll("\\{|\\}", "").replace("=", ":").replace(",", ";").replaceAll("\\s*", "")+";";
		}
		return mapString;
	}
}
