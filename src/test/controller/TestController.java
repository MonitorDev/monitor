package test.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import test.help.DetectionUrlThread;
import test.help.PingUrlThread;
import test.view.TestView;

import com.google.gson.Gson;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.LoadingCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.ServiceLocator;


public class TestController extends MultiActionController{
	 //获取当前系统的CPU 数目
	static int cpuNums = Runtime.getRuntime().availableProcessors();
	//ExecutorService通常根据系统资源情况灵活定义线程池大小
	private static ExecutorService pool = Executors.newFixedThreadPool(cpuNums*Constants.POOL_SIZE);
	private List<List<Map<String, String>>> resultList ;
	private List<List<Map<String, String>>> pingResultList ;
	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale loc = getLocale(request);
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
		System.out.println(cpuNums);
		BaseView view = TestView.buildIndexView(loc,viewFactory);
		CommandGroup cg = new CommandGroup("cg");
		UpdateCommand uc = new UpdateCommand("uc");
		uc.setContent(view);
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}
	
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response){
		Locale loc = getLocale(request);
		ViewFactory viewFactory = FrameworkHelper.getViewFactory(request);
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		
		String url = Utils.getParameter(request, "url");
		String testNum = Utils.getParameter(request, "num");
		String monitorPoint = Utils.getParameter(request,"monitorPoint");
		
		
		
		String type = "http";
//		String monitorPoint = "00000005,00000004";
		
		String schlId = "00001010";
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", url);
		Task task = null;
		if(Utils.notEmpty(schlId)){
			map.put("schlId", schlId);
			task = ServiceLocator.getTaskService().getTaskBySchlId(schlId);
		}
		if(task!=null){
			map.put("taskId", task.getTaskId());
		}
		String [] point = null;
		List<MonitoringPoint> list = new ArrayList<MonitoringPoint>();
		int num = 0;
		if(Utils.notEmpty(monitorPoint)){
			point = monitorPoint.split(",");
		}
		if(point!=null&&point.length>0){
			MonitoringPoint monitoringPoint = null;
			for(String temp:point){
				if(ServiceLocator.getMonitoringPointService()!=null){
					monitoringPoint = ServiceLocator.getMonitoringPointService().getMonitoringPoint(temp);
				}
				
				if(monitoringPoint!=null){
					list.add(monitoringPoint);
				}
			}
			num = point.length;
		}
		int number = 0;
		if(Utils.notEmpty(testNum)){
			number = Integer.parseInt(testNum);
		}
		for(int i=0;i<number;i++){
			DetectionUrlThread detectionUrlThread = new DetectionUrlThread(num,list,map);
			pool.execute(detectionUrlThread);
		}

		return null;
	}
	
	
	public ModelAndView detectionHttpUrl(HttpServletRequest request,
			HttpServletResponse response) {
	
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		outPutXML(response, TestView.buildDetectionHttpUrlView(viewFactory));
		return null;
	}

	public ModelAndView detectionUrl(HttpServletRequest request,
			HttpServletResponse response) {
		Gson gson = new Gson();
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String url = request.getParameter("url");
		String testNum = request.getParameter("num");
		String monitorPoint = Utils.getParameter(request,"monitorPoint");
		Pattern pat = Pattern
				.compile("^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$");
		Matcher mat = pat.matcher(url); // url is String.
		boolean isUrl = mat.find();
		CommandGroup cg = new CommandGroup(null);
		if(Utils.isEmpty(monitorPoint)) {
			cg.add(new AlertCommand("alt", "监测点不能为空!",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			cg.add(new LoadingCommand("loading", false));
			outPutXML(response, cg);
			return null;
		}
		if (!isUrl) {
			cg.add(new AlertCommand("alt", "很抱歉，您输入的格式不正确，请重新输入！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			cg.add(new LoadingCommand("loading", false));
			outPutXML(response, cg);
			return null;
		}
		
		Pattern pat2 = Pattern
		.compile("[0-9]");
		Matcher mat2 = pat2.matcher(testNum);
		if(Utils.notEmpty(testNum)&&!mat2.find()){
			cg.add(new AlertCommand("alt", "很抱歉，您输入的触发次数格式正确，请重新输入！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			cg.add(new LoadingCommand("loading", false));
			outPutXML(response, cg);
			return null;
		}

		String type = "http";
//		String monitorPoint = "00000005,00000004";
		
		String schlId = "00001010";
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", url);
		Task task = null;
		if(Utils.notEmpty(schlId)){
			map.put("schlId", schlId);
			task = ServiceLocator.getTaskService().getTaskBySchlId(schlId);
		}
		if(task!=null){
			map.put("taskId", task.getTaskId());
		}
		String [] point = null;
		List<MonitoringPoint> list = new ArrayList<MonitoringPoint>();
		int num = 0;
		if(Utils.notEmpty(monitorPoint)){
			point = monitorPoint.split(",");
		}
		if(point!=null&&point.length>0){
			MonitoringPoint monitoringPoint = null;
			for(String temp:point){
				if(ServiceLocator.getMonitoringPointService()!=null){
					monitoringPoint = ServiceLocator.getMonitoringPointService().getMonitoringPoint(temp);
				}
				
				if(monitoringPoint!=null){
					list.add(monitoringPoint);
				}
			}
			num = point.length;
		}
		int number = 0;
		if(Utils.notEmpty(testNum)){
			number = Integer.parseInt(testNum);
		}
		
		List<DetectionUrlThread> list2 = new ArrayList<DetectionUrlThread>();
		for(int i=0;i<number;i++){
			DetectionUrlThread detectionUrlThread = new DetectionUrlThread(num,list,map);
			list2.add(detectionUrlThread);
			pool.execute(detectionUrlThread);
		}
		boolean isOver = false;
		int trueCount = 0;
		resultList = new ArrayList<List<Map<String, String>>>();
		if(Utils.notEmpty(list2)){
			while(!isOver){
				trueCount = 0;
				resultList.clear();
				for(DetectionUrlThread detectionUrlThread:list2){
					if(detectionUrlThread.isOver()){
						trueCount++;
						resultList.add(detectionUrlThread.getListMap());
					}
				}
				if(trueCount==list2.size()){
					isOver = true;
				}
			}
		}
		cg.add((CommandGroup) TestView.updateDetectionHttpUrlView(
				viewFactory, resultList));
		cg.add(new LoadingCommand("loading", false));

		outPutXML(response, cg);
		return null;
	}
//
//	public ModelAndView showResponseHead(HttpServletRequest request,
//			HttpServletResponse response) {
//
//		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
//				.getSkin(request));
//		String index = request.getParameter("index");
//		int num = 0;
//		if (Utils.notEmpty(index)) {
//			num = Integer.parseInt(index);
//		}
//		Map<String, String> map = null;
//		if (Utils.notEmpty(detectionResultList)
//				&& detectionResultList.size() >= num + 1) {
//			map = detectionResultList.get(num);
//		}
//		outPutXML(response, JkbTestView.buildShowResponseHead(viewFactory, map));
//		return null;
//	}
//	
	public ModelAndView showResponseTime(HttpServletRequest request,
			HttpServletResponse response) {

		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		String index = request.getParameter("index");
		String row = Utils.getParameter(request, "row");
		int num = 0,num2=0;
		if (Utils.notEmpty(index)) {
			num = Integer.parseInt(index);
		}
		if (Utils.notEmpty(row)) {
			num2 = Integer.parseInt(row);
		}
		Map<String, String> map = null;
		
		if (Utils.notEmpty(resultList)
				&& resultList.size() >= num + 1) {
			map = resultList.get(num).get(num2);
		}
		outPutXML(response, TestView.buildShowResponseTime(viewFactory, map));
		return null;
	}

	public ModelAndView pingHttpUrl(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		outPutXML(response, TestView.buildPingHttpUrlView(viewFactory));
		return null;
	}

	public ModelAndView pingUrl(HttpServletRequest request,
			HttpServletResponse response) {
		Gson gson = new Gson();
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper
				.getSkin(request));
		CommandGroup cg = new CommandGroup(null);
		String url = request.getParameter("url");
		String testNum = request.getParameter("num");
		String monitorPoint = Utils.getParameter(request,"monitorPoint");
		if(Utils.isEmpty(monitorPoint)) {
			cg.add(new AlertCommand("alt", "监测点不能为空!",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			cg.add(new LoadingCommand("loading", false));
			outPutXML(response, cg);
			return null;
		}
		Pattern pat = Pattern
				.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
		Matcher mat = pat.matcher(url); // url is String.
		boolean isIP = mat.find();
		// if(!isIP){
		// cg.add(new AlertCommand("alt", "很抱歉，您输入的格式不正确，请重新输入！",
		// "img/p/alert-warn.gif", DialogPosition.middle, 5));
		// cg.add(new LoadingCommand("loading",false));
		// outPutXML(response,cg);
		// return null;
		// }
		Pattern pat2 = Pattern
				.compile("[www.]*?\\.(com|cn|net|org|biz|info|cc|tv)");
		Matcher mat2 = pat2.matcher(url); // url is String.
		boolean isUrl = mat2.find();
		if (isUrl) {
			isUrl = url.startsWith("http://") ? false : true;
		}
		if (!isUrl && !isIP) {
			cg.add(new AlertCommand("alt", "很抱歉，您输入的格式不正确，请重新输入！",
					"img/p/alert-warn.gif", DialogPosition.middle, 5));
			cg.add(new LoadingCommand("loading", false));
			outPutXML(response, cg);
			return null;
		}
		
		
		String type = "ping";
//		String monitorPoint = "00000005,00000004";
		
		String schlId = "00001010";
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", url);
		Task task = null;
		if(Utils.notEmpty(schlId)){
			map.put("schlId", schlId);
			task = ServiceLocator.getTaskService().getTaskBySchlId(schlId);
		}
		if(task!=null){
			map.put("taskId", task.getTaskId());
		}
		String [] point = null;
		List<MonitoringPoint> list = new ArrayList<MonitoringPoint>();
		int num = 0;
		if(Utils.notEmpty(monitorPoint)){
			point = monitorPoint.split(",");
		}
		if(point!=null&&point.length>0){
			MonitoringPoint monitoringPoint = null;
			for(String temp:point){
				if(ServiceLocator.getMonitoringPointService()!=null){
					monitoringPoint = ServiceLocator.getMonitoringPointService().getMonitoringPoint(temp);
				}
				
				if(monitoringPoint!=null){
					list.add(monitoringPoint);
				}
			}
			num = point.length;
		}
		int number = 0;
		if(Utils.notEmpty(testNum)){
			number = Integer.parseInt(testNum);
		}
		
		List<PingUrlThread> list2 = new ArrayList<PingUrlThread>();
		for(int i=0;i<number;i++){
			PingUrlThread pingUrlThread = new PingUrlThread(num,list,map);;
			list2.add(pingUrlThread);
			pool.execute(pingUrlThread);;
			
			
		}
		boolean isOver = false;
		int trueCount = 0;
		pingResultList = new ArrayList<List<Map<String, String>>>();
		if(Utils.notEmpty(list2)){
			while(!isOver){
				trueCount = 0;
				pingResultList.clear();
				for(PingUrlThread detectionUrlThread:list2){
					if(detectionUrlThread.isOver()){
						trueCount++;
						pingResultList.add(detectionUrlThread.getListMap());
					}
				}
				if(trueCount==list2.size()){
					isOver = true;
				}
			}
		}

		cg.add((CommandGroup) TestView.updatePingUrlView(viewFactory,
				pingResultList));
		cg.add(new LoadingCommand("loading", false));

		outPutXML(response, cg);
		return null;
	}
//
//	// 求数组最大值
//	public int getMaxArray(int[] intArray) {
//
//		int maxArray = 0;
//		// 数组中为正数的第一个位置
//		int index = -1;
//		for (int i = 0; i < intArray.length; i++) {
//			if (intArray[i] >= 0) {
//				maxArray = intArray[0];
//				index = i;
//				break;
//			}
//		}
//		if (index > -1) {
//			for (int i = index; i < intArray.length; i++) {
//				if (intArray[i] >= 0 && intArray[i] > maxArray) {
//					maxArray = intArray[i];
//				}
//			}
//		} else {
//			maxArray = -1;
//		}
//		return maxArray;
//	}
//
//	// 求数组最小值
//	public int getMinArray(int[] intArray) {
//		int minArray = 0;
//		// 数组中为正数的第一个位置
//		int index = -1;
//		for (int i = 0; i < intArray.length; i++) {
//			if (intArray[i] >= 0) {
//				minArray = intArray[i];
//				index = i;
//				break;
//			}
//		}
//		if (index > -1) {
//			for (int i = index; i < intArray.length; i++) {
//				if (intArray[i] >= 0 && intArray[i] < minArray) {
//					minArray = intArray[i];
//				}
//			}
//		} else {
//			minArray = -1;
//		}
//
//		return minArray;
//	}
//
//	// 求数组的平均值
//	public double getAvgArray(int[] intArray) {
//		int count = 0;
//		int sum = 0;
//		double avg = 0;
//		for (int i = 0; i < intArray.length; i++) {
//			if (intArray[i] >= 0) {
//				sum += intArray[i];
//				count++;
//			}
//		}
//		if (count > 0) {
//			avg = sum * 100 / count * 0.01;
//		}
//
//		return avg;
//	}
//
//	public String getIP(String ipAddress) {
//		String ipStr = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
//		Pattern pattern = Pattern.compile(ipStr);
//		Matcher matcher = pattern.matcher(ipAddress);
//		String ip = "";
//		if (matcher.find()) {
//			ip = matcher.group();
//		}
//		return ip;
//
//	}

	
	
	
}
