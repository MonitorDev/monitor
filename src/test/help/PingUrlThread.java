package test.help;

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
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.ServiceLocator;

public class PingUrlThread implements Runnable {
	//台数
	private int num;
	//监测点
	private List<MonitoringPoint> list;
	//检测的参数
	private Map<String,String> map;
	private boolean isOver;
	private List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();

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

	public PingUrlThread(int num, List<MonitoringPoint> list,
			Map<String, String> map) {
		super();
		this.num = num;
		this.list = list;
		this.map = map;
	}

	@Override
	public void run() {
		
		
//		Map<String, String> serverMap = null;
		List<PingUrl> detectionUrlList = new ArrayList<PingUrl>();
//
//		PingUrl detectionUrl = new PingUrl("9091", "218.5.2.35","192.168.4.150","福建电信");
//		detectionUrlList.add(detectionUrl);
		if(num>0){
			for(MonitoringPoint point:list){
				PingUrl detectionUrl = new PingUrl(point.getMpPort(),map.get("url"),point.getMpIp(),point.getMpName(),point.getMpId());
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
					if (!thread.isAlive()) {
						if (!dataMap.containsKey(i + "")) {
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
						

					}
					
					
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
			for(Map.Entry<String, Map<String, String>> entry:dataMap.entrySet()){
				tempMap = entry.getValue();
				if(tempMap!=null){
					results = tempMap.get("result");
					if(Utils.notEmpty(results)&&!"可用".equals(results)){
						count++;	
					}
					this.listMap.add(tempMap);	
					//将得到的数据保存到数据库中
//					ServiceLocator.getTaskResultService().saveResultByMap(tempMap,Constants.PING_TYPE,map.get("taskId"),time);
				}
			}	
		}
		
		this.isOver = isOver;

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
