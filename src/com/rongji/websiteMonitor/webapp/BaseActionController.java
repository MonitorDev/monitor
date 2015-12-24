package com.rongji.websiteMonitor.webapp;



import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.button.ClickButton;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.licence.core.cache.LicenceCacheMgr;
import com.rongji.licence.util.Utils;

/**
 * 所有ActionController的基类,该类主要完成系统异常错误捕捉处理
 * <p>
 * MultiActionController 继承该方法的类 可以在类中写多个方法，实现Controller的类中默认实行的方法是 handleRequest，MultiActionController相当于多个Controller  比较常用。
 * 在配置的时候需要注意 该类需要客户端传递个参数  
 * 1.参数值对应的值就是MultiActionController中想对应的方法  
 * 2.参数名在ApplicationContext.xml中的ParameterMethodNameResoler中定义  
 * </p>
 * <p>Title: 榕基RJ-CMS</p>
 * <p>Description: 所有ActionController的基类,该类主要完成系统异常错误捕捉处理</p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author HQJ
 * @version 1.0
 * @since	1.0.0	HQJ		2009-12-01
 */
@SuppressWarnings( { "serial", "unchecked" })
public class BaseActionController extends MultiActionController {
	public static final String isUseZXFT_KEY = "com.rongji.licence.zxft";

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
//			Map paraMap = request.getParameterMap();
//			String act = ((String[])paraMap.get("act"))[0];
////			if(LicenceCacheMgr.getInstance().isExpire()||!LicenceCacheMgr.getInstance().getBooleanValue("com.rongji.licence.zxft")){
//			if(true){
//				if(LicenceCacheMgr.getInstance().isExceptACT(act)){
//					return super.handleRequest(request, response);
//				}else{
//					CommandGroup cg = new CommandGroup("cg");
//					
////					popupMessageView(cg);
//					cg.add(licenceHint(request, response));	
//					if (request instanceof MultipartHttpServletRequest) {
//						return FrameworkHelper.setViewFeedbkForMultiDataForm((MultipartHttpServletRequest) request, response,
//								cg.asXML());
//					}
//					outPutXML(response, cg);
//					return null;
//				}
//			}else{
				return super.handleRequest(request, response);
//			}
			
			
		} catch (Exception ex) {
			CommandGroup cg = new CommandGroup("cg");
			Throwable t = ex.getCause();
			ex.printStackTrace();
			if (t != null) {
				
				 if (t instanceof IllegalAccessException) {
					cg.add(error(request, response, t));
				} else if (t instanceof NoSuchMethodException) {
					cg.add(error(request, response, t));
				} else if (t instanceof Exception) {
					cg.add(error(request, response, t));
				} else {
					ex.printStackTrace();
					cg.add(new AlertCommand("alt", ex.getMessage(), "img/p/alert-crack.gif", DialogPosition.middle, 5));
				}
				cg.add(new LoadingCommand("", false));
			} else {
				cg.add(new LoadingCommand("", false));
				cg.add(new AlertCommand("alt", ex.getMessage(), "img/p/alert-crack.gif", DialogPosition.middle, 5));
			}
			if (request instanceof MultipartHttpServletRequest) {
				return FrameworkHelper.setViewFeedbkForMultiDataForm((MultipartHttpServletRequest) request, response,
						cg.asXML());
			}
			outPutXML(response, cg);
			return null;
		}
	}
	


	public DialogCommand error(HttpServletRequest request, HttpServletResponse response, Throwable t) throws Exception {
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		BaseView view = buildDlgView4Error(loc, viewFactory, t);
		DialogCommand error = new DialogCommand("error",  ViewFactory.ID_DIALOG_STANDARD, "系统提示信息", "error_dlg",
				DialogCommand.WIDTH_MEDIUM, DialogCommand.HEIGHT_MEDIUM, DialogPosition.middle, null);
		error.setView(view);
		error.setCover(true);
		return error;
	}
	
	public BaseView buildDlgView4Error(Locale loc, ViewFactory viewFactory, Throwable t) {
		int errNum = -1; // 错误编号
		String errType = null; // 错误类型
		String errMsg = null; // 错误信息
		String errDetail = null; // 详细信息
		BaseView view = new BaseView();
		VerticalPanel root = new VerticalPanel(viewFactory.getId(LogicComponent.VIEW_ROOT), "*,30");
		FormPanel form = new FormPanel("f_form");
		form.setStyleClass("mx");
		form.setScroll(Scroll.auto);
		form.setCellspacing(0);
		form.setCellpadding(5);
		root.addSubPanel(form);
		
		form.add(new Label("", "", "<font color=\"#000000\"><b>错误编号：</b></font>"+errNum+"", false, false, true));
		form.add(new Label("", "", "<font color=\"#000000\"><b>错误类型：</b></font>"+errType+"", false, false, true));
		form.add(new Label("", "", "<font color=\"#000000\"><b>错误信息：</b></font>"+errMsg+"", false, false, true));
		form.add(new Label("", "", "<font color=\"#000000\"><b>详细信息：</b></font><br><font style='font-family:Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+errDetail+"</span></font>", false, false, true));
		
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(null);
		buttonBarPanel.setFace(ButtonFace.classic);
		buttonBarPanel.setAlign(Align.center);
		buttonBarPanel.setStyleClass("bg-form");
		buttonBarPanel.setCellspacing(8);
		buttonBarPanel.addButton(new ClickButton(null, "  关闭  ", "DFish.g_dialog(this).close()"));
		//buttonBarPanel.addButton(new ClickButton(null, "  取消  ", "DFish.g_dialog(this).close()"));
		root.addSubPanel(buttonBarPanel);
		view.setRootPanel(root);
		return view;
	}
	
	private Command licenceHint(HttpServletRequest request,
			HttpServletResponse response) {
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		BaseView view = buildDlgView4LicenceHint(loc, viewFactory );
		DialogCommand error = new DialogCommand("error",  ViewFactory.ID_DIALOG_STANDARD, "系统提示信息", "error_dlg",
				DialogCommand.WIDTH_SMALL, DialogCommand.HEIGHT_SMALL, DialogPosition.middle, null);
		error.setView(view);
		error.setCover(true);
		return error;
	}
	
	private BaseView buildDlgView4LicenceHint(Locale loc,
			ViewFactory viewFactory) {
		
		BaseView view = new BaseView();
		VerticalPanel root = new VerticalPanel(viewFactory.getId(LogicComponent.VIEW_ROOT), "*,30");
		FormPanel form = new FormPanel("f_form");
		form.setStyleClass("mx");
		form.setScroll(Scroll.auto);
		form.setCellspacing(0);
		form.setCellpadding(5);
		root.addSubPanel(form);
		
		form.add(new Label("", "", "<font color=\"#000000\"><b>项目LICENCE未开通或已过期，请联系管理员进行处理...</b></font>"+"", false, false, true));
//		form.add(new Label("", "", "<font color=\"#000000\"><b>错误类型：</b></font>"+errType+"", false, false, true));
//		form.add(new Label("", "", "<font color=\"#000000\"><b>错误信息：</b></font>"+errMsg+"", false, false, true));
//		form.add(new Label("", "", "<font color=\"#000000\"><b>详细信息：</b></font><br><font style='font-family:Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+errDetail+"</span></font>", false, false, true));
		
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(null);
		buttonBarPanel.setFace(ButtonFace.classic);
		buttonBarPanel.setAlign(Align.center);
		buttonBarPanel.setStyleClass("bg-form");
		buttonBarPanel.setCellspacing(8);
		buttonBarPanel.addButton(new ClickButton(null, "  关闭  ", "DFish.g_dialog(this).close()"));
		//buttonBarPanel.addButton(new ClickButton(null, "  取消  ", "DFish.g_dialog(this).close()"));
		root.addSubPanel(buttonBarPanel);
		view.setRootPanel(root);
		return view;
	}



	public static void popupMessageView(CommandGroup cg){
		StringBuilder sb = new StringBuilder();
		if(!LicenceCacheMgr.getInstance().getBooleanValue(isUseZXFT_KEY)){
			sb.append("在线访谈未开通，请进行注册开通!");
			
		}
		if(LicenceCacheMgr.getInstance().isExpire()){
			sb.append("Licenec已过期，请续延!");
		}
		if(Utils.notEmpty(sb.toString())){
			cg.add(new AlertCommand("alt",sb.toString(),"img/p/alert-info.gif",DialogPosition.middle,5));
		}
		
		
	}
	
}
