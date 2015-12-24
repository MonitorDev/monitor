package com.rongji.websiteMonitor.dao;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.webapp.visit.help.SubQueryCondition;

public interface SubprojectDao {

	/**
	 * 插入
	 * @param subproject
	 */
	public void insertSubproject(Subproject subproject);
	/**
	 * 删除
	 * @param subproject
	 */
	public void deleteSubproject(Subproject subproject);
	/**
	 * 删除多个
	 * @param listSubproject
	 */
	public void deleteSubprojects(List<Subproject> listSubproject);
	/**
	 * 更新
	 * @param subproject
	 */
	public void updateSubproject(Subproject subproject);
	/**
	 * 根据ID获取
	 * @param id
	 * @return
	 */
	public Subproject getSubprojectById(String id);
	/**
	 * 更加项目ID获取
	 * @param projectId
	 * @return
	 */
	public List<Subproject> finSubprojectByProjectId(String projectId);
	/**
	 * 分页查询
	 * @param page
	 * @return
	 */
	public List<Subproject> findSubprojectByPage(Page page);
	/**
	 * 根据项目Id和子项目名称查询
	 * @param name 子项目名称
	 * @param projectId  项目ID
	 * @return
	 */
	public List<Subproject> findSubprojectByName(String name, String projectId);
	
	/**
	 * 获取开启的子项目
	 * @return
	 */
	public List<Subproject> findSubprojectByUsable();
	
	/**
	 * 获取无项目的项目服务
	 * @return
	 */
	public List<Subproject> findSubprojectByNullProject();
	
	/**
	 * 
	 * @param projectId
	 * @param isExternal
	 * @return
	 */
	public List<Subproject> findSubprojectByIsExternal(String projectId, String isExternal);

	/**
	 * 根据条件查询
	 * @param condition
	 * @return
	 */
	public List<Subproject> findSubprojectByCondition(SubQueryCondition condition);
}
