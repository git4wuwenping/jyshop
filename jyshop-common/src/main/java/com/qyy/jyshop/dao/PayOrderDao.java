package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.qyy.jyshop.model.PayOrder;
import com.qyy.jyshop.supple.MyMapper;

public interface PayOrderDao extends MyMapper<PayOrder>{

    /**
     * 根据支付流水号与支付状态查询支付订单
     * @author hwc
     * @created 2018年1月11日 下午3:49:33
     * @param paySn
     * @param payStatus
     * @return
     */
    PayOrder selectByPaySnAndPayStatus(@Param("paySn")String paySn,@Param("payStatus")Integer payStatus);
    
    /**
     * 修改支付订单状态
     * @author hwc
     * @created 2018年1月11日 下午3:51:59
     * @param payId
     * @param payStatus
     */
    void updatePayStatus(@Param("payId")Long payId,@Param("payStatus")Integer payStatus);
    
    List<Map<String, Object>> selectPayOrderList(@Param("nickname")String nickname,@Param("payType")Integer payType);
    /**
     * 根据会员id查找充值金额（前端接口）
     * @author tonny
     * @created 2018年2月511日 下午2:46:37
     * @param memberId
     * @return
     */
	List<Map<String, Object>> selectPayOrderListByMemberId(@Param("memberId")Long memberId);
}
