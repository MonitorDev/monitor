package com.rongji.websiteMonitor.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.FaultHistoryDao;
import com.rongji.websiteMonitor.persistence.FaultHistory;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class FaultHistoryDaoImpl extends PubCommonDAOImpl implements
		FaultHistoryDao {
	private static final Logger logger = Logger.getLogger(FaultHistoryDaoImpl.class);
	private static final String tblName = "FaultHistory";
	private static final String idName = "fhId";
	private static final String initId = "0000000000000001";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void saveFaultHistory(FaultHistory faultHistory) {
		if(faultHistory!=null){
			faultHistory.setFhId(FrameworkHelper.getNewId(tblName, idName, initId));
			this.saveObject(faultHistory);
		}
		
	}
	@Override
	public FaultHistory getFaultHistoryByTaskId(String taskId,
			boolean isIgnoreEndTime) {
		return getFaultHistoryByParam(taskId, null, null, isIgnoreEndTime);
	}
	@Override
	public void updateFaultHistory(FaultHistory faultHistory) {
		if(faultHistory!=null){
			this.updateObject(faultHistory);
		}
		
	}
	@Override
	public Object getAvailableRate(QueryCondition condition) {
		if(condition==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		List par = new ArrayList();
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = condition.getStartTime()!=null?sdf2.parse(sdf.format(condition.getStartTime())+" 00:00:01"):null;
			 endDate = condition.getEndTime()!=null?sdf2.parse(sdf.format(condition.getEndTime())+" 23:59:59"):null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String mpId = condition.getMpId();
		String taskId = condition.getTaskId();
		long diff = 0L;
		if(startDate!=null&&endDate!=null){
			diff = (endDate.getTime()-startDate.getTime())/1000;
		}else if(startDate!=null){
			diff = (new Date().getTime()-startDate.getTime())/1000;
		}else{
			Task task = ServiceLocator.getTaskService().getTask(taskId);
			if(task!=null){
				startDate = task.getCreateTime();
			}
			diff = (new Date().getTime()-startDate.getTime())/1000;
		}
		//计算到秒
		sb.append("Select  to_char((1-sum((t.fhEndTime-t.fhBeginTime)* 24 * 60*60)/").append(diff).append(")*100,'99999999.99') From ").append(tblName).append(" t where  1=1 ");
	
		if(startDate!=null){
			sb.append(" and t.fhBeginTime>= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.fhEndTime<= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}else{
			sb.append(" and t.mpId is null");
		}
		sb.append(" and t.isIgnore is null");
		List<Object> list = getQueryList(sb.toString(), par.toArray());
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<Object[]> getAvailableRatesByCondition(QueryCondition condition) {
		if(condition==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		List par = new ArrayList();
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = condition.getStartTime()!=null?sdf2.parse(sdf.format(condition.getStartTime())+" 00:00:01"):null;
			 endDate = condition.getEndTime()!=null?sdf2.parse(sdf.format(condition.getEndTime())+" 23:59:59"):null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String taskId = condition.getTaskId();
		String mpId = condition.getMpId();
//		select  to_char(t.fh_begin_time,'yyyy-mm-dd') as dateName, to_char((1-sum((t.fh_end_time-t.fh_begin_time)* 24 * 60*60)/86400)*100,'999999.99') 
//		from fault_history t where t.task_id='00000001' group by to_char(t.fh_begin_time,'yyyy-mm-dd') order by to_char(t.fh_begin_time,'yyyy-mm-dd') desc ;
		//计算到秒
		sb.append("Select to_char(t.fhBeginTime,'mmdd') as dateName, to_char((1-sum((t.fhEndTime-t.fhBeginTime)* 24 * 60*60)/86400)*100,'99999999.99')").append(" From ");
		sb.append(tblName).append(" t where  1=1 ");
		sb.append(" and t.isIgnore is null");
		if(startDate!=null){
			sb.append(" and t.fhBeginTime>= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.fhEndTime<= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}else{
			sb.append(" and t.mpId is null");
		}
		sb.append(" group by to_char(t.fhBeginTime,'mmdd')");
//		 order by to_char(t.fhBeginTime,'yyyy-mm-dd') desc 
		List<Object[]> list = getQueryList(sb.toString(), par.toArray());
		
		return list;
	}
	@Override
	public List<FaultHistory> getFaultHistoryByCondition(
			QueryCondition condition,Page page) {
		if(condition==null){
			return null;
		}
		String hql = "From "+tblName +" t where 1=1";
		List<Object> par = new ArrayList<Object>();		
		Date startDate = condition.getStartTime();
		Date endDate = condition.getEndTime();
		
//		try {
//			startDate = condition.getStartTime()!=null?sdf2.parse(sdf.format(condition.getStartTime())+" 00:00:01"):null;
//			 endDate = condition.getEndTime()!=null?sdf2.parse(sdf.format(condition.getEndTime())+" 23:59:59"):null;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		String taskId = condition.getTaskId();
		String mpId = condition.getMpId();
		String fhId = condition.getFhId();
		if(Utils.notEmpty(mpId)){
			hql = hql +" and (t.mpId = ? or t.mpId is null)";
			par.add(mpId);
		}else{
			hql = hql +" and t.mpId is null";
		}
		if(Utils.notEmpty(taskId)){
			hql = hql +" and t.taskId = ?";
			par.add(taskId);
		}
		if(startDate!=null){
			hql = hql +" and (t.fhBeginTime>= ? or t.fhEndTime is null)";
			par.add(startDate);
		}
		if(endDate!=null){
			hql = hql +" and (t.fhEndTime<= ? or t.fhEndTime is null)";
			par.add(endDate);
		}
		if(Utils.notEmpty(fhId)){
			hql = hql +" and t.fhId = ?";
			par.add(fhId);
		}
		hql = hql +" order by t.fhBeginTime desc";
		List<FaultHistory> list = null;
		if(page!=null){
			list = getQueryList(hql, page, true, par.toArray());
		}else{
			list = getQueryList(hql, par.toArray());;
		}
		
		return list;
	}
	@Override
	public FaultHistory getFaultHistoryByParam(String taskId, String type, String mpId,
			boolean isIgnoreEndTime) {
		String hql = "From "+tblName +" t where 1=1";
		List<Object> par = new ArrayList<Object>();
		if(!isIgnoreEndTime){
			hql = hql + " and t.fhEndTime is null";
		}
		if(Utils.notEmpty(taskId)){
			hql = hql +" and t.taskId = ?";
			par.add(taskId);
		}
		if(Utils.notEmpty(type)) {
			hql = hql + " and t.fhType = ?";
			par.add(type);
		}
		if(Utils.notEmpty(mpId)){
			hql = hql +" and t.mpId = ?";
			par.add(mpId);
		}else{
			hql = hql +" and t.mpId is null";
		}
		List<FaultHistory> list = getQueryList(hql, par.toArray());
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<FaultHistory> getFaultHistoryByProjectId(String projectId) {
		StringBuffer bf = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		bf.append(" from ").append(tblName).append(" t where 1 = 1");
		if(Utils.notEmpty(projectId)) {
			bf.append(" and t.taskId in(select s.id from Subproject s where s.projectId = ? and s.isuable = 1 )");
			list.add(projectId);
		}
		bf.append(" and  t.fhEndTime is null and t.isIgnore is null and t.mpId is null order by t.fhBeginTime desc");
		return getQueryList(bf.toString(), projectId);
	}
}
