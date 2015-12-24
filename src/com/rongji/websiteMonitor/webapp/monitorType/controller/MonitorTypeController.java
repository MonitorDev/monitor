package com.rongji.websiteMonitor.webapp.monitorType.controller;


import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.getMsg;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

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
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.monitorType.view.MonitorTypeView;

public class MonitorTypeController extends BaseActionController {
	private MonitoringTypeService typeService;
	
	
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
		List<Monitortype> dataList = this.typeService.getMonitorTypesByPage(page, true); 
		BaseView view = MonitorTypeView.buildIndexView(loc,viewFactory, dataList, page);
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
		String mpId = Utils.getParameter(request, "mpId");
		Monitortype point = null;
		if(Utils.notEmpty(mpId)){
			point = typeService.getMonitoringType(mpId);
			if (point == null) {
				outPutXML(response, new AlertCommand("alt", "获取ID为[" + mpId + "]的监测点失败！", "img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		
		
		CommandGroup cg = MonitorTypeView.updateView4AddEdit(loc, viewFactory, point); 
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

		String mpId = Utils.getParameter(request, "mpId");
		String mpName = Utils.getParameter(request, "mpName");
		String mpAlias = Utils.getParameter(request,"mpAlias");
//		String stId = StringUtils.trim(Utils.getParameter(request, "stId"));
		
		// 验证名称是否已存在
		boolean isNew = Utils.isEmpty(mpId); // 判断是否新建
		Monitortype point = null;
		if (isNew) {
			// 验证名称是否已存在
			SearchContainer container = new SearchContainer();
			container.and("mtName", Op.EQ, mpName);
			List <Monitortype> tmpList = typeService.findMonitorTypeByContainer(container, null);
			if (Utils.notEmpty(tmpList)) {
				AlertCommand alt = new AlertCommand("alt", "该名称已定义！", "img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			container = null;
			container = new SearchContainer();
			container.and("mtAlias", Op.EQ, mpAlias);
			tmpList = typeService.findMonitorTypeByContainer(container, null);
			if (Utils.notEmpty(tmpList)) {
				AlertCommand alt = new AlertCommand("alt", "该别名已定义！", "img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			point = new Monitortype();
	
		} else {
			point = typeService.getMonitoringType(mpId);
			if (point == null) {
				outPutXML(response, new AlertCommand("alt", "保存失败！", "img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		
		boolean isExist = Utils.isEmpty(mpName);
		if(!isExist){
			point.setMtName(mpName);
		}else{
			outPutXML(response, new AlertCommand("alt", "名称不能为空！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		
		isExist = Utils.isEmpty(mpAlias);
		if(!isExist){
			point.setMtAlias(mpAlias);
		}else{
			outPutXML(response, new AlertCommand("alt", "别名不能为空！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		
		if (isNew) {
			point = typeService.saveMonitorType(point);
		} else {
			point = typeService.updateMonitorType(point);
		}
		//保存数据后更新相关面板
		CommandGroup cg = new CommandGroup(null);;

		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		
		List <Monitortype> dataList = null;
	
		dataList = typeService.getMonitorTypesByPage(page, true);
			
		
		String monitorPointName = null;
		cg = MonitorTypeView.updateView4Save(loc, viewFactory, dataList, page, point,monitorPointName);
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
	 * 查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		String loginUserId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		String monitorPointName = Utils.getParameter(request, "monitorPointName");
		Page page = FrameworkHelper.createPersonalPage(loginUserId, "1");
		SearchContainer container = new SearchContainer();
		container.and("mpName", Op.LIKE, monitorPointName);
		container.orderByDesc("mpId");
		List <Monitortype> dataList = typeService.findMonitorTypeByContainer(container, page);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		CommandGroup cg = new CommandGroup(null);
		cg.add(MonitorTypeView.updateView4Save(loc, viewFactory, dataList, page, new Monitortype(),monitorPointName));
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

		String[] mpIds = request.getParameterValues("selectItem");
		
		String monitorPointName = Utils.getParameter(request, "monitorPointName");

		
		boolean isSuc = typeService.deleteTypeByIds(mpIds);

		// 数据成功删除后，处理一序列动作
		CommandGroup cg = new CommandGroup(null);
		if (isSuc) {
			String currentPage = Utils.getParameter(request,"cp");
			if(Utils.isEmpty(currentPage)){
				currentPage ="1";
			}
			Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
			
			List <Monitortype>dataList = null;
			if(Utils.isEmpty(monitorPointName)){
				
				dataList = typeService.getMonitorTypesByPage(page, true);
				if(Utils.isEmpty(dataList)){
					if(Integer.parseInt(currentPage)>1){
						currentPage = (Integer.parseInt(currentPage)-1)+"";
						page = FrameworkHelper.createPersonalPage(userId, currentPage);
						dataList = typeService.getMonitorTypesByPage(page, true);
					}	
				}
			}else {
				SearchContainer container = new SearchContainer();
				container.and("mpName", Op.LIKE, monitorPointName);
				container.orderByDesc("mpId");
				dataList = typeService.findMonitorTypeByContainer(container, page);
				if(Utils.isEmpty(dataList)){
					if(Integer.parseInt(currentPage)>1){
						currentPage = (Integer.parseInt(currentPage)-1)+"";
						page = FrameworkHelper.createPersonalPage(userId, currentPage);
						dataList = typeService.findMonitorTypeByContainer(container, page);
					}
				}
				
			}
			cg.add(MonitorTypeView.updateView4Delete(loc, viewFactory, dataList, page, new Monitortype(),monitorPointName)); 
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

		String mpName = Utils.getParameter(request, "mpName");
		
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<Monitortype> dataList = null;
		if(Utils.isEmpty(mpName)){
			dataList = typeService.getMonitorTypesByPage(page, true);
		}else{
			SearchContainer container = new SearchContainer();
			container.and("mpName", Op.LIKE, mpName);
			container.orderByDesc("mpId");
			dataList = typeService.findMonitorTypeByContainer(container, page);
		}
		CommandGroup cg = MonitorTypeView.updateView4Save(loc, viewFactory, dataList, page, new Monitortype(),mpName); 
		outPutXML(response, cg);
		return null;
	}

}
