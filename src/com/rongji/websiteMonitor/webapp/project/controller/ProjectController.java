package com.rongji.websiteMonitor.webapp.project.controller;


import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.getMsg;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

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
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.spl.SearchContainer.Op;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.service.ProjectService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.monitorType.view.MonitorTypeView;
import com.rongji.websiteMonitor.webapp.project.view.ProjectView;

public class ProjectController extends BaseActionController {
	private ProjectService projectService;
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
		List<Project> dataList = this.projectService.findProjectByPage(page); 
		BaseView view = ProjectView.buildIndexView(loc,viewFactory, dataList, page);
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
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String id = Utils.getParameter(request, "id");
		Project project = null;
		if(Utils.notEmpty(id)){
			project = projectService.getProjectById(id);
			if (project == null) {
				outPutXML(response, new AlertCommand("alt", "获取ID为[" + id + "]的监测点失败！", "img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		
		
		CommandGroup cg = ProjectView.updateView4AddEdit(loc, viewFactory, project); 
		outPutXML(response, cg);
		return null;
	}
	
	/**
	 * 保存
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		String id = Utils.getParameter(request, "id");
		String name = Utils.getParameter(request, "name");
//		String stId = StringUtils.trim(Utils.getParameter(request, "stId"));
		if(Utils.notEmpty(name)) {
			name = name.trim();
		}
		// 验证名称是否已存在
		boolean isNew = Utils.isEmpty(id); // 判断是否新建
		Project project = null;
		if (isNew) {
			// 验证名称是否已存在
			List <Project> tmpList = projectService.findProjectByName(name);
			if (Utils.notEmpty(tmpList)) {
				AlertCommand alt = new AlertCommand("alt", "该名称已定义！", "img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			project = new Project();
			project.setCreateTime(new Date());
			project.setIsusable("1");
		} else {
			project = projectService.getProjectById(id);
			if (project == null) {
				outPutXML(response, new AlertCommand("alt", "保存失败！", "img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		boolean isExist = Utils.isEmpty(name);
		if(!isExist){
			project.setName(name);
		}else{
			outPutXML(response, new AlertCommand("alt", "名称不能为空！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		
		if (isNew) {
			projectService.insertProject(project);
		} else {
			projectService.updateProject(project);
		}
		//保存数据后更新相关面板
		CommandGroup cg = new CommandGroup(null);;

		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		
		List <Project> dataList = null;
	
		dataList = projectService.findProjectByPage(page);
			
		
		cg = ProjectView.updateView4Save(loc, viewFactory, dataList, page, project);
		if(isNew){
//			cg.add(new JSCommand("","VM(this).f('pointName',DFish.SET_VALUE,'');VM(this).f('stId',DFish.SET_VALUE, '');"));
			cg.add(new AlertCommand("alt", getMsg(loc, "添加成功"), "img/p/alert-info.gif", DialogPosition.southeast, 5));
		}else{
			cg.add(new AlertCommand("alt", getMsg(loc, "修改成功"), "img/p/alert-info.gif", DialogPosition.southeast, 5));
		}
	
		outPutXML(response, cg);
		return null;
	}
	
	/**
	 * 删除
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);

		String[] ids = request.getParameterValues("selectItem");
		boolean isSuc = projectService.deleteTypeByIds(ids);
		// 数据成功删除后，处理一序列动作
		CommandGroup cg = new CommandGroup(null);
		if (isSuc) {
			String currentPage = Utils.getParameter(request,"cp");
			if(Utils.isEmpty(currentPage)){
				currentPage ="1";
			}
			Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
			List <Project>dataList = null;
			dataList = projectService.findProjectByPage(page);
			if(Utils.isEmpty(dataList)){
				if(Integer.parseInt(currentPage)>1){
					currentPage = (Integer.parseInt(currentPage)-1)+"";
					page = FrameworkHelper.createPersonalPage(userId, currentPage);
					dataList = projectService.findProjectByPage(page);
				}	
			}
			cg.add(ProjectView.updateView4Delete(loc, viewFactory, dataList, page, new Project())); 
			cg.add(new AlertCommand("alt", getMsg(loc, "p.msg.del_success"), "img/p/alert-info.gif", DialogPosition.southeast, 5));
		} else {
			cg.add(new AlertCommand("alt", getMsg(loc, "p.msg.del_success"), "img/p/alert-info.gif",
					DialogPosition.southeast, 5));
		}
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

//		String mpName = Utils.getParameter(request, "mpName");
		
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Project> dataList = projectService.findProjectByPage(page);
//		if(Utils.isEmpty(mpName)){
//			dataList = typeService.getMonitorTypesByPage(page, true);
//		}else{
//			SearchContainer container = new SearchContainer();
//			container.and("mpName", Op.LIKE, mpName);
//			container.orderByDesc("mpId");
//			dataList = typeService.findMonitorTypeByContainer(container, page);
//		}
		CommandGroup cg = ProjectView.updateView4Save(loc, viewFactory, dataList, page, new Project()); 
		outPutXML(response, cg);
		return null;
	}
	
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

}
