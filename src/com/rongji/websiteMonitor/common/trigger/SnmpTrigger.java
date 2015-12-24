package com.rongji.websiteMonitor.common.trigger;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


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
import com.rongji.websiteMonitor.persistence.SnmpModel;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ServiceLocator;

/**
 * 
 * @author zf
 *
 */
public class SnmpTrigger implements TriggerAction {
	Snmp snmp = null;
	
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
		String id = configMap.get("id");
		getSnmpStatistics(id, type);
	}

	
	

	@Override
	public String getDescription(Locale loc) {
		if (loc.getLanguage().equals("zh")) {
			return "该触发器用于触发监测功能";
		}
		return "scheduler Monitor trigger";
	}


	private void getSnmpStatistics(final String id, String type) {
		try {
			PubCommonDAO dao = FrameworkHelper.getDAO();
			List<Subproject> listSubproject = dao.getQueryList(" FROM Subproject t where t.id=?",id);
			if(Utils.isEmpty(listSubproject)) {
				return;
			}
			final Subproject subproject = listSubproject.get(0);
			final Map<String, String> configMap  = Utils.string2Map(subproject.getConfigXml());
			snmp = new Snmp(new DefaultUdpTransportMapping());
			snmp.listen();
			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString("public"));
			target.setVersion(SnmpConstants.version2c);
			target.setAddress(new UdpAddress(subproject.getUrl()));
			target.setTimeout(3000); // 3s
			target.setRetries(3);
			
 			PDU pudFirst = new PDU();
			// 网络流量写入
			pudFirst.add(new VariableBinding(new OID(SNMPConstants.IFIN_OID)));
			// 网络流量写出
			pudFirst.add(new VariableBinding(new OID(SNMPConstants.IFOUT_OID)));
			// 磁盘读取量
			pudFirst.add(new VariableBinding(new OID(SNMPConstants.DISKIONREAD_OID)));
			// 磁盘写入量
			pudFirst.add(new VariableBinding(new OID(SNMPConstants.DISKIONWRITTEN_OID)));
			final SnmpModel first = new SnmpModel();
			snmp.send(pudFirst, target, null, new ResponseListener() {
				@Override
				public void onResponse(ResponseEvent event) {
					PDU response = event.getResponse();
					if (response == null) {
						System.out.println("TimeOut...");
					} else {
						if (response.getErrorStatus() == PDU.noError) {
							Vector<? extends VariableBinding> vbs = response
									.getVariableBindings();
							for (VariableBinding vb : vbs) {
								if(SNMPConstants.DISKIONREAD_OID.equals(vb.getOid().toString())){
									first.setDiskIOReadSize(vb.toValueString());
								}else if(SNMPConstants.DISKIONWRITTEN_OID.equals(vb.getOid().toString())){
									first.setDiskIOWrittenSize(vb.toValueString());
								}else if(SNMPConstants.IFIN_OID.equals(vb.getOid().toString())){
									first.setIfInSize(vb.toValueString());
								}else if(SNMPConstants.IFOUT_OID.equals(vb.getOid().toString())){
									first.setIfOutSize(vb.toValueString());
								}
							}
						}
					}
				}
			});
			TimeUnit.SECONDS.sleep(10);
			PDU pdu = new PDU();
			// 系统cpu使用
			pdu.add(new VariableBinding(new OID(SNMPConstants.CPU_OID)));
			// 系统内存总量
			pdu.add(new VariableBinding(new OID(SNMPConstants.MEMORYSIZE_OID)));
			// 系统内存使用
			pdu.add(new VariableBinding(new OID(SNMPConstants.MEMORY_OID)));
			// jvm堆总量
			pdu.add(new VariableBinding(new OID(SNMPConstants.JVMHEADSIZE_OID)));
			// jvm堆使用
			pdu.add(new VariableBinding(new OID(SNMPConstants.JVMHEAD_OID)));
			// jvm线程数
			pdu.add(new VariableBinding(new OID(SNMPConstants.JVMTHREAD_OID)));
			// 网络流量写入
			pdu.add(new VariableBinding(new OID(SNMPConstants.IFIN_OID)));
			// 网络流量写出
			pdu.add(new VariableBinding(new OID(SNMPConstants.IFOUT_OID)));
			// 磁盘读取量
			pdu.add(new VariableBinding(new OID(SNMPConstants.DISKIONREAD_OID)));
			// 磁盘写入量
			pdu.add(new VariableBinding(new OID(SNMPConstants.DISKIONWRITTEN_OID)));			
			// 系统进程数
			pdu.add(new VariableBinding(new OID(SNMPConstants.SYSTEMPROCESS_OID)));	
			//磁盘总量
			pdu.add(new VariableBinding(new OID(SNMPConstants.STORAGESIZE_OID)));
			//磁盘使用量
			pdu.add(new VariableBinding(new OID(SNMPConstants.STORAGEUSED_OID)));
			pdu.setType(PDU.GET);
			snmp.send(pdu, target, null, new ResponseListener() {
				@Override
				public void onResponse(ResponseEvent event) {
					PDU response = event.getResponse();
					if (response == null) {
						System.out.println("TimeOut...");
					} else {
						if (response.getErrorStatus() == PDU.noError) {
							Vector<? extends VariableBinding> vbs = response
									.getVariableBindings();
							SnmpModel sm = new SnmpModel();
							sm.setTaskId(id);
							for (VariableBinding vb : vbs) {
								if(SNMPConstants.CPU_OID.equals(vb.getOid().toString())) {
									sm.setCpuUsedRate(vb.toValueString());
								}else if(SNMPConstants.MEMORY_OID.equals(vb.getOid().toString())){
									sm.setMemoryUsedSize(vb.toValueString());
								}else if(SNMPConstants.JVMHEAD_OID.equals(vb.getOid().toString())){
									sm.setJvmHeapUsedSize(vb.toValueString());
								}else if(SNMPConstants.JVMTHREAD_OID.equals(vb.getOid().toString())){
									sm.setJvmTheadSize(vb.toValueString());
								}else if(SNMPConstants.MEMORYSIZE_OID.equals(vb.getOid().toString())){
									sm.setMemoryTotalSize(vb.toValueString());
								}else if(SNMPConstants.JVMHEADSIZE_OID.equals(vb.getOid().toString())){
									sm.setJvmHeadTotalSize(vb.toValueString());
								}else if(SNMPConstants.DISKIONREAD_OID.equals(vb.getOid().toString())){
									String ioReadSize = first.getDiskIOReadSize();
									if(Utils.notEmpty(ioReadSize)) {
										sm.setDiskIOReadSize((Long.parseLong(vb.toValueString())-Long.parseLong(ioReadSize))/10 +"");
									}else {
										sm.setDiskIOReadSize("0");
									}
									
								}else if(SNMPConstants.DISKIONWRITTEN_OID.equals(vb.getOid().toString())){
									String ioWriteSize = first.getDiskIOWrittenSize();
									if(Utils.notEmpty(ioWriteSize)) {
										sm.setDiskIOWrittenSize((Long.parseLong(vb.toValueString())-Long.parseLong(ioWriteSize))/10 +"");
									}else {
										sm.setDiskIOWrittenSize("0");
									}
								}else if(SNMPConstants.SYSTEMPROCESS_OID.equals(vb.getOid().toString())){
									sm.setSystemProcess(vb.toValueString());
								}else if(SNMPConstants.STORAGESIZE_OID.equals(vb.getOid().toString())){
									sm.setStorageSize(vb.toValueString());
								}else if(SNMPConstants.STORAGEUSED_OID.equals(vb.getOid().toString())){
									sm.setStorageUsed(vb.toValueString());
								}else if(SNMPConstants.IFIN_OID.equals(vb.getOid().toString())){
									String ifin = first.getIfInSize();
									if(Utils.notEmpty(ifin)) {
										sm.setIfInSize((Long.parseLong(vb.toValueString())-Long.parseLong(ifin))/10 +"");
									}else {
										sm.setIfInSize("0");
									}
									sm.setIoUsedSize(vb.toValueString());
								}else if(SNMPConstants.IFOUT_OID.equals(vb.getOid().toString())){
									String ifOut = first.getIfOutSize();
									if(Utils.notEmpty(ifOut)) {
										sm.setIfOutSize((Long.parseLong(vb.toValueString())-Long.parseLong(ifOut))/10 +"");
									}else {
										sm.setIfOutSize("0");
									}
								}
							}
							sm.setCreateTime(new Date());
//							System.out.println(sm.getCpuUsedRate() + ";memory:" + sm.getMemoryUsedSize() + "; jvmhead:" + sm.getJvmHeapUsedSize() + "; jvmThread: " + sm.getJvmTheadSize() + "; io:" + sm.getIoUsedSize() + "; jvmTotal:" + sm.getJvmHeadTotalSize() + "; memoryTotal:" + sm.getMemoryTotalSize());
							ServiceLocator.getSnmpService().insertSnmp(sm);
							String continueGroup = configMap.get("continueGroup");
//							System.out.println("configMap");
							if(Utils.notEmpty(continueGroup)) {
								String[] continueGroups = continueGroup.split(",");
								String sequenceWarningStr = configMap.get("sequenceWarning");
								int sequenceWarning = 0; 
								if(Utils.notEmpty(sequenceWarningStr)) {
									sequenceWarning = Integer.parseInt(sequenceWarningStr);
								}
								for(String cgId : continueGroups) {
									String phone = configMap.get("phone"+cgId);
									String email = configMap.get("email"+cgId);
//									if(Utils.notEmpty(phone) || Utils.notEmpty(email)) {
										String[] phones = Utils.isEmpty(phone)?new String[]{}:phone.split(",");
										String[] emails = Utils.isEmpty(email)?new String[]{}:email.split(",");
										Threshold threshold = ServiceLocator.getThresholdService().getThresholdById(cgId);
										if(threshold != null) {
											Map<String, String> map = Utils.string2Map(threshold.getContent());
											double cpu = Double.parseDouble(map.get("cpu"));
											double memory = Double.parseDouble(map.get("memory"));
											double jvmMemory = Double.parseDouble(map.get("jvmMemory"));
											int jvmThread = Integer.parseInt(map.get("jvmThread"));
//											double io = Double.parseDouble(map.get("io"));
											double systemProcess = Double.parseDouble(map.get("systemProcess"));
											double diskUsage = Double.parseDouble(map.get("diskUsage"));
											int retry = threshold.getRetry();
//											System.out.println("jvmThread------------" + jvmThread + ", " + sm.getJvmTheadSize());
											handlerThreshold(id, subproject,"cpu", "CPU使用率", cpu, Double.parseDouble(sm.getCpuUsedRate()),retry, sequenceWarning, emails, phones);
											handlerThreshold(id, subproject,"memory", "内存使用率", memory, Double.parseDouble(sm.getMemoryUsedSize())/Double.parseDouble(sm.getMemoryTotalSize())*100,retry, sequenceWarning, emails, phones);
											handlerThreshold(id, subproject, "jvmMemory", "JVM内存使用率", jvmMemory, Double.parseDouble(sm.getJvmHeapUsedSize())/Double.parseDouble(sm.getJvmHeadTotalSize())*100,retry, sequenceWarning, emails, phones);
											handlerThreshold(id, subproject, "jvmThread", "JVM线程数量", jvmThread, Double.parseDouble(sm.getJvmTheadSize()),retry, sequenceWarning, emails, phones);
											handlerThreshold(id, subproject,"systemProcess", "系统进程数", systemProcess, Double.parseDouble(sm.getSystemProcess()),retry, sequenceWarning, emails, phones);
											handlerThreshold(id, subproject,"diskUsage", "磁盘使用率", diskUsage, Double.parseDouble(sm.getStorageUsed())/Double.parseDouble(sm.getStorageSize())*100,retry, sequenceWarning, emails, phones);
										}
//									}
								}
							}
						}
						
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			if (snmp != null) {
				try {
					snmp.close();
				} catch (IOException ex1) {
					snmp = null;
				}
			}
		}
	}
	
	public void handlerThreshold(String id, Subproject subproject, String name, String title, double thresholdValue, double value, int retry, int sequenceWarning, String[] email, String[] phone) {
		FaultHistory sfh = ServiceLocator.getFaultHistoryService().getFaultHistoryByParam(id, name,null, false);
		if(thresholdValue < value) {
			if(sfh == null) {
				sfh = new FaultHistory();
				sfh.setTaskId(id);
				sfh.setFhBeginTime(new Date());
				sfh.setFhType(name);
				sfh.setRetry(1);
				sfh.setFhReason(title+"超标");
				ServiceLocator.getFaultHistoryService().saveFaultHistory(sfh);
			}else {
				StringBuilder content = new StringBuilder();
				if(retry == sfh.getRetry()){
					//alarm(configMap, "网络流量");
					content.append("项目‘").append(subproject.getName()).append("’出现故障;");
					content.append(title).append("使用超出预定阀值");
					alarm(email, phone,"故障通知",content.toString(),subproject);
					Date date = new Date();
					sfh.setAlarmTime(date);
				}else if(retry < sfh.getRetry()){
					long alertTime = sfh.getAlarmTime().getTime();
					if(sequenceWarning > 0) {
						Date date = new Date();
						if((date.getTime()-alertTime)>=sequenceWarning*60*1000) {
							sfh.setAlarmTime(date);
							content.append("项目‘").append(subproject.getName()).append("’出现故障;");
							content.append(title).append("使用超出预定阀值");
							alarm(email, phone,"故障通知",content.toString(),subproject);
						}
					}
				}
				sfh.setRetry(sfh.getRetry() + 1);
				ServiceLocator.getFaultHistoryService().updateFaultHistory(sfh);
			}
		}else {
			if(sfh != null) {
				if(retry < sfh.getRetry()){
					StringBuilder content = new StringBuilder();
					content.append("项目‘").append(subproject.getName()).append("’故障恢复;");
					content.append(title).append("使用超出预定阀值的故障已经恢复");
					alarm(email, phone,"故障恢复",content.toString(),subproject);
				}
				sfh.setFhEndTime(new Date());
				ServiceLocator.getFaultHistoryService().updateFaultHistory(sfh);
			}
		}
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
	
	private void alarm(String[]email, String[] tel, String title, String content, Subproject subproject) {
		if(email!=null&&email.length>0){
			for(String eml:email){
				ServiceLocator.getSenderMailService().sendMail(ConfigConstants.getInstance().getEmail(), eml, title, content.toString());
				//记录告警通知
				AlarmInformHistory alarmInformHistory = new AlarmInformHistory(); 
				alarmInformHistory.setAiTime(new Date());
				alarmInformHistory.setAiType(Constants.EMAIL_TYPE);
				alarmInformHistory.setAiWay(eml);
				alarmInformHistory.setTaskId(subproject.getId());
				alarmInformHistory.setAiContent(content);
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
				alarmInformHistory.setAiContent(content);
				ServiceLocator.getAlarmInformHistoryService().saveAlarmInformHistory(alarmInformHistory);
			}
		}
	}
}
