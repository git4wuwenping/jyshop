package com.qyy.jyshop.withdrawal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.withdrawal.service.MemberWithdrawInfoService;

import net.sf.json.JSONObject;
@RestController
public class MemberWithdrawInfoController extends AppBaseController{
	
	@Autowired
	private MemberWithdrawInfoService withdrawalServiceImpl;
	@Autowired
	private MemberWithdrawInfoService memberWithdrawInfoServiceImpl;
	/**
	 * 设为默认银行卡
	 */
	@RequestMapping(value = "setDefaultBankCard")
	public Map<String,Object> setDefaultBankCard(String token,String cardNum){
		memberWithdrawInfoServiceImpl.setDefaultBankCard(token,cardNum);
		return null;
	}
	/**
	 * 银行卡解除绑定
	 */
	@RequestMapping(value = "bankCardUnbind")
	public Map<String,Object> bankCardUnbind(String token,String cardNum){
		try {
			memberWithdrawInfoServiceImpl.updateBankCardUnbind(token,cardNum);
			return this.outMessage(0, "解除绑定成功", null);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	/**
	 * 银行卡列表
	 */
	@RequestMapping(value = "bankCardList")
	public Map<String,Object> bankCardList(String token){
		try {
			List<Map<String,Object>> resultMap = memberWithdrawInfoServiceImpl.selectBankCardList(token);
			Map<String,Object> map = new HashMap<>();
			map.put("resultMap", resultMap);
			return this.outMessage(0, "获取成功", map);
		} catch (Exception e) {
			return this.outMessage(1, e.getMessage(), null);
		}
	}
	/**
	 * 添加银行卡
	 * @param cardNum 卡号
	 * @param cardholder 持卡人
	 * @param mobile 持卡人手机号
	 * @param cardType 卡类型
	 */
	@RequestMapping(value = "bankCardCertificate")
	public Map<String,Object> bankCardCertificate(String token,String cardNum,String cardholder,String mobile,String cardType){
		String string =null;
		try {
			string = memberWithdrawInfoServiceImpl.bankCardCertificate(token,cardNum,cardholder,mobile,cardType);
			JSONObject object = JSONObject.fromObject(string);
			if((int)object.get("error_code") == 0){
				return this.outMessage(0, "认证成功", object);
			}else{
				return this.outMessage(1, "认证失败", object);
			}
		} catch (Exception e) {
			return this.outMessage(2, e.getMessage(), null);
		}
	}
	
	/**
	 * 保存提现方式信息
	 * @param token
	 * @param accountName
	 * @param accountNo
	 * @param drawType
	 * @return
	 */
	@RequestMapping(value = "saveWithdrawal")
    public Map<String,Object> saveWithdrawal(String token,String accountName,String accountNo,int drawType){
		Map<String,Object> map= new HashMap<String, Object>();
		map = withdrawalServiceImpl.saveWithdrawal(token,accountName,accountNo,drawType);
		if(map!=null && map.get("msg").equals("保存失败")){
			return this.outMessage(01, "保存失败", null);
		}
		return this.outMessage(0, "保存成功", null);
    }
	
	/**
	 * 获取会员提现方式信息
	 * @param token
	 * @param accountName
	 * @param accountNo
	 * @param drawType
	 * @return
	 */
	@RequestMapping(value = "getWithdrawalDetial")
    public Map<String,Object> getWithdrawalDetial(String token){
		Map<String,Object> map= new HashMap<String, Object>();
		map = withdrawalServiceImpl.getWithdrawalDetial(token);
        return this.outMessage(0, "获取成功", map);
    }
	
	
	/**
	 * 获取会员提现门槛
	 * @param token
	 * @param accountName
	 * @param accountNo
	 * @param drawType
	 * @return
	 */
	@RequestMapping(value = "getDrawSill")
    public Map<String,Object> getDrawSill(String token){
		Map<String,Object> map= new HashMap<String, Object>();
		map = withdrawalServiceImpl.getDrawSill(token);
        return this.outMessage(0, "获取成功", map);
    }
}
