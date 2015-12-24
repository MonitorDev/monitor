package com.rongji.websiteMonitor.webapp.system.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.TreeItem;
import com.rongji.dfish.engines.xmltmpl.component.TreePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyle;
import com.rongji.websiteMonitor.tool.MonitorStyle;
import com.rongji.websiteMonitor.tool.ViewlistView;

public class MonitorView {
	
	
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
			String userId,ItaskStyle style,HttpServletRequest request) {
		BaseView view = MonitorStyle.buildIndexView(request);
		VerticalPanel menuContent = new VerticalPanel(MonitorStyle.MENU_COUNT_ID, "*");
		menuContent.addSubPanel(getMenuPanel(""));
		view.replacePanelById(menuContent);
		SourcePanel sfmain = new SourcePanel(MonitorStyle.RIGHT_PNALE_ID,
				"vm:|MonitorPointMgr.sp?act=index&type=index&folder=$0");
		view.replacePanelById(sfmain);
		fillPerformCommand(view,"last");
		return view;
	}
	
	public static void fillPerformCommand(BaseView rootView, String type) {
		AjaxCommand navigaClick = new AjaxCommand("navigaClick", "perform.sp?act=navigaClick");
		AjaxCommand monitoryPoint = new AjaxCommand("monitoryPoint", "MonitorPointMgr.sp?act=index");
		AjaxCommand monitoryType = new AjaxCommand("monitoryType", "MonitorTypeMgr.sp?act=index");
		AjaxCommand scheduler = new AjaxCommand("scheduler", "scheduler.sp?act=index");
		AjaxCommand TaskMgr = new AjaxCommand("TaskMgr", "TaskMgr.sp?act=index");
		AjaxCommand monitorDetailMgr = new AjaxCommand("monitorCenter", "monitorDetailMgr.sp?act=index");
		AjaxCommand project = new AjaxCommand("project", "project.sp?act=index");
		AjaxCommand subproject = new AjaxCommand("subproject", "subproject.sp?act=index");
		AjaxCommand test = new AjaxCommand("test", "test.sp?act=index");
		AjaxCommand option = new AjaxCommand("option", "option.sp?act=index");
		//AjaxCommand clientScheduler = new AjaxCommand("clientScheduler", "clientScheduler.sp?act=index&folder=$0");
		rootView.addCommand(navigaClick).add(monitoryPoint).add(monitoryType).add(scheduler).add(TaskMgr).add(monitorDetailMgr).add(test).add(project).add(subproject).add(option);
	}
	
	public static Panel getMenuPanel(String folder) {
		TreePanel treePanel = new TreePanel("left_btn");
		treePanel.addTreeItem(new TreeItem(null, "监测点管理", null,"VM(this).cmd('monitoryPoint')"," ","",""));
		treePanel.addTreeItem(new TreeItem(null, "监控类别管理", null,"VM(this).cmd('monitoryType')"," ","",""));
		treePanel.addTreeItem(new TreeItem(null, "项目管理", null,"VM(this).cmd('project')"," ","",""));
		treePanel.addTreeItem(new TreeItem(null, "监控服务管理", null,"VM(this).cmd('subproject')"," ","",""));
		treePanel.addTreeItem(new TreeItem(null, "监控中心", null,"VM(this).cmd('monitorCenter')"," ","",""));
		treePanel.addTreeItem(new TreeItem(null, "设置", null,"VM(this).cmd('option')"," ","",""));
		treePanel.addTreeItem(new TreeItem(null, "测试", null,"VM(this).cmd('test')"," ","",""));
//		ButtonBarPanel bbp = new ButtonBarPanel("left_btn");
//		bbp.setFace(ButtonFace.start_menu);
////		bbp.setDirectionVertical(false);
//		ClickButton monitorPointManager = new ClickButton(null, "监测点管理",
//				"VM(this).cmd('monitoryPoint','0');");
//		ClickButton monitorTypeManager = new ClickButton(null, "监控类别管理",
//				"VM(this).cmd('monitoryType','1');");
//		ClickButton projectManager = new ClickButton(null, "项目管理",
//				"VM(this).cmd('project','6');");
//		ClickButton subprojectManager = new ClickButton(null, "监控服务管理",
//				"VM(this).cmd('subproject','7');");
//		ClickButton monitorManager = new ClickButton(null, "监控项目管理",
//				"VM(this).cmd('TaskMgr','2');");
////		ClickButton monitorTaskManager = new ClickButton(null, "定时监控管理",
////				"VM(this).cmd('scheduler','3');");
//		ClickButton monitorOption = new ClickButton(null, "设置",
//				"VM(this).cmd('option','8');");
//		ClickButton monitorCenterManager = new ClickButton(null, "监控中心",
//				"VM(this).cmd('monitorDetailMgr','4');");
//		ClickButton test = new ClickButton(null, "测试",
//				"VM(this).cmd('test','5');");
//		if ("1".equals(folder)) {
//			monitorTypeManager.setFocus(true);
//		} else if ("2".equals(folder)) {
//			monitorManager.setFocus(true);
//		} 
////		else if ("3".equals(folder)) {
////			monitorTaskManager.setFocus(true);
////		} 
//		else if ("4".equals(folder)) {
//			monitorCenterManager.setFocus(true);
//		}  else if ("5".equals(folder)) {
//			test.setFocus(true);
//		}else if ("0".equals(folder)) {
//			monitorPointManager.setFocus(true);
//		}else if ("6".equals(projectManager)) {
//			projectManager.setFocus(true);
//		}else if ("7".equals(subprojectManager)) {
//			subprojectManager.setFocus(true);
//		}else if ("8".equals(subprojectManager)) {
//			monitorOption.setFocus(true);
//		}else {
//			monitorPointManager.setFocus(true);
//		}
//
//		bbp.addButton(monitorPointManager);
//		bbp.addButton(monitorTypeManager);
//		bbp.addButton(projectManager);
//		bbp.addButton(subprojectManager);
////		bbp.addButton(monitorManager);
////		bbp.addButton(monitorTaskManager);
//		bbp.addButton(monitorCenterManager);
//		bbp.addButton(monitorOption);
//		bbp.addButton(test);
		return treePanel;
	}

}