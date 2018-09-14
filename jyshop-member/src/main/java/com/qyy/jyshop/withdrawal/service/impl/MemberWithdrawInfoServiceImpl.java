package com.qyy.jyshop.withdrawal.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ctc.wstx.util.StringUtil;
import com.google.gson.Gson;
import com.qyy.jyshop.conf.SmsLogTypeConstants;
import com.qyy.jyshop.dao.PointParamDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.model.PointParam;
import com.qyy.jyshop.smsLog.service.SmsLogService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.BankCardCertificationUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.withdrawal.service.MemberWithdrawInfoService;

import net.sf.json.JSONObject;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class MemberWithdrawInfoServiceImpl extends AbstratService<MemberWithdrawInfo> implements MemberWithdrawInfoService {

	@Autowired
	private com.qyy.jyshop.dao.MemberWithdrawInfoDao MemberWithdrawInfoDao;
	@Autowired
	private SmsLogService smsLogServiceImpl;
	@Autowired
	private PointParamDao pointParamDao;
	@Override
	public Map<String, Object> getWithdrawalDetial(String token) {
		Map<String,Object> returnMap= new HashMap<>();
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		Example example = new Example(MemberWithdrawInfo.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId",member.getMemberId());
		List<MemberWithdrawInfo> WithdrawInfoList = MemberWithdrawInfoDao.selectByExample(example);
		Map<String,Object> alipayInfo = new HashMap<>();
		Map<String,Object> wechatInfo = new HashMap<>();
		List<MemberWithdrawInfo> bankcardList = new ArrayList<>();
		for(MemberWithdrawInfo m : WithdrawInfoList) {
			if(m.getDrawType() == 0) {//支付宝
				alipayInfo.put("accountName", m.getAccountName());
				alipayInfo.put("accountNo", m.getAccountNo());
				//alipayInfo.put("openBank", m.getOpenBank());
			}else if(m.getDrawType() == 1) {//微信
				wechatInfo.put("accountName", m.getAccountName());
				wechatInfo.put("accountNo", m.getAccountNo());
				//wechatInfo.put("openBank", m.getOpenBank());
			}else { //银行卡
				bankcardList.add(m);
			}
		}
		if(MapUtils.isEmpty(alipayInfo)) {
			alipayInfo.put("accountName", "");
			alipayInfo.put("accountNo", "");
			//alipayInfo.put("openBank", "");
		}
		if(MapUtils.isEmpty(wechatInfo)) {
			wechatInfo.put("accountName", "");
			wechatInfo.put("accountNo", "");
			//wechatInfo.put("openBank", "");
		}
		returnMap.put("alipayInfo", alipayInfo);
		returnMap.put("wechatInfo", wechatInfo);
		returnMap.put("bankcardList", bankcardList);
		return returnMap;
	}

	@Override
	public Map<String, Object> saveWithdrawal(String token, String accountName, String accountNo, int drawType) {
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		
		MemberWithdrawInfo MemberWithdrawInfo = new MemberWithdrawInfo();
		if(accountName ==null && accountNo== null && drawType==0){
			throw new AppErrorException("支付宝用户名和账号不能为空！");
		}else if(accountName ==null && accountNo== null && drawType==1){
			throw new AppErrorException("微信昵称和微信号不能为空！");
		}else if(accountName ==null && accountNo== null && drawType==2){
			throw new AppErrorException("开户名和卡号不能为空！");
		}else{
			MemberWithdrawInfo.setMemberId(member.getMemberId());
			MemberWithdrawInfo.setAccountName(accountName);		//银行卡-开户名 支付宝-真实姓名 微信-真实姓名
			MemberWithdrawInfo.setAccountNo(accountNo);			//银行卡-卡号 支付宝-帐号 微信-昵称
			MemberWithdrawInfo.setDrawType(drawType);
			MemberWithdrawInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
			
			if(drawType == 2){			//银行卡
					String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardBinCheck=true";
					String param = "cardNo=" +accountNo;
					String snedResult = SendGET(url, param);
					System.out.println(snedResult);
					Gson gson = new Gson();
					Map<String, Object> map = new HashMap<String, Object>();
					map = gson.fromJson(snedResult, map.getClass());
					String openBank = DictionaryUtil.getDataLabelByName("open_bank",(String)map.get("bank"));
					MemberWithdrawInfo.setOpenBank(openBank);
			}
			
			Example example = new Example(MemberWithdrawInfo.class);
			Criteria createCriteria = example.createCriteria();
			createCriteria.andEqualTo("memberId",member.getMemberId());
			createCriteria.andEqualTo("accountName",accountName);
			createCriteria.andEqualTo("accountNo",accountNo);
			List<MemberWithdrawInfo> WithdrawInfoList = MemberWithdrawInfoDao.selectByExample(example);
			
			if(WithdrawInfoList.size()>0){
				this.update(MemberWithdrawInfo);
			}else{
				this.insert(MemberWithdrawInfo);
			}
			return null;
		}
	}
		
	/**
	 * 查询开户行
	 */
	public static String SendGET(String url,String param){
		   String result="";//访问返回结果
		   BufferedReader read=null;//读取访问结果
		    
		   try {
		    //创建url
		    URL realurl=new URL(url+"&"+param);
		    //打开连接
		    URLConnection connection=realurl.openConnection();
		     // 设置通用的请求属性
		             connection.setRequestProperty("accept", "*/*");
		             connection.setRequestProperty("connection", "Keep-Alive");
		             connection.setRequestProperty("user-agent",
		                     "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		             //建立连接
		             connection.connect();
		             // 获取所有响应头字段
		             Map<String, List<String>> map = connection.getHeaderFields();
		             // 遍历所有的响应头字段，获取到cookies等
		             for (String key : map.keySet()) {
		                 System.out.println(key + "--->" + map.get(key));
		             }
		             // 定义 BufferedReader输入流来读取URL的响应
		             read = new BufferedReader(new InputStreamReader(
		                     connection.getInputStream(),"UTF-8"));
		             String line;//循环读取
		             while ((line = read.readLine()) != null) {
		                 result += line;
		             }
		   } catch (IOException e) {
		    e.printStackTrace();
		   }finally{
		    if(read!=null){//关闭流
		     try {
		      read.close();
		     } catch (IOException e) {
		      e.printStackTrace();
		     }
		    }
		   }
		   return result; 
		 }

	/**
	 * 获取会员提现门槛
	 */
	@Override
	public Map<String, Object> getDrawSill(String token) {
		Member member = this.getMember(token);
		if (member == null) {
			throw new AppErrorException("请先登录！");
		}
		
		PointParam pointParam = pointParamDao.selectAll().get(0);
		Map<String,Object> returnMap = new HashMap<String, Object>();
		returnMap.put("drawSill", pointParam.getWithdrawDepositMin());
		return returnMap;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public String bankCardCertificate(String token, String cardNum, String cardholder, String mobile, String cardType) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		//银行卡号格式校验
		System.out.println("check code: " + getBankCardCheckCode(cardNum));  
        System.out.println("是否为银行卡:"+checkBankCard(cardNum)); 
		if(!this.checkBankCard(cardNum)){
			throw new AppErrorException("银行卡号不符合格式");
		}
		//判断是否已经绑定过该银行卡
		Example example = new Example(MemberWithdrawInfo.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId",member.getMemberId());
		createCriteria.andEqualTo("accountNo",cardNum);
		createCriteria.andEqualTo("isBinding",0);
		int count = MemberWithdrawInfoDao.selectCountByExample(example);
		if(count>0){
			throw new AppErrorException("该银行卡已经添加，不能重复添加");
		}
		/*认证银行卡*/
		//请求地址
        String url="http://aliyun-bankcard-verify.apistore.cn/bank";
        //请求参数
        String params = "bankcard="+cardNum;
        	   params += "&realName="+cardholder;
        	   //params += "&cardNo=身份证号码";  //可为空
        	   //params += "&Mobile="+mobile; 
        String appcode = "c45fd20eee2d44978fea8bd1541db200";
        String apistore_GET = BankCardCertificationUtil.APISTORE_GET(url, params, appcode);
        System.out.println("apistore_GET===="+apistore_GET);
        JSONObject object = JSONObject.fromObject(apistore_GET);
      /*  Map maps = (Map)JSON.parse(apistore_GET);
        for (Object map : maps.entrySet()){  
            System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());  
        }  */
        if((int)object.get("error_code")== 0){	//验证成功
        	/*if(!com.qyy.jyshop.util.StringUtil.isEmpty(object.get("mobile"))){	
        		//判断输入的手机号与银行卡绑定的手机号是否一致
        		if(mobile.equals(object.get("mobile"))){	
        			//一致发送短信
        			try {
        			    this.smsLogServiceImpl.sendSmsCode(token,mobile,SmsLogTypeConstants.OLD_MOBILE);
        			} catch (Exception e) {
        				throw new AppErrorException(e.getMessage());
        			}
        		}else{
        			throw new AppErrorException("输入的手机号与银行卡绑定的手机号不一致");
        		}
        	}else{
        		throw new AppErrorException("该银行卡没有绑定手机号,请到相关支行或网上营业厅办理办理");
        	}*/
        	return apistore_GET;
        }else{
        	return apistore_GET;
        }
	}

	@Override
	public List<Map<String, Object>> selectBankCardList(String token) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		List<Map<String, Object>> bankCardList = MemberWithdrawInfoDao.selectWithDrawalsByMEmberId(member.getMemberId());
		if(bankCardList==null){
			throw new AppErrorException("该用户没有绑定银行卡");
		}else{
			return bankCardList;
		}
	}

	@Override
	public void updateBankCardUnbind(String token, String cardNum) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		MemberWithdrawInfoDao.updateBankCardUnbind(member.getMemberId(),cardNum);
	}
	
	
	
	 /** 
     * 校验银行卡卡号 
     * @param cardId 
     * @return 
     */  
    public static boolean checkBankCard(String cardId) {  
             char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));  
             if(bit == 'N'){  
                 return false;  
             }  
             return cardId.charAt(cardId.length() - 1) == bit;  
    }  
      
    /** 
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位 
     * @param nonCheckCodeCardId 
     * @return 
     */  
    public static char getBankCardCheckCode(String nonCheckCodeCardId){  
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0  
                || !nonCheckCodeCardId.matches("\\d+")) {  
            //如果传的不是数据返回N  
            return 'N';  
        }  
        char[] chs = nonCheckCodeCardId.trim().toCharArray();  
        int luhmSum = 0;  
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {  
            int k = chs[i] - '0';  
            if(j % 2 == 0) {  
                k *= 2;  
                k = k / 10 + k % 10;  
            }  
            luhmSum += k;             
        }  
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');  
    }

	@Override
	public void insertMemberWithdrawInfo(String apistore_GET,Long memberId,String path) {
			if(apistore_GET==null){
				throw new AppErrorException("银行卡信息不存在");
			}
			//Member member = this.getMember(token);
			/*
			if(member==null){
				throw new AppErrorException("该用户不存在");
			}*/
			JSONObject result = JSONObject.fromObject(apistore_GET);
			//JSONObject result = (JSONObject) object.get("result");
	    	JSONObject information = (JSONObject) result.get("information");
	    	MemberWithdrawInfo memberWithdrawInfo = new MemberWithdrawInfo();
	    	memberWithdrawInfo.setMemberId(memberId);
	    	memberWithdrawInfo.setOpenBank(information.get("bankname").toString());
	    	memberWithdrawInfo.setAccountName(result.get("realName").toString());
	    	memberWithdrawInfo.setAccountNo(result.get("bankcard").toString());
	    	memberWithdrawInfo.setDrawType(2);
	    	memberWithdrawInfo.setCreateTime(TimestampUtil.getNowTime());
	    	memberWithdrawInfo.setIsBinding(0);
	    	memberWithdrawInfo.setCardType(information.get("cardtype").toString());
	    	memberWithdrawInfo.setAbbreviation(information.get("abbreviation").toString());
	    	String bankImage = DictionaryUtil.getDataValueByName("open_bank", information.get("abbreviation").toString());
	    	memberWithdrawInfo.setBankImage(path+bankImage);
	    	this.insert(memberWithdrawInfo);
	}

	@Override
	public void setDefaultBankCard(String token, String cardNum) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		MemberWithdrawInfoDao.updateDefaultBankCard(member.getMemberId(),cardNum);
	}  
}
