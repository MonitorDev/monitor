package com.rongji.websiteMonitor.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.websiteMonitor.dao.SubprojectDao;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.service.SubprojectService;
import com.rongji.websiteMonitor.webapp.visit.help.SubQueryCondition;

public class SubprojectServiceImpl implements SubprojectService {

	private SubprojectDao subprojectDao;
	
	@Override
	public void insertSubproject(Subproject subproject) {
		subprojectDao.insertSubproject(subproject);
	}

	@Override
	public void deleteSubproject(Subproject subproject) {
		subprojectDao.deleteSubproject(subproject);
	}

	@Override
	public void deleteSubprojects(List<Subproject> listSubproject) {
		subprojectDao.deleteSubprojects(listSubproject);
	}

	@Override
	public void updateSubproject(Subproject subproject) {
		subprojectDao.updateSubproject(subproject);
	}

	@Override
	public Subproject getSubprojectById(String id) {
		return subprojectDao.getSubprojectById(id);
	}

	@Override
	public List<Subproject> finSubprojectByProjectId(String projectId) {
		return subprojectDao.finSubprojectByProjectId(projectId);
	}

	@Override
	public List<Subproject> findSubprojectByPage(Page page) {
		return subprojectDao.findSubprojectByPage(page);
	}

	@Override
	public List<Subproject> findSubprojectByName(String name, String projectId) {
		return subprojectDao.findSubprojectByName(name, projectId);
	}

	@Override
	public List<Subproject> findSubprojectByUsable() {
		return subprojectDao.findSubprojectByUsable();
	}

	public void setSubprojectDao(SubprojectDao subprojectDao) {
		this.subprojectDao = subprojectDao;
	}

	@Override
	public void deleteSubprojectByIds(String[] ids) {
		if(Utils.notEmpty(ids)) {
			List<Subproject> list = new ArrayList<Subproject>();
			for(String id : ids) {
				Subproject subproject = new Subproject();
				subproject.setId(id);
				list.add(subproject);
			}
			this.deleteSubprojects(list);
		}
	}

	@Override
	public List<Subproject> findSubprojectByNullProject() {
		return subprojectDao.findSubprojectByNullProject();
	}

	@Override
	public List<Subproject> findSubprojectByIsExternal(String projectId,
			String isExternal) {
		return subprojectDao.findSubprojectByIsExternal(projectId, isExternal);
	}

	@Override
	public List<Subproject> findSubprojectByCondition(
			SubQueryCondition condition) {
		return subprojectDao.findSubprojectByCondition(condition);
	}

	@Override
	public Map<String, Subproject> findSubprojectsByProjectId(String projectId) {
		List<Subproject> list = subprojectDao.finSubprojectByProjectId(projectId);
		Map<String, Subproject> map = new HashMap<String, Subproject>();
		if(Utils.notEmpty(list)) {
			for(Subproject sp : list) {
				map.put(sp.getId(), sp);
			}
		}
		return map;
	}

}
