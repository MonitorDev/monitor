package com.rongji.websiteMonitor.dao;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public interface TaskDao extends PubCommonDAO {

	public List<Task> findTasksByPage(Page page,
			boolean ignoreSearchConditions);

	public Task getTask(String taskId);

	public List<Task> findTasks(String taskName);

	public void saveTask(Task task);

	public void updateTask(Task task);

	public List<Task> findTaskByContainer(
			SearchContainer container, Page page);
	
	public boolean delete(Task task)throws Exception;

	public Task getTaskBySchlId(String schiId);

	public List<Task> getTaskByMtAlias(Page page, String mtAlias);

	public List<Task> getTaskByCondition(Page page, QueryCondition condition);

}
