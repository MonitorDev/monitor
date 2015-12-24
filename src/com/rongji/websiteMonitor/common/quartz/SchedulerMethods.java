package com.rongji.websiteMonitor.common.quartz;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.persistence.PubScheduler;
import com.rongji.websiteMonitor.persistence.Subproject;



/**
 * 定时器通用方法类
 * @author I-TASK team
 *
 */
public class SchedulerMethods {
	private static Scheduler scheduler = null;
	/**
	 * 取得定时器的句柄
	 * @return
	 */
	public static Scheduler getScheduler(){
		if(scheduler==null){
			synchronized(SchedulerMethods.class){
				if(scheduler==null){
					try {
						scheduler=StdSchedulerFactory.getDefaultScheduler();
						scheduler.start();
					} catch (SchedulerException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		return scheduler;
	}
	/**
	 * 装载数据库中的定时器
	 * @return Map<String,String>;"true":"操作成功";"false":"操作失败信息";
	 */
	@SuppressWarnings("unchecked")
	public static synchronized Map<String,String> loadJobs(){
		Map<String,String> result = new HashMap<String, String>();
		String ret = "true";
		StringBuilder msg = new StringBuilder();
		Scheduler sch=getScheduler();
		PubCommonDAO dao = FrameworkHelper.getDAO();
		List<Subproject> list = dao.getQueryList("FROM Subproject t WHERE t.isuable='1' ORDER BY t.id");
		
		try {
			// 判断哪些事务要重新安排，哪些事务需要新增，哪些事务需要删除。
			String[] fJobNames=scheduler.getJobNames(Scheduler.DEFAULT_GROUP);
			Set<String> fJobNameSet=new HashSet<String>(fJobNames.length);
			for(String jobName:fJobNames){
				fJobNameSet.add(jobName.substring(1));
			}
			Set<String> nJobNameSet=new HashSet<String>();
			for(Subproject o:list){
				nJobNameSet.add(o.getId());
			}
			Set<String> addNameSet = new HashSet<String>(nJobNameSet);
			Set<String> delNameSet = new HashSet<String>(fJobNameSet);
			addNameSet.removeAll(fJobNameSet);
			delNameSet.removeAll(nJobNameSet);
			//删除事务
			for(String name :delNameSet){
				sch.deleteJob("j"+name, Scheduler.DEFAULT_GROUP);
				sch.unscheduleJob("t"+name, Scheduler.DEFAULT_GROUP);
			}
			int count = 0;
			for(Subproject p:list){
				JobDetail jobDetail=buildJobDetail(p);
				try{
					Trigger trigger=buildTrigger(p);
					if(addNameSet.contains(p.getId())){
						//新增事务
						sch.scheduleJob(jobDetail, trigger);
					}else{
						//修改事务
						sch.addJob(jobDetail, true);
						sch.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
					}
					count++;
				}catch(Exception ex){
					ret = "false";
					ex.printStackTrace();
					p.setIsuable("0");
					dao.updateObject(p);
					msg.append("定时服务 【 "+p.getName()+" 】 应用出错!\r异常提示：｛"+ex.getMessage()+"｝\r");
				}
			}
		} catch (SchedulerException e) {
			ret = "false";
			msg.append("应用出现异常：｛"+e.getMessage()+"｝\n");
			e.printStackTrace();
		}
		result.put("ret", ret);
		result.put("msg", msg.length()>0?(msg.toString()+"请检查配置是否正确，或者联系管理员！"):"");
		return result;
	}
	public static void rescheduleJob(Subproject schl){
		if(!"1".equals(schl.getIsuable())){
			return;
		}
		Scheduler sch=getScheduler();
		PubCommonDAO dao = FrameworkHelper.getDAO();
		JobDetail jobDetail=buildJobDetail(schl);
		try {
			Trigger trigger=buildTrigger(schl);
			sch.addJob(jobDetail, true);
			sch.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
		} catch (Exception ex) {
			ex.printStackTrace();
			schl.setIsuable("0");
			dao.updateObject(schl);
		}
	}
	
	public static void scheduleJob(Subproject schl){
		if(!"1".equals(schl.getIsuable())){
			return;
		}
		Scheduler sch=getScheduler();
		PubCommonDAO dao = FrameworkHelper.getDAO();
		JobDetail jobDetail=buildJobDetail(schl);
		try {
			Trigger trigger=buildTrigger(schl);
			sch.scheduleJob(jobDetail, trigger);
		} catch (Exception ex) {
			ex.printStackTrace();
			schl.setIsuable("0");
			dao.updateObject(schl);
		}
	}
	public static void unscheduleJob(PubScheduler schl) throws SchedulerException{
		if("1".equals(schl.getUsable())){
			return;
		}
		Scheduler sch=getScheduler();
		String name=schl.getSchlId();
		sch.deleteJob("j"+name, Scheduler.DEFAULT_GROUP);
		sch.unscheduleJob("t"+name, Scheduler.DEFAULT_GROUP);
	}
	
	public static void applyScheduler(Subproject schl) throws SchedulerException, ParseException{
		Scheduler sch=getScheduler();
		String name=schl.getId();
		String[] jobNameArray=sch.getJobNames(Scheduler.DEFAULT_GROUP);
		List<String> jobNames=Arrays.asList(jobNameArray);
//		System.out.println("jobNames="+jobNames);
		if("1".equals(schl.getIsuable())){
			JobDetail jobDetail=buildJobDetail(schl);
			Trigger trigger=buildTrigger(schl);
			if(jobNames.contains("j"+name)){
				sch.addJob(jobDetail, true);
				sch.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
			}else{
				sch.scheduleJob(jobDetail, trigger);
			}
		}else{
			if(jobNames.contains("j"+name)){
				sch.deleteJob("j"+name, Scheduler.DEFAULT_GROUP);
				sch.unscheduleJob("t"+name, Scheduler.DEFAULT_GROUP);
			}
		}
	}
	
	/**
	 * 根据配置产生JobDetail
	 * @param p
	 * @return
	 */
	private static JobDetail buildJobDetail(Subproject p) {
		JobDetail jobDetail = new JobDetail("j"+p.getId(),Scheduler.DEFAULT_GROUP, ActionExecutor.class);
		jobDetail.getJobDataMap().put("clzName", p.getClzName());
		StringBuffer sb = new StringBuffer();
		sb.append("id:").append(p.getId()).append(";")
			.append("type:").append(p.getType()).append(";");
		jobDetail.getJobDataMap().put("configXml", sb.toString());
		return jobDetail;
	}
	/**
	 * 根据配置产生Trigger
	 * @param p
	 * @return
	 * @throws ParseException 
	 */
	private static Trigger buildTrigger(Subproject p) throws ParseException {
		Trigger trigger =null;
//		if("2".equals(p.getType())){
//			SimpleTrigger stg=new SimpleTrigger();
//			trigger=stg;
//			stg.setEndTime(p.getEndTime());
//			stg.setStartTime(p.getStartTime()==null?new Date():p.getStartTime());
//			stg.setRepeatCount(p.getRepeatCount()==null?0:p.getRepeatCount().intValue());
//			int repeatInterval=p.getRepeatInterval()==null?0:p.getRepeatInterval().intValue();
//			stg.setRepeatInterval(repeatInterval*1000L);
//		}else{
			CronTrigger ctg=new CronTrigger();
			trigger=ctg;
			ctg.setCronExpression(p.getCronExpression());
//			ctg.setEndTime(p.getEndTime());
//			ctg.setStartTime(p.getStartTime()==null?new Date():p.getStartTime());	
			ctg.setStartTime(new Date());
//		}
		trigger.setName("t"+p.getId());
		trigger.setJobGroup(Scheduler.DEFAULT_GROUP);
		trigger.setJobName("j"+p.getId());
		return trigger;
	}
	
	/**
	 * 关闭定时器
	 */
	public static void shutdown(){
		try {
			shutdown(true);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 关闭定时器
	 * @param waitForJobsToComplete
	 * @throws SchedulerException
	 */
	public static void shutdown(boolean waitForJobsToComplete) throws SchedulerException{
			Scheduler scheduler=getScheduler();
			scheduler.shutdown(true);
	}
	/**
	 * 取得这个定时事务预计执行时间
	 * @param sch
	 * @return
	 */
	public static Date getNextFireTime(PubScheduler sch) {
		Trigger trigger;
		try {
			Scheduler scheduler=getScheduler();
			if(scheduler==null)return null;//定时未启动
			trigger =scheduler.getTrigger("t"+sch.getSchlId(),Scheduler.DEFAULT_GROUP);
			if(trigger==null)return null;//没有找到Trigger,可能已经禁用或失效
			return trigger.getNextFireTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}




