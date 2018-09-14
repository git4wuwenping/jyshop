/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.member.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.dao.AddressDao;
import com.qyy.jyshop.dao.MemberAddressDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.member.service.MemberAddressService;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberAddress;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


@Service
public class MemberAddressServiceImpl extends AbstratService<MemberAddress> implements MemberAddressService {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private MemberAddressDao memberAddressDao;

    @Override
    public MemberAddress queryByAddrId(Long addrId,String token){
        if(StringUtil.isEmpty(addrId))
            throw new AppErrorException("地址ID不能为空...");
        Member member=this.getMember(token);
        return this.memberAddressDao.selectByAddrIdAndMemberId(addrId,member.getMemberId());
    }
    
    @Override
    public List<MemberAddress> queryByMemberId(String token){
        Member member=this.getMember(token);
        return this.memberAddressDao.selectByMemberId(member.getMemberId());
    }
    
    @Override 
    @Transactional
    public void doAddMemAddress(MemberAddress memberAddress,String token) {
        
        this.checkInfo(memberAddress);
        
        Member member=this.getMember(token);
        memberAddress.setMemberId(member.getMemberId());
        Example example = new Example(Member.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberId", member.getMemberId());
        
        Integer addressCount=this.memberAddressDao.selectCountByExample(example);
        
        if (addressCount >= 10) {
            throw new AppErrorException("收货地址最多只能添加10个");
        }
        if (addressCount>0) {// 已有收货地址
            
            if(StringUtil.isEmpty(memberAddress.getDefAddr())){
                memberAddress.setDefAddr(0);
            }
            
            if (memberAddress.getDefAddr().equals(1)) {
                this.memberAddressDao.updateNoDefAddr(memberAddress.getMemberId());
            }
        } else {// 没有收货地址把新增的设为默认收货地址
            memberAddress.setDefAddr(1);
        }
        
        
        
        this.memberAddressDao.insert(memberAddress);
    }
    
    @Override
    @Transactional
    public void doEditMemAddress(MemberAddress memberAddress,String token){
        
        this.checkInfo(memberAddress);
        
        Member member=this.getMember(token);
        
        if(StringUtil.isEmpty(memberAddress.getAddrId()))
            throw new AppErrorException("收货地址ID不能为空...");
        
        MemberAddress oldMemberAddress=this.memberAddressDao.selectByPrimaryKey(memberAddress.getAddrId());
        
        if(oldMemberAddress==null)
            throw new AppErrorException("获取原收货地址失败...");
        
        if(!oldMemberAddress.getMemberId().equals(member.getMemberId()))
            throw new AppErrorException("您没有权限修改该收货地址...");
        
        if(!StringUtil.isEmpty(memberAddress.getDefAddr()) &&
                memberAddress.getDefAddr().equals(1)){
            this.memberAddressDao.updateNoDefAddr(member.getMemberId());  
        }
        
        this.memberAddressDao.updateByPrimaryKeySelective(memberAddress);
    }
    
    @Override
    @Transactional
    public void doEditDefAddr(String token,Long addrId) {
        
        Member member=this.getMember(token);
        this.memberAddressDao.updateNoDefAddr(member.getMemberId());  
        this.memberAddressDao.updateDefAddr(addrId, member.getMemberId(), 1);
    }
    
    @Override
    @Transactional
    public void doDelMemberAddress(String token,String addrIds) {
        
        if(StringUtil.isEmpty(addrIds))
            throw new AppErrorException("地址Id不能为空...");
        Member member=this.getMember(token);
        
        List<MemberAddress> memberAddressList = new ArrayList<MemberAddress>();
        MemberAddress memberAddress = null;
        for(String addrId: addrIds.split(",")){
            memberAddress = new MemberAddress();
            memberAddress.setMemberId(member.getMemberId());
            memberAddress.setAddrId(Long.valueOf(addrId));
            memberAddressList.add(memberAddress);
        }
        
        this.memberAddressDao.batchDel(memberAddressList);
    }
    
    /**
     * 验证信息
     * @author hwc
     * @created 2017年11月25日 上午10:22:59
     * @param memberAddress
     */
    private void checkInfo(MemberAddress memberAddress){
        
        if(memberAddress==null)
            throw new AppErrorException("地址信息不能为空...");
        
        if(StringUtil.isEmpty(memberAddress.getProvinceId()))
            throw new AppErrorException("省ID不能为空...");
        else{
            memberAddress.setProvinceName(this.addressDao.selectAddressName(memberAddress.getProvinceId()));
            if(StringUtil.isEmpty(memberAddress.getProvinceName()))
                throw new AppErrorException("获取省名称失败...");
        }
        if(StringUtil.isEmpty(memberAddress.getCityId()))
            throw new AppErrorException("市ID不能为空...");
        else{
            memberAddress.setCityName(this.addressDao.selectAddressName(memberAddress.getCityId()));
            if(StringUtil.isEmpty(memberAddress.getCityName()))
                throw new AppErrorException("获取市名称失败...");
        }
        if(StringUtil.isEmpty(memberAddress.getDistrictId()))
            throw new AppErrorException("县ID不能为空...");
        else{
            memberAddress.setDistrictName(this.addressDao.selectAddressName(memberAddress.getDistrictId()));
            if(StringUtil.isEmpty(memberAddress.getDistrictName()))
                throw new AppErrorException("获取县名称失败...");
        }
        
        if(StringUtil.isEmpty(memberAddress.getName()))
            throw new AppErrorException("收货人不能为空...");
        if(StringUtil.isEmpty(memberAddress.getAddressDetail()))
            throw new AppErrorException("详细地址不能为空...");
//        if(StringUtil.isEmpty(memberAddress.getZip()))
//            throw new AppErrorException("邮编不能为空...");
        if(StringUtil.isEmpty(memberAddress.getTel()) && StringUtil.isEmpty(memberAddress.getMobile()))
            throw new AppErrorException("手机或电话至少填写一个...");
        
        
    }
    
}
