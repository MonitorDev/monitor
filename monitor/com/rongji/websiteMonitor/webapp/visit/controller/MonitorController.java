package com.rongji.websiteMonitor.webapp.visit.controller;

import static com.rongji.dfish.framework.FrameworkConstants.LOGIN_USER_KEY;
import static com.rongji.dfish.framework.FrameworkHelper.getLocale;
import static com.rongji.dfish.framework.FrameworkHelper.outPutXML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.rongji.dfish.base.Page;
import com.rongji.dfish.engines.xmltmpl.BaseView;
import com.rongji.dfish.engines.xmltmpl.DialogPosition;
import com.rongji.dfish.engines.xmltmpl.ViewFactory;
import com.rongji.dfish.engines.xmltmpl.command.AlertCommand;
import com.rongji.dfish.engines.xmltmpl.command.CommandGroup;
import com.rongji.dfish.engines.xmltmpl.command.JSCommand;
import com.rongji.dfish.engines.xmltmpl.command.UpdateCommand;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.DateUtils;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.MonitorDetialService;
import com.rongji.websiteMonitor.service.MonitoringTypeService;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.service.TaskService;
import com.rongji.websiteMonitor.webapp.BaseActionController;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;
import com.rongji.websiteMonitor.webapp.visit.view.MonitorDetailView;
import com.rongji.websiteMonitor.webapp.visit.view.MonitorView;

public class MonitorController extends BaseActionController {
	
	private TaskService taskService;
	private MonitoringTypeService typeService;
	private MonitorDetialService detialService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	Gson getDayAvaliable = new Gson();
	String taskId = "00000018";
	
	public MonitorDetialService getDetialService() {
		return detialService;
	}


	public void setDetialService(MonitorDetialService detialService) {
		this.detialService = detialService;
	}


	public MonitoringTypeService getTypeService() {
		return typeService;
	}


	public void setTypeService(MonitoringTypeService typeService) {
		this.typeService = typeService;
	}


	public TaskService getTaskService() {
		return taskService;
	}


	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}


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
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		
		String currentPage = request.getParameter("cp");
		Page page = FrameworkHelper.createPersonalPage(userId, currentPage);
	//	List<Task> dataList = this.taskService.getTasksByPage(page, true); 
		List<Monitortype> typeList = typeService.getMonitorTypesByPage(null, false);
//		BaseView view = TaskView.buildIndexView(loc,viewFactory, dataList, page,typeList);
		QueryCondition condition = new QueryCondition(request);
		condition.setTaskId(taskId);
		Date now = new Date();
		condition.setStartTime(DateUtils.getMonthBegin(now));
		condition.setEndTime(now);
		List<Object[]> dataList = detialService.getMonitorDetialByCondition(page,condition);
		List<Object[]> listMonitorStatus = detialService.getCurrentMonitorStatus(taskId);
		BaseView view = MonitorView.buildMain(loc,viewFactory,condition, getHourAvaliable(condition), listMonitorStatus);
		outPutXML(response, view);
		return null;
	}
	
	
	public ModelAndView showOverView(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		QueryCondition condition = new QueryCondition(request);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		condition.setTaskId(taskId);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		if(Utils.isEmpty(startTime)) {
			Date today = new Date();
			condition.setStartTime(today);
		}else if(startTime.indexOf("-") > 0){
			condition.setStartTime(sdf.parse(startTime));
		}else {
			condition.setStartTime(sdf2.parse(startTime));
		}
		if(Utils.isEmpty(endTime)) {
			Date today = new Date();
			condition.setEndTime(today);
		}else if(endTime.indexOf("-") > 0){
			condition.setEndTime(sdf.parse(endTime));
		}else {
			condition.setEndTime(sdf2.parse(endTime));
		}
		String dataJson = "";
		if((condition.getEndTime().getTime() - condition.getStartTime().getTime()) >= (1000*60*60*24)) {
			dataJson = getDayAvaliable(condition);
		}else {
			dataJson = getHourAvaliable(condition);
		}
		
		//0:mpId  1:isusable  2:count  3:hour
		List<Object[]> listMonitorStatus = detialService.getCurrentMonitorStatus(taskId);
		CommandGroup cg = new CommandGroup("");
		UpdateCommand uc = new UpdateCommand("uc");
		uc.setContent(MonitorView.buildOverViewPanel(loc, viewFactory, condition, dataJson,listMonitorStatus));
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}
	
	public ModelAndView showAvailable(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		QueryCondition condition = new QueryCondition(request);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		condition.setTaskId(taskId);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String isIgnoreMpId = request.getParameter("isIgnoreMpId");
		if(Utils.isEmpty(isIgnoreMpId)) {
			String mpId = request.getParameter("mpId");
			condition.setMpId(mpId);
		}else {
			condition.setMpId(null);
		}
		Task task = taskService.getTask(taskId);
		String[] mpIds = null;
		if(task!=null&&Utils.notEmpty(task.getMonitorPoint())){
			mpIds = task.getMonitorPoint().split(",");
		}
		if(Utils.isEmpty(startTime)) {
			Date today = new Date();
			condition.setStartTime(today);
		}else if(startTime.indexOf("-") > 0){
			condition.setStartTime(sdf.parse(startTime));
		}else {
			condition.setStartTime(sdf2.parse(startTime));
		}
		if(Utils.isEmpty(endTime)) {
			Date today = new Date();
			condition.setEndTime(today);
		}else if(endTime.indexOf("-") > 0){
			condition.setEndTime(sdf.parse(endTime));
		}else {
			condition.setEndTime(sdf2.parse(endTime));
		}
		String dataJson = "";
		if((condition.getEndTime().getTime() - condition.getStartTime().getTime()) >= (1000*60*60*24)) {
			dataJson = getDayAvaliable(condition);
		}else {
			dataJson = getHourAvaliable(condition);
		}
		
		//0:mpId  1:isusable  2:count  3:hour
		List<Object[]> listMonitorStatus = detialService.getCurrentMonitorStatus(taskId);
		List<Object[]> list2 = detialService.getMPAvailableRateByCondition(condition);
		Map<String, String> isuableMap = getIsusable(list2, condition);
		List<FaultHistory> list = ServiceLocator.getFaultHistoryService().getFaultHistoryByCondition(condition,null);
		CommandGroup cg = new CommandGroup("");
		UpdateCommand uc = new UpdateCommand("uc");
		uc.setContent(MonitorView.buildAvailablePanel(loc, viewFactory, condition, dataJson,mpIds, isuableMap, list));
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}
	
	public ModelAndView showResponse(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		QueryCondition condition = new QueryCondition(request);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		condition.setTaskId(taskId);
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String isIgnoreMpId = request.getParameter("isIgnoreMpId");
		if(Utils.isEmpty(isIgnoreMpId)) {
			String mpId = request.getParameter("mpId");
			condition.setMpId(mpId);
		}else {
			condition.setMpId(null);
		}
		Task task = taskService.getTask(taskId);
		String[] mpIds = null;
		if(task!=null&&Utils.notEmpty(task.getMonitorPoint())){
			mpIds = task.getMonitorPoint().split(",");
		}
		if(Utils.isEmpty(startTime)) {
			Date today = new Date();
			condition.setStartTime(today);
		}else if(startTime.indexOf("-") > 0){
			condition.setStartTime(sdf.parse(startTime));
		}else {
			condition.setStartTime(sdf2.parse(startTime));
		}
		if(Utils.isEmpty(endTime)) {
			Date today = new Date();
			condition.setEndTime(today);
		}else if(endTime.indexOf("-") > 0){
			condition.setEndTime(sdf.parse(endTime));
		}else {
			condition.setEndTime(sdf2.parse(endTime));
		}
		
		Map<String, Object> responseTimeMap = getResponseTime(condition);
		Map<String, Object> responsetDetailMap = getResponseDetailTime(condition);
		Object[] listResponseTime = detialService.getOnlyResponseTimeByCondition(condition);
		//0:mpId  1:isusable  2:count  3:hour
		List<Object[]> listMonitorStatus = detialService.getCurrentMonitorStatus(taskId);
		List<Object[]> list2 = detialService.getMPAvailableRateByCondition(condition);
		Map<String, String> isuableMap = getIsusable(list2, condition);
		List<FaultHistory> list = ServiceLocator.getFaultHistoryService().getFaultHistoryByCondition(condition,null);
		CommandGroup cg = new CommandGroup("");
		UpdateCommand uc = new UpdateCommand("uc");
		Gson gson = new Gson();
		uc.setContent(MonitorView.buildResponsePanel(loc, viewFactory,condition, mpIds, null, listResponseTime,gson.toJson(responseTimeMap), gson.toJson(responsetDetailMap)));
		cg.add(uc);
		outPutXML(response, cg);
		return null;
	}
	
	/**
	 * 忽略故障
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView ignoreFault(HttpServletRequest request, HttpServletResponse response){
		Locale loc = getLocale(request);
		ViewFactory viewFactory = ViewFactory.getViewFactory(FrameworkHelper.getSkin(request));
		String userId = (String) request.getSession().getAttribute(LOGIN_USER_KEY);
		
		QueryCondition condition = new QueryCondition(request);
		List<FaultHistory>  faultHistorys = ServiceLocator.getFaultHistoryService().getFaultHistoryByCondition(condition,null);
		FaultHistory faultHistory = null;
		if(Utils.notEmpty(faultHistorys)){
			faultHistory = faultHistorys.get(0);
		}
		if(faultHistory!=null){
		
			if(Utils.notEmpty(faultHistory.getIsIgnore())&&Constants.OPTION_IS_YES.equals(faultHistory.getIsIgnore())){
				faultHistory.setIsIgnore(null);
				
			}else{
				faultHistory.setIsIgnore(Constants.OPTION_IS_YES);
			}
			
			
			ServiceLocator.getFaultHistoryService().updateFaultHistory(faultHistory);
		}
		String source = Utils.notEmpty(condition.getMpId())?"source"+condition.getMpId():"source1";
		SimpleDateFormat sdform = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CommandGroup cg = new CommandGroup("cg");
		JSCommand command1 = new JSCommand("1","var a = VM( '/import_dlg' ).find( 'f_f_grid' ).getRowData( 0 );alert(a.beginTime);");
		JSCommand command = new JSCommand("js","VM( this ).find( 'f_f_grid' ).updateRow( { " 
				+"beginTime : '"+sdform.format(faultHistory.getFhBeginTime())+"', " 
				+"endTime : '"+sdform.format(faultHistory.getFhEndTime())+"', "
				+"duration : '"+MonitorDetailView.getDuration(faultHistory.getFhBeginTime(),faultHistory.getFhEndTime())+"',"
				+"reason : '"+faultHistory.getFhReason()+"',"
				+"op : '"+faultHistory.getFhId()+"',"
				+"isIgnore:'"+faultHistory.getIsIgnore()+"'"
				+"},{op:'"+faultHistory.getFhId()+"'});");
		cg.add(command);
		outPutXML(response, cg);
		return null;
	}
	
	/**
	 * 获取一天内的可用率
	 * @param condition
	 * @return
	 */
	public String getHourAvaliable(QueryCondition condition) {
		List<Object[]> list = ServiceLocator.getTaskResultService().getHourAvailableByCondition(condition);
		List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
		Map<String, String> labelMap = null;
		Map<String, Object> maps = new HashMap<String, Object>();
		Map<String, long[]> availableMap = new HashMap<String, long[]>();
		String isuseable,hour;long count;
		for(Object[] obj : list) {
			isuseable = (String)obj[0];
			count = (Long)obj[1];
			hour = (String)obj[2];
			long[] val = availableMap.get(hour);
			if(val == null) {
				val = new long[]{0L, 0L};
			}
			if("1".equals(isuseable)) {
				val[0] += count;
			}else {
				val[1] += count;
			}
			availableMap.put(hour, val);
		}
		//存放dataset值
		List<Map<String, Object>> datasetList = new ArrayList<Map<String,Object>>();
		long[] valCount;
		Map<String, String> tempMap = null;
		List<Map<String, String>> dataMap = new ArrayList<Map<String, String>>();
		for(int i=0;i<24;i++){
			labelMap = new HashMap<String, String>();
			tempMap = new HashMap<String, String>();
			if(i<10){
				labelMap.put("label", "0"+i);
				valCount = availableMap.get("0"+i);
				if(valCount != null) {
					labelMap.put("value", (valCount[0])*10000/(valCount[0]+valCount[1])/100 +"");
				}else {
					labelMap.put("value", "0");
				}
			}else{
				labelMap.put("label", i+"");
				valCount = availableMap.get(i+"");
				if(valCount != null) {
					labelMap.put("value", (valCount[0])*10000/(valCount[0]+valCount[1])/100 +"");
				}else {
					labelMap.put("value", "0");
				}
			}
			//dataMap.add(tempMap);
			dataList.add(labelMap);
		}
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("caption", "可用率变化曲线图");
		headMap.put("xAxisName", "日期");
		headMap.put("yAxisName", "可用率(%)");
		headMap.put("labelStep", "5");
		headMap.put("palette", "4");
		headMap.put("yAxisMaxValue", "100");
		headMap.put("slantLabels", "1");

//		headMap.put("numDivLines", "10");
		
		maps.put("chart", headMap);
		maps.put("data", dataList);
		return new Gson().toJson(maps);
	}

	/**
	 * 获取一定时间范围内的使用率
	 * @param condition
	 * @return
	 */
	public String getDayAvaliable(QueryCondition condition) {
		
		List<Object[]> list2 = detialService.getMPAvailableRateByCondition(condition);
		Map<String, Object> maps = new HashMap<String, Object>();
		if(Utils.notEmpty(list2)){
			List<Object[]> list = detialService.getAvailableRateByCondition(condition);
			
			List<Long> dateList = null;
			boolean isHas = false;
			List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
			if (Utils.notEmpty(list)) {
				dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf.format(condition
								.getStartTime()) : null,
						condition.getEndTime() != null ? sdf.format(condition
								.getEndTime()) : null,true);
				//塞值
				if(Utils.notEmpty(dateList)){
					Map<String, String> map = null;
					for(Long temp:dateList){
						map = new HashMap<String, String>();
						isHas = false;
						map.put("label",(temp%10000/100)+"-"+(temp%10000%100));
						for(Object[] objects:list){
							if(temp.longValue()==(objects[0]!=null?(Long.parseLong((String)objects[0])):0L)){
								map.put("value", (String)objects[1]);
								isHas = true;
								break;
							}
						}
						if(!isHas){
							map.put("value", "100");
						}
						dataList.add(map);
					}
				}
			}else{
				dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf.format(condition
								.getStartTime()) : null,
						condition.getEndTime() != null ? sdf.format(condition
								.getEndTime()) : null,true);
				//塞值
				if(Utils.notEmpty(dateList)){
					Map<String, String> map = null;
					for(Long temp:dateList){
						map = new HashMap<String, String>();
						isHas = false;
						map.put("label", (temp%10000/100)+"-"+(temp%10000%100));
						for(Object[] objects:list){
							if(temp.longValue()==(objects[0]!=null?(Long.parseLong((String)objects[0])):0L)){
								map.put("value", (String)objects[1]);
								isHas = true;
								break;
							}
						}
						if(!isHas){
							map.put("value", "100");
						}
						dataList.add(map);
					}
				}
			}
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("caption", "可用率变化曲线图");
			headMap.put("xAxisName", "日期");
			headMap.put("yAxisName", "可用率(%)");
			headMap.put("labelStep", "5");
			headMap.put("palette", "4");
			headMap.put("yAxisMaxValue", "100");
			headMap.put("slantLabels", "1");

//			headMap.put("numDivLines", "10");
			
			maps.put("chart", headMap);
			maps.put("data", dataList);
		}
		
		return new Gson().toJson(maps);
	}
	
	private Map<String, String> getIsusable(List<Object[]> list2, QueryCondition condition) {
		Gson gson = new Gson();
		Map<String, String[]> pointMap = new HashMap<String, String[]>();
		Map<String, String> pointResult = null;
		Map<String, String> pointHeadMap = null;
		String[] objtemp = null;
		if(Utils.notEmpty(list2)){
			
			for(Object[] objects:list2){
				if(pointMap.containsKey((String)objects[0])){
					objtemp = pointMap.get((String)objects[0]);
					if("0".equals((String)objects[2])){
						objtemp[2] = objects[3]+"";//可用总数
					}else{
						objtemp[3] = objects[3]+"";//不可用总数
					}
					pointMap.put((String)objects[0],objtemp);
				}else{
					objtemp = new String[4];
					objtemp[0] = (String)objects[0];//mpId
					objtemp[1] = (String)objects[1];//mpName
					if("0".equals((String)objects[2])){
						objtemp[2] = objects[3]+"";//可用总数
					}else{
						objtemp[3] = objects[3]+"";//不可用总数
					}
					pointMap.put((String)objects[0],objtemp);
				}
				
			}
		}
		if(pointMap!=null&&pointMap.size()>0){
			pointResult = new HashMap<String, String>();
			List<Map<String, String>> list3 = null;
			String[] strtemp = null;
			Map<String, String> map = null;
			Map<String, String> map2 = null;
			Map<String, Object> map3 = null;
			int count = 0,unuse=0,use=0,unuseAll=0,useAll=0;
			if(Utils.isEmpty(condition.getMpId())){
				for(Map.Entry<String,String[]> entry:pointMap.entrySet()){
					strtemp = entry.getValue();
					use = Utils.notEmpty(strtemp[2])?Integer.parseInt(strtemp[2]):0;
					unuse = Utils.notEmpty(strtemp[3])?Integer.parseInt(strtemp[3]):0;
					unuseAll = unuseAll +unuse;
					useAll = useAll + use;
					count = count+use+unuse;
					
				}
				map = new HashMap<String, String>();
				map2 = new HashMap<String, String>();
				list3 = new ArrayList<Map<String,String>>();
				pointHeadMap = new HashMap<String, String>();
				map.put("label", "故障");
				map.put("value",unuseAll*100.00/count+"");
				map2.put("label", "正常");
				map2.put("value", useAll*100.00/count+"");
				list3.add(map);
				list3.add(map2);
				pointHeadMap.put("caption", "平均");
				pointHeadMap.put("palette", "4");
//				pointHeadMap.put("manageLabelOverflow", "true");
				pointHeadMap.put("startingAngle", "60");
				pointHeadMap.put("smartLabelClearance", "1");
				
				map3 = new HashMap<String, Object>();
				map3.put("chart", pointHeadMap);
				map3.put("data", list3);
				pointResult.put("pp", gson.toJson(map3));
				
			}else{
				for(Map.Entry<String,String[]> entry:pointMap.entrySet()){
					strtemp = entry.getValue();
					use = Utils.notEmpty(strtemp[2])?Integer.parseInt(strtemp[2]):0;
					unuse = Utils.notEmpty(strtemp[3])?Integer.parseInt(strtemp[3]):0;
					count = use+unuse;
					map = new HashMap<String, String>();
					map2 = new HashMap<String, String>();
					list3 = new ArrayList<Map<String,String>>();
					pointHeadMap = new HashMap<String, String>();
					map.put("label", "故障");
					map.put("value",unuse*100.00/count+"");
					map2.put("label", "正常");
					map2.put("value", use*100.00/count+"");
					list3.add(map);
					list3.add(map2);
					pointHeadMap.put("caption", strtemp[1]);
					pointHeadMap.put("palette", "4");
//					pointHeadMap.put("manageLabelOverflow", "true");
					pointHeadMap.put("startingAngle", "60");
					pointHeadMap.put("smartLabelClearance", "1");
					
					map3 = new HashMap<String, Object>();
					map3.put("chart", pointHeadMap);
					map3.put("data", list3);
					pointResult.put(strtemp[0], gson.toJson(map3));
				}
			}
			
		}
		return pointResult;
	}

	/**
	 * 获取一定范围的响应时间
	 * @param condition
	 * @return
	 */
	private Map<String, Object> getResponseTime(QueryCondition condition) {
		List<Object[]> list = null;
		boolean isHour = false;
		if((condition.getEndTime().getTime() - condition.getStartTime().getTime()) >= (1000*60*60*24)) {
			list = detialService.getResponseTimeByCondition(condition,false);
		}else {
			list = detialService.getHourResponseTimeByCondition(condition);
			isHour = true;
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		if(Utils.notEmpty(list)){
			List<Long> dateList = null;
			boolean isHas = false;
			Map<String, Object> minMap = new HashMap<String, Object>();
			List<Map<String, String>> minList = new ArrayList<Map<String,String>>();
			minMap.put("seriesname", "最小响应时间");
			Map<String,Object> maxMap = new HashMap<String, Object>();
			List<Map<String, String>> maxList = new ArrayList<Map<String,String>>();
			maxMap.put("seriesname", "最大响应时间");
			Map<String, Object> avgMap = new HashMap<String, Object>();
			List<Map<String, String>> avgList = new ArrayList<Map<String,String>>();
			avgMap.put("seriesname", "平均响应时间");
			List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
			if(isHour) {
				Map<String, String> labelMap = null;
				for(int i=0;i<24;i++){
					labelMap = new HashMap<String, String>();
					if(i<10){
						labelMap.put("label", "0"+i);
					}else{
						labelMap.put("label", i+"");
					}
					dataList.add(labelMap);
				}
				Map<String, String> tempMap = null,tempMap2 = null,tempMap3 = null;
				for(Map<String, String> hourMap : dataList) {
					String hour = hourMap.get("label");
					isHas = false;
					for(Object[] objects:list){
						if(hour.equals(String.valueOf(objects[0]))) {
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap.put("value", objects[1]+"");
							minList.add(tempMap);
							tempMap2.put("value", objects[2]+"");
							maxList.add(tempMap2);
							tempMap3.put("value", objects[3]+"");
							avgList.add(tempMap3);
							isHas = true;
							break;
						}
					}
					if(!isHas){
						tempMap = new HashMap<String, String>();
						tempMap2 = new HashMap<String, String>();
						tempMap3 = new HashMap<String, String>();
						tempMap.put("value", "0");
						minList.add(tempMap);
						tempMap2.put("value", "0");
						maxList.add(tempMap2);
						tempMap3.put("value", "0");
						avgList.add(tempMap3);
						
					}
				}
				
			}else {
				dateList = DateUtils.getDateBetween2Date(
						condition.getStartTime() != null ? sdf.format(condition
								.getStartTime()) : null,
						condition.getEndTime() != null ? sdf.format(condition
								.getEndTime()) : null,false);
				//塞值
				if(Utils.notEmpty(dateList)){
				
					Map<String, String> map = null;
					for(Long temp:dateList){
						map = new HashMap<String, String>();
						isHas = false;
						map.put("label", (temp%10000/100)+"-"+(temp%10000%100));
						Map<String, String> tempMap = null,tempMap2 = null,tempMap3 = null;
						for(Object[] objects:list){
							if(temp.longValue()==(objects[0]!=null?((Long)objects[0]).longValue():0L)){
								tempMap = new HashMap<String, String>();
								tempMap2 = new HashMap<String, String>();
								tempMap3 = new HashMap<String, String>();
								tempMap.put("value", objects[1]+"");
								minList.add(tempMap);
								tempMap2.put("value", objects[2]+"");
								maxList.add(tempMap2);
								tempMap3.put("value", objects[3]+"");
								avgList.add(tempMap3);
								isHas = true;
								break;
							}
						}
						if(!isHas){
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap.put("value", "0");
							minList.add(tempMap);
							tempMap2.put("value", "0");
							maxList.add(tempMap2);
							tempMap3.put("value", "0");
							avgList.add(tempMap3);
							
						}
						dataList.add(map);
					}
				}
			}
			
//			if (Utils.notEmpty(list)) {
//				dateList = DateUtils.getDateBetween2Date(
//						condition.getStartTime() != null ? sdf.format(condition
//								.getStartTime()) : null,
//						condition.getEndTime() != null ? sdf.format(condition
//								.getEndTime()) : null,false);
//				//塞值
//				if(Utils.notEmpty(dateList)){
//				
//					Map<String, String> map = null;
//					for(Long temp:dateList){
//						map = new HashMap<String, String>();
//						isHas = false;
//						map.put("label", (temp%10000/100)+"-"+(temp%10000%100));
//						Map<String, String> tempMap = null,tempMap2 = null,tempMap3 = null;
//						for(Object[] objects:list){
//							if(temp.longValue()==(objects[0]!=null?((Long)objects[0]).longValue():0L)){
//								tempMap = new HashMap<String, String>();
//								tempMap2 = new HashMap<String, String>();
//								tempMap3 = new HashMap<String, String>();
//								tempMap.put("value", objects[1]+"");
//								minList.add(tempMap);
//								tempMap2.put("value", objects[2]+"");
//								maxList.add(tempMap2);
//								tempMap3.put("value", objects[3]+"");
//								avgList.add(tempMap3);
//								isHas = true;
//								break;
//							}
//						}
//						if(!isHas){
//							tempMap = new HashMap<String, String>();
//							tempMap2 = new HashMap<String, String>();
//							tempMap3 = new HashMap<String, String>();
//							tempMap.put("value", "0");
//							minList.add(tempMap);
//							tempMap2.put("value", "0");
//							maxList.add(tempMap2);
//							tempMap3.put("value", "0");
//							avgList.add(tempMap3);
//							
//						}
//						dataList.add(map);
//					}
//				}
//			}else{
//				dateList = DateUtils.getDateBetween2Date(
//						condition.getStartTime() != null ? sdf.format(condition
//								.getStartTime()) : null,
//						condition.getEndTime() != null ? sdf.format(condition
//								.getEndTime()) : null,false);
//				//塞值
//				if(Utils.notEmpty(dateList)){
//					Map<String, String> map = null;
//					for(Long temp:dateList){
//						map = new HashMap<String, String>();
//						isHas = false;
//						map.put("label",(temp%10000/100)+"-"+(temp%10000%100));
//						Map<String, String> tempMap = null,tempMap2 = null,tempMap3 = null;
//						for(Object[] objects:list){
//							if(temp.longValue()==(objects[0]!=null?((Long)objects[0]).longValue():0L)){
//								
//								tempMap = new HashMap<String, String>();
//								tempMap2 = new HashMap<String, String>();
//								tempMap3 = new HashMap<String, String>();
//								tempMap.put("value", objects[1]+"");
//								minList.add(tempMap);
//								tempMap2.put("value", objects[2]+"");
//								maxList.add(tempMap2);
//								tempMap3.put("value", objects[3]+"");
//								avgList.add(tempMap3);
//								isHas = true;
//								break;
//							}
//						}
//						if(!isHas){
//							tempMap = new HashMap<String, String>();
//							tempMap2 = new HashMap<String, String>();
//							tempMap3 = new HashMap<String, String>();
//							tempMap.put("value", "0");
//							minList.add(tempMap);
//							tempMap2.put("value", "0");
//							maxList.add(tempMap2);
//							tempMap3.put("value", "0");
//							avgList.add(tempMap3);
//						}
//						dataList.add(map);
//					}
//				}
//			}
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("caption", "响应时间变化曲线图");
			headMap.put("xAxisName", "日期");
			headMap.put("yAxisName", "时间(ms)");
			headMap.put("labelStep", "5");
			headMap.put("formatNumberScale", "0");
			headMap.put("formatNumber", "0");
			Map<String, Object> trendlinesMap = new HashMap<String, Object>();
			Map<String, String> lineMap = new HashMap<String, String>();
			lineMap.put("color","91C728");
			lineMap.put("showontop","1");
 			trendlinesMap.put("line", lineMap);
 			
 			Map<String, Object> stylesMap = new HashMap<String, Object>();
 			Map<String, Object> definitionMap = new HashMap<String, Object>();
 			Map<String, Object> applicationMap = new HashMap<String, Object>();
 			Map<String, String> styleMap = new HashMap<String, String>();
 			Map<String, String> applyMap = new HashMap<String, String>();
 			styleMap.put("name","CanvasAnim");
 			styleMap.put("type","animation");
 			styleMap.put("param","_xScale");
 			styleMap.put( "start","0");
 			styleMap.put("duration","1");
 			definitionMap.put("definition", styleMap);
 			applyMap.put("toobject","Canvas");
 			applyMap.put("styles","CanvasAnim");
 			applicationMap.put("apply", applyMap);
 			stylesMap.put("application", applicationMap);
			
 			//存放dataset值
 			List<Map<String, Object>> datasetList = new ArrayList<Map<String,Object>>();
 			minMap.put("data", minList);
 			maxMap.put("data", maxList);
 			avgMap.put("data", avgList);
 			datasetList.add(minMap);
 			datasetList.add(avgMap);
 			datasetList.add(maxMap);
 			
			maps.put("chart", headMap);
			Map<String, Object> categoryMap =new HashMap<String, Object>();
			categoryMap.put("category", dataList);
			maps.put("categories",categoryMap );
			maps.put("trendlines", trendlinesMap);
			maps.put("styles", stylesMap);
			maps.put("dataset", datasetList);
		}
		return maps;
	}

	/**
	 * 获取一定范围的响应详细（链接时间，下载时间，访问时间）
	 * @param condition
	 * @return
	 */
	private Map<String, Object> getResponseDetailTime(QueryCondition condition) {
		//获取响应的各个连接时间（域名解析时间，下载时间等）
		List<Object[]> respDetial = null;
		boolean isHour = false;
		if((condition.getEndTime().getTime() - condition.getStartTime().getTime()) >= (1000*60*60*24)) {
			respDetial = detialService.getResponseDetialTimeByCondition(condition,false);
		}else {
			respDetial = detialService.getHourResponseDetailByCondition(condition);
			isHour = true;
		}
		Map<String, Object> respDetialMap = new HashMap<String, Object>();
		if(Utils.notEmpty(respDetial)){
			List<Long> dateList = null;
			boolean isHas = false;
			Map<String, Object> ext1Map = new HashMap<String, Object>();
			List<Map<String, String>> ext1List = new ArrayList<Map<String,String>>();
			ext1Map.put("seriesname", "DNS域名解析");
			Map<String,Object> ext2Map = new HashMap<String, Object>();
			List<Map<String, String>> ext2List = new ArrayList<Map<String,String>>();
			ext2Map.put("seriesname", "建立连接");
			Map<String, Object> ext3Map = new HashMap<String, Object>();
			List<Map<String, String>> ext3List = new ArrayList<Map<String,String>>();
			ext3Map.put("seriesname", "服务器计算");
			Map<String, Object> ext4Map = new HashMap<String, Object>();
			List<Map<String, String>> ext4List = new ArrayList<Map<String,String>>();
			ext4Map.put("seriesname", "下载内容");
			List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
			if (Utils.notEmpty(respDetial)) {
				if(isHour) {
					Map<String, String> labelMap = null;
					for(int i=0;i<24;i++){
						labelMap = new HashMap<String, String>();
						if(i<10){
							labelMap.put("label", "0"+i);
						}else{
							labelMap.put("label", i+"");
						}
						dataList.add(labelMap);
					}
					Map<String, String> tempMap = null,tempMap2 = null,tempMap3 = null,tempMap4 = null;
					for(Map<String, String> hourMap : dataList) {
						String hour = hourMap.get("label");
						isHas = false;
						for(Object[] objects:respDetial){
							if(hour.equals(String.valueOf(objects[0]))) {
								tempMap = new HashMap<String, String>();
								tempMap2 = new HashMap<String, String>();
								tempMap3 = new HashMap<String, String>();
								tempMap4 = new HashMap<String, String>();
								tempMap.put("value", objects[1]+"");
								ext1List.add(tempMap);
								tempMap2.put("value", objects[2]+"");
								ext2List.add(tempMap2);
								tempMap3.put("value", objects[3]+"");
								ext3List.add(tempMap3);
								tempMap4.put("value", objects[4]+"");
								ext4List.add(tempMap4);
								isHas = true;
								break;
							}
						}
						if(!isHas){
							tempMap = new HashMap<String, String>();
							tempMap2 = new HashMap<String, String>();
							tempMap3 = new HashMap<String, String>();
							tempMap4 = new HashMap<String, String>();
							tempMap.put("value", "0");
							ext1List.add(tempMap);
							tempMap2.put("value", "0");
							ext2List.add(tempMap2);
							tempMap3.put("value", "0");
							ext3List.add(tempMap3);
							tempMap4.put("value", "0");
							ext4List.add(tempMap4);
						}
					}
					
				}else {
					dateList = DateUtils.getDateBetween2Date(
							condition.getStartTime() != null ? sdf.format(condition
									.getStartTime()) : null,
							condition.getEndTime() != null ? sdf.format(condition
									.getEndTime()) : null,false);
					//塞值
					if(Utils.notEmpty(dateList)){
					
						Map<String, String> map = null;
						for(Long temp:dateList){
							map = new HashMap<String, String>();
							isHas = false;
							map.put("label", (temp%10000/100)+"-"+(temp%10000%100));
							Map<String, String> tempMap = null,tempMap2 = null,tempMap3 = null,tempMap4 = null;
							for(Object[] objects:respDetial){
								if(temp.longValue()==(objects[0]!=null?((Long)objects[0]).longValue():0L)){
									tempMap = new HashMap<String, String>();
									tempMap2 = new HashMap<String, String>();
									tempMap3 = new HashMap<String, String>();
									tempMap4 = new HashMap<String, String>();
									tempMap.put("value", objects[1]+"");
									ext1List.add(tempMap);
									tempMap2.put("value", objects[2]+"");
									ext2List.add(tempMap2);
									tempMap3.put("value", objects[3]+"");
									ext3List.add(tempMap3);
									tempMap4.put("value", objects[4]+"");
									ext4List.add(tempMap4);
									isHas = true;
									break;
								}
							}
							if(!isHas){
								tempMap = new HashMap<String, String>();
								tempMap2 = new HashMap<String, String>();
								tempMap3 = new HashMap<String, String>();
								tempMap4 = new HashMap<String, String>();
								tempMap.put("value", "0");
								ext1List.add(tempMap);
								tempMap2.put("value", "0");
								ext2List.add(tempMap2);
								tempMap3.put("value", "0");
								ext3List.add(tempMap3);
								tempMap4.put("value", "0");
								ext4List.add(tempMap4);
							}
							dataList.add(map);
						}
					}
				}
			}
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("caption", "响应时间变化详细曲线图");
			headMap.put("xAxisName", "日期");
			headMap.put("yAxisName", "时间(ms)");
			headMap.put("labelStep", "5");
			headMap.put("formatNumberScale", "0");
			headMap.put("formatNumber", "0");
			Map<String, Object> trendlinesMap = new HashMap<String, Object>();
			Map<String, String> lineMap = new HashMap<String, String>();
			lineMap.put("color","91C728");
			lineMap.put("showontop","1");
 			trendlinesMap.put("line", lineMap);
 			
 			Map<String, Object> stylesMap = new HashMap<String, Object>();
 			Map<String, Object> definitionMap = new HashMap<String, Object>();
 			Map<String, Object> applicationMap = new HashMap<String, Object>();
 			Map<String, String> styleMap = new HashMap<String, String>();
 			Map<String, String> applyMap = new HashMap<String, String>();
 			styleMap.put("name","CanvasAnim");
 			styleMap.put("type","animation");
 			styleMap.put("param","_xScale");
 			styleMap.put( "start","0");
 			styleMap.put("duration","1");
 			definitionMap.put("definition", styleMap);
 			applyMap.put("toobject","Canvas");
 			applyMap.put("styles","CanvasAnim");
 			applicationMap.put("apply", applyMap);
 			stylesMap.put("application", applicationMap);
			
 			//存放dataset值
 			List<Map<String, Object>> datasetList = new ArrayList<Map<String,Object>>();
 			ext1Map.put("data", ext1List);
 			ext2Map.put("data", ext2List);
 			ext3Map.put("data", ext3List);
 			ext4Map.put("data", ext4List);
 			datasetList.add(ext1Map);
 			datasetList.add(ext2Map);
 			datasetList.add(ext3Map);
 			datasetList.add(ext4Map);
 			
 			respDetialMap.put("chart", headMap);
			Map<String, Object> categoryMap =new HashMap<String, Object>();
			categoryMap.put("category", dataList);
			respDetialMap.put("categories",categoryMap );
			respDetialMap.put("trendlines", trendlinesMap);
			respDetialMap.put("styles", stylesMap);
			respDetialMap.put("dataset", datasetList);
		}
		return respDetialMap;
	}


}
