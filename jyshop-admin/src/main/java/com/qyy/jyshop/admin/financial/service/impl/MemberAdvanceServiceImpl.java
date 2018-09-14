package com.qyy.jyshop.admin.financial.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.financial.service.MemberAdvanceService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.PayLogDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.PayLog;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class MemberAdvanceServiceImpl implements MemberAdvanceService {
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public List<PayLog> queryPayLogListByMemberId(Long memberId) {
        Example example = new Example(PayLog.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andEqualTo("memberId", memberId);
        createCriteria.andNotEqualTo("payMoney", 0);
        example.setOrderByClause("pay_time asc");
        return payLogDao.selectByExample(example);
    }

    @Override
    @Transactional
    public AjaxResult recharge(String memberId, String rmb,String adminRemark) {
        try{
            Long memberIdL = Long.parseLong(memberId);
            BigDecimal advanceAdd = new BigDecimal(rmb);
            
            Member member = memberDao.selectByPrimaryKey(memberIdL);
            Member memberUpdate = new Member();
            memberUpdate.setMemberId(memberIdL);
            memberUpdate.setAdvance(member.getAdvance().add(advanceAdd));
            memberDao.updateByPrimaryKeySelective(memberUpdate);
            
            //记录日志
            PayLog payLog = new PayLog();
            payLog.setOrderId(null);
            payLog.setOrderSn(null);
            payLog.setOrderType(null);
            payLog.setMemberId(memberIdL);
            payLog.setMethodId(10); //admin代充值
            payLog.setPayMoney(advanceAdd);
            payLog.setAdvanceBefore(member.getAdvance());
            payLog.setAdvanceAfter(member.getAdvance().add(advanceAdd));
            payLog.setPayTime(TimestampUtil.getNowTime());
            payLog.setRemark("admin代充值");
            payLog.setAdminRemark(adminRemark);
            payLogDao.insert(payLog);
            
        }catch(Exception e){
            return AppUtil.returnObj(2, e.getMessage());
        }
        return AppUtil.returnObj(null);
    }

}
