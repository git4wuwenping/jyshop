package com.qyy.jyshop.order.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.GiftOrderItemsDao;
import com.qyy.jyshop.dao.GiftOrderLogDao;
import com.qyy.jyshop.dao.GiftPackageDao;
import com.qyy.jyshop.dao.GiftPackageProductDao;
import com.qyy.jyshop.dao.MemberAddressDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.GiftOrderItems;
import com.qyy.jyshop.model.GiftOrderLog;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberAddress;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.order.service.GiftOrderItemsService;
import com.qyy.jyshop.order.service.GiftOrderService;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.CurrencyUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.JsonUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class GiftOrderServiceImpl extends AbstratService<GiftOrder> implements GiftOrderService{

    
    @Autowired
    private MemberAddressDao memberAddressDao;
    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private GiftOrderItemsDao giftOrderItemsDao;
    @Autowired
    private GiftOrderLogDao giftOrderLogDao;
    @Autowired
    private GiftOrderItemsService giftOrderItemsService;
    @Autowired
    private GiftPackageDao giftPackageDao;
    @Autowired
    private GiftPackageProductDao giftPackageProductDao;
    @Autowired
    private ShopBaseConf shopBaseConf;
    
    @Override
    @Transactional
    public Map<String,Object> getOrderCheckout(String goodsData,String token)throws Exception{
        
//        goodsData="["+
//                "{"+
//                    "\"productId\": 8,"+
//                    "\"buyCound\": 2"+
//                    "\"cartId\": 2"+
//                "},"+
//                "{"+
//                    "\"productId\": 1425,"+
//                    "\"cartId\": 250,"+
//                    "\"buyCound\": 1"+
//                "},"+
//                "{"+
//                    "\"productId\": 1279,"+
//                    "\"cartId\": 245,"+
//                    "\"buyCound\": 1"+
//                "},"+
//                "{"+
//                    "\"productId\": 139,"+
//                    "\"cartId\": 238,"+
//                    "\"buyCound\": 1"+
//                "},"+
//   "]";
//                "{"+
//                    "\"productId\": 2,"+
//                    "\"buyCound\": 2,"+
//                    "\"cartId\": 2"+
//                "},"+
//                "{"+
//                    "\"productId\": 3,"+
//                    "\"buyCound\": 3,"+
//                    "\"cartId\": 3"+
//                "},"+
//                "{"+
//                    "\"productId\": 79,"+
//                    "\"buyCound\": 1"+
//                    "\"cartId\": 4"+
//                "}"+
//            "]";
        System.out.println("goodsData:"+goodsData);
        Map<String,Object> model=new HashMap<String, Object>();
//        token ="yLz6F8wLyBmkeXIdb2HL7A==";
        System.out.println("/////////////////////////////////");
        System.out.println("memberId:"+this.getMemberId(token)+",token:"+token);
        Member member=this.getMember(token);
        System.out.println("请求下单商品:"+goodsData);
        if(goodsData!=null && !goodsData.equals("[]")){//
            JSONArray jsonArray=JSONArray.fromObject(goodsData);
            
            if(jsonArray!=null && jsonArray.size()>0){
                
                List<Map<String,Object>> goodsList=new ArrayList<Map<String,Object>>();
                List<Map<String,Object>> comList=new ArrayList<Map<String,Object>>();
                
                //获取会员收货地址
                List<MemberAddress> memberAdddressList=this.memberAddressDao.selectByMemberId(member.getMemberId());           
                model.put("memberAddressList", memberAdddressList);
                
                Double weight=0D;
                Double goodsTotalPrice=0D;
                System.out.println(jsonArray.size());
                addGoods:for(int i=0;i<jsonArray.size();i++){
                    Map<String,Object> goodsMap=new HashMap<String, Object>();
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Long productId=Long.valueOf(jsonObject.get("productId")==null?null:jsonObject.get("productId").toString());
                    if(productId!=null){
                             
                        goodsMap=this.giftPackageProductDao.selectByGpProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
                        
                        if(goodsMap==null)
                            throw new AppErrorException("获取商品出错,无法下单,可能该商品己下架…………");
                        
                        Integer store=(Integer)goodsMap.get("store");
                     
                        if(StringUtil.isEmpty(store) || store<=0)
                            throw new AppErrorException("商品"+jsonObject.get("name")+"库存不足…………");
                        
                        Integer buyCount=jsonObject.get("buyCount")==null?1:(Integer)jsonObject.get("buyCount");
                        
                        if(buyCount>store){//判断商品购买量是否大于库存,如果购买数量大于库存量,则设置购买量为库存量
                            buyCount=store;
                        }
                        //设置商品购买量
                        goodsMap.put("buyCount", buyCount);
                        goodsMap.put("activityId", jsonObject.get("activityId")==null?0:jsonObject.get("activityId"));
                        goodsMap.put("activityName", "普通商品");
                        goodsMap.put("totalPrice", Double.valueOf(goodsMap.get("price").toString())*buyCount);
                        goodsTotalPrice+=Double.valueOf(goodsMap.get("totalPrice").toString());
                        String activityId=jsonObject.get("activityId")==null?"0":String.valueOf(jsonObject.get("activityId"));
                        
                        goodsMap.put("activityId", activityId);
                        
                        weight+=CurrencyUtil.mul(Double.valueOf(goodsMap.get("weight").toString()),buyCount);
                        
                        goodsList.add(goodsMap);
                        
                        for (Map<String,Object> comMap : comList) {
                            if(!StringUtil.isEmpty(goodsMap.get("comId")) && 
                                    goodsMap.get("comId").toString().equals(
                                    comMap.get("comId").toString())){
                                
                                List<Map<String,Object>> comGoodsList=(List<Map<String,Object>>)comMap.get("goodsList");
                                comGoodsList.add(goodsMap);
                                
                                comMap.put("totalPrice", CurrencyUtil.add(Double.valueOf(comMap.get("totalPrice").toString()), 
                                        Double.valueOf(goodsMap.get("totalPrice").toString())));
                                break addGoods;
                            }
                            
                        }
                        
                        Map<String,Object> comMap=new HashMap<String, Object>();
                        comMap.put("totalPrice", goodsMap.get("totalPrice"));
                        List<Map<String,Object>> comGoodsList=new ArrayList<Map<String,Object>>();
                        comGoodsList.add(goodsMap);
                        comMap.put("goodsList", comGoodsList);
                        comMap.put("goodsCount", comGoodsList.size());
                        comMap.put("shipAmount", 0D);
//                        if(!StringUtil.isEmpty(goodsMap.get("comId")) && 
//                                goodsMap.get("comId").toString().equals("0")){
                            comMap.put("isOwnOne", 1);
                            comMap.put("comName", "自营商品");
                            comMap.put("comId", 0);
//                        }else{
//                            if(!StringUtil.isEmpty(goodsMap.get("comId"))){
//                                comMap.put("isOwnOne", 0);
//                                comMap.put("comId", goodsMap.get("comId"));
//                                comMap.put("comName", this.companyDao.selectComName(
//                                        Integer.valueOf(goodsMap.get("comId").toString())));
//                            }else{
//                                comMap=null;
//                                break ;
//                            }
//                        }
                        
                        comList.add(comMap);
 
                    }
                    
                }
                
                model.put("comList", comList);
                model.put("goodsTotalPrice", goodsTotalPrice);
                model.put("goodsJsonList", JsonUtil.listToJson(goodsList, null).replaceAll("\"", "'"));
                model.put("shipAmount", 0D);
            }
        }
        return model;
    }
    
    @Override
    public Map<String,Object> queryOrderDetails(String token,Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
        
        Map<String,Object> orderMap=new HashMap<String, Object>();
        
        GiftOrder order=this.giftOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            order.setOrderStatusName(DictionaryUtil.getDataLabelByValue("order_status", String.valueOf(order.getOrderStatus())));
            orderMap.put("order", order);
            List<GiftOrderItems> orderItemsList=this.giftOrderItemsDao.selectByOrderId(order.getOrderId());
            orderMap.put("orderItemsList", orderItemsList);
            
            long dd1 = TimestampUtil.getNowTime().getTime(); 
            
            OrderParamConfig orderParamConfig = shopBaseConf.getParamConfig(OrderParamConfig.class, "shopParam_orderParamConfig");
            BigDecimal secondsOrderAutoClose = orderParamConfig.getHourOrderAutoClose().multiply(new BigDecimal(3600));
            Timestamp createTime = order.getCreateTime();
            if(createTime != null){
                long dd2 = createTime.getTime(); 
                BigDecimal seconds = secondsOrderAutoClose.subtract(new BigDecimal((int) (dd1-dd2)/1000)); 
                orderMap.put("autoCloseSeconds", seconds.intValue());
            }else{
                orderMap.put("autoCloseSeconds", null);
            }
            
            Timestamp deliverTime = order.getDeliverTime();
            BigDecimal secondsCommonAutoConfirm = orderParamConfig.getDayCommonAutoConfirm().multiply(new BigDecimal(3600*24));
            if(deliverTime != null){
                long dd3 = deliverTime.getTime(); 
                BigDecimal autoConfirmSeconds = secondsCommonAutoConfirm.subtract(new BigDecimal((int) (dd1-dd3)/1000)); 
                orderMap.put("autoConfirmSeconds", autoConfirmSeconds.intValue());
            }else{
                orderMap.put("autoConfirmSeconds", null);
            }
        }
        
        return orderMap;
    }
    

    @Override
    public PageAjax<Map<String,Object>> pageOrder(String token,Integer orderType,Integer pageNo,Integer pageSize){
        
        Member member=this.getMember(token);
        ParamData params = this.getParamData(pageNo,pageSize);
        params.put("memberId",  member.getMemberId());
        if(!StringUtil.isEmpty(orderType)){
            if(orderType.equals(1)){//待支付
                params.put("orderStatus", DictionaryUtil.getDataValueByName("order_status", "order_not_pay"));
            }else if(orderType.equals(2)){//待发货
                params.put("orderStatusArray", DictionaryUtil.getDataValueByName("order_status", "order_pay")+","+
                        DictionaryUtil.getDataValueByName("order_status", "order_confirm")+","+
                        DictionaryUtil.getDataValueByName("order_status", "order_allocation"));
            }else if(orderType.equals(3)){//待收货
                params.put("orderStatus", DictionaryUtil.getDataValueByName("order_status", "order_ship"));
            }else if(orderType.equals(4)){//待评价
                params.put("orderStatusArray", DictionaryUtil.getDataValueByName("order_status", "order_rog")+","+
                        DictionaryUtil.getDataValueByName("order_status", "order_complete"));
                params.put("evaluateStatus", 0);  
            }
        }
        return this.pageQuery("GiftOrderDao.selectMemberOrderList", params);
    }
    
    
    @Override
    @Transactional
    public GiftOrder doCreateOrder(String goodsList,Long memberAddressId,String cartIds,String token){
        
        //String goodsJsonList="[{'buyCount':1,'productId':1,'totalPrice':111,'goodsId':1,'activityName':'普通商品','weight':1000,'store':100,'specs':'白色、XL','activityId':'0','price':111,'name':'测试商品1','sn':'A001','comId':0,'isOwnOne':1},{'image':'http://192.168.2.246:8080/bm-shop/static/upload/img/20171201/8aec901f-93db-4d6a-9f4b-a93a208c95ee.jpg','buyCount':2,'productId':2,'totalPrice':444,'goodsId':2,'activityName':'普通商品','weight':1000,'store':99,'specs':'白色、M','activityId':'0','price':222,'name':'测试商品2','sn':'A002','comId':1,'isOwnOne':0},{'image':'http://192.168.2.246:8080/bm-shop/static/upload/img/20171201/8aec901f-93db-4d6a-9f4b-a93a208c95ee.jpg','buyCount':3,'productId':3,'totalPrice':999,'goodsId':2,'activityName':'普通商品','weight':1100,'store':222,'specs':'黑色、M','activityId':'0','price':333,'name':'测试商品3','sn':'A003','comId':1,'isOwnOne':0},{'buyCount':4,'productId':4,'totalPrice':1776,'goodsId':4,'activityName':'普通商品','weight':1100,'store':12323,'specs':'黄色、M','activityId':'0','price':444,'name':'测试商品4','sn':'A004','comId':2,'isOwnOne':0}]";
        
//        if(StringUtil.isEmpty(goodsList))
//            throw new AppErrorException("商品列表不能为空...");

        System.out.println("|||||||||||||||||||||||||创建订单");
        System.out.println("memberId:"+this.getMemberId(token)+",token:"+token);
        Member member=this.getMember(token);
        
        GiftOrder order=new GiftOrder();
//        JSONObject jsonObject=JsonUtil.getFromStrToJSONObject(orderData);
        
        //解析商品购买数据 String
//        String goodsJsonList=jsonObject.getString("goodsJsonList");
        BigDecimal goodsAmount=BigDecimal.valueOf(0); // 用于记录商品总价
        Integer goodsBuyCount=0; //用于记录购买数量
        BigDecimal orderAmount=BigDecimal.valueOf(0); //用于记录订单总价
        Long nowDate=System.currentTimeMillis();
        
        Double weight=0D;
        if(!StringUtil.isEmpty(goodsList)){
            
            JSONArray goodsJsonArray=JSONArray.fromObject(goodsList);
            if(goodsJsonArray!=null && goodsJsonArray.size()>0){
                
                for(int i=0;i<goodsJsonArray.size();i++){
                        
                    JSONObject goodsJson= goodsJsonArray.getJSONObject(i);
                    Long productId=Long.valueOf(goodsJson.get("productId")==null?null:goodsJson.get("productId").toString());
                    if(!StringUtil.isEmpty(productId)){
                        
                        Map<String,Object> goodsMap=this.giftPackageProductDao.selectByGpProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
                        
                        if(goodsMap==null)
                            throw new AppErrorException("获取商品出错,无法下单,可能该商品己下架…………");
                            
                            goodsJson.put("goodsId", goodsMap.get("goodsId"));
                            goodsJson.put("productSn", goodsMap.get("sn"));
                            goodsJson.put("goodsName", goodsMap.get("goodsName"));
                            goodsJson.put("original", goodsMap.get("original"));
                            goodsJson.put("specs", goodsMap.get("specs"));
                            goodsJson.put("cost", goodsMap.get("cost"));
                            goodsJson.put("price", goodsMap.get("price"));
                            goodsJson.put("weight", goodsMap.get("weight"));
                            goodsJson.put("unit", goodsMap.get("unit"));                            
                            goodsJson.put("comId", goodsMap.get("comId"));        
                            
                            Integer store=(Integer)goodsMap.get("store");
                         
                            if(StringUtil.isEmpty(store) || store<=0)
                                throw new AppErrorException("商品"+goodsMap.get("name")+"库存不足…………");
                            
                            Integer buyCount=goodsJson.get("buyCount")==null?1:(Integer)goodsJson.get("buyCount");
                            
                            if(buyCount>store)
                                throw new AppErrorException("商品"+goodsMap.get("name")+"库存不足…………");
                            
                            //商品购买价格
                            BigDecimal buyPrice=BigDecimal.valueOf(Double.valueOf(goodsMap.get("price").toString())).
                                    multiply(new BigDecimal(buyCount));
                            
                            goodsAmount=goodsAmount.add(buyPrice); //增加商品价格
                            goodsBuyCount+=buyCount; //增加购买数量
                             
                            if(!StringUtil.isEmpty(goodsMap.get("weight")) && !StringUtil.isEmpty(goodsMap.get("unit"))){
                                if(goodsMap.get("unit").equals("g")){
                                    weight+=CurrencyUtil.mul(Double.valueOf(goodsMap.get("weight").toString()),buyCount);
                                }else if(goodsMap.get("unit").equals("kg")){
                                    weight+=CurrencyUtil.mul(Double.valueOf(goodsMap.get("weight").toString())*1000,buyCount);
                                }
                            }
                            
                            if(StringUtil.isEmpty(goodsJson.get("activityId")) || goodsJson.get("activityId").toString().trim().equals("0")){
                                orderAmount=orderAmount.add(buyPrice);
                            }else{//活动先没写 
                                
                            }
                            
                        }else{
                            throw new AppErrorException("获取下单商品数据出错...");
                        }
                    }
                    
                    order.setGoodsAmount(goodsAmount);
                    order.setGoodsCount(goodsJsonArray.size()); //订单商品数量(包含赠品)
                    order.setBuyCount(goodsBuyCount);
                    order.setWeight(new BigDecimal(weight));
                    order.setMemberId(member.getMemberId());
                    
                    order.setShipAmount(new BigDecimal(0D));
                    
                    order.setOrderAmount(orderAmount);
                    order.setOrderSn(String.valueOf(nowDate));
                    order.setCreateTime(new Timestamp(nowDate));
                    
                    //获取收货地址
                    if(StringUtil.isEmpty(memberAddressId))
                        throw new AppErrorException("收货地址Id不能为空...");
//                    Long memberAddressId=jsonObject.getLong("memberAddressId");
                    MemberAddress memberAddress =this.memberAddressDao.selectByAddrIdAndMemberId(memberAddressId,member.getMemberId());
                    if(memberAddress!=null){
                        order.setShipId(1);
                        order.setShipType("快递");
                        order.setShipAddress(memberAddress.getProvinceName()+memberAddress.getCityName()+
                                memberAddress.getDistrictName()+memberAddress.getAddressDetail());
                        order.setShipProvinceId(memberAddress.getProvinceId());
                        order.setShipProvinceName(memberAddress.getProvinceName());
                        order.setShipCityId(memberAddress.getCityId());
                        order.setShipCityName(memberAddress.getCityName());
                        order.setShipDistrictId(memberAddress.getDistrictId());
                        order.setShipDistrictName(memberAddress.getDistrictName());
                        order.setShipName(memberAddress.getName());
                        order.setShipAddr(memberAddress.getAddressDetail());
                        order.setShipZip(memberAddress.getZip());
                        order.setShipMobile(memberAddress.getMobile());
                        order.setShipTel(memberAddress.getTel());
                        order.setShipDay("工作日周未都可配送");
                    }else
                        throw new AppErrorException("获取收货地址失败...");
                    
                    this.giftOrderDao.insertSelective(order);
                    
                    
                    System.out.println("|||||||||||||||||||||||||创建订单");
                    System.out.println("orderId:"+order.getOrderId()+"memberId:"+this.getMemberId(token)+",token:"+token);
                    
                    //添加订单货品
                    giftOrderItemsService.doAddOrderItems(goodsJsonArray,order);
            }
        }else{
            throw new AppErrorException("获取下单商品数据出错...");
        }
            
        //添加日志
        GiftOrderLog orderLog=new GiftOrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderSn(order.getOrderSn());
        orderLog.setMessage("用户创建商品订单...");
        orderLog.setOpId(member.getMemberId());
//        orderLog.setOpUser(member.getUname());
        orderLog.setCreationTime(new Timestamp(nowDate));
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "user")));
        giftOrderLogDao.insert(orderLog);
        
        return this.giftOrderDao.selectMemberOrder(order.getOrderId(), member.getMemberId());
   
    }
    
    
    @Override
    public void doOrderCancel(String token,Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
        
        GiftOrder order=this.giftOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            
            if(DictionaryUtil.getDataValueByName("order_status","order_not_pay").
                    equals(String.valueOf(order.getOrderStatus())) &&
               DictionaryUtil.getDataValueByName("pay_status","pay_no").
                    equals(String.valueOf(order.getPayStatus()))){
            	order.setOrderId(orderId);
            	order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_cancel")));
            	order.setDeleteFlag(1);
            	this.update(order);
            }else
                throw new AppErrorException("该订单不是待付款订单不能进行取消订单操作..."); 
            
        }else
            throw new AppErrorException("该订单不存在...");
    }
    
    @Override
    public void doOrderRog(String token,Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
        
        GiftOrder order=this.giftOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            
            if(DictionaryUtil.getDataValueByName("order_status","order_ship").
                    equals(String.valueOf(order.getOrderStatus())) &&
               DictionaryUtil.getDataValueByName("pay_status","pay_yes").
                    equals(String.valueOf(order.getPayStatus())) &&
               DictionaryUtil.getDataValueByName("ship_status","ship_yes").
                    equals(String.valueOf(order.getShipStatus()))){
                
                GiftOrder updateOrder=new GiftOrder();
                updateOrder.setOrderId(order.getOrderId());
                updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
                updateOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status","ship_receipt")));
                updateOrder.setSigningTime(new Timestamp(System.currentTimeMillis()));
                this.update(updateOrder);
                System.out.println("订单确认收货...");
            }else
                throw new AppErrorException("订单状态错误,收货失败..."); 
            
        }else
            throw new AppErrorException("该订单不存在...");
    }
    
    @Override
    public void doDelOrder(String token,Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
        
        GiftOrder order=this.giftOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            //只有作废订单,已取消订单,己完成订单才能删除
            if(DictionaryUtil.getDataValueByName("order_status","order_cancel").
                    equals(String.valueOf(order.getOrderStatus())) || 
               DictionaryUtil.getDataValueByName("order_status","order_complete").
                    equals(String.valueOf(order.getOrderStatus())) || 
               DictionaryUtil.getDataValueByName("order_status","order_returnlation").
                    equals(String.valueOf(order.getOrderStatus()))){
                
                this.giftOrderDao.updateDeleteFlag(orderId);
            }else
                throw new AppErrorException("该状态的订单不是删除..."); 
            
        }else
            throw new AppErrorException("该订单不存在...");
    }
    
    
    @Override
    @Transactional
    public void doOrderMaintain(String token,Long orderId){
        
        GiftOrder order=this.giftOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
        if(order!=null){
            this.giftOrderDao.updateOrderStatus(orderId, -2, null, null);
        }else{
            throw new AppErrorException("该订单不存在...");
        }
    }

	@Override
	public List<Map<String, Object>> orderCount(String token) {
		return giftOrderDao.orderCount(this.getMemberId(token));
	}
}
