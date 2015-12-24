package com.rongji.websiteMonitor.webapp.monitorPoint.controller;


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
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.ip.IPv4Filter;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.spl.SearchContainer.Op;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.service.MonitoringPointService;
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.monitorPoint.view.MonitorPointView;

public class MonitorPointController extends BaseActionController {
	private MonitoringPointService pointService;
	private MonitoringTypeService monitortype;

	public MonitoringTypeService getMonitortype() {
		return monitortype;
	}

	public void setMonitortype(MonitoringTypeService monitortype) {
		this.monitortype = monitortype;
	}

	public MonitoringPointService getPointService() {
		return pointService;
	}

	public void setPointService(MonitoringPointService pointService) {
		this.pointService = pointService;
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
		String type = request.getParameter("type");
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		List<MonitoringPoint> dataList = this.pointService.getMonitorPointsByPage(page, true); 
		BaseView view = MonitorPointView.buildIndexView(loc,viewFactory, dataList, page);
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand uc = new UpdateCommand("uc");
		uc.setContent(view);
		cg.add(uc);
		if("index".equals(type)) {
			outPutXML(response, view);
		}else {
			outPutXML(response, cg);
		}
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
		MonitoringPoint point = null;
		if(Utils.notEmpty(mpId)){
			point = pointService.getMonitoringPoint(mpId);
			if (point == null) {
				outPutXML(response, new AlertCommand("alt", "获取ID为[" + mpId + "]的监测点失败！", "img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		
		
		CommandGroup cg = MonitorPointView.updateView4AddEdit(loc, viewFactory, point); 
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
		String mpPort = Utils.getParameter(request, "mpPort");
		String mpIp = Utils.getParameter(request, "mpIp");
		String mpRegion = Utils.getParameter(request, "mpRegion");
		String mpNetType = Utils.getParameter(request, "mpNetType"); 
		String mpmonitorType = Utils.getParameter(request, "mpmonitorType"); 
		String isExternal = Utils.getParameter(request, "isExternal");
		
		String monitorPointName = Utils.getParameter(request, "monitorPointName");
		
		// 验证名称是否已存在
		boolean isNew = Utils.isEmpty(mpId); // 判断是否新建
		MonitoringPoint point = null;
		if (isNew) {
			// 验证名称是否已存在
			List <MonitoringPoint> tmpList = pointService.findMonitorPoints(mpName);
			if (Utils.notEmpty(tmpList)) {
				AlertCommand alt = new AlertCommand("alt", "该名称已定义！", "img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			point = new MonitoringPoint();
			point.setCreatorId(userId);
			point.setCreateTime(new Date());
		} else {
			point = pointService.getMonitoringPoint(mpId);
			if (point == null) {
				outPutXML(response, new AlertCommand("alt", "保存失败！", "img/p/alert-crack.gif", DialogPosition.middle, 5));
				return null;
			}
		}
		
		boolean isExist = Utils.isEmpty(mpName);
		if(!isExist){
			point.setMpName(mpName);
		}else{
			outPutXML(response, new AlertCommand("alt", "名称不能为空！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		isExist = Utils.isEmpty(mpPort);
		if(!isExist){
			String [] ports = mpPort.split(",");
			if(ports!=null&&ports.length>0){
				for(String str:ports){
					if(!isNum(str)){
						outPutXML(response, new AlertCommand("alt", "端口格式不正确！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
						return null;
					}
				}
			}
			point.setMpPort(mpPort);
		}else{
			outPutXML(response, new AlertCommand("alt", "通信端口不能为空！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		isExist = Utils.isEmpty(mpIp);
		 
		if(!isExist){
			boolean isIp = false;
			IPv4Filter filter = null;
	       	 try {
	       		 if(mpIp.trim().equals("")){
	       			outPutXML(response, new AlertCommand("alt", "IP格式不正确！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
	       		 }
	       		 filter = new IPv4Filter(mpIp);
	       		 isIp = true;
				} catch (Exception e) {
					isIp = false;
				}
			if(!isIp){
				outPutXML(response, new AlertCommand("alt", "IP格式不正确！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
				return null;
			}
			if(mpIp.length() > 16) {
				outPutXML(response, new AlertCommand("alt", "IP长度超长！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
				return null;
			}
			point.setMpIp(mpIp);
		}else{
			outPutXML(response, new AlertCommand("alt", "IP不能为空！", "img/p/alert-warn.gif", DialogPosition.middle, 5));
			return null;
		}
		
		point.setMpmonitorType(mpmonitorType);
		point.setMpNetType(mpNetType);
		point.setMpRegion(mpRegion);
		point.setIsExternal(isExternal);
		
		if (isNew) {
			point = pointService.saveMonitorPoint(point);
		} else {
			point = pointService.updateMonitorPoint(point);
		}
		//保存数据后更新相关面板
		CommandGroup cg = new CommandGroup(null);;

		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
		
		List <MonitoringPoint> dataList = null;
		if(!isNew){
			if(Utils.isEmpty(monitorPointName)){
				dataList = pointService.getMonitorPointsByPage(page, true);
			}else{
				SearchContainer container = new SearchContainer();
				container.and("mpName", Op.LIKE, monitorPointName);
				container.orderByDesc("mpId");
				dataList = pointService.findMonitorPointByContainer(container, page);
			}
		}else{
			dataList = pointService.getMonitorPointsByPage(page, true);
			monitorPointName = null;
		}
		
		cg = MonitorPointView.updateView4Save(loc, viewFactory, dataList, page, point,monitorPointName);
		if(isNew){
			cg.add(new AlertCommand("alt", getMsg(loc, "添加成功"), "img/p/alert-info.gif", DialogPosition.southeast, 5));
		}else{
			cg.add(new AlertCommand("alt", getMsg(loc, "修改成功"), "img/p/alert-info.gif", DialogPosition.southeast, 5));
		}
	
		outPutXML(response, cg);
		return null;
	}
	
	private boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
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
		List <MonitoringPoint> dataList = pointService.findMonitorPointByContainer(container, page);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		CommandGroup cg = new CommandGroup(null);
		cg.add(MonitorPointView.updateView4Save(loc, viewFactory, dataList, page, new MonitoringPoint(),monitorPointName));
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

		
		boolean isSuc = pointService.deletepointByIds(mpIds);

		// 数据成功删除后，处理一序列动作
		CommandGroup cg = new CommandGroup(null);
		if (isSuc) {
			String currentPage = Utils.getParameter(request,"cp");
			if(Utils.isEmpty(currentPage)){
				currentPage ="1";
			}
			Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
			
			List <MonitoringPoint>dataList = null;
			if(Utils.isEmpty(monitorPointName)){
				
				dataList = pointService.getMonitorPointsByPage(page, true);
				if(Utils.isEmpty(dataList)){
					if(Integer.parseInt(currentPage)>1){
						currentPage = (Integer.parseInt(currentPage)-1)+"";
						page = FrameworkHelper.createPersonalPage(userId, currentPage);
						dataList = pointService.getMonitorPointsByPage(page, true);
					}	
				}
			}else {
				SearchContainer container = new SearchContainer();
				container.and("mpName", Op.LIKE, monitorPointName);
				container.orderByDesc("mpId");
				dataList = pointService.findMonitorPointByContainer(container, page);
				if(Utils.isEmpty(dataList)){
					if(Integer.parseInt(currentPage)>1){
						currentPage = (Integer.parseInt(currentPage)-1)+"";
						page = FrameworkHelper.createPersonalPage(userId, currentPage);
						dataList = pointService.findMonitorPointByContainer(container, page);
					}
				}
				
			}
			cg.add(MonitorPointView.updateView4Delete(loc, viewFactory, dataList, page, new MonitoringPoint(),monitorPointName)); 
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
		List<MonitoringPoint> dataList = null;
		if(Utils.isEmpty(mpName)){
			dataList = pointService.getMonitorPointsByPage(page, true);
		}else{
			SearchContainer container = new SearchContainer();
			container.and("mpName", Op.LIKE, mpName);
			container.orderByDesc("mpId");
			dataList = pointService.findMonitorPointByContainer(container, page);
		}
		CommandGroup cg = MonitorPointView.updateView4Save(loc, viewFactory, dataList, page, new MonitoringPoint(),mpName); 
		outPutXML(response, cg);
		return null;
	}

	public ModelAndView typeGridOptions(HttpServletRequest request, HttpServletResponse response){
		
		Locale loc = null;// getLocale(request);// 国际化所用
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
        Page page = new Page();
        page.setPageSize(50);
        page.setCurrentPage(1);
		outPutXML(response, MonitorPointView.getOptionGridView(loc,
				viewFactory,monitortype.getMonitorTypesByPage(page, true)));
		return null;
	}
}
