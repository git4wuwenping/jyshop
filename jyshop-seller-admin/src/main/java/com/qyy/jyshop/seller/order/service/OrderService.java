package com.qyy.jyshop.seller.order.service;

import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;

import java.util.List;
import java.util.Map;

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
     * 查询订单列表(分页)
     * @author hwc
     * @created 2017年12月16日 下午4:38:46
     * @param page
     * @return
     */
    public PageAjax<Map<String,Object>> pageOrder(PageAjax<Map<String,Object>> page);
    
    /**
     * 订单发货
     * @author hwc
     * @created 2017年12月22日 上午10:15:40
     * @param params
     * @return
     */
    public String doOrderDelivery(Map<String,Object> params);
    
    /**
     * 订单作废
     * @author hwc
     * @created 2017年12月21日 下午6:40:35
     * @param orderId
     * @return
     */
    public String doCancleOrder(Long orderId);

    /**
     * 导出订单到Excel
     * @param map
     * @return
     */
    ExcelData getExportData(Map<String, Object> map);
}
