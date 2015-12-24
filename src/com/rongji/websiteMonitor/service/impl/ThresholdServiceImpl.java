package com.rongji.websiteMonitor.service.impl;

import java.util.List;

import com.rongji.dfish.base.Page;
import com.rongji.websiteMonitor.dao.ThresholdDao;
import com.rongji.websiteMonitor.persistence.Threshold;
import com.rongji.websiteMonitor.service.ThresholdService;

public class ThresholdServiceImpl implements ThresholdService {

	private ThresholdDao thresholdDao;
	
	@Override
	public void saveThreshold(Threshold threshold) {
		thresholdDao.saveThreshold(threshold);
	}

	@Override
	public Threshold getThresholdById(String id) {
		return thresholdDao.getThresholdById(id);
	}

	@Override
	public List<Threshold> findAllThreshold(String type, boolean isIgnoreUsable) {
		return thresholdDao.findAllThreshold(type, isIgnoreUsable);
	}

	@Override
	public void updateThreshold(Threshold threshold) {
		thresholdDao.updateThreshold(threshold);
	}


	public void setThresholdDao(ThresholdDao thresholdDao) {
		this.thresholdDao = thresholdDao;
	}

	@Override
	public List<Threshold> findThresholdByName(String type, String name) {
		return thresholdDao.findThresholdByName(type, name);
	}

	@Override
	public void deleteThreshold(Threshold threshold) {
		thresholdDao.delete(threshold);
	}

	@Override
	public List<Threshold> findThresholdByPage(String type, Page page) {
		return thresholdDao.findThresholdByPage(type, page);
	}

	@Override
	public List<Threshold> findThresholdByPage(Page page) {
		return thresholdDao.findThresholdByPage(page);
	}

}
