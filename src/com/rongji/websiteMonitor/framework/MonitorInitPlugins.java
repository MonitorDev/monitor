package com.rongji.websiteMonitor.framework;

import com.rongji.dfish.framework.InitApp;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.framework.singletonimpl.DefaultSystemConfig;
import com.rongji.websiteMonitor.common.quartz.SchedulerMethods;


/**
 * 监控服务入口，启动定时任务
 * @author zf
 *
 */
public class MonitorInitPlugins extends InitApp {

	@Override
	public void init() {
		super.init();
		 DefaultSystemConfig dsc = new DefaultSystemConfig();
		 dsc.setConfigFile("website_config.xml");
         SystemData.getInstance().setSystemConfig(dsc);
         SchedulerMethods.loadJobs();
//         ServiceLocator.getSenderMailService().sendMail(ConfigConstants.getInstance().getEmail(), "734844277@qq.com", "test", "test");
//         ServiceLocator.getSendNoteMesaageService().sendMessageForMany(new String[]{"18860131216"}, "asdfsadf");
	}

}
