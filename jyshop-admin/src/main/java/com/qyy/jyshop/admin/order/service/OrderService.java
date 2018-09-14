package com.qyy.jyshop.admin.order.service;

import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface OrderService {
    
    /**
     * 查询订单详情
     * @author hwc
     * @created 2017年12月15日 下午3:28:28
     * @param orderId
     * @return
     */
    public Order queryOrder(Long orderId);
    
    /**
     * 查询已支付待分配订单
     * @author hwc
     * @created 2017年12月19日 上午11:45:38
     * @return
     */
    public List<Long> queryPayOrderId();
    
    /**
     * 根据条件查询订单列表(含货品列表)
     * @author hwc
     * @created 2018年3月29日 下午2:30:30
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryOrderList(Map<String,Object> params);
    
    /**
     * 查询订单列表(分页)
     * @author hwc
     * @created 2017年12月16日 下午4:38:46
     * @param page
     * @return
     */
    public PageAjax<Map<String,Object>> pageOrder(PageAjax<Map<String,Object>> page);
    
    /**
     * 拆单
     * @author hwc
     * @created 2017年12月16日 下午4:39:01
     * @param orderId
     */
    public void automaticDistribution(Long orderId);
    
    /**
     * 订单自动收货
     * @author hwc
     * @created 2018年1月30日 下午3:40:24
     */
    public void doAutomaticReceipt();
    
    /**
     * 订单自动完成
     * @author hwc
     * @created 2018年1月30日 下午3:40:15
     */
    public void doAutomaticComplete();
    
    /**
     * 订单发货
     * @author hwc
     * @created 2017年12月22日 上午10:15:40
     * @param params
     * @return
     */
    public String doOrderDelivery(Map<String,Object> params);
    
    /**
     * 批量发货
     * @author hwc
     * @created 2018年3月29日 下午6:03:46
     * @return
     */
    public String  doBatchDelivery();
    
    /**
     * 订单作废
     * @author hwc
     * @created 2017年12月21日 下午6:40:35
     * @param orderId
     * @return
     */
    public String doCancleOrder(Long orderId);
    
    /**
     * 根据会员ID查询订单统计
     */
    public Map<String,Object> getOrderCountByMemberId(Long memberId);

    /**
     * 导出订单数据到excel
     * @param map
     * @param response 
     * @return
     * @throws Exception 
     */
    ExcelData getExportData(Map<String, Object> map, HttpServletResponse response) throws Exception;

	public List<Map<String,Object>> queryOrderProfit(Order order,Member member);

	/**
	 * 订单自动关闭
	 * @author jiangbin
	 * @created 2018年3月22日 上午9:34:41
	 * @param order
	 * @param hourOrderAutoClose 
	 */
    public void autoCloseOrder(Order order, BigDecimal hourOrderAutoClose);

    /**
     * 订单自动收货
     * @author jiangbin
     * @created 2018年3月22日 上午9:34:57
     * @param order
     * @param dayCommonAutoConfirm 
     */
    public void autoConfirmCommonOrder(Order order, BigDecimal dayCommonAutoConfirm);

    /**
     * 已收货订单若干天内不发起维权，系统自动关闭维权功能
     * @author jiangbin
     * @param dayLegalAutoClose 
     */
    public void autoCloseOrderLegal(Order order, BigDecimal dayLegalAutoClose);
}
