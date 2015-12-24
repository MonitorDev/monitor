package com.rongji.websiteMonitor.webapp.visit.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.HighlightStyle;
import com.rongji.dfish.engines.xmltmpl.JSParser;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.PanelContainerPart;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.View;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.button.ExpandableButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.ConfirmCommand;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.DockPanel;
import com.rongji.dfish.engines.xmltmpl.component.FlowPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridColumn;
import com.rongji.dfish.engines.xmltmpl.component.GridColumnType;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanelPubInfo;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.Horizontalgroup;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.TabPanel;
import com.rongji.dfish.engines.xmltmpl.component.TreeItem;
import com.rongji.dfish.engines.xmltmpl.component.TreePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.EmbededButton;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.engines.xmltmpl.form.Radio;
import com.rongji.dfish.engines.xmltmpl.form.Radiogroup;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.HighChart;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.HighChartPlugin;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Axis;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Chart;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Events;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Exporting;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Labels;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Legend;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Series;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Title;
import com.rongji.dfish.engines.xmltmpl.plugin.highchart.json.Tooltip;
import com.rongji.dfish.framework.view.ViewTemplate;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class ClientMonitorView {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Panel buildMain(Project project,
			List<Subproject> listSubproject, String cp) {
		TabPanel tabPanel = new TabPanel("m-main2");
		tabPanel.setStyle("margin-top:2px;ont-size:12pt;font-weight:bold");
		tabPanel.setStyle("margin:0 30px");
		tabPanel.setVerticalMinus(-4);
		tabPanel.setHorizontalMinus(-5);
		tabPanel.setFilmHmins(2);
		tabPanel.setFilmVmins(2);
		tabPanel.setFilmStyle("border:0px;");
		String overView = "vm:|clientMonitor.sp?act=showOverView&projectId="
				+ project.getId();
		tabPanel.addSubPanel(null, "概况", new SourcePanel("overView", overView));
		String avaliableView = "vm:|clientMonitor.sp?act=showAvailableView&type="+Constants.HTTP_TYPE+"&projectId="+project.getId();
		tabPanel.addSubPanel(null,  "HTTP监控",
				new SourcePanel("httpView_" + project.getId(),
						avaliableView));
		
		String pingView = "vm:|clientMonitor.sp?act=showAvailableView&type="+Constants.PING_TYPE+"&projectId="+project.getId();
		tabPanel.addSubPanel(null,  "PING监控",
				new SourcePanel("pingView_" + project.getId(),
						pingView));
		
		String snmpView = "vm:|clientMonitor.sp?act=showSnmpView&type="+Constants.SNMP_TYPE+"&projectId="+project.getId();
		tabPanel.addSubPanel(null,  "服务器监控",
				new SourcePanel("snmpView_" + project.getId(),
						snmpView));
//		for (Subproject sp : listSubproject) {
//			if ("http".equals(sp.getType().toLowerCase())) {
//				String avaliableView = "vm:|clientMonitor.sp?act=showAvailableView&id="
//						+ sp.getId();
//				tabPanel.addSubPanel(null,  sp.getName(),
//						new SourcePanel("avaliableView_" + sp.getId(),
//								avaliableView));
//			}
//		}
//		for (Subproject sp : listSubproject) {
//			if ("ping".equals(sp.getType().toLowerCase())) {
//				String avaliableView = "vm:|clientMonitor.sp?act=showAvailableView&type=ping&id="
//						+ sp.getId();
//				tabPanel.addSubPanel(null, sp.getName() ,
//						new SourcePanel("avaliableView_" + sp.getId(),
//								avaliableView));
//			}
//		}
//		for (Subproject sp : listSubproject) {
//			if ("snmp".equals(sp.getType().toLowerCase())) {
//				String snmpView = "vm:|clientMonitor.sp?act=showSnmpView&id="
//						+ sp.getId();
//				tabPanel.addSubPanel(null, sp.getName(),
//						new SourcePanel("snmpView_" + sp.getId(), snmpView));
//			}
//		}
		VerticalPanel right = new VerticalPanel("m-main", "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(null, project.getName());
		HorizontalPanel title = new HorizontalPanel("f_title", "93%,*");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		title.addSubPanel(titleHtml);
		HtmlPanel backHtml = new HtmlPanel(null, "<a href=\"javascript:void(0);\" onclick=\"VM(this).cmd('monitorDetailMgr','4','"+cp+"')\">返回</a>");
		backHtml.setStyle("margin-right:10px");
		title.addSubPanel(backHtml);
		right.addSubPanel(title, tabPanel);
		return right;
	}

	private static Panel fillFaultHistoryGrid(List<Object[]> list) {
		GridPanel grid = new GridPanel("faultHistory_grid");
		grid.pub().setHeaderClass("bg-mid");
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(40);
		grid.setFilter(false);
		grid.setScroll(Scroll.hidden);
		grid.setData(list);
		grid.addHidden("0", "fhId");
		grid.addTextColumn(1, "taskName", "监控服务名称", "*", "taskName");
		grid.addTextColumn(2, "beginTime", "开始时间", "*", "beginTime");
		grid.addTextColumn(3, "endTime", "恢复时间", "*", "endTime");
		grid.addTextColumn(4, "duration", "故障持续时间", "*", "duration");
		grid.addTextColumn(5, "reason", "故障原因", "*", "reason");
//		GridColumn objId = new GridColumn(GridColumnType.TEXT, 5, "op", "操作",
//				"70");
//		objId.setParser("converObject");
//		grid.addColumn(objId);
//		grid.addHiddenColumn(6, "isIgnore");
		return grid;
	}
	private static Panel fillFauleGrid(List<FaultHistory> list) {
		GridPanel grid = new GridPanel("f_f_grid");
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objects = null;
		if (Utils.notEmpty(list)) {
			for (FaultHistory history : list) {
				objects = new Object[7];
				objects[0] = history.getFhId();
				objects[1] = history.getFhBeginTime();
				objects[2] = history.getFhEndTime();
				objects[3] = getDuration(history.getFhBeginTime(),
						history.getFhEndTime());
				objects[4] = history.getFhReason();
				// objects[5] = history.getFhType();
				objects[5] = history.getFhId();
				objects[6] = history.getIsIgnore();
				dataList.add(objects);
			}
		}
		grid.pub().setHeaderClass("bg-mid");
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(30);
		grid.setScroll(Scroll.miniscroll);
		grid.setData(dataList);
		grid.addHidden("0", "fhId");
		grid.addTextColumn(1, "beginTime", "开始时间", "*", "beginTime");
		grid.addTextColumn(2, "endTime", "恢复时间", "*", "endTime");
		grid.addTextColumn(3, "duration", "故障持续时间", "*", "duration");
		grid.addTextColumn(4, "reason", "故障原因", "*", "reason");
		GridColumn objId = new GridColumn(GridColumnType.TEXT, 5, "op", "操作",
				"70");
		objId.setParser("converObject");
		grid.addColumn(objId);
		grid.addHiddenColumn(6, "isIgnore");
		return grid;
	}

	public static String getDuration(Date beginTime, Date endTime) {
		if (beginTime == null) {
			return "";
		}
		if(endTime == null) {
			endTime = new Date();
		}
		long begin = beginTime.getTime();
		long end = endTime.getTime();
		long temp = end - begin;
		long temp2 = 0L, temp3 = 0L, temp4 = 0L;
		if (temp > 0) {
			temp = temp / 1000;
		}
		if (temp > 0 && temp < 60) {
			return temp + "秒";
		} else {
			temp2 = temp % 60;// 秒
			temp = temp / 60;
			if (temp > 0 && temp < 60) {
				return temp + "分" + (temp2 != 0 ? temp2 + "秒" : "");
			} else {
				temp3 = temp % 60;// 分
				temp = temp / 60;
				if (temp > 0 && temp < 24) {
					return temp + "小时" + (temp3 != 0 ? temp3 + "分" : "");
				} else {
					temp4 = temp % 24;// 小时
					temp = temp / 24;
					return temp + "天" + (temp4 != 0 ? temp4 + "小时" : "");
				}
			}
		}
	}
	
	public static VerticalPanel buildOverviewChart(String json, String overviewResponseTimeJson) {
		VerticalPanel overviewChartPanel = new VerticalPanel("overview_chart", "40, 310");
		HtmlPanel overviewCharTitle = new HtmlPanel("f_title", "监控项概述");
		overviewCharTitle.setStyleClass("bg-low");
		overviewCharTitle.setStyle("margin-top:6px;padding-left:6px;font-size:12pt;color:black");
		StringBuilder str = new StringBuilder();
		str.append("<div id='overviewChart' style='width:48%;float: left;'></div>");
		str.append("<script type='text/javascript'>");
		str.append("buildChart('overviewChart'," + json + ");");
		str.append("</script>");
		str.append("<div id='overviewResponseTimeChart' style='width:48%;float:right'></div>");
		str.append("<script type='text/javascript'>");
		str.append("buildChart('overviewResponseTimeChart'," + overviewResponseTimeJson + ");");
		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html", str.toString());
		overviewChartPanel.addSubPanel(overviewCharTitle, htmlPanel);
		return overviewChartPanel;
	}
	
	public static Panel buildOverviewHttpPanel(List<Object[]> listTaskResult) {
		VerticalPanel avaliablePanel = new VerticalPanel("overview_http", "50, *");
		HtmlPanel title = new HtmlPanel(null, "网站监控状态");
		title.setStyleClass("bg-low  bg-white");
		title.setStyle("margin-top:6px;padding-left:6px;font-size:12pt;color:black");
		avaliablePanel.addSubPanel(title);
		if(Utils.notEmpty(listTaskResult)) {
			GridPanel grid = new GridPanel("http_status");
			grid.setScroll(Scroll.hidden);
			grid.pub().setHeaderClass("bg-mid");
			grid.setFace(GridPanelPubInfo.FACE_LINE);
			grid.setRowHeight(40);
			grid.setFilter(false);
			// grid.setHighlightStyle(HighlightStyle.always); // 设置点击某行总是高亮显示
			grid.setData(listTaskResult);
			grid.addHiddenColumn(0, "taskId");
			grid.addTextColumn(1, "name", "监控服务名称", "*", null);
			grid.addTextColumn(2, "date", "监控时间", "*", null);
			grid.addTextColumn(3, "type", "类型", "*", null);
			grid.addTextColumn(6, "time", "响应时间", "*", null);
			grid.addTextColumn(4, "status", "状态", "*", null);
			avaliablePanel.addSubPanel(grid);
		}else {
			avaliablePanel.addSubPanel(new HtmlPanel(null,
					"<center>未添加网络监控服务</center><br><br>"));
		}
		return avaliablePanel;
	}
	
	public static Panel buildOverviewSnmpPanel(List<Object[]> dataList) {
		VerticalPanel snmpPanel = new VerticalPanel("snmpPanel", "50, *");
		HtmlPanel snmpTitlePanel = new HtmlPanel("snmpTitlePanel", "服务器状态");
		snmpTitlePanel.setStyleClass("bg-low");
		snmpTitlePanel.setStyle("margin-top:12px;padding-left:6px;font-size:12pt;color:black");

		if (dataList != null && dataList.size() > 0) {
			GridPanel grid = new GridPanel("f_f_grid1");
			grid.setScroll(Scroll.hidden);
			grid.setData(dataList);
			grid.pub().setHeaderClass("bg-mid");
			grid.setFace(GridPanelPubInfo.FACE_LINE);
			grid.setRowHeight(40);
			grid.setFilter(false);
			// grid.setHighlightStyle(HighlightStyle.always); // 设置点击某行总是高亮显示
			grid.setData(dataList);
			grid.addTextColumn(0, "name", "监控服务名称", "*", null);
			grid.addTextColumn(1, "ip", "服务器IP", "*", null);
//			grid.addTextColumn(2, "time", "运行时间", "*", null);
			grid.addTextColumn(3, "cpu", "cpu使用率（%）", "*", null);
			grid.addTextColumn(4, "memory", "内存使用率（%）", "*", null);
			grid.addTextColumn(5, "jvmMemory", "JVM使用率（%）", "*", null);
			grid.addTextColumn(6, "jvmThread", "JVM线程数", "*", null);
			grid.addTextColumn(7, "ifOctets", "网络流量（读|写）", "*", null);
			grid.addTextColumn(8, "io", "磁盘IO（读|写）", "*", null);
			grid.addTextColumn(9, "memRate", "磁盘使用率", "*", null);
			grid.addTextColumn(10, "systemThread", "系统进程数", "*", null);
			snmpPanel.addSubPanel(snmpTitlePanel, grid);
		} else {
			snmpPanel.addSubPanel(snmpTitlePanel, new HtmlPanel(null,
					"<center>未添加服务器统计</center><br><br>"));
		}
		return snmpPanel;
	}
	
	public static Panel buildOverviewErrorPanel(List<Object[]> listFaultHistory) {
		VerticalPanel errorListPanel = new VerticalPanel("errorListPanel",
				"50,*");
		HtmlPanel errorListTitle = new HtmlPanel(null, "故障事件");
		errorListTitle.setStyleClass("bg-low");
		errorListTitle.setStyle("margin-top:12px;padding-left:6px;font-size:12pt;color:black");
		errorListPanel.addSubPanel(errorListTitle);
		if (Utils.notEmpty(listFaultHistory)) {
			errorListPanel.addSubPanel(fillFaultHistoryGrid(listFaultHistory));
		} else {
			errorListPanel.addSubPanel(new HtmlPanel(null,
					"<center>该时间范围内不存在故障！</center><br><br>"));
		}
		return errorListPanel;
	}

	public static Panel buildOverView(Locale loc, ViewFactory viewFactory,
			QueryCondition condition, String json, String overviewResponseTimeJson,
			List<Object[]> dataList, List<Object[]> listTaskResult, List<Object[]> listFaultHistory, boolean isTimer, String projectId) {
		VerticalPanel root = new VerticalPanel("root2", "*");
		root.setStyleClass("overviewView");

		FlowPanel fp = new FlowPanel("ovewView_fp");
		fp.setScroll(Scroll.miniscroll);
		
		// 添加可用率统计图表-------------start
		VerticalPanel overviewChartPanel = buildOverviewChart(json, overviewResponseTimeJson);
		// 添加可用率统计图表-------------end
		
		//添加网站监控状态 --------start
		Panel avaliablePanel = buildOverviewHttpPanel(listTaskResult);
		//添加网站监控状态 --------end
		
		

		// 添加snmp统计--------------start
		Panel snmpPanel = buildOverviewSnmpPanel(dataList);
		// 添加snmp统计--------------end
		
		//添加故障统计------------start
		Panel errorListPanel = buildOverviewErrorPanel(listFaultHistory);
		//添加故障统计-------------end
		fp.addSubPanel(overviewChartPanel,avaliablePanel, snmpPanel, errorListPanel);
		root.addSubPanel(fp);
		return root;
	}

	public static TabPanel buildHttpPanel(QueryCondition condition, String[] pointId) {
		// 添加各个监控点的可用率统计--------start
		TabPanel tabPanel = new TabPanel("httptab");
		tabPanel.addSubPanel(
				null,
				"平均",
				new SourcePanel("source1",
						"vm:|clientMonitor.sp?act=showAvailable&isIgnoreMpId&id="
								+ condition.getTaskId()
								+ condition.getUrlSearchConditionParm()), null);
		if (pointId != null && pointId.length > 0) {
			MonitoringPoint monitoringPoint = null;
			for (String mpId : pointId) {
				monitoringPoint = ServiceLocator.getMonitoringPointService()
						.getMonitoringPoint(mpId);
				if (monitoringPoint != null) {
					tabPanel.addSubPanel(
							null,
							monitoringPoint.getMpName(),
							new SourcePanel(
									"source" + monitoringPoint.getMpId(),
									"vm:|clientMonitor.sp?act=showAvailable&id="
											+ condition.getTaskId()
											+ "&mpId="
											+ mpId
											+ condition
													.getUrlSearchConditionParm()),
							null);
				}

			}
		}
		// 添加各个监控点的可用率统计--------end
		return tabPanel;
	}
	
	public static FormPanel buildHttpTimePanel(QueryCondition condition, String subprojectId) {
		// 添加时间面板--------------start
		if (condition == null || condition.getStartTime() == null) {
			condition.setStartTime(new Date());
			condition.setEndTime(new Date());
		}
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg", "时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
		EmbededButton button = new EmbededButton("确定",
				"VM(this).cmd('availableQuery');");
		hg.add(button);
		formPanel.addHidden("subprojectId", subprojectId);
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);
		// 添加时间面板--------------end
		return formPanel;
	}
	
	public static Panel buildAvailablePanel(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String[] pointId,List<Subproject> listSubproject) {
		VerticalPanel root = new VerticalPanel("root", "60,60,*");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='mod_tab' >")
			.append("<div class='tab clearfix'><ul>");
		String subprojectId = "";
		if(Utils.notEmpty(listSubproject)) {
			for(int i=0; i<listSubproject.size(); i++) {
				Subproject sp = listSubproject.get(i);
				if(i == 0) {
					subprojectId = sp.getId();
					sb.append("<li class='on' onclick=\"jQuery('.mod_tab li').removeClass('on'); jQuery(this).addClass('on');VM(this).cmd('changePoint','"+sp.getId()+"')\">").append(sp.getName()).append("</li>");
				}else if(i==(listSubproject.size()-1)) {
					sb.append("<li class='nb' onclick=\"jQuery('.mod_tab li').removeClass('on'); jQuery(this).addClass('on');VM(this).cmd('changePoint','"+sp.getId()+"')\">").append(sp.getName()).append("</li>");
				}else {
					sb.append("<li onclick=\"jQuery('.mod_tab li').removeClass('on'); jQuery(this).addClass('on');VM(this).cmd('changePoint','"+sp.getId()+"')\">").append(sp.getName()).append("</li>");
				}
			}
		}
		sb.append("</ul></div></div>");
		root.addSubPanel(new HtmlPanel(null,sb.toString()));
		root.addSubPanel(buildHttpTimePanel(condition, subprojectId), buildHttpPanel(condition, pointId));
		return root;
	}

	public static Panel buildAvailableSubPanel(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String json,
			String[] pointId, Map<String, String> map, List<FaultHistory> list,
			Object[] resMap, String jsonData, String jsonDate2) {
//		VerticalPanel root = new VerticalPanel("root", "350,*");
		FlowPanel fp = new FlowPanel("root");
		fp.setScroll(Scroll.miniscroll);
		// 响应时间
		StringBuffer bf = new StringBuffer();
		bf.append("<div style='margin:10px auto; '><div class='tpanner'>")
				.append("<table border='0' cellspacing='0' cellpadding='0'><tr><td><p class=''><strong>最小响应时间</strong></p>")
				.append("<p class='font-color-lightblue'>" + (resMap[0]==null?0:resMap[0])
						+ " ms</p>")
				.append("</td><td>&nbsp;&nbsp;</td><td> <p class=''><strong>平均响应时间</strong></p>")
				.append("<p class='font-color-red'>" + (resMap[2]==null?0:resMap[2]) + " ms</p>")
				.append("</td> <td>&nbsp;&nbsp;</td><td><p class=''><strong>最大响应时间</strong></p>")
				.append("<p class='font-color-red'>" + (resMap[1]==null?0:resMap[1])
						+ " ms</p></td></tr></table> </div></div>");

		fp.addSubPanel(new HtmlPanel(null, bf.toString()));
		// 添加可用率图表---------start
		VerticalPanel echartPanel = new VerticalPanel("echart", "30, *");
		HtmlPanel title = new HtmlPanel("f_title", "可用率统计");
		title.setStyleClass("bg-low");
		title.setStyle("padding-left:6px;font-size:10pt;color:black");

		StringBuilder str = new StringBuilder();
		str.append("<div id='chartdiv_"+condition.getTaskId()+"_"
				+ (condition.getMpId() == null ? "" : condition.getMpId())
				+ "'></div>");
		str.append("<script type='text/javascript'>");
		str.append("showFusionLines('" + json + "','chartdiv_"+condition.getTaskId()+"_"
				+ (condition.getMpId() == null ? "" : condition.getMpId())
				+ "');");
		str.append("</script>");
		HorizontalPanel horizontalPanel = new HorizontalPanel("hpanel", "20%,*");
		if (map != null && map.size() > 0) {
			StringBuilder str2 = null;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				str2 = new StringBuilder();
				str2.append("<div id='chartdiv" + condition.getTaskId() + "_"+entry.getKey() + "'></div>");
				str2.append("<script type='text/javascript'>");
				str2.append("showFusionCharts2('" + condition.getTaskId() + "_"+entry.getKey() + "','"
						+ entry.getValue() + "','" + 1 + "');");
				str2.append("</script>");
				HtmlPanel htmlPanel2 = new HtmlPanel("html" + entry.getKey(),
						str2.toString());
				horizontalPanel.addSubPanel(htmlPanel2);
				break;
			}

		} else {
			horizontalPanel.addSubPanel(new HtmlPanel("empty_2", "该段时间没有监控数据"));
		}
		horizontalPanel.addSubPanel(new HtmlPanel("html", str.toString()));
		echartPanel.addSubPanel(title, horizontalPanel);
		fp.addSubPanel(echartPanel);

		// 添加平均响应时间图表---------start
		VerticalPanel avgResPanel = new VerticalPanel("avgResEchart", "30, *");
		HtmlPanel avgRestitle = new HtmlPanel("f_title2", "平均响应时间统计");
		avgRestitle.setStyleClass("bg-low");
		avgRestitle.setStyle("padding-left:6px;font-size:10pt;color:black");
		StringBuilder avgResStr = new StringBuilder();
		avgResStr.append("<div id='chartdivs_"+condition.getTaskId()+"_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "'></div>");
		avgResStr.append("<script type='text/javascript'>");
		avgResStr.append("showFusionZoomLine('"
				+ jsonData
				+ "','chartdivs_"+condition.getTaskId()+"_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "');");
		avgResStr.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("avgResHtml", avgResStr.toString());
		avgResPanel.addSubPanel(avgRestitle, htmlPanel);
		fp.addSubPanel(avgResPanel);
		// 添加平均响应时间图表---------end

		// 添加响应时间详细图表---------start
		if(Utils.notEmpty(jsonDate2)) {
			VerticalPanel echartPanel2 = new VerticalPanel("echart2", "30, *");
			HtmlPanel detialT = new HtmlPanel("f_title3", "响应时间详细统计");
			detialT.setStyleClass("bg-low");
			detialT.setStyle("padding-left:6px;font-size:10pt;color:black");
			StringBuilder str1 = new StringBuilder();
			str1.append("<div id='chartdivs2_"+condition.getTaskId()+"_"
					+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
							: "") + "'></div>");
			str1.append("<script type='text/javascript'>");
			str1.append("showFusionZoomLine2('"
					+ jsonDate2
					+ "','chartdivs2_"+condition.getTaskId()+"_"
					+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
							: "") + "');");
			str1.append("</script>");
			HtmlPanel htmlPanel1 = new HtmlPanel("html_detial", str1.toString());
			echartPanel2.addSubPanel(detialT, htmlPanel1);
			fp.addSubPanel(echartPanel2);
		}
		// 添加响应时间详细图表---------end

		VerticalPanel errorListPanel = new VerticalPanel("errorListPanel",
				"30,*");
		HtmlPanel errorListTitle = new HtmlPanel(null, "故障时间统计");
		errorListTitle.setStyleClass("bg-low");
		errorListTitle.setStyle("padding-left:6px;font-size:10pt;color:black");
		errorListPanel.addSubPanel(errorListTitle);
		if (Utils.notEmpty(list)) {
			errorListPanel.addSubPanel(fillFauleGrid(list));
		} else {
			errorListPanel.addSubPanel(new HtmlPanel(null,
					"<center>该时间范围内不存在故障时间统计！</center><br><br>"));
		}
		fp.addSubPanel(errorListPanel);
//		FlowPanel flowPanel = new FlowPanel("fp");
//		flowPanel.addSubPanel(errorListPanel);
//		root.addSubPanel(echartPanel, errorListPanel);
		return fp;
	}

	public static Panel buildResponsePanel(Locale loc, ViewFactory viewFactory,
			QueryCondition condition, String[] pointId) {

		VerticalPanel root = new VerticalPanel("root", "60,*");
		// 添加时间面板--------------start
		if (condition == null || condition.getStartTime() == null) {
			condition.setStartTime(new Date());
			condition.setEndTime(new Date());
		}
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg", "时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
		EmbededButton button = new EmbededButton("确定",
				"VM(this).cmd('responseQuery');");
		hg.add(button);
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);
		// 添加时间面板--------------end

		// 添加各个监控点的可用率统计--------start
		TabPanel tabPanel = new TabPanel(null);
		tabPanel.addSubPanel(
				null,
				"平均",
				new SourcePanel("source2",
						"vm:|clientMonitor.sp?act=showResponse&isIgnoreMpId&id="
								+ condition.getTaskId()
								+ condition.getUrlSearchConditionParm()), null);
		if (pointId != null && pointId.length > 0) {
			MonitoringPoint monitoringPoint = null;
			for (String mpId : pointId) {
				monitoringPoint = ServiceLocator.getMonitoringPointService()
						.getMonitoringPoint(mpId);
				if (monitoringPoint != null) {
					tabPanel.addSubPanel(
							null,
							monitoringPoint.getMpName(),
							new SourcePanel(
									"source" + monitoringPoint.getMpId(),
									"vm:|clientMonitor.sp?act=showResponse&id="
											+ condition.getTaskId()
											+ "&mpId="
											+ mpId
											+ condition
													.getUrlSearchConditionParm()),
							null);
				}

			}
		}
		// 添加各个监控点的可用率统计--------end

		root.addSubPanel(formPanel, tabPanel);
		return root;
	}

	public static Panel buildResponseSubPanel(Locale loc,
			ViewFactory viewFactory, QueryCondition condition,
			String[] pointId, Object[] map, String jsonData, String jsonDate2) {
		FlowPanel vpanel = new FlowPanel("overViewPanel");
		vpanel.setScroll(Scroll.miniscroll);

		StringBuffer bf = new StringBuffer();
		bf.append("<div style='margin:10px auto; '><div class='tpanner'>")
				.append("<table border='0' cellspacing='0' cellpadding='0'><tr><td><p class=''><strong>最小响应时间</strong></p>")
				.append("<p class='font-color-lightblue'>" + map[0] + " ms</p>")
				.append("</td><td>&nbsp;&nbsp;</td><td> <p class=''><strong>平均响应时间</strong></p>")
				.append("<p class='font-color-red'>" + map[2] + " ms</p>")
				.append("</td> <td>&nbsp;&nbsp;</td><td><p class=''><strong>最大响应时间</strong></p>")
				.append("<p class='font-color-red'>" + map[1]
						+ " ms</p></td></tr></table> </div></div>");

		// 添加平均响应时间图表---------start
		VerticalPanel echartPanel = new VerticalPanel("echart", "30, *");
		HtmlPanel title = new HtmlPanel("f_title2", "平均响应时间统计");
		title.setStyleClass("bg-low");
		title.setStyle("padding-left:6px;font-size:10pt;color:black");
		StringBuilder str = new StringBuilder();
		str.append("<div id='chartdivs_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "'></div>");
		str.append("<script type='text/javascript'>");
		str.append("showFusionZoomLine('"
				+ jsonData
				+ "','chartdivs_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "');");
		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html", str.toString());
		echartPanel.addSubPanel(title, htmlPanel);
		// 添加平均响应时间图表---------end

		// 添加响应时间详细图表---------start
		VerticalPanel echartPanel2 = new VerticalPanel("echart2", "30, *");
		HtmlPanel detialT = new HtmlPanel("f_title", "响应时间详细统计");
		detialT.setStyleClass("bg-low");
		detialT.setStyle("padding-left:6px;font-size:10pt;color:black");
		StringBuilder str1 = new StringBuilder();
		str1.append("<div id='chartdivs2_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "'></div>");
		str1.append("<script type='text/javascript'>");
		str1.append("showFusionZoomLine2('"
				+ jsonDate2
				+ "','chartdivs2_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "');");
		str1.append("</script>");
		HtmlPanel htmlPanel1 = new HtmlPanel("html_detial", str1.toString());
		echartPanel2.addSubPanel(detialT, htmlPanel1);
		// 添加响应时间详细图表---------end
		vpanel.addSubPanel(new HtmlPanel(null, bf.toString()), echartPanel,
				echartPanel2);
		return vpanel;
	}

	public static Panel buildSnmpStatisticsPanel(QueryCondition condition,
			String chartsMapStr, String jvmThreadMapStr, String ioMapStr, List<Subproject> listSubproject) {
		VerticalPanel root = new VerticalPanel("root", "60,60,*");
		root.setStyleClass("snmpView");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='mod_tab' >")
			.append("<div class='tab clearfix'><ul>");
		String subprojectId = "";
		if(Utils.notEmpty(listSubproject)) {
			for(int i=0; i<listSubproject.size(); i++) {
				Subproject sp = listSubproject.get(i);
				if(i == 0) {
					subprojectId = sp.getId();
					sb.append("<li class='on' onclick=\"jQuery('.mod_tab li').removeClass('on'); jQuery(this).addClass('on');VM(this).cmd('changePoint','"+sp.getId()+"')\">").append(sp.getName()).append("</li>");
				}else if(i==(listSubproject.size()-1)){
					sb.append("<li class='nb' onclick=\"jQuery('.mod_tab li').removeClass('on'); jQuery(this).addClass('on');VM(this).cmd('changePoint','"+sp.getId()+"')\">").append(sp.getName()).append("</li>");
				}else {
					sb.append("<li onclick=\"jQuery('.mod_tab li').removeClass('on'); jQuery(this).addClass('on');VM(this).cmd('changePoint','"+sp.getId()+"')\">").append(sp.getName()).append("</li>");
				}
			}
		}
		sb.append("</ul></div></div>");
		root.addSubPanel(new HtmlPanel(null,sb.toString()));
		// 添加时间面板--------------start
		if (condition == null || condition.getStartTime() == null) {
			condition.setStartTime(new Date());
			condition.setEndTime(new Date());
		}
		Panel formPanel = buildSelectTimePanel(condition, false, subprojectId);
		// 添加时间面板--------------end

		root.addSubPanel(
				formPanel,
				buildSnmpStatisticsPanel(chartsMapStr, jvmThreadMapStr,
						ioMapStr));
		return root;
	}

	public static Panel buildSelectTimePanel(QueryCondition condition,
			boolean isTimer, String subprojectId) {
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg", "时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
		EmbededButton button = new EmbededButton("确定",
				"VM(this).cmd('snmpQuery');");
		hg.add(button);
		EmbededButton button2 = null;
		if (isTimer) {
			button2 = new EmbededButton("关闭定时刷新图表",
					"VM(this).cmd('refreshPanel','open','"+subprojectId+"');");
		} else {
			button2 = new EmbededButton("打开定时刷新图表",
					"VM(this).cmd('refreshPanel','close','"+subprojectId+"');");
		}
		button2.setName("open");
		hg.add(button2);
		formPanel.add(hg);
		formPanel.addHidden("subprojectId", subprojectId);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);
		return formPanel;
	}

	public static Panel buildSnmpStatisticsPanel(String rateStatisticsStr,
			String jvmThreadMapStr, String ioMapStr) {
		FlowPanel panel = new FlowPanel("snmpStatistics");
		panel.setScroll(Scroll.miniscroll);
		// 添加系统使用率统计图表---------start
		VerticalPanel chartRatePanel = new VerticalPanel("chartRate", "30, *");
		HtmlPanel title = new HtmlPanel("chartRate_title2", "系统使用率统计");
		title.setStyleClass("bg-low");
		title.setStyle("padding-left:6px;font-size:10pt;color:black");
		StringBuilder str = new StringBuilder();
		str.append("<div id='chartdivs_rate'></div>");
		str.append("<script type='text/javascript'>");
		// str.append("showFusionZoomLine('" + rateStatisticsStr
		// + "','chartdivs_rate');");
		str.append("initChart('chartdivs_rate'," + rateStatisticsStr + ");");
		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("chartRatehtml", str.toString());
		chartRatePanel.addSubPanel(title, htmlPanel);
		// 添加系统使用率统计图表---------end

		// 添加jvm内存使用统计图表---------start
		VerticalPanel jvmThreadPanel = new VerticalPanel("jvmThread", "30, *");
		HtmlPanel jvmThreadTitle = new HtmlPanel("jvmThread_title2", "线程数统计");
		jvmThreadTitle.setStyleClass("bg-low");
		jvmThreadTitle.setStyle("padding-left:6px;font-size:10pt;color:black");
		StringBuilder jvmThread = new StringBuilder();
		jvmThread.append("<div id='chartdivs_jvmThread'></div>");
		jvmThread.append("<script type='text/javascript'>");
		jvmThread.append("jvmThreadChart('chartdivs_jvmThread',"
				+ jvmThreadMapStr + ");");
		jvmThread.append("</script>");
		HtmlPanel jvmThreadhtmlPanel = new HtmlPanel("jvmThreadhtml",
				jvmThread.toString());
		jvmThreadPanel.addSubPanel(jvmThreadTitle, jvmThreadhtmlPanel);
		// 添加jvm内存使用统计图表---------end

		// 添加jvm内存使用统计图表---------start
		VerticalPanel ioPanel = new VerticalPanel("io", "30, *");
		HtmlPanel ioTitle = new HtmlPanel("io_title2", "流量统计");
		ioTitle.setStyleClass("bg-low");
		ioTitle.setStyle("padding-left:6px;font-size:10pt;color:black");
		StringBuilder ioThread = new StringBuilder();
		ioThread.append("<div id='chartdivs_io'></div>");
		ioThread.append("<script type='text/javascript'>");
		ioThread.append("ioChart('chartdivs_io'," + ioMapStr + ");");
		ioThread.append("</script>");
		HtmlPanel iohtmlPanel = new HtmlPanel("iohtml", ioThread.toString());
		ioPanel.addSubPanel(ioTitle, iohtmlPanel);
		// 添加jvm内存使用统计图表---------end
		panel.addSubPanel(chartRatePanel, jvmThreadPanel, ioPanel);
		return panel;
	}

	public static Panel createChartPnale() {
		HighChart hc = new HighChart();
		Chart chart = new Chart();
		chart.setType("spline");
		chart.setMarginRight(10);
		Events events = new Events();
		// String load = "function() { var series = this.series[0]; "+
		// "setInterval(function() {  "+
		// "   jQuery.get('clientMonitor.sp?act=cycleRefreshCharts2', function(data){ "+
		// "  	console.log(data);}) "+
		// " series.addPoint([x, y], true, true);       "+
		// " }, 2000);}  ";
		String load = "function(){setInterval(function(){console.log('d')},2000)}";
		events.setLoad(load);
		chart.setEvents(events);
		hc.setChart(chart);

		Title title = new Title();
		title.setText("Live random data");
		hc.setTitle(title);

		Axis xAxis = new Axis();
		xAxis.setType("datetime");
		xAxis.setTickPixelInterval(150);
		Object[] data = new Object[10];
		for (int i = 0; i < 10; i++) {
			data[i] = i + 5;
		}
		xAxis.setCategories(data);
		hc.setXAxis(xAxis);

		Axis yAxis = new Axis();
		Title yAxistitle = new Title();
		yAxistitle.setText("Value");
		yAxis.setTitle(yAxistitle);
		hc.setYAxis(yAxis);

		Tooltip tooltip = new Tooltip();
		String formatter = " function() {return '<b>'+ this.series.name +'</b><br/>'"
				+ "Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'"
				+ "Highcharts.numberFormat(this.y, 2)} ";
		// tooltip.setFormatter(formatter);
		hc.setTooltip(tooltip);

		Legend legend = new Legend();
		legend.setEnabled(true);
		hc.setLegend(legend);

		Exporting exporting = new Exporting();
		exporting.setEnabled(true);
		hc.setExporting(exporting);

		Series series = new Series();
		series.setName("Random data");
		// List<Integer> listData = new ArrayList<Integer>();
		Object[] datas = new Object[10];
		for (int i = 0; i < 10; i++) {
			datas[i] = i + 7;
		}
		series.setData(datas);
		hc.setSeries(new Series[] { series });

		HighChartPlugin plugin = new HighChartPlugin("test1", hc);
		return plugin;
	}
}
