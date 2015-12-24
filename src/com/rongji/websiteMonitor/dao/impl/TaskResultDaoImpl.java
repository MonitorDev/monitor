package com.rongji.websiteMonitor.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.Constants;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.TaskResultDao;
import com.rongji.websiteMonitor.persistence.TaskResult;
import com.rongji.websiteMonitor.service.TaskResultService;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class TaskResultDaoImpl extends PubCommonDAOImpl implements
		TaskResultDao {

	private static final Logger logger = Logger.getLogger(TaskResultDaoImpl.class);
	private static final String tblName = "TaskResult";
	private static final String idName = "trId";
	private static final String initId = "0000000000000001";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void saveResult(TaskResult result) {
		if(result!=null){
			String id = FrameworkHelper.getNewId(tblName, idName, initId);
			result.setTrId(id);
			saveObject(result);
		}
		
	}
	@Override
	public Object getAvgResponse(QueryCondition condition) {
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
		sb.append("Select round(avg(t.trRespTime) ").append(",3) From ").append(tblName).append(" t where  1=1 ");
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
//		sb.append(" and t.trRespTime is not null");
		List<Object> list = getQueryList(sb.toString(), par.toArray());
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<Object[]> getMPAvailableRateByCondition(QueryCondition condition) {
		if(condition==null){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		List par = new ArrayList();
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
		sb.append("Select t.mpId,t1.mpName,t.isuseable,sum(t.times) From ").append(tblName).append(" t,MonitoringPoint t1 where  1=1 ");
		sb.append(" and t1.mpId = t.mpId");
		//		select t.mp_id,t1.mp_name,t.isuseable,count(t.isuseable) from task_result t,monitoring_point t1 where t1.mp_id = t.mp_id 
//		and t.task_id = '00000001'  ;
		if(startDate!=null){
			sb.append(" and t.staDate>= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate<= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		
		sb.append(" group by t.mpId,t.isuseable,t1.mpName");
		
		return  getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getResponseTimeByCondition(QueryCondition condition,boolean isGetMonitorPoint) {
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
		if(isGetMonitorPoint){
			sb.append("Select t.mpId, t.dataTime ,min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");
		}else{
			sb.append("Select t.dataTime ,min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(isGetMonitorPoint){
			sb.append("group by t.mpId,t.dataTime  order by t.dataTime  asc");
		}else{
			sb.append("group by t.dataTime  order by t.dataTime  asc");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getResponseDetialTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint) {
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
		if(isGetMonitorPoint){
			sb.append("Select t.mpId, t.dataTime ,round(avg(t.ext1),3),round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3) From ");
		}else{
			sb.append("Select t.dataTime ,round(avg(t.ext1),3),round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(isGetMonitorPoint){
			sb.append("group by t.mpId,t.dataTime  order by t.dataTime  asc");
		}else{
			sb.append("group by t.dataTime  order by t.dataTime  asc");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getStartAndEndTime() {
		String hql = "select min(t.staDate),max(t.staDate) from "+tblName+" t";
		return getQueryList(hql);
	}
	@Override
	public List<Object[]> getSumResult(Date startTime, Date endTime,
			String statType) {
//		select min(t.tr_id),t.task_id,t.isuseable,count(t.isuseable) ,min(t.tr_resp_time),max(t.tr_resp_time),to_char(avg(t.tr_resp_time),'999999.999'),to_char(avg(t.ext_1),'999999.999')
//		,to_char(avg(t.ext_2),'999999.999'),to_char(avg(t.ext_3),'999999.999'),to_char(avg(t.ext_4),'999999.999'),t.tr_type,t.mp_id,sum(t.times),to_char(t.sta_date,'yyyy-MM-dd')
//		from task_result t where t.tr_type='http' and t.sta_date>=to_date('2013-08-29 00:00:01','YYYY-MM-dd hh24:mi:ss') 
//		and t.sta_date<=to_date('2013-08-29 23:59:59','yyyy-MM-dd hh24:mi:ss') group by t.isuseable,t.task_id,t.mp_id,t.tr_type,to_char(t.sta_date,'yyyy-MM-dd') ;

		StringBuilder sb = new StringBuilder();
		sb.append("Select min(t.trId),t.taskId,t.isuseable,count(t.times) ,min(t.trRespTime),max(t.trRespTime),round(avg(t.trRespTime),3),round(avg(t.ext1),3)");
		sb.append(",round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3),t.trType,t.mpId,sum(t.times),t.dataTime,");
		sb.append("t.monitoringPoint From ").append(tblName).append(" t where 1=1");
		List<Object> pra = new ArrayList<Object>();
		if(Utils.notEmpty(statType)){
			sb.append(" and t.trType= ?");
			pra.add(statType);
		}
		if(startTime!=null){
			sb.append(" and t.staDate>=?");
			pra.add(startTime);
		}
		if(endTime!=null){
			sb.append(" and t.staDate<=?");
			pra.add(endTime);
		}
		sb.append(" and t.ArchiveLevel=?");
		pra.add(Constants.ARCHIVE_LEVEL_NONR);
		sb.append(" group by t.isuseable,t.taskId,t.mpId,t.trType,t.monitoringPoint,t.dataTime");
		
		return getQueryList(sb.toString(), pra.toArray());
	}
	@Override
	public TaskResult getTaskResultById(String trId) {
		if(Utils.isEmpty(trId)){
			return null;
		}
		String hql = "From "+tblName+" t where t.trId= ?";
		List<TaskResult> list = getQueryList(hql,trId);
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public void deleteResult(String startDate, String endDate, String statType) {
		Session session = this.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Connection con=session.connection();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("delete TASK_RESULT t Where  t.TR_TYPE=? and t.STA_DATE>= to_date(?,'YYYY-MM-DD HH24:mi:ss') and t.STA_DATE <= to_date(?,'YYYY-MM-DD HH24:mi:ss') and t.ARCHIVELEVEL = ?");
			stmt.setString(1, statType);
//			java.sql.Date date = new java.sql.Date(startDate.getTime());
//			java.sql.Date date2 = new java.sql.Date(endDate.getTime());
//			stmt.setDate(2, new java.sql.Date(startDate.getTime()));
//			stmt.setDate(3, new java.sql.Date(endDate.getTime()));
			stmt.setString(2,startDate);
			stmt.setString(3,endDate);
			stmt.setString(4, Constants.ARCHIVE_LEVEL_NONR);
			stmt.executeUpdate();
			tx.commit(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tx.rollback();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			session.close();
		}
		
		
	}
	@Override
	public List<Object[]> getHourAvailableRateByCondition(
			QueryCondition condition) {
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
		sb.append("Select t.mpId,t.isuseable,count(t.trId),to_char(t.staDate,'HH24') From ").append(tblName).append(" t where  1=1 ");
//		sb.append(" and t1.mpId = t.mpId");
		//		select t.mp_id,t1.mp_name,t.isuseable,count(t.isuseable) from task_result t,monitoring_point t1 where t1.mp_id = t.mp_id 
//		and t.task_id = '00000001'  ;
		if(startDate!=null){
			sb.append(" and t.staDate>= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate<= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		
		sb.append("  group by t.mpId,t.isuseable,to_char(t.staDate,'HH24')");
		
		return  getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition, boolean isGetMonitorPoint) {
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
		if(isGetMonitorPoint){
			sb.append("Select t.mpId, to_char(t.staDate,'HH24') ,round(avg(t.trRespTime),3) From ");
		}else{
			sb.append("Select to_char(t.staDate,'HH24') ,round(avg(t.trRespTime),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 ");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(isGetMonitorPoint){
			sb.append("group by t.mpId,to_char(t.staDate,'HH24') ");
		}else{
			sb.append("group by to_char(t.staDate,'HH24') ");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<TaskResult> getTaskResultByDay(long dateTime, String taskId,
			String mapId, String trType, String isuseable) {
		StringBuilder sb = new StringBuilder();
		List par = new ArrayList();
		//sb.append("SELECT t.trId, t.taskId, t.isuseable, t.ip, t.trMinRespTime, t.trMaxRespTime, t.trType, t.staDate, t.mpId, t.times, t.trRespTime, t.ext1, t.ext2, t.ext3, t.ext4 FROM " + tblName + " t WHERE t.dataTime=? AND t.taskId=? AND t.mpId=? AND t.isuseable=? AND t.trType=?");
		sb.append(" FROM " + tblName + " t WHERE t.dataTime=? AND t.taskId=? AND t.mpId=? AND t.isuseable=? AND t.trType=?");
		par.add(dateTime);
		par.add(taskId);
		par.add(mapId);
		par.add(isuseable);
		par.add(trType);
		return getQueryList(sb.toString(), par.toArray());
	}
	
	
	@Override
	public List<Object[]> getCurrentMonitorStatus(String taskId) {
		String str = "select t.staDate,m.mpName,t.trRespTime, t.isuseable, t.trOtherResult from " + tblName + " t, MonitoringPoint m where t.mpId = m.mpId and t.trId in(select max(tt.trId) from TaskResult tt  where tt.taskId = ? group by tt.monitoringPoint)";
		return getQueryList(str, taskId);
	}
	@Override
	public Object[] getOnlyResponseTimeByCondition(QueryCondition condition) {
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
		sb.append("Select min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");	
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		List<Object[]> list = getQueryList(sb.toString(),par.toArray());
		if(list != null && list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	@Override
	public List<Object[]> getHourResponseTimeByCondition(
			QueryCondition condition) {
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
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("Select to_char(t.staDate,'HH24') ,min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");
		}else{
			sb.append("Select to_char(t.staDate,'HH24') ,min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("group by t.mpId,to_char(t.staDate,'HH24')");
		}else{
			sb.append("group by to_char(t.staDate,'HH24')");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getHourResponseDetailByCondition(
			QueryCondition condition) {
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
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("Select to_char(t.staDate,'HH24'),round(avg(t.ext1),3),round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3) From ");
		}else{
			sb.append("Select to_char(t.staDate,'HH24'),round(avg(t.ext1),3),round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("group by t.mpId,to_char(t.staDate,'HH24')");
		}else{
			sb.append("group by to_char(t.staDate,'HH24')");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getHourAvailableByCondition(QueryCondition condition) {
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
		sb.append("Select t.isuseable,count(t.trId),to_char(t.staDate,'HH24') From ").append(tblName).append(" t where  1=1 ");
//		sb.append(" and t1.mpId = t.mpId");
		//		select t.mp_id,t1.mp_name,t.isuseable,count(t.isuseable) from task_result t,monitoring_point t1 where t1.mp_id = t.mp_id 
//		and t.task_id = '00000001'  ;
		if(startDate!=null){
			sb.append(" and t.staDate>= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate<= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		
		sb.append("  group by t.isuseable,to_char(t.staDate,'HH24')");
		
		return  getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getMinuteAvailableByCondition(QueryCondition condition) {
		if(condition==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
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
		sb.append("Select t.isuseable,count(t.trId),to_char(t.staDate,'HH24:mi') From ").append(tblName).append(" t where  1=1 ");
		if(startDate!=null){
			sb.append(" and t.staDate>= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate<= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		
		sb.append("  group by t.isuseable,to_char(t.staDate,'HH24:mi') order by to_char(t.staDate,'HH24:mi')");
		
		return  getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getMinuteResponseTimeByCondition(
			QueryCondition condition) {
		if(condition==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		List par = new ArrayList();
		Date startDate = condition.getStartTime();
		Date endDate = condition.getEndTime();
		String taskId = condition.getTaskId();
		String mpId = condition.getMpId();
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("Select to_char(t.staDate,'HH24:mi') ,min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");
		}else{
			sb.append("Select to_char(t.staDate,'HH24:mi') ,min(t.trMinRespTime),max(t.trMaxRespTime),round(avg(t.trRespTime),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate> ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate< ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=?");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("group by t.mpId,to_char(t.staDate,'HH24:mi') order by to_char(t.staDate,'HH24:mi')");
		}else{
			sb.append("group by to_char(t.staDate,'HH24:mi') order by to_char(t.staDate,'HH24:mi')");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<Object[]> getMinuteResponseDetailByCondition(
			QueryCondition condition) {
		if(condition==null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		List par = new ArrayList();
		Date startDate = condition.getStartTime();
		Date endDate = condition.getEndTime();
		String taskId = condition.getTaskId();
		String mpId = condition.getMpId();
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append("Select to_char(t.staDate,'HH24:mi'),round(avg(t.ext1),3),round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3) From ");
		}else{
			sb.append("Select to_char(t.staDate,'HH24:mi'),round(avg(t.ext1),3),round(avg(t.ext2),3),round(avg(t.ext3),3),round(avg(t.ext4),3) From ");	
		}
		
		sb.append(tblName).append(" t where  1=1 and t.isuseable=0");
		
		if(startDate!=null){
			sb.append(" and t.staDate >= ?");
			par.add(startDate);
		}
		if(endDate!=null){
			sb.append(" and t.staDate <= ?");
			par.add(endDate);
		}
		if(Utils.notEmpty(taskId)){
			sb.append(" and t.taskId=? ");
			par.add(taskId);
		}
		if(Utils.notEmpty(mpId)){
			sb.append(" and t.mpId=?");
			par.add(mpId);
		}
		if(!Utils.isEmpty(condition.getMpId())){
			sb.append(" group by t.mpId,to_char(t.staDate,'HH24:mi') order by to_char(t.staDate,'HH24:mi')");
		}else{
			sb.append(" group by to_char(t.staDate,'HH24:mi') order by to_char(t.staDate,'HH24:mi')");
		}
		
		return getQueryList(sb.toString(), par.toArray());
	}
	@Override
	public List<TaskResult> getLatestByProjectId(String projectId) {
		return getQueryList("from TaskResult tt where tt.trId in (select max(t.trId) from TaskResult t where t.taskId in (select s.id from Subproject s where s.projectId = ? and s.isuable = 1) group by t.taskId, t.monitoringPoint)", projectId);
	}
}
