package com.qyy.jyshop.admin.member.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.MemberWithdrawInfoDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.MemberWithdrawInfo;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;

@Service
public class MemberServiceImpl extends AbstratService<Member> implements MemberService {

	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProfitParamDao profitParamDao;
	@Autowired
	private MemberPointDao memberPointDao;
	@Autowired
	private MemberWithdrawInfoDao memberWithdrawInfoDao;
	
	/**
	 * 查询会员列表
	 */
	public PageAjax<Map<String, Object>> selectMemberByParams(PageAjax<Member> page) {
		 ParamData params = this.getParamData(page.getPageNo(),page.getPageSize());
	        //params.put("state", state);
	        return this.pageQuery("MemberDao.selectMemberByParams", params);
	}

	/**
	 * 查询会员详情到用户详情页
	 */
	@Override
	public Member preDetaileCustomer(Long memId) {
		return this.queryByID(memId);
	}

	@Override
	public PageAjax<Order> PageIntegral(PageAjax<Order> page, Order order, Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> PageIntegral(Long memberId) {
		List<Order> orders = orderDao.findOrderByMemId(memberId);
		return orders;
	}

	@Override
	public List<Member> pagelowerMemAjax(Long memberId) {
		List<Member> memList =  memberDao.findLowerMem(memberId);
		return memList;
	}
	/**
	 * 根据会员id查询订单中的积分记录
	 */
	/*@Override
	public PageAjax<Order> PageIntegral(PageAjax<Order> page, Order order, Long memberId) {
		List<Order> orders = orderDao.findOrderByMemId(memberId);
		page.setRows(orders);
		return page;
	}*/

	@Override
	@Transactional
	public void updateMemberPointById(Long memberId, BigDecimal redPoint) {
		memberDao.updateMemberPointById(memberId,redPoint);
	}

	@Override
	public List<MemberPoint> getPointListByMemberId(Long memberId) {
		Example example = new Example(MemberPoint.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId",memberId);
		return memberPointDao.selectByExample(example);
	}

	@Override
	public List<Member> getSonMemberList(Long memberId) {
		Example example = new Example(Member.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("parentId",memberId);
		return this.queryByExample(example);
	}

	@Override
	public List<Order> getOrderListByMemberId(Long memberId) {
		Example example = new Example(Order.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId",memberId);
		return orderDao.selectByExample(example);
	}

	@Override
	public Map<String,Object> getDrawTypeListByMemberId(Long memberId) {
		Map<String,Object> returnMap= new HashMap<>();
		Example example = new Example(MemberWithdrawInfo.class);
		Criteria createCriteria = example.createCriteria();
		createCriteria.andEqualTo("memberId",memberId);
		List<MemberWithdrawInfo> list = memberWithdrawInfoDao.selectByExample(example);
		Map<String,Object> alipayInfo = new HashMap<>();
		Map<String,Object> wechatInfo = new HashMap<>();
		List<MemberWithdrawInfo> bankcardList = new ArrayList<>();
		for(MemberWithdrawInfo m : list) {
			if(m.getDrawType() == 0) {//支付宝
				alipayInfo.put("accountName", m.getAccountName());
				alipayInfo.put("accountNo", m.getAccountNo());
			}else if(m.getDrawType() == 1) {//微信
				wechatInfo.put("accountName", m.getAccountName());
				wechatInfo.put("accountNo", m.getAccountNo());
			}else { //银行卡
				bankcardList.add(m);
			}
		}
		if(MapUtils.isEmpty(alipayInfo)) {
			alipayInfo.put("accountName", "");
			alipayInfo.put("accountNo", "");
		}
		if(MapUtils.isEmpty(wechatInfo)) {
			wechatInfo.put("accountName", "");
			wechatInfo.put("accountNo", "");
		}
		returnMap.put("alipayInfo", alipayInfo);
		returnMap.put("wechatInfo", wechatInfo);
		returnMap.put("bankcardList", bankcardList);
		return returnMap;
	}

	@Override
	@Transactional
	public String modifyMemberState(Long memberId, Integer state) {
		Member member = new Member();
		member.setMemberId(memberId);
		member.setState(state);
		this.updateByID(member);
		return null;
	}

	@Override
	public List<MemberPoint> getPointUnconfirmedListByMemberId(Long memberId) {
		List<MemberPoint> returnList = new ArrayList<MemberPoint>();
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
        			//if(firstProfit.compareTo(BigDecimal.ZERO)<=0){
        			//	continue;
        			//}
        			//计算自购首次分润订单对应的分润
            		BigDecimal profitAmount=firstProfit.subtract(firstProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
            		BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
            		map.put("profit", divide);
            		memberPoint.setGetType(0);//0 未确认首次自购
            		memberPoint.setPoint(divide);
        		}else if(type != null && type.equals("secProfit")){
        			BigDecimal secProfit = (BigDecimal) map.get("profit");
        			//计算自购分润
        			BigDecimal profitAmount=secProfit.subtract(secProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
        			BigDecimal divide = profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
        			map.put("profit", divide);
        			memberPoint.setGetType(1); //1 未确认自购
        			memberPoint.setPoint(divide);
        		}else if(type != null && type.equals("subProfit")){
        			BigDecimal subProfit = (BigDecimal) map.get("profit");
        			//计算下级待确认分润
        			BigDecimal profitAmount=subProfit.subtract(subProfit.multiply(totalScale).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
                	BigDecimal divide = profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN).divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
                	map.put("profit", divide);
                	memberPoint.setGetType(2); //2 未确认下级
                	memberPoint.setPoint(divide);
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
        		    if(memberPoint.getOrderType() == 1 && memberPoint.getPoint().compareTo(BigDecimal.ZERO) > 0){
        		    	returnList.add(memberPoint);
        		    }
        		});
        	}
        }
		return returnList;
	}

	@ServiceLog("会员分页<DataTablePage>")
    @Override
    public DataTablePage<Map<String, Object>> pageDataTableMember(Map<String, Object> map) {
	    //分页
        Integer start = Integer.parseInt(map.get("start").toString());
        Integer pageSize = Integer.parseInt(map.get("length").toString());
        Integer draw = Integer.parseInt(map.get("draw").toString());
        
        ParamData params = this.getParamData(start/pageSize+1, pageSize);
        if(map.get("search[nickname]") != null && StringUtils.isNotBlank(map.get("search[nickname]").toString())){
            params.put("nickname", map.get("search[nickname]"));
        }
        if(map.get("search[mobile]") != null && StringUtils.isNotBlank(map.get("search[mobile]").toString())){
            params.put("mobile", map.get("search[mobile]"));
        }
        PageAjax<Map<String,Object>> page = this.pageQuery("MemberDao.selectDataTableMember", params);
        return new DataTablePage(draw, page.getRows(),page.getTotal(), page.getTotal());
    }

}
