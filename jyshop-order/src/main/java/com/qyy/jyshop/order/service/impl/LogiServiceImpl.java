package com.qyy.jyshop.order.service.impl;

import com.qyy.jyshop.dao.BargainDeliveryDao;
import com.qyy.jyshop.dao.DeliveryDao;
import com.qyy.jyshop.dao.GiftDeliveryDao;
import com.qyy.jyshop.dao.SpellDeliveryDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.order.service.LogiService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.HttpRequest;
import com.qyy.jyshop.util.MD5;
import com.qyy.jyshop.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LogiServiceImpl extends AbstratService<Logi> implements LogiService {
    
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private GiftDeliveryDao giftDeliveryDao;
    @Autowired
    private BargainDeliveryDao bargainDeliveryDao;
    @Autowired
    private SpellDeliveryDao spellDeliveryDao;
	
    @Override
	public Map<String, Object> queryLogiDistributionInfo(Long orderId,Integer orderType,String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isEmpty(this.getMember(token))){
			throw new AppErrorException("请先登陆...");
		}
		System.out.println("token============"+token);
		String logiCode = null;
        String logiNo = null;
        String logiName = null;
		if(orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")))){
		    GiftDelivery delivery=giftDeliveryDao.selectLogiByOrderId(orderId);
            if(delivery==null)
                throw new AppErrorException("获取发货信息失败...");
            logiCode = delivery.getLogiCode();
            logiNo = delivery.getLogiNo();
            logiName = delivery.getLogiName();
            
		}else if(orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")))){
		    Delivery delivery=deliveryDao.selectLogiByOrderId(orderId);
		    if(delivery==null)
	            throw new AppErrorException("获取发货信息失败...");
		    logiCode = delivery.getLogiCode();
	        logiNo = delivery.getLogiNo();
	        logiName = delivery.getLogiName();
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
        }else if (orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "bargain_order")))) {
        	BargainDelivery delivery = bargainDeliveryDao.selectLogiByOrderId(orderId);
            if (delivery == null)
                throw new AppErrorException("获取发货信息失败...");
            logiCode = delivery.getLogiCode();
            logiNo = delivery.getLogiNo();
            logiName = delivery.getLogiName();
        }else if (orderType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "spell_order")))) {
            SpellDelivery delivery = spellDeliveryDao.selectLogiByOrderId(orderId);
            if (delivery == null)
                throw new AppErrorException("获取发货信息失败...");
            logiCode = delivery.getLogiCode();
            logiNo = delivery.getLogiNo();
            logiName = delivery.getLogiName();
        }
        else{
		    throw new AppErrorException("错误的订单类型...");
		}
    
    	String param ="{\"com\":\""+logiCode+"\",\"num\":\""+logiNo+"\"}";
        //String param ="{\"com\":\"zhongtong\",\"num\":\"474311891677\"}";
        String customer ="065EE38CB55838D4C58BF447FA9C9F68";
        String key = "gFHvkFcD1808";
        String sign = MD5.encode(param+key+customer);
        HashMap params = new HashMap();
        params.put("param",param);
        params.put("sign",sign);
        params.put("customer",customer);
        String resp;
        map.put("logiNo", logiNo);
        map.put("logiName", logiName);
        try {
            resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
            
            map.put("resp", resp);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
	}

}
