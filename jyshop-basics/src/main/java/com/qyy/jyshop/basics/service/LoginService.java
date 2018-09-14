package com.qyy.jyshop.basics.service;

import java.util.Map;

public interface LoginService {

	/**
	 * 用户登陆
	 * @author hwc
	 * @created 2017年12月9日 下午3:36:28
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Map<String,Object> doMemberLogin(String mobile,String password);
	
}
