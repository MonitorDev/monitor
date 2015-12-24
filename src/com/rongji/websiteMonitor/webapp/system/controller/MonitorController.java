package com.rongji.websiteMonitor.webapp.system.controller;

import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;
import static com.rongji.dfish.framework.FrameworkHelper.setPersonalConfig;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Skin;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.framework.FrameworkConstants;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.misc.FilterParam;
import com.rongji.dfish.webapp.pub.business.SchedulerMethods;
import com.rongji.websiteMonitor.platform.ums.style.I73dStyle;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyle;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyleManager;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.system.view.IndexView;
import com.rongji.websiteMonitor.webapp.system.view.MonitorView;


@SuppressWarnings("all")
public class MonitorController extends BaseActionController {
	
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
		BaseView view = MonitorView.buildIndexView(loc, viewFactory, userId, style,request);
		FrameworkHelper.outPutXML(response, view);
		return null;
	}

}
