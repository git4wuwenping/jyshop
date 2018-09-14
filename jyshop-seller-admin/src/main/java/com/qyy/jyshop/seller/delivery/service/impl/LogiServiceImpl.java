package com.qyy.jyshop.seller.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.DeliveryDao;
import com.qyy.jyshop.dao.GiftDeliveryDao;
import com.qyy.jyshop.dao.LogiDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.GiftDelivery;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.delivery.service.LogiService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.HttpRequest;
import com.qyy.jyshop.util.MD5;
import com.qyy.jyshop.util.StringUtil;


@Service
public class LogiServiceImpl extends AbstratService<Logi> implements LogiService{

    @Autowired
    private LogiDao logiDao;
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private GiftDeliveryDao giftDeliveryDao;
    
    @Override
    @ServiceLog("获取物流配送信息")
    public Map<String,Object> queryLogiDistributionInfo(Long orderId,Integer orderType){
        
        String logiCode = null;
        String logiNo = null;
        String logiName = null;
    	if(orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")))){
            GiftDelivery delivery=giftDeliveryDao.selectLogiByOrderId(orderId);
            if(delivery!=null){
                logiCode = delivery.getLogiCode();
                logiNo = delivery.getLogiNo();
                logiName = delivery.getLogiName();
            }
            
        }else if(orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")))){
            Delivery delivery=deliveryDao.selectLogiByOrderId(orderId);
            if(delivery!=null){
                logiCode = delivery.getLogiCode();
                logiNo = delivery.getLogiNo();
                logiName = delivery.getLogiName();
            }
        }else if (orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "right_order")))) {
            Delivery delivery = new Delivery();
            delivery.setOrderId(orderId);
            delivery.setType(2);
            delivery = deliveryDao.selectOne(delivery);
            if (delivery == null)
                throw new AppErrorException("获取发货信息失败...");
            logiCode = delivery.getLogiCode();
            logiNo = delivery.getLogiNo();
            logiName = delivery.getLogiName();
        } else{
            throw new AppErrorException("错误的订单类型...");
        }
    	if(StringUtil.isEmpty(logiCode))
    	    return null;
    	String param ="{\"com\":\""+logiCode+"\",\"num\":\""+logiNo+"\"}";
    	Map<String, Object> map = new HashMap<String, Object>();
	    String customer ="065EE38CB55838D4C58BF447FA9C9F68";
	    String key = "gFHvkFcD1808";
	    String sign = MD5.encode(param+key+customer);
	    HashMap params = new HashMap();
	    params.put("param",param);
	    params.put("sign",sign);
	    params.put("customer",customer);
	    String resp;
	    map.put("logiName", logiName);
	    try {
	        resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
	        map.put("resp", resp);//JSONObject.parseObject(resp)
	        System.out.println(resp);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return map;
    }
    
    @Override
    @ServiceLog("获取物流公司详情")
    public List<Logi> queryLogi(){
        return this.queryAll();
    }
    
}
