package com.qyy.jyshop.member.service.impl;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.conf.SmsLogTypeConstants;
import com.qyy.jyshop.dao.CoinDtlDao;
import com.qyy.jyshop.dao.GoodsDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.member.service.MemberService;
import com.qyy.jyshop.model.CoinDtl;
import com.qyy.jyshop.model.CoinParamConfig;
import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.smsLog.service.SmsLogService;
import com.qyy.jyshop.supple.AbstratService;
//import com.qyy.jyshop.util.BankCardCertificationUtil;
import com.qyy.jyshop.util.LogoQRCode;
import com.qyy.jyshop.util.MD5Util;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.util.TwoDimensionCode;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class MemberServiceImpl extends AbstratService<Member> implements MemberService {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private SmsLogService smsLogServiceImpl;
	@Autowired
	private CoinDtlDao coinDtlDao;
	@Autowired
	private ShopBaseConf shopBaseConf;

	@Override
	public List<Map<String, Object>> getMemberGoods(String token,String docBase, String path) {
		 Member member=this.getMember(token);
		 Long memberId = member.getMemberId();
		 //将商品加入到当前登录会员的推广商品中
		// memberDao.addGoodsToMember(goodsId,memberId);
		 //查询“我的商品”
		 List<Map<String, Object>> memGoods = goodsDao.getMemberGoods(memberId);
		 for (Map<String, Object> map : memGoods) {
			String qRCodePath = (String) map.get("qRCodePath");
			if(StringUtil.isEmpty(qRCodePath)){
				long currentTimeMillis = System.currentTimeMillis();
				TwoDimensionCode twoDimensionCode = new TwoDimensionCode();
				String content = "http://wx.cheertea.com/#/goods/detail?goodsId="+map.get("goodsId");
				
				twoDimensionCode.encoderQRCode(content, null, "jpg", docBase+currentTimeMillis+".jpg");
				map.put("qRCodePath", path+currentTimeMillis+".jpg");
				//更新商品二维码
				Long goodsId = (Long) map.get("goodsId");
				qRCodePath = path+currentTimeMillis+".jpg";
				goodsDao.updateGoodsQRCodePathById(goodsId,qRCodePath);
			}
		}
		return memGoods;
	}


	@Override
	public String applyForShopowner(String token) {
		Member member=this.getMember(token);
		Long memberId = member.getMemberId();
		memberDao.applyForShopowner(memberId);
		member.setIdentityId(1);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		member.setBecomeShopownerTime(timestamp);
		member.setProfitTime(timestamp);
		member.setIsProfit(1);
		redisDao.saveObject(token, member, 3600*24*30);
		return null;
	}


	@Override
	public String selcetMobile(String token) {
		Member member = this.getMember(token);
		//String mobile = memberDao.selcetMobile(member.getMemberId());
		//return mobile;
		String mobile = member.getMobile();
		return mobile;
	}


	@Override
	public Integer seletConsumptionSituation(String token) {
		Member member = this.getMember(token);
		return memberDao.seletConsumptionSituation(member.getMemberId());
	}


	@Override
	public boolean updateMemberMobile(String token, String mobile) {
		Member member = this.getMember(token);
		int count = memberDao.seletMemberByMobile(member.getMemberId(),mobile);
		if(count>0){
			return true;
		}else{
			if(!mobile.equals(this.queryByID(member.getMemberId()).getMobile())){
				String password = StringUtil.md5("123456");
				memberDao.updateMemberMobile(member.getMemberId(),mobile,password);
				redisDao.saveObject(token, member, 3600*24*30);
			}else{
				throw new AppErrorException("新手机号与原手机号一致，不能重复绑定");
			}
			return false;
		}
	}


	@Override
	public String MyTwoDimensionCode(Long memberId,String docBase,String path,String logoPath) throws Exception {
		//Member member = this.getMember(token);
		Member member = memberDao.findMemerById(memberId);
		System.out.println("memberId:"+memberId);
		System.out.println("qRCodePath:"+member.getqRCodePath());
		if(!StringUtil.isEmpty(member.getqRCodePath())){
			return member.getqRCodePath();
		}else{
			member.setMemberId(member.getMemberId());
			//long currentTimeMillis = System.currentTimeMillis();
			String content = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
					"appid=wxb4868f50223328db&redirect_uri=http%3a%2f%2fwx.cheertea.com%2f%23%2flogin&response_type=code&scope=snsapi_userinfo&state="+member.getMemberId()+"#wechat_redirect";
			//TwoDimensionCode twoDimensionCode = new TwoDimensionCode();
			//twoDimensionCode.encoderQRCode(content, null, "jpg", docBase+currentTimeMillis+".jpg");
			long time = new Date().getTime();
			docBase= docBase+ time;
			path = path +time;
			LogoQRCode.getLogoQRCode(content, null,docBase,logoPath);
			member.setqRCodePath(path + ".jpg");
			this.update(member);
			//redisDao.saveObject(token, member, 3600*24);
			return member.getqRCodePath();
		}
	}

	@Override
	public List<Member> queryMyChildMemberList(String token) {
		Member member=this.getMember(token);
		Long memberId = member.getMemberId();
		Example example = new Example(Member.class);
		Example.Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("parentId",memberId);
		//redisDao.saveObject(token, member, 3600*24*30);
		return this.queryByExample(example);
	}


	@Override
	public void memberPromote(Long goodsId,String token) {
		 Member member=this.getMember(token);
		 Long memberId = member.getMemberId();
		 int count = memberDao.selectMemberGoodsById(goodsId,memberId);
		 if(count>0){
			 throw new AppErrorException("一个用户不能重复推广同一件商品");
		 }else{
			 //将商品加入到当前登录会员的推广商品中
			 memberDao.addGoodsToMember(goodsId,memberId);
		 }
	}


	@Override
	public Long selectSumAmountByMemberId(Long memberId) {
		
		return memberDao.selectSumAmountByMemberId(memberId);
	}


	@Override
	public Member jifenyuetongji(String token) {
		Member member = this.queryByID(this.getMemberId(token));
		redisDao.saveObject(token, member, 3600*24*30);
		return member;
	}


	@Override
	public List<Map<String, Object>> orderMemberGoods(String token, String docBase, String path, String orderType) {
		
		return null;
	}


	@Override
	public void removeMemberGoods(Long goodsId, String token) {
        if(StringUtil.isEmpty(goodsId))
            throw new AppErrorException("商品Id不能为空");
        Member member=this.getMember(token);
        Long memberId = member.getMemberId();
        int count = memberDao.selectMemberGoodsById(goodsId, memberId);
        if(count==0){
        	 throw new AppErrorException("改商品已经被删除");
        }
        memberDao.removeMemberGoods(memberId,goodsId);
	}


	@Override
	public boolean checkMemberUserName(String mobile) {
		int count = memberDao.checkMemberUserName(mobile);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public void updateMemberInfo(Map<String, Object> map) {
		String token = (String) map.get("token");
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		Long memberId = member.getMemberId();
		ParamData params = new ParamData();
		params.put("memberId", memberId);
		for(Map.Entry<String, Object> entry : map.entrySet()){
			
			String filed = entry.getKey();
			if(filed.equals("nickname")){
				params.put("nickname",entry.getValue());
			}else if(filed.equals("weixinFace")){
				params.put("weixinFace",entry.getValue());
			}else if(filed.equals("payPassword")){
				String newPayPassword = (String) map.get("newPayPassword");	
				if(newPayPassword==null ||StringUtil.isEmpty(newPayPassword)){	//设置支付密码
					params.put("payPassword",StringUtil.md5(entry.getValue()+""));
				}else{		//修改支付密码
					int count = memberDao.checkPayPasswordByMemberId(memberId,StringUtil.md5((String)map.get("payPassword")));
					if(count==0){	//原支付密码错误
						throw new AppErrorException("原支付密码错误");
					}else{
						params.put("newPayPassword",StringUtil.md5(newPayPassword));
					}
				}
			}
			
		}
		this.updateBy("MemberDao.updateMemberInfo", params);
		this.redisDao.saveObject(token, member,3600*24*30);
	}


	@Override
	public Member selectMemberBYMemberId(Long memberId) {
		
		return this.queryByID(memberId);
	}


	@Override
	public void forgetPayPassword(String token, String mobile, String code, String newPayPassword) {
		Integer type = SmsLogTypeConstants.OLD_MOBILE;
		if(smsLogServiceImpl.checkCode(token,mobile,code,type)){		//短信验证成功
			ParamData params = new ParamData();
			Long memberId = this.getMemberId(token);
			params.put("newPayPassword", StringUtil.md5(newPayPassword));
			params.put("memberId", memberId);
			this.updateBy("MemberDao.updateMemberInfo", params);
			this.redisDao.saveObject(token, this.getMember(token),3600*24*30);
		}else{
			throw new AppErrorException("短信验证失败");
		}
	}


	@Override
	public Map<String, Object>  memberSign(String token,short coinType) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		CoinParamConfig coinParamConfig = shopBaseConf.getParamConfig(CoinParamConfig.class, "shopParam_coinParamConfig");
		
		List<Map<String, Object>> memberSign = coinDtlDao.selectCoinDtlByMemberId(member.getMemberId(),coinType);
		if(memberSign.size()>0){
			throw new AppErrorException("已签到");
		}
		CoinDtl shopCoinDtl  = new CoinDtl();
		shopCoinDtl.setCoinType(coinType);
		shopCoinDtl.setCreateTime(TimestampUtil.getNowTime());
		shopCoinDtl.setMemberId(member.getMemberId());
		shopCoinDtl.setCoinNum(coinParamConfig.getCoinSigninGet());
		coinDtlDao.insert(shopCoinDtl);
		
		member.setCoin(member.getCoin().add(new BigDecimal(shopCoinDtl.getCoinNum())));
		this.update(member);
		
		this.redisDao.saveObject(token, member,3600*24*30);
		
		Map<String, Object> map = new HashMap<>();
		map.put("coinNum", coinParamConfig.getCoinSigninGet());
		return map;
	}


	@Override
	public Map<String, Object> returnSignAndCoin(String token, Integer month) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		//获取当前年份
		 String curYear = new SimpleDateFormat("yyyy").format(new Date());
		 curYear= curYear+"-"+month+"-01";
		 List<Map<String, Object>> signList =  coinDtlDao.selectSignListById(member.getMemberId(),curYear);
		 //已获取金币
		 Map<String, Object> map = new HashMap<>();
		 map.put("signList", signList);
		 map.put("coins", member.getCoin());
		 
		return map;
	}


	@Override
	public Integer selectGiftOrderCount(String token) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		return memberDao.selectGiftOrderCount(member.getMemberId());
	}


	@Override
	public void setPassword(String token, String payPassword,String password) {
		Member member=this.getMember(token);
		if(member==null){
			throw new AppErrorException("该用户不存在");
		}
		
		if(!StringUtil.isEmpty(payPassword)){
			member.setPayPassword(StringUtil.md5(payPassword));
		}else{
			throw new AppErrorException("支付密码不能为空");
		}
		
		/*if(!StringUtil.isEmpty(password)){
			member.setPassword(StringUtil.md5(password));
		}else{
			throw new AppErrorException("登录密码不能为空");
		}*/
		
		this.update(member);
		this.redisDao.saveObject(token, member,3600*24*30);
	}


	@SuppressWarnings("rawtypes")
//	@Override
	public void bankCardCertificate(String token, String cardNum, String cardholder, String mobile, String cardType) {
		// TODO Auto-generated method stub
		//请求地址
        String url="http://aliyun-bankcard-verify.apistore.cn/bank";
        //请求参数
        String params = "bankcard="+cardNum;
        		   params += "&realName="+cardholder;
        		   //params += "&cardNo=身份证号码";  //可为空
        		   params += "&Mobile="+mobile; //可为空
        String appcode = "c45fd20eee2d44978fea8bd1541db200";
//        String apistore_GET = BankCardCertificationUtil.APISTORE_GET(url, params, appcode);
//        System.out.println("apistore_GET===="+apistore_GET);
//        Map maps = (Map)JSON.parse(apistore_GET);
//        for (Object map : maps.entrySet()){  
//            System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());  
//        }  
//        if(maps.get("error_code").equals("0")){}
	}
	
	
}
