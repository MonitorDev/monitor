package com.rongji.dfish.webapp.pub.view;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.TreeItem;
import com.rongji.dfish.engines.xmltmpl.component.TreePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.resources.PropertyHandler;
import com.rongji.dfish.webapp.pub.business.EditorMethods;

public class EditorView {

	public static void fillShortcutDialogView(BaseView view,
			ViewFactory viewFactory) {
		VerticalPanel root=new VerticalPanel("root","26,*");//按钮栏和下面的动作
		final String STYLECLASS_BORDER=viewFactory.getStyleClass(LogicComponent.BORDER_DEEP);
		root.setStyleClass(STYLECLASS_BORDER);
		root.setHorizontalMinus(2);
		root.setVerticalMinus(2);
		view.setRootPanel(root);
		
		ButtonBarPanel bbp=new ButtonBarPanel("b");
		HorizontalPanel shell_1=new HorizontalPanel(null,"*");
		shell_1.setStyle("padding:3px");
		shell_1.setStyleClass("bg-lower");
		shell_1.setHorizontalMinus(6);
		shell_1.setVerticalMinus(6);
		root.addSubPanel(bbp,shell_1);

		//fill main content
		TreePanel t=new TreePanel("menu", STYLECLASS_BORDER, null, 2, 2);
		t.setStyle("background:white");
		t.setStyleClass(viewFactory.getStyleClass(LogicComponent.BG_WHITE));
		
		HtmlPanel resizeBar=new HtmlPanel(null,null);
		resizeBar.setHorizontalResize("80,80");
		
		VerticalPanel main=new VerticalPanel("main","*",STYLECLASS_BORDER,null,2,2);
		main.setStyle("background:white");
		
		HtmlPanel splitBar=new HtmlPanel(null,null);
		splitBar.setStyleClass("bg-lower");

//		newText.add(new Hr());
//		newText.addRadio("cfgWithDform", null, null, options, true, false, false, "VM(this).cmd('useDform',this.value);", false);
//		VerticalPanel main=new VerticalPanel("main","*",STYLECLASS_BORDER,null,2,2);
//		main.setStyle("background:white");
		
		shell_1.addSubPanel(main);
		
		t.pub().setDefaultValue("VM(this).cmd('upd','$pk')", null, null);
		AjaxCommand ac=new AjaxCommand("upd","shtctIpt.sp?act=queryClz&clzId=$0");
		view.add(ac);
		// 数据由业务代码填充
		TreeItem rootTree = new TreeItem("root", "分类", 
				"/shtctIpt.sp?act=openClz&clzId=root", null, null, null, null);//"选择分类"
		t.addTreeItem(rootTree);
		List<String[]> clzs=EditorMethods.getShctInpClz();
		if(clzs!=null&& clzs.size()>0){
			String foucs=clzs.get(0)[0];
			t.pub().setFocus(foucs);
			for(String[] clz:clzs){
				TreeItem item=new TreeItem();
				item.setProperty(TreeItem.KEY_PRIMARY_KEY, clz[0]);
				item.setProperty(TreeItem.KEY_TEXT, clz[1]);
				rootTree.addTreeItem(item);
			}
			fillShtctMainPanel(main,view, viewFactory,foucs);
		}else{
			fillShtctMainPanel(main,view, viewFactory,"FAKE");
		}
//		
		
		
//		fillShtctMainPanel(main,view, viewFactory);
	}
	public static void fillShtctMainPanel(VerticalPanel main,BaseView view, ViewFactory viewFactory, String clzId) {
		Locale locale=viewFactory.getLoc();
		final String STYLECLASS_PANEL_GRID = viewFactory.getStyleClass(LogicComponent.PANEL_GRID);
		GridPanel gp=new GridPanel("f_grid", STYLECLASS_PANEL_GRID, null, 0, 0);
//		FormPanel newText=new FormPanel("newtext");
//		newText.setStyleClass("mx");
//		newText.setScroll(Scroll.hidden);
//		HtmlPanel splitBar=new HtmlPanel(null,null);
//		splitBar.setStyleClass("bg-lower");
//		HorizontalPanel hp=new HorizontalPanel(null,"*,40");
//		ButtonBarPanel bbp=new ButtonBarPanel("adbbp");
//		bbp.setFace(ButtonFace.classic);
//		hp.addSubPanel(newText,bbp);
		main.addSubPanel(gp);
//		newText.addHidden("clzId",clzId);
//		newText.addHtml("", true, "<textarea layout=textarea2 rows=1 cols=53 name=inputContent></textarea>");
//		SubmitCommand addContent= new SubmitCommand("addcon","shtctIpt.sp?act=addContent","newtext",false);
//		view.addCommand(addContent);
//		boolean selfShtct=clzId.compareTo("00001000")>=0;
//		ClickButton cbAdd=new ClickButton(null, getMsg(locale,"p.msg.add"), "VM(this).cmd('"+addContent.getId()+"')",false,true,!selfShtct);
//		bbp.addButton(cbAdd);
		
		gp.setButtonPlace("b");//总按钮栏编号
		JSCommand ok=new JSCommand("ok","VM.MX.shtipt(this,'f_grid')");
		view.addCommand(ok);
		
		ClickButton cbConfirm=new ClickButton("img/b/ok.gif", PropertyHandler.getMsg(locale, "p.btn.confirm"), "VM(this).cmd('"+ok.getId()+"')");		
		gp.addButton(cbConfirm);
//		if(selfShtct){
//			SubmitCommand del=new SubmitCommand("del","shtctIpt.sp?act=delContent",gp.getId(),false);
//			view.addCommand(del);
//			ClickButton cbDelete=new ClickButton("img/b/delete.gif", PropertyHandler.getMsg(locale, "p.btn.delete"), "VM(this).cmd('"+del.getId()+"')");		
//			gp.addButton(cbDelete);
//		}
//		gp.addHidden("clzId", clzId);
		fillGridData(gp, locale,clzId);
	}
	public static void fillGridData(GridPanel grid, Locale locale, String clzId) {
//		PubCommonDAO dao = getDAO();
//		List data = dao
//				.getQueryList(
//						"SELECT t.inputId, t.inputContent FROM PubShtctInput t WHERE t.pubShtctClz.clzId=? ORDER BY t.inputId",
//						new Object[] { clzId });
//		grid.setData(data);
		grid.addSelectitem("C1", "40");
//		grid.addHiddenColumn(0, "inputId");
		List<String> content=EditorMethods.getShctInpContent().get(clzId);
		List<Object[]> data=new ArrayList<Object[]>();
		for(String s:content){
			data.add(new String[]{s});
		}
		grid.setData(data);
		grid.addTextColumn(0, "C1", "内容", "*", null);
		
	}
}
