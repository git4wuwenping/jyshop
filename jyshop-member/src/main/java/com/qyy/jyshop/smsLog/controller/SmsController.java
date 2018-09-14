package com.qyy.jyshop.smsLog.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.smsLog.service.SmsLogService;

import net.sf.json.JSONObject;
@RestController
public class SmsController extends AppBaseController{
	
	@Autowired
	private SmsLogService smsLogServiceImpl;
	/**
     * 发送手机短信验证
     * @author tonny
     * @created 2018年1月8日 上午9:50:50
     * @param mobile
	 * @throws Exception 
     */
	@RequestMapping(value = "mobileCheck")
    public Map<String,Object> mobileCheck(String token,String mobile,Integer type) throws Exception {
	    
		try {
		    this.smsLogServiceImpl.sendSmsCode(token,mobile,type);
		} catch (Exception e) {
			return this.outMessage(0, e.getMessage(), null);
		}
        return this.outMessage(0, "获取成功", null);
    }
	
	/**
	 * 检查手机验证码
	 */
	@RequestMapping(value = "checkCode")
	public Map<String,Object> checkCode(String token,String mobile,String code,Integer type){
	    if(smsLogServiceImpl.checkCode(token,mobile,code,type)){		//验证短信
			return this.outMessage(0, "验证成功", null);
		}else{
			return this.outMessage(1, "验证码有误请重新输入", null);
		}
	}
	
	/**
	 * 检查银行卡手机验证码
	 */
	//银行卡图片访问地址
	@Value("${upload.bankImagepath}")
	private String bankImagepath;
	//private String bankImagepath="D:/image/bankImage/";
	@RequestMapping(value = "checkCodeBankCard",method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String,Object> checkCodeBankCard(@RequestParam("json")String json,String token,String mobile,String code,Integer type){
		System.out.println("token====="+token);
		if(smsLogServiceImpl.checkCodeBankCard(token,mobile,code,type,json,bankImagepath)){		//验证短信
			return this.outMessage(0, "验证成功", null);
		}else{
			return this.outMessage(1, "验证码有误请重新输入", null);
		}
	}
	
}
