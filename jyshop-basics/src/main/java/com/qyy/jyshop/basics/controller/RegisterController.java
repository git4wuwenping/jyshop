package com.qyy.jyshop.basics.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.basics.service.RegisterService;
import com.qyy.jyshop.controller.AppBaseController;
/**
 * 注册
 * @author Tonny
 * @created 2018年3月5日 下午3:04:09
 */
@RestController
public class RegisterController  extends AppBaseController{
	
	@Autowired
	private RegisterService registerServiceImpl;
	
	@RequestMapping("/memberRegister")
	public Map<String,Object> memberRegister(String uname,String password){
		try {
			registerServiceImpl.memberRegister(uname,password);
			return this.outMessage(0, "注册成功...", null);
		} catch (Exception e) {
			Map<String,Object> map = new HashMap<>();
			map.put("message", e.getMessage());
			return this.outMessage(1, "注册失败请联系管理员...",map );
		}
	}
}
