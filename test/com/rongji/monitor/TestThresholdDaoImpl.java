package com.rongji.monitor;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.websiteMonitor.dao.ThresholdDao;
import com.rongji.websiteMonitor.persistence.Threshold;

public class TestThresholdDaoImpl {

	ApplicationContext context = null;
	@Before
	public void before() {
		context = new ClassPathXmlApplicationContext( 
		        "applicationContext.xml"); 
	}
	
	@Test
	public void testSave() {
		
	}
	
	@Test
	public void testGetById() {
		ThresholdDao td = (ThresholdDao) context.getBean("thresholdDao");
		Threshold th = td.getThresholdById("0000001");
		System.out.println(th.getName());
	}
}
