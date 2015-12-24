package com.rongji.websiteMonitor.webapp.subproject.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.getMsg;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

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
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.EventTarget;
import com.rongji.dfish.engines.xmltmpl.FormElement;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.WidgetControlCommand;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.DeleteCommand;
import com.rongji.dfish.engines.xmltmpl.command.InsertCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.engines.xmltmpl.component.GroupingGridPanel;
import com.rongji.dfish.engines.xmltmpl.form.Combobox;
import com.rongji.dfish.engines.xmltmpl.form.Hidden;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.quartz.SchedulerMethods;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.MonitoringPointService;
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.service.ProjectService;
import com.rongji.websiteMonitor.service.SubprojectService;
import com.rongji.websiteMonitor.service.TaskService;
import com.rongji.websiteMonitor.service.ThresholdService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.subproject.view.SubprojectView;

public class SubprojectController extends BaseActionController {
	private TaskService taskService;
	private MonitoringTypeService typeService;
	private MonitoringPointService pointService;
	private ThresholdService thresholdService;
	private SubprojectService subprojectService;
	private ProjectService projectService;

	public MonitoringPointService getPointService() {
		return pointService;
	}

	public void setPointService(MonitoringPointService pointService) {
		this.pointService = pointService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
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
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);

		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Project> listProject = projectService.findProjectByPage(page);
		Map<Project, List<Subproject>> mapDatas = new HashMap<Project, List<Subproject>>();
		if (Utils.notEmpty(listProject)) {
			for (Project p : listProject) {
				List<Subproject> listSubproject = subprojectService
						.finSubprojectByProjectId(p.getId());
				if(Utils.notEmpty(listSubproject)) {
					mapDatas.put(p, listSubproject);
				}
			}
		}
		List<Subproject> listAchive = subprojectService.findSubprojectByNullProject();
		if(Utils.notEmpty(listAchive)) {
			Project p = new Project();
			p.setName("归档服务");
			mapDatas.put(p, listAchive);
		}
		BaseView view = SubprojectView.buildIndexView(loc, viewFactory,
				mapDatas, page);
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
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String id = Utils.getParameter(request, "id");
		String type = Utils.getParameter(request, "type");
		if (Constants.SNMP_TYPE.equals(type)) {
			List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.snmp.getValue(),false);
			List<Monitortype> typeList = typeService.getMonitorTypesByPage(
					null, false);
			List<Project> listProject = projectService.findAllProject();
			Subproject subproject = null;
			if (Utils.notEmpty(id)) {
				subproject = subprojectService.getSubprojectById(id);
				if (subproject == null) {
					outPutXML(response, new AlertCommand("alt", "获取ID为[" + id
							+ "]的监控服务失败！", "img/p/alert-crack.gif",
							DialogPosition.middle, 5));
				}
			} else {
				subproject = new Subproject();
				subproject.setType(type);
			}
			CommandGroup cg = SubprojectView.updateSnmpView4AddEdit(loc,
					viewFactory, subproject, typeList, listThreshold,
					listProject);
			outPutXML(response, cg);
			return null;
		} else {
			List<Monitortype> typeList = typeService.getMonitorTypesByPage(
					null, false);
			List<Project> listProject = projectService.findAllProject();
			Subproject subproject = null;
			boolean isArchive = false;
			if (Utils.notEmpty(id)) {
				subproject = subprojectService.getSubprojectById(id);
				if (subproject == null) {
					outPutXML(response, new AlertCommand("alt", "获取ID为[" + id
							+ "]的监控服务失败！", "img/p/alert-crack.gif",
							DialogPosition.middle, 5));
				}
				if(Utils.isEmpty(subproject.getProjectId())) {
					isArchive = true;
				}
			} else {
				subproject = new Subproject();
				subproject.setType(type);
			}
			CommandGroup cg = null;
			if(isArchive) {
				cg = SubprojectView.addArchiveView(loc, viewFactory,
						subproject, typeList);
			}else {
				List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.http.getValue(), false);
				cg = SubprojectView.updateView4AddEdit(loc,
						viewFactory, subproject, typeList, listProject,listThreshold);
			}
			outPutXML(response, cg);
			return null;
		}
	}

	public ModelAndView addArchive(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String id = Utils.getParameter(request, "id");
		Subproject subproject = null;
		if (Utils.notEmpty(id)) {
			subproject = subprojectService.getSubprojectById(id);
			if (subproject == null) {
				outPutXML(response, new AlertCommand("alt", "获取ID为[" + id
						+ "]的监测服务失败！", "img/p/alert-crack.gif",
						DialogPosition.middle, 5));
				return null;
			}
		}
		List<Monitortype> typeList = typeService.getMonitorTypesByPage(null,
				false);
		CommandGroup cg = SubprojectView.addArchiveView(loc, viewFactory,
				subproject, typeList);
		outPutXML(response, cg);
		return null;
	}

	public ModelAndView saveArchive(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String id = Utils.getParameter(request, "id");
		String name = Utils.getParameter(request, "name");
		String cronExpression = Utils.getParameter(request, "cronExpression");
		String type = Utils.getParameter(request, "type");
		String isuable = Utils.getParameter(request, "isusable");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		// 验证名称是否已存在
		boolean isNew = Utils.isEmpty(id); // 判断是否新建
		Subproject subproject = null;
		
		if (isNew) {
			subproject = new Subproject();
			subproject.setCreateTime(new Date());
			subproject.setClzName("com.rongji.websiteMonitor.common.trigger.ArchiveTrigger");
		} else {
			subproject = subprojectService.getSubprojectById(id);
			if (subproject == null) {
				outPutXML(response, new AlertCommand("alt", "保存失败！",
						"img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		boolean isExist = Utils.isEmpty(name);
		if (isExist) {
			outPutXML(response, new AlertCommand("alt", "名称不能为空！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
			
		} 
		isExist = Utils.isEmpty(cronExpression);
		if (isExist) {
			outPutXML(response, new AlertCommand("alt", "时钟方式不能为空！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		subproject.setName(name);
		subproject.setType(type);
		subproject.setIsuable(isuable);
		subproject.setCronExpression(cronExpression);
		
		
		CommandGroup cg = new CommandGroup("");
		if (isNew) {
			subprojectService.insertSubproject(subproject);
			cg.add(new AlertCommand("alt", getMsg(loc, "添加成功"),
					"img/p/alert-info.gif", DialogPosition.southeast, 5));
		} else {
			subprojectService.updateSubproject(subproject);
			cg.add(new AlertCommand("alt", getMsg(loc, "修改成功"),
					"img/p/alert-info.gif", DialogPosition.southeast, 5));
		}
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Project> listProject = projectService.findProjectByPage(page);
		Map<Project, List<Subproject>> mapDatas = new HashMap<Project, List<Subproject>>();
		if (Utils.notEmpty(listProject)) {
			for (Project p : listProject) {
				List<Subproject> listSubproject = subprojectService
						.finSubprojectByProjectId(p.getId());
				if(Utils.notEmpty(listSubproject)) {
					mapDatas.put(p, listSubproject);
				}
			}
		}
		List<Subproject> listAchive = subprojectService.findSubprojectByNullProject();
		if(Utils.notEmpty(listAchive)) {
			Project p = new Project();
			p.setName("归档服务");
			mapDatas.put(p, listAchive);
		}
		
		
		GroupingGridPanel grid = new GroupingGridPanel("grid");
		grid.setId("grid");
		// 将保存的定时任务激活
		SchedulerMethods.loadJobs();

		UpdateCommand ucGrid = new UpdateCommand("uc_grid"); // 更新GridPanel命令
		 SubprojectView.fillGridPanel(loc, ucGrid.getView(), viewFactory,
		 grid, mapDatas);
		ucGrid.getView().addSubPanel(grid);
		cg.add(ucGrid);
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
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);

		String id = Utils.getParameter(request, "id");
		String name = Utils.getParameter(request, "name");
		String type = Utils.getParameter(request, "type");
		String projectId = Utils.getParameter(request, "projectId");
		String url = Utils.getParameter(request, "url");
		String frequency = Utils.getParameter(request, "frequency");
		String isExternal = Utils.getParameter(request, "isExternal");
		String warningFrequency = Utils.getParameter(request,
				"warningFrequency");
		String retry = Utils.getParameter(request, "retry");
		String isusable = Utils.getParameter(request, "isuable");
		String monitorPoint = Utils.getParameter(request,"monitorPoint");
		String responseTime = Utils.getParameter(request, "threshold");
		String[] notifyType = request
				.getParameterValues("checkboxgroup");
		Subproject subproject = null;
		if (Utils.isEmpty(id)) {
			subproject = new Subproject();
			if (Utils.isEmpty(name)) {
				AlertCommand alt = new AlertCommand("alt", "项目服务名称不能为空！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			List<Subproject> list = subprojectService.findSubprojectByName(
					name, projectId);
			if (Utils.notEmpty(list)) {
				AlertCommand alt = new AlertCommand("alt", "项目服务名称已存在！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			subproject.setName(name);
			subproject.setType(type);
			subproject
					.setClzName("com.rongji.websiteMonitor.common.trigger.MonitorTrigger");
			subproject.setCreateTime(new Date());
		} else {
			subproject = subprojectService.getSubprojectById(id);
			if (subproject == null) {
				AlertCommand alt = new AlertCommand("alt", "获取项目服务失败！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
		}
		subproject.setProjectId(projectId);
		if (Utils.isEmpty(url)) {
			AlertCommand alt = new AlertCommand("alt", "项目服务地址不能为空！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5);
			outPutXML(response, alt);
			return null;
		}
		subproject.setUrl(url);
		subproject.setRetry(Integer.parseInt(retry));
		subproject.setFrequency(Integer.parseInt(frequency));
		subproject.setWarningFrequency(Integer.parseInt(warningFrequency));
		subproject.setCronExpression("0 */" + frequency + " * * * ?");
		subproject.setIsuable(isusable);
		subproject.setMonitorPoint(monitorPoint);
		subproject.setIsExternal(isExternal);
		subproject.setConfigXml("continueGroup:" + responseTime);
		List<Map<String, String>> notifyTypeList = new ArrayList<Map<String,String>>();
		Map<String, String> item = null;
		if(notifyType!=null&&notifyType.length>0){
			for(String temp:notifyType){
				item = new HashMap<String, String>();
				if(Utils.notEmpty(temp)&&"eMail".equals(temp)){
					item.put("type", temp);
					item.put("content", Utils.getParameter(request, "eMail"));
				}else if(Utils.notEmpty(temp)&&"phone".equals(temp)){
					item.put("type", temp);
					item.put("content", Utils.getParameter(request, "phone"));
				}
				notifyTypeList.add(item);
			}
		}
		Gson gson = new Gson();
		subproject.setNotification(gson.toJson(notifyTypeList));

		CommandGroup cg = new CommandGroup(null);
		if (Utils.isEmpty(id)) {
			subprojectService.insertSubproject(subproject);
			cg.add(new AlertCommand("alt", getMsg(loc, "添加成功"),
					"img/p/alert-info.gif", DialogPosition.southeast, 5));
		} else {
			subprojectService.updateSubproject(subproject);
			cg.add(new AlertCommand("alt", getMsg(loc, "修改成功"),
					"img/p/alert-info.gif", DialogPosition.southeast, 5));
		}
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		// 保存数据后更新相关面板
		List<Monitortype> typeList = typeService.getMonitorTypesByPage(null,
				false);
		List<Project> listProject = projectService.findAllProject();
		List<Project> listPageProject = projectService.findProjectByPage(page);
		Map<Project, List<Subproject>> mapDatas = new HashMap<Project, List<Subproject>>();
		if (Utils.notEmpty(listPageProject)) {
			for (Project p : listPageProject) {
				List<Subproject> listSubproject = subprojectService
						.finSubprojectByProjectId(p.getId());
				if(Utils.notEmpty(listSubproject)) {
					mapDatas.put(p, listSubproject);
				}
			}
		}
		List<Subproject> listAchive = subprojectService.findSubprojectByNullProject();
		if(Utils.notEmpty(listAchive)) {
			Project p = new Project();
			p.setName("归档服务");
			mapDatas.put(p, listAchive);
		}
		SchedulerMethods.loadJobs();
		List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.http.getValue(), false);
		cg.add(SubprojectView.updateView4Save(loc, viewFactory,
				mapDatas, page, subproject, typeList, listProject, listThreshold));
		outPutXML(response, cg);
		return null;
	}

	public ModelAndView saveSnmp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);

		String id = Utils.getParameter(request, "id");
		String name = Utils.getParameter(request, "name");
		String type = Utils.getParameter(request, "type");
		String projectId = Utils.getParameter(request, "projectId");
		String url = Utils.getParameter(request, "url");
//		String isExternal = Utils.getParameter(request, "isExternal");
		String frequency = Utils.getParameter(request, "frequency");
		String warningFrequency = Utils.getParameter(request,
				"warningFrequency");
		String retry = Utils.getParameter(request, "retry");
		String isusable = Utils.getParameter(request, "isusable");
		StringBuilder sb = new StringBuilder();
		String continueGroup = request.getParameter("threshold");
//		String[] continueGroup = request
//				.getParameterValues("continueWarninggroup");
		Subproject subproject = null;
		if (Utils.isEmpty(id)) {
			subproject = new Subproject();
			if (Utils.isEmpty(name)) {
				AlertCommand alt = new AlertCommand("alt", "项目服务名称不能为空！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			List<Subproject> list = subprojectService.findSubprojectByName(
					name, projectId);
			if (Utils.notEmpty(list)) {
				AlertCommand alt = new AlertCommand("alt", "项目服务名称已存在！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			subproject.setName(name);
			subproject.setType(type);
			subproject
					.setClzName("com.rongji.websiteMonitor.common.trigger.SnmpTrigger");
			subproject.setCreateTime(new Date());
		} else {
			subproject = subprojectService.getSubprojectById(id);
			if (subproject == null) {
				AlertCommand alt = new AlertCommand("alt", "获取项目服务失败！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
		}
		subproject.setProjectId(projectId);
		if (Utils.isEmpty(url)) {
			AlertCommand alt = new AlertCommand("alt", "项目服务地址不能为空！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5);
			outPutXML(response, alt);
			return null;
		}
		subproject.setUrl(url);
		subproject.setRetry(Integer.parseInt(retry));
		subproject.setFrequency(Integer.parseInt(frequency));
		subproject.setWarningFrequency(Integer.parseInt(warningFrequency));
		subproject.setCronExpression("0 */" + frequency + " * * * ?");
		subproject.setIsuable(isusable);
//		subproject.setIsExternal(isExternal);
//		sb.append("continueGroup:" + StringUtils.join(continueGroup, ",") + ";");
//		String email;
//		String phone;
//		for (String str : continueGroup) {
//			email = Utils.getParameter(request, "email" + str);
//			phone = Utils.getParameter(request, "phone" + str);
//			if (Utils.notEmpty(email)) {
//				sb.append("email" + str + ":" + email + ";");
//			}
//			if (Utils.notEmpty(phone)) {
//				sb.append("phone" + str + ":" + phone + ";");
//			}
//		}
		if(Utils.notEmpty(continueGroup)) {
			sb.append("continueGroup:" + continueGroup + ";");
			String email = Utils.getParameter(request, "email");
			String phone = Utils.getParameter(request, "phone");
			if (Utils.notEmpty(email)) {
				sb.append("email"+continueGroup  + ":" + email + ";");
			}
			if (Utils.notEmpty(phone)) {
				sb.append("phone"+continueGroup + ":" + phone + ";");
			}
		}
		subproject.setConfigXml(sb.toString());

		CommandGroup cg = new CommandGroup(null);
		if (Utils.isEmpty(id)) {
			subprojectService.insertSubproject(subproject);
			cg.add(new AlertCommand("alt", getMsg(loc, "添加成功"),
					"img/p/alert-info.gif", DialogPosition.southeast, 5));
		} else {
			subprojectService.updateSubproject(subproject);
			cg.add(new AlertCommand("alt", getMsg(loc, "修改成功"),
					"img/p/alert-info.gif", DialogPosition.southeast, 5));
		}
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		// 保存数据后更新相关面板
		List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.snmp.getValue(),false);
		List<Monitortype> typeList = typeService.getMonitorTypesByPage(null,
				false);
		List<Project> listProject = projectService.findAllProject();
		List<Project> listPageProject = projectService.findProjectByPage(page);
		Map<Project, List<Subproject>> mapDatas = new HashMap<Project, List<Subproject>>();
		if (Utils.notEmpty(listPageProject)) {
			for (Project p : listPageProject) {
				List<Subproject> listSubproject = subprojectService
						.finSubprojectByProjectId(p.getId());
				if(Utils.notEmpty(listSubproject)) {
					mapDatas.put(p, listSubproject);
				}
			}
		}
		List<Subproject> listAchive = subprojectService.findSubprojectByNullProject();
		if(Utils.notEmpty(listAchive)) {
			Project p = new Project();
			p.setName("归档服务");
			mapDatas.put(p, listAchive);
		}
		SchedulerMethods.loadJobs();
		cg.add(SubprojectView.updateSnmpView4Save(loc, viewFactory, subproject,
				mapDatas, page, typeList, listThreshold, listProject));
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
	public ModelAndView delete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);

		String[] ids = request.getParameterValues("selectItem");

		subprojectService.deleteSubprojectByIds(ids);

		// 数据成功删除后，处理一序列动作
		CommandGroup cg = new CommandGroup(null);
		String currentPage = Utils.getParameter(request, "cp");
		if (Utils.isEmpty(currentPage)) {
			currentPage = "1";
		}
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);

		// 重新激活定时任务
		SchedulerMethods.loadJobs();

		List<Monitortype> typeList = typeService.getMonitorTypesByPage(null,
				false);
		List<Project> listProject = projectService.findAllProject();
		List<Project> listPageProject = projectService.findProjectByPage(page);
		Map<Project, List<Subproject>> mapDatas = new HashMap<Project, List<Subproject>>();
		if (Utils.notEmpty(listPageProject)) {
			for (Project p : listPageProject) {
				List<Subproject> listSubproject = subprojectService
						.finSubprojectByProjectId(p.getId());
				if(Utils.notEmpty(listSubproject)) {
					mapDatas.put(p, listSubproject);
				}
			}
		}
		List<Subproject> listAchive = subprojectService.findSubprojectByNullProject();
		if(Utils.notEmpty(listAchive)) {
			Project p = new Project();
			p.setName("归档服务");
			mapDatas.put(p, listAchive);
		}
		Subproject subproject = new Subproject();
		subproject.setType(Constants.HTTP_TYPE);
		List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.http.getValue(), false);
		 cg.add(SubprojectView.updateView4Delete(loc, viewFactory, mapDatas,
		 page, subproject, typeList, listProject,listThreshold));
		cg.add(new AlertCommand("alt", getMsg(loc, "p.msg.del_success"),
				"img/p/alert-info.gif", DialogPosition.southeast, 5));
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
	public ModelAndView turnPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);

		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Project> listProject = projectService.findProjectByPage(page);
		List<Monitortype> typeList = typeService.getMonitorTypesByPage(page, true);
		Map<Project, List<Subproject>> mapDatas = new HashMap<Project, List<Subproject>>();
		if (Utils.notEmpty(listProject)) {
			for (Project p : listProject) {
				List<Subproject> listSubproject = subprojectService
						.finSubprojectByProjectId(p.getId());
				if(Utils.notEmpty(listSubproject)) {
					mapDatas.put(p, listSubproject);
				}
			}
		}
		List<Subproject> listAchive = subprojectService.findSubprojectByNullProject();
		if(Utils.notEmpty(listAchive)) {
			Project p = new Project();
			p.setName("归档服务");
			mapDatas.put(p, listAchive);
		}
		Subproject subproject = new Subproject();
		subproject.setType(Constants.HTTP_TYPE);
		List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.http.getValue(), false);
		 CommandGroup cg = SubprojectView.updateView4Save(loc, viewFactory, mapDatas, page, subproject, typeList, listProject,listThreshold);
		outPutXML(response, cg);
		return null;
	}

	public ModelAndView typeGridOptions(HttpServletRequest request,
			HttpServletResponse response) {

		Locale loc = null;// getLocale(request);// 国际化所用
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String isExternal = request.getParameter("isExternal");
		Page page = new Page();
		page.setPageSize(50);
		page.setCurrentPage(1);
		List<MonitoringPoint> listMonitorPoint = null;
		if(Constants.OPTION_IS_NO.equals(isExternal)) {
			listMonitorPoint = pointService.findMonitorPointsByPageAndIsExternal(page, true, isExternal);
		}else {
			listMonitorPoint = pointService.getMonitorPointsByPage(page, true);
			
		}
		
		outPutXML(response,SubprojectView.getOptionGridView(loc, viewFactory,listMonitorPoint));
		return null;
	}
	
	public ModelAndView typeRefresh(HttpServletRequest request,
			HttpServletResponse response) {
		String isExternal = request.getParameter("isExternal");
		CommandGroup cg = new CommandGroup(null);
		UpdateCommand uc = new UpdateCommand(null);
		Combobox monitoerPoint = new Combobox("monitorPoint", "监控点",
				"", "loading",
				"subproject.sp?act=typeGridOptions&isExternal="+isExternal, null,
				"VM(this).cmd('alert','$0')", 200, false, false, false, true,
				false, true);
		monitoerPoint.setNotnull(true);
		uc.setContent(monitoerPoint);
		cg.add(uc);
		outPutXML(response,cg);
		return null;
	}


	public ModelAndView insert(HttpServletRequest request,
			HttpServletResponse response) {
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String numStr = request.getParameter("num");
		int num = 0;
		if (Utils.notEmpty(numStr)) {
			num = Integer.parseInt(numStr);
		}
		CommandGroup cg = new CommandGroup("dd");
		InsertCommand ic = new InsertCommand("ee");
		ic.setTarget("continueWarninggroup");
		ic.setWhere(InsertCommand.WEHRE_AFTEREND);
		List<FormElement> list = SubprojectView.createElement(viewFactory,
				new HashMap<String, String>(), num);
		ic.setContent(list.toArray(new FormElement[list.size()]));
		cg.add(ic);
		outPutXML(response, cg);
		return null;
	}

	public ModelAndView deleteThreshold(HttpServletRequest request,
			HttpServletResponse response) {
//		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
//				.getSkin(request));
		String num = request.getParameter("num");
		CommandGroup cg = new CommandGroup("dd");
		DeleteCommand hr = new DeleteCommand("hr");
		hr.setTarget("hr" + num);
		hr.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(hr);

		DeleteCommand cpu = new DeleteCommand("cpu");
		cpu.setTarget("cpu" + num);
		cpu.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(cpu);

		DeleteCommand memory = new DeleteCommand("memory");
		memory.setTarget("memory" + num);
		memory.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(memory);

		DeleteCommand jvmMemory = new DeleteCommand("jvmMemory");
		jvmMemory.setTarget("jvmMemory" + num);
		jvmMemory.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(jvmMemory);

		DeleteCommand jvmThread = new DeleteCommand("jvmThread");
		jvmThread.setTarget("jvmThread" + num);
		jvmThread.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(jvmThread);

		DeleteCommand io = new DeleteCommand("io");
		io.setTarget("io" + num);
		io.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(io);

		DeleteCommand retry = new DeleteCommand("retry");
		retry.setTarget("retry" + num);
		retry.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(retry);

		DeleteCommand isuse = new DeleteCommand("isuse");
		isuse.setTarget("isuse" + num);
		isuse.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(isuse);

		DeleteCommand checkboxgroup = new DeleteCommand("checkboxgroup");
		checkboxgroup.setTarget("checkboxgroup" + num);
		checkboxgroup.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(checkboxgroup);

		// DeleteCommand delBtn = new DeleteCommand("delBtn");
		// delBtn.setTarget("delBtn"+num);
		// delBtn.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		// cg.add(delBtn);

		DeleteCommand label = new DeleteCommand("label");
		label.setTarget("label" + num);
		label.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		cg.add(label);

		// DeleteCommand vg = new DeleteCommand("vg");
		// vg.setTarget("vg"+num);
		// vg.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		// cg.add(vg);

		// DeleteCommand retry = new DeleteCommand("retry");
		// retry.setTarget("retry"+num);
		// retry.setType(WidgetControlCommand.TYPE_FORMELEMENT);
		// cg.add(retry);

		outPutXML(response, cg);
		return null;
	}

	public ModelAndView showThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		outPutXML(response, SubprojectView.buildThresholdView());
		return null;
	}
	
	public ModelAndView showHttpThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		outPutXML(response, SubprojectView.buildHttpThresholdView());
		return null;
	}
	
	public ModelAndView saveThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String cpu = request.getParameter("cpu");
		String memory = request.getParameter("memory");
		String jvmMemory = request.getParameter("jvmMemory");
		String jvmThread = request.getParameter("jvmThread");
//		String io = request.getParameter("io");
		String retry = request.getParameter("retry");
		String diskUsage = request.getParameter("diskUsage");
		String systemProcess = request.getParameter("systemProcess");
		
		
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
		
		Threshold th = new Threshold();
		th.setName(name);
		StringBuffer content = new StringBuffer();
		content.append("cpu:").append(cpu)
			.append(";memory:").append(memory)
			.append(";jvmMemory:").append(jvmMemory)
			.append(";jvmThread:").append(jvmThread)
			.append(";diskUsage:").append(diskUsage)
			.append(";systemProcess:").append(systemProcess);
		th.setContent(content.toString());
		th.setRetry(Integer.parseInt(retry));
		th.setType(Threshold.Type.snmp.getValue());
		th.setCreateTime(new Date());
		th.setIsUsable(Constants.OPTION_IS_YES);
		th.setIsDefault(Constants.OPTION_IS_NO);
		thresholdService.saveThreshold(th);
		
		CommandGroup cg = new CommandGroup(null);
		CommandGroup cg2 = new CommandGroup(null);
		UpdateCommand uc = new UpdateCommand(null);
		List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.snmp.getValue(), false);
		List<Object[]> o5 = new ArrayList<Object[]>();  
		if (listThreshold != null) {
			for (int i = 0; i < listThreshold.size(); i++) {
				Threshold threshold = listThreshold.get(i);
				o5.add(new Object[]{threshold.getId(), threshold.getName()});
			}
		}
		
		Select select = new Select("threshold","阀值模板", new Object[] {th.getId()}, o5);
		select.setRemark("&nbsp;&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"VM(this).cmd('addThreshold');\" style='color:#87C3FD'>添加阀值模板</a>");
		StringBuffer sb = new StringBuffer();
		sb.append("var value = VM(this).f('threshold',DFish.GET_VALUE );var jsonStr = VM(this).f(value,DFish.GET_VALUE );")
		.append("var thJson = JSON.parse(jsonStr);")
		.append("VM(this).fs('cpu',thJson.cpu?thJson.cpu:'未设置');")
		.append("VM(this).fs('memory',thJson.memory?thJson.memory:'未设置');")
		.append("VM(this).fs('jvmMemory',thJson.jvmMemory?thJson.jvmMemory:'未设置');")
		.append("VM(this).fs('jvmThread',thJson.jvmThread?thJson.jvmThread:'未设置');")
//		.append("VM(this).fs('io',thJson.io);")
		.append("VM(this).fs('thresholdRetry',thJson.retry?thJson.retry:'未设置');")
		.append("VM(this).fs('diskUsage',thJson.diskUsage?thJson.diskUsage:'未设置');")
		.append("VM(this).fs('systemProcess',thJson.systemProcess?thJson.systemProcess:'未设置');");
		select.setOn(EventTarget.EVENT_CHANGE,sb.toString());
		Label cpuElement = new Label("cpu", "cpu使用预警（百分比%）",	cpu);

		Label memorycpuElement = new Label("memory", "系统内存使用预警（百分比%）",	memory);

		Label jvmMemorycpuElement = new Label("jvmMemory", "JVM内存使用预警（百分比%）",jvmMemory);

		Label jvmThreadcpuElement = new Label("jvmThread", "JVM线程使用预警（数量）",	jvmThread);

//		Label iocpuElement = new Label("io", "网络流量（Kb/s）", io);
		
		Label diskUsageElement = new Label("diskUsage", "系统使用率（百分比%）", diskUsage);
		
		Label systemProcessElement = new Label("systemProcess", "系统进程数", systemProcess);
		
		Label adminRetrycpuElement = new Label("thresholdRetry", "重试几次后告警",
				th.getRetry());
		uc.setContent(select);
		cg2.add(uc);
		
		UpdateCommand uc2 = new UpdateCommand(null);
		uc2.setContent(cpuElement);
		cg2.add(uc2);
		
		UpdateCommand uc3 = new UpdateCommand(null);
		uc3.setContent(memorycpuElement);
		cg2.add(uc3);
		
		UpdateCommand uc4 = new UpdateCommand(null);
		uc4.setContent(jvmMemorycpuElement);
		cg2.add(uc4);
		
		UpdateCommand uc5 = new UpdateCommand(null);
		uc5.setContent(jvmThreadcpuElement);
		cg2.add(uc5);
		
//		UpdateCommand uc6 = new UpdateCommand(null);
//		uc6.setContent(iocpuElement);
//		cg2.add(uc6);
		
		UpdateCommand uc7 = new UpdateCommand(null);
		uc7.setContent(adminRetrycpuElement);
		cg2.add(uc7);
		
		UpdateCommand uc8 = new UpdateCommand(null);
		uc8.setContent(diskUsageElement);
		cg2.add(uc8);
		
		
		UpdateCommand uc9 = new UpdateCommand(null);
		uc9.setContent(systemProcessElement);
		cg2.add(uc9);
		
		
		
		cg2.setPath("/monitor");
		InsertCommand ic = new InsertCommand(null);
		ic.setTarget("threshold");
		ic.setWhere(InsertCommand.WEHRE_AFTEREND);
		Threshold threshold = listThreshold.get(0);
		Map<String, String> config = Utils.string2Map(threshold.getContent());
		config.put("retry", threshold.getRetry()+"");
		Hidden hidden = new Hidden(listThreshold.get(0).getId(), new Gson().toJson(config));
		ic.setContent(hidden);
		cg2.add(ic);
		
		JSCommand close = JSCmdLib.dialogClose("f_std");
		cg.add(cg2).add(close);
		outPutXML(response,cg);
		
		return null;
	}
	
	public ModelAndView saveHttpThreshold(HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		String responseTime = request.getParameter("responseTime");
		String retry = request.getParameter("retry");
		
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
		
		Threshold th = new Threshold();
		th.setName(name);
		StringBuffer content = new StringBuffer();
		content.append("responseTime:").append(responseTime);
		th.setContent(content.toString());
		th.setRetry(Integer.parseInt(retry));
		th.setType(Threshold.Type.http.getValue());
		th.setCreateTime(new Date());
		th.setIsUsable(Constants.OPTION_IS_YES);
		th.setIsDefault(Constants.OPTION_IS_NO);
		thresholdService.saveThreshold(th);
		
		CommandGroup cg = new CommandGroup(null);
		CommandGroup cg2 = new CommandGroup(null);
		UpdateCommand uc = new UpdateCommand(null);
		List<Threshold> listThreshold = thresholdService.findAllThreshold(Threshold.Type.http.getValue(), false);
		List<Object[]> o5 = new ArrayList<Object[]>();  
		if (listThreshold != null) {
			for (int i = 0; i < listThreshold.size(); i++) {
				Threshold threshold = listThreshold.get(i);
				o5.add(new Object[]{threshold.getId(), threshold.getName()});
			}
		}
		
		Select select = new Select("threshold","阀值模板", new Object[] {th.getId()}, o5);
		select.setRemark("&nbsp;&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"VM(this).cmd('addHttpThreshold');\" style='color:#87C3FD'>添加阀值模板</a>");
		StringBuffer sb = new StringBuffer();
		sb.append("var value = VM(this).f('threshold',DFish.GET_VALUE );var jsonStr = VM(this).f(value,DFish.GET_VALUE );")
		.append("var thJson = JSON.parse(jsonStr);")
		.append("VM(this).fs('responseTime',thJson.responseTime);")
		.append("VM(this).fs('thresholdRetry',thJson.retry);");
		select.setOn(EventTarget.EVENT_CHANGE,sb.toString());
		Label cpuElement = new Label("responseTime", "响应时间(ms)",	responseTime);
		Label adminRetrycpuElement = new Label("thresholdRetry", "重试几次后告警",
				th.getRetry());
		uc.setContent(select);
		cg2.add(uc);
		
		UpdateCommand uc2 = new UpdateCommand(null);
		uc2.setContent(cpuElement);
		cg2.add(uc2);
		UpdateCommand uc7 = new UpdateCommand(null);
		uc7.setContent(adminRetrycpuElement);
		cg2.add(uc7);
		
		
		cg2.setPath("/monitor");
		InsertCommand ic = new InsertCommand(null);
		ic.setTarget("threshold");
		ic.setWhere(InsertCommand.WEHRE_AFTEREND);
		Threshold threshold = listThreshold.get(0);
		Map<String, String> config = Utils.string2Map(threshold.getContent());
		config.put("retry", threshold.getRetry()+"");
		Hidden hidden = new Hidden(listThreshold.get(0).getId(), new Gson().toJson(config));
		ic.setContent(hidden);
		cg2.add(ic);
		
		JSCommand close = JSCmdLib.dialogClose("f_std");
		cg.add(cg2).add(close);
		outPutXML(response,cg);
		
		return null;
	}

	public void setThresholdService(ThresholdService thresholdService) {
		this.thresholdService = thresholdService;
	}

	public void setSubprojectService(SubprojectService subprojectService) {
		this.subprojectService = subprojectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
}
