package com.qyy.jyshop.order.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.pojo.PageAjax;

public interface GiftOrderService {

    /**
     * 获取结算数据
     * @author hwc
     * @created 2017年12月12日 下午2:33:25
     * @param goodsData
     * @param token
     * @return
     * @throws Exception
     */
    public Map<String,Object> getOrderCheckout(String goodsData,String token)throws Exception;

    /**
     * 获取订单详情
     * @author hwc
     * @created 2017年12月20日 下午3:18:30
     * @param token
     * @param orderId
     * @return
     */
    public Map<String,Object> queryOrderDetails(String token,Long orderId);
    
    /**
     * 获取用户订单列表(分页)
     * @author hwc
     * @created 2017年12月20日 上午10:48:21
     * @param token
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageAjax<Map<String,Object>> pageOrder(String token,Integer orderType,Integer pageNo,Integer pageSize);
    
    
    /**
     * 创建订单
     * @author hwc
     * @created 2017年12月20日 上午10:23:32
     * @param orderData
     * @param token
     * @return
     */
    public GiftOrder doCreateOrder(String goodsList,Long memberAddressId,String cartIds,String token);
    
    /**
     * 取消订单
     * @author hwc
     * @created 2017年12月20日 下午3:49:11
     * @param token
     * @param orderId
     */
    public void doOrderCancel(String token,Long orderId);
    
    /**
     * 确定收货
     * @author hwc
     * @created 2017年12月20日 下午4:21:29
     * @param token
     * @param orderId
     */
    public void doOrderRog(String token,Long orderId);
    
    /**
     * 删除订单
     * @author hwc
     * @created 2017年12月20日 下午4:20:40
     * @param token
     * @param orderId
     */
    public void doDelOrder(String token,Long orderId);
    
    public void doOrderMaintain(String token,Long orderId);

	public List<Map<String,Object>> orderCount(String token);
}
