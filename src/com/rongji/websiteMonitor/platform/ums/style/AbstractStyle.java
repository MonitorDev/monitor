package com.rongji.websiteMonitor.platform.ums.style;

import java.util.Locale;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseDialogTemplate;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.DialogTemplate;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FlowPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.PagePanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;

public abstract class AbstractStyle implements ItaskStyle{
	public int getNavWidth() {
		return 250;
	}
	
	public DialogTemplate getDialogTemplate(String id) {
		if("f_std".equals(id)){
			return TMPL_STD;
		}else if("f_std_mx".equals(id)){
			return TMPL_MAX;
		}else if("f_std_x".equals(id)){
			return TMPL_CLOSE;
		}
		
		return null;
	}
	private static BaseDialogTemplate TMPL_STD=new BaseDialogTemplate("f_std");
	private static BaseDialogTemplate TMPL_MAX=new BaseDialogTemplate("f_std_mx");
	private static BaseDialogTemplate TMPL_CLOSE=new BaseDialogTemplate("f_std_x");
	static{
		SourcePanel CONTENT=new SourcePanel("dlg-src","");
		CONTENT.setStyleClass("d-bd white");
		CONTENT.setHorizontalMinus(2);
		CONTENT.setVerticalMinus(1);
		HtmlPanel DLG_TOP_LEFT=new HtmlPanel(null,null);
		DLG_TOP_LEFT.setStyleClass("d-tl");
		HtmlPanel DLG_TITLE=new HtmlPanel("dlg-tt",null);
		DLG_TITLE.setStyleClass("d-tt");
		HtmlPanel DLG_MAX_BTN=new HtmlPanel("dlg-max",null);
		DLG_MAX_BTN.setStyleClass("d-b-max");
		HtmlPanel DLG_CLOSE_BTN=new HtmlPanel("dlg-x",null);
		DLG_CLOSE_BTN.setStyleClass("d-b-x");
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
		HorizontalPanel topTitle = new HorizontalPanel(null,"*,36,36");
		topTitle.setStyleClass("d-tc-r");
		topCenter.addSubPanel(topTitle);
		topTitle.addSubPanel(DLG_TITLE,DLG_MAX_BTN,DLG_CLOSE_BTN);
		
		
		VerticalPanel stdRoot=new VerticalPanel(null,"42,*,0");
		VerticalPanel stdTop = new VerticalPanel(null,"*");
		stdTop.setStyleClass("d-bg-top");
		HorizontalPanel stdHead=new HorizontalPanel(null,"1,*,1");
		stdHead.setStyleClass("d-top");
		stdTop.addSubPanel(stdHead);
		HorizontalPanel stdFoot=new HorizontalPanel(null,"2,*,2");
		stdRoot.addSubPanel(stdTop,CONTENT,stdFoot);
		stdHead.addSubPanel(DLG_TOP_LEFT,topCenter,DLG_TOP_RIGHT);
		stdFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
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
		HorizontalPanel maxFoot=new HorizontalPanel(null,"2,*,2");
		maxRoot.addSubPanel(maxHead,CONTENT,maxFoot);
		maxHead.addSubPanel(DLG_TOP_LEFT,maxTopCenter,DLG_TOP_RIGHT);
		maxFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_MAX.addSubPanel(maxRoot);
		TMPL_MAX.setFace("dlg-std");


		VerticalPanel closeTopCenter = new VerticalPanel(null,"*");
		closeTopCenter.setStyleClass("d-tc");
		HorizontalPanel closeTopTitle = new HorizontalPanel(null,"*,36");
		closeTopTitle.setStyleClass("d-tc-r");
		closeTopCenter.addSubPanel(closeTopTitle);
		closeTopTitle.addSubPanel(DLG_TITLE,DLG_CLOSE_BTN);

		VerticalPanel closeRoot=new VerticalPanel(null,"42,*,0");
		VerticalPanel closeTop = new VerticalPanel(null,"*");
		closeTop.setStyleClass("d-bg-top");
		HorizontalPanel closeHead=new HorizontalPanel(null,"1,*,1");
		closeHead.setStyleClass("d-top");
		closeTop.addSubPanel(closeHead);
		HorizontalPanel closeFoot=new HorizontalPanel(null,"2,*,2");
		closeRoot.addSubPanel(closeTop,CONTENT,closeFoot);
		closeHead.addSubPanel(DLG_TOP_LEFT,closeTopCenter,DLG_TOP_RIGHT);
		closeFoot.addSubPanel(DLG_BOTTOM_LEFT,DLG_BOTTOM_CENTER,DLG_BOTTOM_RIGHT);
		TMPL_CLOSE.addSubPanel(closeRoot);
		TMPL_CLOSE.setFace("dlg-std");
		
	}
	private Locale locale;
	public Locale getLocale() {
		if(locale==null){
			return Locale.getDefault();
		}
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale=locale;
	}
	
	private boolean isMobile = false;
	public boolean getMobile() {
		return isMobile;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile=isMobile;
	}
	
	
	
	public BaseView getPopupFormView() {
		BaseView view =new BaseView();
		VerticalPanel root=new VerticalPanel("f_root","*,55");
		view.setRootPanel(root);
		FormPanel form=new FormPanel("f_form");
		form.setStyle("padding:12px 16px 0 16px");
		form.setScroll(Scroll.hidden);
		root.addSubPanel(form);
		ButtonBarPanel bbp=new ButtonBarPanel("f_btn");
		bbp.setAlign(Align.right);
	    bbp.setFace(ButtonFace.classic);
	    bbp.setStyleClass("d-bg bd-form bd-onlytop");
	    bbp.setStyle("margin:0 28px");
//	    bbp.setVerticalMinus(28);
	    root.addSubPanel(bbp);
		
		return view;
	}
	

	protected DialogTemplate getMsgDialogTemplate() {
		BaseDialogTemplate dialog = new BaseDialogTemplate("msg");
		/*	<fr type="set" rows="20,*" class="bd-mid white" style="padding:1px" hmin="4" vmin="4">
			<fr type="set" cols="42,*,16" class="ttul">
				<fr type="html"><![CDATA[&nbsp;<img src=img/p/collapse.gif align=absmiddle vspace=3><img src=img/p/ring.gif align=absmiddle vspace=3 style=margin-left:3px>]]></fr>
				<fr type="html" id="dlg-tt"/>
				<fr type="html"><![CDATA[<a href=javascript:void(0) class=ttul-x onclick=M.Msg.hide(this)></a>]]></fr>
			</fr>
			<fr type="src" id="dlg-src" src="" class=""/>
		</fr>*/
			VerticalPanel root=new VerticalPanel(null,"37,2,*");
			dialog.setFace("dlg-msg");
			dialog.addSubPanel(root);
			
			HorizontalPanel head=new HorizontalPanel(null,"*,35");
			root.addSubPanel(head);
			head.setStyleClass("d-top");
			head.setHorizontalMinus(1);
			head.setVerticalMinus(1);
			
			HtmlPanel title=new HtmlPanel("dlg-tt",null);
			title.setStyleClass("d-tt");
			HtmlPanel titleRight=new HtmlPanel(null,"<fish:js>return $.htm.ico('.i-x',{style:'height:100%',clk:'DFish.close(this)'})</fish:js>");
			head.addSubPanel(title,titleRight);
			
			HtmlPanel line=new HtmlPanel(null, null);
			line.setStyleClass("top-line white");
			root.addSubPanel(line);
			
			SourcePanel source=new SourcePanel("dlg-src",null);
			source.setStyleClass("d-bottom");
			source.setHorizontalMinus(1);
			source.setVerticalMinus(1);
			root.addSubPanel(source);
			
			return dialog;
	}
	protected DialogTemplate getNoneDialogTempate() {
		DialogTemplate dialog = new BaseDialogTemplate("none");
		VerticalPanel vp = new VerticalPanel("f_root", "*");
		vp.setStyleClass("bg-white");
		SourcePanel content = new SourcePanel("dlg-src", null);
		vp.addSubPanel(content);
		dialog.addSubPanel(vp);
		return dialog;
	}
//	public static void main(String[] main) throws ParseException{
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		long year=(((365L*24+5L)*60+48L)*60+46L);
//		double d=year/86400.0;
//		System.out.println("一年有"+d+"天,约为365.2422天");
//		Date d2017=sdf.parse("2017-02-03");
//		Date d2017real=new Date(d2017.getTime()+(long)(0.7424*86400000));
//		System.out.println("2017年立春大约为"+d2017real);
//		Date d1900=new Date(d2017real.getTime()-year*1000*(2017-1900));
//		System.out.println("1900年立春大约为"+d1900);
//		//7424
//	}
	
	
	/**
	 * <table border='1'>
	 * <tr><td>主要按钮<br/>f_btn_left</td>
	 * <td>标题<br/>f_main_title_cat</td>
	 * <td>显示控制按钮<br/>f_main_title_right</td>
	 * </tr>
	 * <tr><td colspan='3'>内容<br/>f_grid<br/><br/><br/></td></tr>
	 * </tr><td colspan='3' align='right'>底部翻页栏<br/>f_page_bottom</td></tr>
	 * </table>
	 */
	public Panel getGridContentPanel(int style,boolean hasPage) {
		String rightBorderClass=getMainBorderClass();
		String rightTitleClass=getMainTitleClass();
		int rightTitleHeight=getMainTitleHeight();

		String rootSplit=rightTitleHeight+",*";
		VerticalPanel mainVP=new VerticalPanel("f_main",rootSplit);
		mainVP.setStyleClass(rightBorderClass);

		FlowPanel mainTitle=new FlowPanel("f_main_title");
		mainTitle.setDirection("h");
		mainTitle.setStyleClass("bd-form bd-onlybottom");
		mainTitle.setStyle("margin:0 30px");
		mainVP.addSubPanel(mainTitle);
		
		ButtonBarPanel btnLeft = new ButtonBarPanel("f_btn_left");
		btnLeft.setFace(ButtonFace.office);
		btnLeft.setStyleClass("left");
		mainTitle.addSubPanel(btnLeft);
		
		HtmlPanel title= new HtmlPanel("f_main_title_cat","");
		title.setStyleClass("left tt-main");
		title.setStyle("padding-left:20px");
		mainTitle.addSubPanel(title);
		
		FlowPanel mainTitleRight = new FlowPanel("f_main_title_right");
		mainTitleRight.setDirection("h");
		mainTitleRight.setStyleClass("right");
		mainTitle.addSubPanel(mainTitleRight);

		if(hasPage){
			PagePanel pageTop=new PagePanel("f_page_top");
			pageTop.setFace("office");
			mainTitleRight.addSubPanel(pageTop);
		}
		
		ButtonBarPanel btnRight = new ButtonBarPanel("f_btn_right");
		btnRight.setFace(ButtonFace.office);
		btnRight.setStyle("margin-left:8px");
		mainTitleRight.addSubPanel(btnRight);
		

		FlowPanel flow=new FlowPanel("f_main_content");//用这个flowpanel实现滚动条效果
		flow.setScroll(Scroll.miniscroll);
		flow.setStyle("padding:0 30px");
		mainVP.addSubPanel(flow);
		
		GridPanel grid=new GridPanel("f_grid");
		
		grid.pub().setNobr(true);
		grid.pub().setFixhead(true);
		grid.setScroll(Scroll.hidden);
		grid.pub().setFace("bd-dot");
		
		flow.addSubPanel(grid);
		
		
//		if(hasPage){
//			VerticalPanel pageBV = new VerticalPanel(null,"56");
//			flow.addSubPanel(pageBV);
//			PagePanel pageBottom=new PagePanel("f_page_bottom");
//			pageBottom.setFace("office");
//			pageBV.addSubPanel(pageBottom);
//		}
		return mainVP;
	}
}
