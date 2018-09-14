package com.qyy.jyshop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.MemberBargain;
import com.qyy.jyshop.supple.MyMapper;

public interface MemberBargainDao extends MyMapper<MemberBargain>{

    /**
     * 获取订单砍价记录
     * @author hwc
     * @created 2018年4月13日 上午10:11:52
     * @param orderId
     * @return
     */
    List<MemberBargain> selectByOrderId(@Param("orderId")Long orderId);

    /**
     * 批量删除
     * @param list
     */
    void batchDel(List<MemberBargain> list);
}
