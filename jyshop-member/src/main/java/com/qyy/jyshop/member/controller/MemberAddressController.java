package com.qyy.jyshop.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.member.service.MemberAddressService;
import com.qyy.jyshop.model.MemberAddress;


/**
 * 用户收货地址
 * @author hwc
 * @created 2017年11月25日 下午2:34:22
 */
@RestController
public class MemberAddressController extends AppBaseController{

    
    @Autowired
    private MemberAddressService memberAddressService; 
    
    /**
     * 获取收货地址详情
     * @author hwc
     * @created 2017年11月25日 下午2:42:32
     * @param token
     * @param addrId
     * @return
     */
    @RequestMapping(value = "queryMemAddress")
    public Map<String,Object> queryMemAddress(String token,Long addrId){

        MemberAddress memberAddress=this.memberAddressService.queryByAddrId(addrId, token);
        
        Map<String,Object> returnMap=new HashMap<String, Object>();
        returnMap.put("memberAddress", memberAddress);
        
        return this.outMessage(0, "获取成功", returnMap);

    }
    
    /**
     * 查询用户收货地址列表
     * @author hwc
     * @created 2017年11月25日 下午2:41:21
     * @param token
     * @return
     */
    @RequestMapping(value = "queryMemAddressList")
    public Map<String,Object> queryMemAddressList(String token){

        List<MemberAddress> memberAddressList=this.memberAddressService.queryByMemberId(token);
        
        Map<String,Object> returnMap=new HashMap<String, Object>();
        returnMap.put("memberAddressList", memberAddressList);
        
        return this.outMessage(0, "获取成功", returnMap);
      
    }
    
    
    /**
     * 添加收货地址
     * @author hwc
     * @created 2017年11月25日 下午2:37:07
     * @param token
     * @param memberAddress
     * @return
     */
    @RequestMapping(value = "addMemAddress")
    public Map<String,Object> addMemAddress(String token,MemberAddress memberAddress){

        this.memberAddressService.doAddMemAddress(memberAddress, token);
        return this.outMessage(0, "添加成功", null);
      
    }

    /**
     * 设置默认地址
     * @author hwc
     * @created 2017年11月25日 下午2:40:07
     * @param token
     * @param addrId
     * @return
     */
    @RequestMapping(value = "editDefAddr")
    public Map<String,Object> editDefAddr(String token,Long addrId){

        this.memberAddressService.doEditDefAddr(token, addrId);
        return this.outMessage(0,"设置成功", null);
      
    }
    
    /**
     * 修改收货地址
     * @author hwc
     * @created 2017年11月25日 下午2:38:17
     * @param token
     * @param memberAddress
     * @return
     */
    @RequestMapping(value = "editMemAddress")
    public Map<String,Object> editMemAddress(String token,MemberAddress memberAddress){

        this.memberAddressService.doEditMemAddress(memberAddress, token);
        return this.outMessage(0, "修改成功", null);
      
    }
    
    /**
     * 删除收货地址
     * @author hwc
     * @created 2017年11月25日 下午2:39:00
     * @param token
     * @param addrId
     * @return
     */
    @RequestMapping(value = "delMemAddress")
    public Map<String,Object> delMemAddress(String token,String addrId){

        this.memberAddressService.doDelMemberAddress(token, addrId);
        return this.outMessage(0, "删除成功", null);
      
    }
}
