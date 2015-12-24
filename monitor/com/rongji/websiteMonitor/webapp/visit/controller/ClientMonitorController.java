package com.rongji.websiteMonitor.webapp.visit.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.text.DecimalFormat;
import java.text.ParseException;
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
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.JSParser;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.DateUtils;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.persistence.SnmpModel;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.MonitorDetialService;
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.service.ProjectService;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.service.SnmpService;
import com.rongji.websiteMonitor.service.SubprojectService;
import com.rongji.websiteMonitor.service.ThresholdService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;
import com.rongji.websiteMonitor.webapp.visit.help.SubQueryCondition;
import com.rongji.websiteMonitor.webapp.visit.view.ClientMonitorView;
import com.rongji.websiteMonitor.webapp.visit.view.MonitorDetailView;

public class ClientMonitorController extends BaseActionController {
	private MonitoringTypeService typeService;
	private MonitorDetialService detialService;
	private ThresholdService thresholdService;
	private SubprojectService subprojectService;
	private ProjectService projectService;
	private SnmpService snmpService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
	private static final String DAY = "DAY";
	private static final String HOUR = "HOUR";
	private static final String MINUTE = "MINUTE";
	private Gson gson = new Gson();

	public void setSnmpService(SnmpService snmpService) {
		this.snmpService = snmpService;
	}

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
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("startTime");
		request.getSession().removeAttribute("endTime");
		String id = request.getParameter("id");
		String cp = request.getParameter("cp");
		Project project = projectService.getProjectById(id);
		List<Subproject> listSubptoject = subprojectService.finSubprojectByProjectId(id);
		Panel panel = ClientMonitorView.buildMain(project, listSubptoject, cp);
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand uc = new UpdateCommand("");
		uc.setContent(panel);
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}

	public ModelAndView showOverView(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);
		String update = request.getParameter("update");
		QueryCondition condition = new QueryCondition(request);
		String projectId = request.getParameter("projectId");
		String isTimerStr = request.getParameter("isTimer");
		boolean isTimer = false;
		if("true".equals(isTimerStr)) {
			isTimer = true;
		}
		if(Utils.notEmpty(projectId)) {
			request.getSession().setAttribute("projectId", projectId);
		}else {
			projectId = (String) request.getSession().getAttribute("projectId");
			if(Utils.isEmpty(projectId)) {
				return null;
			}
		}
		
		Map<String, Integer> monitorMap = new HashMap<String, Integer>();
		List<Object[]> dataList = new ArrayList<Object[]>();
		List<Subproject> listSubproject = subprojectService.finSubprojectByProjectId(projectId);
		List<Object[]> listTaskResult = detialService.getLatestStatusByProjectId(projectId);
		DecimalFormat df = new DecimalFormat("0.00");
		if(Utils.notEmpty(listSubproject)) {
			for(Subproject sp : listSubproject) {
				if(Constants.SNMP_TYPE.equals(sp.getType().toLowerCase())) {
					if(!"1".equals(sp.getIsuable())) {
						continue;
					}
					String configStr = sp.getConfigXml();
					Threshold threshold = null;
					if(Utils.notEmpty(configStr)) {
						Map<String, String> map = Utils.string2Map(configStr);
						String thresholdId = map.get("continueGroup");
						threshold = thresholdService.getThresholdById(thresholdId);
					}
					SnmpModel snmpModel = snmpService.getLatestSnmp(sp.getId());
					if(snmpModel != null) {
						Object[] obj = new Object[11];
						obj[0] = sp.getName();
						obj[1] = sp.getUrl();
						obj[2] = sdf.format(snmpModel.getCreateTime());
						boolean isError = false;
						if(threshold != null) {
							Map<String, String> map = Utils.string2Map(threshold.getContent());
							
							if(Double.parseDouble(map.get("cpu")) <= Double.parseDouble(snmpModel.getCpuUsedRate())) {
								obj[3] = "<span style='color:red'>" + snmpModel.getCpuUsedRate() + "(阀值:"+map.get("cpu")+")</span>";
								isError = true;
							}else {
								obj[3] = snmpModel.getCpuUsedRate();
							}
							
							double memoryRate = Double.parseDouble(snmpModel.getMemoryUsedSize()) / Double.parseDouble(snmpModel.getMemoryTotalSize()) * 100;
							if(Double.parseDouble(map.get("memory")) <= memoryRate) {
								obj[4] = "<span style='color:red'>" +  df.format(memoryRate) + "(阀值:"+map.get("memory")+")</span>";
								isError = true;
							}else {
								obj[4] = df.format(memoryRate);
							}
							
							double jvmMemoryRate =Double.parseDouble(snmpModel.getJvmHeapUsedSize()) /Double.parseDouble(snmpModel.getJvmHeadTotalSize()) * 100;
							if(Double.parseDouble(map.get("jvmMemory")) <= jvmMemoryRate) {
								obj[5] = "<span style='color:red'>" + df.format(jvmMemoryRate) + "(阀值:"+map.get("jvmMemory")+")</span>";
								isError = true;
							}else {
								obj[5] = df.format(jvmMemoryRate);
							}
							
							if(Double.parseDouble(map.get("jvmThread")) <= Double.parseDouble(snmpModel.getJvmTheadSize())) {
								obj[6] = "<span style='color:red'>" + snmpModel.getJvmTheadSize() + "(阀值:"+map.get("jvmThread")+")</span>";
								isError = true;
							}else {
								obj[6] = snmpModel.getJvmTheadSize();
							}
							double io = Double.parseDouble(snmpModel.getIfInSize()) / 1024;
//							if(Double.parseDouble(map.get("io")) <= io) {
//								obj[7] = "<span style='color:red'>" + df.format(io) + "(阀值:"+map.get("io")+")</span>";
//								isError = true;
//							}else {
//								obj[7] = df.format(io);
//							}
							obj[7] = df.format(Double.parseDouble(snmpModel.getIfInSize()) / 1024)+"|"+df.format(Double.parseDouble(snmpModel.getIfOutSize())/1024)+"(Kb/s)";
							obj[8] = df.format(Double.parseDouble(snmpModel.getDiskIOReadSize())/1024)+ "|" +df.format(Double.parseDouble(snmpModel.getDiskIOWrittenSize())/1024)+"(Kb/s)";
							double storageUsed = Double.parseDouble(snmpModel.getStorageUsed())/Double.parseDouble(snmpModel.getStorageSize())*100;
							if(Double.parseDouble(map.get("diskUsage")) <= storageUsed) {
								obj[9] = "<span style='color:red'>"+ df.format(storageUsed) + "%" + "(阀值:"+map.get("diskUsage")+")</span>";
							}else {
								obj[9] = df.format(storageUsed) + "%";
							}
							double systemProcess = Double.parseDouble(snmpModel.getSystemProcess());
							if(Double.parseDouble(map.get("systemProcess")) <= systemProcess) {
								obj[10] = "<span style='color:red'>"+snmpModel.getSystemProcess()+ "(阀值:"+map.get("systemProcess")+")</span>";
							}else {
								obj[10] = snmpModel.getSystemProcess();
							}
							
						}else {
							obj[3] = snmpModel.getCpuUsedRate();
							obj[4] = df
									.format(Double.parseDouble(snmpModel.getMemoryUsedSize()) / Double.parseDouble(snmpModel
											.getMemoryTotalSize()) * 100);
							obj[5] = df.format(Double.parseDouble(snmpModel
									.getJvmHeapUsedSize()) /Double.parseDouble(snmpModel
											.getJvmHeadTotalSize()) * 100);
							obj[6] = Integer.parseInt(snmpModel.getJvmTheadSize());
							obj[7] = df.format(Double.parseDouble(snmpModel.getIfInSize()) / 1024 / 1024)+"|"+df.format(Double.parseDouble(snmpModel.getIfOutSize())/1024/1024)+"(Kb/s)";
							obj[8] = df.format(Double.parseDouble(snmpModel.getDiskIOReadSize())/1024/1024)+ "|" +df.format(Double.parseDouble(snmpModel.getDiskIOWrittenSize())/1024/1024)+"(Mb/s)";
							obj[9] = df.format(Double.parseDouble(snmpModel.getStorageUsed())/Double.parseDouble(snmpModel.getStorageSize())*100) + "%";
							obj[10] = snmpModel.getSystemProcess();
						}
						dataList.add(obj);
						if(isError) {
							if(monitorMap.get("snmp_error") == null) {
								monitorMap.put("snmp_error", 1);
							}else {
								monitorMap.put("snmp_error", monitorMap.get("snmp_error")+1);
							}
						}else {
							if(monitorMap.get("snmp_success") == null) {
								monitorMap.put("snmp_success", 1);
							}else {
								monitorMap.put("snmp_success", monitorMap.get("snmp_success")+1);
							}
						}
						
					}
				}
			}
		}else {
			AlertCommand alt = new AlertCommand("alt", "尚未创建监控服务！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5);
			outPutXML(response, alt);
			return null;
		}
		
		
		for(Object[] obj : listTaskResult) {
			if(Constants.HTTP_TYPE.equals(obj[3])) {
				if("可用".equals(obj[4])) {
					if(monitorMap.get("http_success") == null) {
						monitorMap.put("http_success", 1);
					}else {
						monitorMap.put("http_success", monitorMap.get("http_success")+1);
					}
				}else {
					if(monitorMap.get("http_error") == null) {
						monitorMap.put("http_error", 1);
					}else {
						monitorMap.put("http_error", monitorMap.get("http_error")+1);
					}
				}
			}else if(Constants.PING_TYPE.equals(obj[3])) {
				if("可用".equals(obj[4])) {
					if(monitorMap.get("ping_success") == null) {
						monitorMap.put("ping_success", 1);
					}else {
						monitorMap.put("ping_success", monitorMap.get("ping_success")+1);
					}
				}else {
					if(monitorMap.get("ping_error") == null) {
						monitorMap.put("ping_error", 1);
					}else {
						monitorMap.put("ping_error", monitorMap.get("ping_error")+1);
					}
				}
			}
		}
		
		Map<String, Subproject> subprojectMap = subprojectService.findSubprojectsByProjectId(projectId);
		List<FaultHistory> listFaultHistory = new ArrayList<FaultHistory>();
		List<Object[]> listHistory = new ArrayList<Object[]>();
		if(subprojectMap != null) {
			listFaultHistory = ServiceLocator.getFaultHistoryService().getFaultHistoryInfoByProjectId(projectId);
			Object[] objects = null;
			for(FaultHistory fh : listFaultHistory) {
				objects = new Object[7];
				objects[0] = fh.getFhId();
				objects[1] = subprojectMap.get(fh.getTaskId()).getName();
				objects[2] = fh.getFhBeginTime();
				objects[3] = fh.getFhEndTime();
				objects[4] = ClientMonitorView.getDuration(fh.getFhBeginTime(),
						fh.getFhEndTime());
				objects[5] = fh.getFhReason();
				listHistory.add(objects);
			}
		}
		
		
		Gson gson = new Gson();
		Map<String, Object> overviewCharts = getOverviewChart(monitorMap);
		Map<String, Object> overviewResponseTimeCharts = getOverviewResponseTimeChart(listTaskResult);
		setTime(request, condition);
		Panel panel = ClientMonitorView.buildOverView(loc, viewFactory,
				condition, gson.toJson(overviewCharts), gson.toJson(overviewResponseTimeCharts),  dataList,listTaskResult, listHistory, isTimer, projectId);
		if (update != null && "1".equals(update)) {
//			System.out.println("==================================update=====================================");
			CommandGroup cg = new CommandGroup("");
			UpdateCommand uc = new UpdateCommand("uc");
			uc.setContent(panel);
			cg.add(uc);
			cg.setPath("/monitor/overView");
			outPutXML(response, cg);
		} else {
			BaseView view = new BaseView();
			view.setRootPanel(panel);
			view.add(new JSCommand(
					"overViewQuery",
					"VM( this ).ext( 'startTime', VM(this).fv('startTime'));"
							+ "VM( this ).ext( 'endTime', VM(this).fv('endTime'));"
							+ "VM(this).ext('sel',VM(this).fv('sel'));"
							+ "VM(this).cmd('query',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ))"));
			view.add(new JSCommand(
					"query",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=showOverView&update=1&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&taskId="
							+ condition.getTaskId() + "'});"));
			view.add(new JSCommand(
					"setRefreshOverview",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=setRefreshOverview&isOpen='+$0+'&id='+$1});"));
			view.addLoadEvent("registerOverviewTimer();");
			outPutXML(response, view);
		}
		return null;
	}
	

	
	public ModelAndView setRefreshOverview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String projectId = request.getParameter("id");
		String isOpen = request.getParameter("isOpen");
		Panel panel = null;
		CommandGroup cg = new CommandGroup("");
		UpdateCommand uc = new UpdateCommand("uc");
		if("close".equals(isOpen)) {
			//Panel chartPanel = 
			JSCommand js = new JSCommand("","removeOverviewTimer()");
			panel = new HtmlPanel("overview_timer", "<a href=\"javascript:void(0);\" onclick=\"VM(this).cmd('setRefreshOverview','open','"+projectId+"')\">打开定时刷新</a>");
			cg.add(js);
		}else {
			panel = new HtmlPanel("overview_timer", "<a href=\"javascript:void(0);\" onclick=\"VM(this).cmd('setRefreshOverview','close','"+projectId+"')\">关闭定时刷新</a>");
			JSCommand js = new JSCommand("","registerOverviewTimer()");
			cg.add(js);
		}
		uc.setContent(panel);
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}

	
	public ModelAndView showAvailableView(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);
		QueryCondition condition = new QueryCondition(request);
		String update = request.getParameter("update");
		String projectId = request.getParameter("projectId");
		String subprojectId = request.getParameter("id");
		String type = request.getParameter("type");
		setTime(request, condition);
		String isIgnoreMpId = request.getParameter("isIgnoreMpId");
		if (Utils.isEmpty(isIgnoreMpId)) {
			String mpId = request.getParameter("mpId");
			condition.setMpId(mpId);
		} else {
			condition.setMpId(null);
		}
		if(Utils.isEmpty(update) && Utils.isEmpty(subprojectId)) {
			SubQueryCondition sqc = new SubQueryCondition();
			sqc.setType(type);
			sqc.setProjectId(projectId);
			sqc.setIsusable(Constants.OPTION_IS_YES);
			List<Subproject> listSubproject = subprojectService.findSubprojectByCondition(sqc);
			if(Utils.isEmpty(listSubproject)) {
				AlertCommand alt = new AlertCommand("alt", "尚未创建监控服务！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			Subproject subproject = listSubproject.get(0);
			condition.setTaskId(subproject.getId());
			String[] mpIds = subproject.getMonitorPoint().split(",");
			Panel panel = ClientMonitorView.buildAvailablePanel(loc, viewFactory,
					condition, mpIds,listSubproject);
			BaseView view = new BaseView();
			view.setRootPanel(panel);
			view.add(new JSCommand(
					"availableQuery",
					"VM( this ).ext( 'startTime', VM(this).fv('startTime'));"
							+ "VM( this ).ext( 'endTime', VM(this).fv('endTime'));"
							+ "VM(this).ext('sel',VM(this).fv('sel'));"
							+ "VM(this).ext('subprojectId',VM(this).fv('subprojectId'));"
							+ "VM(this).cmd('availablequery',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ),VM(this).ext('subprojectId'))"));
			view.add(new JSCommand(
					"availablequery",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=showAvailableView&handler=query&update=1&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&id='+$3});"));
			view.add(new JSCommand(
					"changePoint",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=showAvailableView&id='+$0+'&update=1'})"));
			outPutXML(response, view);
			return null;
		}else if("1".equals(update) && Utils.notEmpty(subprojectId)){
			String handler = request.getParameter("handler");
			Subproject subproject = subprojectService.getSubprojectById(subprojectId);
			condition.setTaskId(subproject.getId());
			String[] mpIds = subproject.getMonitorPoint().split(",");
			Panel panel = ClientMonitorView.buildHttpPanel(condition, mpIds);
			
			CommandGroup ug = new CommandGroup("");
			UpdateCommand uc = new UpdateCommand(null);
			uc.setContent(panel);
			if(Utils.isEmpty(handler)) {
				UpdateCommand uc2 = new UpdateCommand("uc2");
				Panel timePanl = ClientMonitorView.buildHttpTimePanel(condition, subprojectId);
				uc2.setContent(timePanl);
				ug.add(uc2);
			}
			ug.add(uc);
			outPutXML(response, ug);
			return null;
		}
		return null;
	}
	

	public ModelAndView showAvailable(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		QueryCondition condition = new QueryCondition(request);
		String subprojectId = request.getParameter("id");
		condition.setTaskId(subprojectId);
		setTime(request, condition);
		String isIgnoreMpId = request.getParameter("isIgnoreMpId");
		if (Utils.isEmpty(isIgnoreMpId)) {
			String mpId = request.getParameter("mpId");
			condition.setMpId(mpId);
		} else {
			condition.setMpId(null);
		}
		Subproject subproject = subprojectService.getSubprojectById(subprojectId);
		String[] mpIds = subproject.getMonitorPoint().split(",");
		List<Object[]> list = null;
		List<Object[]> list2 = null;
		String period = "";
		long startTime = condition.getStartTime().getTime();
		long endTime = condition.getEndTime().getTime();
		if ((endTime - startTime) >= (1000 * 60 * 60 * 24)) {
			list = detialService.getAvailableRateByCondition(condition);
			QueryCondition condition2 = condition;
			condition2.setStartTime(sdf3.parse(sdf4.format(condition.getStartTime())+" 00:00:01"));
			condition2.setEndTime(sdf3.parse(sdf4.format(condition.getEndTime())+" 23:59:59"));
			list2 = detialService
					.getMPAvailableRateByCondition(condition2);
			period = DAY;
		} else if ((endTime - startTime) <= (1000 * 60 * 60)
				&& (endTime - startTime) > 0) {
			list = detialService.getMinuteAvailableByCondition(condition);
			list2 = detialService
					.getMPAvailableRateByCondition(condition);
			period = MINUTE;
		} else {
			list = detialService.getHourAvailableByCondition(condition);
			QueryCondition condition2 = condition;
			condition2.setStartTime(sdf3.parse(sdf4.format(condition.getStartTime())+" 00:00:01"));
			condition2.setEndTime(sdf3.parse(sdf4.format(condition.getEndTime())+" 23:59:59"));
			list2 = detialService
					.getMPAvailableRateByCondition(condition2);
			period = HOUR;
		}
		String dataJson = getAvaliable(list, condition, period);
		if (!period.equals(MINUTE)) {
			condition.setStartTime(sdf.parse(sdf4.format(condition
					.getStartTime()) + " 00:00:00"));
			condition.setEndTime(sdf.parse(sdf4.format(condition
					.getEndTime()) + " 23:59:59"));
		}
		
		Map<String, String> isuableMap = getIsusable(list2, condition);
		List<FaultHistory> listFaultHistory = ServiceLocator
				.getFaultHistoryService().getFaultHistoryByCondition(condition,
						null);
		Gson gson = new Gson();
		Map<String, Object> responseTimeMap = getResponseTime(condition);
		Map<String, Object> responsetDetailMap = null;
		if(Constants.HTTP_TYPE.equals(subproject.getType().toLowerCase())) {
			responsetDetailMap = getResponseDetailTime(condition);
		}
		Object[] listResponseTime = detialService
				.getOnlyResponseTimeByCondition(condition);
		Panel panel = ClientMonitorView.buildAvailableSubPanel(loc,
				viewFactory, condition, dataJson, mpIds, isuableMap,
				listFaultHistory,listResponseTime,
				gson.toJson(responseTimeMap), gson.toJson(responsetDetailMap));
		BaseView view = new BaseView();
		JSParser converObjectParser = new JSParser(
				"converObject",
				"var objId = xml.getAttribute('op');var isIgnore = xml.getAttribute('isIgnore');"
						+ "if(isIgnore!=null&&isIgnore==1){"
						+ "return '"
						+ "<span><a href=\"javascript:void(0)\" onclick=VM(this).cmd(\"ignore\",\"'+objId+'\",\""
						+ (Utils.notEmpty(condition.getMpId()) ? condition
								.getMpId() : "")
						+ "\");>恢复</a></span>';"
						+ "}else{"
						+ "return '"
						+ "<span><a href=\"javascript:void(0)\" onclick=VM(this).cmd(\"ignore\",\"'+objId+'\",\""
						+ (Utils.notEmpty(condition.getMpId()) ? condition
								.getMpId() : "") + "\");>忽略</a></span>';" + "}");

		view.addParser(converObjectParser);
		view.add(new AjaxCommand("ignore",
				"clientMonitor.sp?act=ignoreFault&fhId=$0&mpId=$1"));
		view.setRootPanel(panel);
		outPutXML(response, view);
		return null;
	}

	public ModelAndView showSnmpView(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
//		Locale loc = getLocale(request);
//		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
//				.getSkin(request));
		QueryCondition condition = new QueryCondition(request);
		String subprojectId = request.getParameter("id");
		String update = request.getParameter("update");
		String projectId = request.getParameter("projectId");
//		condition.setTaskId(subprojectId);
		setTime(request, condition);
		
		List<Subproject> listSubproject = null;
		if(Utils.isEmpty(update) && Utils.isEmpty(subprojectId)) {
			SubQueryCondition sqc = new SubQueryCondition();
			sqc.setType(Constants.SNMP_TYPE);
			sqc.setProjectId(projectId);
			sqc.setIsusable(Constants.OPTION_IS_YES);
			listSubproject = subprojectService.findSubprojectByCondition(sqc);
			if(Utils.isEmpty(listSubproject)) {
				AlertCommand alt = new AlertCommand("alt", "尚未创建监控服务！",
						"img/p/alert-warn.gif", DialogPosition.middle, 5);
				outPutXML(response, alt);
				return null;
			}
			Subproject subproject = listSubproject.get(0);
			condition.setTaskId(subproject.getId());
		}else {
			condition.setTaskId(subprojectId);
		}
		
		
		List<Object[]> listSnmpStatistics = null;
		String isHour = "";
		long startTime = condition.getStartTime().getTime();
		long endTime = condition.getEndTime().getTime();
		if ((endTime - startTime) >= (1000 * 60 * 60 * 24)) {
			listSnmpStatistics = snmpService.getDayStatistics(
					condition.getTaskId(), condition.getStartTime(),
					condition.getEndTime());
			isHour = DAY;
		} else if ((endTime - startTime) <= (1000 * 60 * 60)
				&& (endTime - startTime) > 0) {
			listSnmpStatistics = snmpService.getMimuteStatistics(
					condition.getTaskId(), condition.getStartTime(),
					condition.getEndTime());
			isHour = MINUTE;
		} else {
			listSnmpStatistics = snmpService.getHourStatistics(
					condition.getTaskId(), condition.getStartTime(),
					condition.getEndTime());
			isHour = HOUR;
		}
		
		Map<String, String> configMap = new HashMap<String, String>();
		double jvmThreadThreshold = 0,ioThreshold = 0;
		
		Subproject subproject = subprojectService.getSubprojectById(condition.getTaskId());
		if(subproject != null && Utils.notEmpty(subproject.getConfigXml())) {
			Map<String, String> configmap = Utils.string2Map(subproject.getConfigXml());
			String thresholdIds = configmap.get("continueGroup");
			if(Utils.notEmpty(thresholdIds)) {
				String[] ids = thresholdIds.split(",");
				Threshold threshold = thresholdService.getThresholdById(ids[0]);
				if(threshold != null) {
					configMap = Utils.string2Map(threshold.getContent());
				}
			}
		}
		
		Map<String, Object> jvmThreadMap = getSnmpJvmThread2(listSnmpStatistics,
				condition, jvmThreadThreshold, isHour);
		Map<String, Object> ioMap = getSnmpIo2(listSnmpStatistics, condition, ioThreshold,
				isHour);
		Map<String, Object> useableRateMap = getSnmpRate2(listSnmpStatistics, condition, configMap,  isHour);
		if (update != null && "1".equals(update)) {
			String handler = request.getParameter("handler");
			Panel panel = ClientMonitorView.buildSnmpStatisticsPanel(gson.toJson(useableRateMap),
					gson.toJson(jvmThreadMap), gson.toJson(ioMap));
			CommandGroup cg = new CommandGroup("");
			UpdateCommand uc = new UpdateCommand("uc");
			uc.setContent(panel);
			if(Utils.isEmpty(handler)) {
				Panel timePanle = ClientMonitorView.buildSelectTimePanel(condition, false, subprojectId);
				UpdateCommand uc2 = new UpdateCommand("uc2");
				uc2.setContent(timePanle);
				cg.add(uc2);
			}
		
			cg.add(uc);
			outPutXML(response, cg);
		} else {
			Panel panel = ClientMonitorView.buildSnmpStatisticsPanel(condition, gson.toJson(useableRateMap),
					gson.toJson(jvmThreadMap), gson.toJson(ioMap), listSubproject);
			BaseView view = new BaseView();
			view.setRootPanel(panel);
			view.add(new JSCommand(
					"snmpQuery",
					"VM( this ).ext( 'startTime', VM(this).fv('startTime'));"
							+ "VM( this ).ext( 'endTime', VM(this).fv('endTime'));"
							+ "VM(this).ext('sel',VM(this).fv('sel'));"
							+ "VM(this).ext('subprojectId',VM(this).fv('subprojectId'));"
							+ "VM(this).cmd('snmpsequery',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ),VM(this).ext('subprojectId'))"));
			view.add(new JSCommand(
					"snmpsequery",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=showSnmpView&handler=query&update=1&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&id='+$3});"));
			
			view.add(new JSCommand(
					"refreshPanel",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=setRefreshCharts&isOpen='+$0+'&id='+$1});"));
			view.add(new JSCommand(
					"changePoint",
					"VM(this).cmd({tagName:'ajax',src:'clientMonitor.sp?act=showSnmpView&id='+$0+'&update=1'})"));
		//	view.addLoadEvent("cycleRefreshCharts('"+taskId+"')");
			outPutXML(response, view);
		}
		return null;
	}
	/**
	 * 忽略故障
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView ignoreFault(HttpServletRequest request,
			HttpServletResponse response) {
		QueryCondition condition = new QueryCondition(request);
		List<FaultHistory> faultHistorys = ServiceLocator
				.getFaultHistoryService().getFaultHistoryByCondition(condition,
						null);
		FaultHistory faultHistory = null;
		if (Utils.notEmpty(faultHistorys)) {
			faultHistory = faultHistorys.get(0);
		}
		if (faultHistory != null) {

			if (Utils.notEmpty(faultHistory.getIsIgnore())
					&& Constants.OPTION_IS_YES.equals(faultHistory
							.getIsIgnore())) {
				faultHistory.setIsIgnore(null);

			} else {
				faultHistory.setIsIgnore(Constants.OPTION_IS_YES);
			}

			ServiceLocator.getFaultHistoryService().updateFaultHistory(
					faultHistory);
		}
		SimpleDateFormat sdform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CommandGroup cg = new CommandGroup("cg");
		JSCommand command = new JSCommand("js",
				"VM( this ).find( 'f_f_grid' ).updateRow( { "
						+ "beginTime : '"
						+ sdform.format(faultHistory.getFhBeginTime())
						+ "', "
						+ "endTime : '"
						+ sdform.format(faultHistory.getFhEndTime())
						+ "', "
						+ "duration : '"
						+ MonitorDetailView.getDuration(
								faultHistory.getFhBeginTime(),
								faultHistory.getFhEndTime()) + "',"
						+ "reason : '" + faultHistory.getFhReason() + "',"
						+ "op : '" + faultHistory.getFhId() + "',"
						+ "isIgnore:'" + faultHistory.getIsIgnore() + "'"
						+ "},{op:'" + faultHistory.getFhId() + "'});");
		cg.add(command);
		outPutXML(response, cg);
		return null;
	}

	public String getAvaliable(List<Object[]> list, QueryCondition condition,
			String period) {
		String info = "";
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> labelMap = null;
		Map<String, Object> maps = new HashMap<String, Object>();
		if (Utils.notEmpty(list)) {
			List<Long> dateList = null;
			boolean isHas = false;
			if (DAY.equals(period)) {
				info = "(天)";
				dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf2
								.format(condition.getStartTime()) : null,
						condition.getEndTime() != null ? sdf2.format(condition
								.getEndTime()) : null, true);
				// 塞值
				if (Utils.notEmpty(dateList)) {
					Map<String, String> map = null;
					for (Long temp : dateList) {
						map = new HashMap<String, String>();
						isHas = false;
						map.put("label", (temp % 10000 / 100) + "-"
								+ (temp % 10000 % 100));
						for (Object[] objects : list) {
							if (temp.longValue() == (objects[0] != null ? (Long
									.parseLong((String) objects[0])) : 0L)) {
								String value = (String) objects[1];
								if(Utils.isNumeric(value) && Integer.parseInt(value) >=0 ) {
									map.put("value", value);
								}else {
									map.put("value", "0");
								}
								
								isHas = true;
								break;
							}
						}
						if (!isHas) {
							map.put("value", "100");
						}
						dataList.add(map);
					}
				}
			} else if (MINUTE.equals(period)) {
				info = "(检测频率)";
				Map<String, long[]> availableMap = new HashMap<String, long[]>();
				String isuseable, hour;
				long count;
				List<String> listAvaliable = new ArrayList<String>();
				long[] valCount;
				for (Object[] obj : list) {
					isuseable = (String) obj[0];
					count = (Long) obj[1];
					hour = (String) obj[2];
					long[] val = availableMap.get(hour);
					if (val == null) {
						val = new long[] { 0L, 0L };
						listAvaliable.add(hour);
					}
					if ("0".equals(isuseable)) {
						val[0] += count;
					} else {
						val[1] += count;
					}
					availableMap.put(hour, val);
				}
				for (String str : listAvaliable) {
					labelMap = new HashMap<String, String>();
					labelMap.put("label", str);
					valCount = availableMap.get(str);
					if (valCount != null) {
						labelMap.put("value", (valCount[0]) * 10000
								/ (valCount[0] + valCount[1]) / 100 + "");
					} else {
						labelMap.put("value", "0");
					}
					dataList.add(labelMap);
				}
			} else if (HOUR.equals(period)) {
				info = "(小时)";
				Map<String, long[]> availableMap = new HashMap<String, long[]>();
				String isuseable, hour;
				long count;
				for (Object[] obj : list) {
					isuseable = (String) obj[0];
					count = (Long) obj[1];
					hour = (String) obj[2];
					long[] val = availableMap.get(hour);
					if (val == null) {
						val = new long[] { 0L, 0L };
					}
					if ("0".equals(isuseable)) {
						val[0] += count;
					} else {
						val[1] += count;
					}
					availableMap.put(hour, val);
				}
				// 存放dataset值
				long[] valCount;
				for (int i = 0; i < 24; i++) {
					labelMap = new HashMap<String, String>();
					if (i < 10) {
						labelMap.put("label", "0" + i);
						valCount = availableMap.get("0" + i);
						if (valCount != null) {
							labelMap.put("value", (valCount[0]) * 10000
									/ (valCount[0] + valCount[1]) / 100 + "");
						} else {
							labelMap.put("value", "0");
						}
					} else {
						labelMap.put("label", i + "");
						valCount = availableMap.get(i + "");
						if (valCount != null) {
							labelMap.put("value", (valCount[0]) * 10000
									/ (valCount[0] + valCount[1]) / 100 + "");
						} else {
							labelMap.put("value", "0");
						}
					}
					dataList.add(labelMap);
				}
			}
		}
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("caption", "可用率变化曲线图");
		headMap.put("xAxisName", "日期" + info);
		headMap.put("yAxisName", "可用率(%)");
		headMap.put("labelStep", "5");
		headMap.put("palette", "4");
		headMap.put("yAxisMaxValue", "100");
		headMap.put("slantLabels", "1");
		headMap.put("formatNumberScale", "0");
		headMap.put("formatNumber", "0");

		Map<String, Object> trendlinesMap = new HashMap<String, Object>();
		Map<String, String> lineMap = new HashMap<String, String>();
		lineMap.put("color", "91C728");
		lineMap.put("showontop", "1");
		trendlinesMap.put("line", lineMap);

		Map<String, String> applyMap = new HashMap<String, String>();
		applyMap.put("toobject", "Canvas");
		applyMap.put("styles", "CanvasAnim");
		Map<String, Object> applicationMap = new HashMap<String, Object>();
		applicationMap.put("apply", applyMap);
		Map<String, Object> stylesMap = new HashMap<String, Object>();
		stylesMap.put("application", applicationMap);
		maps.put("chart", headMap);
		maps.put("data", dataList);
		maps.put("trendlines", trendlinesMap);
		maps.put("styles", stylesMap);
		return new Gson().toJson(maps);
	}

	/**
	 * 获取一天内的可用率
	 * 
	 * @param condition
	 * @return
	 */
	public String getHourAvaliable(QueryCondition condition) {
		List<Object[]> list = ServiceLocator.getTaskResultService()
				.getHourAvailableByCondition(condition);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, String> labelMap = null;
		Map<String, Object> maps = new HashMap<String, Object>();
		Map<String, long[]> availableMap = new HashMap<String, long[]>();
		String isuseable, hour;
		long count;
		for (Object[] obj : list) {
			isuseable = (String) obj[0];
			count = (Long) obj[1];
			hour = (String) obj[2];
			long[] val = availableMap.get(hour);
			if (val == null) {
				val = new long[] { 0L, 0L };
			}
			if ("0".equals(isuseable)) {
				val[0] += count;
			} else {
				val[1] += count;
			}
			availableMap.put(hour, val);
		}
		// 存放dataset值
		long[] valCount;
		for (int i = 0; i < 24; i++) {
			labelMap = new HashMap<String, String>();
			if (i < 10) {
				labelMap.put("label", "0" + i);
				valCount = availableMap.get("0" + i);
				if (valCount != null) {
					labelMap.put("value", (valCount[0]) * 10000
							/ (valCount[0] + valCount[1]) / 100 + "");
				} else {
					labelMap.put("value", "0");
				}
			} else {
				labelMap.put("label", i + "");
				valCount = availableMap.get(i + "");
				if (valCount != null) {
					labelMap.put("value", (valCount[0]) * 10000
							/ (valCount[0] + valCount[1]) / 100 + "");
				} else {
					labelMap.put("value", "0");
				}
			}
			// dataMap.add(tempMap);
			dataList.add(labelMap);
		}
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("caption", "可用率变化曲线图");
		headMap.put("xAxisName", "日期");
		headMap.put("yAxisName", "可用率(%)");
		headMap.put("labelStep", "5");
		headMap.put("palette", "4");
		headMap.put("yAxisMaxValue", "100");
		headMap.put("slantLabels", "1");
		headMap.put("formatNumberScale", "0");
		headMap.put("formatNumber", "0");

		Map<String, Object> trendlinesMap = new HashMap<String, Object>();
		Map<String, String> lineMap = new HashMap<String, String>();
		lineMap.put("color", "91C728");
		lineMap.put("showontop", "1");
		trendlinesMap.put("line", lineMap);

		Map<String, String> applyMap = new HashMap<String, String>();
		applyMap.put("toobject", "Canvas");
		applyMap.put("styles", "CanvasAnim");
		Map<String, Object> applicationMap = new HashMap<String, Object>();
		applicationMap.put("apply", applyMap);
		Map<String, Object> stylesMap = new HashMap<String, Object>();
		stylesMap.put("application", applicationMap);
		maps.put("chart", headMap);
		maps.put("data", dataList);
		maps.put("trendlines", trendlinesMap);
		maps.put("styles", stylesMap);
		return new Gson().toJson(maps);
	}

	/**
	 * 获取一定时间范围内的使用率
	 * 
	 * @param condition
	 * @return
	 */
	public String getDayAvaliable(QueryCondition condition) {

		Map<String, Object> maps = new HashMap<String, Object>();
		List<Object[]> list = detialService
				.getAvailableRateByCondition(condition);

		List<Long> dateList = null;
		boolean isHas = false;
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		if (Utils.notEmpty(list)) {
			dateList = DateUtils.getDateBetween2Date(
					condition.getStartTime() != null ? sdf2.format(condition
							.getStartTime()) : null,
					condition.getEndTime() != null ? sdf2.format(condition
							.getEndTime()) : null, true);
			// 塞值
			if (Utils.notEmpty(dateList)) {
				Map<String, String> map = null;
				for (Long temp : dateList) {
					map = new HashMap<String, String>();
					isHas = false;
					map.put("label", (temp % 10000 / 100) + "-"
							+ (temp % 10000 % 100));
					for (Object[] objects : list) {
						if (temp.longValue() == (objects[0] != null ? (Long
								.parseLong((String) objects[0])) : 0L)) {
							map.put("value", (String) objects[1]);
							isHas = true;
							break;
						}
					}
					if (!isHas) {
						map.put("value", "100");
					}
					dataList.add(map);
				}
			}
		}
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("caption", "可用率变化曲线图");
		headMap.put("xAxisName", "日期(天)");
		headMap.put("yAxisName", "可用率(%)");
		headMap.put("labelStep", "5");
		headMap.put("palette", "4");
		headMap.put("yAxisMaxValue", "100");
		headMap.put("slantLabels", "1");
		headMap.put("formatNumberScale", "0");
		headMap.put("formatNumber", "0");

		Map<String, Object> trendlinesMap = new HashMap<String, Object>();
		Map<String, String> lineMap = new HashMap<String, String>();
		lineMap.put("color", "91C728");
		lineMap.put("showontop", "1");
		trendlinesMap.put("line", lineMap);

		Map<String, String> applyMap = new HashMap<String, String>();
		applyMap.put("toobject", "Canvas");
		applyMap.put("styles", "CanvasAnim");
		Map<String, Object> applicationMap = new HashMap<String, Object>();
		applicationMap.put("apply", applyMap);
		Map<String, Object> stylesMap = new HashMap<String, Object>();
		stylesMap.put("application", applicationMap);
		maps.put("chart", headMap);
		maps.put("data", dataList);
		maps.put("trendlines", trendlinesMap);
		maps.put("styles", stylesMap);

		return new Gson().toJson(maps);
	}

	private Map<String, String> getIsusable(List<Object[]> list2,
			QueryCondition condition) {
		Gson gson = new Gson();
		Map<String, String[]> pointMap = new HashMap<String, String[]>();
		Map<String, String> pointResult = null;
		Map<String, String> pointHeadMap = null;
		String[] objtemp = null;
		if (Utils.notEmpty(list2)) {

			for (Object[] objects : list2) {
				if (pointMap.containsKey((String) objects[0])) {
					objtemp = pointMap.get((String) objects[0]);
					if ("0".equals((String) objects[2])) {
						objtemp[2] = objects[3] + "";// 可用总数
					} else {
						objtemp[3] = objects[3] + "";// 不可用总数
					}
					pointMap.put((String) objects[0], objtemp);
				} else {
					objtemp = new String[4];
					objtemp[0] = (String) objects[0];// mpId
					objtemp[1] = (String) objects[1];// mpName
					if ("0".equals((String) objects[2])) {
						objtemp[2] = objects[3] + "";// 可用总数
					} else {
						objtemp[3] = objects[3] + "";// 不可用总数
					}
					pointMap.put((String) objects[0], objtemp);
				}

			}
		}
		if (pointMap != null && pointMap.size() > 0) {
			pointResult = new HashMap<String, String>();
			List<Map<String, String>> list3 = null;
			String[] strtemp = null;
			Map<String, String> map = null;
			Map<String, String> map2 = null;
			Map<String, Object> map3 = null;
			int count = 0, unuse = 0, use = 0, unuseAll = 0, useAll = 0;
			if (Utils.isEmpty(condition.getMpId())) {
				for (Map.Entry<String, String[]> entry : pointMap.entrySet()) {
					strtemp = entry.getValue();
					use = Utils.notEmpty(strtemp[2]) ? Integer
							.parseInt(strtemp[2]) : 0;
					unuse = Utils.notEmpty(strtemp[3]) ? Integer
							.parseInt(strtemp[3]) : 0;
					unuseAll = unuseAll + unuse;
					useAll = useAll + use;
					count = count + use + unuse;

				}
				map = new HashMap<String, String>();
				map2 = new HashMap<String, String>();
				list3 = new ArrayList<Map<String, String>>();
				pointHeadMap = new HashMap<String, String>();
				map.put("label", "故障");
				map.put("value", unuseAll * 100.00 / count + "");
				map2.put("label", "正常");
				map2.put("value", useAll * 100.00 / count + "");
				list3.add(map);
				list3.add(map2);
				pointHeadMap.put("caption", "平均");
				pointHeadMap.put("palette", "4");
				// pointHeadMap.put("manageLabelOverflow", "true");
				pointHeadMap.put("startingAngle", "60");
				pointHeadMap.put("smartLabelClearance", "1");

				map3 = new HashMap<String, Object>();
				map3.put("chart", pointHeadMap);
				map3.put("data", list3);
				pointResult.put("pp", gson.toJson(map3));

			} else {
				for (Map.Entry<String, String[]> entry : pointMap.entrySet()) {
					strtemp = entry.getValue();
					use = Utils.notEmpty(strtemp[2]) ? Integer
							.parseInt(strtemp[2]) : 0;
					unuse = Utils.notEmpty(strtemp[3]) ? Integer
							.parseInt(strtemp[3]) : 0;
					count = use + unuse;
					map = new HashMap<String, String>();
					map2 = new HashMap<String, String>();
					list3 = new ArrayList<Map<String, String>>();
					pointHeadMap = new HashMap<String, String>();
					map.put("label", "故障");
					map.put("value", unuse * 100.00 / count + "");
					map2.put("label", "正常");
					map2.put("value", use * 100.00 / count + "");
					list3.add(map);
					list3.add(map2);
					pointHeadMap.put("caption", strtemp[1]);
					pointHeadMap.put("palette", "4");
					// pointHeadMap.put("manageLabelOverflow", "true");
					pointHeadMap.put("startingAngle", "60");
					pointHeadMap.put("smartLabelClearance", "1");

					map3 = new HashMap<String, Object>();
					map3.put("chart", pointHeadMap);
					map3.put("data", list3);
					pointResult.put(strtemp[0], gson.toJson(map3));
				}
			}

		}
		return pointResult;
	}

	/**
	 * 获取一定范围的响应时间
	 * 
	 * @param condition
	 * @return
	 */
	private Map<String, Object> getResponseTime(QueryCondition condition) {
		List<Object[]> list = null;
		String period = "";
		long startTime = condition.getStartTime().getTime();
		long endTime = condition.getEndTime().getTime();
		if ((endTime - startTime) >= (1000 * 60 * 60 * 24)) {
			list = detialService.getResponseTimeByCondition(condition, false);
			period = DAY;
		} else if ((endTime - startTime) <= (1000 * 60 * 60)
				&& (endTime - startTime) > 0) {
			list = detialService.getMinuteResponseTimeByCondition(condition);
			period = MINUTE;
		} else {
			list = detialService.getHourResponseTimeByCondition(condition);
			period = HOUR;
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		if (Utils.notEmpty(list)) {
			List<Long> dateList = null;
			boolean isHas = false;
			Map<String, Object> minMap = new HashMap<String, Object>();
			List<Map<String, String>> minList = new ArrayList<Map<String, String>>();
			minMap.put("seriesname", "最小响应时间");
			Map<String, Object> maxMap = new HashMap<String, Object>();
			List<Map<String, String>> maxList = new ArrayList<Map<String, String>>();
			maxMap.put("seriesname", "最大响应时间");
			Map<String, Object> avgMap = new HashMap<String, Object>();
			List<Map<String, String>> avgList = new ArrayList<Map<String, String>>();
			avgMap.put("seriesname", "平均响应时间");
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			if (HOUR.equals(period)) {
				Map<String, String> labelMap = null;
				for (int i = 0; i < 24; i++) {
					labelMap = new HashMap<String, String>();
					if (i < 10) {
						labelMap.put("label", "0" + i);
					} else {
						labelMap.put("label", i + "");
					}
					dataList.add(labelMap);
				}
				Map<String, String> tempMap = null, tempMap2 = null, tempMap3 = null;
				for (Map<String, String> hourMap : dataList) {
					String hour = hourMap.get("label");
					isHas = false;
					for (Object[] objects : list) {
						if (hour.equals(String.valueOf(objects[0]))) {
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap.put("value", objects[1] + "");
							minList.add(tempMap);
							tempMap2.put("value", objects[2] + "");
							maxList.add(tempMap2);
							tempMap3.put("value", objects[3] + "");
							avgList.add(tempMap3);
							isHas = true;
							break;
						}
					}
					if (!isHas) {
						tempMap = new HashMap<String, String>();
						tempMap2 = new HashMap<String, String>();
						tempMap3 = new HashMap<String, String>();
						tempMap.put("value", "0");
						minList.add(tempMap);
						tempMap2.put("value", "0");
						maxList.add(tempMap2);
						tempMap3.put("value", "0");
						avgList.add(tempMap3);

					}
				}
			} else if (MINUTE.equals(period)) {
				Map<String, String> tempMap = null, tempMap2 = null, tempMap3 = null;
				Map<String, String> labelMap = null;
				for (Object[] objects : list) {
					tempMap = new HashMap<String, String>();
					tempMap2 = new HashMap<String, String>();
					tempMap3 = new HashMap<String, String>();
					tempMap.put("value", objects[1] + "");
					minList.add(tempMap);
					tempMap2.put("value", objects[2] + "");
					maxList.add(tempMap2);
					tempMap3.put("value", objects[3] + "");
					avgList.add(tempMap3);
					labelMap = new HashMap<String, String>();
					labelMap.put("label", String.valueOf(objects[0]));
					dataList.add(labelMap);
				}
			} else if (DAY.equals(period)) {
				dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf2
								.format(condition.getStartTime()) : null,
						condition.getEndTime() != null ? sdf2.format(condition
								.getEndTime()) : null, false);
				// 塞值
				if (Utils.notEmpty(dateList)) {

					Map<String, String> map = null;
					for (Long temp : dateList) {
						map = new HashMap<String, String>();
						isHas = false;
						map.put("label", (temp % 10000 / 100) + "-"
								+ (temp % 10000 % 100));
						Map<String, String> tempMap = null, tempMap2 = null, tempMap3 = null;
						for (Object[] objects : list) {
							if (temp.longValue() == (objects[0] != null ? ((Long) objects[0])
									.longValue() : 0L)) {
								tempMap = new HashMap<String, String>();
								tempMap2 = new HashMap<String, String>();
								tempMap3 = new HashMap<String, String>();
								tempMap.put("value", objects[1] + "");
								minList.add(tempMap);
								tempMap2.put("value", objects[2] + "");
								maxList.add(tempMap2);
								tempMap3.put("value", objects[3] + "");
								avgList.add(tempMap3);
								isHas = true;
								break;
							}
						}
						if (!isHas) {
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap.put("value", "0");
							minList.add(tempMap);
							tempMap2.put("value", "0");
							maxList.add(tempMap2);
							tempMap3.put("value", "0");
							avgList.add(tempMap3);

						}
						dataList.add(map);
					}
				}
			}
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("caption", "响应时间变化曲线图");
			headMap.put("xAxisName", "日期");
			headMap.put("yAxisName", "时间(ms)");
			headMap.put("labelStep", "5");
			headMap.put("formatNumberScale", "0");
			headMap.put("formatNumber", "0");
			Map<String, Object> trendlinesMap = new HashMap<String, Object>();
			Map<String, String> lineMap = new HashMap<String, String>();
			lineMap.put("color", "91C728");
			lineMap.put("showontop", "1");
			trendlinesMap.put("line", lineMap);

			Map<String, Object> stylesMap = new HashMap<String, Object>();
			Map<String, Object> definitionMap = new HashMap<String, Object>();
			Map<String, Object> applicationMap = new HashMap<String, Object>();
			Map<String, String> styleMap = new HashMap<String, String>();
			Map<String, String> applyMap = new HashMap<String, String>();
			styleMap.put("name", "CanvasAnim");
			styleMap.put("type", "animation");
			styleMap.put("param", "_xScale");
			styleMap.put("start", "0");
			styleMap.put("duration", "1");
			definitionMap.put("definition", styleMap);
			applyMap.put("toobject", "Canvas");
			applyMap.put("styles", "CanvasAnim");
			applicationMap.put("apply", applyMap);
			stylesMap.put("application", applicationMap);

			// 存放dataset值
			List<Map<String, Object>> datasetList = new ArrayList<Map<String, Object>>();
			minMap.put("data", minList);
			maxMap.put("data", maxList);
			avgMap.put("data", avgList);
			datasetList.add(minMap);
			datasetList.add(avgMap);
			datasetList.add(maxMap);

			maps.put("chart", headMap);
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			categoryMap.put("category", dataList);
			maps.put("categories", categoryMap);
			maps.put("trendlines", trendlinesMap);
			maps.put("styles", stylesMap);
			maps.put("dataset", datasetList);
		}
		return maps;
	}

	/**
	 * 获取一定范围的响应详细（链接时间，下载时间，访问时间）
	 * 
	 * @param condition
	 * @return
	 */
	private Map<String, Object> getResponseDetailTime(QueryCondition condition) {
		String info = "";
		// 获取响应的各个连接时间（域名解析时间，下载时间等）
		List<Object[]> respDetial = null;
		String period = "";
		long startTime = condition.getStartTime().getTime();
		long endTime = condition.getEndTime().getTime();
		if ((endTime - startTime) >= (1000 * 60 * 60 * 24)) {
			respDetial = detialService.getResponseDetialTimeByCondition(
					condition, false);
			period = DAY;
		} else if ((endTime - startTime) <= (1000 * 60 * 60)
				&& (endTime - startTime) > 0) {
			respDetial = detialService
					.getMinuteResponseDetailByCondition(condition);
			period = MINUTE;
		} else {
			respDetial = detialService
					.getHourResponseDetailByCondition(condition);
			period = HOUR;
		}
		Map<String, Object> respDetialMap = new HashMap<String, Object>();
		if (Utils.notEmpty(respDetial)) {
			List<Long> dateList = null;
			boolean isHas = false;
			Map<String, Object> ext1Map = new HashMap<String, Object>();
			List<Map<String, String>> ext1List = new ArrayList<Map<String, String>>();
			ext1Map.put("seriesname", "DNS域名解析");
			Map<String, Object> ext2Map = new HashMap<String, Object>();
			List<Map<String, String>> ext2List = new ArrayList<Map<String, String>>();
			ext2Map.put("seriesname", "建立连接");
			Map<String, Object> ext3Map = new HashMap<String, Object>();
			List<Map<String, String>> ext3List = new ArrayList<Map<String, String>>();
			ext3Map.put("seriesname", "服务器计算");
			Map<String, Object> ext4Map = new HashMap<String, Object>();
			List<Map<String, String>> ext4List = new ArrayList<Map<String, String>>();
			ext4Map.put("seriesname", "下载内容");
			Map<String, Object> ext5Map = new HashMap<String, Object>();
			List<Map<String, String>> ext5List = new ArrayList<Map<String, String>>();
			ext5Map.put("seriesname", "总时间");
			List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
			if (Utils.notEmpty(respDetial)) {
				Map<String, String> tempMap = null, tempMap2 = null, tempMap3 = null, tempMap4 = null, tempMap5 = null;
				if (HOUR.equals(period)) {
					info = "(小时)";
					Map<String, String> labelMap = null;
					for (int i = 0; i < 24; i++) {
						labelMap = new HashMap<String, String>();
						if (i < 10) {
							labelMap.put("label", "0" + i);
						} else {
							labelMap.put("label", i + "");
						}
						dataList.add(labelMap);
					}
					for (Map<String, String> hourMap : dataList) {
						String hour = hourMap.get("label");
						isHas = false;
						for (Object[] objects : respDetial) {
							if (hour.equals(String.valueOf(objects[0]))) {
								tempMap = new HashMap<String, String>();
								tempMap2 = new HashMap<String, String>();
								tempMap3 = new HashMap<String, String>();
								tempMap4 = new HashMap<String, String>();
								tempMap5 = new HashMap<String, String>();
								tempMap.put("value", objects[1] + "");
								ext1List.add(tempMap);
								tempMap2.put("value", objects[2] + "");
								ext2List.add(tempMap2);
								tempMap3.put("value", objects[3] + "");
								ext3List.add(tempMap3);
								tempMap4.put("value", objects[4] + "");
								ext4List.add(tempMap4);
								tempMap5.put("value", String.valueOf(Double
										.valueOf(String.valueOf(objects[1]))
										+ Double.valueOf(String
												.valueOf(objects[2]))
										+ Double.valueOf(String
												.valueOf(objects[3]))
										+ Double.valueOf(String
												.valueOf(objects[4]))));
								ext5List.add(tempMap5);
								isHas = true;
								break;
							}
						}
						if (!isHas) {
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap4 = new HashMap<String, String>();
							tempMap5 = new HashMap<String, String>();
							tempMap.put("value", "0");
							ext1List.add(tempMap);
							tempMap2.put("value", "0");
							ext2List.add(tempMap2);
							tempMap3.put("value", "0");
							ext3List.add(tempMap3);
							tempMap4.put("value", "0");
							ext4List.add(tempMap4);
							tempMap5.put("value", "0");
							ext5List.add(tempMap5);
						}
					}

				} else if (MINUTE.equals(period)) {
					info = "(监控频率)";
					Map<String, String> labelMap = null;
					for (Object[] objects : respDetial) {
						tempMap = new HashMap<String, String>();
						tempMap2 = new HashMap<String, String>();
						tempMap3 = new HashMap<String, String>();
						tempMap4 = new HashMap<String, String>();
						tempMap5 = new HashMap<String, String>();
						tempMap.put("value", objects[1] + "");
						ext1List.add(tempMap);
						tempMap2.put("value", objects[2] + "");
						ext2List.add(tempMap2);
						tempMap3.put("value", objects[3] + "");
						ext3List.add(tempMap3);
						tempMap4.put("value", objects[4] + "");
						ext4List.add(tempMap4);
						tempMap5.put("value", String.valueOf(Double
								.valueOf(String.valueOf(objects[1]))
								+ Double.valueOf(String.valueOf(objects[2]))
								+ Double.valueOf(String.valueOf(objects[3]))
								+ Double.valueOf(String.valueOf(objects[4]))));
						ext5List.add(tempMap5);
						labelMap = new HashMap<String, String>();
						labelMap.put("label", String.valueOf(objects[0]));
						dataList.add(labelMap);
					}
				} else if (DAY.equals(period)) {
					info = "(天)";
					dateList = DateUtils.getDateBetween2Date(
							condition.getStartTime() != null ? sdf2
									.format(condition.getStartTime()) : null,
							condition.getEndTime() != null ? sdf2
									.format(condition.getEndTime()) : null,
							false);
					// 塞值
					if (Utils.notEmpty(dateList)) {
						Map<String, String> map = null;
						for (Long temp : dateList) {
							map = new HashMap<String, String>();
							isHas = false;
							map.put("label", (temp % 10000 / 100) + "-"
									+ (temp % 10000 % 100));
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap4 = new HashMap<String, String>();
							tempMap5 = new HashMap<String, String>();
							for (Object[] objects : respDetial) {
								if (temp.longValue() == (objects[0] != null ? ((Long) objects[0])
										.longValue() : 0L)) {

									tempMap.put("value", objects[1] + "");
									ext1List.add(tempMap);
									tempMap2.put("value", objects[2] + "");
									ext2List.add(tempMap2);
									tempMap3.put("value", objects[3] + "");
									ext3List.add(tempMap3);
									tempMap4.put("value", objects[4] + "");
									ext4List.add(tempMap4);
									tempMap5.put(
											"value",
											String.valueOf(Double.valueOf(String
													.valueOf(objects[1]))
													+ Double.valueOf(String
															.valueOf(objects[2]))
													+ Double.valueOf(String
															.valueOf(objects[3]))
													+ Double.valueOf(String
															.valueOf(objects[4]))));
									ext5List.add(tempMap5);
									isHas = true;
									break;
								}
							}
							if (!isHas) {
								tempMap.put("value", "0");
								ext1List.add(tempMap);
								tempMap2.put("value", "0");
								ext2List.add(tempMap2);
								tempMap3.put("value", "0");
								ext3List.add(tempMap3);
								tempMap4.put("value", "0");
								ext4List.add(tempMap4);
								tempMap5.put("value", "0");
								ext5List.add(tempMap5);
							}
							dataList.add(map);
						}
					}
				}
			}
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("caption", "响应时间变化详细曲线图");
			headMap.put("xAxisName", "日期" + info);
			headMap.put("yAxisName", "时间(ms)");
			headMap.put("labelStep", "5");
			headMap.put("formatNumberScale", "0");
			headMap.put("formatNumber", "0");
			Map<String, Object> trendlinesMap = new HashMap<String, Object>();
			Map<String, String> lineMap = new HashMap<String, String>();
			lineMap.put("color", "91C728");
			lineMap.put("showontop", "1");
			trendlinesMap.put("line", lineMap);

			Map<String, Object> stylesMap = new HashMap<String, Object>();
			Map<String, Object> definitionMap = new HashMap<String, Object>();
			Map<String, Object> applicationMap = new HashMap<String, Object>();
			Map<String, String> styleMap = new HashMap<String, String>();
			Map<String, String> applyMap = new HashMap<String, String>();
			styleMap.put("name", "CanvasAnim");
			styleMap.put("type", "animation");
			styleMap.put("param", "_xScale");
			styleMap.put("start", "0");
			styleMap.put("duration", "1");
			definitionMap.put("definition", styleMap);
			applyMap.put("toobject", "Canvas");
			applyMap.put("styles", "CanvasAnim");
			applicationMap.put("apply", applyMap);
			stylesMap.put("application", applicationMap);

			// 存放dataset值
			List<Map<String, Object>> datasetList = new ArrayList<Map<String, Object>>();
			ext1Map.put("data", ext1List);
			ext2Map.put("data", ext2List);
			ext3Map.put("data", ext3List);
			ext4Map.put("data", ext4List);
			ext5Map.put("data", ext5List);
			datasetList.add(ext1Map);
			datasetList.add(ext2Map);
			datasetList.add(ext3Map);
			datasetList.add(ext4Map);
			datasetList.add(ext5Map);

			respDetialMap.put("chart", headMap);
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			categoryMap.put("category", dataList);
			respDetialMap.put("categories", categoryMap);
			respDetialMap.put("trendlines", trendlinesMap);
			respDetialMap.put("styles", stylesMap);
			respDetialMap.put("dataset", datasetList);
		}
		return respDetialMap;
	}
	private void setTime(HttpServletRequest request, QueryCondition condition)
			throws ParseException {
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		Date st = (Date) request.getSession().getAttribute("startTime");
		Date et = (Date) request.getSession().getAttribute("endTime");
		long dateTime = new Date().getTime();
		// Date st = null;
		// Date et = null;
		if (!Utils.isEmpty(startTime)) {
			st = sdf.parse(startTime);
		} else if (st == null) {
			st = new Date(dateTime - 1000 * 60 * 60);
		}
		condition.setStartTime(st);
		request.getSession().setAttribute("startTime", st);

		if (!Utils.isEmpty(endTime)) {

			et = sdf.parse(endTime);
		} else if (et == null) {
			et = new Date(dateTime);
		}
		condition.setEndTime(et);
		request.getSession().setAttribute("endTime", et);
	}
	
	
	
	public ModelAndView cycleRefreshCharts(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subprojectId = request.getParameter("id");
		String timerInterval = request.getParameter("timerInterval");
		String type = request.getParameter("type");
		Date startTime = null;
		DecimalFormat df = new DecimalFormat("0.00");
		if(Utils.notEmpty(timerInterval)) {
			startTime = new Date(new Date().getTime()-Long.parseLong(timerInterval));
		}else {
			startTime = new Date();
		}
		List<Object[]> listSnmpStatistics = snmpService.getMimuteStatistics(
				subprojectId, startTime,
				null);
		List<Object[]> listRateSnmp = new ArrayList<Object[]>();
		if("rate".equals(type)) {
			for(Object[] obj : listSnmpStatistics) {
				Object[] o = new Object[4];
				o[0] = obj[7];
				o[1] = obj[0];
				o[2] = Double.parseDouble(df.format((Double) obj[1]
						/ (Double) obj[5] * 100));
				o[3] = Double.parseDouble(df.format((Double) obj[2]
						/ (Double) obj[6] * 100));
				listRateSnmp.add(o);
			}
		}else if("jvmThead".equals(type)){
			for(Object[] obj : listSnmpStatistics) {
				Object[] o = new Object[2];
				o[0] = obj[7];
				o[1] = obj[3];
				listRateSnmp.add(o);
			}
		}else if("io".equals(type)) {
			for(Object[] obj : listSnmpStatistics) {
				Object[] o = new Object[2];
				o[0] = obj[7];
				o[1] = obj[4];
				listRateSnmp.add(o);
			}
		}
		
		response.getWriter().write(gson.toJson(listRateSnmp));
		return null;
	}
	public ModelAndView setRefreshCharts(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryCondition condition = new QueryCondition();
		String subprojectId = request.getParameter("id");
		String isOpen = request.getParameter("isOpen");
		condition.setTaskId(subprojectId);
//		setTime(request, condition);
		Date time = new Date();
		condition.setStartTime(new Date(time.getTime()-1000*60*60));
		condition.setEndTime(time);
		Panel panel = null;
		CommandGroup cg = new CommandGroup("");
		UpdateCommand uc = new UpdateCommand("uc");
		if("close".equals(isOpen)) {
			panel = ClientMonitorView.buildSelectTimePanel(condition, true,subprojectId);
			Map<String, String> configMap = new HashMap<String, String>();
			double jvmThreadThreshold = 0,ioThreshold = 0;
			Subproject subproject = subprojectService.getSubprojectById(subprojectId);
			if(subproject != null && Utils.notEmpty(subproject.getConfigXml())) {
				Map<String, String> configmap = Utils.string2Map(subproject.getConfigXml());
				String thresholdIds = configmap.get("continueGroup");
				if(Utils.notEmpty(thresholdIds)) {
					String[] ids = thresholdIds.split(",");
					Threshold threshold = thresholdService.getThresholdById(ids[0]);
					if(threshold != null) {
						configMap = Utils.string2Map(threshold.getContent());
					}
				}
			}
			
			List<Object[]> listSnmpStatistics = snmpService.getMimuteStatistics(
					condition.getTaskId(), condition.getStartTime(),
					condition.getEndTime());
			Map<String, Object> jvmThreadMap = getSnmpJvmThread2(listSnmpStatistics,
					condition, jvmThreadThreshold, MINUTE);
			Map<String, Object> ioMap = getSnmpIo2(listSnmpStatistics, condition, ioThreshold,
					MINUTE);
			Map<String, Object> useableRateMap = getSnmpRate2(listSnmpStatistics, condition, configMap,  MINUTE);
			Panel chartPanel = ClientMonitorView.buildSnmpStatisticsPanel(gson.toJson(useableRateMap),
					gson.toJson(jvmThreadMap), gson.toJson(ioMap));
			UpdateCommand chartPanelCommand = new UpdateCommand(null);
			chartPanelCommand.setContent(chartPanel);
			cg.add(chartPanelCommand);
			//Panel chartPanel = 
			JSCommand js = new JSCommand("","registerTimer()");
			cg.add(js);
		}else {
			panel = ClientMonitorView.buildSelectTimePanel(condition, false,subprojectId);
			JSCommand js = new JSCommand("","removeTimer()");
			cg.add(js);
		}
		uc.setContent(panel);
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}
	
	private Map<String, Object> getSnmpRate2(List<Object[]> list, QueryCondition condition, Map<String, String> configMap, String isHour) {
		DecimalFormat df = new DecimalFormat("0.00");
		Map<String, Object> map = new HashMap<String, Object>();
		String[] xAxis = null;
		Object[] cpuData = null;
		Object[] memoryData = null;
		Object[] jvmMemoryData = null;
		Object[] diskUsage = null;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (!Utils.isEmpty(list)) {
			if (HOUR.equals(isHour)) {
				xAxis = new String[24];
				cpuData = new Object[24];
				memoryData = new Object[24];
				jvmMemoryData = new Object[24];
				diskUsage = new Object[24];
				for (int i = 0; i < 24; i++) {
					if (i < 10) {
						xAxis[i]= "0" + i;
					} else {
						xAxis[i]= i + "";
					}
				}
				String xAxi;
				for(int i=0; i<xAxis.length; i++) {
					xAxi = xAxis[i];
					boolean isHas = false;
					for (Object[] objects : list) {
						if (xAxi.equals(String.valueOf(objects[7]))) {
							cpuData[i] = objects[0];
							if((Double) objects[5] == 0) {
								memoryData[i] = 0;
							}else {
								memoryData[i] = Double.parseDouble(df.format((Double) objects[1]
										/ (Double) objects[5] * 100));
							}
							if((Double) objects[6] == 0) {
								jvmMemoryData[i] = 0;
							}else {
								jvmMemoryData[i] = Double.parseDouble(df.format((Double) objects[2]
										/ (Double) objects[6] * 100));
							}
							if((Double) objects[13] == 0) {
								diskUsage[i] = 0;
							}else {
								diskUsage[i] = Double.parseDouble(df.format((Double) objects[14]
										/ (Double) objects[13] * 100));
							}
							isHas = true;
							break;
						}
					}
					if(!isHas) {
						cpuData[i] = 0;
						memoryData[i] = 0;
						jvmMemoryData[i] = 0;
					}
				}
			}else if (MINUTE.equals(isHour)) {
				xAxis = new String[list.size()];
				cpuData = new Object[list.size()];
				memoryData = new Object[list.size()];
				jvmMemoryData = new Object[list.size()];
				diskUsage = new Object[list.size()];
				Object[] objects;
				for (int i=0; i<list.size(); i++) {
					objects = list.get(i);
					xAxis[i] = String.valueOf(objects[7]);
					cpuData[i] = objects[0];
					if((Double) objects[5] == 0) {
						memoryData[i] = 0;
					}else {
						memoryData[i] = Double.parseDouble(df.format((Double) objects[1]
								/ (Double) objects[5] * 100));
					}
					if((Double) objects[6] == 0) {
						jvmMemoryData[i] = 0;
					}else {
						jvmMemoryData[i] = Double.parseDouble(df.format((Double) objects[2]
								/ (Double) objects[6] * 100));
					}
					if((Double) objects[13] == 0) {
						diskUsage[i] = 0;
					}else {
						diskUsage[i] = Double.parseDouble(df.format((Double) objects[14]
								/ (Double) objects[13] * 100));
					}
				}
			}else if(DAY.equals(isHour)) {
				List<Long> dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf2
								.format(condition.getStartTime()) : null,
						condition.getEndTime() != null ? sdf2.format(condition
								.getEndTime()) : null, false);
				xAxis = new String[dateList.size()];
				if (Utils.notEmpty(dateList)) {
					Long temp = null;
					cpuData = new Object[dateList.size()];
					memoryData = new Object[dateList.size()];
					jvmMemoryData = new Object[dateList.size()];
					diskUsage = new Object[dateList.size()];
					for (int i=0; i<dateList.size(); i++) {
						temp = dateList.get(i);
						xAxis[i] =  (temp % 10000 / 100) + "-"+ (temp % 10000 % 100);
						Object[] objects;
						boolean isHas = false;
						for (int j=0; j<list.size(); j++) {
							objects = list.get(j);
							if (String.valueOf(temp).equals(
									String.valueOf(objects[7]))) {
								cpuData[i] = objects[0];
								if((Double) objects[5] == 0) {
									memoryData[i] = 0;
								}else {
									memoryData[i] = Double.parseDouble(df.format((Double) objects[1]
											/ (Double) objects[5] * 100));
								}
								if((Double) objects[6] == 0) {
									jvmMemoryData[i] = 0;
								}else {
									jvmMemoryData[i] = Double.parseDouble(df.format((Double) objects[2]
											/ (Double) objects[6] * 100));
								}
								if((Double) objects[13] == 0) {
									diskUsage[i] = 0;
								}else {
									diskUsage[i] = Double.parseDouble(df.format((Double) objects[14] / (Double) objects[13] * 100));
								}
								isHas = true;
								break;
							}
						}
						if(!isHas) {
							cpuData[i] = 0;
							memoryData[i] = 0;
							jvmMemoryData[i] = 0;
							diskUsage[i] = 0;
						}
					}
				}
			}
		}
//		map.put("xAxis", xAxis);
		Map<String, Object> cpuMap = new HashMap<String, Object>();
		cpuMap.put("name", "cpu使用率");
		cpuMap.put("data", cpuData);
		data.add(cpuMap);
		Map<String, Object> memoryMap = new HashMap<String, Object>();
		memoryMap.put("name", "内存使用率");
		memoryMap.put("data", memoryData);
		data.add(memoryMap);
		Map<String, Object> jvmMemoryMap = new HashMap<String, Object>();
		jvmMemoryMap.put("name", "jvm内存使用率");
		jvmMemoryMap.put("data", jvmMemoryData);
		data.add(jvmMemoryMap);
		Map<String, Object> diskUsageMap = new HashMap<String, Object>();
		diskUsageMap.put("name", "磁盘使用率");
		diskUsageMap.put("data", diskUsage);
		data.add(diskUsageMap);
		
		
		//构建chart
		Map<String, Object> chartMap = new HashMap<String, Object>();
		chartMap.put("type", "spline");
		chartMap.put("height", 300);
		map.put("chart", chartMap);
		
		//构建title
		Map<String, Object> titleMap = new HashMap<String, Object>();
		titleMap.put("text", "系统使用率图表");
		map.put("title", titleMap);
		
		//build xAxis
		Map<String, Object> xAxisMap = new HashMap<String, Object>();
		xAxisMap.put("gridLineWidth", 1);
		xAxisMap.put("lineColor", "#000");
		xAxisMap.put("tickColor", "#000");
		xAxisMap.put("type", "category");
		xAxisMap.put("categories", xAxis);
		map.put("xAxis", xAxisMap);

		//build yAxis
		Map<String, Object> yAxisMap = new HashMap<String, Object>();
		Map<String, Object> yAxisTitleMap = new HashMap<String, Object>();
		yAxisTitleMap.put("text", "使用率(%)");
		yAxisMap.put("title", yAxisTitleMap);
		yAxisMap.put("min", 0);
		//build threshold value
		List<Map<String, Object>> listPlotLine = new ArrayList<Map<String, Object>>();
		if (Utils.notEmpty(configMap.get("cpu"))) {
			Map<String, Object> thresholdValue = new HashMap<String, Object>();
			thresholdValue.put("color", "red");
			thresholdValue.put("dashStyle", "ShortDot");
			thresholdValue.put("value", configMap.get("cpu"));
			thresholdValue.put("width", 2);
			Map<String, Object> labelMap = new HashMap<String, Object>();
			labelMap.put("text", "cpu阀值");
			labelMap.put("align", "left");
			thresholdValue.put("label", labelMap);
			listPlotLine.add(thresholdValue);
		}
		if (Utils.notEmpty(configMap.get("memory"))) {
			Map<String, Object> thresholdValue = new HashMap<String, Object>();
			thresholdValue.put("color", "red");
			thresholdValue.put("dashStyle", "ShortDot");
			thresholdValue.put("value", configMap.get("memory"));
			thresholdValue.put("width", 2);
			Map<String, Object> labelMap = new HashMap<String, Object>();
			labelMap.put("text", "memory阀值");
			labelMap.put("align", "left");
			thresholdValue.put("label", labelMap);
			listPlotLine.add(thresholdValue);
		}
		if (Utils.notEmpty(configMap.get("jvmMemory"))) {
			Map<String, Object> thresholdValue = new HashMap<String, Object>();
			thresholdValue.put("color", "red");
			thresholdValue.put("dashStyle", "ShortDot");
			thresholdValue.put("value", configMap.get("jvmMemory"));
			thresholdValue.put("width", 2);
			Map<String, Object> labelMap = new HashMap<String, Object>();
			labelMap.put("text", "jvmMemory阀值");
			labelMap.put("align", "left");
			thresholdValue.put("label", labelMap);
			listPlotLine.add(thresholdValue);
		}
		
		if (Utils.notEmpty(configMap.get("diskUsage"))) {
			Map<String, Object> thresholdValue = new HashMap<String, Object>();
			thresholdValue.put("color", "red");
			thresholdValue.put("dashStyle", "ShortDot");
			thresholdValue.put("value", configMap.get("diskUsage"));
			thresholdValue.put("width", 2);
			Map<String, Object> labelMap = new HashMap<String, Object>();
			labelMap.put("text", "磁盘使用率阀值");
			labelMap.put("align", "left");
			thresholdValue.put("label", labelMap);
			listPlotLine.add(thresholdValue);
		}
		yAxisMap.put("plotLines", listPlotLine);
		map.put("yAxis", yAxisMap);
		
		
		//build credits
		Map<String, Object> creditsMap = new HashMap<String, Object>();
		creditsMap.put("enabled", false);
		map.put("credits", creditsMap);
		
		//build legend
		
		//build exporting
		Map<String, Object> exportingMap = new HashMap<String, Object>();
		exportingMap.put("enabled", false);
		map.put("exporting", exportingMap);
		
		//build series
		map.put("series", data);
		
		
		//add taskId
		map.put("taskId", condition.getTaskId());
		return map;
	}
	
	private Map<String, Object> getSnmpJvmThread2(List<Object[]> list, QueryCondition condition, double thresholeValue, String isHour) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] xAxis = null;
		Object[] jvmThreadData = null;
		Object[] systemProcessData = null;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (!Utils.isEmpty(list)) {
			if (HOUR.equals(isHour)) {
				xAxis = new String[24];
				jvmThreadData = new Object[24];
				systemProcessData = new Object[24];
				for (int i = 0; i < 24; i++) {
					if (i < 10) {
						xAxis[i]= "0" + i;
					} else {
						xAxis[i]= i + "";
					}
				}
				String xAxi;
				for(int i=0; i<xAxis.length; i++) {
					xAxi = xAxis[i];
					boolean isHas = false;
					for (Object[] objects : list) {
						if (xAxi.equals(String.valueOf(objects[7]))) {
							jvmThreadData[i] = objects[3];
							systemProcessData[i] = objects[12];
							isHas = true;
							break;
						}
					}
					if(!isHas) {
						jvmThreadData[i] = 0;
						systemProcessData[i] = 0;
					}
				}
			}else if (MINUTE.equals(isHour)) {
				xAxis = new String[list.size()];
				jvmThreadData = new Object[list.size()];
				systemProcessData = new Object[list.size()];
				Object[] objects;
				for (int i=0; i<list.size(); i++) {
					objects = list.get(i);
					xAxis[i] = String.valueOf(objects[7]);
					jvmThreadData[i] = objects[3];
					systemProcessData[i] = objects[12];
				}
			}else if(DAY.equals(isHour)) {
				List<Long> dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf2
								.format(condition.getStartTime()) : null,
						condition.getEndTime() != null ? sdf2.format(condition
								.getEndTime()) : null, false);
				xAxis = new String[dateList.size()];
				if (Utils.notEmpty(dateList)) {
					Long temp = null;
					jvmThreadData = new Object[dateList.size()];
					systemProcessData = new Object[dateList.size()];
					for (int i=0; i<dateList.size(); i++) {
						temp = dateList.get(i);
						xAxis[i] =  (temp % 10000 / 100) + "-"+ (temp % 10000 % 100);
						Object[] objects;
						boolean isHas = false;
						for (int j=0; j<list.size(); j++) {
							objects = list.get(j);
							if (String.valueOf(temp).equals(
									String.valueOf(objects[7]))) {
								jvmThreadData[i] = objects[3];
								systemProcessData[i] = objects[12];
								isHas = true;
								break;
							}
						}
						if(!isHas) {
							jvmThreadData[i] = 0;
							systemProcessData[0] = 0;
						}
					}
				}
			}
		}
//		map.put("xAxis", xAxis);
		Map<String, Object> jvmThreadMap = new HashMap<String, Object>();
		jvmThreadMap.put("name", "jvm线程数");
		jvmThreadMap.put("data", jvmThreadData);
		data.add(jvmThreadMap);
		
		Map<String, Object> systemProcessMap = new HashMap<String, Object>();
		systemProcessMap.put("name", "系统进程数");
		systemProcessMap.put("data", systemProcessData);
		data.add(systemProcessMap);
		
		
		//构建chart
		Map<String, Object> chartMap = new HashMap<String, Object>();
		chartMap.put("type", "spline");
		chartMap.put("height", 300);
		map.put("chart", chartMap);
		
		//构建title
		Map<String, Object> titleMap = new HashMap<String, Object>();
		titleMap.put("text", "线程数图表");
		map.put("title", titleMap);
		
		//build xAxis
		Map<String, Object> xAxisMap = new HashMap<String, Object>();
		xAxisMap.put("gridLineWidth", 1);
		xAxisMap.put("lineColor", "#000");
		xAxisMap.put("tickColor", "#000");
		xAxisMap.put("type", "category");
		xAxisMap.put("categories", xAxis);
		map.put("xAxis", xAxisMap);

		//build yAxis
		Map<String, Object> yAxisMap = new HashMap<String, Object>();
		Map<String, Object> yAxisTitleMap = new HashMap<String, Object>();
		yAxisTitleMap.put("text", "数量");
		yAxisMap.put("title", yAxisTitleMap);
		yAxisMap.put("min", 0);
		List<Map<String, Object>> listPlotLine = new ArrayList<Map<String, Object>>();
		if (thresholeValue > 0) {
			Map<String, Object> thresholdValueMap = new HashMap<String, Object>();
			thresholdValueMap.put("color", "red");
			thresholdValueMap.put("dashStyle", "ShortDot");
			thresholdValueMap.put("value", thresholeValue);
			thresholdValueMap.put("width", 2);
			Map<String, Object> labelMap = new HashMap<String, Object>();
			labelMap.put("text", "jvm线程数阀值");
			labelMap.put("align", "left");
			thresholdValueMap.put("label", labelMap);
			listPlotLine.add(thresholdValueMap);
			//cpuMap.put("name", "CPU使用率(阀值:"+configMap.get("cpu")+"%)");
		}
		yAxisMap.put("plotLines", listPlotLine);
		map.put("yAxis", yAxisMap);
		
		
		//build credits
		Map<String, Object> creditsMap = new HashMap<String, Object>();
		creditsMap.put("enabled", false);
		map.put("credits", creditsMap);
		
		//build legend
		
		//build exporting
		Map<String, Object> exportingMap = new HashMap<String, Object>();
		exportingMap.put("enabled", false);
		map.put("exporting", exportingMap);
		
		//build series
		map.put("series", data);
		
		
		//add taskId
		map.put("taskId", condition.getTaskId());
		return map;
	}
	
	private Map<String, Object> getSnmpIo2(List<Object[]> list, QueryCondition condition, double thresholeValue, String isHour) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] xAxis = null;
		Object[] ifInData = null;
		Object[] ifOutData = null;
		Object[] diskReadData = null;
		Object[] diskWriteData = null;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (!Utils.isEmpty(list)) {
			if (HOUR.equals(isHour)) {
				xAxis = new String[24];
				ifInData = new Object[24];
				ifOutData = new Object[24];
				diskReadData = new Object[24];
				diskWriteData = new Object[24];
				for (int i = 0; i < 24; i++) {
					if (i < 10) {
						xAxis[i]= "0" + i;
					} else {
						xAxis[i]= i + "";
					}
				}
				String xAxi;
				for(int i=0; i<xAxis.length; i++) {
					xAxi = xAxis[i];
					boolean isHas = false;
					for (Object[] objects : list) {
						if (xAxi.equals(String.valueOf(objects[7]))) {
							ifInData[i] = objects[8];
							ifOutData[i] = objects[9];
							diskReadData[i] = objects[10];
							diskWriteData[i] = objects[11];
							isHas = true;
							break;
						}
					}
					if(!isHas) {
						ifInData[i] = 0;
						ifOutData[i] = 0;
						diskReadData[i] = 0;
						diskWriteData[i] = 0;
					}
				}
			}else if (MINUTE.equals(isHour)) {
				xAxis = new String[list.size()];
				ifInData = new Object[list.size()];
				ifOutData = new Object[list.size()];
				diskReadData = new Object[list.size()];
				diskWriteData = new Object[list.size()];
				Object[] objects;
				for (int i=0; i<list.size(); i++) {
					objects = list.get(i);
					xAxis[i] = String.valueOf(objects[7]);
					ifInData[i] = objects[8];
					ifOutData[i] = objects[9];
					diskReadData[i] = objects[10];
					diskWriteData[i] = objects[11];
				}
			}else if(DAY.equals(isHour)) {
				List<Long> dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf2
								.format(condition.getStartTime()) : null,
						condition.getEndTime() != null ? sdf2.format(condition
								.getEndTime()) : null, false);
				xAxis = new String[dateList.size()];
				if (Utils.notEmpty(dateList)) {
					Long temp = null;
					ifInData = new Object[dateList.size()];
					ifOutData = new Object[dateList.size()];
					diskReadData = new Object[dateList.size()];
					diskWriteData = new Object[dateList.size()];
					for (int i=0; i<dateList.size(); i++) {
						temp = dateList.get(i);
						xAxis[i] =  (temp % 10000 / 100) + "-"+ (temp % 10000 % 100);
						Object[] objects;
						boolean isHas = false;
						for (int j=0; j<list.size(); j++) {
							objects = list.get(j);
							if (String.valueOf(temp).equals(
									String.valueOf(objects[7]))) {
								ifInData[i] = objects[8];
								ifOutData[i] = objects[9];
								diskReadData[i] = objects[10];
								diskWriteData[i] = objects[11];
								isHas = true;
								break;
							}
						}
						if(!isHas) {
							ifInData[i] = 0;
							ifOutData[i] = 0;
							diskReadData[i] = 0;
							diskWriteData[i] = 0;
						}
					}
				}
			}
		}
//		map.put("xAxis", xAxis);
		Map<String, Object> ifInMap = new HashMap<String, Object>();
		ifInMap.put("name", "网络流量(流入)");
		ifInMap.put("data", ifInData);
		data.add(ifInMap);
		
		Map<String, Object> ifOutMap = new HashMap<String, Object>();
		ifOutMap.put("name", "网络流量(流出)");
		ifOutMap.put("data", ifOutData);
		data.add(ifOutMap);
		
		Map<String, Object> diskReadMap = new HashMap<String, Object>();
		diskReadMap.put("name", "磁盘IO(写入)");
		diskReadMap.put("data", diskReadData);
		data.add(diskReadMap);
		
		Map<String, Object> diskWriteMap = new HashMap<String, Object>();
		diskWriteMap.put("name", "磁盘IO(写出)");
		diskWriteMap.put("data", diskWriteData);
		data.add(diskWriteMap);
		
		
		//构建chart
		Map<String, Object> chartMap = new HashMap<String, Object>();
		chartMap.put("type", "spline");
		chartMap.put("height", 300);
		map.put("chart", chartMap);
		
		//构建title
		Map<String, Object> titleMap = new HashMap<String, Object>();
		titleMap.put("text", "流量图表");
		map.put("title", titleMap);
		
		//build xAxis
		Map<String, Object> xAxisMap = new HashMap<String, Object>();
		xAxisMap.put("gridLineWidth", 1);
		xAxisMap.put("lineColor", "#000");
		xAxisMap.put("tickColor", "#000");
		xAxisMap.put("type", "category");
		xAxisMap.put("categories", xAxis);
		map.put("xAxis", xAxisMap);

		//build yAxis
		Map<String, Object> yAxisMap = new HashMap<String, Object>();
		Map<String, Object> yAxisTitleMap = new HashMap<String, Object>();
		yAxisTitleMap.put("text", "速度(Kb/s)");
		yAxisMap.put("title", yAxisTitleMap);
		yAxisMap.put("min", 0);
//		List<Map<String, Object>> listPlotLine = new ArrayList<Map<String, Object>>();
//		if (thresholeValue > 0) {
//			Map<String, Object> thresholdValueMap = new HashMap<String, Object>();
//			thresholdValueMap.put("color", "red");
//			thresholdValueMap.put("dashStyle", "ShortDot");
//			thresholdValueMap.put("value", thresholeValue*1024);
//			thresholdValueMap.put("width", 2);
//			Map<String, Object> labelMap = new HashMap<String, Object>();
//			labelMap.put("text", "网络流量阀值");
//			labelMap.put("align", "left");
//			thresholdValueMap.put("label", labelMap);
//			listPlotLine.add(thresholdValueMap);
//			//cpuMap.put("name", "CPU使用率(阀值:"+configMap.get("cpu")+"%)");
//		}
//		yAxisMap.put("plotLines", listPlotLine);
		map.put("yAxis", yAxisMap);
		
		
		//build credits
		Map<String, Object> creditsMap = new HashMap<String, Object>();
		creditsMap.put("enabled", false);
		map.put("credits", creditsMap);
		
		//build legend
		
		//build exporting
		Map<String, Object> exportingMap = new HashMap<String, Object>();
		exportingMap.put("enabled", false);
		map.put("exporting", exportingMap);
		
		//build series
		map.put("series", data);
		
		
		//add taskId
		map.put("taskId", condition.getTaskId());
		return map;
	}
	
	public Map<String, Object> getOverviewChart(Map<String, Integer> monitorMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		//构建chart
		Map<String, Object> chartMap = new HashMap<String, Object>();
		chartMap.put("type", "column");
		chartMap.put("height", 300);
		map.put("chart", chartMap);
		
		//构建title
		Map<String, Object> titleMap = new HashMap<String, Object>();
		titleMap.put("text", "监控服务统计");
		map.put("title", titleMap);
		
		//构建xAxis
		Map<String, Object> xAxisMap = new HashMap<String, Object>();
		xAxisMap.put("categories", new String[]{"HTTP监控","PING监控","SNMP监控"});
		map.put("xAxis", xAxisMap);
		
		//构建yAxis
		Map<String, Object> yAxisMap = new HashMap<String, Object>();
		yAxisMap.put("min", 0);
		Map<String, Object> yAxisTitle = new HashMap<String, Object>();
		yAxisTitle.put("text", "个数");
		yAxisMap.put("title", yAxisTitle);
		map.put("yAxis", yAxisMap);
		
		//build exporting
		Map<String, Object> exportingMap = new HashMap<String, Object>();
		exportingMap.put("enabled", false);
		map.put("exporting", exportingMap);
		
		//build credits
		Map<String, Object> creditsMap = new HashMap<String, Object>();
		creditsMap.put("enabled", false);
		map.put("credits", creditsMap);
		
		//构建series
		Map<String, Object> seriesMap = new HashMap<String, Object>();
		List<Map<String, Object>> listSeries = new ArrayList<Map<String, Object>>();
		Map<String, Object> usableMap = new HashMap<String, Object>();
		usableMap.put("name", "正常");
		usableMap.put("color", "#a0d36e");
		usableMap.put("data", new int[]{monitorMap.get("http_success")==null?0:monitorMap.get("http_success"),monitorMap.get("ping_success")==null?0:monitorMap.get("ping_success"),monitorMap.get("snmp_success")==null?0:monitorMap.get("snmp_success")});
		Map<String, Object> dataLabelMap = new HashMap<String, Object>();
		dataLabelMap.put("enabled", true);
		dataLabelMap.put("color", "#000000");
		dataLabelMap.put("align", "center");
		dataLabelMap.put("x", 0);
		dataLabelMap.put("y", 0);
		usableMap.put("dataLabels", dataLabelMap);
		
		listSeries.add(usableMap);
		Map<String, Object> unusableMap = new HashMap<String, Object>();
		unusableMap.put("name", "异常");
		unusableMap.put("color", "#fb6e52");
		unusableMap.put("data", new int[]{monitorMap.get("http_error")==null?0:monitorMap.get("http_error"),monitorMap.get("ping_error")==null?0:monitorMap.get("ping_error"),monitorMap.get("snmp_error")==null?0:monitorMap.get("snmp_error")});
		unusableMap.put("dataLabels", dataLabelMap);
		listSeries.add(unusableMap);
		map.put("series", listSeries);
		return map;
	}
	
	public Map<String, Object> getOverviewResponseTimeChart(List<Object[]> list) {
		List<String> listCategories = new ArrayList<String>();
		List<Double> listValues = new ArrayList<Double>();
		for(Object[] obj : list) {
			if((Double)obj[5] > 0) {
				listCategories.add((String)obj[1]);
				listValues.add((Double)obj[5]);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//构建chart
		Map<String, Object> chartMap = new HashMap<String, Object>();
		chartMap.put("type", "column");
		chartMap.put("height", 300);
		map.put("chart", chartMap);
		
		//构建title
		Map<String, Object> titleMap = new HashMap<String, Object>();
		titleMap.put("text", "网站响应时间统计");
		map.put("title", titleMap);
		
		//构建xAxis
		Map<String, Object> xAxisMap = new HashMap<String, Object>();
		xAxisMap.put("categories", listCategories.toArray());
		map.put("xAxis", xAxisMap);
		
		//构建yAxis
		Map<String, Object> yAxisMap = new HashMap<String, Object>();
		yAxisMap.put("min", 0);
		Map<String, Object> yAxisTitle = new HashMap<String, Object>();
		yAxisTitle.put("text", "响应时间(ms)");
		yAxisMap.put("title", yAxisTitle);
		map.put("yAxis", yAxisMap);
		
		//build exporting
		Map<String, Object> exportingMap = new HashMap<String, Object>();
		exportingMap.put("enabled", false);
		map.put("exporting", exportingMap);
		
		//build credits
		Map<String, Object> creditsMap = new HashMap<String, Object>();
		creditsMap.put("enabled", false);
		map.put("credits", creditsMap);
		
		//构建series
		List<Map<String, Object>> listSeries = new ArrayList<Map<String, Object>>();
		Map<String, Object> usableMap = new HashMap<String, Object>();
		usableMap.put("name", "网站响应时间");
		usableMap.put("color", "#92dff7");
		usableMap.put("data", listValues.toArray());
		Map<String, Object> dataLabelMap = new HashMap<String, Object>();
		dataLabelMap.put("enabled", true);
		dataLabelMap.put("color", "#000000");
		dataLabelMap.put("align", "center");
		dataLabelMap.put("x", 0);
		dataLabelMap.put("y", 0.1);
		usableMap.put("dataLabels", dataLabelMap);
		listSeries.add(usableMap);
		map.put("series", listSeries);
		return map;
	}
	
	
	public ModelAndView updateOverView(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String userId = (String) request.getSession().getAttribute(
				LOGIN_USER_KEY);
		String update = request.getParameter("update");
		QueryCondition condition = new QueryCondition(request);
		String projectId = request.getParameter("projectId");
		if(Utils.notEmpty(projectId)) {
			request.getSession().setAttribute("projectId", projectId);
		}else {
			projectId = (String) request.getSession().getAttribute("projectId");
			if(Utils.isEmpty(projectId)) {
				return null;
			}
		}
		
		Map<String, Integer> monitorMap = new HashMap<String, Integer>();
		List<Object[]> dataList = new ArrayList<Object[]>();
		List<Subproject> listSubproject = subprojectService.finSubprojectByProjectId(projectId);
		List<Object[]> listTaskResult = detialService.getLatestStatusByProjectId(projectId);
		DecimalFormat df = new DecimalFormat("0.00");
		if(Utils.notEmpty(listSubproject)) {
			for(Subproject sp : listSubproject) {
				if(Constants.SNMP_TYPE.equals(sp.getType().toLowerCase())) {
					if(!"1".equals(sp.getIsuable())) {
						continue;
					}
					String configStr = sp.getConfigXml();
					Threshold threshold = null;
					if(Utils.notEmpty(configStr)) {
						Map<String, String> map = Utils.string2Map(configStr);
						String thresholdId = map.get("continueGroup");
						threshold = thresholdService.getThresholdById(thresholdId);
					}
					SnmpModel snmpModel = snmpService.getLatestSnmp(sp.getId());
					if(snmpModel != null) {
						Object[] obj = new Object[11];
						obj[0] = sp.getName();
						obj[1] = sp.getUrl();
						obj[2] = sdf.format(snmpModel.getCreateTime());
						boolean isError = false;
						if(threshold != null) {
							Map<String, String> map = Utils.string2Map(threshold.getContent());
							
							if(Double.parseDouble(map.get("cpu")) <= Double.parseDouble(snmpModel.getCpuUsedRate())) {
								obj[3] = "<span style='color:red'>" + snmpModel.getCpuUsedRate() + "(阀值:"+map.get("cpu")+")</span>";
								isError = true;
							}else {
								obj[3] = snmpModel.getCpuUsedRate();
							}
							
							double memoryRate = Double.parseDouble(snmpModel.getMemoryUsedSize()) / Double.parseDouble(snmpModel.getMemoryTotalSize()) * 100;
							if(Double.parseDouble(map.get("memory")) <= memoryRate) {
								obj[4] = "<span style='color:red'>" +  df.format(memoryRate) + "(阀值:"+map.get("memory")+")</span>";
								isError = true;
							}else {
								obj[4] = df.format(memoryRate);
							}
							
							double jvmMemoryRate =Double.parseDouble(snmpModel.getJvmHeapUsedSize()) /Double.parseDouble(snmpModel.getJvmHeadTotalSize()) * 100;
							if(Double.parseDouble(map.get("jvmMemory")) <= jvmMemoryRate) {
								obj[5] = "<span style='color:red'>" + df.format(jvmMemoryRate) + "(阀值:"+map.get("jvmMemory")+")</span>";
								isError = true;
							}else {
								obj[5] = df.format(jvmMemoryRate);
							}
							
							if(Double.parseDouble(map.get("jvmThread")) <= Double.parseDouble(snmpModel.getJvmTheadSize())) {
								obj[6] = "<span style='color:red'>" + snmpModel.getJvmTheadSize() + "(阀值:"+map.get("jvmThread")+")</span>";
								isError = true;
							}else {
								obj[6] = snmpModel.getJvmTheadSize();
							}
							double io = Double.parseDouble(snmpModel.getIoUsedSize()) / 1024 / 1024;
//							if(Double.parseDouble(map.get("io")) <= io) {
//								obj[7] = "<span style='color:red'>" + df.format(io) + "(阀值:"+map.get("io")+")</span>";
//								isError = true;
//							}else {
								obj[7] = df.format(Double.parseDouble(snmpModel.getIfInSize()) / 1024)+"|"+df.format(Double.parseDouble(snmpModel.getIfOutSize())/1024)+"(Kb/s)";
//							}
								obj[8] = df.format(Double.parseDouble(snmpModel.getDiskIOReadSize())/1024)+ "|" +df.format(Double.parseDouble(snmpModel.getDiskIOWrittenSize())/1024)+"(Kb/s)";
							double storageUsed = Double.parseDouble(snmpModel.getStorageUsed())/Double.parseDouble(snmpModel.getStorageSize())*100;
							if(Double.parseDouble(map.get("diskUsage")) <= storageUsed) {
								obj[9] = "<span style='color:red'>"+ df.format(storageUsed) + "%" + "(阀值:"+map.get("diskUsage")+")</span>";
							}else {
								obj[9] = df.format(storageUsed) + "%";
							}
							double systemProcess = Double.parseDouble(snmpModel.getSystemProcess());
							if(Double.parseDouble(map.get("systemProcess")) <= systemProcess) {
								obj[10] = "<span style='color:red'>"+snmpModel.getSystemProcess()+ "(阀值:"+map.get("systemProcess")+")</span>";
							}else {
								obj[10] = snmpModel.getSystemProcess();
							}
						}else {
							obj[3] = snmpModel.getCpuUsedRate();
							obj[4] = df
									.format(Double.parseDouble(snmpModel.getMemoryUsedSize()) / Double.parseDouble(snmpModel
											.getMemoryTotalSize()) * 100);
							obj[5] = df.format(Double.parseDouble(snmpModel
									.getJvmHeapUsedSize()) /Double.parseDouble(snmpModel
											.getJvmHeadTotalSize()) * 100);
							obj[6] = Integer.parseInt(snmpModel.getJvmTheadSize());
							obj[7] = df.format(Double.parseDouble(snmpModel.getIfInSize()) / 1024)+"|"+df.format(Double.parseDouble(snmpModel.getIfOutSize())/1024)+"(Kb/s)";
							obj[8] = df.format(Double.parseDouble(snmpModel.getDiskIOReadSize())/1024/1024)+ "/" +df.format(Double.parseDouble(snmpModel.getDiskIOWrittenSize())/1024/1024);
							obj[9] = df.format(Double.parseDouble(snmpModel.getStorageUsed())/Double.parseDouble(snmpModel.getStorageSize())*100) + "%";
							obj[10] = snmpModel.getSystemProcess();
						}
						dataList.add(obj);
						if(isError) {
							if(monitorMap.get("snmp_error") == null) {
								monitorMap.put("snmp_error", 1);
							}else {
								monitorMap.put("snmp_error", monitorMap.get("snmp_error")+1);
							}
						}else {
							if(monitorMap.get("snmp_success") == null) {
								monitorMap.put("snmp_success", 1);
							}else {
								monitorMap.put("snmp_success", monitorMap.get("snmp_success")+1);
							}
						}
						
					}
				}
			}
		}else {
			AlertCommand alt = new AlertCommand("alt", "尚未创建监控服务！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5);
			outPutXML(response, alt);
			return null;
		}
		
		
		for(Object[] obj : listTaskResult) {
			if(Constants.HTTP_TYPE.equals(obj[3])) {
				if("可用".equals(obj[4])) {
					if(monitorMap.get("http_success") == null) {
						monitorMap.put("http_success", 1);
					}else {
						monitorMap.put("http_success", monitorMap.get("http_success")+1);
					}
				}else {
					if(monitorMap.get("http_error") == null) {
						monitorMap.put("http_error", 1);
					}else {
						monitorMap.put("http_error", monitorMap.get("http_error")+1);
					}
				}
			}else if(Constants.PING_TYPE.equals(obj[3])) {
				if("可用".equals(obj[4])) {
					if(monitorMap.get("ping_success") == null) {
						monitorMap.put("ping_success", 1);
					}else {
						monitorMap.put("ping_success", monitorMap.get("ping_success")+1);
					}
				}else {
					if(monitorMap.get("ping_error") == null) {
						monitorMap.put("ping_error", 1);
					}else {
						monitorMap.put("ping_error", monitorMap.get("ping_error")+1);
					}
				}
			}
		}
		
		Map<String, Subproject> subprojectMap = subprojectService.findSubprojectsByProjectId(projectId);
		List<FaultHistory> listFaultHistory = new ArrayList<FaultHistory>();
		List<Object[]> listHistory = new ArrayList<Object[]>();
		if(subprojectMap != null) {
			listFaultHistory = ServiceLocator.getFaultHistoryService().getFaultHistoryInfoByProjectId(projectId);
			Object[] objects = null;
			for(FaultHistory fh : listFaultHistory) {
				objects = new Object[7];
				objects[0] = fh.getFhId();
				objects[1] = subprojectMap.get(fh.getTaskId()).getName();
				objects[2] = fh.getFhBeginTime();
				objects[3] = fh.getFhEndTime();
				objects[4] = ClientMonitorView.getDuration(fh.getFhBeginTime(),
						fh.getFhEndTime());
				objects[5] = fh.getFhReason();
				listHistory.add(objects);
			}
		}
		
		
		Gson gson = new Gson();
		Map<String, Object> overviewCharts = getOverviewChart(monitorMap);
		Map<String, Object> overviewResponseTimeCharts = getOverviewResponseTimeChart(listTaskResult);
		setTime(request, condition);
		Panel chartPanel = ClientMonitorView.buildOverviewChart(gson.toJson(overviewCharts), gson.toJson(overviewResponseTimeCharts));
		Panel httpPanel = ClientMonitorView.buildOverviewHttpPanel(listTaskResult);
		Panel snmpPanel = ClientMonitorView.buildOverviewSnmpPanel(dataList);
		Panel errorPanel = ClientMonitorView.buildOverviewErrorPanel(listHistory);
		if (update != null && "1".equals(update)) {
//			System.out.println("==================================update=====================================");
			CommandGroup cg = new CommandGroup("");
			UpdateCommand uc = new UpdateCommand("uc");
			uc.setContent(chartPanel);
			cg.add(uc);
			
			UpdateCommand uc1 = new UpdateCommand("uc1");
			uc1.setContent(httpPanel);
			cg.add(uc1);
			
			UpdateCommand uc2 = new UpdateCommand("uc2");
			uc2.setContent(snmpPanel);
			cg.add(uc2);
			
			UpdateCommand uc3 = new UpdateCommand("uc3");
			uc3.setContent(errorPanel);
			cg.add(uc3);
			cg.setPath("/monitor/overView");
			outPutXML(response, cg);
		} 
		return null;
	}
	
	

	public void setSubprojectService(SubprojectService subprojectService) {
		this.subprojectService = subprojectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public void setThresholdService(ThresholdService thresholdService) {
		this.thresholdService = thresholdService;
	}

		
}
