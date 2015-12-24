package com.rongji.websiteMonitor.service;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.persistence.Task;
import com.rongji.websiteMonitor.webapp.visit.help.QueryCondition;

public interface TaskService {
	/**
	 * 返回所有作者列表(支持带分页列表)
	 * 
	 * @param page
	 *            分页对象
	 * @param ignoreSearchConditions
	 *            如果为true，表示忽略查询条件，按初始条件查询。
	 * @return
	 * @throws Exception
	 */
	public List<Task> getTasksByPage(Page page, boolean ignoreSearchConditions);

	public Task getTask(String taskId);

	public List<Task> findTasks(String taskName);

	public Task saveTask(Task Task);

	public Task updateTask(Task Task);

	public List<Task> findTaskByContainer(
			SearchContainer container, Page page);

	public boolean deleteTaskByIds(String[] taskIds)throws Exception;

	/**
	 * 通过定时任务ID获取task
	 * @param schiId
	 * @return
	 */
	public Task getTaskBySchlId(String schiId);

	/**
	 * 根据监控类别取监控项目
	 * @param mtAlias
	 * @return
	 */
	public List<Task> getTaskByMtAlias(Page page,String mtAlias);

	public List<Task> getTaskByCondition(Page page, QueryCondition condition);

}
