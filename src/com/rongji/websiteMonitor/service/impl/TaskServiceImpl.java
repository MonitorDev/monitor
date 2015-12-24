package com.rongji.websiteMonitor.service.impl;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.common.spl.SearchContainer.Op;
import com.rongji.websiteMonitor.common.util.Utils;
import com.rongji.websiteMonitor.dao.MonitoringPointDao;
import com.rongji.websiteMonitor.dao.TaskDao;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.service.MonitoringPointService;
import com.rongji.websiteMonitor.service.ServiceLocator;
import com.rongji.websiteMonitor.service.TaskService;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public class TaskServiceImpl implements TaskService {

	private TaskDao taskDao;
	
	
	
	public TaskDao getTaskDao() {
		return taskDao;
	}



	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}



	@Override
	public List<Task> getTasksByPage(Page page,
			boolean ignoreSearchConditions) {
		
		return this.taskDao.findTasksByPage(page,ignoreSearchConditions);
	}



	@Override
	public Task getTask(String taskId) {
		return taskDao.getTask(taskId);
	}



	@Override
	public List<Task> findTasks(String taskName) {
		
		return taskDao.findTasks(taskName);
	}



	@Override
	public Task saveTask(Task task) {
		taskDao.saveTask(task);
		return task;
	}



	@Override
	public Task updateTask(Task task) {
		taskDao.updateTask(task);
		return task;
	}



	@Override
	public List<Task> findTaskByContainer(
			SearchContainer container, Page page) {
		
		return taskDao.findTaskByContainer(container,page);
	}



	@Override
	public boolean deleteTaskByIds(String[] taskIds) throws Exception {
		if ((taskIds == null) || (taskIds.length == 0))
			return false;
		for (int i = 0; i < taskIds.length; i++) {
			deleteTaskById(taskIds[i]);
		}
		return true;
	}



	private void deleteTaskById(String taskId) throws Exception{
	
		if (Utils.isEmpty(taskId)) {
			return ;
		}
		Task task = this.taskDao.getTask(taskId);
		if (task != null) {
			FrameworkHelper.getDAO().deleteSQL("from PubScheduler t where t.schlId = ?", task.getSchlId());
			deleteTask(task);
		}
	
	}
	public boolean deleteTask(Task task) throws Exception {
		if (task == null)
			return false;
		return taskDao.delete(task);
	}



	@Override
	public Task getTaskBySchlId(String schiId) {
		if(Utils.notEmpty(schiId)){
			return taskDao.getTaskBySchlId(schiId);
		}
		return null;
	}



	@Override
	public List<Task> getTaskByMtAlias(Page page,String mtAlias) {

		return taskDao.getTaskByMtAlias(page,mtAlias);

	}



	@Override
	public List<Task> getTaskByCondition(Page page, QueryCondition condition) {

		return taskDao.getTaskByCondition(page,condition);
	}
}
