package com.rongji.websiteMonitor.platform.ums.style;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Skin;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.button.ExpandableButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FilmPanel;
import com.rongji.dfish.engines.xmltmpl.component.FlowPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.tool.ViewlistView;

public class I73dStyle extends AbstractStyle{

	public BaseView buildIndexView(HttpServletRequest request) throws Exception {
		
		BaseView view=new BaseView();
		VerticalPanel layout_bg = new VerticalPanel("layout","*");
		layout_bg.setStyleClass("ly-bg");
		view.setRootPanel(layout_bg);
		
		
		VerticalPanel layout_top = new VerticalPanel("ly_top","*");
		layout_top.setStyleClass("ly-top");
		layout_bg.addSubPanel(layout_top);
		
		VerticalPanel root=new VerticalPanel("root","43,*");
		root.setStyleClass("ly-bottom");
		layout_top.addSubPanel(root);

		HorizontalPanel top=new HorizontalPanel(null,"*,70,120");//
//		top.setStyle("background:url('./m/index/img/bg-banner.gif') repeat-x;");
		root.addSubPanel(top);
		
		
		ButtonBarPanel mainOper=new ButtonBarPanel("main_oper");
		mainOper.setFace(ButtonFace.tab_workspace);
		mainOper.setBindFilmId("main_film");
		mainOper.setButtonMaxWidth(134);
		mainOper.setButtonMinWidth(30);
		String loginUser = FrameworkHelper.getLoginUser(request);
		top.addSubPanel(mainOper);

		
		HtmlPanel system = new HtmlPanel(null, "");
		system.setAlign(Align.right);
		system.setStyle("padding-top:2px;color:#fff;");
		
		String title = loginUser;
		system.setHtml("<a href='javaScript:void(0);' style='color:white' onclick=\"pub.user.pop('"+loginUser+"')\">"+title+"</a>");
		top.addSubPanel(system);

		ClickButton cb = new ClickButton("img/m/x16/workbench.png", "系统导航", null);
		cb.setId("btn_m-main");
		cb.setFocus(true);
		mainOper.addButton(cb);
				
		ButtonBarPanel topmenu = new ButtonBarPanel("topmenu");
		top.addSubPanel(topmenu);
		topmenu.setFace(ButtonFace.topmenu);
		topmenu.setAlign(Align.right);
		topmenu.setStyle("padding-top:2px");
		
		ExpandableButton tmLink = new ExpandableButton(".i-link-top", "", null);
		tmLink.addButton(new ClickButton(null, "系统切换", "var path = Cfg.intf.ssoPath;VM( this ).cmd( { tagName : 'dialog',src : path + '/changeSystem.jsp', w : 625, h : 420, tpl : 'f_std', t : '系统切换', pos : 0 } );"));
		tmLink.addButton(new ClickButton(null, "参数设置", "VM( this ).cmd( { tagName : 'dialog',src : 'vm:|config.sp?act=index', w : 500, h : 320, tpl : 'f_std', t : '参数设置', pos : 0 } );"));
		tmLink.addButton(new ClickButton(null, "密码修改", "var path = Cfg.intf.ssoPath;VM( this ).cmd( { tagName : 'dialog',src : path + '/changePwd.jsp', w : 440, h : 250, tpl : 'f_std', t : '密码修改', pos : 0 } );"));
		topmenu.addButton(tmLink);
		tmLink.setOvermenu(true);
		AjaxCommand selectSyetem = new AjaxCommand("selectSyetem", "portalIndex.sp?act=selectSystem&systemId=$0");
		view.add(selectSyetem);
		Command sys = new JSCommand("sys", "vm:|portalIndex.sp?act=systemNavigation");
		view.add(sys);
		
			
		ClickButton tmLogout = new ClickButton(".i-logout-top", "", "window.location='index.sp?act=logout'");
		topmenu.addButton(tmLogout);

		
		HorizontalPanel wrap=new HorizontalPanel("wrap","0,*");
		root.addSubPanel(wrap);
		
		HorizontalPanel sd = new HorizontalPanel("", "54,*");
		VerticalPanel sdwrp = new VerticalPanel( "sidebar", "*,27" );
		sd.addSubPanel(sdwrp);
		ButtonBarPanel ico = new ButtonBarPanel("sidebar-ico");
		ico.setFace(ButtonFace.tab_sidebar);
		ico.setDirectionVertical(true);
		ClickButton btnfav = new ClickButton(".i-sdbar-fav", null, "M.sd().run(this)");
		btnfav.setId("sdbar-fav");
		ClickButton btntxl = new ClickButton(".i-sdbar-txl", null, "M.sd().run(this)");
		btntxl.setId("sdbar-txl");
		ClickButton btntsk = new ClickButton(".i-sdbar-tsk", null, "M.sd().run(this)");
		btntsk.setId("sdbar-tsk");
		sdwrp.addSubPanel(ico);
		
		sd.addSubPanel(new HtmlPanel("sidebar-win", ""));
		HtmlPanel sdcfg = new HtmlPanel("sdbar-cfg","");
		sdwrp.addSubPanel(sdcfg);
		
		wrap.addSubPanel(new HtmlPanel("wrap_rsz", null));
		
		FilmPanel film=new FilmPanel("main_film");
		film.setFocus("m-main");
		wrap.addSubPanel(film);
		
		SourcePanel mMain = new SourcePanel("m-main",
		"vm:|portalIndex.sp?act=systemNavigation");
		mMain.setStyleClass("m-wspage m-wsp-systemNavigation");
		mMain.setCache(true);
		film.addSubPanel(mMain);
		// 添加dialog
		view.addDialogTemplate(getDialogTemplate("f_std"));
		view.addDialogTemplate(getDialogTemplate("f_std_x"));
		view.setTemplateSource("index.sp?act=dialogTemplate&tpl=$tpl");
		// 增加消息的绑定 LinLW 2010-11-17
		view.addDialogTemplate(getMsgDialogTemplate());
		view.addDialogTemplate(getNoneDialogTempate());
		
		
	
		
		return view;
	}

	
	
	public int getButtonBarHeight() {
		return 57;
	}

	public int getOutterPadding() {
		return 1;
	}

	public String getOutterPaddingClass() {
		return "bg-gray";
	}

	public String getNavBorderClass() {
		return "";
	}

	public String getNavTitleClass() {
		return "tt-nav bd-gray bd-onlybottom";
	}

	public int getNavTitleHeight() {
		return 36;
	}

	public String getMainBorderClass() {
		return "";
	}

	public String getMainTitleClass() {
		return "tt-main bd-form bd-onlybottom";
	}

	public int getMainTitleHeight() {
		return 57;
	}

	public Skin getSkin() {
		return Skin.VISTA;
	}

	public int getPagePanelHeight() {
		return 26;
	}
	public int getNavWidth() {
		return 230;
	}
	
	public BaseView buildContentShell() {
		BaseView view=new BaseView();
		view.setRootPanel(getContentShellPanel());
		return view;
	}
	
	
	
	protected Panel getContentShellPanel() {
		String rightBorderClass=getMainBorderClass();
		String rightTitleClass=getMainTitleClass();
		int rightTitleHeight=getMainTitleHeight();

		VerticalPanel mainVP=new VerticalPanel("f_main",rightTitleHeight+",*");

		FlowPanel mainTitle=new FlowPanel("f_main_title");
		mainTitle.setStyleClass("bd-form bd-onlybottom");
		mainTitle.setStyle("margin:0 30px");
		mainTitle.setDirection(FlowPanel.HORIZONTAL);
		mainVP.addSubPanel(mainTitle);
		
		ButtonBarPanel bbp1 = new ButtonBarPanel("f_btn_left");
		bbp1.setStyleClass("left");
		mainTitle.addSubPanel(bbp1);

		HtmlPanel title= new HtmlPanel("f_main_title_cat","请设置标题f_main_title_cat");
		title.setStyleClass("left tt-main");
		title.setStyle("padding-left:30px");
		mainTitle.addSubPanel(title);
		
		
		FlowPanel titleRight = new FlowPanel("f_main_title_right");
		titleRight.setDirection(FlowPanel.HORIZONTAL);
		titleRight.setStyleClass("right");
		mainTitle.addSubPanel(titleRight);
		
		PagePanel pageTop = new PagePanel("f_page_top");
		titleRight.addSubPanel(pageTop);
		pageTop.setFace(PagePanel.FACE_OFFICE);

		ButtonBarPanel bbp2 = new ButtonBarPanel("f_btn_right");
		bbp2.setFace(ButtonFace.group);
		titleRight.addSubPanel(bbp2);
		
		
		VerticalPanel padding=new VerticalPanel(null,"*");
//		padding.setHorizontalMinus(16);
//		padding.setStyle("padding:0 8px");
		mainVP.addSubPanel(padding);

		HtmlPanel mainPanel=new HtmlPanel("f_main_content","请替换该面板，ID为f_main_content");
		padding.addSubPanel(mainPanel);
		mainPanel.setStyle("margin:2px;border:gray 1px dot");
		mainPanel.setHorizontalMinus(6);
		mainPanel.setVerticalMinus(6);
		
//		PagePanel pageBottom = new PagePanel("f_page_bottom");
//		pageBottom.setStyle("padding:0 40px");
//		mainVP.addSubPanel(pageBottom);
		
		
		return mainVP;
	}

	public BaseView buildMenuAndContentShell() {
		BaseView view=new BaseView();
//		int spacing=getOutterPadding();
		int leftWidth=getNavWidth();
		int leftTitleHeight=getNavTitleHeight();
		String leftBorderClass=getNavBorderClass();
		String leftTitleClass=getNavTitleClass();
		
		HorizontalPanel root=new HorizontalPanel("f_root",leftWidth+",*");
		view.setRootPanel(root);

		VerticalPanel navVP=new VerticalPanel("f_nav","*");
		root.addSubPanel(navVP);
		navVP.setStyle("padding-top:18px");
		navVP.setStyleClass("bd-form bd-onlyright");
		navVP.setHorizontalMinus(1);
		navVP.setVerticalMinus(18);
		
		
		HtmlPanel navPanel=new HtmlPanel("f_nav_content","请替换该面板，ID为 f_nav_content");
		navVP.addSubPanel(navPanel);
		navPanel.setHorizontalMinus(6);
		navPanel.setVerticalMinus(6);
		
		root.addSubPanel(getContentShellPanel());

		return view;
	}


}
