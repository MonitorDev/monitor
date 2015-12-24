package test.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rongji.dfish.engines.xmltmpl.Align;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.ButtonFace;
import com.rongji.dfish.engines.xmltmpl.Command;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.Scroll;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.XMLObject;
import com.rongji.dfish.engines.xmltmpl.command.AjaxCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.DialogCommand;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.command.SubmitCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.engines.xmltmpl.component.ButtonBarPanel;
import com.rongji.dfish.engines.xmltmpl.component.FormPanel;
import com.rongji.dfish.engines.xmltmpl.component.GridPanel;
import com.rongji.dfish.engines.xmltmpl.component.HorizontalPanel;
import com.rongji.dfish.engines.xmltmpl.component.Horizontalgroup;
import com.rongji.dfish.engines.xmltmpl.component.HtmlPanel;
import com.rongji.dfish.engines.xmltmpl.component.SourcePanel;
import com.rongji.dfish.engines.xmltmpl.component.TabPanel;
import com.rongji.dfish.engines.xmltmpl.component.VerticalPanel;
import com.rongji.dfish.engines.xmltmpl.form.Combobox;
import com.rongji.dfish.engines.xmltmpl.form.EmbededButton;
import com.rongji.dfish.engines.xmltmpl.form.Label;
import com.rongji.dfish.engines.xmltmpl.form.Select;
import com.rongji.dfish.engines.xmltmpl.form.Spinner;
import com.rongji.dfish.engines.xmltmpl.form.Text;
import com.rongji.websiteMonitor.common.util.Utils;


public class TestView {

	public static BaseView buildIndexView(Locale loc,ViewFactory viewFactory) {
		BaseView view = new BaseView();
		VerticalPanel right = new VerticalPanel("m-main", "57,*");
		right.setStyleClass("bd-main bg-white");
		HtmlPanel titleHtml = new HtmlPanel(null, "监测点测试");
		HorizontalPanel title = new HorizontalPanel("f_title", "*,1,700");
		title.setStyle("margin:0 30px");
		title.setStyleClass("tt-main bd-form bd-onlybottom");
		ButtonBarPanel buttonBarPanel = new ButtonBarPanel(ViewFactory.ID_PANEL_BUTTON_BAR);
		buttonBarPanel.setFace(ButtonFace.group);
		buttonBarPanel.setAlign(Align.right);
		title.addSubPanel(titleHtml,HtmlPanel.EMPTY,buttonBarPanel);
		TabPanel tabPanel = new TabPanel("tp");
		tabPanel.setStyle("margin-top:2px;");
		tabPanel.setStyle("margin:0 30px");
		tabPanel.setVerticalMinus(-4);
		tabPanel.setHorizontalMinus(-5);
		tabPanel.setFilmHmins(2);
		tabPanel.setFilmVmins(2);
		tabPanel.setFilmStyle("border:0px;");
		String souseUrl = "vm:|test.sp?act=detectionHttpUrl";
		String onliveURL = "vm:|test.sp?act=pingHttpUrl";
		tabPanel.addSubPanel(null, "网页/HTTP", new SourcePanel("s1", souseUrl));
		tabPanel.addSubPanel(null, "Ping",new SourcePanel("s2",onliveURL));
		right.addSubPanel(title, tabPanel);
		view.setRootPanel(right);
		return view;
	}

	public static XMLObject buildDetectionHttpUrlView(ViewFactory viewFactory) {
		BaseView view = new BaseView();
		VerticalPanel root = new VerticalPanel("root", "50,*");
		HtmlPanel panel = new HtmlPanel(null, "网页/HTTP检测");
		panel.setStyle("margin:0 30px");
		panel.setStyleClass("tt-main bd-form bd-onlybottom");
		VerticalPanel vp = new VerticalPanel("f_root", "140,*");
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		// GridPanel gridPanel = viewFactory.findGridPanel(view);
		// gridPanel.setId("grid");
		HtmlPanel htmlPanel = new HtmlPanel("html", "xcx");
		
		fillFormPanel(formPanel);
		fillHtmlPanel(htmlPanel, null);
		// fillGridPanel(gridPanel);
		vp.addSubPanel(formPanel, htmlPanel);
		root.addSubPanel(panel, vp);
		view.setRootPanel(root);
		fillCommand(view);
		return view;
	}

	private static void fillCommand(BaseView view) {
//		view.add(new SubmitCommand("test", "test.sp?act=test" , "form", true));
		Command jsCommand = new JSCommand("detection",
				"VM(this).cmd('load');VM(this).cmd('detectionUrl',$0,$1,$2);");
		Command detectionUrl = new AjaxCommand("detectionUrl",
				"test.sp?act=detectionUrl&url=$0&num=$1&monitorPoint=$2");
		Command loading = new LoadingCommand("load", true);

		Command showResponseHead = new DialogCommand("showResponseHead",
				ViewFactory.ID_DIALOG_STANDARD, "HTTP响应头信息", "msg", 500, 400,
				DialogPosition.middle,
				"vm:|test.sp?act=showResponseHead&index=$0&row=$1");
		Command showResponseTime = new DialogCommand("showResponseTime",
				ViewFactory.ID_DIALOG_STANDARD, "HTTP响应时间分析", "msg", 500, 400,
				DialogPosition.middle,
				"vm:|test.sp?act=showResponseTime&index=$0&row=$1");
		view.add(jsCommand);
		view.add(loading);
		view.add(detectionUrl);
		view.add(showResponseHead);
		view.add(showResponseTime);
	}

	private static void fillHtmlPanel(HtmlPanel htmlPanel,
			List<List<Map<String, String>>> list) {
		htmlPanel.setScroll(Scroll.auto);
		StringBuilder str = new StringBuilder();

		str
				.append("<table width='90%' border='0' cellspacing='0' cellpadding='1' style='border-bottom:0;display: table;font-size: 12px;border: 1px solid #BBCCED;'>");
		str
				.append(" <tbody><tr style='font-weight: bold;font-size: 14px;line-height: 18px;background-color: #E5ECF9;vertical-align: middle;height: 35px;'>");
		str
				.append("        <td style='text-align:left;width:160px;padding-left:20px;'>分布式监测点</td>");
		str.append("        <td style='width:160px;'>IP</td>");
		str.append("        <td style='text-align:left;'>检测结果</td>");
		str.append("        <td style='text-align:right;'>状态</td>");
		str.append("        <td style='text-align:right;'>响应时间</td>");
		str
				.append("        <td style='text-align:right;padding-right:20px;'>下载字节数</td>");
		str.append("</tr>");

		Map<String, String> map = null;
		List<Map<String, String>> listMap = null;
		if (Utils.notEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				listMap = list.get(i);
				if(Utils.notEmpty(listMap)){
					for(int j=0;j<listMap.size();j++){
						map = listMap.get(j);
						str.append(" <tr>");
						str.append("<td style='text-align:left;padding-left:20px;'>"
								+ map.get("address") + "</td>");
						str.append("<td style=''>" + map.get("ip") + "</td>");

						str
								.append("<td style='text-align:left;'>"
										+ ((!"无响应".equals(map.get("result"))&&!"失败".equals(map.get("result"))) ? "<span style='color: #28AB17;'>"
												+ map.get("result") + "</span>"
												:("失败".equals(map.get("result")))?"<span style='color:#FF0000;'>"+map.get("result")+"<span>": map.get("result")) + "</td>");
						str
								.append("<td style='text-align:right;'>"
										+ ((!"无响应".equals(map.get("result"))&&!"失败".equals(map.get("result"))) ? "<span><span class='status_1'>"
												+ map.get("status") + "</span></span>"
												:("失败".equals(map.get("result")))?"<span style='color:#FF0000;'>"+map.get("status")+"<span>": map.get("status")) + "</td>");
						
						str
								.append("<td style='text-align:right;'>"
										
								
								
							
										+ ((!"无响应".equals(map.get("result"))) ? "<span style='color:#28AB17'>" +
												"<span onclick='VM(this).cmd(\"showResponseTime\",\""
												+ i
												+ "\",\""
												+ j
												+ "\")' style='padding-top: 0pt;float: right;margin-left: 10px;cursor: pointer;'><img title='HTTP响应时间分析' src='img/b/icon_resptime_detail.gif'/></span>"
												+ map.get("totalTime")==null?0:map.get("totalTime") + "</span>"
												: map.get("totalTime")==null?0:map.get("totalTime")) + "</td>");
						str.append("<td style='text-align:right;padding-right:20px;'>"
								+ (Utils.notEmpty(map.get("size")) ? map.get("size")
										: "读取数据超时") + "</td>");
						str.append("</tr>");
					}
				}
				
			}
		}

		str.append("</tbody></table>");
		htmlPanel.setHtml(str.toString());

	}

	private static void fillFormPanel(FormPanel formPanel) {
		Horizontalgroup hg = new Horizontalgroup("hg", "请输入URL");
		// hg.setFullLine(true);
		Text text = new Text("url", "请输入URL", "http://www.baidu.com", 100);
		text.setWidth("500");
		text.setGrayTip("比如:http://www.baidu.com/index.html");
		Label button = new Label(
				"button",
				"",
				"<a href='javascript:void(0);' onclick=VM(this).cmd('detection',VM(this).fv(\"url\"),VM(this).fv(\"num\"),VM(this).fv(\"monitorPoint\"))>开始检测</a>");
		button.setFilter(false);
		// EmbededButton button = new EmbededButton("开始检测","");
		// button.setOn("click", "alert(1);");
		hg.add(text, new Label("label1", "", "     "), button);
		formPanel.add(hg);
		Spinner num = new Spinner("num","触发次数",1,1,100,1);
//		Text num = new Text("num","触发次数","10",-1);
		num.setWidth("480");
		formPanel.add(num);
		Horizontalgroup horizontalgroup = new Horizontalgroup("hg2", "监控服务器");
		Combobox monitoerPoint = new Combobox("monitorPoint","监控点",null,"loading",
				"subproject.sp?act=typeGridOptions",null,null, 200, false, false, false, true,
				false, true);
		monitoerPoint.setWidth("500");
		monitoerPoint.setNotnull(true);
//		Label label = new Label("l2", "",
//				"&nbsp;&nbsp;&nbsp;&nbsp;(<font color='red'>选择越多台，速度越慢</font>)");
//		label.setFilter(false);
//		horizontalgroup.add(monitoerPoint);

		formPanel.add(monitoerPoint);

	}

	public static XMLObject updateDetectionHttpUrlView(ViewFactory viewFactory,
			List<List<Map<String, String>>> list) {
		CommandGroup cg = new CommandGroup("");
//		cg.setPath("/user_manage/s1");
		UpdateCommand updateCommand = new UpdateCommand("htmlUpdate");
		HtmlPanel htmlPanel = new HtmlPanel("html", null);
		fillHtmlPanel(htmlPanel, list);
		updateCommand.setContent(htmlPanel);
		cg.add(updateCommand);
		return cg;
	}

	public static XMLObject buildShowResponseHead(ViewFactory viewFactory,
			Map<String, String> map) {
		BaseView view = viewFactory.buildGridView(false, false, false, false);
		GridPanel grid = viewFactory.findGridPanel(view);
		Collection<Object[]> col = new ArrayList<Object[]>();
		int i = 0;
		String headContent = map.get("headContent");
		if(headContent!=null&&!"".equals(headContent)){
			i = headContent.indexOf("\n");
		}
		if(i!=-1){
			headContent = headContent.substring(i+1);
		}
		String [] spits = headContent.split("\n");
		String [] temp = null;
		if(spits!=null&&spits.length>0){
			for(String str:spits){
				temp = str.split(": ");
				if(temp!=null&&temp.length>0){
					if(temp.length>1){
						col.add(new Object[] { temp[0],temp[1]});
					}else{
						col.add(new Object[] { temp[0],null});
					}
				}
				
			}
		}	
	
		grid.setData(col);
		grid.addTextColumn(0, "id", "参数", "150", "id");
		grid.addTextColumn(1, "c1", "内容", "*", "c1");
		return view;
	}

	public static XMLObject buildShowResponseTime(ViewFactory viewFactory,
			Map<String, String> map) {
		BaseView view = viewFactory.buildGridView(false, false, false, false);
		GridPanel grid = viewFactory.findGridPanel(view);
		Collection<Object[]> col = new ArrayList<Object[]>();
		int i = 0;
			
		col.add(new Object[] { "DNS域名解析",map.get("time_namelookup")});
		col.add(new Object[] { "建立连接",map.get("linkTime")});
		col.add(new Object[] { "服务器计算",map.get("serverArTime")});
		col.add(new Object[] { "下载内容",map.get("downloadTime")});



		// col.add(new Object[] { "1", "GirdPanel", "用于表达数据表格" });
		// col.add(new Object[] { "2", "FormPanel", "用于表达表单界面" });
		// col.add(new Object[] { "3", "TreePanel", "用于展示动态装载树" });
		grid.setData(col);
		grid.addTextColumn(0, "id", "步骤", "150", "id");
		grid.addTextColumn(1, "c1", "时间", "*", "c1");
		return view;
	}
	
	public static XMLObject buildPingHttpUrlView(ViewFactory viewFactory) {
		BaseView view = new BaseView();
		VerticalPanel root = new VerticalPanel("root", "50,*");
		HtmlPanel panel = new HtmlPanel(null, "Ping");
		panel.setStyle("margin:0 30px");
		panel.setStyleClass("tt-main bd-form bd-onlybottom");
		VerticalPanel vp = new VerticalPanel("f_root", "140,*");
		FormPanel formPanel = new FormPanel("form");
		formPanel.setScroll(Scroll.hidden);
		// GridPanel gridPanel = viewFactory.findGridPanel(view);
		// gridPanel.setId("grid");
		HtmlPanel htmlPanel = new HtmlPanel("html", "xcx");
		fillPingFormPanel(formPanel);
		fillPingHtmlPanel(htmlPanel,null);
		// fillGridPanel(gridPanel);
		vp.addSubPanel(formPanel, htmlPanel);
		root.addSubPanel(panel, vp);
		view.setRootPanel(root);
		fillPingCommand(view);
		return view;
	}

	private static void fillPingCommand(BaseView view) {
		Command jsCommand = new JSCommand("ping",
				"VM(this).cmd('load');VM(this).cmd('pingUrl',$0,$1,$2);");
		Command detectionUrl = new AjaxCommand("pingUrl",
				"test.sp?act=pingUrl&url=$0&num=$1&monitorPoint=$2");
		Command loading = new LoadingCommand("load", true);
		
		Command showResponseHead = new DialogCommand("showResponseHead",
				ViewFactory.ID_DIALOG_STANDARD, "HTTP响应头信息", "msg", 500, 400,
				DialogPosition.middle,
				"vm:|jkbTest.sp?act=showResponseHead&index=$0");
	
		view.add(jsCommand);
		view.add(loading);
		view.add(detectionUrl);
		view.add(showResponseHead);
		
	}

	private static void fillPingHtmlPanel(HtmlPanel htmlPanel,
			List<List<Map<String, String>>> list) {
		htmlPanel.setScroll(Scroll.auto);
		StringBuilder str = new StringBuilder();

		str
				.append("<table width='90%' border='0' cellspacing='0' cellpadding='1' style='border-bottom:0;display: table;font-size: 12px;border: 1px solid #BBCCED;'>");
		str
				.append(" <tbody><tr style='font-weight: bold;font-size: 14px;line-height: 18px;background-color: #E5ECF9;vertical-align: middle;height: 35px;'>");
		str
				.append("        <td style='text-align:left;width:160px;padding-left:20px;'>分布式监测点</td>");
		str.append("        <td style='width:160px;'>IP</td>");
		str.append("        <td style='text-align:left;'>检测结果</td>");
		str.append("        <td style='text-align:right;'>最小响应时间</td>");
		str.append("        <td style='text-align:right;'>平均响应时间</td>");
		str
				.append("        <td style='text-align:right;padding-right:20px;'>最大响应时间</td>");
		str.append("</tr>");
		List<Map<String, String>> listMap = null;
		Map<String, String> map = null;
		if (Utils.notEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				listMap = list.get(i);
				for(int j=0;j<listMap.size();j++){
					map = listMap.get(j);
					str.append(" <tr>");
					str.append("<td style='text-align:left;padding-left:20px;'>"
							+ map.get("address") + "</td>");
					str.append("<td style='width:160px;'>" + map.get("ip") + "</td>");

					str
							.append("<td style='text-align:left;'><span id='__status_1'><span style='color:#28AB17;'>"
									+ ((!"不可用".equals(map.get("result"))&&!map.get("result").startsWith("丢包")) ? "<span style='color: #28AB17;'>"
											+ map.get("result") + "</span>"
											:"<span style='color:#FF0000;'>"+map.get("result")+"<span>") + "</span></span></td>");
					str
							.append("<td style='text-align:right;'>"
									+ ((!"不可用".equals(map.get("result"))) ? "<span id='__min_1'><span style='color:#33CCFF'>"
											+ map.get("min") + "</span></span>"
											:"")+"</td>");
					
					str
							.append("<td style='text-align:right;'><span id='__avg_1'>"
									+ ((!"不可用".equals(map.get("result"))) ? "<span style='color:#33CCFF'>"
											+ map.get("avg") + "</span>"
											:"")+"</span></td>");
					str.append("<td style='text-align:right;padding-right:20px;'><span id='__max_1'><span style='color:#33CCFF'>"
							+ ((!"不可用".equals(map.get("result"))) ? map.get("max")
									: "") + "</span></span></td>");
					str.append("</tr>");
				}
				
				
			}
		}

		str.append("</tbody></table>");
		htmlPanel.setHtml(str.toString());
		
	}

	private static void fillPingFormPanel(FormPanel formPanel) {
		Horizontalgroup hg = new Horizontalgroup("hg", "服务器IP地址或域名");
		// hg.setFullLine(true);
		Text text = new Text("text", "服务器IP地址或域名", "218.5.2.35", 100);
		text.setWidth("500");
		text.setGrayTip("比如:218.5.2.35或www.baidu.com");
		Label button = new Label(
				"button",
				"",
				"<a href='javascript:void(0);' onclick=VM(this).cmd('ping',VM(this).fv(\"text\"),VM(this).fv(\"num\"),VM(this).fv(\"monitorPoint\"))>开始检测</a>");
		button.setFilter(false);
		// EmbededButton button = new EmbededButton("开始检测","");
		// button.setOn("click", "alert(1);");
		hg.add(text, new Label("label1", "", "     "), button);
		formPanel.add(hg);
		Spinner num = new Spinner("num","触发次数",1,1,100,1);
//		Text num = new Text("num","触发次数","10",-1);
		num.setWidth("480");
		formPanel.add(num);
		Horizontalgroup horizontalgroup = new Horizontalgroup("hg2", "监控服务器");
		Combobox monitoerPoint = new Combobox("monitorPoint","监控点",null,"loading",
				"subproject.sp?act=typeGridOptions",null,"VM(this).cmd('alert','$0')", 200, false, false, false, true,
				false, true);
		monitoerPoint.setWidth("500");
		monitoerPoint.setNotnull(true);

		formPanel.add(monitoerPoint);
		
	}

	public static CommandGroup updatePingUrlView(ViewFactory viewFactory,
			List<List<Map<String, String>>> list) {
		CommandGroup cg = new CommandGroup("");
		UpdateCommand updateCommand = new UpdateCommand("htmlUpdate");
		HtmlPanel htmlPanel = new HtmlPanel("html", null);
		fillPingHtmlPanel(htmlPanel, list);
		updateCommand.setContent(htmlPanel);
		cg.add(updateCommand);
		return cg;
	}
	
	
	

}
