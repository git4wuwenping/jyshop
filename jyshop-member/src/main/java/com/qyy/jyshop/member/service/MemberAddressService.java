package com.qyy.jyshop.member.service;

import java.util.List;

import com.qyy.jyshop.model.MemberAddress;


public interface MemberAddressService {
    
    /**
     * 获取用户收货地址详情
     * @author hwc
     * @created 2017年11月25日 上午10:57:25
     * @param addrId
     * @return
     */
    public MemberAddress queryByAddrId(Long addrId,String token);
    
    /**
     * 获取用户收货地址列表
     * @author hwc
     * @created 2017年11月25日 上午10:53:33
     * @param token
     * @return
     */
    public List<MemberAddress> queryByMemberId(String token);
    
    /**
     * 添加用户收货地址
     * @author hwc
     * @created 2017年11月25日 上午10:42:04
     * @param memberAddress
     * @param token
     */
    public void doAddMemAddress(MemberAddress memberAddress,String token);
    
    /**
     * 修改收货地址
     * @author hwc
     * @created 2017年11月25日 下午2:21:58
     * @param memberAddress
     * @param token
     */
    public void doEditMemAddress(MemberAddress memberAddress,String token);
    
    /**
     * 修改默认地址
     * @author hwc
     * @created 2017年11月25日 上午11:49:11
     * @param token
     * @param addrId
     */
    public void doEditDefAddr(String token,Long addrId);
    
    /**
     * 删除收货地址
     * @author hwc
     * @created 2017年11月25日 上午11:39:18
     * @param token
     * @param addrIds
     */
    public void doDelMemberAddress(String token,String addrIds);
}
