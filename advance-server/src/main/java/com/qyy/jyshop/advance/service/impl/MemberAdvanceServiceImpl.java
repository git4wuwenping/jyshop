package com.qyy.jyshop.advance.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.advance.service.MemberAdvanceService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class MemberAdvanceServiceImpl extends AbstratService<Member> implements MemberAdvanceService{

    @Autowired
    private MemberDao memberDao;
    
    @Transactional
    @Override
    public Boolean doMemberAdvancePay(BigDecimal payAmount, Long orderId,Integer orderType,String payPwd,String token){
        
        if(StringUtil.isEmpty(token))
            throw new AppErrorException("令牌不能为空...");
        
        synchronized (token) {
            Member member=this.memberDao.selectByPrimaryKey(this.getMemberId(token));
            if(member!=null){
                
                if(StringUtil.isEmpty(member.getPayPassword()))
                    throw new AppErrorException(3, "请先设置支付密码...");
                if(StringUtil.isEmpty(payPwd))
                    throw new AppErrorException("支付密码不能为空...");
                if(!member.getPayPassword().equals(StringUtil.md5(payPwd)))
                    throw new AppErrorException("支付密码错误...");
                if(payAmount.compareTo(new BigDecimal(0))<=0)
                    throw new AppErrorException("支付金额不能不于等于0...");
                if(member.getAdvance()!=null && member.getAdvance().compareTo(payAmount)>=0){
                    
                    int i=this.memberDao.updateMemberAdvanceOfCut(member.getMemberId(), payAmount);
                    if(i==1){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    throw new AppErrorException("预存款不足...");
                }
            }else{
                throw new AppErrorException("获取用户信息失败...");
            }
        }
    }
}
