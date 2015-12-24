package com.rongji.websiteMonitor.webapp.visit.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.DateUtils;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.MonitorDetialService;
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.service.ProjectService;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.service.SubprojectService;
import com.rongji.websiteMonitor.service.TaskService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;
import com.rongji.websiteMonitor.webapp.visit.view.MonitorDetailView;

public class MonitorDetailController extends BaseActionController {
	
	private MonitoringTypeService typeService;
	private MonitorDetialService detialService;
	private SubprojectService subprojectService;
	private ProjectService projectService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	Gson gson = new Gson();
	
	public MonitorDetialService getDetialService() {
		return detialService;
	}


	public void setDetialService(MonitorDetialService detialService) {
		this.detialService = detialService;
	}


	public MonitoringTypeService getTypeService() {
		return typeService;
	}


	public void setTypeService(MonitoringTypeService typeService) {
		this.typeService = typeService;
	}


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
		List<Monitortype> typeList = typeService.getMonitorTypesByPage(null, false);
		QueryCondition condition = new QueryCondition(request);
		Date now = new Date();
		condition.setStartTime(DateUtils.getMonthBegin(now));
		condition.setEndTime(now);
		List<Object[]> dataList = new ArrayList<Object[]>();
		List<Project> listProject = projectService.findProjectByPage(page);
		if(Utils.notEmpty(listProject)) {
			for(Project p : listProject) {
				Object[] o = new Object[]{p.getId(), p.getName()};
				dataList.add(o);
			}
		}
		BaseView view = MonitorDetailView.buildMain(loc,viewFactory,dataList,typeList,page,condition);
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand uc = new UpdateCommand("uc");
		uc.setContent(view);
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}
	
	/**
	 * 查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		QueryCondition condition = new QueryCondition(request);
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Object[]> dataList = detialService.getMonitorDetialByCondition(page,condition);
		CommandGroup cg = new CommandGroup("");
		cg.add(MonitorDetailView.updateView4chuangpage(loc, viewFactory, dataList, page,condition));
		outPutXML(response, cg);
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
		QueryCondition condition = new QueryCondition(request);
		
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Object[]> dataList = detialService.getMonitorDetialByCondition(page,condition);
		CommandGroup cg = new CommandGroup("");
		cg.add(MonitorDetailView.updateView4chuangpage(loc, viewFactory, dataList, page,condition));
		outPutXML(response, cg);
		return null;
	}
	
	
	public void setSubprojectService(SubprojectService subprojectService) {
		this.subprojectService = subprojectService;
	}


	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	
}
