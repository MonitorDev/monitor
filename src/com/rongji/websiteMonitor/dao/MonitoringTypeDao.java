package com.rongji.websiteMonitor.dao;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.dao.PubCommonDAO;
import com.rongji.websiteMonitor.common.spl.SearchContainer;
import com.rongji.websiteMonitor.persistence.MonitoringPoint;
import com.rongji.websiteMonitor.persistence.Monitortype;

public interface MonitoringTypeDao extends PubCommonDAO {

	public List<Monitortype> findMonitorTypesByPage(Page page,
			boolean ignoreSearchConditions);

	public Monitortype getMonitoringType(String mpId);

	public List<Monitortype> findMonitorTypes(String mpName);

	public void saveMonitorType(Monitortype Type);

	public void updateMonitorType(Monitortype Type);

	public List<Monitortype> findMonitorTypeByContainer(
			SearchContainer container, Page page);
	
	public boolean delete(Monitortype Type)throws Exception;

}
