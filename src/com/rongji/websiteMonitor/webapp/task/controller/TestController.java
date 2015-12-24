package com.rongji.websiteMonitor.webapp.task.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.webapp.BaseActionController;

public class TestController extends BaseActionController {

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<PubScheduler> list = null;
		PubCommonDAO dao = FrameworkHelper.getDAO();
		list = dao.getQueryList("FROM PubScheduler t ");
		for(PubScheduler ps : list) {
			String str = ps.getConfigXml();
			str = str.replace("monitorPoint:00000001%3B00000003%3B00000002", "monitorPoint:00000001");
			System.out.println(ps.getSchlId() + ", " + str);
			ps.setConfigXml(str);
			dao.updateObject(ps);
		}
		return null;
	}
}
