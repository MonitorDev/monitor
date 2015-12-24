package com.rongji.websiteMonitor.webapp.subproject.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Button;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.EventTarget;
import com.rongji.dfish.engines.xmltmpl.FormElement;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
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
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.Fieldset;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridColumn;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanelPubInfo;
import com.rongji.dfish.engines.xmltmpl.component.GroupingGridPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.Hr;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Checkboxgroup;
import com.rongji.dfish.engines.xmltmpl.form.Combobox;
import com.rongji.dfish.engines.xmltmpl.form.Hidden;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.engines.xmltmpl.form.Radio;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Threshold;

public class SubprojectView {

	public static BaseView buildIndexView(Locale loc, ViewFactory viewFactory,
			Map<Project, List<Subproject>> dataList, Page page) {
		BaseView view = new BaseView();
		VerticalPanel right = new VerticalPanel("m-main", "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(null, "监控服务管理");
		HorizontalPanel title = new HorizontalPanel("f_title", "*,1,700");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(
				ViewFactory.ID_PANEL_BUTTON_BAR);
		buttonBarPanel.setFace(ButtonFace.group);
		buttonBarPanel.setAlign(Align.right);
		fillButtonPanel(loc, viewFactory, buttonBarPanel, true, Constants.HTTP_TYPE);
		title.addSubPanel(titleHtml, HtmlPanel.EMPTY, buttonBarPanel);

		HorizontalPanel vp = new HorizontalPanel(null, "400,*");
		vp.setStyle("margin:0 30px");
		GroupingGridPanel grid = new GroupingGridPanel("grid");
		grid.setId("grid");
		fillGridPanel(loc, view, viewFactory, grid, dataList);
		VerticalPanel leftListPanel = new VerticalPanel(null, "*, 26");
		leftListPanel.setStyleClass("bg-white");
		PagePanel pagePanel = new PagePanel("f_page");
		fillPagePanel(viewFactory, pagePanel, page);
		leftListPanel.addSubPanel(grid, pagePanel);
		right.addSubPanel(title, vp);
		view.setRootPanel(right);
		FormPanel form = new FormPanel(viewFactory.ID_PANEL_FORM);
		fillIndexFormPanel(form);
		fillCommand(view);
		vp.addSubPanel(leftListPanel);
		vp.addSubPanel(form);
		return view;
	}

	private static void fillButtonPanel(Locale loc, ViewFactory viewFactory,
			ButtonBarPanel bbp, boolean isDisable, String type) {
		bbp.setAlign(Align.right);
		bbp.setFace(ButtonFace.group);
		ExpandableButton btn = new ExpandableButton(null, "新建", null);
		Button add = new ClickButton(null, "新建网址（http）",
				"VM(this).cmd('add','"+Constants.HTTP_TYPE+"')");
		Button add2 = new ClickButton(null, "新建Ping",
				"VM(this).cmd('add','"+Constants.PING_TYPE+"')");
		Button add4 = new ClickButton(null, "新建服务器(snmp)",
				"VM(this).cmd('add','"+Constants.SNMP_TYPE+"')");
		Button add3 = new ClickButton(null, "新建归档",
				"VM(this).cmd('addArchive')");
		// Button add3 = new
		// ClickButton(null,"新建SNMP","VM(this).cmd('addSnmp','snmp')");
		btn.addButton(add).addButton(add2).addButton(add4).addButton(add3);
		ClickButton save = new ClickButton(null, "保存", "VM(this).cmd('save')");
		if (Constants.SNMP_TYPE.equals(type)) {
			save = new ClickButton(null, "保存", "VM(this).cmd('saveSnmp')");
		}
		if (isDisable) {
			save.setDisabled(true);
		} else {
			save.setDisabled(false);
		}
		Button delete = new ClickButton(null, "删除", "VM(this).cmd('onDelete')");
		Button reflesh = new ClickButton(null, "刷新", "VM(this).cmd('refresh')");
		bbp.addButton(btn).addButton(save).addButton(delete).addButton(reflesh);

	}

	private static void fillCommand(BaseView view) {
		String panelToSubmit = ViewFactory.ID_PANEL_FORM;
//		view.add(new JSCommand(
//				"addThreshold",
//				"var num=VM(this).f('thresholdNum',DFish.GET_VALUE);num=parseInt(num)+1;VM(this).f('thresholdNum').value=num;VM(this).cmd('addThresholdElement',num);"));
		view.add(new AjaxCommand("addThresholdElement",
				"subproject.sp?act=insert&num=$0"));
		view.add(new AjaxCommand("deleteThreshold",
				"subproject.sp?act=deleteThreshold&num=$0"));
		view.add(new AjaxCommand("add", "subproject.sp?act=edit&type=$0"));
		view.add(new AjaxCommand("addArchive",
				"subproject.sp?act=addArchive&type=$0"));
		view.add(new SubmitCommand("saveSnmp", "subproject.sp?act=saveSnmp",
				panelToSubmit, true));
		view.add(new SubmitCommand("save", "subproject.sp?act=save",
				panelToSubmit, true));
		view.add(new SubmitCommand("saveArchive",
				"subproject.sp?act=saveArchive", panelToSubmit, true));
		// view.add(new
		// JSCommand("onSave","VM( this ).ext( 'authorName', VM(this).f('authorName',DFish.GET_VALUE));VM( this ).ext( 'stId', VM(this).f('stId',DFish.GET_VALUE));VM(this).cmd('save',VM( this ).ext( 'authorName' ),VM( this ).ext( 'stId' ))"));
		view.add(new JSCommand(
				"onSearch",
				"var theH='0';if(VM(this).find('f_grid').g_sep().charAt(0)=='0')theH='121';VM(this).find('f_grid').reset(theH+',*');"));
		view.add(new JSCommand(
				"onDelete",
				"var sel=VM(this).f('selectItem',DFish.GET_VALUE);"
						+ "VM( this ).ext( 'monitorPointName', VM(this).f('monitorPointName',DFish.GET_VALUE));"
						+ "VM(this).ext('cp',VM( this ).find( 'f_page' ).attr( 'currentPage' ));"
						+ "if(sel&&sel.split(',').length>=1){"
						+ "VM(this).cmd('del_cfm',sel)"
						+ "}else if(VM(this)._selRow){"
						+ "VM(this).cmd('del_cfm',VM(this)._selRow)"
						+ "}else{"
						+ "VM(this).cmd({ tagName : 'alert', cn : '请选择要删除的行!',tm : 3 });"
						+ "}"));
		Command delCmd = new SubmitCommand("delCmd",
				"subproject.sp?act=delete", "grid", true);
		Command delCfm = new ConfirmCommand("del_cfm", "确定要删除选中的行吗？", delCmd);
		view.add(delCfm).add(delCmd);
		view.add(JSCmdLib.reload("refresh", null));
		view.add(new AjaxCommand("page", "subproject.sp?act=turnPage&cp=$0"));
		DialogCommand showCronRule = new DialogCommand("showCronRule", "f_std",
				"时间表达式说明", "name", DialogCommand.WIDTH_MEDIUM,
				DialogCommand.HEIGHT_LARGE, DialogCommand.POSITION_MIDDLE,
				"webapp/modules/services/cron_help_zh.html");
		DialogCommand showThreshold = new DialogCommand("addThreshold",
				"f_std", "添加阀值模板", "import_dlg", DialogCommand.WIDTH_MEDIUM,
				DialogCommand.HEIGHT_MEDIUM, DialogCommand.POSITION_MIDDLE,
				"vm:|subproject.sp?act=showThreshold");
		
		DialogCommand addHttpThreshold = new DialogCommand("addHttpThreshold",
				"f_std", "添加阀值模板", "import_dlg", DialogCommand.WIDTH_MEDIUM,
				DialogCommand.HEIGHT_MEDIUM, DialogCommand.POSITION_MIDDLE,
				"vm:|subproject.sp?act=showHttpThreshold");
		
		view.add(showCronRule);
		view.add(showThreshold);
		view.add(addHttpThreshold);
		view.add(new AjaxCommand("typeRefresh", "subproject.sp?act=typeRefresh&isExternal=$0"));

	}

	public static void fillGridPanel(Locale loc, BaseView view,
			ViewFactory viewFactory, GroupingGridPanel grid,
			Map<Project, List<Subproject>> dataList) {
		if (dataList != null) {
			for (Entry<Project, List<Subproject>> entry : dataList.entrySet()) {
				Project cate = entry.getKey();
				List<Subproject> operList = entry.getValue();
				List<String[]> subList = new ArrayList<String[]>();
				if (Utils.notEmpty(operList)) {
					for (Subproject oper : operList) {
						subList.add(new String[] { oper.getId(),
								oper.getName(), oper.getType() });
					}
				}
				grid.addData(cate.getName() + " (" + subList.size() + ")",
						subList);
			}
		}
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(40);
		grid.setScroll(Scroll.miniscroll);
		grid.pub().setHeaderClass("bg-mid");
		grid.addHiddenColumn(0, "id");
		grid.addSelectitem("id", "40");
		grid.addTextColumn(1, "name", "项目名称", "*", null);
		grid.addTextColumn(2, "type", "项目类别", "100", null);
		grid.setNobr(true);
		grid.addClickAttach("VM(this).cmd('clkRow','$id','$type')");
		view.addCommand(new AjaxCommand("clkRow",
				"subproject.sp?act=edit&id=$0&type=$1"));

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

	private static void fillIndexFormPanel(FormPanel form) {
		String html = "<div style=\"padding:24px;line-height:160%\">"
				// +
				// "<p style=\"text-align:left;font-size:12pt;font-weight:bold\">监控项目管理：</p>"
				+ "<p>主要是提供对监控服务管理。监控服务管理具有以下特点：<br/>" + "<ul>"
				+ "<li>可以点击上面的按钮进行增删改。</li>" 
				+ "<li>每个监控服务都基于监控项目存在的</li>"
				+ "<li>一个监控项目中可以建立多个监控服务</li>"
				+ "</ul>" + "</div>";
		form.setScroll(Scroll.auto);
		form.setStyleClass("introduction_bg");
		form.addHtml(null, true, html);

	}

	/**
	 * 执行新建编辑更新
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param typeList
	 * @param type
	 * @param catalogRelation
	 * @param caId
	 * @return
	 */
	public static CommandGroup updateView4AddEdit(Locale loc,
			ViewFactory viewFactory, Subproject subproject,
			List<Monitortype> typeList, List<Project> listProject, List<Threshold> listThreshold) {
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucForm = new UpdateCommand("uc_form"); // 更新FormPanel命令
		UpdateCommand ucBtn = new UpdateCommand("uc_btn"); // 更新按钮部分
		cg.add(ucForm).add(ucBtn);
		FormPanel form = viewFactory.getDefaultFormPanel();
		// form.setStyle("");
		ucForm.setContent(form);
		fillFormPanel(loc, viewFactory, form, subproject, typeList, listProject, listThreshold);
		ButtonBarPanel bbp = new ButtonBarPanel("bbp");
		bbp.setId(ViewFactory.ID_PANEL_BUTTON_BAR);
		ucBtn.setContent(bbp);
		fillButtonPanel(loc, viewFactory, bbp, false, subproject.getType());
		return cg;
	}

	public static CommandGroup addArchiveView(Locale loc,
			ViewFactory viewFactory, Subproject subproject, List<Monitortype> typeList) {
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucForm = new UpdateCommand("uc_form"); // 更新FormPanel命令
		UpdateCommand ucBtn = new UpdateCommand("uc_btn"); // 更新按钮部分
		cg.add(ucForm).add(ucBtn);
		FormPanel form = viewFactory.getDefaultFormPanel();
		ucForm.setContent(form);

		addArchiveViewfillFormPanel(loc, viewFactory, form, subproject, typeList);
		ButtonBarPanel bbp = new ButtonBarPanel("bbp");
		bbp.setId(ViewFactory.ID_PANEL_BUTTON_BAR);
		ucBtn.setContent(bbp);
		addArchiveFillButtonPanel(loc, viewFactory, bbp, false);
		return cg;
	}

	private static void addArchiveFillButtonPanel(Locale loc,
			ViewFactory viewFactory, ButtonBarPanel bbp, boolean isDisable) {
		bbp.setAlign(Align.right);
		bbp.setFace(ButtonFace.group);
		ExpandableButton btn = new ExpandableButton(null, "新建", null);
		Button add = new ClickButton(null, "新建网址（http）",
				"VM(this).cmd('add','"+Constants.HTTP_TYPE+"')");
		Button add2 = new ClickButton(null, "新建Ping",
				"VM(this).cmd('add','"+Constants.PING_TYPE+"')");
		Button add4 = new ClickButton(null, "新建服务器(snmp)",
				"VM(this).cmd('add','"+Constants.SNMP_TYPE+"')");
		Button add3 = new ClickButton(null, "新建归档",
				"VM(this).cmd('addArchive')");
		// Button add3 = new
		// ClickButton(null,"新建SNMP","VM(this).cmd('addSnmp','snmp')");
		btn.addButton(add).addButton(add2).addButton(add4).addButton(add3);
		ClickButton save = new ClickButton(null, "保存",
				"VM(this).cmd('saveArchive')");
		if (isDisable) {
			save.setDisabled(true);
		} else {
			save.setDisabled(false);
		}
		Button delete = new ClickButton(null, "删除", "VM(this).cmd('onDelete')");
		Button reflesh = new ClickButton(null, "刷新", "VM(this).cmd('refresh')");
		bbp.addButton(btn).addButton(save).addButton(delete).addButton(reflesh);

	}

	private static void addArchiveViewfillFormPanel(Locale loc,
			ViewFactory viewFactory, FormPanel form, Subproject subproject,
			List<Monitortype> typeList) {
		if (subproject == null) {
			subproject = new Subproject();
		}
		form.setScroll(Scroll.auto);
		if (subproject != null && Utils.notEmpty(subproject.getId())) {
			form.add(new Hidden("id", subproject.getId()));
		}
		Fieldset info = new Fieldset("归档信息");

		Text taskName = new Text("name", "归档名称", subproject.getName(), 100,
				true, false, false, null, null);
		info.add(taskName);
		List<Object[]> optionList = null;
//		if (Utils.notEmpty(typeList)) {
//			optionList = new ArrayList<Object[]>(typeList.size());
//			for (Monitortype monitortype : typeList) {
//				optionList.add(new Object[] { monitortype.getMtAlias(),
//						monitortype.getMtName() });
//			}
//
//		}
		optionList = new ArrayList<Object[]>(typeList.size());
		optionList.add(new Object[]{"http",Constants.HTTP_TYPE});
		optionList.add(new Object[]{"ping",Constants.PING_TYPE});
		
		String value = subproject.getType();
		Select type = new Select("type", "归档类别", new Object[] { value },
				optionList);
		type.setOnchange("var type = VM(this).f('type',DFish.GET_VALUE);if(type!=null&&type=='"+Constants.HTTP_TYPE+"'){"
				+ "VM(this).f('more',DFish.HIDDEN,false);"
				+ "}else{VM(this).f('more',DFish.HIDDEN,true);}");
		info.add(type);
		info.addText(
				"cronExpression",
				"时钟方式",
				subproject.getCronExpression(),
				200,
				true,
				false,
				null,
				null,
				"<a href='javascript:void(0);' onclick=\"VM(this).cmd('showCronRule')\">帮助</a>",
				"40", false); // .addText();
		List<Object[]> o3 = new ArrayList<Object[]>();
		o3.add(new Object[] { Constants.OPTION_IS_YES, "是" });
		o3.add(new Object[] { Constants.OPTION_IS_NO, "否" });
		Radio isUseable = new Radio("isusable", "是否启用",
				Utils.notEmpty(subproject.getIsuable()) ? subproject.getIsuable()
						: Constants.OPTION_IS_YES, o3);
		info.add(isUseable);
		form.add(info);

	}

	/**
	 * 执行新建编辑更新
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param typeList
	 * @param type
	 * @param catalogRelation
	 * @param caId
	 * @return
	 */
	public static CommandGroup updateView4AddSnmpEdit(Locale loc,
			ViewFactory viewFactory, Subproject subproject,
			List<Monitortype> typeList, List<Threshold> listThreshold,
			List<Project> listProject) {
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucForm = new UpdateCommand("uc_form"); // 更新FormPanel命令
		UpdateCommand ucBtn = new UpdateCommand("uc_btn"); // 更新按钮部分
		cg.add(ucForm).add(ucBtn);
		FormPanel form = viewFactory.getDefaultFormPanel();
		ucForm.setContent(form);
		fillSnmpFormPanel(loc, viewFactory, form, subproject, typeList,
				listThreshold, listProject);
		ButtonBarPanel bbp = new ButtonBarPanel("bbp");
		bbp.setId(ViewFactory.ID_PANEL_BUTTON_BAR);
		ucBtn.setContent(bbp);
		fillButtonPanel(loc, viewFactory, bbp, false, subproject.getType());
		return cg;
	}

	/**
	 * 创建表单面板
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param view
	 * @param form
	 * @param typeList
	 * @param contype
	 * @param source
	 * @param viewType
	 */
	private static void fillFormPanel(Locale loc, ViewFactory viewFactory,
			FormPanel form, Subproject subproject, List<Monitortype> typeList,
			List<Project> listProject, List<Threshold> listThreshold) {
		// form.setHighlightMouseover(false);
		// form.setStyle("background-color:#FFF");
		if (subproject == null) {
			subproject = new Subproject();
		}
		Gson gson = new Gson();
		form.setScroll(Scroll.auto);

		Fieldset info = new Fieldset("监控信息");
		if (subproject != null && Utils.notEmpty(subproject.getId())) {
			info.add(new Hidden("id", subproject.getId()));
		}
		Text name = new Text("name", "项目名称", subproject.getName(), 100,
				true, false, false, null, null);
		name.setGrayTip("如：项目1");
		if(Utils.notEmpty(subproject.getId())) {
			name.setDisabled(true);
		}
		name.setNotnull(true);
		info.add(name);
		List<Object[]> optionList = null;
		if (Utils.notEmpty(typeList)) {
			optionList = new ArrayList<Object[]>(typeList.size());
			for (Monitortype monitortype : typeList) {
				optionList.add(new Object[] { monitortype.getMtAlias(),
						monitortype.getMtName() });
			}

		}
		String typeValue = subproject.getType();
		Select type = new Select("type", "监测类别", new Object[] { typeValue },
				optionList);
		if(Utils.notEmpty(subproject.getId())) {
			type.setDisabled(true);
		}
		type.setNotnull(true);
		type.setOnchange("var type=VM(this).f('type',DFish.GET_VALUE);VM(this).cmd('add',type)");
		info.add(type);
		
		List<Object[]> listProjectOptions = null;
		if (Utils.notEmpty(listProject)) {
			listProjectOptions = new ArrayList<Object[]>(listProject.size());
			for (Project p : listProject) {
				listProjectOptions.add(new Object[] { p.getId(), p.getName() });
			}
		}
		String projectValue = subproject.getProjectId();
		Select project = new Select("projectId", "项目",
				new Object[] { projectValue }, listProjectOptions);
		project.setNotnull(true);
		info.add(project);
		Text describe = new Text("url", "URL/IP/主机", subproject.getUrl(), 500);
		describe.setNotnull(true);
		if(Constants.PING_TYPE.equals(typeValue)) {
			describe.setGrayTip("如：www.baidu.com");
		}else {
			describe.setGrayTip("如：192.168.4.17:8090/monitor/index.jsp或www.baidu.com");
		}
		info.add(describe);
		Radio isExternal = new Radio("isExternal", "监控方式", subproject.getIsExternal(), Arrays.asList(new String[][]{{"1","外部监控"},{"0","内部监控"}}),  
		        false, false, false, false, null);  
		isExternal.setNotnull(true);
		isExternal.setOn(EventTarget.EVENT_CHANGE, "VM(this).fs('monitorPoint','');var isExternal=VM(this).fv('isExternal');VM(this).cmd('typeRefresh',isExternal);");
		info.add(isExternal);
		Combobox monitoerPoint = new Combobox("monitorPoint", "监控点",
				subproject.getMonitorPoint(), "loading",
				"subproject.sp?act=typeGridOptions", null,
				null, 200, false, false, false, true,
				false, true);
		monitoerPoint.setNotnull(true);
		info.add(monitoerPoint);

		Fieldset option = new Fieldset("监控选项");
		List<Object[]> o = new ArrayList<Object[]>();
		o.add(new Object[] { "2", "2分钟" });
		o.add(new Object[] { "5", "5分钟" });
		o.add(new Object[] { "10", "10分钟" });
		o.add(new Object[] { "15", "15分钟" });
		o.add(new Object[] { "20", "20分钟" });
		o.add(new Object[] { "30", "30分钟" });
		o.add(new Object[] { "60", "60分钟" });

		Select frequency = new Select("frequency", "监控频率",
				new Object[] { subproject.getFrequency() }, o);
		frequency.setRemark("(<font color='red'>频率监控越快越好</font>)");
		option.add(frequency);

		List<Object[]> sequenceWar = new ArrayList<Object[]>();

		sequenceWar.add(new Object[] { "0", "无" });
		sequenceWar.add(new Object[] { "30", "30分钟" });
		sequenceWar.add(new Object[] { "60", "60分钟" });
		sequenceWar.add(new Object[] { "90", "90分钟" });
		sequenceWar.add(new Object[] { "120", "120分钟" });
		sequenceWar.add(new Object[] { "300", "300分钟" });

		Select sequenceWarning = new Select("warningFrequency", "连续告警提醒",
				new Object[] { subproject.getWarningFrequency() }, sequenceWar);
		option.add(sequenceWarning);

		List<Object[]> o2 = new ArrayList<Object[]>();
		o2.add(new Object[] { "1", "1次" });
		o2.add(new Object[] { "2", "2次" });
		o2.add(new Object[] { "3", "3次" });

		Select retry = new Select("retry", "重试几次后告警",
				new Object[] { subproject.getRetry() }, o2);
		retry.setRemark("(<font color='red'>发现故障后自动进行以上次数的重试，多次重试都失败后，才会触发故障告警。重试时间间隔目前固定为1分钟</font>)");
		option.add(retry);

		List<Object[]> o3 = new ArrayList<Object[]>();
		o3.add(new Object[] { Constants.OPTION_IS_YES, "是" });
		o3.add(new Object[] { Constants.OPTION_IS_NO, "否" });
		Radio isUseable = new Radio("isuable", "是否启用",
				Utils.notEmpty(subproject.getIsuable()) ? subproject
						.getIsuable() : Constants.OPTION_IS_YES, o3);
		option.add(isUseable);
		
		Map<String, String> map = Utils.string2Map(subproject.getConfigXml());
		Threshold th = null, defaultThreshld=null;
		List<Object[]> o5 = new ArrayList<Object[]>();  
		option.addHidden("0", gson.toJson(new Threshold()));
		if (listThreshold != null) {
			for (int i = 0; i < listThreshold.size(); i++) {
				Threshold threshold = listThreshold.get(i);
				o5.add(new Object[]{threshold.getId(), threshold.getName()});
				Map<String, String> config = Utils.string2Map(threshold.getContent());
				config.put("retry", threshold.getRetry()+"");
				option.addHidden(threshold.getId(), gson.toJson(config));
				if(threshold.getId().equals(map.get("continueGroup"))) {
					th = threshold;
				}else {
					if(Constants.OPTION_IS_YES.equals(threshold.getIsDefault())) {
						defaultThreshld = threshold;
					}
				}
			}
			if(th == null) {
				th = defaultThreshld;
			}
		}
		Fieldset thresholdSet = new Fieldset("阀值策略");
		Select select = new Select("threshold","阀值模板", new Object[] {th.getId()}, o5);
		select.setRemark("&nbsp;&nbsp;&nbsp;<a href=\"javascript:void(0);\" onclick=\"VM(this).cmd('addHttpThreshold');\" style='color:#87C3FD'>添加阀值模板</a>");
		StringBuffer sb2 = new StringBuffer();
		sb2.append("var value = VM(this).f('threshold',DFish.GET_VALUE );var jsonStr = VM(this).f(value,DFish.GET_VALUE );")
		.append("var thJson = JSON.parse(jsonStr);")
		.append("VM(this).fs('responseTime',thJson.responseTime);")
		.append("VM(this).fs('thresholdRetry',thJson.retry);");
		select.setOn(EventTarget.EVENT_CHANGE,sb2.toString());
		thresholdSet.add(select);
		Map<String, String> config = Utils.string2Map(th.getContent());
		Label cpu = new Label("responseTime", "响应时间(S)",
				config.get("responseTime"));
		thresholdSet.add(cpu);
		option.add(thresholdSet);
		
		Fieldset general = new Fieldset("常规设置");
		List<Map<String, String>> list = gson.fromJson(
				subproject.getNotification(),
				new TypeToken<List<Map<String, String>>>() {
				}.getType());
		StringBuilder sb = new StringBuilder();
		String emailContent = "";
		String phoneNum = "";
		if (Utils.notEmpty(list)) {
			for (Map<String, String> map1 : list) {
				sb.append(map1.get("type")).append(",");
				if ("eMail".equals(map1.get("type"))) {
					emailContent = map1.get("content");
				} else if ("phone".equals(map1.get("type"))) {
					phoneNum = map1.get("content");
				}
			}
		}
		Checkboxgroup checkboxgroup = new Checkboxgroup("checkboxgroup",
				"通知方式", new Object[] { sb.toString() }, new Object[] {
						Constants.EMAIL_TYPE, Constants.PHONE_TYPE });
		Text EMail = new Text("eMail", "E-Mail", emailContent, -1);
		EMail.setGrayTip("多个邮箱用,分隔");
		checkboxgroup.add(EMail);

		Text phone = new Text("phone", "电话", phoneNum, -1);
		phone.setGrayTip("多个电话用,分隔");
		checkboxgroup.add(phone);
		general.add(checkboxgroup);
		form.add(info).add(option).add(general);

	}

	/**
	 * 创建Snmp表单面板
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param view
	 * @param form
	 * @param typeList
	 * @param contype
	 * @param source
	 * @param viewType
	 */
	private static void fillSnmpFormPanel(Locale loc, ViewFactory viewFactory,
			FormPanel form, Subproject subproject, List<Monitortype> typeList,
			List<Threshold> listThreshold, List<Project> listProject) {
		form.setScroll(Scroll.auto);
		if (subproject != null && Utils.notEmpty(subproject.getId())) {
			form.add(new Hidden("id", subproject.getId()));
		}
		Fieldset info = new Fieldset("监控信息");

		Text name = new Text("name", "项目名称", subproject.getName(), 100,
				true, false, false, null, null);
		name.setGrayTip("如：项目1");
		if(Utils.notEmpty(subproject.getId())) {
			name.setDisabled(true);
		}
		info.add(name);
		List<Object[]> optionList = null;
		if (Utils.notEmpty(typeList)) {
			optionList = new ArrayList<Object[]>(typeList.size());
			for (Monitortype monitortype : typeList) {
				optionList.add(new Object[] { monitortype.getMtAlias(),
						monitortype.getMtName() });
			}

		}
		String value = subproject.getType();
		Select type = new Select("type", "监测类别", new Object[] { value },
				optionList);
		type.setOnchange("var type=VM(this).f('type',DFish.GET_VALUE);VM(this).cmd('add',type)");
		if(Utils.notEmpty(subproject.getId())) {
			type.setDisabled(true);
		}
		type.setNotnull(true);
		info.add(type);
		List<Object[]> listProjectOptions = null;
		if (Utils.notEmpty(listProject)) {
			listProjectOptions = new ArrayList<Object[]>(listProject.size());
			for (Project p : listProject) {
				listProjectOptions.add(new Object[] { p.getId(), p.getName() });
			}
		}
		String projectValue = subproject.getProjectId();
		Select project = new Select("projectId", "项目",
				new Object[] { projectValue }, listProjectOptions);
		project.setNotnull(true);
		info.add(project);

		Text describe = new Text("url", "服务器地址", subproject.getUrl(), 500);
		describe.setNotnull(true);
		describe.setGrayTip("如：192.168.4.17/161");
		info.add(describe);
//		Radio isExternal = new Radio("isExternal", "监控方式", subproject.getIsExternal(), Arrays.asList(new String[][]{{"1","外部监控"},{"0","内部监控"}}),  
//		        false, false, false, false, null);  
//		isExternal.setNotnull(true);
//		info.add(isExternal);

		Fieldset option = new Fieldset("监控选项");
		List<Object[]> o = new ArrayList<Object[]>();
		o.add(new Object[] { "2", "2分钟" });
		o.add(new Object[] { "5", "5分钟" });
		o.add(new Object[] { "10", "10分钟" });
		o.add(new Object[] { "15", "15分钟" });
		o.add(new Object[] { "20", "20分钟" });
		o.add(new Object[] { "30", "30分钟" });
		o.add(new Object[] { "60", "60分钟" });

		Select frequency = new Select("frequency", "监控频率",
				new Object[] { subproject.getFrequency() }, o);
		frequency.setRemark("(<font color='red'>频率监控越快越好</font>)");
		option.add(frequency);

		List<Object[]> sequenceWar = new ArrayList<Object[]>();

		sequenceWar.add(new Object[] { "0", "无" });
		sequenceWar.add(new Object[] { "30", "30分钟" });
		sequenceWar.add(new Object[] { "60", "60分钟" });
		sequenceWar.add(new Object[] { "90", "90分钟" });
		sequenceWar.add(new Object[] { "120", "120分钟" });
		sequenceWar.add(new Object[] { "300", "300分钟" });

		Select sequenceWarning = new Select("warningFrequency", "连续告警提醒",
				new Object[] { subproject.getWarningFrequency() }, sequenceWar);
		option.add(sequenceWarning);

		List<Object[]> o2 = new ArrayList<Object[]>();
		o2.add(new Object[] { "1", "1次" });
		o2.add(new Object[] { "2", "2次" });
		o2.add(new Object[] { "3", "3次" });

		Select retry = new Select("retry", "重试几次后告警",
				new Object[] { subproject.getRetry() }, o2);
		retry.setRemark("(<font color='red'>发现故障后自动进行以上次数的重试，多次重试都失败后，才会触发故障告警。重试时间间隔目前固定为1分钟</font>)");
		option.add(retry);

		List<Object[]> o3 = new ArrayList<Object[]>();
		o3.add(new Object[] { Constants.OPTION_IS_YES, "是" });
		o3.add(new Object[] { Constants.OPTION_IS_NO, "否" });
		Radio isUseable = new Radio("isusable", "是否启用",
				Utils.notEmpty(subproject.getIsuable()) ? subproject
						.getIsuable() : Constants.OPTION_IS_YES, o3);
		option.add(isUseable);

		Map<String, String> map = Utils.string2Map(subproject.getConfigXml());
		Gson gson = new Gson();
		Threshold th = null, defaultThreshld = null;
		List<Object[]> o5 = new ArrayList<Object[]>();  
		option.addHidden("0", gson.toJson(new Threshold()));
		if (listThreshold != null) {
			for (int i = 0; i < listThreshold.size(); i++) {
				Threshold threshold = listThreshold.get(i);
				o5.add(new Object[]{threshold.getId(), threshold.getName()});
				Map<String, String> config = Utils.string2Map(threshold.getContent());
				config.put("retry", threshold.getRetry()+"");
				option.addHidden(threshold.getId(), gson.toJson(config));
				if(threshold.getId().equals(map.get("continueGroup"))) {
					th = threshold;
				}else {
					if(Constants.OPTION_IS_YES.equals(threshold.getIsDefault())) {
						defaultThreshld = threshold;
					}
				}
			}
			if(th == null) {
				th = defaultThreshld;
			}
		}
		Fieldset thresholdSet = new Fieldset("阀值策略");
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
		thresholdSet.add(select);
		Map<String, String> config = Utils.string2Map(th.getContent());
		Label cpu = new Label("cpu", "cpu使用预警（百分比%）",
				config.get("cpu"));
		thresholdSet.add(cpu);
		Label memory = new Label("memory", "系统内存使用预警（百分比%）",
				config.get("memory"));
		thresholdSet.add(memory);

		Label jvmMemory = new Label("jvmMemory", "JVM内存使用预警（百分比%）",
				config.get("jvmMemory")
						);
		thresholdSet.add(jvmMemory);

		Label jvmThread = new Label("jvmThread", "JVM线程使用预警（数量）",
				config.get("jvmThread"));
		thresholdSet.add(jvmThread);

//		Label io = new Label("io", "网络流量（Kb/s）", config.get("io"));
		Label diskUsageElement = new Label("diskUsage", "系统使用率（百分比%）", config.get("diskUsage"));
		
		Label systemProcessElement = new Label("systemProcess", "系统进程数", config.get("systemProcess"));
		Label adminRetry = new Label("thresholdRetry", "重试几次后告警",
				th.getRetry());
//		thresholdSet.add(io);
		thresholdSet.add(diskUsageElement);
		thresholdSet.add(systemProcessElement);
		thresholdSet.add(adminRetry);
		
		option.add(thresholdSet);

		Text email1 = new Text("email", "E-Mail",
				map.get("email"+th.getId()), 600);
		email1.setGrayTip("多个邮件用逗号(,)分割");
		Text phone1 = new Text("phone", "电话",
				map.get("phone"+th.getId()), 600);
		phone1.setGrayTip("多个电话用逗号(,)分割");
//		phone1.setWidth("400px");
		option.add(email1).add(phone1);
		form.add(info).add(option);

	}

	/**
	 * 翻页动作后刷新界面
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param dataList
	 * @param page
	 * @param monitorPointName
	 * @param authorName
	 * @param syn
	 * @param caId
	 * @return
	 */
	public static CommandGroup updateView4Save(Locale loc,
			ViewFactory viewFactory, Map<Project, List<Subproject>> mapDatas, Page page,
			Subproject subproject, List<Monitortype> typeList, List<Project> listProject, List<Threshold> listThreshold) {
		// 序列命令初始化，用于有顺序的组装一些执行的命令
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucGrid = new UpdateCommand("uc_grid"); // 更新GridPanel命令
		UpdateCommand ucPage = new UpdateCommand("uc_page"); // 更新PagePanel命令
		UpdateCommand ucPageCmd = new UpdateCommand("uc_page_cmd");// 更新PageCommand命令
		cg.add(ucGrid)
				.add(ucPage)
				.add(ucPageCmd)
				.add(new JSCommand("", "VM(this).find('grid').focus({id:'"
						+ subproject.getId() + "'});"));
		// 更新GridPanel
		GroupingGridPanel grid = new GroupingGridPanel("grid");
		grid.setId("grid");
		fillGridPanel(loc, ucGrid.getView(), viewFactory, grid, mapDatas);
		ucGrid.getView().addSubPanel(grid);

		// 更新PagePanel
		AjaxCommand cpage = null;
		cpage = new AjaxCommand(
							viewFactory.getId(LogicComponent.CMD_PAGE),
							"subproject.sp?act=turnPage&cp=$0");
		PagePanel pagePanel = new PagePanel(
				viewFactory.getId(LogicComponent.PANEL_PAGE));
		fillPagePanel(viewFactory, pagePanel, page);
		pagePanel.setClickCommand(cpage);
		ucPage.setContent(pagePanel);
		ucPageCmd.setContent(cpage);

		// 更新FormPanel
		CommandGroup uc2 = (CommandGroup) updateView4AddEdit(loc, viewFactory,
				subproject, typeList, listProject, listThreshold);
		cg.add(uc2);

		return cg;
	}

	public static Command updateView4Delete(Locale loc,
			ViewFactory viewFactory, Map<Project, List<Subproject>> mapDatas, Page page,
			Subproject subproject, List<Monitortype> typeList, List<Project> listProject, List<Threshold> listThreshold) {
		 return updateView4Save(loc, viewFactory, mapDatas,
				 page, new Subproject(), typeList, listProject, listThreshold);
	}

	public static BaseView getOptionGridView(Locale loc,
			ViewFactory viewFactory, List<MonitoringPoint> dataList) {
		BaseView view = viewFactory.buildGridView(false, false, false, true);
		GridPanel grid = viewFactory.findGridPanel(view);
		List<String[]> data = new ArrayList<String[]>(dataList.size());
		for (MonitoringPoint person : dataList) {
			String[] obj = new String[4];

			obj[0] = person.getMpId();
			obj[1] = person.getMpName();
			obj[2] = person.getMpId();
			data.add(obj);
		}
		grid.setData(data);
		grid.setStyleClass("lb");
		grid.setScroll(Scroll.hidden);
		/*
		 * 当作combobox或onlinebox使用的时候设置它的值和文本字段
		 */
		grid.pub().setComboboxSupport("mtId", "mtName", "mtAlias");
		grid.setPageSize(50);
		grid.setHasTableHead(false);
		grid.addHiddenColumn(0, "mtId");
		GridColumn userName = GridColumn.text(1, "mtName", "监测点", "*");
		userName.setStyle("text-align:left;");
		grid.addHiddenColumn(2, "mtAlias");
		grid.addColumn(userName);
		PagePanel page = viewFactory.findPagePanel(view);
		page.setBindGridId(ViewFactory.ID_PANEL_GRID);
		return view;
	}

	public static List<FormElement> createElement(ViewFactory viewFactory,
			Map<String, String> map, int num) {
		List<FormElement> list = new ArrayList<FormElement>();
		// 添加管理员snmp监控数据的阀值---------start
		Hr hr2 = new Hr();
		hr2.setName("hr" + num);
		hr2.setTitle("阀值配置(" + num + ")");
		hr2.setToggle(Hr.TOGGLE_EXPEND);
		list.add(hr2);
		Text cpu = new Text("cpu" + num, "cpu使用预警（百分比%）",
				map.get("cpu") == null ? "" : map.get("cpu").toString(), 500);
		cpu.setStyleClass("thresholed" + num);
		// cpu.setNotnull(true);
		list.add(cpu);

		Text memory = new Text("memory" + num, "系统内存使用预警（百分比%）",
				map.get("memory") == null ? "" : map.get("memory").toString(),
				500);
		memory.setStyleClass("thresholed" + num);
		// memory.setNotnull(true);
		list.add(memory);

		Text jvmMemory = new Text("jvmMemory" + num, "JVM内存使用预警（百分比%）",
				map.get("jvmMemory") == null ? "" : map.get("jvmMemory")
						.toString(), 500);
		jvmMemory.setStyleClass("thresholed" + num);
		// jvmMemory.setNotnull(true);
		list.add(jvmMemory);

		Text jvmThread = new Text("jvmThread" + num, "JVM线程使用预警（数量）",
				map.get("jvmThread") == null ? "" : map.get("jvmThread")
						.toString(), 500);
		jvmThread.setStyleClass("thresholed" + num);
		// jvmThread.setNotnull(true);
		list.add(jvmThread);

		Text io = new Text("io" + num, "网络流量（Kb/s）", map.get("io") == null ? ""
				: map.get("io").toString(), 500);
		io.setStyleClass("thresholed" + num);
		// jvmThread.setNotnull(true);
		list.add(io);
		//
		List<Object[]> o1 = new ArrayList<Object[]>();
		o1.add(new Object[] { "1", "1次" });
		o1.add(new Object[] { "2", "2次" });
		o1.add(new Object[] { "3", "3次" });
		o1.add(new Object[] { "3", "4次" });
		o1.add(new Object[] { "3", "5次" });
		Select adminRetry = new Select("retry" + num, "重试几次后告警",
				new Object[] { map.get("retry") }, o1);
		adminRetry.setStyleClass("thresholed" + num);
		// adminRetry.setRemark("<a href='javascript:void(0);' onclick=\"VM(this).cmd('createThrehold')\">新建阀值</a>");
		list.add(adminRetry);

		List<Object[]> o4 = new ArrayList<Object[]>();
		o4.add(new Object[] { Constants.OPTION_IS_YES, "是" });
		o4.add(new Object[] { Constants.OPTION_IS_NO, "否" });
		Radio isUseable4 = new Radio("isuse" + num, "是否启用", Utils.notEmpty(map
				.get("isuse")) ? map.get("isuse") : Constants.OPTION_IS_NO, o4);
		isUseable4.setStyleClass("thresholed" + num);
		list.add(isUseable4);

		// 通知方式
		StringBuilder adminType = new StringBuilder();
		String adminEmailContent = "";
		String adminPhoneNum = "";
		if (Utils.notEmpty(adminEmailContent)) {
			adminType.append("email" + num).append(",");
		} else {
			adminEmailContent = "";
		}
		if (Utils.notEmpty(adminPhoneNum)) {
			adminType.append("phone" + num).append(",");
		} else {
			adminPhoneNum = "";
		}
		Checkboxgroup admincheckboxgroup = new Checkboxgroup("checkboxgroup"
				+ num, "通知方式", new Object[] { adminType.toString() },
				new Object[] { "email" + num, "phone" + num });
		// admincheckboxgroup.setStyleClass("thresholed"+num);
		// 创建options对象数组

		Text adminEMail = new Text("email" + num, "E-Mail", adminEmailContent,
				-1);
		adminEMail.setStyleClass("thresholed" + num);
		admincheckboxgroup.add(adminEMail);

		Text adminPhone = new Text("ohone" + num, "电话", adminPhoneNum, -1);
		adminPhone.setStyleClass("thresholed" + num);
		admincheckboxgroup.add(adminPhone);
		list.add(admincheckboxgroup);
		Label label = new Label("label" + num, "操作", "删除");
		label.setFullLine(false);
		label.setOn(EventTarget.EVENT_CLICK, "VM(this).cmd('deleteThreshold',"
				+ num + ")");
		list.add(label);
		// 添加管理员snmp监控数据的阀值---------end
		return list;
	}

	public static View buildThresholdView() {
		BaseView view = new BaseView();
		view.add(new SubmitCommand("saveThreshold",
				"subproject.sp?act=saveThreshold", "thresholdForm", true));
		VerticalPanel vp = new VerticalPanel("threshold_vp", "85%,*");
		FormPanel form = new FormPanel("thresholdForm");
		
		Text name = new Text("name", "阀值模板名称", "", 500);
		name.setGrayTip("如：模板1");
		form.add(name);
		name.setNotnull(true);
		
		Text cpu = new Text("cpu", "cpu使用预警（百分比%）", "", 500);
		cpu.setGrayTip("如：80");
		form.add(cpu);

		Text memory = new Text("memory", "系统内存使用预警（百分比%）",
				"", 500);
		memory.setGrayTip("如：80");
		form.add(memory);

		Text jvmMemory = new Text("jvmMemory", "JVM内存使用预警（百分比%）",
				"", 500);
		jvmMemory.setGrayTip("如：80");
		form.add(jvmMemory);

		Text jvmThread = new Text("jvmThread", "JVM线程使用预警（数量）",
				"", 500);
		jvmThread.setGrayTip("如：100");
		form.add(jvmThread);

//		Text io = new Text("io", "网络流量（Kb/s）","", 500);
//		io.setGrayTip("如：1024");
//		form.add(io);
		
		Text diskUsage = new Text("diskUsage", "磁盘使用率（百分比）","", 500);
		diskUsage.setGrayTip("如：80");
		form.add(diskUsage);
		
		Text systemProcess = new Text("systemProcess", "系统线程数","", 500);
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
				new Object[] { "" }, o1);
		form.add(adminRetry);
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
		bbp.addButton(save);
		bbp.addButton(close);
		vp.addSubPanel(bbp);
		
		view.setRootPanel(vp);
		return view;
	}
	
	public static View buildHttpThresholdView() {
		BaseView view = new BaseView();
		view.add(new SubmitCommand("saveThreshold",
				"subproject.sp?act=saveHttpThreshold", "thresholdForm", true));
		VerticalPanel vp = new VerticalPanel("threshold_vp", "85%,*");
		FormPanel form = new FormPanel("thresholdForm");
		
		Text name = new Text("name", "阀值模板名称", "", 500);
		name.setGrayTip("如：模板1");
		form.add(name);
		name.setNotnull(true);
		
		Text responseTime = new Text("responseTime", "响应时间(ms)", "", 500);
		responseTime.setGrayTip("如：5000");
		form.add(responseTime);
		List<Object[]> o1 = new ArrayList<Object[]>();
		o1.add(new Object[] { "1", "1次" });
		o1.add(new Object[] { "2", "2次" });
		o1.add(new Object[] { "3", "3次" });
		o1.add(new Object[] { "3", "4次" });
		o1.add(new Object[] { "3", "5次" });
		Select adminRetry = new Select("retry", "重试几次后告警",
				new Object[] { "" }, o1);
		form.add(adminRetry);
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
		bbp.addButton(save);
		bbp.addButton(close);
		vp.addSubPanel(bbp);
		
		view.setRootPanel(vp);
		return view;
	}

	public static CommandGroup updateSnmpView4AddEdit(Locale loc,
			ViewFactory viewFactory, Subproject subproject,
			List<Monitortype> typeList, List<Threshold> listThreshold,
			List<Project> listProject) {
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucForm = new UpdateCommand("uc_form"); // 更新FormPanel命令
		UpdateCommand ucBtn = new UpdateCommand("uc_btn"); // 更新按钮部分
		cg.add(ucForm).add(ucBtn);
		FormPanel form = viewFactory.getDefaultFormPanel();
		ucForm.setContent(form);
		fillSnmpFormPanel(loc, viewFactory, form, subproject, typeList,
				listThreshold, listProject);
		ButtonBarPanel bbp = new ButtonBarPanel("bbp");
		bbp.setId(ViewFactory.ID_PANEL_BUTTON_BAR);
		ucBtn.setContent(bbp);
		fillButtonPanel(loc, viewFactory, bbp, false, subproject.getType());
		return cg;
	}

	public static Command updateSnmpView4Save(Locale loc,
			ViewFactory viewFactory, Subproject subproject, Map<Project, List<Subproject>> mapDatas, Page page,
			List<Monitortype> typeList, List<Threshold> listThreshold,
			List<Project> listProject) {
		// 序列命令初始化，用于有顺序的组装一些执行的命令
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucGrid = new UpdateCommand("uc_grid"); // 更新GridPanel命令
		UpdateCommand ucPage = new UpdateCommand("uc_page"); // 更新PagePanel命令
		UpdateCommand ucPageCmd = new UpdateCommand("uc_page_cmd");// 更新PageCommand命令
		cg.add(ucGrid)
				.add(ucPage)
				.add(ucPageCmd)
				.add(new JSCommand("", "VM(this).find('grid').focus({id:'"
						+ subproject.getId() + "'});"));
		// 更新GridPanel
		GroupingGridPanel grid = new GroupingGridPanel("grid");
		grid.setId("grid");
		fillGridPanel(loc, ucGrid.getView(), viewFactory, grid, mapDatas);
		ucGrid.getView().addSubPanel(grid);

		// 更新PagePanel
		AjaxCommand cpage = null;
		cpage = new AjaxCommand(
							viewFactory.getId(LogicComponent.CMD_PAGE),
							"subproject.sp?act=turnPage&cp=$0");
		PagePanel pagePanel = new PagePanel(
				viewFactory.getId(LogicComponent.PANEL_PAGE));
		fillPagePanel(viewFactory, pagePanel, page);
		pagePanel.setClickCommand(cpage);
		ucPage.setContent(pagePanel);
		ucPageCmd.setContent(cpage);

		// 更新FormPanel
		CommandGroup uc2 = (CommandGroup) updateSnmpView4AddEdit(loc,viewFactory, subproject,
				 typeList,  listThreshold,listProject);
		cg.add(uc2);

		return cg;
	}
}
