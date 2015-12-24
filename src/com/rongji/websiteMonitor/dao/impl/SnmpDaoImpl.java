package com.rongji.websiteMonitor.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.rongji.dfish.base.Page;
import com.rongji.dfish.base.Utils;
import com.rongji.dfish.dao.impl.PubCommonDAOImpl;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.websiteMonitor.dao.SnmpDao;
import com.rongji.websiteMonitor.persistence.SnmpModel;

public class SnmpDaoImpl extends PubCommonDAOImpl implements SnmpDao {
	private static final String tblName = "SnmpModel";
	private static final String idName = "id";
	private static final String initId = "00000000001";
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	public void insertSnmp(SnmpModel snmpModel) {

		if (snmpModel != null) {
			String id = FrameworkHelper.getNewId(tblName, idName, initId);
			snmpModel.setId(id);

			this.saveObject(snmpModel);
		}
	}

	@Override
	public void updateSnmp(SnmpModel snmpModel) {

	}

	@Override
	public SnmpModel getSnmpById(String id) {
		return null;
	}

	@Override
	public SnmpModel getLatestSnmp(String taskId) {
		List<SnmpModel> list = this.getQueryList("from " + tblName
				+ " s where s.id = (select max(id) from "
				+ tblName + " ss where ss.taskId = ?)", taskId);
		if (!Utils.isEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<Object[]> getHourStatistics(String taskId, Date startDate,
			Date endDate) {
		StringBuffer sb = new StringBuffer();
		List<Object> par = new ArrayList<Object>();
		sb.append(
				"select round(avg(t.cpuUsedRate),2),round(avg(t.memoryUsedSize)/1024,2),round(avg(t.jvmHeapUsedSize)/1024/1024,2),round(avg(t.jvmTheadSize),0),round(avg(t.ioUsedSize)/1024,2),round(avg(t.memoryTotalSize)/1024,2),round(avg(t.jvmHeadTotalSize)/1024/1024,2),to_char(t.createTime,'HH24'),round(avg(t.ifInSize)/1024,2),round(avg(t.ifOutSize)/1024,2),round(avg(t.diskIOReadSize)/1024,2),round(avg(t.diskIOWrittenSize)/1024,2),round(avg(t.systemProcess),0),round(avg(t.storageSize),0),round(avg(t.storageUsed),0) from ")
				.append(tblName).append(" t where 1 = 1");
		if (!Utils.isEmpty(taskId)) {
			sb.append(" and t.taskId = ?");
			par.add(taskId);
		}
		try {
			if (startDate != null) {
				startDate = sdf2.parse(sdf.format(startDate) + " 00:00:00");
				sb.append(" and t.createTime >= ?");
				par.add(startDate);
			}
			if (endDate != null) {
				endDate = sdf2.parse(sdf.format(endDate) + " 23:59:59");
				sb.append(" and t.createTime <= ?");
				par.add(endDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sb.append(" group by to_char(t.createTime,'HH24')");
		return this.getQueryList(sb.toString(), par.toArray());
	}

	@Override
	public List<Object[]> getDayStatistics(String taskId, Date startDate,
			Date endDate) {
		try {
			StringBuffer sb = new StringBuffer();
			List<Object> par = new ArrayList<Object>();
			sb.append(
					"select round(avg(t.cpuUsedRate),2),round(avg(t.memoryUsedSize)/1024,2),round(avg(t.jvmHeapUsedSize)/1024/1024,2),round(avg(t.jvmTheadSize),0),round(avg(t.ioUsedSize)/1024,2),round(avg(t.memoryTotalSize)/1024,2),round(avg(t.jvmHeadTotalSize)/1024/1024,2),to_char(t.createTime,'yyyymmdd'),round(avg(t.ifInSize)/1024,2),round(avg(t.ifOutSize)/1024,2),round(avg(t.diskIOReadSize)/1024,2),round(avg(t.diskIOWrittenSize)/1024,2),round(avg(t.systemProcess),0),round(avg(t.storageSize),0),round(avg(t.storageUsed),0) from ")
					.append(tblName).append(" t where 1 = 1");
			if (!Utils.isEmpty(taskId)) {
				sb.append(" and t.taskId = ?");
				par.add(taskId);
			}
			if (startDate != null) {
				startDate = sdf2.parse(sdf.format(startDate) + " 00:00:00");
				sb.append(" and t.createTime >= ?");
				par.add(startDate);
			}
			if (endDate != null) {
				endDate = sdf2.parse(sdf.format(endDate) + " 23:59:59");
				sb.append(" and t.createTime <= ?");
				par.add(endDate);
			}
			sb.append(" group by to_char(t.createTime,'yyyymmdd')");
			return this.getQueryList(sb.toString(), par.toArray());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Object[]> getMimuteStatistics(String taskId, Date startDate,
			Date endDate) {
		StringBuffer sb = new StringBuffer();
		List<Object> par = new ArrayList<Object>();
		sb.append(
				"select round(avg(t.cpuUsedRate),2),round(avg(t.memoryUsedSize)/1024,2),round(avg(t.jvmHeapUsedSize)/1024/1024,2),round(avg(t.jvmTheadSize),0),round(avg(t.ioUsedSize)/1024,2),round(avg(t.memoryTotalSize)/1024,2),round(avg(t.jvmHeadTotalSize)/1024/1024,2),to_char(t.createTime,'hh24:mi'),round(avg(t.ifInSize)/1024,2),round(avg(t.ifOutSize)/1024,2),round(avg(t.diskIOReadSize)/1024,2),round(avg(t.diskIOWrittenSize)/1024,2),round(avg(t.systemProcess),0),round(avg(t.storageSize),0),round(avg(t.storageUsed),0) from ")
				.append(tblName).append(" t where 1 = 1");
		if (!Utils.isEmpty(taskId)) {
			sb.append(" and t.taskId = ?");
			par.add(taskId);
		}
		if (startDate != null) {
			sb.append(" and t.createTime >= ?");
			par.add(startDate);
		}
		if (endDate != null) {
			sb.append(" and t.createTime <= ?");
			par.add(endDate);
		}
		sb.append(" group by to_char(t.createTime,'hh24:mi') order by to_char(t.createTime,'hh24:mi')");
		return this.getQueryList(sb.toString(), par.toArray());
	}

}
