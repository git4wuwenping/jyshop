package com.qyy.jyshop.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.member.service.RedPointService;
import com.qyy.jyshop.model.MemberPointExt;
import com.qyy.jyshop.pojo.PageAjax;

@RestController
public class RedPointController extends AppBaseController{

	@Autowired
	private RedPointService redPointService;
	
	/**
	 * 获取会员红积分信息
	 */
	@RequestMapping(value = "getMemberRedPointInfo")
    public Map<String,Object> getMemberRedPointInfo(String token){
		Map<String,Object> memberRedPointInfo =this.redPointService.getMemberRedPointInfo(token);
        return this.outMessage(0, "获取成功", memberRedPointInfo);
    }
	
	/**
	 * 红积分提现明细
	 */
	@RequestMapping(value = "getMemberRedPointDrawList")
    public Map<String,Object> getMemberRedPointDrawList(String token,int status,Integer pageNo,Integer pageSize){
		PageAjax<Map<String,Object>> memberRedPointDrawList =this.redPointService.getMemberRedPointDrawList(token,status,pageNo, pageSize);
		if(memberRedPointDrawList == null || memberRedPointDrawList.getSize()==0){
        	return this.outMessage(1, "没有提现明细", null);
        }
		return this.outMessage(0, "获取成功", this.getPageMap(memberRedPointDrawList));
    }
	
	/**
	 * 获取红积分明细
	 */
	@RequestMapping(value = "getMemberRedPointGainList")
    public Map<String,Object> getMemberRedPointGainList(String token){
		List<Map<String, Object>> memberRedPointGainList = this.redPointService.getMemberRedPointGainList(token);
		List<Map<String, Object>> memberRedPointGainUnconfirmedList = this.redPointService.getMemberRedPointGainUnconfirmedList(token);
		memberRedPointGainList.addAll(memberRedPointGainUnconfirmedList);
        if(CollectionUtils.isEmpty(memberRedPointGainList)){
        	return this.outMessage(1, "没有红积分明细记录", null);
        }
        Map<String,Object> returnMap = new HashMap<String, Object>();
        returnMap.put("memberRedPointGainList", memberRedPointGainList);
		return this.outMessage(0, "获取成功", returnMap);
    }
	
	/**
	 * 获取已确认红积分明细
	 */
	@RequestMapping(value = "getMemberRedPointGainConfirmedList")
    public Map<String,Object> getMemberRedPointGainConfirmedList(String token){
		List<Map<String, Object>> memberRedPointGainList = this.redPointService.getMemberRedPointGainList(token);
        if(CollectionUtils.isEmpty(memberRedPointGainList)){
        	return this.outMessage(1, "没有已确认红积分明细记录", null);
        }
        Map<String,Object> returnMap = new HashMap<String, Object>();
        returnMap.put("memberRedPointGainList", memberRedPointGainList);
		return this.outMessage(0, "获取成功", returnMap);
    }
	
	/**
	 * 获取待确认红积分明细
	 */
	@RequestMapping(value = "getMemberRedPointGainUnconfirmedList")
    public Map<String,Object> getMemberRedPointGainUnconfirmedList(String token){
		List<Map<String, Object>> memberRedPointGainUnconfirmedList = this.redPointService.getMemberRedPointGainUnconfirmedList(token);
        if(CollectionUtils.isEmpty(memberRedPointGainUnconfirmedList)){
        	return this.outMessage(1, "没有待确认红积分明细记录", null);
        }
        Map<String,Object> returnMap = new HashMap<String, Object>();
        returnMap.put("memberRedPointGainList", memberRedPointGainUnconfirmedList);
		return this.outMessage(0, "获取成功", returnMap);
    }
	
	/**
	 * 红积分提现
	 * param
	 * 	token 用户token
	 * 	drawPoint 申请兑换红积分
	 * 	MemberPointExt memberPointExt
	 */
	@RequestMapping(value = "redPointDraw")
    public Map<String,Object> redPointDraw(String token,Integer drawPoint,Integer style,MemberPointExt memberPointExt){
		Map<String, Object> result = this.redPointService.redPointDraw(token,drawPoint,style,memberPointExt);
		if(MapUtils.isNotEmpty(result)){
			return this.outMessage((Integer)result.get("code"), (String)result.get("msg"),null);
		}
        return this.outMessage(0, "操作成功",null);
    }
	
}
