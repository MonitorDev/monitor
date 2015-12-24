package com.rongji.websiteMonitor.webapp.monitorType.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Button;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.HighlightStyle;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.ConfirmCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.command.jsdoc.JSCmdLib;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FlowPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridColumn;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanelPubInfo;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Combobox;
import com.rongji.dfish.engines.xmltmpl.form.Hidden;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.engines.xmltmpl.form.Radio;
import com.rongji.dfish.engines.xmltmpl.form.Spinner;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;

public class MonitorTypeView {

	public static BaseView buildIndexView(Locale loc, ViewFactory viewFactory,
			List<Monitortype> dataList, Page page) {
		
		BaseView view = new BaseView();
		VerticalPanel right = new VerticalPanel("m-main", "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(null, "监测类型管理");
		HorizontalPanel title = new HorizontalPanel("f_title", "*,1,700");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(ViewFactory.ID_PANEL_BUTTON_BAR);
		buttonBarPanel.setFace(ButtonFace.group);
		buttonBarPanel.setAlign(Align.right);
		fillButtonPanel(loc, viewFactory, buttonBarPanel,true);
		title.addSubPanel(titleHtml,HtmlPanel.EMPTY,buttonBarPanel);
		
		HorizontalPanel vp = new HorizontalPanel(null, "400,*");
		vp.setStyle("margin:0 30px");
		GridPanel grid = viewFactory.getDefaultGridPanel();
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
			ButtonBarPanel bbp, boolean isDisable) {
		bbp.setAlign(Align.right);
		bbp.setFace(ButtonFace.group);
		Button add = new ClickButton(null,"新建","VM(this).cmd('add')");
		ClickButton save = new ClickButton(null,"保存","VM(this).cmd('save')");
		if(isDisable){
			save.setDisabled(true);
		}else{
			save.setDisabled(false);
		}
//		Button search = new ClickButton("img/b/new.gif","查询","VM(this).cmd('onSearch')");
		Button delete = new ClickButton(null,"删除","VM(this).cmd('onDelete')");
		Button reflesh = new ClickButton(null,"刷新","VM(this).cmd('refresh')");
		bbp.addButton(add).addButton(save).addButton(delete).addButton(reflesh);
		
	}

	private static void fillCommand(BaseView view) {
		String panelToSubmit = ViewFactory.ID_PANEL_FORM;
		
		view.add(new AjaxCommand("add", "MonitorTypeMgr.sp?act=edit" ));
		view.add(new SubmitCommand("save", "MonitorTypeMgr.sp?act=save" , panelToSubmit, true));
//		view.add(new JSCommand("onSave","VM( this ).ext( 'authorName', VM(this).f('authorName',DFish.GET_VALUE));VM( this ).ext( 'stId', VM(this).f('stId',DFish.GET_VALUE));VM(this).cmd('save',VM( this ).ext( 'authorName' ),VM( this ).ext( 'stId' ))"));
//		view.add(new JSCommand("onSearch","var theH='0';if(VM(this).find('f_grid').g_sep().charAt(0)=='0')theH='71';VM(this).find('f_grid').reset(theH+',*');"));
		view.add(new JSCommand("onDelete","var sel=VM(this).f('selectItem',DFish.GET_VALUE);"
				+"VM( this ).ext( 'monitorPointName', VM(this).f('monitorPointName',DFish.GET_VALUE));"
				+"VM(this).ext('cp',VM( this ).find( 'f_page' ).attr( 'currentPage' ));"
				+ "if(sel&&sel.split(',').length>=1){"
				+ "VM(this).cmd('del_cfm',sel)"
				+ "}else if(VM(this)._selRow){"
				+ "VM(this).cmd('del_cfm',VM(this)._selRow)" + "}else{"
				+ "VM(this).cmd({ tagName : 'alert', cn : '请选择要删除的行!',tm : 3 });" 
				+ "}"));
		Command delCmd = new SubmitCommand("delCmd", "MonitorTypeMgr.sp?act=delete", "grid", true);
		Command delCfm = new ConfirmCommand("del_cfm", "确定要删除选中的行吗？", delCmd);
		view.add(delCfm).add(delCmd);
		view.add(JSCmdLib.reload("refresh", null));
		view.add(new AjaxCommand("page", "MonitorTypeMgr.sp?act=turnPage&cp=$0"));
		
		
		
	}


	private static void fillGridPanel(Locale loc, BaseView view,
			ViewFactory viewFactory, GridPanel grid,
			List<Monitortype> dataList) {
		Collection<Object[]> col = new ArrayList<Object[]>();
		if(Utils.notEmpty(dataList)){
			for(Monitortype point:dataList){
				col.add(new Object[] { point.getMtId(),point.getMtName()});
			}
		}
		grid.pub().setHeaderClass("bg-mid");  
		grid.setFace(GridPanelPubInfo.FACE_LINE);
		grid.setRowHeight(40);
		grid.setScroll(Scroll.miniscroll);
		grid.setData(col);
		grid.addHiddenColumn(0, "mpId");
		grid.addSelectitem("mpId", "40");
		grid.addTextColumn(1, "mpName", "监测类型名称", "*", "mpName");
		grid.setNobr(true);
		grid.addClickAttach("VM(this).cmd('clkRow','$mpId')");
		view.addCommand(new AjaxCommand("clkRow", "MonitorTypeMgr.sp?act=edit&mpId=$0"));
		
	}


	private static void fillPagePanel(ViewFactory viewFactory,
			PagePanel pagePanel, Page page) {
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


	private static void fillIndexFormPanel(FormPanel form) {
		String html = "<div style=\"padding:24px;line-height:160%\">"
//			+ "<p style=\"text-align:left;font-size:12pt;font-weight:bold\">监测类型管理：</p>"
			+ "<p>主要是提供对监控类别的管理。监控类别管理具有以下特点：<br/>" + "<ul>"
			+ "<li>可以点击上面的按钮进行增删改。</li>"
			+ "<li>监控类别的名称不允许重复的。</li>"
			+ "</ul>" + "</div>";
		form.setScroll(Scroll.auto);
		form.setStyleClass("introduction_bg");
		form.addHtml(null, true, html);
		
	}


	private static void fillSearchFormPanel(Locale loc, BaseView view,
			ViewFactory viewFactory, VerticalPanel vpSearch) {
		vpSearch.setStyleClass("bd-deep");
		FormPanel searchForm = new FormPanel("searchForm");
		ButtonBarPanel bbp=new ButtonBarPanel("bbp1");
		vpSearch.addSubPanel(searchForm,bbp);
		searchForm.setScroll(Scroll.hidden);

		searchForm.add(new Text("monitorPointName", "监测类型名称", null, 500));

		bbp.setStyleClass(viewFactory.getStyleClass(LogicComponent.PANEL_FORM));
		bbp.setCellspacing(12);
		bbp.setStyle("text-align:right");
		bbp.setFace(ButtonFace.classic);
		view.add(new SubmitCommand("search", "MonitorTypeMgr.sp?act=doSearch","searchForm", false));
		String resetJs = "VM(this).f('monitorPointName',DFish.SET_VALUE,'');";
		Button buttonOk = new ClickButton("", "确定", "VM(this).cmd('search')");
		Button buttonReset = new ClickButton("", "重置", resetJs);
		Button buttonClose = new ClickButton("", "关闭", "VM(this).find('f_grid').reset('0,*');");
		bbp.addButton(buttonOk).addButton(buttonReset).addButton(buttonClose);
		
	}


	private static void fillTitle(HtmlPanel titlePanel) {

		titlePanel.setHtml("监测类型管理");
		
	}
	
	/**
	 * 执行新建编辑更新
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param catalogRelation
	 * @param caId
	 * @return
	 */
	public static CommandGroup updateView4AddEdit(Locale loc,
			ViewFactory viewFactory, Monitortype point) {
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucForm = new UpdateCommand("uc_form"); //更新FormPanel命令
		UpdateCommand ucBtn = new UpdateCommand("uc_btn"); //更新按钮部分
		cg.add(ucForm).add(ucBtn);
		FormPanel form = viewFactory.getDefaultFormPanel();
		ucForm.setContent(form);
		fillFormPanel(loc, viewFactory, form, point);
		ButtonBarPanel bbp = new ButtonBarPanel("bbp");
		bbp.setId(ViewFactory.ID_PANEL_BUTTON_BAR);
		ucBtn.setContent(bbp);
		fillButtonPanel(loc, viewFactory, bbp,false);
//		ClickButton saveBtn = new ClickButton("img/b/save.gif", "保存", "VM(this).cmd('onSave')");
//		saveBtn.setId("save_btn");
		
		return cg;
	}
	
	/**
	 * 创建表单面板
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param view
	 * @param form
	 * @param source
	 * @param viewType
	 */
	private static void fillFormPanel(Locale loc, ViewFactory viewFactory,
			FormPanel form, Monitortype point) {
		if (point == null) {
			point = new Monitortype();
		}
		form.setScroll(Scroll.auto);
		if (point != null && Utils.notEmpty(point.getMtId())) {
			form.add(new Hidden("mpId", point.getMtId()));
		}
		Text mpName = new Text("mpName", "监测类型名称", point.getMtName(), 100, true, false, false, null, null);
		form.add(mpName);
		Text mpAlias = new Text("mpAlias", "监测类型别名", point.getMtAlias(), 100, true, false, false, null, null);
		form.add(mpAlias);
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
			ViewFactory viewFactory, List<Monitortype> dataList, Page page,
			Monitortype point, String monitorPointName) {
		// 序列命令初始化，用于有顺序的组装一些执行的命令
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand ucSearchForm = new UpdateCommand("uc_search_form");        // 更新ButtonPanel命令
		UpdateCommand ucGrid = new UpdateCommand("uc_grid");      // 更新GridPanel命令
		UpdateCommand ucPage = new UpdateCommand("uc_page");      // 更新PagePanel命令
		UpdateCommand ucPageCmd = new UpdateCommand("uc_page_cmd");//更新PageCommand命令
		cg.add(ucGrid).add(ucPage).add(ucPageCmd).add(new JSCommand("", "VM(this).find('grid').focus({mpId:'"+point.getMtId()+"'});"));
	
		
		//更新searchFormPanel
		VerticalPanel vpSearch = new VerticalPanel("vpSearch","20,*,40");
		fillSearchFormPanel(loc, ucSearchForm.getView(), viewFactory, vpSearch);
		ucSearchForm.getView().addSubPanel(vpSearch);
		
		// 更新GridPanel
		GridPanel grid = viewFactory.getDefaultGridPanel();
		grid.setId("grid");
		fillGridPanel(loc, ucGrid.getView(), viewFactory, grid, dataList);
		ucGrid.getView().addSubPanel(grid); 
		
		// 更新PagePanel
		AjaxCommand cpage = null;
		if(Utils.isEmpty(monitorPointName)){
			cpage = new AjaxCommand(viewFactory.getId(LogicComponent.CMD_PAGE), "MonitorTypeMgr.sp?act=turnPage&cp=$0");
		}else{
			cpage = new AjaxCommand(viewFactory.getId(LogicComponent.CMD_PAGE), "MonitorTypeMgr.sp?act=turnPage&cp=$0&mpName="+Utils.URLEncoder(monitorPointName));
		}
		cpage = new AjaxCommand(viewFactory.getId(LogicComponent.CMD_PAGE), "MonitorTypeMgr.sp?act=turnPage&cp=$0");
		PagePanel pagePanel = new PagePanel(viewFactory.getId(LogicComponent.PANEL_PAGE));
		fillPagePanel(viewFactory, pagePanel, page);
		pagePanel.setClickCommand(cpage);
		ucPage.setContent(pagePanel);
		ucPageCmd.setContent(cpage);
		
		// 更新FormPanel
		CommandGroup uc2 = (CommandGroup) updateView4AddEdit(loc, viewFactory,point);
		cg.add(uc2);
		
		return cg;
	}
	public static Command updateView4Delete(Locale loc,
			ViewFactory viewFactory, List <Monitortype> dataList, Page page,
			Monitortype point, String monitorPointName) {
		return updateView4Save(loc, viewFactory, dataList, page, point,monitorPointName);
	}

}
