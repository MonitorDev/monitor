package com.rongji.websiteMonitor.webapp.monitorOption.controller;


import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ThresholdService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.monitorOption.view.MonitorOptionView;

public class MonitorOptionController extends BaseActionController {
	private ThresholdService thresholdService;

	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Object[]> list = new ArrayList<Object[]>();
		list.add(new Object[]{Threshold.Type.snmp.getValue(), "阀值模板管理"});
//		list.add(new Object[]{Threshold.Type.http.getValue(), "HTTP阀值模板管理"});
		List<Threshold> listThreshold = thresholdService.findThresholdByPage(page);
		BaseView view = MonitorOptionView.buildIndexView(loc,viewFactory, list, page,listThreshold);
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand uc = new UpdateCommand("uc");
		uc.setContent(view);
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}

	/**
	 * 编辑
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView optionList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String type = Utils.getParameter(request, "type");
		String currentPage = request.getParameter("cp");
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
//		if(Threshold.Type.snmp.getValue().equals(type)) {
			List<Threshold> listThreshold = thresholdService.findThresholdByPage(page);
			CommandGroup cg = MonitorOptionView.updateThresholdPanel(loc, viewFactory, listThreshold,page); 
			outPutXML(response, cg);
//		}else if(Threshold.Type.http.getValue().equals(type)) {
//			List<Threshold> listThreshold = thresholdService.findThresholdByPage(Threshold.Type.http.getValue(),page);
//			CommandGroup cg = MonitorOptionView.updateThresholdPanel(loc, viewFactory, listThreshold,page); 
//			outPutXML(response, cg);
//		}
	
		return null;
	}
	
	public ModelAndView showThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		String thresholdId = request.getParameter("id");
		String handler = request.getParameter("handler");
		String type = request.getParameter("type");
		if(Threshold.Type.snmp.getValue().equals(type)) {
			if(Utils.notEmpty(thresholdId)) {
				Threshold threshold = thresholdService.getThresholdById(thresholdId);
				if("detail".equals(handler)) {
					outPutXML(response, MonitorOptionView.buildThresholdView(threshold, false));
				}else if("edit".equals(handler)) {
					outPutXML(response, MonitorOptionView.buildThresholdView(threshold, true));
				}
			}else {
				outPutXML(response, MonitorOptionView.buildThresholdView(new Threshold(), true));
			}
		}else if(Threshold.Type.http.getValue().equals(type)) {
			if(Utils.notEmpty(thresholdId)) {
				Threshold threshold = thresholdService.getThresholdById(thresholdId);
				if("detail".equals(handler)) {
					outPutXML(response, MonitorOptionView.buildHttpThresholdView(threshold, false));
				}else if("edit".equals(handler)) {
					outPutXML(response, MonitorOptionView.buildHttpThresholdView(threshold, true));
				}
			}else {
				outPutXML(response, MonitorOptionView.buildHttpThresholdView(new Threshold(), true));
			}
		}
		
		return null;
	}
	
	public ModelAndView showHttpThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		String thresholdId = request.getParameter("id");
		String type = request.getParameter("type");
		if(Utils.notEmpty(thresholdId)) {
			Threshold threshold = thresholdService.getThresholdById(thresholdId);
			if("detail".equals(type)) {
				outPutXML(response, MonitorOptionView.buildHttpThresholdView(threshold, false));
			}else if("edit".equals(type)) {
				outPutXML(response, MonitorOptionView.buildHttpThresholdView(threshold, true));
			}
		}else {
			outPutXML(response, MonitorOptionView.buildHttpThresholdView(new Threshold(), true));
		}
		return null;
	}
	public ModelAndView saveThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String cpu = request.getParameter("cpu");
		String memory = request.getParameter("memory");
		String jvmMemory = request.getParameter("jvmMemory");
		String jvmThread = request.getParameter("jvmThread");
//		String io = request.getParameter("io");
		String retry = request.getParameter("retry");
		String isUsable = request.getParameter("isUsable");
		String diskUsage = request.getParameter("diskUsage");
		String systemProcess = request.getParameter("systemProcess");
		
		Threshold th = null;
		if(Utils.notEmpty(id)) {
			th = thresholdService.getThresholdById(id);
			if(th == null) {
				AlertCommand alt = new AlertCommand("alt", "获取阀值模板失败！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			th.setUpdateTime(new Date());
		}else {
			th = new Threshold();
			th.setCreateTime(new Date());
			th.setName(name);
			if(Utils.isEmpty(name)) {
				AlertCommand alt = new AlertCommand("alt", "阀值名称不能为空！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}else {
				List<Threshold> list = thresholdService.findThresholdByName(Threshold.Type.snmp.getValue(),name);
				if(Utils.notEmpty(list)) {
					AlertCommand alt = new AlertCommand("alt", "阀值名称已经存在！",
							"img/p/alert-warn.gif", DialogPosition.middle, 5);
					outPutXML(response, alt);
					return null;
				}
			}
			th.setType(Threshold.Type.snmp.getValue());
			th.setIsDefault(Constants.OPTION_IS_NO);
		}
		StringBuffer content = new StringBuffer();
		content.append("cpu:").append(cpu)
			.append(";memory:").append(memory)
			.append(";jvmMemory:").append(jvmMemory)
			.append(";jvmThread:").append(jvmThread)
			.append(";diskUsage:").append(diskUsage)
			.append(";systemProcess:").append(systemProcess);
		th.setContent(content.toString());
		th.setRetry(Integer.parseInt(retry));
		th.setIsUsable(isUsable);
		
		if(Utils.notEmpty(id)) {
			thresholdService.updateThreshold(th);
		}else {
			thresholdService.saveThreshold(th);
		}
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		CommandGroup cg = new CommandGroup(null);
		List<Threshold> listThreshold = thresholdService.findThresholdByPage(page);
		CommandGroup cg3 = MonitorOptionView.updateThresholdPanel(loc, viewFactory, listThreshold,page); 
		cg3.setPath("/monitor");
		JSCommand close = JSCmdLib.dialogClose("f_std");
		cg.add(cg3).add(close);
		outPutXML(response,cg);
		return null;
	}
	
	
	public ModelAndView saveHttpThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String responseTime = request.getParameter("responseTime");
		String retry = request.getParameter("retry");
		String isUsable = request.getParameter("isUsable");
		
		Threshold th = null;
		if(Utils.notEmpty(id)) {
			th = thresholdService.getThresholdById(id);
			if(th == null) {
				AlertCommand alt = new AlertCommand("alt", "获取阀值模板失败！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			th.setUpdateTime(new Date());
		}else {
			th = new Threshold();
			th.setCreateTime(new Date());
			th.setName(name);
			if(Utils.isEmpty(name)) {
				AlertCommand alt = new AlertCommand("alt", "阀值名称不能为空！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}else {
				List<Threshold> list = thresholdService.findThresholdByName(Threshold.Type.http.getValue(),name);
				if(Utils.notEmpty(list)) {
					AlertCommand alt = new AlertCommand("alt", "阀值名称已经存在！",
							"img/p/alert-warn.gif", DialogPosition.middle, 5);
					outPutXML(response, alt);
					return null;
				}
			}
			th.setIsDefault(Constants.OPTION_IS_NO);
			th.setType(Threshold.Type.http.getValue());
		}
		StringBuffer content = new StringBuffer();
		content.append("responseTime:").append(responseTime);
		th.setContent(content.toString());
		th.setRetry(Integer.parseInt(retry));
		th.setIsUsable(isUsable);
		if(Utils.notEmpty(id)) {
			thresholdService.updateThreshold(th);
		}else {
			thresholdService.saveThreshold(th);
		}
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		CommandGroup cg = new CommandGroup(null);
		List<Threshold> listThreshold = thresholdService.findThresholdByPage(page);
		CommandGroup cg3 = MonitorOptionView.updateThresholdPanel(loc, viewFactory, listThreshold,page); 
		cg3.setPath("/monitor");
		JSCommand close = JSCmdLib.dialogClose("f_std");
		cg.add(cg3).add(close);
		outPutXML(response,cg);
		return null;
	}
	
	public ModelAndView deleteThread(HttpServletRequest request,
			HttpServletResponse response) {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String id = request.getParameter("id");
		if(Utils.isEmpty(id)) {
			AlertCommand alt = new AlertCommand("alt", "获取阀值模板失败！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5);
			outPutXML(response, alt);
			return null;
		}else {
			Threshold th = new Threshold();
			th.setId(id);
			thresholdService.deleteThreshold(th);
		}
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Threshold> listThreshold = thresholdService.findThresholdByPage(page);
		CommandGroup cg = MonitorOptionView.updateThresholdPanel(loc, viewFactory, listThreshold,page);
		outPutXML(response,cg);
		return null;
	}
	
	/**
	 * 翻页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView turnPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Threshold> listThreshold = thresholdService.findThresholdByPage(page);
		CommandGroup cg = MonitorOptionView.updateThresholdPanel(loc, viewFactory, listThreshold,page);
		outPutXML(response,cg);
		return null;
	}

	public void setThresholdService(ThresholdService thresholdService) {
		this.thresholdService = thresholdService;
	}

}
