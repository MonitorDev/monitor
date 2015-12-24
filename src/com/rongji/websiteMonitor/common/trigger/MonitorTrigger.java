package com.rongji.websiteMonitor.common.trigger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.MapKey;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.ConfigConstants;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.SNMPConstants;
import com.rongji.websiteMonitor.common.quartz.TriggerAction;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.persistence.AlarmInformHistory;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.SnmpModel;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.webapp.task.helper.thread.DetectionUrl;
import com.rongji.websiteMonitor.webapp.task.helper.thread.DetectionUrlThread;
import com.rongji.websiteMonitor.webapp.task.helper.thread.PingUrl;
import com.rongji.websiteMonitor.webapp.task.helper.thread.PingUrlThread;


public class MonitorTrigger implements TriggerAction {
	
	
	public static Gson gson = new Gson();
    //获取当前系统的CPU 数目
	static int cpuNums = Runtime.getRuntime().availableProcessors();
	//ExecutorService通常根据系统资源情况灵活定义线程池大小
	private static ExecutorService pool = Executors.newFixedThreadPool(cpuNums*Constants.POOL_SIZE);
	//private static ExecutorService pool = MyThreadFactory.getExecutorSevice();
	
	@Override
	public void execute(String config) throws Exception {
		Map<String, String> configMap = Utils.string2Map(config);
		String type = configMap.get("type");
		switch (TYPE.toTYPE(type.toUpperCase())) {
		case HTTP:
			//System.out.println(list.size() + "----------------------------------------" +"monitorPoint" + monitorPoint + ", "+ monitorPoint.split("%3B")[0]);
			DetectionUrlThread detectionUrlThread = new DetectionUrlThread(configMap);
			pool.execute(detectionUrlThread);
			break;
		case PING:
			PingUrlThread pingUrlThread = new PingUrlThread(configMap);
			pool.execute(pingUrlThread);
			break;
		case FTP:
			
			break;
		case DNS:
			
			break;
		case TCP:
			
			break;
			
		case UDP:
			
			break;
		case SMTP:
			
			break;
		case NGINX:
			
			break;

		default:
			break;
		}

	}

	/**
	 * ping ip
	 * @return
	 */
	private String pingUrl()  {
		int number = 0;
	
		Map<String, String> serverMap = null;
		List<PingUrl> detectionUrlList = new ArrayList<PingUrl>();

		PingUrl detectionUrl = new PingUrl("9091", "218.5.2.35","192.168.4.150","福建电信","");
		detectionUrlList.add(detectionUrl);
		
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
		Map<String, String> map = null;
		List<Map<String, String>> list = null;
		int trueCount = 0;
		int[] ints = new int[4];
		int index = 0;

		while (!isOver) {
			if (Utils.notEmpty(threadList)) {
				for (int i = 0; i < threadList.size(); i++) {
					index = 0;
					list = null;
					thread = threadList.get(i);
					if (!thread.isAlive()) {
						if (!dataMap.containsKey(i + "")) {
							list = gson.fromJson(detectionUrlList.get(i)
									.getStr(),
									new TypeToken<List<Map<String, String>>>() {
									}.getType());
						}

					}
					if (Utils.notEmpty(list)) {
						int loss_data = 0;
						map = new HashMap<String, String>();
						for (Map<String, String> tempMap : list) {
							for (Map.Entry<String, String> entry : tempMap
									.entrySet()) {
								if (!"address".equals(entry.getKey())) {
									if (entry.getValue().equals("Timed out")
											|| entry.getValue().startsWith(
													"fail:")) {
										loss_data++;
										ints[index++] = -1;
										map.put("ip", getIP(entry.getKey()));
									} else {
										ints[index++] = Integer.parseInt(entry
												.getValue().substring(
														0,
														entry.getValue()
																.lastIndexOf(
																		"ms")));
										map.put("ip", getIP(entry.getKey()));
									}
								} else {
									map.put("address", entry.getValue());

								}
							}
						}

						map.put("min", getMinArray(ints) + " ms");
						map.put("max", getMaxArray(ints) + " ms");
						map.put("avg", getAvgArray(ints) + " ms");
						if (loss_data > 0) {
							if (loss_data < 4) {
								map.put("result", "丢包("
										+ (loss_data * 100 / 4 * 0.01) + "%)");
							} else {
								map.put("result", "不可用");
							}

						} else {
							map.put("result", "可用");
						}
						dataMap.put(i + "", map);
						trueCount++;
					}
				}
			}
			if (trueCount == threadList.size()) {
				isOver = true;
			}
		}
		return map.toString();
	}

	/**
	 * 检测url
	 * @return
	 */
	private String ditectionUrl() {
		
		List<DetectionUrl> detectionUrlList = new ArrayList<DetectionUrl>();


		DetectionUrl detectionUrl = new DetectionUrl("9091", "http://www.baidu.com","192.168.4.150","福建电信","");
		detectionUrlList.add(detectionUrl);

		List<Thread> threadList = new ArrayList<Thread>();
		// 加到线程数组中
		if (Utils.notEmpty(detectionUrlList)) {
			for (DetectionUrl detection : detectionUrlList) {
				Thread thread = new Thread(detection);
				thread.start();
				threadList.add(thread);
			}
		}
		boolean isOver = false;
		Thread thread = null;
		Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
		Map<String, String> map = null;
		int trueCount = 0;
		while (!isOver) {
			if (Utils.notEmpty(threadList)) {
				for (int i = 0; i < threadList.size(); i++) {
					thread = threadList.get(i);
					if (!thread.isAlive()) {
						if (!dataMap.containsKey(i + "")) {
							map = gson.fromJson(detectionUrlList.get(i)
									.getStr(),
									new TypeToken<Map<String, String>>() {
									}.getType());
							dataMap.put(i + "", map);
							trueCount++;
						}

					}
				}
			}
			if (trueCount == threadList.size()) {
				isOver = true;
			}
		}
		return map.toString();
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
	
	
	

	@Override
	public String getDescription(Locale loc) {
		if (loc.getLanguage().equals("zh")) {
			return "该触发器用于触发监测功能";
		}
		return "scheduler Monitor trigger";
	}

	public enum TYPE {

		HTTP,PING,FTP,DNS,TCP,UDP,SMTP,NGINX,WRONGVALUE;

		public static TYPE toTYPE(String str) {

			try {

				return valueOf(str);

			} catch (Exception e) {

				return WRONGVALUE;

			}

		}

	}


}
