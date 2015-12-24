package com.rongji.monitor;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.websiteMonitor.dao.TaskResultDao;
import com.rongji.websiteMonitor.persistence.TaskResult;

public class TestTaskResultDaoImp {

	ApplicationContext context = null;
	@Before
	public void before() {
		context = new ClassPathXmlApplicationContext( 
		        "applicationContext.xml"); 
	}
	
	
	@Test
	public void testGetTaskResultByProjectId() {
		TaskResultDao trd = (TaskResultDao) context.getBean("taskResultDao");
		List<TaskResult> list = trd.getLatestByProjectId("00000000002");
		for(TaskResult tr : list) {
			System.out.println(tr.getTrId());
		}
	}
}
