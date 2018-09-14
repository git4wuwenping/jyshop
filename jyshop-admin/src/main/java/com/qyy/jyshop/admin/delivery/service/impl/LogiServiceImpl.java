package com.qyy.jyshop.admin.delivery.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.delivery.service.LogiService;
import com.qyy.jyshop.dao.DeliveryDao;
import com.qyy.jyshop.dao.LogiDao;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.HttpRequest;
import com.qyy.jyshop.util.MD5;
import com.qyy.jyshop.util.StringUtil;


@Service
public class LogiServiceImpl extends AbstratService<Logi> implements LogiService{

    @Autowired
    private LogiDao logiDao;
    
    @Override
    @ServiceLog("获取物流公司详情")
    public Logi queryByLogiId(Integer logiId) {
        return this.logiDao.selectByPrimaryKey(logiId);
    }
    
    @Override
    @ServiceLog("获取物流配送信息")
    public Map<String,Object> queryLogiDistributionInfo(String logiName,String logiCode,String logiNo){
//    	Delivery delivery = deliveryDao.selectLogiByOrderId(orderId);
    	/*String logiCode = delivery.getLogiCode();
    	String logiNo = delivery.getLogiNo();*/
//    	Map<String, Object> map = new HashMap<String, Object>();
//    	map.put("resp", "");
//    	map.put("logiName", "");
//    	if(delivery!=null){
    		Map<String, Object> map = new HashMap<String, Object>();
//	    	String logiName = delivery.getLogiName();
	    	String param ="{\"com\":\""+logiCode+"\",\"num\":\""+logiNo+"\"}";
//	        String param ="{\"com\":\"zhongtong\",\"num\":\"474311891677\"}";
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
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return map;
//    	}
//    	return null;
    }
    
    
    @Override
    @ServiceLog("获取物流公司详情")
    public List<Logi> queryLogi(){
        return this.queryAll();
    }
    
    @Override
    @ServiceLog("获取物流公司列表(分页)")
    public PageAjax<Logi> pageLogi(PageAjax<Logi> page, Logi Logi) {
        return this.queryPage(page, Logi);
    }

    @Override
    @Transactional
    @ServiceLog("添加物流公司")
    public String doAddLogi(Logi logi) {
        if(logi==null)
            return "添加的物流数据不能为空...";
        if(StringUtil.isEmpty(logi.getLogiCode()))
            return "物流编号不能为空 ...";
        if(StringUtil.isEmpty(logi.getLogiName()))
            return "物流名称不能为空 ...";
        
        if(this.logiDao.selectByLogiCode(logi.getLogiCode())!=null)
            return "物流代码己存在,请换一个物流代码添加...";
        if(this.logiDao.selectByLogiName(logi.getLogiName())!=null)
            return "物流名称己存在,请换一个物流名称添加...";
        this.logiDao.insert(logi);
        return null;
    }

    @Override
    @Transactional
    @ServiceLog("修改物流公司")
    public String doEditLogi(Logi logi) {
        
        if(logi==null)
            return "添加的物流数据不能为空...";
        if(StringUtil.isEmpty(logi.getLogiCode()))
            return "物流编号不能为空 ...";
        if(StringUtil.isEmpty(logi.getLogiName()))
            return "物流名称不能为空 ...";
        
        Logi oldLogi=this.logiDao.selectByPrimaryKey(logi.getLogiId());
        if(oldLogi==null)
            return "获取物流信息失败 ...";
        
        Logi logistics=this.logiDao.selectByLogiCode(logi.getLogiCode());
        if(logistics!=null && !logistics.getLogiId().equals(logi.getLogiId()))
            return "物流代码己存在,请换一个物流代码添加...";
        logistics=this.logiDao.selectByLogiName(logi.getLogiName());
        if(logistics!=null && !logistics.getLogiId().equals(logi.getLogiId()))
            return "物流名称己存在,请换一个物流名称添加...";
        
        this.logiDao.updateByPrimaryKeySelective(logi);
        
        return null;
    }

    @Override
    @Transactional
    @ServiceLog("删除物流公司")
    public String doDelLogi(Integer logiId) {
        if(StringUtil.isEmpty(logiId))
            return "物流Id不能为空 ...";
        if(this.logiDao.deleteByPrimaryKey(logiId)>0)
            return null;
        else
            return "物流信息不存在,删除失败...";
    }

}
