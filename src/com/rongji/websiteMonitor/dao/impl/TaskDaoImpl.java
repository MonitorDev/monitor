package com.rongji.websiteMonitor.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;

import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.TaskDao;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class TaskDaoImpl extends PubCommonDAOImpl implements TaskDao  {
	private static final Logger logger = Logger.getLogger(TaskDaoImpl.class);
	private static final String tblName = "Task";
	private static final String idName = "taskId";
	private static final String initId = "00000001";
	@Override
	public List<Task> findTasksByPage(Page page,
			boolean ignoreSearchConditions) {
		List <Task>list = null;
		if (ignoreSearchConditions) {
			list = getQueryList("SELECT t FROM " + tblName + " t ORDER BY t.taskId desc  ", page, true);
		} else {
			list = getQueryList("SELECT  FROM " + tblName + " t ORDER BY t.taskId desc  ", page, false);
		}
		return list;
	}
	@Override
	public Task getTask(String taskId) {		
		String hql = "SELECT t FROM " + tblName + " t where t.taskId = ?  ";
		List<Task> list = getQueryList(hql, taskId);
		
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<Task> findTasks(String taskName) {
		String hql = "SELECT t FROM " + tblName + " t where t.taskName = ?  ";
		return getQueryList(hql, taskName);
	}
	@Override
	public void saveTask(Task task) {
		if (task == null)
			return;
		
		if (task.getCreateTime() == null) {
			task.setCreateTime(new Date());
		}
		
		if (Utils.isEmpty(task.getTaskId())) {
			task.setTaskId(FrameworkHelper.getNewId(tblName, idName, initId));
		}
		saveObject(task);
		
		
	}
	@Override
	public void updateTask(Task task) {
		if (task != null) {
			updateObject(task);
		}
		
	}
	@Override
	public List<Task> findTaskByContainer(
			SearchContainer container, Page page) {
		String hql = "From "+tblName+" where 1=1 and ";
		hql = hql+container.toHqlWitOutWhere();
		if(page!=null){
			return getQueryList(hql,page,true, container.getArgs());
		}else{
			return getQueryList(hql, container.getArgs());
		}
		
	}
	@Override
	public boolean delete(Task task) throws Exception {
		if(task!=null){
			super.delete(task);
		}
		return true;
	}
	@Override
	public Task getTaskBySchlId(String schiId) {
		if(Utils.isEmpty(schiId)){
			return null;
		}
		String hql = "From "+tblName+" t where 1=1 and t.schlId = ?";
		List<Task> list = getQueryList(hql, schiId);
		if(Utils.notEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<Task> getTaskByMtAlias(Page page, String mtAlias) {
		String hql = "From "+tblName+" t where 1=1";
		List<Object> par = new ArrayList<Object>();
		if(Utils.notEmpty(mtAlias)){
			hql = hql+"and t.taskType = ? order by t.taskId desc";
			par.add(mtAlias);
		}else{
			hql = hql +" order by t.taskId desc";
		}
		
		List<Task> list = null;
		if(page!=null){
			list = getQueryList(hql, page, true,par.toArray());
		}else{
			list = getQueryList(hql, par.toArray());
		}
		return list;
	}
	@Override
	public List<Task> getTaskByCondition(Page page, QueryCondition condition) {
		String hql = "From "+tblName+" t where 1=1";
		List<Object> par = new ArrayList<Object>();
		String mtAlias = "";
		String summary = "";
		String taskName = "";
		String taskId = "";
		if(condition!=null){
			mtAlias = condition.getMtAlias();
			summary = condition.getSummary();
			taskName = condition.getTaskName();
			taskId = condition.getTaskId();
		}
		if(Utils.notEmpty(mtAlias)){
			hql = hql+" and t.taskType = ? ";
			par.add(mtAlias);
		}
		if(Utils.notEmpty(summary)){
			hql = hql +" and t.taskSummary = ?";
			par.add(summary);
			
		}
		if(Utils.notEmpty(taskName)){
			hql = hql+" and t.taskName like ?";
			par.add("%"+taskName+"%");
		}
		if(Utils.notEmpty(taskId)){
			String [] taskIds = taskId.split(",");
			String str = "";
			if(taskIds!=null&&taskIds.length>0){
				for(String taskTemp:taskIds){
					if(Utils.notEmpty(str)){
						str = str + "?,";
						par.add(taskTemp);
					}else{
						str = " and t.taskId in(?,";
						par.add(taskTemp);
					}
				}
				str = str.substring(0, str.length()-1);
				str = str +")";
				hql = hql +str;
			}
		}
		hql = hql+" order by t.taskId asc";
		List<Task> list = null;
		if(page!=null){
			list = getQueryList(hql, page, true,par.toArray());
		}else{
			list = getQueryList(hql, par.toArray());
		}
		return list;
	}


}
