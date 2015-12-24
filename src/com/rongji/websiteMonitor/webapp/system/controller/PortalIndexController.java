package com.rongji.websiteMonitor.webapp.system.controller;


import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.google.gson.Gson;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.Panel;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.component.FlowPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.Hr;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.ImagePanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Label;

public class PortalIndexController extends MultiActionController {
	public ModelAndView systemNavigation(HttpServletRequest request, HttpServletResponse response)throws Exception {
		BaseView view = new BaseView();
	    FlowPanel fp = new FlowPanel("fp");//这个是主体内容
	    fp.setScroll(Scroll.miniscroll);
		List<String[]> listData = Arrays.asList(config);
		HtmlPanel baseConfigTitle = new HtmlPanel("title_" + 2,"<div class=m-tt-sysnav>服务管理</div>");
		fp.addSubPanel(baseConfigTitle);
		fp.addSubPanel(getImagePanel("leftGrid_" + 1, listData));
	    view.setRootPanel(fp);
		outPutXML(response, view);
		return null;
	}
	
	
	
	public Panel getImagePanel(String gridKey, List<String[]> dataList) {
		ImagePanel ip = new ImagePanel(gridKey);
		ip.setStyle("margin:0 13px");
		ip.setFilter(false);
		//ip.setOn(ImagePanel.EVENT_CLICK, "$click");
		ip.pub().addClickAttach("$click");
		ip.setScroll(Scroll.hidden);
		
		ip.setData(dataList);
		
		ip.addColumn(1, "name");
		ip.addColumn(0, "more");
		ip.addColumn(5, "icon");
		ip.addColumn(4, "click");
		ip.setImageArg(ImagePanel.KEY_SOURCE_FIELD, "icon");
		ip.setImageArg(ImagePanel.KEY_TITLE_FIELD, "name");
		ip.setImageArg(ImagePanel.KEY_HORIZONTAL_SPACE, 40);
		ip.setImageArg(ImagePanel.KEY_VERTICAL_SPACE, 20);
		ip.setImageArg(ImagePanel.KEY_MAX_WIDTH,108);
		ip.setImageArg(ImagePanel.KEY_MAX_HEIGHT, 70);
		return ip;

	}
	
	public static String getMenuJson(String userId){
		Map<String, LinkedHashMap<String, String>> menuMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		List<String[]> menuList = Arrays.asList(config);
		if(Utils.notEmpty(menuList)){
			for(String[] menu : menuList){
				LinkedHashMap<String, String> modeMap = new LinkedHashMap<String, String>();
				modeMap.put("id", menu[0]);
				modeMap.put("title", menu[1]);
				modeMap.put("icon", menu[2]);
				modeMap.put("src", "vm:|" + menu[3]);
				modeMap.put("clk",  "");
				menuMap.put(menu[0], modeMap);
			}
		}
		Gson json = new Gson();
		return json.toJson(menuMap);
	}
	
	static String[][] config = {
			{ "monitor", "网站监测","img/m/x16/sys_info.png",
					"monitor.sp?act=monitorIndex","index.wstab.openfile('monitor')","img/m/x48/2d/system.png" },
//			{ "category_manage", "客户端信息","img/m/x16/sys_info.png",
//					"monitor.sp?act=monitorIndex","index.wstab.openfile('category_manage')","img/m/x48/2d/system.png" },
//			{ "sys_setting", "性能监控","img/m/x16/sys_info.png",
//					"perform.sp?act=index","index.wstab.openfile('sys_setting')","img/m/x48/2d/sys_info.png" },
//			{ "sys_license", "证书服务","img/m/x16/sys_info.png",
//			"license.sp?act=index","index.wstab.openfile('sys_license')","img/m/x48/2d/sys_info.png" }		
            };
	
	
	
}
