 package com.rongji.websiteMonitor.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.websiteMonitor.service.message.SendNoteMessageService;
import com.rongji.websiteMonitor.service.message.SenderMailService;


/**
 * 读取Spring Bean服务实例类
 * 
 * @author RJ-CMS Team
 * @version 1.0.0
 * @since 1.0.0 HQJ 2009-9-8
 */
public class ServiceLocator {
	private static final Logger logger = Logger.getLogger(ServiceLocator.class);

	private static ApplicationContext context = null;
	
	private static BeanFactory factory = null;
	
	
//	public static final ServiceLocator thisService = new ServiceLocator(); // 单例类
//	private static final String[] xmlFiles = new String[] { "/system-config.xml","/core-service-demo.xml","/spring-mailServer.xml" };
	private static final String[] xmlFiles = new String[] {
			"/WEB-INF/system-config.xml", "/WEB-INF/applicationContext.xml" };
	

	/**
	 * 获取指定的bean名称的服务类实例,使用XmlBeanFactory(引用资源)加载配置文件
	 * 
	 * @param beanName
	 *            bean名称
	 * @return
	 */
	public static Object getBean(String beanName) {
		Object bean = null;
		try {
			if (factory == null) {
				factory = com.rongji.dfish.framework.SystemData.getInstance().getBeanFactory();
			}
			bean = factory.getBean(beanName);
		} catch (Exception ex) {
			logger.error("获取指定的Bean对象[" + beanName + "]的实例失败，请检查配置文件!", ex);
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 获取指定的bean名称的服务类实例,使用ClassPathXmlApplicationContext(编译路径)加载多个配置文件
	 * 
	 * @param serviceName
	 *            bean名称
	 * @return
	 */
	public static IService getService(String serviceName) {
		IService bean = null;
		try {
			if (context == null) {
				System.out.println("首次调用ApplicationContext !");
				context = new ClassPathXmlApplicationContext(xmlFiles); //加载XML配置文件，通过CLASSPATH路径搜索配置文件，是读取 src 目录下的配置文件 
			}
			bean = (IService) context.getBean(serviceName);
		} catch (Exception ex) {
			logger.error("获取Service Bean对象失败！", ex);
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 通过对DFish-platform接口的封装实现配置文件管理的本地调用接口
	 * @return
	 */
	public static MonitoringPointService getMonitoringPointService(){
		try {
			return (MonitoringPointService)getBean("pointService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static TaskResultService getTaskResultService(){
		try {
			return (TaskResultService)getBean("taskResultService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static TaskService getTaskService(){
		try {
			return (TaskService)getBean("taskService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static FaultHistoryService getFaultHistoryService(){
		try {
			return (FaultHistoryService)getBean("faultHistoryService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static SenderMailService getSenderMailService(){
		try {
			return (SenderMailService)getBean("mailService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static SendNoteMessageService getSendNoteMesaageService(){
		try {
			return (SendNoteMessageService)getBean("smsService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static AlarmInformHistoryService getAlarmInformHistoryService(){
		try {
			return (AlarmInformHistoryService)getBean("alarmInformHistoryService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static SnmpService getSnmpService(){
		try {
			return (SnmpService)getBean("snmpService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public static ThresholdService getThresholdService() {
		try {
			return (ThresholdService)getBean("thresholdService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public static SubprojectService getSubprojectService() {
		try {
			return (SubprojectService)getBean("subprojectService");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
