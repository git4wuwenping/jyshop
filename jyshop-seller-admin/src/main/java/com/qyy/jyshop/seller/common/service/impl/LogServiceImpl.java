package com.qyy.jyshop.seller.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.seller.common.service.LogService;
import com.qyy.jyshop.dao.LogDao;
import com.qyy.jyshop.model.Log;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;



@Service
public class LogServiceImpl extends AbstratService<Log> implements LogService{

	@Autowired
	private LogDao logDao;
	
	@Override
	public PageAjax<Log> pageLog(PageAjax<Log> page, Log log) {
		return this.queryPage(page, log);
	}
	
	@Override
	public void doAddLog(Log log){
		this.logDao.insert(log);
	}

	
}
