package com.qyy.jyshop.member.controller;


import com.mysql.jdbc.StringUtils;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.member.service.MemberService;
import com.qyy.jyshop.member.service.PayOrderService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.withdrawal.service.MemberWithdrawInfoService;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
public class MemberController extends AppBaseController{

	@Autowired
	private MemberService memberServiceImpl;
	@Autowired
	private PayOrderService payOrderServiceImpl;
	
	/**
	 * 已签到日期、金币数量返回
	 */
	@RequestMapping(value = "returnSignAndCoin")
	public Map<String,Object> returnSignAndCoin(String token,Integer month){
		try {
			Map<String,Object> resultPage = memberServiceImpl.returnSignAndCoin(token,month);
			return this.outMessage(0, "获取成功", resultPage);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	/**
	 * 签到
	 */
	@RequestMapping(value = "sign")
	public Map<String,Object> memberSign(String token,short coinType){
		try {
			Map<String, Object> map = memberServiceImpl.memberSign(token,coinType);
			return this.outMessage(0, "签到成功", map);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	
	/**
	 * 个人信息修改
	 */
	@RequestMapping(value = "updateMemberInfo")
	public Map<String,Object> updateMemberInfo(@RequestParam Map<String, Object> map){
		try {
			memberServiceImpl.updateMemberInfo(map);
			return this.outMessage(0, "更改成功", null);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	
	/**
	 * 重置登录密码
	 */
	
	
	/**
	 * 设置支付/登录密码
	 */
	@RequestMapping(value = "setPassword")
	public Map<String,Object> setPassword(String token,String payPassword,String password){
		try {
			memberServiceImpl.setPassword(token,payPassword,password);
			return this.outMessage(0, "更改成功", null);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	
	/**
	 * 忘记支付密码接口
	 */
	@RequestMapping(value = "forgetPayPassword")
	public Map<String,Object> forgetPayPassword(String token,String mobile ,String code,String newPayPassword){
		try {
			memberServiceImpl.forgetPayPassword(token,mobile,code,newPayPassword);
			return this.outMessage(0, "支付密码修改成功", null);
		} catch (Exception e) {
			return this.outMessage(0, e.getMessage(), null);
		}
	}
	/**
	 * 删除我的商品
	 */
	@RequestMapping(value = "removeMemberGoods")
	public Map<String,Object> removeMemberGoods(String token,Long goodsId){
		try {
			memberServiceImpl.removeMemberGoods(goodsId,token);
			return this.outMessage(0, "删除成功", null);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	/**
	 * 我的商品
	 */
	
	@RequestMapping(value = "getMemberGoods")
    public Map<String,Object> getMemberGoods(String token){
        List<Map<String, Object>> memberGodos = this.memberServiceImpl.getMemberGoods(token,docBase, path);
        Map<String,Object> returnMap=new HashMap<String, Object>();
        returnMap.put("memberGoods", memberGodos);
        return this.outMessage(0, "获取成功", returnMap);
    }
	/**
	 * 我的商品排序
	 */
	@RequestMapping(value = "orderMemberGoods")
    public Map<String,Object> orderMemberGoods(String token,String orderType){
        List<Map<String, Object>> memberGodos = this.memberServiceImpl.orderMemberGoods(token,docBase, path,orderType);
        Map<String,Object> returnMap=new HashMap<String, Object>();
        returnMap.put("memberGoods", memberGodos);
        return this.outMessage(0, "获取成功", returnMap);
    }
	/**
	 * 成为店长
	 */
	@RequestMapping(value = "applyForShopowner")
	public Map<String,Object> applyForShopowner(String token){
			this.memberServiceImpl.applyForShopowner(token);
			return this.outMessage(0, "获取成功",null );
    }

	/**
	 * 判断之前是否消费200是否绑定手机
	 */
	@RequestMapping(value = "seletConsumptionSituation")
	public Map<String,Object> seletConsumptionSituation(String token){
		Integer amount = memberServiceImpl.seletConsumptionSituation(token);	//是否满足200记录
		String mobileNum =  this.memberServiceImpl.selcetMobile(token);		//是否绑定手机号
		BigDecimal payAmount =  payOrderServiceImpl.selectPayAmount(token);	//充值金额
		int pAmount = payAmount.intValue();
		Integer giftOrderCount = memberServiceImpl.selectGiftOrderCount(token); //是否购买店长礼包
		
		if(giftOrderCount==0){		//没有购买店长礼包 判断其他条件
			if(!StringUtils.isNullOrEmpty(mobileNum) && amount == 0){		
				return  this.outMessage(0, "有手机号，消费不满200", null);
			}else if(!StringUtils.isNullOrEmpty(mobileNum) && amount > 0){
				return  this.outMessage(1, "有手机号，消费满200", null);
			}else if(StringUtils.isNullOrEmpty(mobileNum) && amount == 0){
				return  this.outMessage(2, "没有手机号，消费不满200", null);
			}else if(StringUtils.isNullOrEmpty(mobileNum) && amount > 0){
				return  this.outMessage(3, "没有手机号，消费满200", null);
			}else if(!StringUtils.isNullOrEmpty(mobileNum) && pAmount < 200){
				return  this.outMessage(4, "有手机号，充值不满200", null);
			}else if(!StringUtils.isNullOrEmpty(mobileNum) && pAmount >= 200){
				return  this.outMessage(5, "有手机号，充值满200", null);
			}else if(StringUtils.isNullOrEmpty(mobileNum) && pAmount < 200){
				return  this.outMessage(6, "没有手机号，充值不满200", null);
			}else if(StringUtils.isNullOrEmpty(mobileNum) && pAmount >= 200){
				return  this.outMessage(7, "没有手机号，充值满200", null);
			}
		}else{
			return  this.outMessage(8, "该会员已经购买店长礼包", null);
		}
		/*if(mobileNum!=null){	//手机号不为空
			if(amount == 0 && pAmount < 200){
				return  this.outMessage(0, "有手机号，消费不满200，充值不满200", null);
			}else if(amount == 0 && pAmount>=200){
				return  this.outMessage(1, "有手机号，消费不满200，充值满200", null);
			}else if(amount>0 && pAmount < 200){
				return  this.outMessage(2, "有手机号，消费满200，充值不满200", null);
			}else if(amount>0 && pAmount>=200){
				return  this.outMessage(3, "有手机号，消费满200，充值满200", null);
			}
		}else{
			if(amount == 0 && pAmount < 200){
				return  this.outMessage(4, "没有手机号，消费不满200，充值不满200", null);
			}else if(amount == 0 && pAmount>=200){
				return  this.outMessage(5, "没有手机号，消费不满200，充值满200", null);
			}else if(amount>0 && pAmount < 200){
				return  this.outMessage(6, "没有手机号，消费满200，充值不满200", null);
			}else if(amount>0 && pAmount>=200){
				return  this.outMessage(7, "没有手机号，消费满200，充值满200", null);
			}
		}*/
		return null;
	  }
	/***
	 * 生成我的二维码
	 */
    //访问地址
	@Value("${upload.path}")
	private String path;
    //存放地址
	@Value("${upload.docBase}")
	private String docBase;
	//logo图片
	@Value("${upload.logoPath}")
	private String logoPath;
	//private String path = "D:/image/";
	//private String logoPath = "D:/image/juyouLogo.jpg";
    //private String docBase ="D:/image/";
	@RequestMapping(value = "/anon/myTwoDimensionCode")
	public Map<String,Object> MyTwoDimensionCode(Long memberId) throws Exception{
		String codePath = memberServiceImpl.MyTwoDimensionCode(memberId,docBase, path,logoPath);
		Member member = memberServiceImpl.selectMemberBYMemberId(memberId);
		Map<String,Object> map=new HashMap<String, Object>();
		if("".equals(codePath) || codePath==null){
			return this.outMessage(1, "获取失败",null );
		}else{
			map.put("codePath", codePath);
			map.put("member", member);
			return this.outMessage(0, "获取成功",map );
		}
	}
	/***
	 * 我的客户
	 */
	@RequestMapping(value = "myChildMemberList")
	public Map<String,Object> myChildMemberList(String token){
		Map<String,Object> map=new HashMap<String, Object>();
		List<Member> childMemberList = memberServiceImpl.queryMyChildMemberList(token);
		for (Member member : childMemberList) {
			//查询交易总金额
			Long sumAmount = memberServiceImpl.selectSumAmountByMemberId(member.getMemberId());
			if(sumAmount==null || sumAmount==0){
				sumAmount = 0L;
			}
			member.setAmount(new BigDecimal(sumAmount));
		}
		//map.put("sumAmount", sumAmount);
		if(!CollectionUtils.isEmpty(childMemberList)){
			map.put("childMemberList", childMemberList);
			return this.outMessage(0, "获取成功", map );
		}else{
			return this.outMessage(1, "该用户没有下级",null );
		}
	}
	
	/**
	 * 我要推广
	 */
	@RequestMapping(value = "memberPromote")
	public Map<String,Object> memberPromote(Long goodsId,String token){
		try {
			memberServiceImpl.memberPromote(goodsId,token);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(),null );
		}
		return this.outMessage(0, "推广成功", null );
	}
	
	/**
	 * 红黄积分余额
	 */
	@RequestMapping(value = "jifenyuetongji")
	public Map<String,Object> jifenyuetongji(String token){
		Member member = memberServiceImpl.jifenyuetongji(token);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("member", member);
		return this.outMessage(0, "获取成功", map );
	}
	
	/**
	 * appStore审核状态
	 */
	@RequestMapping(value = "examineState")
	public Map<String,Object> examineState(){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("state", 1);
		return this.outMessage(0, "获取成功", map );
	}
}
