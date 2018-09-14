package com.qyy.jyshop.seller.common.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.service.LogService;
import com.qyy.jyshop.model.Log;
import com.qyy.jyshop.pojo.PageAjax;



/**
 * 系统日志
 */
@Controller
@RequestMapping("/admin/log")
public class LogController {

	@Autowired
	private LogService logService;
	
	@RequestMapping("logMain")
	@Authority(opCode = "020201", opName = "日志列表")
	public String logMain(Map<String, Object> map) {
		return "log/log_main";
	}
	
	@ResponseBody
	@RequestMapping("pageLogAjax")
	@Authority(opCode = "02020101", opName = "查询日志列表")
	public PageAjax<Log> pageLogAjax(PageAjax<Log> page, Log log) {
		return logService.pageLog(page, log);
	}
	
}
