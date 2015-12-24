package test.help;

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
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.ServiceLocator;

public class DetectionUrlThread implements Runnable {
	private String reult;
	//台数
	private int num;
	//监测点
	private List<MonitoringPoint> list;
	//检测的参数
	private Map<String,String> map;
	private boolean isOver;
	private List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
	public DetectionUrlThread(){

	}
	public DetectionUrlThread(int num, List<MonitoringPoint> list,Map<String, String>map) {
		this.num = num;
		this.list = list;
		this.map = map;
	}

	@Override
	public void run() {
		List<DetectionUrl> detectionUrlList = new ArrayList<DetectionUrl>();

		if(num>0){
			for(MonitoringPoint point:list){
				DetectionUrl detectionUrl = new DetectionUrl(point.getMpPort(),map.get("url"),point.getMpIp(),point.getMpName(),point.getMpId());
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
		Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
		Map<String, String> map1 = null;
		int trueCount = 0;
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
									"\"result\":\"无响应\",\"status\":\"无响应\",\"responseTime\":\"无响应\",\"size\":\"无响应\"}";
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
		if(dataMap!=null&&dataMap.size()>0){
			Map<String,String> tempMap = null;
			String results = "";
			FaultHistory faultHistory = null;
			int count = 0;
			for(Map.Entry<String, Map<String, String>> entry:dataMap.entrySet()){
				tempMap = entry.getValue();
				if(tempMap!=null){
					results = tempMap.get("result");
					if(Utils.notEmpty(results)&&!"可用".equals(results)){
						//计算不可用的个数，如果不可用的个数与返回的结果数一致，则被视为该url是不可访问。
						count++;	
					}
						
					//将得到的数据保存到数据库中
//					ServiceLocator.getTaskResultService().saveResultByMap(tempMap,Constants.HTTP_TYPE,map.get("taskId"),time);
					listMap.add(tempMap);
				}
			}
			
		}
		this.isOver = isOver;
	}
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
	}
	public boolean isOver() {
		return isOver;
	}
	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}
	public List<Map<String, String>> getListMap() {
		return listMap;
	}
	public void setListMap(List<Map<String, String>> listMap) {
		this.listMap = listMap;
	}
	
}
