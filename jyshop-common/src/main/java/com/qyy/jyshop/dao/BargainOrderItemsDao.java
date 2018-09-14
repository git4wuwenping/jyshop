package com.qyy.jyshop.dao;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.BargainOrderItems;
import com.qyy.jyshop.supple.MyMapper;

import java.util.List;

public interface BargainOrderItemsDao  extends MyMapper<BargainOrderItems>{

    /**
     * 查询用户指定砍价任务数量
     * @author hwc
     * @created 2018年4月12日 上午11:03:14
     * @param bargainId
     * @param memberId
     * @return
     */
    Integer selectCountByMemberIdAndBargainId(@Param("bargainId")Long bargainId,
            @Param("memberId")Long memberId,
            @Param("memberIds")Long memberIds);
    
    /**
     * 查询指定砍价任务的完成数量
     * @author hwc
     * @created 2018年4月12日 下午3:42:54
     * @param bargainId
     * @return
     */
    Integer selectCountByCompleteAndBargainId(@Param("bargainId")Long bargainId);
    
    /**
     * 根据订单ID获取砍价详情
     * @author hwc
     * @created 2018年4月13日 下午4:32:11
     * @param orderId
     * @return
     */
    BargainOrderItems selectByOrderId(@Param("orderId")Long orderId);

    /**
     * 批量删除
     * @param list
     */
    void batchDel(List<BargainOrderItems> list);
}
