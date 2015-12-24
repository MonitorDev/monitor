package com.rongji.websiteMonitor.webapp.system.view;

import java.util.Calendar;
import java.util.Locale;

import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.DialogTemplate;
import com.rongji.dfish.engines.xmltmpl.LogicComponent;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.framework.SystemData;

/**
 * 系统登录界面视图构建类
 * 
 * <p>Title: 榕基RJ-CMSV7.X</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright (c) 2009-2011</p>
 * <p>Company: 榕基软件开发有限公司</p>
 * 
 * @author HQJ
 * @version 1.0
 * @since	1.0.0	HQJ		2009-11-27
 */
public class LoginView {
	/**
	 * 构建登录首页视图
	 * 
	 * @param loc
	 * @param viewFactory
	 * @return
	 */
	public static BaseView buildIndexView(Locale loc, ViewFactory viewFactory) {
		BaseView view = new BaseView();
		view.addDialogTemplate((DialogTemplate) viewFactory.getAttribute(LogicComponent.DIALOG_STANDARD));
		view.add(new JSCommand("validImg",
						"var validImg = document.getElementById('validImg'); validImg.src='" +
						SystemData.getInstance().getServletInfo().getServletContext().getContextPath()+
						"/login.sp?act=validImg&v='+new Date().getTime();"));
		view.add(new JSCommand(
						"reset",
						"document.getElementById('loginName').value='';document.getElementById('passwd').value='';document.getElementById('checkCode').value='';"));
		view.add(new SubmitCommand("login", "login.sp?act=login", null, true));
		view.add(new JSCommand("submit","if(document.getElementById('loginName').value == ''){DFish.alert('登陆名不能为空.');return;}if(document.getElementById('passwd').value == ''){DFish.alert('密码不能为空.');return;} if(document.getElementById('checkCode').value==''){DFish.alert('验证码不能为空.');return;}VM(this).cmd('login');"));
		view.addLoadEvent("document.getElementById('maincon').style.height=document.documentElement.clientHeight+'px';");

		String rootHtml = ""
				+ "<div class='wrapper_login' id='maincon'>"
				+ "<div class='main_login'>"
				+ "<div class='login_ft clearfix'>"
				+ "<form onsubmit='return false'>"
				+ "<table>"
				+ "<tr>"
				+ "<td>用 户 名：</td>"
				+ "<td colspan='2'><input type='text' name='loginName' value='' id='loginName' class='wbk'/></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>密    &nbsp;&nbsp;&nbsp;码：</td>"
				+ "<td colspan='2'><input type='password' name='passwd' id='passwd' class='wbk' value=''/></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>验 证 码：</td>"
				+ "<td><input type='text' name='checkCode' id='checkCode' class='wbk_s'/></td>"
				+ "<td><img id='validImg' onclick='VM(this).cmd(\"validImg\")' src='"+SystemData.getInstance().getServletInfo().getServletContext().getContextPath()+"/login.sp?act=validImg' alt=\"校验码图形(点击刷新)\" /></td>"
				+ "</tr>"
				+ "</table>"
				+ "<div class='btn_div'>"
				+ "<input type='submit' value='登  录'  onclick='VM(this).cmd(\"submit\")' class='login_btn' />&nbsp;&nbsp;"
				+ "<input type='reset' onclick='VM(this).cmd(\"reset\")' value='重  置' class='login_btn' />"
				+ "</div></form>"
				+ "</div>"
				+ "</div>"
				+ "<div class='copyright_div'>"
				+ "<div class='copyright'>"
				+ "<div>Tel:800-858-1121</div>"
				+ "<div>The Site Requires Internet Explorer 6.0+. Best View At 1024*768</div>"
				+ "<div>Copyright <font face=verdana>&copy;</font> 2000-"+Calendar.getInstance().get(Calendar.YEAR)+" Rongji Software Co.Ltd. All Rights Reserved.  </div>"
				+ "</div>" + "</div>" + "</div>";
		HtmlPanel root = new HtmlPanel("f_html", rootHtml);
		root.setScroll(Scroll.auto);
		root.setStyle("position:relative;");
		view.setRootPanel(root);
		return view;
	}

}