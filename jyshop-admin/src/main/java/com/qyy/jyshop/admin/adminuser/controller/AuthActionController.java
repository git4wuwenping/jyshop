package com.qyy.jyshop.admin.adminuser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.adminuser.service.AuthActionService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.model.AuthAction;
import com.qyy.jyshop.pojo.PageAjax;



@Controller
@RequestMapping("/admin/operat")
public class AuthActionController extends BaseController{

	@Autowired  
	private AuthActionService authActionService;
	
	@Authority(opCode = "020103", opName = "权限管理界面")
	@RequestMapping("actionMain")
	public String actionMain(){
		return "adminuser/operat/action_main";
	}

	@ControllerLog("查询权限列表")
    @RequestMapping("pageActionAjax")
    @ResponseBody
    @Authority(opCode = "02010301", opName = "查询权限列表")
    public PageAjax<AuthAction> pageActionAjax(PageAjax<AuthAction> page, AuthAction authAction) {
        return authActionService.pageAuthAction(page, authAction);
    }
	
}
