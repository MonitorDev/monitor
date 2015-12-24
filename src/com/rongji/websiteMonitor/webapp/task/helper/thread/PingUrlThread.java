package com.rongji.websiteMonitor.webapp.task.helper.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ServiceLocator;

public class PingUrlThread implements Runnable {
	//检测的参数
	private Map<String,String> map;
	

	public PingUrlThread(Map<String, String> map) {
		super();
		this.map = map;
	}

	@Override
	public void run() {
		
		List<PingUrl> detectionUrlList = new ArrayList<PingUrl>();
		PubCommonDAO dao = FrameworkHelper.getDAO();
		List<Subproject> listSubproject = dao.getQueryList(" FROM Subproject t where t.id=?", map.get("id"));
		if(Utils.isEmpty(listSubproject)) {
			return;
		}
		Subproject subproject = listSubproject.get(0);
		String [] point = null;
		List<MonitoringPoint> listMonitoringPoint = new ArrayList<MonitoringPoint>();
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
					listMonitoringPoint.add(monitoringPoint);
				}
			}
			num = point.length;
		}
		//将要进行监控的网址、监控点统一塞到一个list中
		if(num>0){
			for(MonitoringPoint p:listMonitoringPoint){
				PingUrl detectionUrl = new PingUrl(p.getMpPort(),subproject.getUrl(),p.getMpIp(),p.getMpName(),p.getMpId());
				detectionUrlList.add(detectionUrl);
			}
		}
		List<Thread> threadList = new ArrayList<Thread>();
		// 加到线程数组中
		if (Utils.notEmpty(detectionUrlList)) {
			for (PingUrl temp : detectionUrlList) {
				Thread thread = new Thread(temp);
				thread.start();
				threadList.add(thread);
			}
		}
		boolean isOver = false;
		Thread thread = null;

		Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
		Map<String, String> map1 = null;
		List<Map<String, String>> list = null;
		int trueCount = 0;
		int[] ints = new int[4];
		int index = 0;
		//判断进程是否结束，并且记录结果
		while (!isOver) {
			if (Utils.notEmpty(threadList)) {
				for (int i = 0; i < threadList.size(); i++) {
					index = 0;
					list = null;
					thread = threadList.get(i);
					if (!dataMap.containsKey(i + "") && !thread.isAlive()) {
						list = MonitorTrigger.gson.fromJson(detectionUrlList.get(i)
								.getStr(),
								new TypeToken<List<Map<String, String>>>() {
								}.getType());
						
						if (Utils.notEmpty(list)) {
							int loss_data = 0;
							map1 = new HashMap<String, String>();
							for (Map<String, String> tempMap : list) {
								for (Map.Entry<String, String> entry : tempMap
										.entrySet()) {
									if (!"address".equals(entry.getKey())) {
										if (entry.getValue().equals("Timed out")
												|| entry.getValue().startsWith(
														"fail:")) {
											loss_data++;
											ints[index++] = -1;
											map1.put("ip", getIP(entry.getKey()));
										} else {
											ints[index++] = Integer.parseInt(entry
													.getValue().substring(
															0,
															entry.getValue()
																	.lastIndexOf(
																			"ms")));
											map1.put("ip", getIP(entry.getKey()));
										}
									} else {
										map1.put("address", entry.getValue());

									}
								}
							}

							map1.put("min", getMinArray(ints)+"");
							map1.put("max", getMaxArray(ints)+"");
							map1.put("avg", getAvgArray(ints)+"");
							if (loss_data > 0) {
								if (loss_data < 4) {
									map1.put("result", "丢包("
											+ (loss_data * 100 / 4 * 0.01) + "%)");
								} else {
									map1.put("result", "不可用");
								}

							} else {
								map1.put("result", "可用");
							}
							if(map1!=null){
								map1.put("mpId",detectionUrlList.get(i).mpId);
							}
							dataMap.put(i + "", map1);
							trueCount++;
						}else{
							map1 = new HashMap<String, String>();
							map1.put("result","不可用");
							map1.put("ip","无响应");
							if(map1!=null){
								map1.put("mpId",detectionUrlList.get(i).mpId);
							}
							dataMap.put(i + "", map1);
							trueCount++;
						}
					}
//					else {
//						if(dataMap.containsKey(i + "")){
//							continue;
//						}
//						long time = System.currentTimeMillis();
//						if(detectionUrlList.get(i).g!=0L&&(time - detectionUrlList.get(i).getBeginTime()>8000)){
//							detectionUrlList.get(i).setInterrupted(true);
////							thread.interrupt();
//							String str = "{\"address\":\""+detectionUrlList.get(i).serverName+"\",\"ip\":\"无响应\"," +
//									"\"result\":\"无响应\",\"status\":\"连接超时\",\"responseTime\":\"无响应\",\"size\":\"无响应\"}";
//							detectionUrlList.get(i).setStr(str);
//						}
//					}
				}
			}
			if (trueCount == threadList.size()) {
				isOver = true;
			}
		}
		Date time = new Date();
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
					boolean isOverTime = false;
					if(responseTimeThreshold > 0) {
						String totalTime = tempMap.get("totalTime");
						if(Utils.notEmpty(totalTime)) {
							if(responseTimeThreshold < Double.parseDouble(totalTime)) {
								isOverTime = true;
							}
						}
					}
					if(Utils.notEmpty(results)&&!"可用".equals(results) && !isOverTime ){
						count++;	
					}
						
					//将得到的数据保存到数据库中
					ServiceLocator.getTaskResultService().saveResultByMap(tempMap,Constants.PING_TYPE,subproject.getId(),time);
				}
			}
			//看看是否有故障历史存在。
			faultHistory = ServiceLocator.getFaultHistoryService().getFaultHistoryByTaskId(subproject.getId(),false);
			
//			Subproject schl =(Subproject) dao.getQueryList("FROM Subproject t WHERE t.id=?",map.get("id")).get(0);
			
			if(count==dataMap.size()){
				//监控访问出错，不可用
				if(faultHistory==null){
					faultHistory = new FaultHistory();
					faultHistory.setTaskId(subproject.getId());
					faultHistory.setFhBeginTime(time);
					faultHistory.setFhReason(Utils.notEmpty(tempMap.get("status"))?tempMap.get("status"):"访问超时");
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
//						subproject.setHasRetry(hasRetry);
//						configMap.put("warningTime", String.valueOf(new Date().getTime()));
//						subproject.setConfigXml(Utils.mapToString(configMap));
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
							content.append("项目‘").append(subproject.getName()).append("’出现故障，发现连接不通");
						}
						
						if(email!=null&&email.length>0){
							StringBuilder sb = new StringBuilder();
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
//						subproject.setFrequency(Math.round(frequency/2));
						faultHistory.setRetry(hasRetry);
						dao.updateObject(faultHistory);
						subproject.setConfigXml(Utils.mapToString(configMap));
						dao.updateObject(subproject);
						//将保存的定时任务激活
						SchedulerMethods.loadJobs();
					}
					
				}
			}else{
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
	
	// 求数组最大值
	public int getMaxArray(int[] intArray) {

		int maxArray = 0;
		// 数组中为正数的第一个位置
		int index = -1;
		for (int i = 0; i < intArray.length; i++) {
			if (intArray[i] >= 0) {
				maxArray = intArray[0];
				index = i;
				break;
			}
		}
		if (index > -1) {
			for (int i = index; i < intArray.length; i++) {
				if (intArray[i] >= 0 && intArray[i] > maxArray) {
					maxArray = intArray[i];
				}
			}
		} else {
			maxArray = -1;
		}
		return maxArray;
	}

	// 求数组最小值
	public int getMinArray(int[] intArray) {
		int minArray = 0;
		// 数组中为正数的第一个位置
		int index = -1;
		for (int i = 0; i < intArray.length; i++) {
			if (intArray[i] >= 0) {
				minArray = intArray[i];
				index = i;
				break;
			}
		}
		if (index > -1) {
			for (int i = index; i < intArray.length; i++) {
				if (intArray[i] >= 0 && intArray[i] < minArray) {
					minArray = intArray[i];
				}
			}
		} else {
			minArray = -1;
		}

		return minArray;
	}

	// 求数组的平均值
	public double getAvgArray(int[] intArray) {
		int count = 0;
		int sum = 0;
		double avg = 0;
		for (int i = 0; i < intArray.length; i++) {
			if (intArray[i] >= 0) {
				sum += intArray[i];
				count++;
			}
		}
		if (count > 0) {
			avg = sum * 100 / count * 0.01;
		}

		return avg;
	}

	public String getIP(String ipAddress) {
		String ipStr = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
		Pattern pattern = Pattern.compile(ipStr);
		Matcher matcher = pattern.matcher(ipAddress);
		String ip = "";
		if (matcher.find()) {
			ip = matcher.group();
		}
		return ip;

	}
	
	private String[] getNotifycation(String notifycationType, String emailType) {
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

}
