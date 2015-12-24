package com.rongji.websiteMonitor.platform.ums.style;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseDialogTemplate;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.DialogTemplate;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;


public class DfishStyle {
	public static int getButtonBarHeight() {
		return 57;
	}

	public static int getOutterPadding() {
		return 1;
	}

	public static String getOutterPaddingClass() {
		return "bg-gray";
	}

	public static String getNavBorderClass() {
		return "";
	}

	public static String getNavTitleClass() {
		return "tt-nav";
	}

	public static int getNavTitleHeight() {
		return 36;
	}

	public static String getMainBorderClass() {
		return "";
	}

	public static String getMainTitleClass() {
		return "tt-main";
	}

	public static int getMainTitleHeight() {
		return 57;
	}

	public static int getPagePanelHeight() {
		return 26;
	}
	public static String getNavWidth() {
		return "230";
	}
	
	public static BaseView getPopupFormView() {
		BaseView view =new BaseView();
		VerticalPanel root=new VerticalPanel("f_root","*,51");
//		
		view.setRootPanel(root); 
		FormPanel form=new FormPanel("f_form");
		form.setStyle("padding:12px 16px 0 16px");
		form.setScroll(Scroll.miniscroll);
		root.addSubPanel(form);
	    ButtonBarPanel bbp=new ButtonBarPanel("f_btn");
		bbp.setAlign(Align.right);
	    bbp.setFace(ButtonFace.office);
	    bbp.setStyleClass("d-bg bd-gray bd-onlytop d-foot");
	    bbp.setStyle("padding:0 35px");
//	    bbp.setVerticalMinus(28);
	    root.addSubPanel(bbp);
		return view;
	}
	
	public static BaseView getView() {
		BaseView view = new BaseView();
		view.addDialogTemplate(DfishStyle.getDialogTemplate("f_std"));
		HorizontalPanel top = new HorizontalPanel("top", "128,*");// 顶部标题栏
		VerticalPanel root = new VerticalPanel("f_root", "48,"+DfishStyle.getButtonBarHeight()+",*");
		root.setStyle("background:white");
		//root.setStyleClass("bg-form");
		HorizontalPanel title = new HorizontalPanel("f_title", "*,520");
		title.setStyle("padding-left:12px;padding-right:12px;");
		HorizontalPanel main = new HorizontalPanel("f_main", "*,1,*");
		main.setStyle("padding-left:12px;padding-right:12px;");
		VerticalPanel leftPanel = new VerticalPanel("f_left", "*");
//		leftPanel.setStyle("width:189px");
		HtmlPanel content = new HtmlPanel(
				"f_content",
				null);
		HtmlPanel resizeBar = new HtmlPanel("f_rz", "");// 分隔栏，可以用于改变窗口大小
		resizeBar.setHorizontalResize("76,520");
		resizeBar.setHtml("<table cellspacing=0 cellpadding=0 height=100% width=100%><tr><td>" +
				"<div  onmousedown=DFish.collpaseLeftPanel(this)></div>" +
				"</td></tr></table>");
		resizeBar.setStyleClass(DfishStyle.getOutterPaddingClass());
		main.addSubPanel(leftPanel, resizeBar, content);
		root.addSubPanel(top, title,main);
		view.setRootPanel(root);
		return view;
	}
	
	public static DialogTemplate getDialogTemplate(String id) {
		if("f_std".equals(id)){
			return TMPL_STD;
		}else if("f_std_mx".equals(id)){
			return TMPL_MAX;
		}else if("f_std_x".equals(id)){
			return TMPL_CLOSE;
		}
		
		return null;
	}
	private static DialogTemplate TMPL_STD=new BaseDialogTemplate("f_std");
	private static DialogTemplate TMPL_MAX=new BaseDialogTemplate("f_std_mx");
	private static DialogTemplate TMPL_CLOSE=new BaseDialogTemplate("f_std_x");
	static{
		SourcePanel CONTENT=new SourcePanel("dlg-src","");
		CONTENT.setStyleClass("d-bd white");
		CONTENT.setHorizontalMinus(2);
		HtmlPanel DLG_TOP_LEFT=new HtmlPanel(null,null);
		DLG_TOP_LEFT.setStyleClass("d-tl");
		HtmlPanel DLG_TITLE=new HtmlPanel("dlg-tt",null);
		DLG_TITLE.setStyleClass("d-tt");
		HtmlPanel DLG_MAX_BTN=new HtmlPanel("dlg-max",null);
		DLG_MAX_BTN.setStyleClass("d-b");
		HtmlPanel DLG_CLOSE_BTN=new HtmlPanel("dlg-x",null);
		DLG_CLOSE_BTN.setStyleClass("d-b");
		HtmlPanel DLG_TOP_RIGHT=new HtmlPanel(null,null);
		DLG_TOP_RIGHT.setStyleClass("d-tr");
		HtmlPanel DLG_BOTTOM_LEFT=new HtmlPanel(null,null);
		DLG_BOTTOM_LEFT.setStyleClass("d-bl");
		HtmlPanel DLG_BOTTOM_RIGHT=new HtmlPanel(null,null);
		DLG_BOTTOM_RIGHT.setStyleClass("d-br");
		HtmlPanel DLG_BOTTOM_CENTER=new HtmlPanel(null,null);
		DLG_BOTTOM_CENTER.setStyleClass("d-bc");
		
		VerticalPanel topCenter = new VerticalPanel(null,"*");
		topCenter.setStyleClass("d-tc");
		HorizontalPanel topTitle = new HorizontalPanel(null,"*,27,27");
		topTitle.setStyleClass("d-tc-r");
		topCenter.addSubPanel(topTitle);
		topTitle.addSubPanel(DLG_TITLE,DLG_MAX_BTN,DLG_CLOSE_BTN);
		
		
		VerticalPanel stdRoot=new VerticalPanel(null,"42,*,2");
		stdRoot.setStyleClass("dlg-std");
		VerticalPanel stdTop = new VerticalPanel(null,"*");
		stdTop.setStyleClass("d-bg-top");
		HorizontalPanel stdHead=new HorizontalPanel(null,"2,*,2");
		stdHead.setStyleClass("d-top");
		stdTop.addSubPanel(stdHead);
		HorizontalPanel stdFoot=new HorizontalPanel(null,"2,*,2");
		stdRoot.addSubPanel(stdTop,CONTENT,stdFoot);
		stdHead.addSubPanel(DLG_TOP_LEFT,topCenter,DLG_TOP_RIGHT);
		stdFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_STD.addSubPanel(stdRoot);

		VerticalPanel maxTopCenter = new VerticalPanel(null,"*");
		maxTopCenter.setStyleClass("d-tc");
		HorizontalPanel maxTopTitle = new HorizontalPanel(null,"*,27");
		maxTopTitle.setStyleClass("d-tc-r");
		maxTopCenter.addSubPanel(maxTopTitle);
		maxTopTitle.addSubPanel(DLG_TITLE,DLG_MAX_BTN);

		VerticalPanel maxRoot=new VerticalPanel(null,"42,*,2");
		maxRoot.setStyleClass("dlg-std");
		HorizontalPanel maxHead=new HorizontalPanel(null,"2,*,2");
		HorizontalPanel maxFoot=new HorizontalPanel(null,"2,*,2");
		maxRoot.addSubPanel(maxHead,CONTENT,maxFoot);
		maxHead.addSubPanel(DLG_TOP_LEFT,maxTopCenter,DLG_TOP_RIGHT);
		maxFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_MAX.addSubPanel(maxRoot);


		VerticalPanel closeTopCenter = new VerticalPanel(null,"*");
		closeTopCenter.setStyleClass("d-tc");
		HorizontalPanel closeTopTitle = new HorizontalPanel(null,"*,27");
		closeTopTitle.setStyleClass("d-tc-r");
		closeTopCenter.addSubPanel(closeTopTitle);
		closeTopTitle.addSubPanel(DLG_TITLE,DLG_CLOSE_BTN);

		VerticalPanel closeRoot=new VerticalPanel(null,"42,*,2");
		closeRoot.setStyleClass("dlg-std");
		VerticalPanel closeTop = new VerticalPanel(null,"*");
		closeTop.setStyleClass("d-bg-top");
		HorizontalPanel closeHead=new HorizontalPanel(null,"2,*,2");
		closeHead.setStyleClass("d-top");
		closeTop.addSubPanel(closeHead);
		HorizontalPanel closeFoot=new HorizontalPanel(null,"2,*,2");
		closeRoot.addSubPanel(closeTop,CONTENT,closeFoot);
		closeHead.addSubPanel(DLG_TOP_LEFT,closeTopCenter,DLG_TOP_RIGHT);
		closeFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_CLOSE.addSubPanel(closeRoot);
		
	}
}
