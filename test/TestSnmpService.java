
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.service.SnmpService;

public class TestSnmpService {

	ApplicationContext context = null;
	@Before
	public void before() {
		context = new ClassPathXmlApplicationContext( 
		        "applicationContext.xml"); 
	}
	
	@Test
	public void testGetHourStatistics() {
		SnmpService ss = (SnmpService) context.getBean("snmpService");
		List<Object[]> list = ss.getDayStatistics("00000002", new Date(new Date().getTime()-3600*1000*24*5), new Date());
		for(Object[] objs : list) {
			System.out.println(objs);
		}
	}
	
	
	@Test
	public void sendMessage() {
		ServiceLocator.getSendNoteMesaageService().sendMessageForMany(new String[]{"18860131216"}, "dddddddddddddd");
	}
}
