package com.qyy.jyshop.admin.point.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.point.service.RedPointService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.PointDrawLogDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.PointDrawLog;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;

@Service
public class RedPointServiceImpl extends AbstratService<MemberPoint> implements RedPointService {

	@Autowired
	private MemberPointDao memberPointDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private ProfitParamDao profitParamDao;
	@Autowired
	private PointDrawLogDao pointDrawLogDao;
	
	@Override
	public PageAjax<Map<String, Object>> pageRedPointAjax(PageAjax<Map<String, Object>> page, Integer conditionType,
			String conditionVal, Integer memberType) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		if(conditionType!=null && conditionType == 1 && StringUtils.isNotBlank(conditionVal)) {
			params.put("nickname", "%"+conditionVal+"%");
		}
		if(conditionType!=null && conditionType == 2 && StringUtils.isNotBlank(conditionVal)) {
			params.put("memberId", "%"+conditionVal+"%");
		}
		params.put("memberType", memberType);
		return this.pageQuery("MemberPointDao.selectMemberRedPointList", params);
	}

	@Override
	public Map<String,Object> queryMemberPointByMemberId(Long memberId) {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Example example = new Example(MemberPoint.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId", memberId);
		createCriteria.andEqualTo("pointType", 2);
		createCriteria.andEqualTo("type", Integer.valueOf(DictionaryUtil.getDataValueByName("use_type", "gain")));
		example.setOrderByClause("create_time desc");
		List<MemberPoint> returnList = this.queryByExample(example);
		
		BigDecimal memberPointUnconfirmed = new BigDecimal(0);
		
		ProfitParam profitParam=this.profitParamDao.selectAll().get(0);
		BigDecimal totalScale=profitParam.getTax().add(profitParam.getManagement());
		totalScale=totalScale.add(profitParam.getOperation());
		totalScale=totalScale.add(profitParam.getGain());
		totalScale=totalScale.add(profitParam.getPresenter());
        
        if(totalScale.compareTo(new BigDecimal(100))<0){
        	List<Map<String,Object>> redPointUnconfirmedList= this.memberPointDao.selectRedPointUnconfirmedList(memberId);
        	List<MemberPoint> memberPointFirst = new ArrayList<MemberPoint>();
        	for(Map map : redPointUnconfirmedList){
        		MemberPoint memberPoint = new MemberPoint();
        		
        		String type = (String) map.get("type");
        		if(type != null && type.equals("firstProfit")){
        			BigDecimal firstProfit = (BigDecimal) map.get("profit");
        			if(firstProfit.compareTo(BigDecimal.ZERO)<=0){
        				continue;
        			}
        			//计算自购首次分润订单对应的分润
            		BigDecimal profitAmount=firstProfit.subtract(firstProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
            		BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
            		map.put("profit", divide);
            		memberPoint.setGetType(0);//0 未确认首次自购
            		memberPoint.setPoint(divide);
            		memberPointUnconfirmed = memberPointUnconfirmed.add(divide);
        		}else if(type != null && type.equals("secProfit")){
        			BigDecimal secProfit = (BigDecimal) map.get("profit");
        			//计算自购分润
        			BigDecimal profitAmount=secProfit.subtract(secProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
        			BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
        			map.put("profit", divide);
        			memberPoint.setGetType(1); //1 未确认自购
        			memberPoint.setPoint(divide);
        			memberPointUnconfirmed = memberPointUnconfirmed.add(divide);
        		}else if(type != null && type.equals("subProfit")){
        			BigDecimal subProfit = (BigDecimal) map.get("profit");
        			//计算下级待确认分润
        			BigDecimal profitAmount=subProfit.subtract(subProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
                	BigDecimal divide = profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
                	map.put("profit", divide);
                	memberPoint.setGetType(2); //2 未确认下级
                	memberPoint.setPoint(divide);
                	memberPointUnconfirmed = memberPointUnconfirmed.add(divide);
        		}
        		memberPoint.setCreateTime((Timestamp)map.get("paymentTime"));
        		//memberPoint.setPointStatus(0);
        		memberPoint.setOrderType(Integer.parseInt((String)map.get("orderType")));
        		memberPoint.setOrderSn((String)map.get("orderSn"));
        		if(memberPoint.getGetType() == 0){
        			memberPointFirst.add(memberPoint);
        		}else{
        			returnList.add(memberPoint);
        		}
        	}
        	if(memberPointFirst!=null && memberPointFirst.size() == 1){
        		returnList.add(memberPointFirst.get(0));
        	}else{
        		memberPointFirst.forEach(memberPoint->{
        		    if(memberPoint.getOrderType() == 1){
        		    	returnList.add(memberPoint);
        		    }
        		});
        	}
        }
        
		returnMap.put("memberPointList", returnList);
		returnMap.put("memberPointUnconfirmed", memberPointUnconfirmed );
		
		return returnMap;
	}

	@Override
	public Map<String, Object> queryMemberInfoByMemberId(Long memberId) {
		return memberPointDao.queryMemberInfoByMemberId(memberId);
	}

	@Override
	public MemberPoint getMemberPointById(Long mpId) {
		return this.queryByID(mpId);
	}

    @Override
    public List<PointDrawLog> queryPointLogListByBillNo(String billNo) {
        Example example = new Example(PointDrawLog.class);
        example.setOrderByClause("CREATION_TIME asc");
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("billNo", billNo);
        return pointDrawLogDao.selectByExample(example);
    }

}
