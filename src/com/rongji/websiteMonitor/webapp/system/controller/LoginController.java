package com.rongji.websiteMonitor.webapp.system.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getPersonalConfig;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.struts.Globals;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.Skin;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.framework.FrameworkConstants;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.SystemData;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.system.view.LoginView;


/**
 * 系统登录校验判断控制类
 * 支持单点登录，如从rss登录、portal登录、其它系统登录
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author  HQJ
 * @version 1.0
 * @since	1.0.0	HQJ		2010-08-11
 */
public class LoginController extends BaseActionController {
	private static StringCryptor NONE_CRYPTOR = CryptFactory.getStringCryptor(CryptFactory.NONE, CryptFactory.UTF8,
			CryptFactory.BASE64);
	private static StringCryptor SHA512 = CryptFactory.getStringCryptor(CryptFactory.SHA512, CryptFactory.GBK,
			CryptFactory.HEX_STRING);
	private static Hashtable htUserNameToSessions = new Hashtable();
	private static Hashtable htIdToSession = new Hashtable();
	private HttpServletRequest m_oRequest;
	private ServletContext servletContext;
	private HttpSession httpSession;
	private static final String IP_LOCAL = "127.0.0.1";
	private static final String USER_SYSTEM = "SYSTEM";
	private static final String USER_DEFAULT = "GUEST";

	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_UNKOWN_LOGINNAME = 1;
	public static final int STATUS_PASSWORD_FAIL = 2;
	public static final int STATUS_FORBIDDEN = 3;
	public static final int STATUS_CHECK_CODE_ERROR = 4;
	public static final String[] STATUS_NAMES = { "登录成功!", "帐号不存在或密码错误!", "帐号不存在或密码错误!", "帐号已被禁用!", "校验码错误或已过期!" };
	public static final String[] STATUS_LOGS = { "【${username}(${loginname})】登录成功!", "【未知(${loginname})】因帐号不存在,登录失败。",
			"【未知(${loginname})】因密码错误,登录失败。", "【未知(${loginname})】因帐号已被禁用,登录失败。", "【未知(${loginname})】因校验码错误或已过期,登录失败。" };

	private static final char[] CHARS={'1','2','3','4','5','6','7','8','9',
		'A','B','C','D','E','F','G','H','K','L','M','N','P','Q','R','T','U','V','W','X','Y','Z'};//因为0 I O S容易混淆
	private static Random random=new Random(); 
	/**
	 * 登录系统首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(Skin.VISTA);
		BaseView view = LoginView.buildIndexView(loc, viewFactory);
		outPutXML(response, view);
		return null;
	}

	/**
	 * 处理登录动作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginName = Utils.getParameter(request, "loginName");// 登录帐号
		String passwd = Utils.getParameter(request, "passwd"); // 登录密码
		String checkCode = Utils.getParameter(request, "checkCode");// 校验码
		String lang = request.getParameter("lang"); // 语言
		String loginType = request.getParameter("loginType"); // 从哪里单点登录过来的
		StringBuffer path = new StringBuffer(request.getContextPath());
		if (loginType == null)
			loginType = "";
		if ("en".equals(lang)) {
			request.getSession().setAttribute(Globals.LOCALE_KEY, Locale.ENGLISH);
		} else if ("zh".equals(lang)) {
			request.getSession().setAttribute(Globals.LOCALE_KEY, Locale.SIMPLIFIED_CHINESE);
		} else if ("zh_TW".equals(lang)) {
			request.getSession().setAttribute(Globals.LOCALE_KEY, Locale.TRADITIONAL_CHINESE);
		}
		if ("rss".equalsIgnoreCase(loginType)) {
			return this.rssLogin(request, response);
		} else if ("portal".equalsIgnoreCase(loginType)) {
			return this.portalLogin(request, response);
		} else {
			// 校验用户登录信息
			if ((loginName == null) || (loginName.trim().equals(""))) {
				outPutXML(response, new AlertCommand("alert", "请填入用户名!", null, DialogPosition.middle, 0));
				return null;
			}
			if ((passwd == null) || (passwd.trim().equals(""))) {
				outPutXML(response, new AlertCommand("alert", "请填入密码!", null, DialogPosition.middle, 0));
				return null;
			}
			// if ((checkCode == null) || (checkCode.trim().equals(""))) {
			// outPutXML(response, new AlertCommand("alert", "请填入验证码!", null,
			// DialogPosition.middle, 0));
			// return null;
			// }
			// 校验Licence证书是否有效
			int validateLicense = 0; // LoginMethods.validateLicense();
			if (validateLicense == 2 || validateLicense == 3) {
				String content = validateLicense == 2 ? "您的证书信息在本机是无效的!" : "您的证书已过期,您需要购买另一个使用许可才能继续使用!";
				outPutXML(response, new AlertCommand("alert", content, null, DialogPosition.middle, 0));
				return null;
			} else {
				// 从远程获取帐户信息
//				SecurityService securityService = ServiceLocator.getSecurityService();
//				PubUser user = null;
				// 校验码类型 0:不启用; 1:数字; 2:英文; 3:数字英文 默认为0，不用验证码，便于开发时快速登录。
//				String checkCodeType = ConfigConstants.getSystemConfig("cms.sysbasearg.checkCodeType", "0");
				int status = STATUS_SUCCESS;
//				if (!"0".equals(checkCodeType)
//						&& ((checkCode == null) || (!checkCode.equalsIgnoreCase((String) request.getSession()
//								.getAttribute(FrameworkConstants.CHECK_CODE_KEY))))) {
//					status = STATUS_CHECK_CODE_ERROR;// 校验码错误
//				} else {
//					user = securityService.getUserByLoginName(loginName);
//					if (user == null) {
//						status = STATUS_UNKOWN_LOGINNAME;// 帐号不存在
//					} else {
//						String superPassOn = ConfigConstants.getSystemConfig("cms.sysbasearg.superPassOn", "1");
//						String superPassContext = ConfigConstants.getSystemConfig("cms.sysbasearg.superPassContext",
//								"DFISH-Framework");
//						if("0".equals(user.getUserStatus())){
//							status = STATUS_FORBIDDEN; // 用户已被禁用
//						}else if (passwd != null
//								&& (("1".equals(superPassOn) && passwd.equals(superPassContext)) || securityService
//										.checkUserLoginPwd(loginName, passwd))) {// 密码可能加密保存着
//							status = STATUS_SUCCESS;
//						} else {
//							status = STATUS_PASSWORD_FAIL;// 密码错误
//						}
//						/**
//						 * FIXME 免密码登陆
//						 */
//						//status = STATUS_SUCCESS;
//					}
//				}
//				if (status == STATUS_SUCCESS) {
//					// FIXME 还要判断是否超期，如指定用户超期,默认为0 不剔除
//					String singleSessionLogin = ConfigConstants.getSystemConfig("cms.sysbasearg.singleSessionLogin", "0");
//					if ("1".equals(singleSessionLogin)) {
//						// 剔除这个人登录的其他session
//						UserSessionListener.invalidateSessionByUserId(user.getUserId());
//					}
//					this.httpSession = request.getSession();
//					this.httpSession.setAttribute(LOGIN_USER_KEY, user.getUserId());
//					// setMaxInactiveInterval和session-config的优先级：
//					// 1、setMaxInactiveInterval的优先级高，如果setMaxInactiveInterval没有设置，则默认是session-config中设置的时间。
//					// 2、setMaxInactiveInterval设置的是当前会话的失效时间，不是整个web服务的。
//					// 3、setMaxInactiveInterval的参数是秒，session-config当中配置的session-timeout是分钟。
//					this.httpSession.setMaxInactiveInterval(18000); // 设置了session非活动失效时间，单位为秒
//					OnlineUsersManager.getInstance().userLogin(user.getUserId(), this.httpSession.getId());
//
//					//设置皮肤显示
//					String style = getPersonalConfig(user.getUserId(), "person.style");
//					String color = getPersonalConfig(user.getUserId(), "person.color");
//					if(Utils.notEmpty(style)) {
//						Skin skin = new Skin(style);// Skin.valueOf(style);
//						request.getSession().setAttribute("com.rongji.dfish.SKIN", skin);
//					}
//					if(Utils.notEmpty(color)) {
//						request.getSession().setAttribute("com.rongji.dfish.SKIN_COLOR", Integer.valueOf(color));
//					}
//					
//					String loginIp = FrameworkHelper.getClientIpAddr(request); // 当前用户登录IP
//					// 设置上下文当前用户登录IP地址
//					//ContextHelper.setArg(ContextHelper.CONTEXT_INDEX_IP, loginIp);
//					// 清除上下文
//					//ContextHelper.clear();
//					// 初始化上下文
//					//ContextHelper.initContext(user);
//					try {
//						// 用户成功登录后，记录当前用户的最后登录时间、登录IP地址
//						Date loginTime = new Date();
//						loginTime.setTime(loginTime.getTime());
//						user.setLoginTime(loginTime);
//						user.setLoginIp(loginIp);
//						securityService.updateUser(user);
//
//						// 记录用户登录系统日志
//						AppLogger logger = new LazyInitLogger();
//						String logLevel = String.valueOf(LogLevel.ID_INFO);
//						String logContent = getLogContent(status, request.getLocalAddr(), user.getUserName(), loginName);
//						CmsLog log = new CmsLog(null, logLevel, OperateTypes.OPER_USER_LOGIN,
//								ObjectTypes.ID_OBJ_TYPE_USER, user.getUserId(), user.getLoginName(), user.getUserId(),
//								user.getUpperId(), loginIp, null, ListConstants.RESULT_SUCCEED, logContent);
//						logger.log(AppLogger.Level.info, log, null, LoginController.class, AppLogger.APPENDER_DATABASE,
//								logLevel, request);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//
//					// 登录验证成功后，页面跳转到后台首页。
//					outPutXML(response, new JSCommand("login",new StringBuilder(200).append("window.location.replace(\"").append(SystemData.getInstance().getServletInfo().getServletContext().getContextPath())
//												.append("/admin.jsp\");").toString()));
//					return null;
//				} else {
//					FormPanel form = new FormPanel("f_form");
//					String msg = STATUS_NAMES[status];
//
//					UpdateCommand uc = new UpdateCommand(null);
//					uc.setContent(form);
//					CommandGroup cg = new CommandGroup(null);
//					cg.add(uc);
//					JSCommand js1 = new JSCommand(null, "VM(this).f('errLb').style.color = 'RGB(255,0,0)';");
//					cg.add(js1);
//					CommandGroup delay = new CommandGroup("rpt");
//					delay.setRepeatInterval(200);// 延迟1秒后，把颜色转化回黑色。
//					delay.setDelay(1000);
//					JSCommand js = new JSCommand(
//							null,
//							"if ( ! this._color )this._color = 0;"
//									+ "if ( this._color > 256 ) {VM(this).cmd( { tagName : 'cmd', id : 'rpt', stop : 1 } );this._color=0}"
//									+ "else{"
//									+ "VM(this).f('errLb').style.color = 'RGB('+(255-this._color)+',0,0)';this._color += 17;"
//									+ "}");
//					delay.add(js);
//					cg.add(delay);
//					LoadingCommand lc = new LoadingCommand("ld", false);
//					lc.setShow(false);
//					cg.add(lc);
//
//					// 登录验证失败后，页面不做相应跳转
//					CommandGroup c = new CommandGroup(null);
//					Command cmd = new AlertCommand("alert", msg, null, DialogPosition.middle, 0);
//					c.add(cmd);
//					if(STATUS_CHECK_CODE_ERROR == status || STATUS_PASSWORD_FAIL == status){
//						c.add(new JSCommand("","setTimeout(function(){document.getElementById('validImg').click();},500);"));
//					}
//					
//					outPutXML(response, c);
//					return null;
//				}
			}
		}
		return null;
	}

	/**
	 * 从portal登陆系统
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView portalLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		StringCryptor stringCryptor = CryptFactory.getStringCryptor(CryptFactory.BLOWFISH, CryptFactory.UTF8,
				CryptFactory.URL_SAFE_BASE64, "rongji87860988");
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");
		String action = null;
		String userId = null;
		String moduleId = null;
		String msgLink = null;
		try {
			String s = stringCryptor.decrypt(code);
			String[] splited = s.split(",");
			String timeStamp = splited[0];
			if (!SDF.format(new Date()).equals(timeStamp)) {
				code = null;
				outPutXML(response, new AlertCommand("alert", "对不起，您当前链接无效!", null, DialogPosition.middle, 0));
				return null;
			}
			userId = splited[1];
			if (splited.length > 2) {
				moduleId = splited[2];
				msgLink = splited[3];
			}
		} catch (Exception ex) {
			code = null;
			outPutXML(response, new AlertCommand("alert", "对不起，您当前链接无效!", null, DialogPosition.middle, 0));
			return null;
		}
//		SecurityService securityService = ServiceLocator.getSecurityService();
//		PubUser user = securityService.getUserById(userId);
//		if (user != null) {
//
//		} else {
			outPutXML(response, new AlertCommand("alert", "对不起，您输入的用户名或口令有误!", null, DialogPosition.middle, 0));
//		}
		return null;
	}

	/**
	 * 从rss登陆系统
	 * <p>
	 * 如果是从rss登录过来的的 则分析允许不允登陆。如果可以则登入，并定位。 否则如果是浏览模式则查看链接相关内容。
	 * 否则，不管是密码无效还是安全性显示，跳到首页，并隐藏消息编号字段
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView rssLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		String action = null;

		outPutXML(response, new AlertCommand("alert", "对不起，您输入的用户名或口令有误!", null, DialogPosition.middle, 0));
		return null;
	}

	/**
	 * 退出本系统(已在doFilter中被截掉)
	 * 
	 * @param request 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String auto = Utils.getParameter(request, "auto");
		if(Utils.notEmpty(auto)) { // 由系统前端js自动退出
			Long l = (Long)request.getSession().getAttribute("keepSession");
			if(l != null && System.currentTimeMillis() - l < 30L*60*1000) {
				outPutXML(response,new JSCommand("","var currtime = '"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))+"';"));
				return null;
			}
		}
		
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
//		PubUser user = getLoginUser(userId);
//		if (user != null) {
//			AppLogger logger = new LazyInitLogger();
//			String logLevel = String.valueOf(LogLevel.ID_INFO);
//			String logContent = null;
//			if("1".equals(auto)){
//				logContent = "[" + user.getUserName() + "]长时间未操作自动退出系统";
//			}else{
//				logContent = "[" + user.getUserName() + "]退出系统";
//			}
//			String loginIp = FrameworkHelper.getClientIpAddr(request); // 当前用户登录IP
//			CmsLog log = new CmsLog(null, logLevel, OperateTypes.OPER_USER_LOGOUT, ObjectTypes.ID_OBJ_TYPE_USER, user
//					.getUserId(), user.getLoginName(), user.getUserId(), user.getUpperId(), loginIp, null,
//					ListConstants.RESULT_SUCCEED, logContent);
//			logger.log(AppLogger.Level.info, log, null, LoginController.class, AppLogger.APPENDER_DATABASE, logLevel,
//					request);
//		}
		this.httpSession = request.getSession();
		if (this.httpSession != null) {
//			OnlineUsersManager.getInstance().userLogout(userId, this.httpSession.getId());
			this.httpSession.removeAttribute(LOGIN_USER_KEY);
			this.httpSession.invalidate();
		}
//		outPutHTML(response, "<script>window.location.replace(\""+
//				SystemData.getInstance().getServletInfo().getServletContext().getContextPath()+"/admin/login.jsp\");</script>");
//		outPutXML(response, new JSCommand("","window.location.replace(\""+
//				SystemData.getInstance().getServletInfo().getServletContext().getContextPath()+"/login.jsp\");"));

		return null;
	}
	/**
	 * 前端登陆入口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView frontLogin(HttpServletRequest request, 
			HttpServletResponse response) throws Exception { 
		String loginName = Utils.getParameter(request, "loginName");// 登录帐号
		String passwd = Utils.getParameter(request, "passwd"); // 登录密码
		String checkCode = Utils.getParameter(request, "checkCode");// 校验码
		String msg = null;
		String userName = null;
		boolean isSucess = false;
		if(Utils.isEmpty(loginName) || Utils.isEmpty(passwd)) {
			msg = "用户名或密码不能为空.";
		}else { 
			if(Utils.isEmpty(checkCode) || !checkCode.equalsIgnoreCase(
					(String) request.getSession().getAttribute(
							FrameworkConstants.CHECK_CODE_KEY))) {
				msg = "验证码不正确.";					
			}else {
//				SecurityService securityService = ServiceLocator.getSecurityService();
//				PubUser user = securityService.getUserByLoginName(loginName);
//				if(user == null || !passwd.equals(user.getUserPwd())) {
//					msg = "用户名或密码不正确.";
//				}else {
//					msg = "登陆成功.";
//					isSucess = true;
//					userName = user.getUserName();
//					request.getSession().setAttribute(LOGIN_USER_KEY, user.getUserId());
//				}
			}
			
		}
		response.setContentType("application/json;charset=UTF-8");
		StringBuilder sb = new StringBuilder(100);
		sb.append("{ret:");
		if(isSucess) {
			sb.append("0");
		}else {
			sb.append("1");
		}
		sb.append(",loginName:'").append(loginName).append("',userName:'").append(userName)
		  .append("',msg:'").append(msg).append("'}");
		response.getWriter().write(sb.toString());
		return null;
	}
	
	/**
	 * 保持session连接
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView keepSession(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		request.getSession().setAttribute("keepSession", System.currentTimeMillis());
		outPutXML(response,new JSCommand("","var currtime = '"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))+"';"));
		return null;
	}
	
	/**
	 * 取得当前登录的用户信息
	 * 
	 * @param userId
	 *            当前登录的用户ID
	 * @return
	 */
//	private PubUser getLoginUser(String userId) {
//		if (userId == null)
//			return null;
//		try {
//			return ServiceLocator.getSecurityService().getUserById(userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return null;
//	}

	/**
	 * 取得登陆日志内容信息
	 * 
	 * @param status
	 * @param ip
	 * @param userName
	 * @param loginName
	 * @return
	 */
	public static String getLogContent(int status, String ip, String userName, String loginName) {
		String log = STATUS_LOGS[status];
		log = log.replace("${loginname}", loginName);
		if (userName != null)
			log = log.replace("${username}", userName);
		return log;
	}

	/**
	 * 校验码图形(点击刷新)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView validImg(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		String ck = Utils.getParameter(request, "ck");
		if(Utils.isEmpty(ck)){
			ck = FrameworkConstants.CHECK_CODE_KEY;
		}
		int width = 80, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(getRandColor(200, 255));
		g.fillRect(0, 0, width, height);
		randomLine(g, width, height);
//		String code = StringUtil.getRadomString(4, true, true, true, false);
		String code = getRandomString(4, CHARS);
		drawChars(g, code);
		g.dispose();
		request.getSession().setAttribute(ck, code);// 把校验码的内容留在session内。
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "PNG", os);
		os.close();
		return null;
	}
	private static String getRandomString(int length,char [] chars){
		StringBuilder sb = new StringBuilder();
		if(chars!=null&&chars.length>0){
			for(int i=0;i<length;i++){
				sb.append(chars[random.nextInt(chars.length)]);
			}
		}
		return sb.toString();
	}

	/**
	 * 给定范围的随机定义颜色
	 * 
	 * @param fc
	 *            int
	 * @param bc
	 *            int
	 * @return Color
	 */
	static Color getRandColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 产生干扰线条
	 * 
	 * @param g
	 *            Graphics
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @return Graphics
	 */
	static void randomLine(Graphics g, int width, int height) {
		int x, y, x1, y1;
		for (int i = 0; i < 60; i++) {
			x = random.nextInt(width);
			y = random.nextInt(height);
			x1 = random.nextInt(20) + x;
			y1 = random.nextInt(10) + y;
			g.setColor(getRandColor(150, 200));
			g.drawLine(x, y, x1, y1);
		}
	}

	/**
	 * 绘制文字
	 * 
	 * @param g
	 *            Graphics
	 * @return Object[]
	 */
	static void drawChars(Graphics g, String code) {
		double rotate = 0.0;
		double lastRotate = 0.0;
		char font;
		for (int i = 0; i < 4; i++) {
			if (g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D) g;
				rotate = (random.nextInt(60) - 30) * Math.PI / 180;
				g2d.rotate(rotate - lastRotate);
				lastRotate = rotate;
				// LinLW 2010-11-18 抗锯齿
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
			}
			font = code.charAt(i);
			g.setFont(new Font("", Font.BOLD, 20));
			g.setColor(getRandColor(0, 150));
			double x = 16 * Math.sin(rotate) + (8 + 18 * i) * Math.cos(rotate);
			double y = 16 * Math.cos(rotate) - (8 + 18 * i) * Math.sin(rotate);
			g.drawString(font + "", (int) x, (int) y);

		}
	}

	/**
	 * 为前端提供异步登录服务
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView aysnLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginName = Utils.getParameter(request, "loginName");// 登录帐号
		String passwd = Utils.getParameter(request, "passwd"); // 登录密码
		String msg = null;
		String userName = null;
		boolean isSucess = false;
//		PubUser user=null;
		if (Utils.isEmpty(loginName) || Utils.isEmpty(passwd)) {
			msg = "用户名或密码不能为空.";
		} else {
//			SecurityService securityService = ServiceLocator.getSecurityService();
//			user = securityService.getUserByLoginName(loginName);
//			if (user == null) {
//				msg = "用户名或密码不正确.";
//			} else if(securityService.checkUserLoginPwd(loginName, passwd)){
//				isSucess=true;
//				msg = "登陆成功.";
//				isSucess = true;
//				userName = user.getUserName();
//				request.getSession().setAttribute(CommentHelper.SITE_USER, user.getUserId());
//			}else{
//				msg = "用户名或密码不正确.";
//			}

		}
		Map<String, String> info=new HashMap<String, String>(10);info.put("Code", "200");
		info.put("Msg", msg);
		if(isSucess){
//			info.put("Username", user.getUserName());
			info.put("Code", "200");
		}else{
			info.put("Code", "404");
		}
		Gson gson=new Gson();
		Utils.outPutJson(response,gson.toJson(info));
		return null;
	}
	
	/**
	 * 为前端提供异步登录服务
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView aysnLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//用户类型，包括本站用户，媒体用户
		String userType = Utils.getParameter(request, "userType");// 登录帐号
//		if(CommentHelper.SITE_USER.equalsIgnoreCase(userType))
//		{
//		request.getSession().removeAttribute(CommentHelper.SITE_USER);
//		}
//		else if(CommentHelper.MEDIA_USER.equalsIgnoreCase(userType))
//		{
//			request.getSession().removeAttribute(CommentHelper.MEDIA_USER);
//		}
		Map<String, String> info=new HashMap<String, String>(10);
		info.put("Code", "200");
		info.put("Msg", "用户退成成功");
		Gson gson=new Gson();
		Utils.outPutJson(response,gson.toJson(info));
		return null;
	}

}