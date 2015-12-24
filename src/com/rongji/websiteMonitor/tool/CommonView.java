package com.rongji.websiteMonitor.tool;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;

public class CommonView {
	public static BaseView buildDialogFormView(ViewFactory fact){
		BaseView view = new BaseView();
		VerticalPanel rootPanel = new VerticalPanel("f_root", "*,1,50");
		view.setRootPanel(rootPanel);
		FormPanel form = fact.getDefaultFormPanel();
		ButtonBarPanel bbp = new ButtonBarPanel("f_btn");
		bbp.setAlign(Align.center);
		bbp.setStyleClass("bg-form");
		bbp.setFace(ButtonFace.classic);
		bbp.setCellspacing(6);
		HtmlPanel split = new HtmlPanel(null, null);
		split.setStyleClass("bg-deep");
		rootPanel.addSubPanel(form, split, bbp);
		return view;
	}
	
	public static void setBorder(Panel border) {
		border.setStyleClass("bd-deep");
		border.setVerticalMinus(2);
		border.setHorizontalMinus(2);
	}
	public static Page getPage(HttpServletRequest request) {
		Page page=new Page();
		page.setPageSize(20);
		String strCp=request.getParameter("cp"); 
		int cp=1;
		try{
			if(strCp!=null&&!strCp.equals("")){
				cp=Integer.parseInt(strCp);
			}
		}catch(Exception ex){}
		page.setCurrentPage(cp);
		return  page;
		
	}
}

