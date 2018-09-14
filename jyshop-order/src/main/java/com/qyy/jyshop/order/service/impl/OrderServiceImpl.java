package com.qyy.jyshop.order.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.conf.ShopBaseConf;
import com.qyy.jyshop.dao.CartDao;
import com.qyy.jyshop.dao.CompanyDao;
import com.qyy.jyshop.dao.MemberAddressDao;
import com.qyy.jyshop.dao.OrderCheckoutDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.dao.OrderLogDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Cart;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberAddress;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.model.OrderParamConfig;
import com.qyy.jyshop.order.service.DlyTypeService;
import com.qyy.jyshop.order.service.OrderItemsService;
import com.qyy.jyshop.order.service.OrderService;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.CurrencyUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.JsonUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class OrderServiceImpl extends AbstratService<Order> implements OrderService{

    
    @Autowired
    private MemberAddressDao memberAddressDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired 
    private OrderDao orderDao;
    @Autowired
    private OrderItemsDao orderItemsDao;
    @Autowired
    private OrderLogDao orderLogDao;
    @Autowired
    private OrderCheckoutDao orderCheckoutDao;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private DlyTypeService dlyTypeService;
    @Autowired
    private ShopBaseConf shopBaseConf;
    
    @Override
    public List<Map<String,Object>> computeOrderFreight(String goodsList,Long memberAddressId,String token){
        
 //       memberAddressId=211L;
//        goodsList="[{'image':'http://192.168.2.250:8082/static/upload/img/20180329/7f5ccdfa-0edb-48a7-95d1-6501962ed3a7.jpg','buyCount':1,'cost':0.01,'productId':3396,'totalPrice':0.02,'goodsId':1148,'dlyTypeId':45,'activityName':'普通商品','weight':5000,'store':5,'goodsType':0,'specs':'灰色大盘,双色卡','activityId':0,'unit':'g ','price':0.02,'name':'直测试000','sn':'801600100120000010001001','comId':0}]";
        
        System.out.println(memberAddressId);
        if(StringUtil.isEmpty(goodsList))
            throw new AppErrorException("商品列表不能为空...");
        
        JSONArray goodsJsonArray=JSONArray.fromObject(goodsList);
        
        if(goodsJsonArray!=null && goodsJsonArray.size()>0){
            
            List<Map<String,Object>> comList=new ArrayList<Map<String,Object>>();
            addGoods:for(int i=0;i<goodsJsonArray.size();i++){
                
                JSONObject goodsJson= goodsJsonArray.getJSONObject(i);
                
                for (Map<String,Object> comMap : comList) {
                    if(!StringUtil.isEmpty(goodsJson.get("comId")) && 
                            goodsJson.get("comId").toString().equals(comMap.get("comId").toString()) &&
                            goodsJson.get("goodsType").toString().equals(comMap.get("goodsType").toString())){
                        
                        Map<String,Object> goodsMap=new HashMap<String, Object>();
                        goodsMap.put("dlyTypeId", goodsJson.get("dlyTypeId"));
                        goodsMap.put("buyCount", goodsJson.get("buyCount"));
                        goodsMap.put("weight", goodsJson.get("weight"));
                        @SuppressWarnings("unchecked")
                        List<Map<String,Object>> comGoodsList=(List<Map<String,Object>>)comMap.get("goodsList");
                        comGoodsList.add(goodsMap);
                        continue addGoods;
                    }
                }
                
                Map<String,Object> comMap=new HashMap<String, Object>();
                List<Map<String,Object>> comGoodsList=new ArrayList<Map<String,Object>>();
                Map<String,Object> goodsMap=new HashMap<String, Object>();
                goodsMap.put("dlyTypeId", goodsJson.get("dlyTypeId"));
                goodsMap.put("buyCount", goodsJson.get("buyCount"));
                goodsMap.put("weight", goodsJson.get("weight"));
                comGoodsList.add(goodsMap);
                comMap.put("goodsList", comGoodsList);
                comMap.put("comId", goodsJson.get("comId"));
                comMap.put("goodsType", goodsJson.get("goodsType"));
                comList.add(comMap);
            }
            
            //计算运费
            for (Map<String,Object> comMap : comList) {
                BigDecimal comShipAmount=this.dlyTypeService.computeFreight((List<Map<String,Object>>)comMap.get("goodsList"),
                        this.memberAddressDao.selectOfCityId(memberAddressId),token);
                comMap.put("shipAmount", comShipAmount);
            }
            
            return comList;
        }
        return null;
    }
    
    @Override
    @Transactional
    public Map<String,Object> getOrderCheckout(String goodsData,String token)throws Exception{

//        goodsData="["+
//                   "{"+
//                       "\"productId\": 3396,"+
//                       "\"buyCound\": 2"+
////                       "\"cartId\": 2"+
//                   "}"+
//                   "{"+
//                       "\"productId\": 3402,"+
////                       "\"cartId\": 250,"+
//                       "\"buyCound\": 1"+
//                   "},"+
//                   "{"+
//                       "\"productId\": 3408,"+
////                       "\"cartId\": 245,"+
//                       "\"buyCound\": 1"+
//                   "}"+
//                   "{"+
//                       "\"productId\": 2704,"+
////                       "\"cartId\": 238,"+
//                       "\"buyCound\": 1"+
//                   "},"+
//                   "{"+
//                       "\"productId\": 3284,"+
//                       "\"buyCound\": 2"+
//                   "}"+
//               "]";
        System.out.println("goodsData:"+goodsData);
        Map<String,Object> model=new HashMap<String, Object>();
//        token ="CvMEPRypOg1mZ7ApSoCEHA==";
        System.out.println("/////////////////////////////////");
        System.out.println("memberId:"+this.getMemberId(token)+",token:"+token);
        Member member=this.getMember(token);
        System.out.println("请求下单商品:"+goodsData);
        Integer orderType=0;
        if(goodsData!=null && !goodsData.equals("[]")){//
            JSONArray jsonArray=JSONArray.fromObject(goodsData);
            
            if(jsonArray!=null && jsonArray.size()>0){
                
                //用于组装购买的商品
                List<Map<String,Object>> goodsList=new ArrayList<Map<String,Object>>();
                //用于组装商家列表
                List<Map<String,Object>> comList=new ArrayList<Map<String,Object>>();
                //用于组装购物车Id
                StringBuffer cartIds=new StringBuffer();
                
                //获取会员收货地址
                List<MemberAddress> memberAdddressList=this.memberAddressDao.selectByMemberId(member.getMemberId());           
                model.put("memberAddressList", memberAdddressList);
                
                //用于
                BigDecimal goodsTotalPrice=new BigDecimal(0);
                BigDecimal shipTotalAmount=new BigDecimal(0);
                System.out.println("购买的商品数量:"+jsonArray.size());
                addGoods:for(int i=0;i<jsonArray.size();i++){
                    Map<String,Object> goodsMap=new HashMap<String, Object>();
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Long productId=Long.valueOf(jsonObject.get("productId")==null?null:jsonObject.get("productId").toString());
                    if(productId!=null){
                        
                        if(!StringUtil.isEmpty(jsonObject.get("cartId"))){
                            if(i==0){
                                cartIds.append(jsonObject.get("cartId"));
                            }else{
                                cartIds.append(","+jsonObject.get("cartId"));
                            }
                        }
                        
                        goodsMap=this.productDao.selectByProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
                        
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
                        goodsMap.put("totalPrice", CurrencyUtil.mul(Double.valueOf(goodsMap.get("price").toString()),buyCount));
                        goodsTotalPrice=goodsTotalPrice.add(new BigDecimal(goodsMap.get("totalPrice").toString()));
                        goodsList.add(goodsMap);
                        
                        for (Map<String,Object> comMap : comList) {
                            if(!StringUtil.isEmpty(goodsMap.get("comId")) && 
                                goodsMap.get("comId").toString().equals(comMap.get("comId").toString())  &&
                                goodsMap.get("goodsType").toString().equals(comMap.get("goodsType").toString())){
                                
                                @SuppressWarnings("unchecked")
                                List<Map<String,Object>> comGoodsList=(List<Map<String,Object>>)comMap.get("goodsList");
                                comGoodsList.add(goodsMap);
                                
                                comMap.put("totalPrice", CurrencyUtil.add(Double.valueOf(comMap.get("totalPrice").toString()), 
                                        Double.valueOf(goodsMap.get("totalPrice").toString())));
                                comMap.put("totalWeight", CurrencyUtil.add(Double.valueOf(comMap.get("totalWeight").toString()), 
                                        CurrencyUtil.mul(Double.valueOf(goodsMap.get("weight").toString()),
                                                buyCount)));
                                continue addGoods;
                            }
                            
                        }
                        
                        Map<String,Object> comMap=new HashMap<String, Object>();
                        comMap.put("totalPrice", goodsMap.get("totalPrice"));
                        comMap.put("totalWeight", CurrencyUtil.mul(Double.valueOf(goodsMap.get("weight").toString()),
                                buyCount));
                        List<Map<String,Object>> comGoodsList=new ArrayList<Map<String,Object>>();
                        comGoodsList.add(goodsMap);
                        comMap.put("goodsType", goodsMap.get("goodsType"));
                        if(goodsMap.get("goodsType").toString().equals("1")){
                            orderType=1;
                        }
                        comMap.put("goodsList", comGoodsList);
                        comMap.put("goodsCount", comGoodsList.size());
                        if(!StringUtil.isEmpty(goodsMap.get("comId")) && 
                                goodsMap.get("comId").toString().equals("0")){
                            comMap.put("isOwnOne", 1);
                            comMap.put("comName", this.companyDao.selectStoreName(0));
                            comMap.put("comId", 0);
                        }else{
                            if(!StringUtil.isEmpty(goodsMap.get("comId"))){
                                comMap.put("isOwnOne", 0);
                                comMap.put("comId", goodsMap.get("comId"));
                                comMap.put("comName", this.companyDao.selectStoreName(
                                        Integer.valueOf(goodsMap.get("comId").toString())));
                            }else{
                                comMap=null;
                                break ;
                            }
                        }
                        
                        comList.add(comMap);
 
                    }
                    
                }
                
                //记录购物车Id以便删除购物车商品
                model.put("cartIds", cartIds);
                model.put("comList", comList);
                model.put("orderType", orderType);
                model.put("goodsTotalPrice", goodsTotalPrice);
                model.put("goodsJsonList", JsonUtil.listToJson(goodsList, null).replaceAll("\"", "'"));
                model.put("comJsonList", JsonUtil.listToJson(comList, null).replaceAll("\"", "'"));
                //计算运费
                if(comList!=null && comList.size()>0){
                    for (Map<String,Object> comMap : comList) {
                        BigDecimal comShipAmount=this.dlyTypeService.computeFreight((List<Map<String,Object>>)comMap.get("goodsList"),null,token);
                        shipTotalAmount=shipTotalAmount.add(comShipAmount);
                        comMap.put("shipAmount", comShipAmount);
                        System.out.println("comId:"+comMap.get("comId")+",shipAmount:"+comShipAmount);
                    }
                }

                model.put("shipAmount", shipTotalAmount);
            }
        }
        return model;
    } 
    
    @Override
    public Map<String,Object> queryOrderDetails(String token,Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
        
        Map<String,Object> orderMap=new HashMap<String, Object>();
        
        Order order=this.orderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            order.setOrderStatusName(DictionaryUtil.getDataLabelByValue("order_status", String.valueOf(order.getOrderStatus())));
            orderMap.put("comName", this.companyDao.selectStoreName(order.getComId()));
            //h5
            order.setComName(String.valueOf(orderMap.get("comName")));
            orderMap.put("order", order);
            List<OrderItems> orderItemsList=this.orderItemsDao.selectByOrderId(order.getOrderId());
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
        return this.pageQuery("OrderDao.selectMemberOrderList", params);
    }
    
    
    @Override
    @Transactional
    public Order doCreateOrder(String goodsList,Long memberAddressId,String cartIds,
            String memberRealName,String memberCardId,String token){
        
//        memberAddressId=191L;
//        goodsList="[{'image':'http://106.15.179.242:8082/static/upload/img/20180319/2642ee43-4bbd-4631-a0f9-f6af974822ad.jpg','buyCount':1,'cost':313,'productId':3304,'totalPrice':3213,'goodsId':1114,'dlyTypeId':38,'activityName':'普通商品','weight':13,'store':111,'goodsType':0,'specs':'金','activityId':0,'unit':'132','price':3213,'name':'自营_普通_邮费_2','sn':'801100200120000010004001','comId':0},{'image':'http://106.15.179.242:8082/static/upload/img/20180319/2681d601-c326-406c-afd7-c169490c197b.jpg','buyCount':1,'cost':2,'productId':3302,'totalPrice':222,'goodsId':1113,'dlyTypeId':19,'activityName':'普通商品','weight':23,'store':1000,'goodsType':0,'specs':'棕色','activityId':0,'unit':'123','price':222,'name':'自营_普通_邮费_1','sn':'801100200120000010003001','comId':0},{'image':'http://106.15.179.242:8082/static/upload/img/20180319/e13b16b9-dfea-4bc2-80e6-3a020b2fb4dd.jpg','buyCount':1,'cost':1,'productId':3300,'totalPrice':111,'goodsId':1112,'dlyTypeId':19,'activityName':'普通商品','weight':1,'store':100,'goodsType':1,'specs':'红色,','activityId':0,'unit':'2','price':111,'name':'自营_海外_邮费','sn':'801100200120000010002001','comId':0},{'image':'http://106.15.179.242:8082/static/upload/img/20180206/946b825f-a3f0-4002-ad6f-858e162a511a.jpg','buyCount':1,'cost':55,'productId':2704,'totalPrice':88,'goodsId':966,'dlyTypeId':21,'activityName':'普通商品','weight':2235,'store':999,'goodsType':0,'specs':'五香,紫色,2.5','activityId':0,'unit':'j','price':88,'name':'商户测试777','sn':'800700200120000460001001','comId':145},{'image':'http://106.15.179.242:8082/static/upload/img/20180210/f9f40631-5a9d-4a6a-ab17-cf490bb9828a.jpg','buyCount':1,'cost':12.5,'productId':3284,'totalPrice':25,'goodsId':1097,'dlyTypeId':0,'activityName':'普通商品','weight':300,'store':1000,'goodsType':0,'specs':'原味','activityId':0,'unit':'盒','price':25,'name':'黄金馅饼板栗','sn':'800400100320000560001001','comId':156}]";
//        
        if(StringUtil.isEmpty(goodsList))
            throw new AppErrorException("商品列表不能为空...");
//        token ="CvMEPRypOg1mZ7ApSoCEHA==";
        System.out.println("|||||||||||||||||||||||||创建订单");
        System.out.println("memberId:"+this.getMemberId(token)+",token:"+token);
        Member member=this.getMember(token);
        
        Order order=new Order();
        List<Order> childOrderList=new ArrayList<Order>();
        BigDecimal goodsAmount=BigDecimal.valueOf(0); // 用于记录商品总价
        Integer goodsBuyCount=0; //用于记录购买数量
        BigDecimal orderAmount=BigDecimal.valueOf(0); //用于记录订单总价
        BigDecimal totalWeight=BigDecimal.valueOf(0); //用于记录总重量
        BigDecimal shipAmount=new BigDecimal(0);
        Long nowDate=System.currentTimeMillis();
        
        if(!StringUtil.isEmpty(goodsList)){
            
            synchronized (nowDate) {
                order.setOrderSn(String.valueOf(nowDate));
            }
            //获取收货地址
            if(StringUtil.isEmpty(memberAddressId))
                throw new AppErrorException("收货地址Id不能为空...");
//                Long memberAddressId=jsonObject.getLong("memberAddressId");
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
            
            //解析商品购买数据 
            JSONArray goodsJsonArray=JSONArray.fromObject(goodsList);
            if(goodsJsonArray!=null && goodsJsonArray.size()>0){
                
                for(int i=0;i<goodsJsonArray.size();i++){
                        
                    JSONObject goodsJson= goodsJsonArray.getJSONObject(i);
                    Long productId=Long.valueOf(goodsJson.get("productId")==null?null:goodsJson.get("productId").toString());
                    if(!StringUtil.isEmpty(productId)){ //productId judge start
                        
                        Map<String,Object> goodsMap=this.productDao.selectByProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
                        
                        if(goodsMap==null)
                            throw new AppErrorException("获取"+goodsJson.get("name")+"商品出错,无法下单,可能该商品己下架…………");
                        Order childOrder=new Order();
                        Boolean addOrderBl=true;
                        childOrder.setOrderSn(order.getOrderSn()+"_"+(i+1));
                        
                        getChildOrder:for (Order beChildOrder : childOrderList) {
                            if(beChildOrder.getComId().equals(Integer.valueOf(goodsMap.get("comId").toString())) &&
                                    beChildOrder.getOrderType().equals(Integer.valueOf(goodsMap.get("goodsType").toString()))){
                                childOrder=beChildOrder;
                                addOrderBl=false;
                                break getChildOrder;
                            }
                        }
                        
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
                        
                        if(buyPrice.compareTo(new BigDecimal(0))<=0)
                            throw new AppErrorException("获取商品"+goodsMap.get("name")+"价格错误,下单失败…………");
                        
                        BigDecimal weight=BigDecimal.valueOf(Double.valueOf(goodsMap.get("weight").toString())).
                                multiply(new BigDecimal(buyCount));
                        
                        //设置商品价格
                        if(childOrder.getGoodsAmount()==null)
                            childOrder.setGoodsAmount(buyPrice);
                        else
                            childOrder.setGoodsAmount(childOrder.getGoodsAmount().add(buyPrice));
                        //设置购买货品数量
                        if(childOrder.getBuyCount()==null){
                            childOrder.setBuyCount(buyCount);
                            childOrder.setGoodsCount(1);
                        }else{
                            childOrder.setBuyCount(childOrder.getBuyCount()+buyCount);
                            childOrder.setGoodsCount(childOrder.getGoodsCount()+1);
                        }
                        if(childOrder.getWeight()==null)
                            childOrder.setWeight(weight);
                        else
                            childOrder.setWeight(childOrder.getWeight().add(weight));
                        
                        goodsAmount=goodsAmount.add(buyPrice); //增加总商品价格
                        goodsBuyCount+=buyCount; //增加总购买数量
                        totalWeight=totalWeight.add(weight); //增加商品总重量
                        
                        if(StringUtil.isEmpty(goodsJson.get("activityId")) || goodsJson.get("activityId").toString().trim().equals("0")){
                            orderAmount=orderAmount.add(buyPrice);
                        }else{//活动先没写 
                                
                        }
                        
                        order.setComId(Integer.valueOf(goodsMap.get("comId").toString()));
                        childOrder.setComId(Integer.valueOf(goodsMap.get("comId").toString()));
                        childOrder.setOrderType(Integer.valueOf(goodsMap.get("goodsType").toString()));
                        //海外订单设置真实姓名和身份证
                        if(childOrder.getOrderType().equals(1)){
                            if(StringUtil.isEmpty(memberRealName))
                                throw new AppErrorException("订单包含海外订单,必须填写真实姓名...");
                            if(StringUtil.isEmpty(memberCardId))
                                throw new AppErrorException("订单包含海外订单,必须填写身份证ID...");
                            order.setMemberRealName(memberRealName);
                            order.setMemberCardId(memberCardId);
                            order.setOrderType(1);
                            
                            childOrder.setMemberRealName(memberRealName);
                            childOrder.setMemberCardId(memberCardId);
                        }
                        List<Map<String,Object>> childGoodsList=childOrder.getGoodsList();
                        childGoodsList.add((Map<String,Object>)goodsJson);
                        childOrder.setGoodsList(childGoodsList);
                        if(addOrderBl)
                        	childOrderList.add(childOrder);
                    }else{ //productId judge end
                        throw new AppErrorException("获取下单商品数据出错...");
                    }
                }
                
                order.setGoodsAmount(goodsAmount);
                order.setGoodsCount(goodsJsonArray.size()); //订单商品数量(包含赠品)
                order.setBuyCount(goodsBuyCount);
                order.setMemberId(member.getMemberId());
                
                order.setCreateTime(new Timestamp(nowDate));

                //计算运费
                for (Order childOrder : childOrderList) {
                    BigDecimal comShipAmount=this.dlyTypeService.computeFreight(childOrder.getGoodsList(),memberAddress.getCityId(),token);
                    shipAmount=shipAmount.add(comShipAmount);
                    childOrder.setShipAmount(comShipAmount);
                    childOrder.setOrderAmount(childOrder.getShipAmount().add(childOrder.getGoodsAmount()));
                }
                    
                order.setWeight(totalWeight);
                order.setShipAmount(shipAmount);
                orderAmount=orderAmount.add(shipAmount);
                order.setOrderAmount(orderAmount);
                
                //判断是否需要拆单
                if(childOrderList.size()==1){
                    childOrderList=new ArrayList<Order>();
                }else{
                    order.setSplitOrder(1);
                }
                this.orderDao.insertSelective(order);
                
                
                
                //设置子订单值
                for (Order childOrder : childOrderList) {
                	childOrder.setParentId(order.getOrderId());
                	childOrder.setMemberId(order.getMemberId());
                	childOrder.setShipId(order.getShipId());
                	childOrder.setShipType(order.getShipType());
                	childOrder.setShipAddress(order.getShipAddress());
                	childOrder.setShipProvinceId(order.getShipProvinceId());
                	childOrder.setShipProvinceName(order.getShipProvinceName());
                    childOrder.setShipCityId(order.getShipCityId());
                    childOrder.setShipCityName(order.getShipCityName());
                    childOrder.setShipDistrictId(order.getShipDistrictId());
                    childOrder.setShipDistrictName(order.getShipDistrictName());
                    childOrder.setShipName(order.getShipName());
                    childOrder.setShipAddr(order.getShipAddr());
                    childOrder.setShipZip(order.getShipZip());
                    childOrder.setShipMobile(order.getShipMobile());
                    childOrder.setShipTel(order.getShipTel());
                    childOrder.setShipDay(order.getShipDay());
                    childOrder.setSplitOrder(0);
                    childOrder.setCreateTime(order.getCreateTime());
                    childOrder.setOrderId(null);
                    this.orderDao.insertSelective(childOrder);
                }
                
                //添加订单货品
                orderItemsService.doAddOrderItems(goodsJsonArray,order,childOrderList);
                //删除购物车货品
                if(!StringUtil.isEmpty(cartIds)){
                    List<Cart> cartList = new ArrayList<Cart>();
                    Cart cart = null;
                    for(String cartId: cartIds.split(",")){
                        cart = new Cart();
                        cart.setMemberId(member.getMemberId());
                        cart.setCartId(Long.valueOf(cartId));
                        cartList.add(cart);
                    }
                    cartDao.batchDel(cartList);
                }
            }
        }else{
            throw new AppErrorException("获取下单商品数据出错...");
        }
            
        //添加日志
        OrderLog orderLog=new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderSn(order.getOrderSn());
        orderLog.setMessage("用户创建商品订单...");
        orderLog.setOpId(member.getMemberId());
//        orderLog.setOpUser(member.getUname());
        orderLog.setCreationTime(new Timestamp(nowDate));
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "user")));
        orderLogDao.insert(orderLog);
        
        return this.orderDao.selectMemberOrder(order.getOrderId(), member.getMemberId());
   
    }
    
    
    @Override
    public void doOrderCancel(String token,Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
        
        Order order=this.orderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            
            if(DictionaryUtil.getDataValueByName("order_status","order_not_pay").
                    equals(String.valueOf(order.getOrderStatus())) &&
               DictionaryUtil.getDataValueByName("pay_status","pay_no").
                    equals(String.valueOf(order.getPayStatus()))){
                
                order=new Order();
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
        
        Order order=this.orderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            
            if(DictionaryUtil.getDataValueByName("order_status","order_ship").
                    equals(String.valueOf(order.getOrderStatus())) &&
               DictionaryUtil.getDataValueByName("pay_status","pay_yes").
                    equals(String.valueOf(order.getPayStatus())) &&
               DictionaryUtil.getDataValueByName("ship_status","ship_yes").
                    equals(String.valueOf(order.getShipStatus()))){
                
                Order updateOrder=new Order();
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
        
        Order order=this.orderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(order!=null){
            //只有作废订单,已取消订单,己完成订单才能删除
            if(DictionaryUtil.getDataValueByName("order_status","order_cancel").
                    equals(String.valueOf(order.getOrderStatus())) || 
               DictionaryUtil.getDataValueByName("order_status","order_complete").
                    equals(String.valueOf(order.getOrderStatus())) || 
               DictionaryUtil.getDataValueByName("order_status","order_returnlation").
                    equals(String.valueOf(order.getOrderStatus()))){
                
                this.orderDao.updateDeleteFlag(orderId);
            }else
                throw new AppErrorException("该状态的订单不是删除..."); 
            
        }else
            throw new AppErrorException("该订单不存在...");
    }
    
    
    @Override
    @Transactional
    public void doOrderMaintain(String token,Long orderId){
        
        Order order=this.orderDao.selectMemberOrder(orderId, this.getMemberId(token));
        if(order!=null){
            this.orderDao.updateOrderStatus(orderId, -2, null, null);
        }else{
            throw new AppErrorException("该订单不存在...");
        }
    }

	@Override
	public List<Map<String, Object>> orderCount(String token) {
		return orderDao.orderCount(this.getMemberId(token));
	}
}
