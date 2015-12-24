package com.rongji.websiteMonitor.platform.ums.style;

import javax.servlet.http.HttpServletRequest;

import com.rongji.dfish.framework.FrameworkHelper;

public class ItaskStyleManager {
//	private static ItaskStyleManager instance=new ItaskStyleManager();
//	public static ItaskStyleManager getInstance(){
//		return instance;
//	}
	public static ItaskStyle getStyle(HttpServletRequest request){
		String loginUser=FrameworkHelper.getLoginUser(request);
		if(loginUser==null)loginUser="default";
		ItaskStyle st=null;
		st = new I73dStyle();
		//st.setMobile(FrameworkHelper.isFromMobile(request));
		st.setLocale(FrameworkHelper.getLocale(request));
		return st;
	}
	
}
