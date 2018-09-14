package com.qyy.jyshop.seller.common.service;


import com.qyy.jyshop.model.Log;
import com.qyy.jyshop.pojo.PageAjax;

public interface LogService {

	/**
	 * 获取日志列表(分页_ajax)
	 * @param page
	 * @param log
	 * @return
	 */
	public PageAjax<Log> pageLog(PageAjax<Log> page, Log log);
	
	/**
	 * 添加操作日志
	 * @param log
	 */
	public void doAddLog(Log log);
}
