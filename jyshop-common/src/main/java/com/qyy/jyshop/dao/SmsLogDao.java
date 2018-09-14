package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.SmsLog;
import com.qyy.jyshop.supple.MyMapper;

public interface SmsLogDao extends MyMapper<SmsLog> {

	SmsLog checkCode(@Param("mobile")String mobile, @Param("code")String code);
	
}