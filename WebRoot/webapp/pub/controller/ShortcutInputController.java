package com.rongji.dfish.webapp.pub.controller;

import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.webapp.pub.view.EditorView;


public class ShortcutInputController extends MultiActionController{
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaseView view = new BaseView();
		EditorView.fillShortcutDialogView(view, FrameworkHelper.getViewFactory(request));
		outPutXML(response,view);
		return null;
	}
	public ModelAndView queryClz(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ViewFactory  viewFactory= FrameworkHelper.getViewFactory(request);
		VerticalPanel main = new VerticalPanel("main", "*", 
				viewFactory.getStyleClass(LogicComponent.BORDER_DEEP), null, 2, 2);
		main.setStyle("background:white");

		String clzId = Utils.getParameter(request,"clzId");

		UpdateCommand uc = new UpdateCommand("");
		uc.getView().setRootPanel(main);
		EditorView.fillShtctMainPanel(main, uc.getView(), viewFactory, clzId);

		outPutXML(response, uc);
		return null;
	}

	
}
