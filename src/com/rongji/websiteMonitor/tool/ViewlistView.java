package com.rongji.websiteMonitor.tool;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseDialogTemplate;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.DialogTemplate;
import com.rongji.dfish.engines.xmltmpl.JSParser;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridLayoutFormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanelPubInfo;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.dfish.misc.FilterParam;

public class ViewlistView extends CommonView{
	public static void fillView(BaseView view,FilterParam fp){
		
		HtmlPanel title = new HtmlPanel("f_title","视图列表");
		title.setStyleClass("bg-deep");
		title.setStyle("padding-left:6px;font-size:12pt;font-weight:bold;color:white; line-height:30px;");
		VerticalPanel root=new VerticalPanel("f_root","30,6,*,6");
		HorizontalPanel top=new HorizontalPanel("f_top","*,100");
		HtmlPanel about = new HtmlPanel("f_about","关于");
		about.setHtml("<a href='javascript:;' onclick=\"VM(this).cmd('aboutDFish')\">关于DFish</a> <a href='javascript:;' onclick=\"VM(this).cmd('aboutCodegen')\">关于代码生成工具</a> ", false);
		about.setStyleClass("bg-deep");
		about.setStyle("padding-right:6px;font-size:10pt;color:white; line-height:30px;");
		about.setAlign(Align.right);
		top.addSubPanel(title,about);
		view.setRootPanel(root);
		HorizontalPanel hp=new HorizontalPanel("","6,*,6");
		root.addSubPanel(top,HtmlPanel.EMPTY,hp,HtmlPanel.EMPTY);
		VerticalPanel border=new VerticalPanel("","22,70,30,24,*,30");
		setBorder(border);
		hp.addSubPanel(HtmlPanel.EMPTY,border,HtmlPanel.EMPTY);

		ButtonBarPanel bbp=new ButtonBarPanel("f_btn");
		bbp.setFace(ButtonFace.classic.group);
		bbp.setAlign(Align.right);
//		bbp.setCellspacing(6);
		bbp.setStyleClass("bg-form");
		bbp.addButton(new ClickButton("","确定","VM(this).cmd('doSearch')"));
		bbp.addButton(new ClickButton("","重置","VM(this).reload();"));
		GridPanel grid=new GridPanel("f_grid");
		HtmlPanel tt=new HtmlPanel("","&nbsp;&middot;<b>搜索条件</b>");
		tt.setStyleClass("bg-mid");
		HtmlPanel tt2=new HtmlPanel("","&nbsp;&middot;<b>搜索结果</b>");
		tt2.setStyleClass("bg-mid");
		GridLayoutFormPanel searchForm=new GridLayoutFormPanel("search");
		searchForm.setHighlightMouseover(false);
		searchForm.setStyleClass("bg-form");
		searchForm.setFace(GridPanelPubInfo.FACE_NONE);
		searchForm.setScroll(Scroll.hidden);
		HorizontalPanel bottom=new HorizontalPanel(null,"80,*");
		ButtonBarPanel newBtnBar=new ButtonBarPanel("newBtnBar");
		newBtnBar.setCellspacing(6);
		newBtnBar.setFace(ButtonFace.group);
		PagePanel page=new PagePanel("f_page");
		bottom.addSubPanel(newBtnBar,page);
		border.addSubPanel(tt,searchForm,bbp,tt2,grid,bottom);
		
		ClickButton newButton=new ClickButton("","新增视图","VM(this).cmd('editViewInfo','NEWID');");
		newBtnBar.addButton(newButton);
		searchForm.add(0,0,new Text("sKey","关键字",fp.getValueAsString("sKey"),-1));
		searchForm.add(0,1,new Text("sAuthor","作者",fp.getValueAsString("sAuthor"),-1));
		//排序 最后修改时间，倒序
		//显示
		String[][] showOptions={{"1","仅显示最新版本"},{"2","仅显示定版"},{"ALL","显示所有"}};
		searchForm.add(1,0,new Select("sStatus","显示范围",new Object[]{fp.getValueAsString("sStatus")},Arrays.asList(showOptions)));
		String[][] orderOptions={{"modDesc","最后修改时间倒序"},{"modAsc","最后修改时间顺序"},
				{"creDesc","创建时间倒序"},{"creAsc","创建时间顺序"}};
		searchForm.add(1,1,new Select("sOrder","显示顺序",new Object[]{fp.getValueAsString("sOrder")},Arrays.asList(orderOptions)));
		
		JSParser psOper=new JSParser("psOper",
				"var id=xml.getAttribute( 'id' );" +
				"var canDelete='1'==(xml.getAttribute( 'C2' ))&&'1'==(xml.getAttribute( 'st' ));" +
				"return " +
				"\"<a href='javascript:;' onclick=\\\"VM(this).reload('vm:|cg_editor.sp?act=showView&id=\"+id+\"');\\\">打开</a> \"+" +
				"(canDelete?\"<a href='javascript:;' onclick=\\\"VM(this).cmd('delView','\"+id+\"');\\\">删除</a>\"" +
				":\"<span style='color:gray'>删除</span>\");");
		view.addParser(psOper);
		
		AjaxCommand turnPage=new AjaxCommand("turnPage","cg_viewlist.sp?act=turnPage&cp=$0"+fp);
		view.addCommand(turnPage);
		AjaxCommand delView=new AjaxCommand("delView","cg_viewlist.sp?act=delView&viewId=$0"+fp);
		view.addCommand(delView);
		SubmitCommand doSearch=new SubmitCommand("doSearch","cg_viewlist.sp?act=doSearch","search",false);
		view.addCommand(doSearch);
		view.add(new DialogCommand("editViewInfo", "f_std", "版本信息", "dialog",
				DialogCommand.WIDTH_MEDIUM, DialogCommand.HEIGHT_MEDIUM,
				DialogCommand.POSITION_MIDDLE,
				"vm:|cg_viewlist.sp?act=editViewInfo&viewId=$0"));
		view.add(new DialogCommand("aboutCodegen", "f_std", "关于代码生成工具", "dialog",
				DialogCommand.WIDTH_MEDIUM, DialogCommand.HEIGHT_MEDIUM,
				DialogCommand.POSITION_MIDDLE,
				"vm:|cg_about.sp?act=codegen"));
		view.add(new DialogCommand("aboutDFish", "f_std", "关于DFish", "dialog",
				DialogCommand.WIDTH_MEDIUM, DialogCommand.HEIGHT_MEDIUM,
				DialogCommand.POSITION_MIDDLE,
				"vm:|cg_about.sp?act=dfish"));
	}



	public static FilterParam getFilterParam(HttpServletRequest request) {
		FilterParam fp=new FilterParam();
		fp.registerKey("sOrder");
		fp.registerKey("sKey");
		fp.registerKey("sAuthor");
		fp.registerKey("sStatus");
		fp.bindRequest(request);
		return fp;
	}

	public static CommandGroup update(Panel... panels) {
		CommandGroup cg=new CommandGroup("update");
		for(Panel p:panels){
			UpdateCommand ud=new UpdateCommand(null);
			cg.add(ud);
			ud.setContent(p);
		}
		return cg;
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
	private static BaseDialogTemplate TMPL_STD = new BaseDialogTemplate("f_std");
	private static BaseDialogTemplate TMPL_MAX = new BaseDialogTemplate("f_std_mx");
	private static BaseDialogTemplate TMPL_CLOSE = new BaseDialogTemplate("f_std_x");
	static{
		SourcePanel CONTENT = new SourcePanel("dlg-src","");
		CONTENT.setStyleClass("d-bd");
		CONTENT.setHorizontalMinus(2);
		CONTENT.setVerticalMinus(1);
//		CONTENT.setStyle("background-color:#F8F8F8;border:1px solid #C6C6C6;border-top-width:0px;");
		HtmlPanel DLG_TOP_LEFT=new HtmlPanel(null,null);
		DLG_TOP_LEFT.setStyleClass("d-tl");
		HtmlPanel DLG_TITLE=new HtmlPanel("dlg-tt",null);
		DLG_TITLE.setStyleClass("d-tt");
		DLG_TITLE.setStyle("font-size:13px;font-weight:bold;color:#fff;");
		HtmlPanel DLG_MAX_BTN=new HtmlPanel("dlg-max",null);
		DLG_MAX_BTN.setStyleClass("d-b-max");
		HtmlPanel DLG_CLOSE_BTN=new HtmlPanel("dlg-x",null);
		DLG_CLOSE_BTN.setStyleClass("d-b-x");
		HtmlPanel DLG_TOP_RIGHT=new HtmlPanel(null,null);
		DLG_TOP_RIGHT.setStyleClass("d-tr");
//		HtmlPanel DLG_BOTTOM_LEFT=new HtmlPanel(null,null);
//		DLG_BOTTOM_LEFT.setStyleClass("d-bl");
//		HtmlPanel DLG_BOTTOM_RIGHT=new HtmlPanel(null,null);
//		DLG_BOTTOM_RIGHT.setStyleClass("d-br");
//		HtmlPanel DLG_BOTTOM_CENTER=new HtmlPanel(null,null);
//		DLG_BOTTOM_CENTER.setStyleClass("d-bc");
		
		VerticalPanel topCenter = new VerticalPanel(null,"*");
//		topCenter.setStyleClass("d-tc");
		HorizontalPanel topTitle = new HorizontalPanel(null,"*,36,36");
		topTitle.setStyleClass("d-tc-r");
		topCenter.addSubPanel(topTitle);
		topTitle.addSubPanel(DLG_TITLE,DLG_MAX_BTN,DLG_CLOSE_BTN);
		
		
		VerticalPanel stdRoot=new VerticalPanel(null,"41,*");//,0
//		stdRoot.setStyleClass("d-bd");
		VerticalPanel stdTop = new VerticalPanel(null,"*");
		stdTop.setStyleClass("d-bg-top");
		stdTop.setHorizontalMinus(2);
		stdTop.setVerticalMinus(1);
//		stdTop.setStyle("background-color:#3FB4C6;");
//		HorizontalPanel stdHead=new HorizontalPanel(null,"1,*,1");
//		stdHead.setStyleClass("d-top");
//		stdTop.addSubPanel(stdHead);
//		HorizontalPanel stdFoot=new HorizontalPanel(null,"2,*,2");
		stdRoot.addSubPanel(stdTop,CONTENT);//,stdFoot
		stdTop.addSubPanel(topCenter);//DLG_TOP_LEFT,   ,DLG_TOP_RIGHT
//		stdFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_STD.addSubPanel(stdRoot);
		TMPL_STD.setFace("dlg-std");

		VerticalPanel maxTopCenter = new VerticalPanel(null,"*");
		maxTopCenter.setStyleClass("d-tc");
		HorizontalPanel maxTopTitle = new HorizontalPanel(null,"*,36");
		maxTopTitle.setStyleClass("d-tc-r");
		maxTopCenter.addSubPanel(maxTopTitle);
		maxTopTitle.addSubPanel(DLG_TITLE,DLG_MAX_BTN);

		VerticalPanel maxRoot=new VerticalPanel(null,"42,*,0");
		HorizontalPanel maxHead=new HorizontalPanel(null,"1,*,1");
//		HorizontalPanel maxFoot=new HorizontalPanel(null,"2,*,2");
		maxRoot.addSubPanel(maxHead,CONTENT);//,maxFoot
		maxHead.addSubPanel(DLG_TOP_LEFT,maxTopCenter,DLG_TOP_RIGHT);
//		maxFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_MAX.addSubPanel(maxRoot);
		TMPL_MAX.setFace("dlg-std");


//		VerticalPanel closeTopCenter = new VerticalPanel(null,"*");
//		closeTopCenter.setStyleClass("d-tc");
		HorizontalPanel closeTopTitle = new HorizontalPanel(null,"*,36");
		closeTopTitle.setStyleClass("d-tc-r");
//		closeTopCenter.addSubPanel(closeTopTitle);
		closeTopTitle.addSubPanel(DLG_TITLE,DLG_CLOSE_BTN);

		VerticalPanel closeRoot=new VerticalPanel(null,"42,*");//,0
		VerticalPanel closeTop = new VerticalPanel(null,"*");
		closeTop.setStyleClass("d-bg-top");
		closeTop.setHorizontalMinus(2);
		closeTop.setVerticalMinus(1);
		
		closeTop.addSubPanel(closeTopTitle);
		
//		HorizontalPanel closeHead=new HorizontalPanel(null,"1,*,1");
//		closeHead.setStyleClass("d-top");
//		closeTop.addSubPanel(closeHead);
//		HorizontalPanel closeFoot=new HorizontalPanel(null,"2,*,2");
		closeRoot.addSubPanel(closeTop,CONTENT);//,closeFoot
//		closeHead.addSubPanel(DLG_TOP_LEFT,closeTopCenter,DLG_TOP_RIGHT);
//		closeFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_CLOSE.addSubPanel(closeRoot);
		TMPL_CLOSE.setFace("dlg-std");
		
	}
}
