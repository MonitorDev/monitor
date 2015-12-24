package com.rongji.websiteMonitor.common.quartz;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.persistence.Subproject;


/**
 * 利用这个执行器执行定时事务
 * @author ITASK team
 */
public class ActionExecutor implements Job{
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			JobDetail jobDetail=jobExecutionContext.getJobDetail();
			String clzName=jobDetail.getJobDataMap().getString("clzName");
			String configXML=jobDetail.getJobDataMap().getString("configXml");
			Object bean = Class.forName(clzName).newInstance();
			TriggerAction act=(TriggerAction)bean;
			act.execute(configXML);
			// 如果运行结束，即没有下次时间要把定时器改为不可用。
			if(jobExecutionContext.getNextFireTime()==null){
				PubCommonDAO dao = FrameworkHelper.getDAO();
				String schlId=jobDetail.getName().substring(1);
				List<Subproject> list=dao.getQueryList("FROM Subproject t WHERE t.idd=?", schlId);
				if(list.size()>0){
					Subproject schl= list.get(0);
					schl.setIsuable("0");
					dao.updateObject(schl);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
