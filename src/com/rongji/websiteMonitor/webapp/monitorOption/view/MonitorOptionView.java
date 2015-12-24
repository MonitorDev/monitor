package com.rongji.websiteMonitor.webapp.monitorOption.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Button;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.View;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.XMLParser;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.ConfirmCommand;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridColumn;
import com.rongji.dfish.engines.xmltmpl.component.GridColumnType;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanelPubInfo;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Hidden;
import com.rongji.dfish.engines.xmltmpl.form.Radio;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.Threshold;

public class MonitorOptionView {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static BaseView buildIndexView(Locale loc, ViewFactory viewFactory,
			List<Object[]> dataList, Page page,List<Threshold> listThreshold) {
		
		BaseView view = new BaseView();
		VerticalPanel right = new VerticalPanel("m-main", "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(null, "设置");
		HorizontalPanel title = new HorizontalPanel("f_title", "*,1,700");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(ViewFactory.ID_PANEL_BUTTON_BAR);
		buttonBarPanel.setFace(ButtonFace.group);
		buttonBarPanel.setAlign(Align.right);
		fillButtonPanel(loc, viewFactory, buttonBarPanel,true);
		title.addSubPanel(titleHtml,HtmlPanel.EMPTY,buttonBarPanel);
		
		HorizontalPanel vp = new HorizontalPanel(null, "*");
		vp.setStyle("margin:0 30px");
		GridPanel grid = viewFactory.getDefaultGridPanel();
		grid.setId("grid");
		fillGridPanel(loc, view, viewFactory, grid, dataList);
		VerticalPanel leftListPanel = new VerticalPanel(null, "*");
		leftListPanel.setStyleClass("bg-white");
		leftListPanel.addSubPanel(grid);
		right.addSubPanel(title, vp);
		view.setRootPanel(right);
		fillCommand(view);
//		vp.addSubPanel(leftListPanel);
//		vp.addSubPanel(HtmlPanel.EMPTY);
		vp.addSubPanel(buildThresholdPanel(listThreshold, page));
		
		return view;
	}
	private static void fillButtonPanel(Locale loc, ViewFactory viewFactory,
			ButtonBarPanel bbp, boolean isDisable) {
		bbp.setAlign(Align.right);
		bbp.setFace(ButtonFace.group);
		Button add = new ClickButton(null,"创建SNMP阀值模板","VM(this).cmd('detail','','add','snmp')");
		Button add2 = new ClickButton(null,"创建HTTP/PING阀值模板","VM(this).cmd('http','','add')");
		bbp.addButton(add).addButton(add2);
		
	}

	private static void fillCommand(BaseView view) {
		String panelToSubmit = ViewFactory.ID_PANEL_FORM;
		view.add(new AjaxCommand("page", "option.sp?act=turnPage&cp=$0"));
		view.addParser(new XMLParser("psOper","<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('detail','$id','detail','$type')\">详细</a> &nbsp;&nbsp;<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('detail','$id','edit','$type')\">修改</a>&nbsp;&nbsp;<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('deleteThreshold','$id')\">删除</a>"));
		view.addCommand( new DialogCommand("detail",
				"f_std", "阀值模板", "import_dlg", DialogCommand.WIDTH_MEDIUM,
				DialogCommand.HEIGHT_MEDIUM, DialogCommand.POSITION_MIDDLE,
				"vm:|option.sp?act=showThreshold&id=$0&type=$2&handler=$1"));
		view.addCommand( new DialogCommand("http",
				"f_std", "阀值模板", "import_dlg", DialogCommand.WIDTH_MEDIUM,
				DialogCommand.HEIGHT_MEDIUM, DialogCommand.POSITION_MIDDLE,
				"vm:|option.sp?act=showHttpThreshold&id=$0&type=$1"));
		Command delThreshold= new AjaxCommand("delCmd", "option.sp?act=deleteThread&id=$0");
		Command delThreshold2 = new ConfirmCommand("deleteThreshold", "确定要删除吗？", delThreshold);
		view.add(delThreshold).add(delThreshold2);
	}

	
	public static View buildThresholdView(Threshold threshold, boolean isEdit) {
		BaseView view = new BaseView();
		view.add(new SubmitCommand("saveThreshold",
				"option.sp?act=saveThreshold", "thresholdForm", true));
		VerticalPanel vp = new VerticalPanel("threshold_vp", "85%,*");
		FormPanel form = new FormPanel("thresholdForm");
		
		Text name = new Text("name", "阀值模板名称", threshold.getName(), 500);
		name.setGrayTip("如：模板1");
		form.add(name);
		name.setNotnull(true);
		if(Utils.notEmpty(threshold.getId())) {
			form.add(new Hidden("id", threshold.getId()));
			name.setDisabled(true);
		}
		Map<String, String> config = Utils.string2Map(threshold.getContent());
		Text cpu = new Text("cpu", "cpu使用预警（百分比%）", config.get("cpu"), 500);
		cpu.setGrayTip("如：80");
		form.add(cpu);

		Text memory = new Text("memory", "系统内存使用预警（百分比%）",
				config.get("memory"), 500);
		memory.setGrayTip("如：80");
		form.add(memory);

		Text jvmMemory = new Text("jvmMemory", "JVM内存使用预警（百分比%）",
				config.get("jvmMemory"), 500);
		jvmMemory.setGrayTip("如：80");
		form.add(jvmMemory);

		Text jvmThread = new Text("jvmThread", "JVM线程使用预警（数量）",
				config.get("jvmThread"), 500);
		jvmThread.setGrayTip("如：200");
		form.add(jvmThread);

//		Text io = new Text("io", "网络流量（Kb/s）",config.get("io"), 500);
//		io.setGrayTip("如：1024");
//		form.add(io);
		
		Text diskUsage = new Text("diskUsage", "磁盘使用率（百分比）",config.get("diskUsage"), 500);
		diskUsage.setGrayTip("如：80");
		form.add(diskUsage);
		
		Text systemProcess = new Text("systemProcess", "系统线程数",config.get("systemProcess"), 500);
		systemProcess.setGrayTip("如：120");
		form.add(systemProcess);
		//
		List<Object[]> o1 = new ArrayList<Object[]>();
		o1.add(new Object[] { "1", "1次" });
		o1.add(new Object[] { "2", "2次" });
		o1.add(new Object[] { "3", "3次" });
		o1.add(new Object[] { "3", "4次" });
		o1.add(new Object[] { "3", "5次" });
		Select adminRetry = new Select("retry", "重试几次后告警",
				new Object[] { threshold.getRetry() }, o1);
		
		Radio radio = new Radio("isUsable", "是否可用", threshold.getIsUsable(), Arrays.asList( new String[][] { { "1", "可用" },  
		        { "0", "不可用" } }  ));
		if(Utils.isEmpty(threshold.getId())) {
			radio.setValue("1");
		}
		form.add(adminRetry);
		form.add(radio);
		vp.addSubPanel(form);
		
		ButtonBarPanel bbp = new ButtonBarPanel("f_btn");
		bbp.setAlign(Align.right);
	    bbp.setFace(ButtonFace.classic);
	    bbp.setStyleClass("d-bg bd-form bd-onlytop");
	    bbp.setStyle("margin:0 28px");
		ClickButton save = new ClickButton("", "  确定  ",
				"VM(this).cmd('saveThreshold');");
		save.setFocus(true);
		ClickButton close = new ClickButton("", "  关闭  ", "DFish.close(this);");
		if(isEdit) {
			bbp.addButton(save);
		}
		bbp.addButton(close);
		vp.addSubPanel(bbp);
		
		view.setRootPanel(vp);
		return view;
	}
	
	public static View buildHttpThresholdView(Threshold threshold, boolean isEdit) {
		BaseView view = new BaseView();
		view.add(new SubmitCommand("saveHttpThreshold",
				"option.sp?act=saveHttpThreshold", "thresholdForm", true));
		VerticalPanel vp = new VerticalPanel("threshold_vp", "85%,*");
		FormPanel form = new FormPanel("thresholdForm");
		
		Text name = new Text("name", "阀值模板名称", threshold.getName(), 500);
		name.setGrayTip("如：模板1");
		form.add(name);
		name.setNotnull(true);
		if(Utils.notEmpty(threshold.getId())) {
			form.add(new Hidden("id", threshold.getId()));
			name.setDisabled(true);
		}
		Map<String, String> config = Utils.string2Map(threshold.getContent());
		Text cpu = new Text("responseTime", "响应时间(ms)", config.get("responseTime"), 500);
		cpu.setGrayTip("如：5000");
		form.add(cpu);
		//
		List<Object[]> o1 = new ArrayList<Object[]>();
		o1.add(new Object[] { "1", "1次" });
		o1.add(new Object[] { "2", "2次" });
		o1.add(new Object[] { "3", "3次" });
		o1.add(new Object[] { "3", "4次" });
		o1.add(new Object[] { "3", "5次" });
		Select adminRetry = new Select("retry", "重试几次后告警",
				new Object[] { threshold.getRetry() }, o1);
		
		Radio radio = new Radio("isUsable", "是否可用", threshold.getIsUsable(), Arrays.asList( new String[][] { { "1", "可用" },  
		        { "0", "不可用" } }  ));
		if(Utils.isEmpty(threshold.getId())) {
			radio.setValue("1");
		}
		form.add(adminRetry);
		form.add(radio);
		vp.addSubPanel(form);
		
		ButtonBarPanel bbp = new ButtonBarPanel("f_btn");
		bbp.setAlign(Align.right);
	    bbp.setFace(ButtonFace.classic);
	    bbp.setStyleClass("d-bg bd-form bd-onlytop");
	    bbp.setStyle("margin:0 28px");
		ClickButton save = new ClickButton("", "  确定  ",
				"VM(this).cmd('saveHttpThreshold');");
		save.setFocus(true);
		ClickButton close = new ClickButton("", "  关闭  ", "DFish.close(this);");
		if(isEdit) {
			bbp.addButton(save);
		}
		bbp.addButton(close);
		vp.addSubPanel(bbp);
		
		view.setRootPanel(vp);
		return view;
	}

	private static void fillGridPanel(Locale loc, BaseView view,
			ViewFactory viewFactory, GridPanel grid,
			List<Object[]> dataList) {
		grid.pub().setHeaderClass("bg-mid");  
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(50);
		grid.setScroll(Scroll.miniscroll);
		grid.setData(dataList);
		grid.addHiddenColumn(0, "type");
//		grid.addSelectitem("mpId", "40");
		grid.addTextColumn(1, "mpName", "设置选项", "*", "mpName");
		grid.setNobr(true);
		grid.addClickAttach("VM(this).cmd('clkRow','$type')");
		view.addCommand(new AjaxCommand("clkRow", "option.sp?act=optionList&type=$0"));
		
	}


	private static void fillPagePanel(PagePanel pagePanel, Page page) {
		pagePanel.setCurrentPage(page.getCurrentPage());
		pagePanel
		.setCurrentRecords(page.getCurrentPage() * page.getPageSize() > page
				.getRowCount() ? page.getRowCount() : page
				.getCurrentPage()
				* page.getPageSize());
		pagePanel.setMaxPage(page.getPageCount());
		pagePanel.setSumRecords(page.getRowCount()); 
		pagePanel.setClk("VM(this).cmd('page',$0)"); 
		
	}


	public static Panel buildThresholdPanel(List<Threshold> listThreshold, Page page) {
		VerticalPanel listPanel = new VerticalPanel("righPanel", "*, 26");
		List<Object[]> dataList = new ArrayList<Object[]>();
		for(Threshold th : listThreshold) {
			Object[] obj = new Object[10];
			obj[0] = th.getId();
			obj[1] = th.getName();
			obj[2] = sdf.format(th.getCreateTime());
			obj[3] = th.getUpdateTime()==null?"":sdf.format(th.getUpdateTime());
			obj[4] = Constants.OPTION_IS_YES.equals(th.getIsUsable())?"可用":"不可用";
			obj[5] = th.getType();
			if(Constants.OPTION_IS_YES.equals(th.getIsDefault())) {
				obj[7] = "<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('detail','"+th.getId()+"','detail','"+th.getType()+"')\">详细</a>";
			}else {
				obj[7] = "<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('detail','"+th.getId()+"','detail','"+th.getType()+"')\">详细</a> &nbsp;&nbsp;<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('detail','"+th.getId()+"','edit','"+th.getType()+"')\">修改</a>&nbsp;&nbsp;<a herf='javascript:;' style='cursor:pointer'onclick=\"VM(this).cmd('deleteThreshold','"+th.getId()+"')\">删除</a>";
			}
			obj[6] = th.getIsDefault();
			dataList.add(obj);
		}
		listPanel.setStyleClass("bg-white");
		GridPanel grid = new GridPanel("thresholdList");
		grid.pub().setHeaderClass("bg-mid");  
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(40);
		grid.setScroll(Scroll.miniscroll);
		grid.setData(dataList);
		grid.setFilter(false);
		grid.addHiddenColumn(0, "id");
		grid.addHiddenColumn(6, "isDefault");
		grid.addTextColumn(1, "name", "模板名称", "*", "name");
		grid.addTextColumn(5, "type", "模板类型", "*", "type");
		grid.addTextColumn(2, "time", "创建时间", "*", "time");
		grid.addTextColumn(3, "updateTime", "更新时间", "*", "updateTime");
		grid.addTextColumn(4, "isUsable", "是否可用", "*", "isUsable");
		grid.addTextColumn(7, "handler", "操作", "*", "handler");
		grid.setNobr(true);
		GridColumn oper=new GridColumn(GridColumnType.TEXT,1,"psOper", "操作", "160");
		oper.setParser("psOper");
//		grid.addColumn(oper);
		PagePanel pagePanel = new PagePanel("f_page");
		fillPagePanel(pagePanel, page);
		listPanel.addSubPanel(grid, pagePanel);
		return listPanel;
	}
	
	public static CommandGroup updateThresholdPanel(Locale loc,
			ViewFactory viewFactory,List<Threshold> listThreshold, Page page) {
		CommandGroup cg = new CommandGroup("cg");
		List<Object[]> dataList = new ArrayList<Object[]>();
		for(Threshold th : listThreshold) {
			Object[] obj = new Object[10];
			obj[0] = th.getId();
			obj[1] = th.getName();
			obj[2] = sdf.format(th.getCreateTime());
			obj[3] = th.getUpdateTime()==null?"":sdf.format(th.getUpdateTime());
			obj[4] = Constants.OPTION_IS_YES.equals(th.getIsUsable())?"可用":"不可用";
			dataList.add(obj);
		}
		UpdateCommand ucBtn = new UpdateCommand("uc_btn"); //更新按钮部分
		
		
		ucBtn.setContent(buildThresholdPanel(listThreshold, page));
		cg.add(ucBtn);
		return cg;
	}


}
