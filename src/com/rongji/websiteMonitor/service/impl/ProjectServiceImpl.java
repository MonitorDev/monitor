package com.rongji.websiteMonitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.websiteMonitor.dao.ProjectDao;
import com.rongji.websiteMonitor.persistence.Project;
import com.rongji.websiteMonitor.service.ProjectService;

public class ProjectServiceImpl implements ProjectService {

	private ProjectDao projectDao;
	
	@Override
	public void insertProject(Project project) {
		projectDao.insertProject(project);
	}

	@Override
	public void deleteProject(Project project) {
		projectDao.deleteProject(project);
	}

	@Override
	public void deleteProjects(List<Project> listProject) {
		projectDao.deleteProjects(listProject);
	}

	@Override
	public void updateProject(Project project) {
		projectDao.updateProject(project);
	}

	@Override
	public Project getProjectById(String id) {
		return projectDao.getProjectById(id);
	}

	@Override
	public List<Project> findAllProject() {
		return projectDao.findAllProject();
	}

	@Override
	public List<Project> findProjectByName(String name) {
		return projectDao.findProjectByName(name);
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	@Override
	public List<Project> findProjectByPage(Page page) {
		return projectDao.findProjectByPage(page);
	}

	@Override
	public boolean deleteTypeByIds(String[] ids) {
		if(Utils.isEmpty(ids)) {
			return false;
		}
		List<Project> list = new ArrayList<Project>();
		for(String id : ids) {
			Project p = new Project();
			p.setId(id);
			list.add(p);
		}
		try {
			this.deleteProjects(list);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

}
