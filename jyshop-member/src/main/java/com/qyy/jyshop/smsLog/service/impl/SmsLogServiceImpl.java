package com.qyy.jyshop.smsLog.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.conf.SmsLogTypeConstants;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.member.service.MemberService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.SmsLog;
import com.qyy.jyshop.smsLog.service.SmsLogService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.SMSUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.withdrawal.service.MemberWithdrawInfoService;
@Service
public class SmsLogServiceImpl extends AbstratService<SmsLog> implements SmsLogService {
	@Autowired
	private MemberService memberServiceImpl;
	@Autowired
	private MemberWithdrawInfoService memberWithdrawInfoServiceImpl;
	
	@Override
	public Map<String, Object> sendSmsCode(String token,String mobile, Integer type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isBlank(mobile)) {
			map.put("error", "手机号不能为空");
			return map;
		}
		// 注册
		/*if (type.equals(SmsLogTypeConstants.REGISTER)) {
			this.sendRegisterCode(mobile,token);
		} else if (type.equals(SmsLogTypeConstants.EDIT_PWD)) {
			this.sendEditPwdCode(mobile);
		} else*/if (type.equals(SmsLogTypeConstants.ANY_MOBILE)) {	//任意可用手机号
			try {
			    System.out.println("发送忘记登录密码验证码:"+mobile+":"+token);
				this.sendAnyMobileCode(mobile,token);
				map.put("success", "发送成功");
			} catch (Exception e) {
				map.put("error", e.getMessage());
			}
		}if (type.equals(SmsLogTypeConstants.FORGET_PWD)) {	//忘记登录密码
			try {
			    System.out.println("发送忘记登录密码验证码:"+mobile+":"+token);
				this.sendForgetPwdCode(mobile,token);
				map.put("success", "发送成功");
			} catch (Exception e) {
				map.put("error", e.getMessage());
			}
		}else if(type.equals(SmsLogTypeConstants.OLD_MOBILE)){	//校验原手机号
			try {
				this.sendOldmobileCode(mobile,token);
				map.put("success", "发送成功");
			} catch (Exception e) {
				map.put("error", e.getMessage());
			}
		} else if(type.equals(SmsLogTypeConstants.NEW_MOBILE)){	//绑定手机
			try {
				this.sendNewmobileCode(mobile,token);
				map.put("success", "发送成功");
			} catch (Exception e) {
				map.put("error", e.getMessage());
			}
		}
		//map.put("success", "发送成功");
		return map;
	}
	

	/**
	 * 忘记登录密码
	 * @author Tonny
	 * @date 2018年3月30日
	 */
	private void sendForgetPwdCode(String mobile, String token)throws Exception {
			// 获取短信验证码。
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(random.nextInt(9));
			}
			String code = sb.toString();
			SMSUtil.postSendMsg(mobile, code+",请勿泄露。","SMS_53205046");
			System.out.println("redis_key:"+"FORGET_PWD_"+mobile);
			redisDao.saveObject("FORGET_PWD_"+mobile, code, 120);
			System.out.println("发送的手机验证码是：" + code);
		
	}
	/**
	 * 任意可用手机号发送
	 * @throws Exception 
	 * @throws  
	 */
	private void sendAnyMobileCode(String mobile, String token) throws Exception {
		// 获取短信验证码。
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(9));
		}
		String code = sb.toString();
		SMSUtil.postSendMsg(mobile, code+",请勿泄露。","SMS_53205046");
		System.out.println(mobile);
		redisDao.saveObject("ANY_MOBILE_"+mobile, code, 120);
		System.out.println("发送的手机验证码是：" + code);
		
	}
	/**
	 * 原手机号发验证码
	 * @author Tonny
	 * @date 2018年3月10日
	 */
	private void sendOldmobileCode(String mobile,String token) throws Exception {
			// 获取短信验证码。
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(random.nextInt(9));
			}
			String code = sb.toString();
			SMSUtil.postSendMsg(mobile, code+",请勿泄露。","SMS_53205046");
			System.out.println(mobile);
			redisDao.saveObject("OLD_MOBILE_"+mobile, code, 120);
			System.out.println("发送的手机验证码是：" + code);
		}
	/**
	 * 绑定手机号
	 * @author Tonny
	 * @date 2018年3月10日
	 */
	private void sendNewmobileCode(String mobile,String token) throws Exception {
			// 获取短信验证码。
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(random.nextInt(9));
			}
			String code = sb.toString();
			SMSUtil.postSendMsg(mobile, code+",请勿泄露。","SMS_53205046");
			System.out.println("发送的手机号："+mobile);
			redisDao.saveObject("NEW_MOBILE_" + mobile, code, 120);
			System.out.println("发送的手机验证码是：" + code);
	}

	@Override
	public Boolean checkCode(String token, String mobile,String code,Integer type) {
	    
	    if(StringUtil.isEmpty(mobile))
	        throw new AppErrorException("手机号不能为空...");
	    Member member = this.getMember(token);
	    
		if(type.equals(SmsLogTypeConstants.NEW_MOBILE)){		//绑定新手机
			Object redisCode=this.redisDao.getObject("NEW_MOBILE_"+mobile);
			if(StringUtil.isEmpty(redisCode))
				throw new AppErrorException("验证码己失效...");
		    System.out.println("redisCode:"+redisCode+"code:"+code+",NEW_MOBILE_"+mobile);
		    if(code.equals(redisCode.toString())){
		    	//查看手机号是否被占用
		        if(!memberServiceImpl.updateMemberMobile(token,mobile)){	
		            return true;
		        }else{
		            throw new AppErrorException("手机号已经被占用...");
		        }
            }else{
                throw new AppErrorException("验证码错误...");
            }
		    
		}else if(type.equals(SmsLogTypeConstants.OLD_MOBILE)){		//验证旧手机
			if(mobile.equals(member.getMobile())){
				Object redisCode=this.redisDao.getObject("OLD_MOBILE_"+mobile);
				if(StringUtil.isEmpty(redisCode))
					throw new AppErrorException("验证码己失效...");
			    System.out.println("redisCode:"+redisCode+"code:"+code+",OLD_MOBILE_"+mobile);
			    if(code.equals(redisCode.toString())){
		    		return true;
		    	}else{
		    	    throw new AppErrorException("验证码错误...");
		    	}
			}else{
				throw new AppErrorException("手机号与原绑定手机不一致");
			}
		    
		}else if(type.equals(SmsLogTypeConstants.FORGET_PWD)){			//忘记登录密码
		    
		    System.out.println("验证忘记登录密码验证码:"+"FORGET_PWD_"+mobile);
			Object redisCode=this.redisDao.getObject("FORGET_PWD_"+mobile);
			if(StringUtil.isEmpty(redisCode))
				throw new AppErrorException("验证码己失效...");
			
		    System.out.println("redisCode:"+redisCode+"code:"+code+",FORGET_PWD_"+mobile);
		    if(code.equals(redisCode.toString())){
		    	//判断登录账号、绑定手机号是否为验证的手机号
		    	if(!member.getUname().equals(mobile) && !member.getMobile().equals(mobile)){			
		    		throw new AppErrorException("输入的手机号与当前账号绑定手机号不一致");
		    	}
	    		return true;
	    	}else{
	    	    throw new AppErrorException("验证码错误...");
	    	}
		}/*else if(type.equals(SmsLogTypeConstants.ANY_MOBILE)){			//验证任意可用手机
			System.out.println("验证忘记登录密码验证码:"+"ANY_MOBILE_"+mobile);
			Object redisCode=this.redisDao.getObject("ANY_MOBILE_"+mobile);
			if(StringUtil.isEmpty(redisCode))
				throw new AppErrorException("验证码己失效...");
		    System.out.println("redisCode:"+redisCode+"code:"+code+",ANY_MOBILE_"+mobile);
		    if(code.equals(redisCode.toString())){
		    	try {
		    		memberWithdrawInfoServiceImpl.insertMemberWithdrawInfo(token,apistore_GET);
		    		return true;
				} catch (Exception e) {
					throw new AppErrorException(e.getMessage());
				}
	    	}else{
	    	    throw new AppErrorException("验证码错误...");
	    	}
		}*/else{
		    throw new AppErrorException("类型错误...");
		}
	}


	@Override
	public boolean checkCodeBankCard(String token, String mobile, String code, Integer type, String json,String path) {
		if(StringUtil.isEmpty(mobile))
	        throw new AppErrorException("手机号不能为空...");
	    Member member = this.getMember(token);
	    System.out.println("token====="+token);
	    if(member==null)
	    	throw new AppErrorException("用户不存在AAAAAAAAAAA");
		if(type.equals(SmsLogTypeConstants.ANY_MOBILE)){			//验证任意可用手机
		System.out.println("member的验证码:"+"ANY_MOBILE_"+mobile);
		Object redisCode=this.redisDao.getObject("ANY_MOBILE_"+mobile);
		if(StringUtil.isEmpty(redisCode))
			throw new AppErrorException("验证码己失效...");
	    System.out.println("redisCode:"+redisCode+"code:"+code+",ANY_MOBILE_"+mobile);
	    if(code.equals(redisCode.toString())){
	    	memberWithdrawInfoServiceImpl.insertMemberWithdrawInfo(json,member.getMemberId(),path);
	    	return true;
	    	/*try {
	    		memberWithdrawInfoServiceImpl.insertMemberWithdrawInfo(token,json);
	    		return true;
			} catch (Exception e) {
				throw new AppErrorException(e.getMessage());
			}*/
    	}else{
    	    throw new AppErrorException("验证码错误...");
    	}
	}
		return false;
	}
	
	
}
