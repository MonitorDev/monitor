package com.rongji.websiteMonitor.webapp.system.view;

import java.util.Locale;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyle;
import com.rongji.websiteMonitor.tool.ViewlistView;


/**
 *系统首页视图界面构建
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司 </p>
 * 
 * @author  HQJ
 * @version 1.0
 * @since   1.0.0 HQJ 2009-11-27
 */
public class IndexView {
	
	/**
	 * 构建系统首页视图
	 * 
	 * @param loc
	 * @param viewFactory
	 * @param userId
	 * @param stackModule
	 * @param treeItemPK
	 * @param mainSrc
	 * @return
	 * @throws Exception
	 */
	public static BaseView buildIndexView(Locale loc, ViewFactory viewFactory,
			String userId,ItaskStyle style) {
		BaseView view = new BaseView();
		
		HorizontalPanel shell = new HorizontalPanel("", style.getNavWidth()
				+ "," + style.getOutterPadding() + ",*");
		VerticalPanel leftPanel = new VerticalPanel("leftPanel", "*");
		leftPanel.setStyle("padding-top:18px");
		leftPanel.setStyleClass(style.getNavBorderClass());
		HtmlPanel resizeBar = new HtmlPanel("f_rz", "");// 分隔栏，可以用于改变窗口大小
		resizeBar.setHorizontalResize(style.getNavWidth()+",500");
		resizeBar.setStyleClass(style.getOutterPaddingClass());
		VerticalPanel menuContent = new VerticalPanel("menuContent", "*");
		menuContent.addSubPanel(getMenuPanel("0"));
		leftPanel.addSubPanel(menuContent);
		shell.addSubPanel(leftPanel, resizeBar, HtmlPanel.EMPTY);
		view.addSubPanel(shell);
		fillPerformCommand(view,"last");
		
		
		
//		DockPanel dock = new DockPanel("root");
//		Panel top = createTopPanel(loc, viewFactory);
//		//dock.addTop(top, "50"); // 顶部标题栏占50像素高
//
//		Panel left = createLeftPanel(view, viewFactory, userId);// 左边菜单栏
//		dock.addLeft(left, "220");
//
//		HtmlPanel resizeBar = new HtmlPanel("f_rz", "");// 分隔栏，可以用于改变窗口大小
//		resizeBar.setHorizontalResize("0,280");
//		resizeBar.setStyleClass("bg-lower");
//		resizeBar.setHtml("<table cellspacing=0 cellpadding=0 height=100% width=100%><tr><td>" +
//				"<div class=bg-clp-l onmousedown=DFish.collpaseLeftPanel(this)></div>" +
//				"</td></tr></table>");
//		dock.addLeft(resizeBar, "4");
//
//		// 右边主窗口
//		SourcePanel sfmain = new SourcePanel("m-main",
//				"webapp/m/intro.html");
//		dock.setCenter(sfmain);
//
////		view.addDialogTemplate(viewFactory
////				.getDialogTemplate(LogicComponent.DIALOG_STANDARD));
//		view.setRootPanel(dock);
//		DialogTemplate tmplNone=new BaseDialogTemplate("none");
//		SourcePanel sp=new SourcePanel("","$src");
//		sp.setStyleClass("bg-white");
//		tmplNone.addSubPanel(sp);
//		//view.addDialogTemplate(tmplNone);
//        //选择页面风格
//		//view.add(dc);
		view.addDialogTemplate(ViewlistView.getDialogTemplate("f_std"));
		view.addDialogTemplate(ViewlistView.getDialogTemplate("f_std_x"));
		return view;
	}
	
	public static void fillPerformCommand(BaseView rootView, String type) {
		AjaxCommand navigaClick = new AjaxCommand("navigaClick", "perform.sp?act=navigaClick&folder=$0");
		rootView.addCommand(navigaClick);
	}
	
	public static ButtonBarPanel getMenuPanel(String folder) {
		ButtonBarPanel bbp = new ButtonBarPanel("left_btn");
		bbp.setFace(ButtonFace.start_menu);
		ClickButton monitorPointManager = new ClickButton(null, "监测点管理",
				"VM(this).cmd('navigaClick','0');");
		ClickButton monitorTypeManager = new ClickButton(null, "监控类别管理",
				"VM(this).cmd('navigaClick','1');");
		ClickButton monitorManager = new ClickButton(null, "监控项目管理",
				"VM(this).cmd('navigaClick','2');");
		ClickButton monitorTaskManager = new ClickButton(null, "定时监控管理",
				"VM(this).cmd('navigaClick','3');");
		ClickButton monitorCenterManager = new ClickButton(null, "监控中心",
				"VM(this).cmd('navigaClick','4');");
		ClickButton test = new ClickButton(null, "测试",
				"VM(this).cmd('navigaClick','5');");
		if ("1".equals(folder)) {
			monitorTypeManager.setFocus(true);
		} else if ("2".equals(folder)) {
			monitorManager.setFocus(true);
		} else if ("3".equals(folder)) {
			monitorTaskManager.setFocus(true);
		} else if ("4".equals(folder)) {
			monitorCenterManager.setFocus(true);
		}  else if ("5".equals(folder)) {
			test.setFocus(true);
		}else {
			monitorPointManager.setFocus(true);
		}

		bbp.addButton(monitorPointManager);
		bbp.addButton(monitorTypeManager);
		bbp.addButton(monitorManager);
		bbp.addButton(monitorTaskManager);
		bbp.addButton(monitorCenterManager);
		bbp.addButton(test);
		return bbp;
	}

}