package com.rongji.websiteMonitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.dao.SubprojectDao;
import com.rongji.websiteMonitor.persistence.Subproject;
import com.rongji.websiteMonitor.webapp.visit.help.SubQueryCondition;

public class SubprojectDaoImpl extends PubCommonDAOImpl implements SubprojectDao{
	private static final String tblName = "Subproject";
	private static final String idName = "id";
	private static final String initId = "00000000001";
	@Override
	public void insertSubproject(Subproject subproject) {
		if(subproject == null) {
			return;
		}
		String id = FrameworkHelper.getNewId(tblName, idName, initId);
		subproject.setId(id);
		this.saveObject(subproject);
	}

	@Override
	public void deleteSubproject(Subproject subproject) {
		this.delete(subproject);
	}

	@Override
	public void deleteSubprojects(List<Subproject> listSubproject) {
		this.getHibernateTemplate().deleteAll(listSubproject);
	}

	@Override
	public void updateSubproject(Subproject subproject) {
		this.updateObject(subproject);
	}

	@Override
	public Subproject getSubprojectById(String id) {
		return (Subproject) this.getHibernateTemplate().get(Subproject.class, id);
	}

	@Override
	public List<Subproject> finSubprojectByProjectId(String projectId) {
		return this.getQueryList(" from " + tblName + " s where s.projectId=?", projectId);
	}

	@Override
	public List<Subproject> findSubprojectByPage(Page page) {
		return this.getQueryList("from " + tblName + " s order by s.createTime desc", page);
	}

	@Override
	public List<Subproject> findSubprojectByName(String name, String projectId) {
		return this.getQueryList(" from " + tblName + " s where s.name = ? and s.projectId = ?", name,projectId);
	}

	@Override
	public List<Subproject> findSubprojectByUsable() {
		return this.getQueryList(" from " + tblName + " s where s.isuable = 1");
	}

	@Override
	public List<Subproject> findSubprojectByNullProject() {
		return this.getQueryList(" from " + tblName + " s where s.projectId is null");
	}

	@Override
	public List<Subproject> findSubprojectByIsExternal(String projectId, String isExternal) {
		return  this.getQueryList(" from " + tblName + " s where s.projectId = ? and s.isExternal = ?", projectId, isExternal);
	}

	@Override
	public List<Subproject> findSubprojectByCondition(
			SubQueryCondition condition) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ").append(tblName).append(" s where 1=1 ");
		List<Object> ops = new ArrayList<Object>();
		if(condition != null) {
			if(Utils.notEmpty(condition.getId())) {
				sb.append(" and s.id = ?");
				ops.add(condition.getId());
			}
			if(Utils.notEmpty(condition.getIsExternal())) {
				sb.append(" and s.isExternal=?");
				ops.add(condition.getIsExternal());
			}
			if(Utils.notEmpty(condition.getProjectId())) {
				sb.append(" and s.projectId =?");
				ops.add(condition.getProjectId());
			}
			if(Utils.notEmpty(condition.getType())) {
				sb.append(" and s.type = ?");
				ops.add(condition.getType());
			}
			if(Utils.notEmpty(condition.getIsusable())) {
				sb.append(" and s.isuable = ?");
				ops.add(condition.getIsusable());
			}
			
			if(Utils.notEmpty(condition.getType())) {
				sb.append(" order by s.isExternal desc, s.createTime desc");
			}
		}
		return this.getQueryList(sb.toString(), ops.toArray());
	}

}
