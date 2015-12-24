package com.rongji.websiteMonitor.webapp.task.helper.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.ConfigConstants;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.quartz.SchedulerMethods;
import com.rongji.websiteMonitor.common.trigger.MonitorTrigger;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.AlarmInformHistory;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ServiceLocator;

/**
 * 网站监控线程类
 * 
 */
public class DetectionUrlThread implements Runnable {
	private String reult;
	//监测点
	private List<MonitoringPoint> list;
	//检测的参数
	private Map<String,String> map;
	public DetectionUrlThread(){

	}
	public DetectionUrlThread(Map<String, String>map) {
		this.map = map;
	}

	@Override
	public void run() {
		List<DetectionUrl> detectionUrlList = new ArrayList<DetectionUrl>();
		PubCommonDAO dao = FrameworkHelper.getDAO();
		List<Subproject> listSubproject = dao.getQueryList(" FROM Subproject t where t.id=?", map.get("id"));
		if(Utils.isEmpty(listSubproject)) {
			return;
		}
		Subproject subproject = listSubproject.get(0);
		String [] point = null;
		List<MonitoringPoint> list = new ArrayList<MonitoringPoint>();
		int num = 0;
		if(Utils.notEmpty(subproject.getMonitorPoint())){
			point = subproject.getMonitorPoint().split(",");
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
		//将要进行监控的网址、监控点统一塞到一个list中
		if(num>0){
			for(MonitoringPoint p:list){
				DetectionUrl detectionUrl = new DetectionUrl(p.getMpPort(),subproject.getUrl(),p.getMpIp(),p.getMpName(),p.getMpId());
				detectionUrlList.add(detectionUrl);
			}
		}

		List<Thread> threadList = new ArrayList<Thread>();
		// 加到线程数组中
		if (Utils.notEmpty(detectionUrlList)) {
			for (DetectionUrl detection : detectionUrlList) {
				Thread thread = new Thread(detection);
				detection.setBeginTime(System.currentTimeMillis());
				thread.start();
				threadList.add(thread);
			}
		}
		boolean isOver = false;
		Thread thread = null;
		//存放返回的结果
		Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
		Map<String, String> map1 = null;
		int trueCount = 0;
		//是否已结束
		while (!isOver) {
			if (Utils.notEmpty(threadList)) {
				for (int i = 0; i < threadList.size(); i++) {
					thread = threadList.get(i);
					if (!dataMap.containsKey(i + "")&&!thread.isAlive()) {
//						if (!dataMap.containsKey(i + "")) {
							map1 = MonitorTrigger.gson.fromJson(detectionUrlList.get(i)
									.getStr(),
									new TypeToken<Map<String, String>>() {
									}.getType());
							if(map1!=null){
								map1.put("mpId",detectionUrlList.get(i).mpId);
							}
							
							dataMap.put(i + "", map1);
							trueCount++;
//						}

					}else{
						if(dataMap.containsKey(i + "")){
							continue;
						}
						long time = System.currentTimeMillis();
						if(detectionUrlList.get(i).getBeginTime()!=0L&&(time - detectionUrlList.get(i).getBeginTime()>8000)){
							detectionUrlList.get(i).setInterrupted(true);
//							thread.interrupt();
							String str = "{\"address\":\""+detectionUrlList.get(i).serverName+"\",\"ip\":\"无响应\"," +
									"\"result\":\"无响应\",\"status\":\"连接超时\",\"responseTime\":\"无响应\",\"size\":\"无响应\"}";
							detectionUrlList.get(i).setStr(str);
						}
//						
						
					}
				}
			}
			if (trueCount == threadList.size()) {
				isOver = true;
			}
		}
//		reult = dataMap.toString();
		
		Date time = new Date();
		if(dataMap == null || dataMap.size() <=0 ) {
			//将得到的数据保存到数据库中
			//ServiceLocator.getTaskResultService().saveResultByMap(null,Constants.HTTP_TYPE,subproject.getId(),time);
		}
		//对结果进行处理
		if(dataMap!=null&&dataMap.size()>0){
			Map<String, String> tempMap = null;
			String results = "";
			FaultHistory faultHistory = null;
			int count = 0;
			double responseTimeThreshold = 0L;
			Map<String, String> map = Utils.string2Map(subproject.getConfigXml());
			if(map != null) {
				String thId = map.get("continueGroup");
				if(Utils.notEmpty(thId)) {
					Threshold th = ServiceLocator.getThresholdService().getThresholdById(thId);
					responseTimeThreshold = Double.parseDouble(Utils.string2Map(th.getContent()).get("responseTime"));
				}
			}
			for(Map.Entry<String, Map<String, String>> entry:dataMap.entrySet()){
				tempMap = entry.getValue();
				if(tempMap!=null){
					results = tempMap.get("result");
					faultHistory = ServiceLocator.getFaultHistoryService().getFaultHistoryByParam(subproject.getId(),null,tempMap.get("mpId"),false);
					boolean isOverTime = false;
					if(responseTimeThreshold > 0) {
						String totalTime = tempMap.get("totalTime");
						if(Utils.notEmpty(totalTime)) {
							if(responseTimeThreshold < Double.parseDouble(totalTime)) {
								isOverTime = true;
							}
						}
					}
					if(Utils.notEmpty(results)&&"可用".equals(results) && !isOverTime ){
						//可用。
						if(faultHistory!=null){
							//若是失败历史存在则进行修改失败历史和定时服务
							faultHistory.setFhEndTime(time);
							ServiceLocator.getFaultHistoryService().updateFaultHistory(faultHistory);
						}
					}else{
						
						//计算不可用的个数，如果不可用的个数与返回的结果数一致，则被视为该url是不可访问。
						count++;	
						
						if(faultHistory==null){
							faultHistory = new FaultHistory();
							faultHistory.setTaskId(subproject.getId());
							faultHistory.setFhBeginTime(time);
							faultHistory.setFhReason(tempMap.get("status").indexOf("200")>=0?"响应时间超时":tempMap.get("status"));
							faultHistory.setMpId(tempMap.get("mpId"));
							ServiceLocator.getFaultHistoryService().saveFaultHistory(faultHistory);
						}
					}
						
					//将得到的数据保存到数据库中
					ServiceLocator.getTaskResultService().saveResultByMap(tempMap,Constants.HTTP_TYPE,subproject.getId(),time);
				}
			}
			//看看是否有故障历史存在。
			faultHistory = ServiceLocator.getFaultHistoryService().getFaultHistoryByTaskId(subproject.getId(),false);
			
//			Subproject schl =(Subproject) dao.getQueryList("FROM Subproject t WHERE t.id=?",subproject.getId()).get(0);
			
			if(count==dataMap.size()){
				if(detectionInternet(list)) {
					//监控访问出错，不可用
					if(faultHistory==null){
						faultHistory = new FaultHistory();
						faultHistory.setTaskId(subproject.getId());
						faultHistory.setFhBeginTime(time);
						faultHistory.setFhReason(tempMap.get("status").indexOf("200")>=0?"响应时间超时":tempMap.get("status"));
						ServiceLocator.getFaultHistoryService().saveFaultHistory(faultHistory);
					}
					//修改定时任务，改为每分钟嗅探一次
					if(subproject!=null){
						Map<String, String> configMap = Utils.string2Map(subproject.getConfigXml());
						int retry =  subproject.getRetry();
						int hasRetry =  faultHistory.getRetry();
						Date now = new Date();
						if(retry<=hasRetry){
							if(hasRetry > retry) {
								Date date = faultHistory.getAlarmTime();
								int sequenceWarning = subproject.getWarningFrequency();
								if(sequenceWarning <= 0) {
									return ;
								}
								if(date != null) {
									long wt = date.getTime();
									if((now.getTime() - wt)<sequenceWarning*1000*60) {
										return;
									}
								}
							}
							hasRetry++;
							faultHistory.setRetry(hasRetry);
							faultHistory.setAlarmTime(now);
//							subproject.setHasRetry(hasRetry);
//							configMap.put("warningTime", String.valueOf(new Date().getTime()));
//							subproject.setConfigXml(Utils.mapToString(configMap));
							dao.updateObject(faultHistory);
							//将保存的定时任务激活
							SchedulerMethods.loadJobs();
							//进行告警
							String [] email = null,tel = null;
							String title = "故障通知";
							StringBuilder content = new StringBuilder();
							if(Utils.notEmpty(subproject.getNotification())) {
								email = getNotifycation(subproject.getNotification(),Constants.EMAIL_TYPE);
								tel = getNotifycation(subproject.getNotification(),Constants.PHONE_TYPE);
								content.append("项目‘").append(subproject.getName()).append("’出现故障,发现连接不通或超时");
							}
							
							if(email!=null&&email.length>0){
								for(String eml:email){
									ServiceLocator.getSenderMailService().sendMail(ConfigConstants.getInstance().getEmail(), eml, title, content.toString());
									//记录告警通知
									AlarmInformHistory alarmInformHistory = new AlarmInformHistory(); 
									alarmInformHistory.setAiTime(new Date());
									alarmInformHistory.setAiType(Constants.EMAIL_TYPE);
									alarmInformHistory.setAiWay(eml);
									alarmInformHistory.setTaskId(subproject.getId());
									alarmInformHistory.setAiContent(content.toString());
									ServiceLocator.getAlarmInformHistoryService().saveAlarmInformHistory(alarmInformHistory);
								}
							}
							if(tel!=null&&tel.length>0){
								ServiceLocator.getSendNoteMesaageService().sendMessageForMany(tel, content.toString());
								for(String tl :tel){
									//记录告警通知
									AlarmInformHistory alarmInformHistory = new AlarmInformHistory(); 
									alarmInformHistory.setAiTime(new Date());
									alarmInformHistory.setAiType(Constants.PHONE_TYPE);
									alarmInformHistory.setAiWay(tl);
									alarmInformHistory.setTaskId(subproject.getId());
									alarmInformHistory.setAiContent(content.toString());
									ServiceLocator.getAlarmInformHistoryService().saveAlarmInformHistory(alarmInformHistory);
								}
							}
						}else{
							hasRetry++;
							int frequency = subproject.getFrequency();
							subproject.setCronExpression("0 */"+Math.round(frequency/2)+" * * * ?");
//							subproject.setFrequency(Math.round(frequency/2));
//							subproject.setHasRetry(hasRetry);
							subproject.setConfigXml(Utils.mapToString(configMap));
							faultHistory.setRetry(hasRetry);
							dao.updateObject(faultHistory);
							dao.updateObject(subproject);
							//将保存的定时任务激活
							SchedulerMethods.loadJobs();
						}
						
					}
				}
			}else{
				//可用。
				if(faultHistory!=null){
					//若是失败历史存在则进行修改失败历史和定时服务
					faultHistory.setFhEndTime(time);
					ServiceLocator.getFaultHistoryService().updateFaultHistory(faultHistory);
					
					//将定时服务改为原来的样子
					if(subproject!=null){
						Map<String, String> configMap = Utils.string2Map(subproject.getConfigXml());
						int retry =  subproject.getRetry();
						int hasRetry =  faultHistory.getRetry();
						if(retry<hasRetry){
							String [] email = null,tel = null;
							String title = "故障恢复通知";
							StringBuilder content = new StringBuilder();
							if(Utils.notEmpty(subproject.getNotification())){
								email = getNotifycation(subproject.getNotification(),Constants.EMAIL_TYPE);
								tel = getNotifycation(subproject.getNotification(),Constants.PHONE_TYPE);
								content.append("监控服务‘").append(subproject.getName()).append("’的故障已经恢复！");
							}
							if(email!=null&&email.length>0){
								for(String eml:email){
									ServiceLocator.getSenderMailService().sendMail(ConfigConstants.getInstance().getEmail(), eml, title, content.toString());
									//记录告警通知
									AlarmInformHistory alarmInformHistory = new AlarmInformHistory(); 
									alarmInformHistory.setAiTime(new Date());
									alarmInformHistory.setAiType(Constants.EMAIL_TYPE);
									alarmInformHistory.setAiWay(eml);
									alarmInformHistory.setTaskId(subproject.getId());
									alarmInformHistory.setAiContent(content.toString());
									ServiceLocator.getAlarmInformHistoryService().saveAlarmInformHistory(alarmInformHistory);
								}
							}
							if(tel!=null&&tel.length>0){
								ServiceLocator.getSendNoteMesaageService().sendMessageForMany(tel, content.toString());
								for(String tl :tel){
									//记录告警通知
									AlarmInformHistory alarmInformHistory = new AlarmInformHistory(); 
									alarmInformHistory.setAiTime(new Date());
									alarmInformHistory.setAiType(Constants.PHONE_TYPE);
									alarmInformHistory.setAiWay(tl);
									alarmInformHistory.setTaskId(subproject.getId());
									alarmInformHistory.setAiContent(content.toString());
									ServiceLocator.getAlarmInformHistoryService().saveAlarmInformHistory(alarmInformHistory);
								}
							}
						}
						subproject.setHasRetry(0);
						configMap.remove("warningTime");
						subproject.setConfigXml(Utils.mapToString(configMap));
						subproject.setCronExpression("0 */"+subproject.getFrequency()+" * * * ?");
						dao.updateObject(subproject);
						//将保存的定时任务激活
						SchedulerMethods.loadJobs();
					}
				}
				
			}
		}
	}
	
	/**
	 * 通过各个检测点去链接百度，检测公司网络是否正常
	 * @param listMonitoringPoint  监测点列表
	 * @return
	 */
	public boolean detectionInternet(List<MonitoringPoint> listMonitoringPoint) {
		List<DetectionUrl> detectionUrlList = new ArrayList<DetectionUrl>();
		//将要进行监控的网址、监控点统一塞到一个list中
		if(Utils.notEmpty(listMonitoringPoint)){
			for(MonitoringPoint p:listMonitoringPoint){
				System.out.println(p.getMpIp() + "," + p.getMpPort());
				DetectionUrl detectionUrl = new DetectionUrl(p.getMpPort(),"www.baidu.com",p.getMpIp(),p.getMpName(),p.getMpId());
				detectionUrlList.add(detectionUrl);
			}
		}

		List<Thread> threadList = new ArrayList<Thread>();
		// 加到线程数组中
		if (Utils.notEmpty(detectionUrlList)) {
			for (DetectionUrl detection : detectionUrlList) {
				Thread thread = new Thread(detection);
				detection.setBeginTime(System.currentTimeMillis());
				thread.start();
				threadList.add(thread);
			}
		}
		boolean isOver = false;
		Thread thread = null;
		Map<String, String> map1 = null;
		int trueCount = 0;
		//存放返回的结果
		List<String> dataList = new ArrayList<String>();
		while(!isOver) {
			if (Utils.notEmpty(threadList)) {
				for (int i = 0; i < threadList.size(); i++) {
					thread = threadList.get(i);
					if (!dataList.contains(i + "")&&!thread.isAlive()) {
						map1 = MonitorTrigger.gson.fromJson(detectionUrlList.get(i)
								.getStr(),
								new TypeToken<Map<String, String>>() {
								}.getType());
						if(map1!=null){
							String results = map1.get("result");
							if(Utils.notEmpty(results) && "可用".equals(results)) {
								isOver = true;
								return true;
							}
						}
						dataList.add(i + "");
						trueCount++;
					}else{
						if(dataList.contains(i + "")){
							continue;
						}
						long time = System.currentTimeMillis();
						if(detectionUrlList.get(i).getBeginTime()!=0L&&(time - detectionUrlList.get(i).getBeginTime()>8000)){
							detectionUrlList.get(i).setInterrupted(true);
						}
					}
				}
			}
			if (trueCount == threadList.size()) {
				isOver = true;
			}
		}
		System.out.println("22222222222222222222222222222222222");
		return false;
	}
	
	public String[] getNotifycation(String notifycationType, String emailType) {
		Gson gson = new Gson();
		String [] temp = null;
		List<Map<String, String>> notifyMap = gson.fromJson(notifycationType, new TypeToken<List<Map<String, String>>>(){}.getType());
		if(Utils.notEmpty(emailType)&&Utils.notEmpty(notifyMap)){
			for(Map<String, String> temMap:notifyMap){
				if(emailType.equals(temMap.get("type"))){
					temp = Utils.notEmpty(temMap.get("content"))?temMap.get("content").split(","):null;
					return temp;
				}
			}
		}
		return null;
	}
	public List<MonitoringPoint> getList() {
		return list;
	}
	public void setList(List<MonitoringPoint> list) {
		this.list = list;
	}
	public String getReult() {
		return reult;
	}
	public void setReult(String reult) {
		this.reult = reult;
	}
	public static void main(String[] args) {
//		{"result":"无响应","status":"无响应","size":"无响应"}
//		String i = "sdfsd";
//		String str = "{\"address\":\""+i+"\"}";
//		System.out.println(str);
		DetectionUrlThread detectionUrlThread = new DetectionUrlThread();
		String str = "[{\"content\":\"kbblnt3440@qq.com,381527417@qq.com\",\"type\":\"eMail\"},{\"content\":\"15060137970,15080458317\",\"type\":\"phone\"}]";
		String [] email = detectionUrlThread.getNotifycation(str,Constants.EMAIL_TYPE);
		String [] tel = detectionUrlThread.getNotifycation(str,Constants.PHONE_TYPE);
		System.out.println();
	}
}
