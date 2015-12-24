package com.rongji.websiteMonitor.webapp.visit.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Button;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.JSParser;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.XMLObject;
import com.rongji.dfish.engines.xmltmpl.XMLParser;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
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
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.EmbededButton;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class MonitorDetailView {

	public static BaseView buildMain(Locale loc, ViewFactory viewFactory,
			List<Object[]> dataList, List<Monitortype> typeList, Page page,
			QueryCondition condition) {
		BaseView view = new BaseView();
		VerticalPanel right = new VerticalPanel("m-main", "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(null, "监控中心");
		HorizontalPanel title = new HorizontalPanel("f_title", "*");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(ViewFactory.ID_PANEL_BUTTON_BAR);
		buttonBarPanel.setFace(ButtonFace.group);
		buttonBarPanel.setAlign(Align.right);
		fillButtonPanel(loc, viewFactory, buttonBarPanel,true);
		title.addSubPanel(titleHtml);
		VerticalPanel vp = new VerticalPanel("vp", "*");
		vp.setStyle("margin:0 30px");
		FlowPanel searchFlow = new FlowPanel(null);
		VerticalPanel vpSearch = new VerticalPanel("vpSearch", "*,26");
		fillSearchFormPanel(loc, view, viewFactory, vpSearch, typeList,
				condition);
		searchFlow.addSubPanel(vpSearch);
		GridPanel grid = viewFactory.getDefaultGridPanel();
		grid.setId("grid");
		PagePanel pagePanel = new PagePanel("f_page");
		fillGridPanel(loc, view, viewFactory, grid, dataList, page);
		fillPagePanel(viewFactory, pagePanel, page);
		fillCommand(view, condition);
		VerticalPanel listPanel = new VerticalPanel(null, "*, 26");
		listPanel.setStyleClass("bg-white");
		listPanel.addSubPanel(grid,pagePanel);
		vp.addSubPanel(listPanel);
		right.addSubPanel(title, vp);
		view.setRootPanel(right);
		return view;
	}

	private static void fillButtonPanel(Locale loc, ViewFactory viewFactory,
			ButtonBarPanel bbp, boolean isDisable) {

		// Button search = new
		// ClickButton("img/b/new.gif","查询","VM(this).cmd('onSearch')");
		// Button delete = new
		// ClickButton("img/b/delete.gif","删除","VM(this).cmd('onDelete')");
		Button reflesh = new ClickButton("img/b/refresh.gif", "刷新",
				"VM(this).cmd('refresh')");
		// bbp.addButton(search).addButton(delete)
		bbp.addButton(reflesh);

	}

	private static void fillCommand(BaseView view, QueryCondition condition) {
		view.add(JSCmdLib.reload("refresh", null));
		view.add(new AjaxCommand("page",
				"monitorDetailMgr.sp?act=turnPage&cp=$0"
						+ condition.getUrlSearchConditionParm()));
		view.add( new AjaxCommand("monitorDetailMgr", "monitorDetailMgr.sp?act=index&folder=$0&cp=$1"));
	}

	private static void fillGridPanel(Locale loc, BaseView view,
			ViewFactory viewFactory, GridPanel grid, List<Object[]> dataList,Page page) {
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(40);
		grid.setScroll(Scroll.miniscroll);
		grid.pub().setHeaderClass("bg-mid");
		grid.setFilter(false);
		grid.setData(dataList);
		grid.setNobr(true);
		grid.addHiddenColumn(0, "id");
		grid.addSelectitem("id", "40");
		grid.addTextColumn(1, "name", "项目", "*", "name");
//		grid.addTextColumn(2, "taskSummary", "摘要", "*", "taskSummary");
//		grid.addTextColumn(3, "taskType", "监控类别", "*", "taskType");
//		grid.addTextColumn(4, "frequency", "监控频率(分/次)", "*", "frequency");
		grid.addClickAttach("VM(this).cmd('clkRow','$id','"+page.getCurrentPage()+"')");
		GridColumn oper=new GridColumn(GridColumnType.TEXT,1,"psOper", "操作", "160");
		oper.setParser("psOper");
		grid.addColumn(oper);
		
		view.addParser(new XMLParser("psOper","<a herf='javascript:;' style='cursor:pointer'  onclick=\"VM(this).cmd('clkRow','$id','"+page.getCurrentPage()+"')\">查看详细</a>"));
//		view.addParser(new JSParser("psName","var id=xml.getAttribute( 'id' );"+
//				"var text=xml.getAttribute( 'en' );" +
//				"if(text=='0'){" +
//				"return \"<span style='color:gray;text-decoration:line-through;'>\"+xml.getAttribute( 'C1' )+'(禁用)</span>';"+
//				"}else{" +
//				"return xml.getAttribute( 'C1' );" +
//				"}"));
		view.addCommand(new AjaxCommand("clkRow", "clientMonitor.sp?act=index&id=$0&cp=$1"));
	}

	private static void fillPagePanel(ViewFactory viewFactory,
			PagePanel pagePanel, Page page) {
		pagePanel.setCurrentPage(page.getCurrentPage());
		pagePanel
				.setCurrentRecords(page.getCurrentPage() * page.getPageSize() > page
						.getRowCount() ? page.getRowCount() : page
						.getCurrentPage() * page.getPageSize());
		pagePanel.setMaxPage(page.getPageCount());
		pagePanel.setSumRecords(page.getRowCount());
		pagePanel.setClk("VM(this).cmd('page',$0)");

	}


	private static void fillSearchFormPanel(Locale loc, BaseView view,
			ViewFactory viewFactory, VerticalPanel vpSearch,
			List<Monitortype> typeList, QueryCondition condition) {
		FormPanel searchForm = new FormPanel("searchForm");
		ButtonBarPanel bbp = new ButtonBarPanel("bbp1");
		searchForm.setScroll(Scroll.hidden);
		searchForm.add(new Text("taskName", "项目名称", null, 500));
		searchForm.add(new Text("summary", "摘要", null, 500));
		List<Object[]> optionList = new ArrayList<Object[]>();
		optionList.add(new Object[] { "", "------" });
		if (Utils.notEmpty(typeList)) {

			for (Monitortype monitortype : typeList) {
				optionList.add(new Object[] { monitortype.getMtAlias(),
						monitortype.getMtName() });
			}

		}
		Horizontalgroup hg = new Horizontalgroup("hg", "监控类型");
		Select select = new Select("type", "监控类型", null, optionList);
		select.setRemark("&nbsp;&nbsp;&nbsp;&nbsp;时间:&nbsp;&nbsp;");
		hg.add(select);
		// hg.add()
		hg.add(CommonView.getQukSelect(condition));
		searchForm.add(hg);
		bbp.setAlign(Align.right);
		//bbp.setStyleClass(viewFactory.getStyleClass(LogicComponent.PANEL_GRID));
		//bbp.setCellspacing(12);
		//bbp.setStyle("text-align:right");
		//bbp.setFace(ButtonFace.classic);
		view.add(new SubmitCommand("search",
				"monitorDetailMgr.sp?act=doSearch", "searchForm", true));
		view.add(new LoadingCommand("loading", true));
		String resetJs = "VM(this).f('taskName',DFish.SET_VALUE,'');VM(this).f('summary',DFish.SET_VALUE,'');";
		// VM(this).cmd('loading');
		Button buttonOk = new ClickButton("", "确定", "VM(this).cmd('search')");
		Button buttonReset = new ClickButton("", "重置", resetJs);
		Button buttonClose = new ClickButton("", "关闭",
				"VM(this).find('vp').reset('0,*');");
		bbp.addButton(buttonOk).addButton(buttonReset).addButton(buttonClose);
		vpSearch.addSubPanel(searchForm, bbp);
	}

	public static Command updateView4chuangpage(Locale loc,
			ViewFactory viewFactory, List<Object[]> dataList, Page page,
			QueryCondition condition) {
		// 序列命令初始化，用于有顺序的组装一些执行的命令
		CommandGroup cg = new CommandGroup("cg");
		cg.setPath("/m-main");
		UpdateCommand ucSearchForm = new UpdateCommand("uc_search_form"); // 更新searchPanel命令
		UpdateCommand ucGrid = new UpdateCommand("uc_grid"); // 更新GridPanel命令
		UpdateCommand ucPage = new UpdateCommand("uc_page"); // 更新PagePanel命令
		UpdateCommand ucPageCmd = new UpdateCommand("uc_page_cmd");// 更新PageCommand命令
		UpdateCommand ucShowDetialCmd = new UpdateCommand("uc_detial_cmd");// 更新统计详细的命令
		UpdateCommand ucResponseCmd = new UpdateCommand("uc_response_cmd");
		cg.add(ucGrid).add(ucPage).add(ucPageCmd).add(ucShowDetialCmd)
				.add(ucResponseCmd);

		// 更新searchFormPanel
		VerticalPanel vpSearch = new VerticalPanel("vpSearch", "*,26");
		;
		// fillSearchFormPanel(loc, ucSearchForm.getView(), viewFactory,
		// vpSearch);
		ucSearchForm.getView().addSubPanel(vpSearch);

		// 更新GridPanel
		GridPanel grid = viewFactory.getDefaultGridPanel();
		grid.setId("grid");
		fillGridPanel(loc, ucGrid.getView(), viewFactory, grid, dataList, page);
		ucGrid.getView().addSubPanel(grid);

		// 更新PagePanel
		AjaxCommand cpage = new AjaxCommand(
				viewFactory.getId(LogicComponent.CMD_PAGE),
				"monitorDetailMgr.sp?act=turnPage&cp=$0"
						+ condition.getUrlSearchConditionParm());

		PagePanel pagePanel = new PagePanel(
				viewFactory.getId(LogicComponent.PANEL_PAGE));
		fillPagePanel(viewFactory, pagePanel, page);
		pagePanel.setClickCommand(cpage);
		ucPage.setContent(pagePanel);
		ucPageCmd.setContent(cpage);

		DialogCommand showDetial = new DialogCommand("showDetials",
				ViewFactory.ID_DIALOG_STANDARD, "$1-详细统计", "import_dlg",
				DialogCommand.WIDTH_LARGE, 700, DialogPosition.middle,
				"vm:|monitorDetailMgr.sp?act=showDetial&taskId=$0"
						+ condition.getUrlSearchConditionParm());
		ucShowDetialCmd.setContent(showDetial);

		DialogCommand showResponseDetial = new DialogCommand(
				"showResponseDetials", ViewFactory.ID_DIALOG_STANDARD,
				"$1-响应时间详细统计", "import_dlg", DialogCommand.WIDTH_LARGE, 700,
				DialogPosition.middle,
				"vm:|monitorDetailMgr.sp?act=showResponseDetial&taskId=$0"
						+ condition.getUrlSearchConditionParm());
		ucResponseCmd.setContent(showResponseDetial);

		return cg;

	}

	public static XMLObject buildIndexDetialView(Locale loc,
			ViewFactory viewFactory, String[] pointId, QueryCondition condition) {
		BaseView view = new BaseView();
		VerticalPanel vp = new VerticalPanel("vp", "50,*");

		view.setRootPanel(vp);
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg", "时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
		EmbededButton button = new EmbededButton("确定",
				"VM(this).cmd('onQuery');");
		hg.add(button);
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);

		vp.addSubPanel(formPanel);

		TabPanel tabPanel = new TabPanel("tab");
		tabPanel.addSubPanel(
				"CompositeTab",
				"综合",
				new SourcePanel("source0",
						"vm:|monitorDetailMgr.sp?act=showCompositeDetialBytab&taskId="
								+ condition.getTaskId()
								+ condition.getUrlSearchConditionParm()), null);
		tabPanel.addSubPanel(
				"avagTab",
				"平均",
				new SourcePanel("source1",
						"vm:|monitorDetailMgr.sp?act=showDetialBytab&taskId="
								+ condition.getTaskId()
								+ condition.getUrlSearchConditionParm()), null);
		vp.addSubPanel(tabPanel);
		if (pointId != null && pointId.length > 0) {
			MonitoringPoint monitoringPoint = null;
			for (String mpId : pointId) {
				monitoringPoint = ServiceLocator.getMonitoringPointService()
						.getMonitoringPoint(mpId);
				if (monitoringPoint != null) {
					tabPanel.addSubPanel(
							monitoringPoint.getMpId() + "Tab",
							monitoringPoint.getMpName(),
							new SourcePanel(
									"source" + monitoringPoint.getMpId(),
									"vm:|monitorDetailMgr.sp?act=showDetialBytab&taskId="
											+ condition.getTaskId()
											+ "&mpId="
											+ mpId
											+ condition
													.getUrlSearchConditionParm()),
							null);
				}

			}
		}

		view.add(new JSCommand(
				"onQuery",
				"VM( this ).ext( 'startTime', VM(this).fv('startTime'));"
						+ "VM( this ).ext( 'endTime', VM(this).fv('endTime'));"
						+ "VM(this).ext('sel',VM(this).fv('sel'));"
						+ "VM(this).cmd('query',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ))"));
		// view.add(JSCmdLib.reload("query",
		// "vm:|monitorDetailMgr.sp?act=showDetial&startTime='+$0+'&endTime='+$1+'&sel='+$2+'&taskId="+condition.getTaskId()));
		view.add(new JSCommand(
				"query",
				"VM(this).reload('vm:|monitorDetailMgr.sp?act=showDetial&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&taskId="
						+ condition.getTaskId() + "');"));

		return view;

	}

	public static XMLObject buildDetialView(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String json,
			Map<String, String> map, List<FaultHistory> list, Page page) {
		BaseView view = new BaseView();
		// 30,300,30,
		VerticalPanel vp = new VerticalPanel("vp", "*");

		view.setRootPanel(vp);
		// FormPanel formPanel = new FormPanel("form");
		// formPanel.setScroll(Scroll.hidden);
		// Horizontalgroup hg = new Horizontalgroup("hg","时间");
		// hg.setFullLine(true);
		// hg.add(CommonView.getQukSelect(condition));
		// EmbededButton button = new
		// EmbededButton("确定","VM(this).cmd('onQuery');");
		// hg.add(button);
		// formPanel.add(hg);
		// formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		// formPanel.setHorizontalMinus(20);
		// formPanel.setVerticalMinus(14);
		//
		// vp.addSubPanel(formPanel);

		// TabPanel panel = new TabPanel("tabP");

		FlowPanel flowPanel = new FlowPanel("fp");
		flowPanel.setScroll(Scroll.auto);
		vp.addSubPanel(flowPanel);
		HtmlPanel t = new HtmlPanel("title", "可用率统计");
		// vp.addSubPanel(new
		// HtmlPanel("title","<div style='height: 18px;line-height: 18px;background: url(g/tt3.gif);text-indent: 6px;color: #083772;text-align: center;'>平均可用率统计</div>"));
		t.setStyleClass("tt3");
		// t.setStyle("margin:15px 0px;");
		flowPanel.addSubPanel(t);
		StringBuilder str = new StringBuilder();
		str.append("<div id='chartdiv_" + condition.getMpId() + "'></div>");
		str.append("<script type='text/javascript'>");
		str.append("showFusionLines('" + json + "','chartdiv_"
				+ condition.getMpId() + "');");
		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html", str.toString());
		HorizontalPanel horizontalPanel = new HorizontalPanel("hpanel", "20%,*");
		if (map != null && map.size() > 0) {
			StringBuilder str2 = null;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				str2 = new StringBuilder();
				str2.append("<div id='chartdiv" + entry.getKey() + "'></div>");
				str2.append("<script type='text/javascript'>");
				str2.append("showFusionCharts2('" + entry.getKey() + "','"
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
		horizontalPanel.addSubPanel(htmlPanel);
		flowPanel.addSubPanel(horizontalPanel);

		HtmlPanel t2 = new HtmlPanel("title3", "故障时间统计");
		// vp.addSubPanel(new
		// HtmlPanel("title","<div style='height: 18px;line-height: 18px;background: url(g/tt3.gif);text-indent: 6px;color: #083772;text-align: center;'>平均可用率统计</div>"));
		t2.setStyleClass("tt3");
		// t.setStyle("margin:15px 0px;");
		flowPanel.addSubPanel(t2);
		if (Utils.notEmpty(list)) {
			flowPanel.addSubPanel(fillFauleGrid(list));
			// PagePanel pagePanel = new PagePanel("page");
			// fillPagePanel(viewFactory,pagePanel , page);
			// flowPanel.addSubPanel(pagePanel);
		} else {
			flowPanel.addSubPanel(new HtmlPanel("empty",
					"<center>该时间范围内不存在故障时间统计！</center><br><br>"));
		}

		// HtmlPanel t1 = new HtmlPanel("title2","监测点可用率统计");
		// // vp.addSubPanel(new
		// HtmlPanel("title","<div style='height: 18px;line-height: 18px;background: url(g/tt3.gif);text-indent: 6px;color: #083772;text-align: center;'>平均可用率统计</div>"));
		// t1.setStyleClass("tt3");
		// // t.setStyle("margin:15px 0px;");
		// flowPanel.addSubPanel(t1);
		// int num = 0;
		// String col = "";
		// if(map!=null&&map.size()>0){
		// num = map.size();
		// for(int i=0;i<num;i++){
		// if(Utils.notEmpty(col)){
		// col = col+",*";
		// }else{
		// col = col+"*";
		// }
		//
		// }
		// HorizontalPanel panel = new HorizontalPanel("hp",col);
		// StringBuilder str2 = null;
		// for(Map.Entry<String, String> entry:map.entrySet()){
		// str2 = new StringBuilder();
		// str2.append("<div id='chartdiv"+entry.getKey()+"'></div>");
		// str2.append("<script type='text/javascript'>");
		// str2.append("showFusionCharts2('"+entry.getKey()+"','"+entry.getValue()+"','"+num+"');");
		// str2.append("</script>");
		// HtmlPanel htmlPanel2 = new
		// HtmlPanel("html"+entry.getKey(),str2.toString());
		// panel.addSubPanel(htmlPanel2);
		// }
		//
		// flowPanel.addSubPanel(panel);
		// }else{
		// flowPanel.addSubPanel(new HtmlPanel("empty_2","该段时间没有监控数据"));
		// }
		// StringBuilder str2 = new StringBuilder();
		// str2.append("<div id='chartdiv2'></div>");
		// str2.append("<script type='text/javascript'>");
		// str2.append("showFusionCharts2('"+json+"');");
		// str2.append("</script>");
		// HtmlPanel htmlPanel2 = new HtmlPanel("html2",str2.toString());
		// vp.addSubPanel(htmlPanel2);
		// view.add(new
		// SubmitCommand("query","monitorDetailMgr.sp?act=queryDetial&taskId="+condition.getTaskId(),"form",true));

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
				"monitorDetailMgr.sp?act=ignoreFault&fhId=$0&mpId=$1"));
		return view;
	}

	private static Panel fillFauleGrid(List<FaultHistory> list) {
		GridPanel grid = new GridPanel("f_f_grid");
		grid.setFace(GridPanelPubInfo.FACE_ODD);
		grid.setScroll(Scroll.hidden);
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

		grid.setData(dataList);
		grid.addHidden("0", "fhId");
		grid.addTextColumn(1, "beginTime", "开始时间", "*", "beginTime");
		grid.addTextColumn(2, "endTime", "恢复时间", "*", "endTime");
		grid.addTextColumn(3, "duration", "故障持续时间", "*", "duration");
		grid.addTextColumn(4, "reason", "故障原因", "*", "reason");
		GridColumn objId = new GridColumn(GridColumnType.TEXT, 5, "op", "操作",
				"70");
		// status.setAlign(Align.left);
		objId.setParser("converObject");
		grid.addColumn(objId);
		grid.addHiddenColumn(6, "isIgnore");
		return grid;
	}

	public static String getDuration(Date beginTime, Date endTime) {
		if (beginTime == null || endTime == null) {
			return "";
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

	public static XMLObject buildIndexResponseView(Locale loc,
			ViewFactory viewFactory, String[] pointId, QueryCondition condition) {
		BaseView view = new BaseView();

		VerticalPanel vpanel = new VerticalPanel("vp", "50,*");

		view.setRootPanel(vpanel);
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		Horizontalgroup hg = new Horizontalgroup("hg", "时间");
		hg.setFullLine(true);
		hg.add(CommonView.getQukSelect(condition));
		EmbededButton button = new EmbededButton("确定",
				"VM(this).cmd('onQuery');");
		hg.add(button);
		formPanel.add(hg);
		formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		formPanel.setHorizontalMinus(20);
		formPanel.setVerticalMinus(14);

		vpanel.addSubPanel(formPanel);

		TabPanel tabPanel = new TabPanel("tab");
		tabPanel.addSubPanel(
				"avagTab",
				"平均",
				new SourcePanel("source1",
						"vm:|monitorDetailMgr.sp?act=showResponseDetialByTab&taskId="
								+ condition.getTaskId()
								+ condition.getUrlSearchConditionParm()), null);
		vpanel.addSubPanel(tabPanel);
		if (pointId != null && pointId.length > 0) {
			MonitoringPoint monitoringPoint = null;
			for (String mpId : pointId) {
				monitoringPoint = ServiceLocator.getMonitoringPointService()
						.getMonitoringPoint(mpId);
				if (monitoringPoint != null) {
					tabPanel.addSubPanel(
							monitoringPoint.getMpId() + "Tab",
							monitoringPoint.getMpName(),
							new SourcePanel(
									"source" + monitoringPoint.getMpId(),
									"vm:|monitorDetailMgr.sp?act=showResponseDetialByTab&taskId="
											+ condition.getTaskId()
											+ "&mpId="
											+ mpId
											+ condition
													.getUrlSearchConditionParm()),
							null);
				}

			}
		}

		view.add(new JSCommand(
				"onQuery",
				"VM( this ).ext( 'startTime', VM(this).fv('startTime'));"
						+ "VM( this ).ext( 'endTime', VM(this).fv('endTime'));"
						+ "VM(this).ext('sel',VM(this).fv('sel'));"
						+ "VM(this).cmd('query',VM( this ).ext( 'startTime' ),VM( this ).ext( 'endTime' ),VM( this ).ext( 'sel' ))"));
		// view.add(JSCmdLib.reload("query",
		// "vm:|monitorDetailMgr.sp?act=showDetial&startTime='+$0+'&endTime='+$1+'&sel='+$2+'&taskId="+condition.getTaskId()));
		view.add(new JSCommand(
				"query",
				"VM(this).reload('vm:|monitorDetailMgr.sp?act=showResponseDetial&startTime='+$0+'&endTime='+$1+'&quickPeriod='+$2+'&taskId="
						+ condition.getTaskId() + "');"));
		return view;
	}

	public static XMLObject buildResponseView(Locale loc,
			ViewFactory viewFactory, QueryCondition condition,
			List<Object[]> list, Map<String, List<Object[]>> map,
			String jsonData, String jsonDate2) {
		BaseView view = new BaseView();

		VerticalPanel vpanel = new VerticalPanel("vp", "*");

		view.setRootPanel(vpanel);
		// FormPanel formPanel = new FormPanel("form");
		// formPanel.setScroll(Scroll.hidden);
		// Horizontalgroup hg = new Horizontalgroup("hg","时间");
		// hg.setFullLine(true);
		// hg.add(CommonView.getQukSelect(condition));
		// EmbededButton button = new
		// EmbededButton("确定","VM(this).cmd('onQuery');");
		// hg.add(button);
		// formPanel.add(hg);
		// formPanel.setStyle("margin:6px;padding:0px 3px;border:1px gray dashed");
		// formPanel.setHorizontalMinus(20);
		// formPanel.setVerticalMinus(14);
		//
		// vpanel.addSubPanel(formPanel);

		FlowPanel vp = new FlowPanel("p_report");
		vpanel.addSubPanel(vp);
		vp.setScroll(Scroll.auto);
		HtmlPanel t0 = new HtmlPanel("title0", "平均响应时间统计");
		t0.setStyleClass("tt3");
		vp.addSubPanel(t0);
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
		vp.addSubPanel(htmlPanel);

		HtmlPanel detialT = new HtmlPanel("detialT", "响应时间详细统计");
		detialT.setStyleClass("tt3");
		vp.addSubPanel(detialT);
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
		vp.addSubPanel(htmlPanel1);

		HtmlPanel t1 = new HtmlPanel("title21", "平均响应时间统计");
		t1.setStyleClass("tt3");
		vp.addSubPanel(t1);
		GridPanel grid = new GridPanel("grid");
		grid.setFace(GridPanelPubInfo.FACE_ODD);
		grid.setScroll(Scroll.hidden);
		grid.setData(list);
		grid.addTextColumn(0, "time", "日期", "*", "time");
		grid.addTextColumn(1, "max", "最小响应时间", "*", "max");
		grid.addTextColumn(2, "min", "最大响应时间", "*", "min");
		grid.addTextColumn(3, "avg", "平均响应时间", "*", "avg");
		vp.addSubPanel(grid);
		if (map != null && map.size() > 0) {
			HtmlPanel t2 = new HtmlPanel("title2", "监测点平均响应时间统计");
			t2.setStyleClass("tt3");
			for (Map.Entry<String, List<Object[]>> entry : map.entrySet()) {
				GridPanel grid1 = new GridPanel("grid_" + entry.getKey());
				grid1.setFace(GridPanelPubInfo.FACE_ODD);
				grid1.setScroll(Scroll.hidden);
				grid1.setData(entry.getValue());
				grid1.addHiddenColumn(0, "mpId");
				grid1.addTextColumn(1, "time", "日期", "*", "time");
				grid1.addTextColumn(2, "max", "最小响应时间", "*", "max");
				grid1.addTextColumn(3, "min", "最大响应时间", "*", "min");
				grid1.addTextColumn(4, "avg", "平均响应时间", "*", "avg");
			}

		}

		return view;
	}

	public static XMLObject buildCompositeDetialView(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String json) {
		BaseView view = new BaseView();

		VerticalPanel vpanel = new VerticalPanel("vp", "*");

		view.setRootPanel(vpanel);
		FlowPanel vp = new FlowPanel("p_report");
		vpanel.addSubPanel(vp);
		vp.setScroll(Scroll.auto);
		HtmlPanel t0 = new HtmlPanel("title0", "可用率统计");
		t0.setStyleClass("tt3");
		vp.addSubPanel(t0);
		StringBuilder str = new StringBuilder();
		str.append("<div id='chartdivs_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "'></div>");
		str.append("<script type='text/javascript'>");
		str.append("showFusionZoomLine('"
				+ json
				+ "','chartdivs_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "');");
		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html", str.toString());
		vp.addSubPanel(htmlPanel);

		return view;
	}

	public static XMLObject buildTodayDetialView(Locale loc,
			ViewFactory viewFactory, QueryCondition condition, String json,
			String respJson) {
		BaseView view = new BaseView();

		VerticalPanel vpanel = new VerticalPanel("vp", "*");

		view.setRootPanel(vpanel);
		FlowPanel vp = new FlowPanel("p_report");
		vpanel.addSubPanel(vp);
		vp.setScroll(Scroll.auto);
		HtmlPanel t0 = new HtmlPanel("title0", "今日可用率统计");
		t0.setStyleClass("tt3");
		vp.addSubPanel(t0);
		StringBuilder str = new StringBuilder();
		str.append("<div id='chartdivs_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "'></div>");
		str.append("<script type='text/javascript'>");
		str.append("showFusionZoomLine('"
				+ json
				+ "','chartdivs_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "');");
		str.append("</script>");
		HtmlPanel htmlPanel = new HtmlPanel("html", str.toString());
		vp.addSubPanel(htmlPanel);

		HtmlPanel t1 = new HtmlPanel("title1", "今日响应时间统计");
		t1.setStyleClass("tt3");
		vp.addSubPanel(t1);
		StringBuilder str1 = new StringBuilder();
		str1.append("<div id='chartdivs2_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "'></div>");
		str1.append("<script type='text/javascript'>");
		str1.append("showFusionZoomLine2('"
				+ respJson
				+ "','chartdivs2_"
				+ (Utils.notEmpty(condition.getMpId()) ? condition.getMpId()
						: "") + "');");
		str1.append("</script>");
		HtmlPanel htmlPanel1 = new HtmlPanel("html1", str1.toString());
		vp.addSubPanel(htmlPanel1);

		return view;
	}

}
