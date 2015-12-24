package com.rongji.websiteMonitor.tool;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyle;
import com.rongji.websiteMonitor.platform.ums.style.ItaskStyleManager;

public class MonitorStyle {

	public static final String MENU_COUNT_ID = "menuContent";
	public static final String RIGHT_PNALE_ID = "m-main";
	public static final String RIGHT_BUTTONBAR_ID = "right_buttonBar";
	public static final String RIGHT_TOP_ID = "right_top";
	public static final String RIGHT_BOTTOM_ID = "right_bottom";
	public static final String RIGHT_TITLE_ID = "right_title";
	
	/**
	 * 创建左右布局的view
	 * @param request
	 * @return
	 */
	public static BaseView buildIndexView(HttpServletRequest request) {
		BaseView view = new BaseView();
		ItaskStyle style = ItaskStyleManager.getStyle(request);
		HorizontalPanel shell = new HorizontalPanel("", style.getNavWidth()
				+ "," + style.getOutterPadding() + ",*");
		VerticalPanel leftPanel = new VerticalPanel("leftPanel", "*");
		leftPanel.setStyle("padding-top:18px");
		leftPanel.setStyleClass(style.getNavBorderClass());
		HtmlPanel resizeBar = new HtmlPanel("f_rz", "");// 分隔栏，可以用于改变窗口大小
		resizeBar.setHorizontalResize(style.getNavWidth()+",500");
		resizeBar.setStyleClass(style.getOutterPaddingClass());
		VerticalPanel menuContent = new VerticalPanel(MENU_COUNT_ID, "*");
		menuContent.addSubPanel(HtmlPanel.EMPTY);
		leftPanel.addSubPanel(menuContent);
		VerticalPanel rightPanel = new VerticalPanel(RIGHT_PNALE_ID,"*");
		rightPanel.addSubPanel(HtmlPanel.EMPTY);
		shell.addSubPanel(leftPanel, resizeBar, rightPanel);
		view.addSubPanel(shell);
		view.addDialogTemplate(ViewlistView.getDialogTemplate("f_std"));
		view.addDialogTemplate(ViewlistView.getDialogTemplate("f_std_x"));
		return view;
	}
	
	/**
	 * 创建右边的面板
	 * @param titleName 面板的名称
	 * @param buttonBarPanel  面板右上侧的按钮组
	 * @param rightBottomPanel  面板的主体panel
	 * @return
	 */
	public static VerticalPanel buildRightPanel(String titleName, ButtonBarPanel buttonBarPanel, Panel rightBottomPanel) {
		VerticalPanel right = new VerticalPanel(RIGHT_PNALE_ID, "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(RIGHT_TITLE_ID, titleName);
		HorizontalPanel title = new HorizontalPanel(RIGHT_TOP_ID, "*,1,700");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		buttonBarPanel.setFace(ButtonFace.group);
		buttonBarPanel.setAlign(Align.right);
		title.addSubPanel(titleHtml,HtmlPanel.EMPTY,buttonBarPanel);
		rightBottomPanel.setStyle("margin:0 30px");
		right.addSubPanel(title, rightBottomPanel);
		return right;
	}

}
