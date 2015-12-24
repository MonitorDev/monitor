package com.rongji.websiteMonitor.service;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.persistence.Project;

public interface ProjectService {

	/**
	 * 插入
	 * @param project
	 */
	public void insertProject(Project project);
	/**
	 * 删除
	 * @param project
	 */
	public void deleteProject(Project project);
	/**
	 * 删除多个
	 * @param listProject
	 */
	public void deleteProjects(List<Project> listProject);
	/**
	 * 更新
	 * @param project
	 */
	public void updateProject(Project project);
	/**
	 * 根据ID获取
	 * @param id
	 * @return
	 */
	public Project getProjectById(String id);
	/**
	 * 获取全部
	 * @return
	 */
	public List<Project> findAllProject();
	
	/**
	 * 根据项目名获取
	 * @param name
	 * @return
	 */
	public List<Project> findProjectByName(String name);
	
	
	/**
	 * 分页查找
	 * @param page
	 * @return
	 */
	public List<Project> findProjectByPage(Page page);
	
	/**
	 * 根据Id删除
	 * @param ids
	 * @return
	 */
	public boolean deleteTypeByIds(String[] ids);
}
