package com.rongji.websiteMonitor.webapp.system.controller;

import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;
import static com.rongji.dfish.framework.FrameworkHelper.setPersonalConfig;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Skin;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.framework.FrameworkConstants;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.webapp.pub.business.SchedulerMethods;
import com.rongji.websiteMonitor.platform.ums.style.I73dStyle;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyle;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyleManager;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.system.view.IndexView;


/**
 * 系统后台首页控制类
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author  HQJ
 * @version 1.0
 * @since	1.0.0	HQJ		2009-08-11
 */
@SuppressWarnings("deprecation")
public class IndexController extends BaseActionController {
	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ItaskStyle st= new I73dStyle();
		BaseView view=st.buildIndexView(request);
		Cookie[] ck = request.getCookies();
		String sbic = "0";
		String sbnail = "0";
		String sbdgw = "0";
		String sbdg = "";
		String sbuic = "";
		if (ck != null) {
			for (Cookie c : ck) {
				if ("sb_ic".equals(c.getName())) {
					sbic = c.getValue();
				} else if ("sb_nail".equals(c.getName())) {
					sbnail = c.getValue();
				} else if ("sb_dgw".equals(c.getName())) {
					sbdgw = c.getValue();
				} else if ("sb_dg".equals(c.getName())) {
					sbdg = c.getValue();
				} else if ("sb_uic".equals(c.getName())) {
					sbuic = c.getValue();
				}
			}
		}

		outPutXML(response,view);
	}
	
	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView monitorIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
		String userId = (String) request.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);
		ItaskStyle style = ItaskStyleManager.getStyle(request);
		BaseView view = IndexView.buildIndexView(loc, viewFactory, userId, style);
		FrameworkHelper.outPutXML(response, view);
		return null;
	}


	/**
	 * 选择页面风格窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView styleSelect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		Skin currenStyle = FrameworkHelper.getSkin(request);
		int currentColor = FrameworkHelper.getSkinColor(request);
		String defaultStyle = String.valueOf(currenStyle) + "_" + currentColor;

//		BaseView view = IndexView.buildDlgIndexView4StyleSelect(loc, viewFactory, defaultStyle);
//		outPutXML(response, view);
		return null;
	}

	/**
	 * 保存选择的页面风格
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView styleSelectSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = (String) request.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);
		String style = request.getParameter("style");
		int color = 0;
		if (style != null && style.indexOf("_") > 0) {
			String[] temp = style.split("_");
			style = temp[0];
			color = new Integer(temp[1]);
		}
		Skin skin = new Skin(style);// Skin.valueOf(style);
		request.getSession().setAttribute("com.rongji.dfish.SKIN", skin);
		request.getSession().setAttribute("com.rongji.dfish.SKIN_COLOR", color);
		setPersonalConfig(userId, "person.style", style);
		setPersonalConfig(userId, "person.color", String.valueOf(color));
		JSCommand refresh = new JSCommand("refresh", "top.location.reload()");
		outPutXML(response, refresh);
		return null;
	}

	/**
	 * 关于RJ-CMS
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView about(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(Skin.VISTA);
		
//		BaseView view = IndexView.buildDlgIndexView4About(loc, viewFactory);
//		outPutXML(response, view);
		return null;
	}
	/**
	 * 切换系统对话框列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	public ModelAndView switchAppDialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String userId = (String) request.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);
//		Locale loc = getLocale(request);
//		ViewFactory viewFactory = ViewFactory.getViewFactory(Framework.getSkin(request));
//		List<PubResource> appSysList = getAppSysList(userId);
//		BaseView view = IndexView.buildDlgIndexView4AppList(loc, viewFactory,appSysList);
//		outPutXML(response, view);
//		return null;
//	}
	
	/**
	 * 加载可用应用系统列表
	 * @param userId
	 * @return List<String [] >
	 * @throws IOException 
	 */
//	private static List<PubResource> getAppSysList(String userId) {
//		List<PubResource> appSysList = new ArrayList<PubResource>();
//		SecurityExtClient secExtClient = (SecurityExtClient) ServiceLocator.getBean("secExtClient");
//		try {
//			SecurityServiceStub.PubModule pubModule = SecurityServiceImpl
//					.getInstance().getPubModuleByCode(
//							ConfigConstants.getInstance().getAppSysCategory());
//			List<Object[]> loadVisibleResData = secExtClient.loadVisibleResData(userId, pubModule.getModId(),
//							ConfigConstants.getInstance().getAppSysCanSeeOperId(), 
//							ConfigConstants.getInstance().getAdminRoleId());
//			if(Utils.notEmpty(loadVisibleResData)){
//				SecurityClient secClient = (SecurityClient) ServiceLocator.getBean("secClient");
//				String appSysResFlag = ConfigConstants.getInstance().getAppSysResFlag();
//				for (Object[] resData : loadVisibleResData) {
//					PubResource res = secClient.findPubResourceById((String)resData[0]);
//					if(res!=null&&!res.getResCode().equals(appSysResFlag)){
//						appSysList.add(res);
//					}
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return appSysList;
//	}	
	/**
	 * 切换系统
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
//	public ModelAndView switchApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Locale loc = getLocale(request);
//		String toUrl = request.getParameter("toUrl");//转向的应用URL
//		String type = request.getParameter("type");//转向的应用URL方式
//		CommandGroup cg = new CommandGroup("");
//		if(Utils.notEmpty(toUrl)){
//			if("newTab".equals(type)){
//				cg.add(new JSCommand("","window.open(\""+toUrl+"\");"));
//				cg.add(JSCmdLib.dialogClose(""));
//			}else{
//				//清空本系统session信息
//				request.getSession().removeAttribute(FrameworkConstants.LOGIN_USER_KEY);
//				request.getSession().invalidate();
//				cg.add(new JSCommand("","window.location.href=\""+toUrl+"\" "));
//			}
//		}else{
//			cg.add(new AlertCommand("","切换地址不存在！","img/p/alert-crack.gif",DialogPosition.middle,5));
//		}
//		outPutXML(response, cg);
//		return null;
//	}

	/**
	 * 链接跳转到相应模块(堆栈子面板)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView jump(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(Skin.VISTA);
		String keyWord = request.getParameter("keyWord"); // 关键字
		String viewMode = request.getParameter("viewMode");// 为1时刷新整个页面，否则只更新局部界面
		String userId = (String) request.getSession().getAttribute(FrameworkConstants.LOGIN_USER_KEY);

//		XMLObject o = IndexView.updateView4jump(loc, viewFactory, keyWord, viewMode, userId);
//		outPutXML(response, o);
		return null;
	}

}
