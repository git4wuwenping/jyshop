package com.qyy.jyshop.basics.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.basics.service.LoginService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.util.StringUtil;


/**
 * 登陆
 * @author hwc
 * @created 2017年11月23日 下午3:24:09
 */
@RestController
public class LoginController extends AppBaseController{

	@Autowired
	private LoginService loginService;
	
	/**
	 * 用户登陆
	 * @author hwc
	 * @created 2017年12月9日 下午3:18:54
	 * @param uname
	 * @param password
	 * @return
	 */
	@RequestMapping("/onLogin")
	public Map<String,Object> memberLogin(String uname,String password){

		Map<String,Object> memberMap=this.loginService.doMemberLogin(uname, password);
		if(memberMap!=null && !StringUtil.isEmpty(memberMap.get("token"))){
		    return this.outMessage(0, "登陆成功...", memberMap);
		}else{
		    return this.outMessage(1, "用户名或密码 ...", null);
		}	
	}
}
