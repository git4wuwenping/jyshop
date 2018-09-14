package com.qyy.jyshop.admin.giftorder.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.pojo.PageAjax;

public interface GiftOrderService {

    /**
     * 查询订单详情
     * 
     * @author hwc
     * @created 2017年12月15日 下午3:28:28
     * @param orderId
     * @return
     */
    public GiftOrder queryOrder(Long orderId);

    /**
     * 查询已支付待分配订单
     * 
     * @author hwc
     * @created 2017年12月19日 上午11:45:38
     * @return
     */
    public List<Long> queryPayOrderId();

    /**
     * 查询订单列表(分页)
     * 
     * @author hwc
     * @created 2017年12月16日 下午4:38:46
     * @param page
     * @return
     */
    public PageAjax<Map<String, Object>> pageOrder(PageAjax<Map<String, Object>> page);

    /**
     * 订单自动收货
     * 
     * @author hwc
     * @created 2018年1月30日 下午3:40:24
     */
    public void doAutomaticReceipt();

    /**
     * 订单自动完成
     * 
     * @author hwc
     * @created 2018年1月30日 下午3:40:15
     */
    public void doAutomaticComplete();

    /**
     * 订单发货
     * 
     * @author hwc
     * @created 2017年12月22日 上午10:15:40
     * @param params
     * @return
     */
    public String doOrderDelivery(Map<String, Object> params);

    /**
     * 订单作废
     * 
     * @author hwc
     * @created 2017年12月21日 下午6:40:35
     * @param orderId
     * @return
     */
    public String doCancleOrder(Long orderId);

    /**
     * 根据会员ID查询订单统计
     */
    public Map<String, Object> getOrderCountByMemberId(Long memberId);

    public List<Map<String, Object>> queryGiftOrderProfit(GiftOrder order, Member member);

    /**
     * 订单自动关闭
     * 
     * @author jiangbin
     * @param hourOrderAutoClose 
     * @created 2018年3月22日 上午9:34:41
     * @param order
     */
    public void autoCloseGiftOrder(GiftOrder giftOrder, BigDecimal hourOrderAutoClose);

    /**
     * 订单自动收货
     * 
     * @author jiangbin
     * @param dayCommonAutoConfirm 
     * @created 2018年3月22日 上午9:34:57
     * @param order
     */
    public void autoConfirmCommonOrder(GiftOrder giftOrder, BigDecimal dayCommonAutoConfirm);

    public void autoCloseOrderLegal(GiftOrder giftOrder, BigDecimal dayLegalAutoClose);
}
