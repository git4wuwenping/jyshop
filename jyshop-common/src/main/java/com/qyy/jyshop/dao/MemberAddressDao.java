package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.MemberAddress;
import com.qyy.jyshop.supple.MyMapper;


public interface MemberAddressDao extends MyMapper<MemberAddress>{
    
    /**
     * 查询默认地址省ID 
     * @author hwc
     * @created 2018年2月3日 下午5:31:37
     * @param memberId
     * @return
     */
    String selectDefOfProvinceId(@Param("memberId")Long memberId);
    
    /**
     * 查询默认地址市ID 
     * @author hwc
     * @created 2018年4月24日 上午9:50:32
     * @param memberId
     * @return
     */
    String selectDefOfCityId(@Param("memberId")Long memberId);
    
    /**
     * 查询地址省ID
     * @author hwc
     * @created 2018年2月7日 下午9:32:29
     * @param memberId
     * @return
     */
    String selectOfProvinceId(@Param("addrId")Long addrId);
    
    /**
     * 查询地址市ID
     * @author hwc
     * @created 2018年4月24日 上午9:50:52
     * @param addrId
     * @return
     */
    String selectOfCityId(@Param("addrId")Long addrId);
    
    /**
     * 获取用户收货地址详情
     * @author hwc
     * @created 2017年11月25日 上午10:59:09
     * @param addrId
     * @param memberId
     * @return
     */
    MemberAddress selectByAddrIdAndMemberId(@Param("addrId")Long addrId,@Param("memberId")Long memberId);
    
    /**
     * 获取用户收货地址
     * @author hwc
     * @created 2017年11月25日 上午10:45:51
     * @param memberId
     * @return
     */
    List<MemberAddress> selectByMemberId(@Param("memberId")Long memberId);
    
    /**
     * 修改用户的所有收货地址为非默认地址
     * @author hwc
     * @created 2017年11月25日 上午10:37:24
     * @param memberId
     */
    void updateNoDefAddr(@Param("memberId")Long memberId);
    
    /**
     * 修改默认地址
     * @author hwc
     * @created 2017年11月25日 上午11:47:19
     * @param addrId
     * @param defAddr
     */
    void updateDefAddr(@Param("addrId")Long addrId,@Param("memberId")Long memberId,@Param("defAddr")Integer defAddr);
    
    /**
     * 删除收货地址
     * @author hwc
     * @created 2017年11月25日 上午11:40:19
     * @param addrId
     * @param memberId
     */
    void deleteMemberAddress(@Param("addrId")Long addrId,@Param("memberId")Long memberId);
    
    /**
     * 批量删除收货地址
     * @author hwc
     * @created 2017年11月27日 上午9:49:29
     * @param MemberAddressList
     */
    void batchDel(List<MemberAddress> MemberAddressList);
}
