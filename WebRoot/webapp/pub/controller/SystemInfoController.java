package com.rongji.dfish.webapp.pub.controller;

import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.base.info.EthNetInfo;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.BtnCmdFactory;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.XMLFrag;
import com.rongji.dfish.engines.xmltmpl.XmlTmplHelper;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.Fieldset;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.Hr;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.util.DataBaseInfo;
import com.rongji.dfish.base.FileUtil;
import com.rongji.dfish.util.ServletInfo;
import com.rongji.dfish.util.SystemInfo;
import com.rongji.itask7.web.pub.controller.ExceptionCaptureController;

public class SystemInfoController extends ExceptionCaptureController{
	public ModelAndView index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
//		com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel
		
		
		BaseView view = viewFactory.buildFormView(true, true, true);
//		FormPanel f = viewFactory.findFormPanel(view);
		HorizontalPanel main=new HorizontalPanel(ViewFactory.ID_PANEL_FORM,"50%,*");
		view.replacePanelById(main);
		main.setScroll(Scroll.scroll);
		FormPanel f1 = viewFactory.getDefaultFormPanel();
		f1.setId("f_form1");
		f1.setScroll(Scroll.hidden);
		FormPanel f2 = viewFactory.getDefaultFormPanel();
		f2.setId("f_form2");
		f2.setScroll(Scroll.hidden);
		main.addSubPanel(f1,f2);
		
		HtmlPanel title = viewFactory.findTitlePanel(view);
		title.setHtml("系统信息");//
	
		ButtonBarPanel bbp=viewFactory.findBttonPanel(view);
		BtnCmdFactory.DEFAULT.addRefreshBtn(viewFactory.getLocale(), view, bbp);

		Fieldset fsSystem=new Fieldset("操作系统");//操作系统
		SystemInfo sysinfo=SystemData.getInstance().getSysinfo();
		fsSystem.add(new Label("","操作系统",sysinfo.getOperationSystem()));//操作系统
		fsSystem.add(new Label("","默认字符集",sysinfo.getFileEncoding()));//默认字符集
		

		
		long sysMemFree=0L;
		long sysMemMax=0L;
		try{
			OperatingSystemMXBean mxbean=ManagementFactory.getOperatingSystemMXBean();
			if ("com.sun.management.OperatingSystem".equals(mxbean.getClass().getName())) {
			com.sun.management.OperatingSystemMXBean sunOSMXBean = (com.sun.management.OperatingSystemMXBean) mxbean;
			sysMemFree=sunOSMXBean.getFreePhysicalMemorySize();
			sysMemMax= sunOSMXBean.getTotalPhysicalMemorySize();
			
			}

		}catch(Throwable t){}
		String memStatus2=FileUtil.getHumanSize(sysMemFree) +
		"(free) / " + FileUtil.getHumanSize(sysMemMax);
		Label memLab2=new Label("","内存大小(整机)",memStatus2);
		memLab2.setFilter(false);
		fsSystem.add(memLab2);//内存大小
	
		Set<String> macAdrs = null;
		try {
			macAdrs = EthNetInfo.getAllMacAddress();
		} catch (Exception ex) {
			macAdrs = new HashSet<String>();
		}
		StringBuilder macAdrStrB = new StringBuilder();
		for (Iterator iter = macAdrs.iterator(); iter.hasNext();) {
			Object item = iter.next();
			macAdrStrB.append(item);
			if (iter.hasNext()) {
				macAdrStrB.append("<br/>");
			}
		}
		Label macLab=new Label("mac", "MAC地址", macAdrStrB);
		macLab.setFilter(false);
		fsSystem.add(macLab);//MAC地址
		
		
		Label more=new Label("","","<a href=\"#\" onclick=\"VM(this).cmd('showDetail')\">更多</a>");
		more.setFilter(false);
		fsSystem.add(more);//内存大小

		Fieldset fsServer=new Fieldset("服务器信息");//服务器信息
		ServletInfo servinfo=SystemData.getInstance().getServletInfo();
		fsServer.add(new Label("","servlet版本",servinfo.getServletVersion()));//servlet版本
		fsServer.add(new Label("","应用路径",servinfo.getServletRealPath()));//应用路径
		
		String memStatus=FileUtil.getHumanSize(Runtime.getRuntime().freeMemory()) +
		"(free) / " + FileUtil.getHumanSize(Runtime.getRuntime().totalMemory());
		Label memLab=new Label("","内存大小(JVM)",memStatus);
		memLab.setFilter(false);
		fsServer.add(memLab);//内存大小
				
		int tCount=ManagementFactory.getThreadMXBean().getThreadCount();
		Label tCountLab=new Label("","当前线程数",tCount);
		fsServer.add(tCountLab);//当前会话数

		
		Fieldset fsDatabase=new Fieldset("数据库信息");//数据库信息
		DataBaseInfo dbinfo=SystemData.getInstance().getDataBaseInfo();
		if(dbinfo==null){
			//数据库无法连接，或者不支持该数据库！
			fsDatabase.add(new Label("","错误信息","数据库无法连接，或者不支持该数据库！"));
		}else{
			fsDatabase.add(new Label("","名称",dbinfo.getDatabaseProductName()));//名称
			fsDatabase.add(new Label("","版本",dbinfo.getDatabaseProductVersion()));//版本
			fsDatabase.add(new Label("","连接字",dbinfo.getDatabaseUrl()));//连接字
			fsDatabase.add(new Label("","用户",dbinfo.getDatabaseUsername()));//用户
		}
		
		Fieldset authInfo = new Fieldset("系统授权");//系统授权
		authInfo.add(new Label("", "框架版本", XMLFrag.OVERALL_VERSION));
//		authInfo.addLabel("", getMsg(loc,"m.pub.framework_limit_date")/*"有效期限"*/, BaseView.getExpire());
		Map<String,String>linceseInfo=getLicenseData();
		String lisnMac= linceseInfo.get("MAC");
		boolean validate=lisnMac!=null&&macAdrs.contains(lisnMac);
		if(linceseInfo.size()==0){
			authInfo.add(new Label("","错误","没有证书信息"));
		}else if(!validate){
			Label error=new Label("","错误","<font color=red>以下证书信息在本机是无效的</red>");
			error.setFilter(false);
			authInfo.add(error);
		}
		for(Map.Entry<String, String> entry:linceseInfo.entrySet()){
			authInfo.add(new Label("",entry.getKey(),entry.getValue()));
		}
		f1.add(fsSystem).add(fsServer).add(fsDatabase);
		f2.add(authInfo);
		
		//显示详细信息
//		String s, String s1, String s2, String s3, int l, int i1, DialogPosition dialogposition, 
//        String s4
		DialogCommand dialog=new DialogCommand("showDetail",ViewFactory.ID_DIALOG_STANDARD,"详细信息","win",DialogCommand.WIDTH_SMALL,DialogCommand.HEIGHT_MEDIUM,DialogCommand.POSITION_MIDDLE,
				"vm:|systemInfo.sp?act=showDetail");	
		view.add(dialog);
		outPutXML(response,view);
		return null; 
	}
	
	private double getSuguestMax(int count) {
		if(count >5000){
			return 10000;
		}else if(count >2000){
			return 5000;
		}else if(count >1000){
			return 2000;
		}else if(count >500){
			return 1000;
		}else if(count >200){
			return 500;
		}else if(count >100){
			return 200;
		}else if(count >50){
			return 100;
		}else if(count > 20){
			return 50;
		}else{
			return 20;
		}
	}

	public ModelAndView showDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ViewFactory viewFactory=FrameworkHelper.getViewFactory(request);
		BaseView view = viewFactory.buildFormView(false, false, false);
		FormPanel form=viewFactory.findFormPanel(view);

		form.add(new Hr(2,"环境信息"));
		showMap(form,System.getenv());
		form.add(new Hr(2,"属性信息"));
		showMap(form,System.getProperties());
		
		
		outPutXML(response, view);
		return null; 
	}
	private void showMap(FormPanel panel,Map map){
		Set<?> keyList=new TreeSet(map.keySet());
		
		for(Object o:keyList){
			System.out.println(o.getClass().getName());
			String title=String.valueOf(o);
			String value=String .valueOf(map.get(o));

			String text="<div style=word-break:break-all>"+XmlTmplHelper.escape(value)+"</div>";
			if(title.length()>20){
				Label titleLab=new Label("","",title+" :");
				titleLab.setFullLine(true);
				panel.add(titleLab);
				Label textLab=new Label("","",text);
				textLab.setFilter(false);
				panel.add(textLab);
			}else{
				Label textLab=new Label("",title,text);
				textLab.setFilter(false);
				panel.add(textLab);
			}
		}
	}
	
	private static byte[] pubKeyBtyeArray=new byte[]{0, -39, 95, 60, -26, -55, 1, 113, 119};
	private static byte[] modBtyeArray=new byte[]{0, -102, -10, -3, 54, 25, -122, 16, 85, 110, 6, -8, 46, -73, 38, 29, 37, 3, -62, 57, 40, 98, 95, 97, 19, -100, -41, 40, 108, 31, -106, -18, 67, -68, 67, 70, 19, -64, -52, 56, 78, 73, 73, 88, 17, -58, -56, 38, 77, -86, -123, -110, 97, -114, -46, 69, 61, 104, 40, 10, -49, 45, 28, 124, 121, 21, -61, -79, -104, -118, -64, -3, 43, -29, -80, 46, 53, 72, 120, 84, 3, 20, 36, -125, -97, 83, 49, 2, -43, -117, 12, -104, -59, -66, 94, 3, -103, -47, 74, -40, -86, -72, 58, 121, -71, -56, 49, -98, 32, -103, -53, 70, -65, 125, -20, -60, 120, -7, -80, -17, 6, 23, 23, -105, 88, -100, -58, 36, -20, 79};
	private static StringCryptor sc_beta =
	      CryptFactory.getStringCryptor(
	          CryptFactory.RSA, CryptFactory.UTF8,
	          CryptFactory.BASE64, new BigInteger[] {
	          new BigInteger(pubKeyBtyeArray),
	          new BigInteger("0"),
	          new BigInteger(modBtyeArray)});
	public static final Map<String,String> getLicenseData(){
		Map<String,String> result=new TreeMap<String,String>();
		try{
		ResourceBundle rb=ResourceBundle.getBundle("com.rongji.dfish.engines.xmltmpl.license");
		String code=rb.getString("code");
		String decode=sc_beta.decrypt(code);
		for(String pair:decode.split("&")){
			String[]s=pair.split("=");
			if(s.length<2)continue;
			try {
				result.put(s[0], java.net.URLDecoder.decode(s[1],"UTF-8"));
			} catch (UnsupportedEncodingException e) {}
		}
		}catch(Throwable t){}
		return result;
	}
}
