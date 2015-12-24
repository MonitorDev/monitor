package com.rongji.dfish.webapp.pub.controller;

import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.webapp.pub.view.EditorSupportView;

public class EditorSupportController extends MultiActionController {
	
	/**
	 * 插入图片视图
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView eidtorImgView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Locale loc = FrameworkHelper.getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		viewFactory.setLocale(loc);

		String edtId = Utils.getParameter(request, "edtId");
		DialogCommand dg = new DialogCommand("d",
				ViewFactory.ID_DIALOG_STANDARD, "插入图片", "",
				DialogCommand.WIDTH_MEDIUM, DialogCommand.HEIGHT_MEDIUM,
				DialogPosition.middle, "");
		dg.setView(EditorSupportView.buildEditorImgView(viewFactory, edtId));

		outPutXML(response, dg);
		return null;
	}
	
	/**
	 * 图片上传
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView uploadImg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(!(request instanceof MultipartHttpServletRequest)) {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{r:'n',n:'请选择上传的图片'}");
			return null;
		}
		
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
		Map<?,?> fileMap= mr.getFileMap();
		if(fileMap.size()==0){
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{r:'n',n:'请选择上传的图片'}");
			return null;
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(Utils.joinString("{r:'y',n:'上传成功',img:{n:'',s:'img/bg.jpg'}}"));
		return null;
	}
	
	/**
	 * 资源上传
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		return null;
	}
	

}
