package com.rongji.websiteMonitor.dao.impl;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.dao.ProjectDao;
import com.rongji.websiteMonitor.persistence.Project;

public class ProjectDaoImpl extends PubCommonDAOImpl implements ProjectDao {

	private static final String tblName = "Project";
	private static final String idName = "id";
	private static final String initId = "00000000001";
	@Override
	public void insertProject(Project project) {
		if(project == null) {
			return;
		}
		String id = FrameworkHelper.getNewId(tblName, idName, initId);
		project.setId(id);
		this.saveObject(project);
	}

	@Override
	public void deleteProject(Project project) {
		this.delete(project);
	}

	@Override
	public void deleteProjects(List<Project> listProject) {
		this.getHibernateTemplate().deleteAll(listProject);
	}

	@Override
	public void updateProject(Project project) {
		this.updateObject(project);
	}

	@Override
	public Project getProjectById(String id) {
		return (Project) this.getHibernateTemplate().get(Project.class, id);
	}

	@Override
	public List<Project> findAllProject() {
		return this.getQueryList(" from Project p where p.isusable=1 order by p.createTime desc");
	}

	@Override
	public List<Project> findProjectByName(String name) {
		return this.getQueryList("from Project p where p.name=?", name);
	}

	@Override
	public List<Project> findProjectByPage(Page page) {
		return this.getQueryList("from Project p order by p.createTime desc", page);
	}

}
