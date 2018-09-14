package com.qyy.jyshop.seller.common.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.service.LoginService;
import com.qyy.jyshop.seller.statistics.service.StatisticsService;
import com.qyy.jyshop.pojo.AjaxResult;

/**
 * 登录Controller
 * @author hwc
 * @created 2017年11月20日 上午11:18:26
 */
@Controller
@RequestMapping("/admin/")
public class LoginController extends BaseController {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private StatisticsService statisticsServiceImpl;
	/**
	 * 登录
	 * @param response
	 * @param userName
	 * @param password
	 * @return
	 */
	@ControllerLog("登录")
	@RequestMapping("login")
	@ResponseBody
	public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
		return loginService.login(request, response);
	}

	/**
	 * 登录成功进入主界面
	 * @param map
	 * @return
	 */
	@Authority(opCode = "0001", opName = "系统主界面")
	@RequestMapping("main")
	public String main() {
		return "common/main";
	}
	

	/**
	 * 退出
	 * @author hwc
	 * @created 2017年11月20日 上午11:18:01
	 * @param response
	 * @param request
	 * @return
	 */
    @ControllerLog("退出")
    @RequestMapping("logout")
    @ResponseBody
    public AjaxResult logout(HttpServletResponse response, HttpServletRequest request) {
        return loginService.logout(response, request);
    }
    
    @ControllerLog("获取菜单")
    @RequestMapping("menuAjax")
    public String menuAjax(Integer opId) {
        return "common/menuAjax";
    }
    
    @ControllerLog("首页")
    @RequestMapping("homePage")
    public String homePage(Map<String, Object> map) {
    	List<Map<String,Object>> countList =statisticsServiceImpl.statisticsCount();
		for (Map<String, Object> map2 : countList) {
			String type = (String) map2.get("type");
			if(type==null || "".equals(type)){
				type="0";
			}
			if(!type.equals("yesterdayTradingVolumeCount")||!type.equals("yesterdayRechargeCount")){
				String count = map2.get("count2")+"";
				map.put(type, count.subSequence(0, count.indexOf(".")));
			}else{
				map.put(type,map2.get("count2"));
			}
		}
        return "common/content";
    }
    
}
