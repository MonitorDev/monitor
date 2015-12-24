package com.rongji.monitor;

import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.service.SchedulerService;

public class TestSchedulerService {

	ApplicationContext context = null;
	@Before
	public void before() {
		context = new ClassPathXmlApplicationContext( 
		        "applicationContext.xml"); 
	}
	
	@Test
	public void testGetSchedulerByTaskId() {
		SchedulerService ss = (SchedulerService) context.getBean("schedulerService");
		PubScheduler s = ss.getSchedulerByTaskId("00000002");
		System.out.println(s.getConfigXml());
	}
}
