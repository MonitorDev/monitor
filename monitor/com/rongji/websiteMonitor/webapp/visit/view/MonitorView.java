package com.rongji.websiteMonitor.webapp.visit.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rongji.dfish.engines.xmltmpl.BaseView;
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
import com.rongji.dfish.engines.xmltmpl.form.Radio;
import com.rongji.dfish.engines.xmltmpl.form.Radiogroup;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.dfish.framework.view.ViewTemplate;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class MonitorView {

	public static BaseView buildMain(Locale loc, ViewFactory viewFactory,
			QueryCondition condition, String json, List<Object[]> listMonitorStatus) {
		
		
		BaseView view = new BaseView();
		
		VerticalPanel layout_bg = new VerticalPanel("layout","*");
		layout_bg.setStyleClass("ly-bg");
		view.setRootPanel(layout_bg);
		
		VerticalPanel layout_top = new VerticalPanel("ly_top","*");
		layout_top.setStyleClass("ly-top");
		layout_bg.addSubPanel(layout_top);
		
		VerticalPanel root=new VerticalPanel("root","26,*");
		root.setStyleClass("ly-bottom");
		layout_top.addSubPanel(root);

		HorizontalPanel top=new HorizontalPanel(null,"*,240");//
		root.addSubPanel(top);
		
		ButtonBarPanel mainOper=new ButtonBarPanel("main_oper");
		mainOper.setBindFilmId("main_film");
		top.addSubPanel(mainOper);
		
		ClickButton overview = new ClickButton("", "概述", "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showOverView' })");
		overview.setId("overview");
		overview.setFocus(true);
		mainOper.addButton(overview);
		
		ClickButton available = new ClickButton("", "可用率", "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showAvailable' });VM(this).find('main_oper').focus(1);");
		available.setId("available");
		mainOper.addButton(available);
		
		ClickButton responseTime = new ClickButton("", "响应时间", "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showResponse' });VM(this).find('main_oper').focus(2);");
		responseTime.setId("responseTime");
		mainOper.addButton(responseTime);
		
		ClickButton snmp = new ClickButton("", "snmp", "VM(this).find('main_oper').focus(3);");
		snmp.setId("snmp");
		mainOper.addButton(snmp);
		
		HtmlPanel hp = new HtmlPanel("hp","<h>minhou</h>", "bd-deep",  
		        null, 2, 2);  
		ButtonBarPanel namePanel=new ButtonBarPanel("namePanel");
		namePanel.setBindFilmId("main_film");
		//namePanel.setButtonMaxWidth(134);
		ClickButton name = new ClickButton("", "闽侯撒旦法", null);
		namePanel.addButton(name);
		top.addSubPanel(namePanel);
		
		view.add(new JSCommand("onQuery","VM( this ).ext( 'startTime', VM(this).fv('startTime'));" +
				"VM( this ).ext( 'endTime', VM(this).fv('endTime'));" +
				"VM(this).ext('sel',VM(this).fv('sel'));"+
				"VM(this).cmd('query',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ))"));
		view.add(new JSCommand("query",
		"VM(this).cmd({tagName:'ajax',src:'monitor.sp?act=showOverView&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&taskId="+condition.getTaskId()+"'});"));
		
		view.add(new JSCommand("availableQuery","VM( this ).ext( 'startTime', VM(this).fv('startTime'));" +
				"VM( this ).ext( 'endTime', VM(this).fv('endTime'));" +
				"VM(this).ext('sel',VM(this).fv('sel'));"+
				"VM(this).cmd('availablequery',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ))"));
		view.add(new JSCommand("availablequery",
		"VM(this).cmd({tagName:'ajax',src:'monitor.sp?act=showAvailable&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&taskId="+condition.getTaskId()+"'});"));
		
		view.add(new JSCommand("responseQuery","VM( this ).ext( 'startTime', VM(this).fv('startTime'));" +
				"VM( this ).ext( 'endTime', VM(this).fv('endTime'));" +
				"VM(this).ext('sel',VM(this).fv('sel'));"+
				"VM(this).cmd('responsequery',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ))"));
		view.add(new JSCommand("responsequery",
		"VM(this).cmd({tagName:'ajax',src:'monitor.sp?act=showResponse&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&taskId="+condition.getTaskId()+"'});"));
		
		JSParser converObjectParser = new JSParser(
				"converObject",
				"var objId = xml.getAttribute('op');var isIgnore = xml.getAttribute('isIgnore');"
				+"if(isIgnore!=null&&isIgnore==1){"
						+ "return '"
						+ "<span><a href=\"javascript:void(0)\" onclick=VM(this).cmd(\"ignore\",\"'+objId+'\",\""+(Utils.notEmpty(condition.getMpId())?condition.getMpId():"")+"\");>恢复</a></span>';" +
								"}else{" 
								+ "return '"
								+ "<span><a href=\"javascript:void(0)\" onclick=VM(this).cmd(\"ignore\",\"'+objId+'\",\""+(Utils.notEmpty(condition.getMpId())?condition.getMpId():"")+"\");>忽略</a></span>';" +
								"}");
		

		view.addParser(converObjectParser);
		view.add(new AjaxCommand("ignore","monitor.sp?act=ignoreFault&fhId=$0&mpId=$1") );
		
		Panel overView = buildOverViewPanel(loc, viewFactory, condition, json, listMonitorStatus);
		root.addSubPanel(overView);
		return view;
	}
	
	public static Panel buildOverViewPanel(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String json, List<Object[]> listMonitorStatus) {
		if(condition == null || condition.getStartTime() == null) {
			condition.setStartTime(new Date());
			condition.setEndTime(new Date());
		}
		VerticalPanel vp = new VerticalPanel("overViewPanel","48,*");
		//时间面板
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg","时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
//		String[][] yesOrno = new String[][] { { "0", "小时" },  
//		        { "1", "天" } };  
//		Radio radio = new Radio("type", "type", "0", Arrays.asList(yesOrno));  
//		hg.add(radio);
		EmbededButton button = new EmbededButton("确定","VM(this).cmd('onQuery');");
		hg.add(button);
		
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);
		vp.addSubPanel(formPanel);
		
		FlowPanel flowPanel = new FlowPanel("fp");
		flowPanel.setScroll(Scroll.auto);
		vp.addSubPanel(flowPanel);
		HtmlPanel t = new HtmlPanel("title","可用率统计");
		t.setStyleClass("tt3");
		flowPanel.addSubPanel(t);
		StringBuilder str = new StringBuilder();
 		str.append("<div id='chartdiv_"+condition.getMpId()+"'></div>");
 		str.append("<script type='text/javascript'>");
 		str.append("showFusionLines('"+json+"','chartdiv_"+condition.getMpId()+"');");
 		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html",str.toString());
		HorizontalPanel horizontalPanel = new HorizontalPanel("hpanel","*");
//		HorizontalPanel horizontalPanel = new HorizontalPanel("hpanel","20%,*");
//		if(map!=null&&map.size()>0){
//			StringBuilder str2 = null;
//			for(Map.Entry<String, String> entry:map.entrySet()){
//				str2 = new StringBuilder();
//				str2.append("<div id='chartdiv"+entry.getKey()+"'></div>");
//		 		str2.append("<script type='text/javascript'>");
//		 		str2.append("showFusionCharts2('"+entry.getKey()+"','"+entry.getValue()+"','"+1+"');");
//		 		str2.append("</script>");
//				HtmlPanel htmlPanel2 = new HtmlPanel("html"+entry.getKey(),str2.toString());
//				horizontalPanel.addSubPanel(htmlPanel2);
//				break;
//			}
//	
//		}else{
//			horizontalPanel.addSubPanel(new HtmlPanel("empty_2","该段时间没有监控数据"));
//		}
		horizontalPanel.addSubPanel(htmlPanel);
		flowPanel.addSubPanel(horizontalPanel);
		
		HtmlPanel monitorStatus = new HtmlPanel("monitorStatus","监测点当前状态");
		monitorStatus.setStyleClass("tt3");
		flowPanel.addSubPanel(monitorStatus);
		
		StringBuffer html = new StringBuffer();
		html.append("<div style='margin:20px auto; '> <ul class=\"panner clearfix\">");
		for(Object[] obj : listMonitorStatus) {
			html.append("<li class=\"first_child\">")
			    .append("<p>" + obj[3] +"</p>")
			    .append("<p>" + obj[4] + "<p> ")
			    .append("<p> 200  OK<p> </li>");
		}
		html.append("</ul></div>");
		HtmlPanel monitorStatus2 = new HtmlPanel("monitorStatus2",html.toString());
		flowPanel.addSubPanel(monitorStatus2);
		return vp;
	}
	
	public static Panel buildAvailablePanel(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String json, String[] pointId, Map<String, String> map, List<FaultHistory> list ) {
		if(condition == null || condition.getStartTime() == null) {
			condition.setStartTime(new Date());
			condition.setEndTime(new Date());
		}
		VerticalPanel vp = new VerticalPanel("overViewPanel","48,32,*");
		//时间面板
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg","时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
//		String[][] yesOrno = new String[][] { { "0", "小时" },  
//		        { "1", "天" } };  
//		Radio radio = new Radio("type", "type", "0", Arrays.asList(yesOrno));  
//		hg.add(radio);
		EmbededButton button = new EmbededButton("确定","VM(this).cmd('availableQuery');");
		hg.add(button);
		
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);
		vp.addSubPanel(formPanel);
		
		TabPanel tabPanel = new TabPanel("tab");
//		tabPanel.addSubPanel("avagTab", "平均", new SourcePanel("source1","vm:|monitorDetailMgr.sp?act=showDetialBytab&taskId="+condition.getTaskId()+condition.getUrlSearchConditionParm()), null);
//		vp.addSubPanel(tabPanel);
//		if(pointId!=null&&pointId.length>0){
//			MonitoringPoint monitoringPoint = null;
//			for(String mpId:pointId){
//				monitoringPoint = ServiceLocator.getMonitoringPointService().getMonitoringPoint(mpId);
//				if(monitoringPoint!=null){
//					tabPanel.addSubPanel(monitoringPoint.getMpId()+"Tab", monitoringPoint.getMpName(), new SourcePanel("source"+monitoringPoint.getMpId(),"vm:|monitor.sp?act=showAvailable&taskId="+condition.getTaskId()+"&mpId="+mpId+condition.getUrlSearchConditionParm()), null);					
//				}
//				
//			}
//		}
		ButtonBarPanel mainOper=new ButtonBarPanel("main_oper3");
		mainOper.setBindFilmId("main_film");
		vp.addSubPanel(mainOper);
		
		ClickButton overview = new ClickButton("", "平均", "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showAvailable&isIgnoreMpId=true&taskId="+condition.getTaskId()+condition.getUrlSearchConditionParm()+"'})");
		overview.setId("round");
		mainOper.addButton(overview);
		if(pointId!=null&&pointId.length>0){
			MonitoringPoint monitoringPoint = null;
			for(String mpId:pointId){
				monitoringPoint = ServiceLocator.getMonitoringPointService().getMonitoringPoint(mpId);
				if(monitoringPoint!=null){
					//tabPanel.addSubPanel(monitoringPoint.getMpId()+"Tab", monitoringPoint.getMpName(), new SourcePanel("source"+monitoringPoint.getMpId(),"vm:|monitor.sp?act=showAvailable&taskId="+condition.getTaskId()+"&mpId="+mpId+condition.getUrlSearchConditionParm()), null);					
					ClickButton cb = new ClickButton("", monitoringPoint.getMpName(), "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showAvailable&taskId="+condition.getTaskId()+"&mpId="+mpId+condition.getUrlSearchConditionParm()+"'})");
					cb.setId(monitoringPoint.getMpId()+"Tab");
					mainOper.addButton(cb);
				}
				
			}
		}
		
		FlowPanel flowPanel = new FlowPanel("fp");
		flowPanel.setScroll(Scroll.auto);
		vp.addSubPanel(flowPanel);
		HtmlPanel t = new HtmlPanel("title","可用率统计");
		t.setStyleClass("tt3");
		flowPanel.addSubPanel(t);
		StringBuilder str = new StringBuilder();
 		str.append("<div id='chartdiv_"+condition.getMpId()+"'></div>");
 		str.append("<script type='text/javascript'>");
 		str.append("showFusionLines('"+json+"','chartdiv_"+condition.getMpId()+"');");
 		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html",str.toString());
		HorizontalPanel horizontalPanel = new HorizontalPanel("hpanel","20%,*");
		if(map!=null&&map.size()>0){
			StringBuilder str2 = null;
			for(Map.Entry<String, String> entry:map.entrySet()){
				str2 = new StringBuilder();
				str2.append("<div id='chartdiv"+entry.getKey()+"'></div>");
		 		str2.append("<script type='text/javascript'>");
		 		str2.append("showFusionCharts2('"+entry.getKey()+"','"+entry.getValue()+"','"+1+"');");
		 		str2.append("</script>");
				HtmlPanel htmlPanel2 = new HtmlPanel("html"+entry.getKey(),str2.toString());
				horizontalPanel.addSubPanel(htmlPanel2);
				break;
			}
	
		}else{
			horizontalPanel.addSubPanel(new HtmlPanel("empty_2","该段时间没有监控数据"));
		}
		horizontalPanel.addSubPanel(htmlPanel);
		flowPanel.addSubPanel(horizontalPanel);
		if(Utils.notEmpty(list)){
			flowPanel.addSubPanel(fillFauleGrid(list));
		}else{
			flowPanel.addSubPanel(new HtmlPanel("empty","<center>该时间范围内不存在故障时间统计！</center><br><br>"));
		}
		
		return vp;
	}
	
	public static Panel buildResponsePanel(Locale loc,
			ViewFactory viewFactory, QueryCondition condition,String[] pointId,
			List<Object[]> list, Object[] map, String jsonData, String jsonDate2) {
		VerticalPanel vpanel =  new VerticalPanel("overViewPanel","48,32,*");
		//时间面板
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg","时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
//				String[][] yesOrno = new String[][] { { "0", "小时" },  
//				        { "1", "天" } };  
//				Radio radio = new Radio("type", "type", "0", Arrays.asList(yesOrno));  
//				hg.add(radio);
		EmbededButton button = new EmbededButton("确定","VM(this).cmd('responseQuery');");
		hg.add(button);
		
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);
		vpanel.addSubPanel(formPanel);
		
		ButtonBarPanel mainOper=new ButtonBarPanel("main_oper3");
		mainOper.setBindFilmId("main_film");
		vpanel.addSubPanel(mainOper);
		
		ClickButton overview = new ClickButton("", "平均", "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showResponse&isIgnoreMpId=true&taskId="+condition.getTaskId()+condition.getUrlSearchConditionParm()+"'})");
		overview.setId("round");
		mainOper.addButton(overview);
		if(pointId!=null&&pointId.length>0){
			MonitoringPoint monitoringPoint = null;
			for(String mpId:pointId){
				monitoringPoint = ServiceLocator.getMonitoringPointService().getMonitoringPoint(mpId);
				if(monitoringPoint!=null){
					//tabPanel.addSubPanel(monitoringPoint.getMpId()+"Tab", monitoringPoint.getMpName(), new SourcePanel("source"+monitoringPoint.getMpId(),"vm:|monitor.sp?act=showAvailable&taskId="+condition.getTaskId()+"&mpId="+mpId+condition.getUrlSearchConditionParm()), null);					
					ClickButton cb = new ClickButton("", monitoringPoint.getMpName(), "VM( this ).cmd( { tagName : 'ajax',src : 'monitor.sp?act=showResponse&taskId="+condition.getTaskId()+"&mpId="+mpId+condition.getUrlSearchConditionParm()+"'})");
					cb.setId(monitoringPoint.getMpId()+"Tab");
					mainOper.addButton(cb);
				}
				
			}
		}
				
		FlowPanel vp  =new FlowPanel("p_report");
		vpanel.addSubPanel(vp);
		vp.setScroll(Scroll.auto);
		HtmlPanel t0 = new HtmlPanel("title0","平均响应时间统计");
		t0.setStyleClass("tt3");
		vp.addSubPanel(t0);
		StringBuilder str = new StringBuilder();
 		str.append("<div id='chartdivs_"+(Utils.notEmpty(condition.getMpId())?condition.getMpId():"")+"'></div>");
 		str.append("<script type='text/javascript'>");
 		str.append("showFusionZoomLine('"+jsonData+"','chartdivs_"+(Utils.notEmpty(condition.getMpId())?condition.getMpId():"")+"');");
 		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html",str.toString());
		vp.addSubPanel(htmlPanel);
		
		HtmlPanel detialT = new HtmlPanel("detialT","响应时间详细统计");
		detialT.setStyleClass("tt3");
		vp.addSubPanel(detialT);
		StringBuilder str1 = new StringBuilder();
		str1.append("<div id='chartdivs2_"+(Utils.notEmpty(condition.getMpId())?condition.getMpId():"")+"'></div>");
		str1.append("<script type='text/javascript'>");
		str1.append("showFusionZoomLine2('"+jsonDate2+"','chartdivs2_"+(Utils.notEmpty(condition.getMpId())?condition.getMpId():"")+"');");
		str1.append("</script>");
		HtmlPanel htmlPanel1 = new HtmlPanel("html_detial",str1.toString());
		vp.addSubPanel(htmlPanel1);
		
		HtmlPanel t1 = new HtmlPanel("title21","平均响应时间统计");
		t1.setStyleClass("tt3");
		vp.addSubPanel(t1);
		GridPanel grid = new GridPanel("grid");
		grid.setFace(GridPanelPubInfo.FACE_ODD);
		grid.setScroll(Scroll.hidden);
		grid.setData(list);
		grid.setHighlightStyle(HighlightStyle.always); // 设置点击某行总是高亮显示
		grid.pub().setHighlightRow(0, false); // 设置高亮显示的行
		grid.addTextColumn(0, "time", "日期", "*", "time");
		grid.addTextColumn(1, "max", "最小响应时间", "*", "max");
		grid.addTextColumn(2, "min", "最大响应时间", "*", "min");
		grid.addTextColumn(3, "avg", "平均响应时间", "*", "avg");
		vp.addSubPanel(grid);
//		if(map!=null&&map.size()>0){
//			HtmlPanel t2 = new HtmlPanel("title2","监测点平均响应时间统计");
//			t2.setStyleClass("tt3");
//			MonitoringPoint point = null;
////			for(Map.Entry<String, List<Object[]>> entry :map.entrySet()){
////				point = ServiceLocator.getMonitoringPointService().getMonitoringPoint(entry.getKey());
////				GridPanel grid1 = new GridPanel("grid_"+entry.getKey());
////				grid1.setFace(GridPanelPubInfo.FACE_ODD);
////				grid1.setScroll(Scroll.hidden);
////				grid1.setData(entry.getValue());
////				grid1.setHighlightStyle(HighlightStyle.always); // 设置点击某行总是高亮显示
////				grid1.pub().setHighlightRow(0, false); // 设置高亮显示的行
////				grid1.addHiddenColumn(0, "mpId");
//////				grid.addSelectitem("mpId", "40");
////				grid1.addTextColumn(1, "time", "日期", "*", "time");
////				grid1.addTextColumn(2, "max", "最小响应时间", "*", "max");
////				grid1.addTextColumn(3, "min", "最大响应时间", "*", "min");
////				grid1.addTextColumn(4, "avg", "平均响应时间", "*", "avg");
////			}
//			
//		}
		return vpanel;
	}
	
	private static Panel fillFauleGrid(List<FaultHistory> list) {
		GridPanel grid = new GridPanel("f_f_grid");
		grid.setFace(GridPanelPubInfo.FACE_ODD);
		grid.setScroll(Scroll.hidden);
		List<Object[]> dataList = new ArrayList<Object[]>();
		Object[] objects = null;
		if(Utils.notEmpty(list)){
			for(FaultHistory history:list){
				objects = new Object[7];
				objects[0] = history.getFhId();
				objects[1] = history.getFhBeginTime();
				objects[2] = history.getFhEndTime();
				objects[3] = getDuration(history.getFhBeginTime(),history.getFhEndTime());
				objects[4] = history.getFhReason();
//				objects[5] = history.getFhType();
				objects[5] = history.getFhId();
				objects[6] = history.getIsIgnore();
				dataList.add(objects);
				
			}
		}
		
		grid.setData(dataList);
		grid.setHighlightStyle(HighlightStyle.always); // 设置点击某行总是高亮显示
		grid.pub().setHighlightRow(0, false); // 设置高亮显示的行
		grid.addHidden("0", "fhId");
		grid.addTextColumn(1, "beginTime", "开始时间", "*", "beginTime");
		grid.addTextColumn(2, "endTime", "恢复时间", "*", "endTime");
		grid.addTextColumn(3, "duration", "故障持续时间", "*", "duration");
		grid.addTextColumn(4, "reason", "故障原因", "*", "reason");
		GridColumn objId = new GridColumn(GridColumnType.TEXT, 5, "op", "操作", "70");
		objId.setParser("converObject");
		grid.addColumn(objId); 
		grid.addHiddenColumn(6, "isIgnore");
		return grid;
	}
	public static String getDuration(Date beginTime, Date endTime) {
		if(beginTime==null||endTime==null){
			return "";
		}
		long begin = beginTime.getTime();
		long end = endTime.getTime();
		long temp = end-begin;
		long temp2 = 0L,temp3=0L,temp4=0L;
		if(temp>0){
			temp = temp/1000;
		}
		if(temp>0&&temp<60){
			return temp+"秒";
		}else{
			temp2 = temp%60;//秒
			temp = temp/60;
			if(temp>0&&temp<60){
				return temp+"分"+(temp2!=0?temp2+"秒":"");
			}else{
				temp3 = temp%60;//分
				temp = temp/60;
				if(temp>0&&temp<24){
					return temp+"小时"+(temp3!=0?temp3+"分":"");
				}else{
					temp4 = temp%24;//小时
					temp = temp/24;
					return temp+"天"+(temp4!=0?temp4+"小时":"");
				}
			}
		}
	}
}
