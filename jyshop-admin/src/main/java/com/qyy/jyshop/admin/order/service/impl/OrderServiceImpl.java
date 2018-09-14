package com.qyy.jyshop.admin.order.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.order.service.OrderService;
import com.qyy.jyshop.dao.AllocationItemsDao;
import com.qyy.jyshop.dao.DeliveryDao;
import com.qyy.jyshop.dao.DepotDao;
import com.qyy.jyshop.dao.LogiDao;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.OrderCheckoutDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.dao.OrderLogDao;
import com.qyy.jyshop.dao.ProductStoreDao;
import com.qyy.jyshop.dao.ProfitParamDao;
import com.qyy.jyshop.model.AllocationItems;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderCheckout;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.model.ProductStore;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.util.excel.ExcelUtil;
import com.qyy.jyshop.util.excel.dto.OrderForExcel;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@SuppressWarnings("ALL")
@Service
public class OrderServiceImpl extends AbstratService<Order> implements OrderService{
    
    public static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemsDao orderItemsDao;
    @Autowired
    private OrderLogDao orderLogDao;
    @Autowired
    private OrderCheckoutDao orderCheckoutDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    @Autowired
    private DepotDao depotDao;
    @Autowired
    private AllocationItemsDao allocationItemsDao;
    @Autowired
    private LogiDao logiDao;
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private MemberPointDao memberPointDao;
    @Autowired
    private ProfitParamDao profitParamDao;
    @Autowired
    private MemberDao memberDao;
    
    @ServiceLog("查询订单详情")
    public Order queryOrder(Long orderId){
        return this.queryByID(orderId);
    }
    
    @Override
    @ServiceLog("查询已支付待分配订单")
    public List<Long> queryPayOrderId(){
        return this.orderDao.selectPayOrderId();
    }
    
    @Override
    @ServiceLog("根据条件查询订单列表(含货品列表)")
    public List<Map<String,Object>> queryOrderList(Map<String,Object> params){
        return this.orderDao.selectOrderByParams(params);
    }
    
    @Override
    @ServiceLog("获取订单列表(分页)")
    public PageAjax<Map<String,Object>> pageOrder(PageAjax<Map<String,Object>> page) {
    	ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
        return this.pageQuery("OrderDao.selectOrderByParams", pd);

    }
    
    @Override
    @Transactional
    public synchronized void automaticDistribution(Long orderId){
        
        if(StringUtil.isEmpty(orderId))
            return;
        
        Order order=new Order();
        order.setOrderId(orderId);
        order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_pay")));
        order.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
        order.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_no")));
        order.setSplitOrder(0);
        order=this.orderDao.selectOne(order);
         
        //分配订单组,用于记录日志
        StringBuffer orderIds=new StringBuffer();
        
        
        if(order!=null){//判断订单是否存在,或已经配过货
            Integer splitOrder=0;   
            StringBuffer depotIds=new StringBuffer("");
            Map<String,String> storeConfig=new HashMap<String, String>();
            
            Example example = new Example(OrderItems.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orderId", orderId);
            
            List<OrderItems> orderItemsList=this.orderItemsDao.selectByExample(example);
            
            Long nowDate=System.currentTimeMillis();
            //开始对货品进行仓库分配
            if(orderItemsList!=null && orderItemsList.size()>0){
                 
                //循环对每个订单货品进行配货
                for(int i=0;i<orderItemsList.size();i++){
                     
                    OrderItems orderItems=orderItemsList.get(i);
                     
                    if(!StringUtil.isEmpty(orderItems.getProductId())){
                         
                        Integer buyCount=orderItems.getBuyCount();
                        Boolean storeBool=true;
                        //货品库存信息
                        List<ProductStore> productStoreList=null;
                         
                        //判断己经把订单的某的商品分配了仓库,如果是,优先从这个仓库配货
                        if(!depotIds.toString().equals("")){
                            
                             productStoreList=this.productStoreDao.selectByUsableStore(orderItems.getProductId(), buyCount, depotIds.substring(0,depotIds.length()-1));
                             if(productStoreList!=null && productStoreList.size()>0){
                                 storeBool=false;
                             }
                        }
                         
                        if(storeBool){
                            productStoreList=this.productStoreDao.selectByUsableStore(orderItems.getProductId(), null, null);
                        }
                           
                        if(productStoreList!=null && productStoreList.size()>0){
                             
                            Long depotId=0L;
                            
                            getDepotId:for(int j=0;j<productStoreList.size();j++){//判断是否有仓库的库存足够配货
                                if(productStoreList.get(j).getUsableStore()>=buyCount){
                                    depotId=productStoreList.get(j).getDepotId();
                                    break getDepotId;
                                }
                            }
                             
                             
                            if(depotId!=null && !depotId.equals(0L)){//单个仓库配货
                                if(StringUtil.useLoop(depotIds.toString().split(","), depotId.toString())){
                                    String config=storeConfig.get(depotId.toString());
                                    storeConfig.put(depotId.toString(), config+",goodsId:"+orderItems.getGoodsId()+"&productId:"+orderItems.getProductId()
                                            +"&productCount:"+buyCount+"&itemId:"+orderItems.getItemId());
                                }else{
                                    storeConfig.put(depotId.toString(), "goodsId:"+orderItems.getGoodsId()+"&productId:"+orderItems.getProductId()
                                            +"&productCount:"+buyCount+"&itemId:"+orderItems.getItemId());
                                    depotIds.append(depotId+",");
                                } 
                            }else{//多个仓库配货
                                 
                                warehouseDistribution:for(int j=0;j<productStoreList.size();j++){
                                     
                                    depotId=productStoreList.get(j).getDepotId();
                                    Integer distribution=productStoreList.get(j).getUsableStore();
                                     
                                    if(distribution>=buyCount){
                                         distribution=buyCount;
                                    }
                                             
                                    if(StringUtil.useLoop(depotIds.toString().split(","), depotId.toString())){
                                        String config=storeConfig.get(depotId.toString());
                                        storeConfig.put(depotId.toString(), config+",goodsId:"+orderItems.getGoodsId()+"&productId:"+orderItems.getProductId()
                                                +"&productCount:"+distribution+"&itemId:"+orderItems.getItemId());
                                    }else{
                                        storeConfig.put(depotId.toString(), "goodsId:"+orderItems.getGoodsId()+"&productId:"+orderItems.getProductId()
                                                +"&productCount:"+distribution+"&itemId:"+orderItems.getItemId());
                                        depotIds.append(depotId+",");
                                    } 
                                     
                                    if(productStoreList.get(j).getUsableStore()>=buyCount){//判断是否足够配货
                                         break warehouseDistribution;
                                    }else{//否则扣减速配货量
                                         buyCount=buyCount-distribution;
                                    }
                                }
                             
                                if(!buyCount.equals(0D)){
                                    logger.error("货品"+orderItems.getGoodsName()+"库存不足,自动配货失败....");
                                    return ;
                                }
                            }
    
                        }else{
                            logger.error("获取不到货品"+orderItems.getGoodsName()+"库存信息,无法进行配货操作....");
                            return ;
                        }
                         
                    }else{
                        logger.error("订单货品信息错误,无法进行配货操作....");
                        return ;
                    }
                }
                 
                //开始对订单分配仓库
                String[] depotIdArray=depotIds.toString().split(",");
                
                if(depotIdArray.length==1){//如果是同一个仓库配货则不进行拆单
                    Long depotId=Long.valueOf(depotIdArray[0]);
                    String strAllocationItems=storeConfig.get(depotIdArray[0]);
                     
                    if(!StringUtil.isEmpty(strAllocationItems)){
                        String[] allocationArrays=strAllocationItems.split(",");
                         
                        //获取仓库所属供应商帐户ID
                        Integer depotComId=this.depotDao.selectComIdById(depotId);
                         
                        if(StringUtil.isEmpty(depotComId)){
                            logger.error("获取仓库供应商失败,自动分配订单出BUG了....");
                            return ;
                        }
                         
                        for(String allocationArray : allocationArrays){
                            //对订单的每个货品进行配货
                            AllocationItems allocationItems =this.getAllocationItems(allocationArray.split("&"));
                            allocationItems.setOrderId(order.getOrderId());
                            allocationItems.setOrderSn(order.getOrderSn());
                            allocationItems.setDepotId(depotId);
                            allocationItems.setComId(depotComId);
                            this.allocationItemsDao.insertSelective(allocationItems);
                        }
                        //修改订单所属供应商
                        this.orderDao.updateComIdByOrderId(order.getOrderId(), depotComId,
                                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                    }
                    
                    orderIds.append(order.getOrderId());
                     
                }else{//如果是不同仓库,则进行拆单
                    splitOrder=1;
                     
                    //商品总额,拆金额用
                    BigDecimal goodsAmount=order.getGoodsAmount();
                    //邮费
                    BigDecimal shipAmount=order.getShipAmount();
                     
                    for(int i=1;i<=depotIdArray.length;i++){
                         
                        Long depotId=Long.valueOf(depotIdArray[i-1]);
                         
                        //获取仓库所属供应商ID
                        Integer depotComId=this.depotDao.selectComIdById(depotId);
                         
                        if(StringUtil.isEmpty(depotComId)){
                            logger.error("获取仓库供应商失败,自动分配订单出BUG了....");
                            return ;
                        }

                        Order childOrder=getOrder(order);
                        childOrder.setParentId(order.getOrderId());
                        childOrder.setOrderSn(order.getOrderSn()+"_"+i);
                        childOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                        childOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                        childOrder.setComId(depotComId);
                        childOrder.setOrderId(null);
                        childOrder.setCreateTime(new Timestamp(nowDate));
                        childOrder.setSplitOrder(null);
                        BigDecimal comShipAmount=orderCheckoutDao.selectOfPayMoney(orderId, 
                                depotComId,
                                Integer.valueOf(DictionaryUtil.getDataValueByName("order_checkout_type", "ship_amount")));
                        if(StringUtil.isEmpty(comShipAmount))
                            comShipAmount=new BigDecimal(0D);
                        childOrder.setShipAmount(comShipAmount);
                        this.orderDao.insertSelective(childOrder);
                        BigDecimal price=new BigDecimal(0);
                        Integer goodsBuyCount=0;
                        Integer goodsCount=0;
                         
                        String strAllocationItems=storeConfig.get(depotId.toString());
                        if(strAllocationItems!=null && !strAllocationItems.trim().equals("")){
                            String[] allocationArrays=strAllocationItems.split(",");
                             
                            for(String allocationArray : allocationArrays){
                                //对订单的每个货品进行配货
                                AllocationItems allocationItems =null;
                                allocationItems=this.getAllocationItems(allocationArray.split("&"));
                                allocationItems.setOrderId(childOrder.getOrderId());
                                allocationItems.setOrderSn(childOrder.getOrderSn());
                                allocationItems.setDepotId(depotId);
                                OrderItems orderItems=this.orderItemsDao.selectByOrderIdAndProductId(order.getOrderId(), allocationItems.getProductId());
                                 
                                if(orderItems!=null){
                                    orderItems.setItemId(null);
                                    orderItems.setOrderId(childOrder.getOrderId());
                                    orderItems.setOrderSn(childOrder.getOrderSn());
                                    orderItems.setBuyCount(allocationItems.getProductCount());
                                }else{
                                    logger.error("获取订单货品信息出错,自动分配订单失败了....");
                                    return ;
                                }
                                 
                                this.orderItemsDao.insertSelective(orderItems);
                                 
                                allocationItems.setItemId(orderItems.getItemId());
                                allocationItems.setComId(depotComId);
                           
                                this.allocationItemsDao.insertSelective(allocationItems);
                                                        
                                price=price.add(orderItems.getGoodsPrice().multiply(new BigDecimal(orderItems.getBuyCount())));
                                goodsBuyCount+=orderItems.getBuyCount();
                                goodsCount++;
                            }
                             
                            Order updateChildOrder=new Order();
                            updateChildOrder.setGoodsAmount(price);//商品价格
                            updateChildOrder.setGoodsCount(goodsCount);//商品数量(即商品的种类数)
                            updateChildOrder.setBuyCount(goodsBuyCount);//商品的购买量
                            updateChildOrder.setOrderId(childOrder.getOrderId());
                             
                            BigDecimal orderAmount=price.add(childOrder.getShipAmount());
                            
                            //订单rmb结算信息
                            OrderCheckout childOrderCheckout = new OrderCheckout();
                            childOrderCheckout.setType(Integer.valueOf(DictionaryUtil.getDataValueByName(
                                    "order_checkout_type", "goods_amount")));
                            childOrderCheckout.setPayMoney(price);
                            childOrderCheckout.setMemberId(childOrder.getMemberId());
                            childOrderCheckout.setOrderId(childOrder.getOrderId());
                            childOrderCheckout.setOrderSn(childOrder.getOrderSn());
                            childOrderCheckout.setCreationTime(new Timestamp(nowDate));
                            childOrderCheckout.setComId(childOrder.getComId());
                            this.orderCheckoutDao.insertSelective(childOrderCheckout);
                             
                            updateChildOrder.setOrderAmount(orderAmount);
                            //修改订单信息
                            this.orderDao.updateByPrimaryKeySelective(updateChildOrder);
                             
                        }
                        orderIds.append(childOrder.getOrderId()+",");
                    }
                }
            
            }else{
                 logger.error("获取待货信息出错,自动分配订单失败了....");
                 return ;
            }
             
            //拆单完成,修改订单状态
            Order updateOrder=new Order();
            updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
            updateOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
            updateOrder.setOrderId(order.getOrderId());
            updateOrder.setSplitOrder(splitOrder);
            this.orderDao.updateByPrimaryKeySelective(updateOrder);
            
            //添加日志
            OrderLog orderLog=new OrderLog();
            orderLog.setOrderId(order.getOrderId());
            orderLog.setOrderSn(order.getOrderSn());
            orderLog.setMessage("系统自动对订单"+order.getOrderSn()+"进行了配单操作,配单订单ID:"+orderIds.substring(0,orderIds.length()-1));
            orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
            orderLog.setCreationTime(new Timestamp(nowDate));
            this.orderLogDao.insertSelective(orderLog);
        }else{
            logger.error("获取待配货订单信息数据出错,自动分配订单失败了....");
            return ;
        }

    }
    
    @Override
    @Transactional
    public void doAutomaticReceipt(){
        
        this.orderDao.updateOfReceipt(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_ship")), 
                7, 
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_rog")));
    }
    
    @Override
    @Transactional
    public void doAutomaticComplete(){
        
        this.orderDao.updateOfComplete(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_rog")), 
                7, 
                Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")));
    }
    
    @Override
    @Transactional
    @ServiceLog("订单发货")
    public String doOrderDelivery(Map<String,Object> params){
        
        if(StringUtil.isEmpty(params.get("logiNo")))
            return "获取物流单号不能为空....";
                    
        Delivery delivery=new Delivery();
        
        Logi logi=logiDao.selectByPrimaryKey(Integer.valueOf(params.get("logiId").toString()));
        if(logi!=null){
            delivery.setLogiId(logi.getLogiId());
            delivery.setLogiCode(logi.getLogiCode());
            delivery.setLogiName(logi.getLogiName());
        }else{
            return "获取物流公司信息失败....";
        }
            
        Order order=this.orderDao.selectByPrimaryKey(Long.valueOf(params.get("orderId").toString()));
        if(order!=null){

            if(!StringUtil.isEmpty(order.getComId()) && !(
                    order.getComId().equals(0) ||
                    order.getComId().equals(this.getLoginUser().getComId()))){
                return "你没你权限对订单进行发货操作...";
            }
            
            if(!order.getOrderStatus().equals(
                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")))){
                return "订单状态错误,可能已进行过发货动作...";
            }
            
            /**
             * 如果是父订单,则直接修改订单状态与货运状态为己发货
             * 如果是子订单,则对所有子订单发货情况进行判断,如果己全部发货,则修改父订单为己发货,否则修改为部分发货
             */
            Integer orderStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_ship"));
            Integer shipStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_yes"));
            
            //修改父订单状态
            Order updateCOrder=new Order();
            updateCOrder.setOrderId(order.getOrderId());
            updateCOrder.setOrderStatus(orderStatus);
            updateCOrder.setShipStatus(shipStatus);
            updateCOrder.setDeliverTime(new Timestamp(System.currentTimeMillis()));
            this.update(updateCOrder);
            
            if(!order.getParentId().equals(0L)){//如果不是父订单,则对子订单的发货状态进行判断
                
                Example example = new Example(Order.class);
                Criteria criteria = example.createCriteria();
                criteria.andEqualTo("parentId", order.getParentId());
                criteria.andEqualTo("orderStatus", DictionaryUtil.getDataValueByName("order_status", "order_allocation"));
                criteria.andEqualTo("shipStatus", DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes"));

                //如果子订单都己发货完成,则修改主订单状态为己发货状态,否则修改为部分发货
                if(this.orderDao.selectCountByExample(example)>0){
                    orderStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_partial_shiped"));
                    shipStatus=Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_partial_shiped"));
                }
                
                //修改父订单状态
                Order updateOrder=new Order();
                updateOrder.setOrderId(order.getParentId());
                updateOrder.setOrderStatus(orderStatus);
                updateOrder.setShipStatus(shipStatus);
                updateOrder.setDeliverTime(new Timestamp(System.currentTimeMillis()));
                this.update(updateOrder);
            }
            
            delivery.setOrderId(order.getOrderId());
            delivery.setOrderSn(order.getOrderSn());
            delivery.setMemberId(order.getMemberId());
            delivery.setLogiNo(params.get("logiNo").toString());
            delivery.setOpId(Long.valueOf(this.getLoginUser().getId()));
            delivery.setOpUser(this.getLoginUser().getUsername());
            delivery.setCreationTime(new Timestamp(System.currentTimeMillis()));
            delivery.setType(Integer.valueOf(DictionaryUtil.getDataValueByName("delivery_type", "delivery")));
            this.deliveryDao.insertSelective(delivery);

            //添加日志
            OrderLog orderLog=new OrderLog();
            orderLog.setOrderId(order.getOrderId());
            orderLog.setOrderSn(order.getOrderSn());
            orderLog.setOpId(Long.valueOf(this.getLoginUser().getId()));
            orderLog.setOpUser(this.getLoginUser().getUsername());
            orderLog.setMessage(this.getLoginUser().getUsername()+"对订单"+order.getOrderSn()+"进行了发货操作,发货订单ID:"+delivery.getOrderId());
            orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "administrator")));
            orderLog.setCreationTime(new Timestamp(System.currentTimeMillis()));
            this.orderLogDao.insertSelective(orderLog);
        }else{
            return "获取发货订单信息失败...";
        }
        return null;
    }
    
    @Override
    @Transactional
    @ServiceLog("批量发货")
    public String  doBatchDelivery(){
        
        HttpServletRequest request = getRequest();
        String[] orderIdArray = request.getParameterValues("orderId");
        String[] logiIdArray = request.getParameterValues("logiId");  
        String[] logiNoArray = request.getParameterValues("logiNo"); 
        
        if(orderIdArray!=null && orderIdArray.length>0){
            
            Map<String,Object> params=new HashMap<String,Object>();
            
            for(int i=0;i<orderIdArray.length;i++){
                params.put("orderId", orderIdArray[i]);
                params.put("logiId", logiIdArray[i]);
                params.put("logiNo", logiNoArray[i]);
                String deliveryInfo=this.doOrderDelivery(params);
                if(!StringUtil.isEmpty(deliveryInfo))
                    return deliveryInfo;
            }
            
            return null;
            
        }
        
        return "请选择要发货的订单...";
    }
    
    @Override
    @Transactional
    @ServiceLog("订单作废")
    public String doCancleOrder(Long orderId){
        
        Order order=this.queryByID(orderId);
        if(order==null)
            return "获取订单信息失败...";
        if(this.getLoginUserComId().equals(0) || this.getLoginUserComId().equals(order.getComId())){
            
            this.orderDao.updateOrderStatus(orderId, 
                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_returnlation")),
                    null, null);
            //添加日志
            OrderLog orderLog=new OrderLog();
            orderLog.setOrderId(order.getOrderId());
            orderLog.setOrderSn(order.getOrderSn());
            orderLog.setOpId(Long.valueOf(this.getLoginUser().getId()));
            orderLog.setOpUser(this.getLoginUser().getUsername());
            orderLog.setMessage(this.getLoginUser().getUsername()+"对订单"+order.getOrderSn()+"进行了作废操作,作废订单ID:"+order.getOrderId());
            orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "administrator")));
            orderLog.setCreationTime(new Timestamp(System.currentTimeMillis()));
            this.orderLogDao.insertSelective(orderLog);
        }else{
            return "你没有权限操作该订单...";
        }
   
        return null;
    }
    
    
    
    /**
     * 获取配货单信息
     * @author hwc
     * @created 2017年12月16日 下午2:55:47
     * @param allocationArray
     * @return
     */
    private AllocationItems getAllocationItems(String[] allocationArray){
        
        AllocationItems allocationItem = new AllocationItems();
        
        //得到类对象
        Class allocationItemCla = (Class) allocationItem.getClass();
        
        //得到类中的所有属性集合
        Field[] fs = allocationItemCla.getDeclaredFields();
        
        for(String  allocation : allocationArray){
            
            String name=allocation.substring(0,allocation.lastIndexOf(":"));
            String value=allocation.substring(allocation.lastIndexOf(":")+1);
            for(int i = 0;i < fs.length;i++){
                Field f = fs[i];
                f.setAccessible(true); // 设置此属性是可以访问的
                
                String fieldName = f.getName();// 得到此属性的名称
                
                if(name.equals(fieldName)){
                   
                   try {
                        if(f.getType().toString().endsWith("long") ||f.getType().toString().endsWith("Long"))
                            f.set(allocationItem, Long.valueOf(value));
                        else if(f.getType().toString().endsWith("int") ||f.getType().toString().endsWith("Integer"))
                            f.set(allocationItem, Integer.valueOf(value));
                        else
                            f.set(allocationItem, value);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } 
                }
            }
            
        }
        
        return allocationItem;
    }
    
 
    /**
     * 复制订单实体
     * @author hwc
     * @created 2017年12月16日 下午3:38:47
     * @param order 被复制的订单实体
     * @return
     */
    private Order getOrder(Order order){
        
        Order childOrder=new Order();
        
        Class ordercla=(Class)order.getClass();
        Field[] fs = ordercla.getDeclaredFields();
        
        Class childOrdercla=(Class)order.getClass();
        Field[] fs1 = childOrdercla.getDeclaredFields();
        
        for(int i = 0;i < fs.length;i++){
            Field f = fs[i];
            f.setAccessible(true);
            fs1[i].setAccessible(true); // 设置此属性是可以访问的
            
            try {
                if(i==0)
                    continue;
                fs1[i].set(childOrder, f.get(order));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } 
        }
    
        return childOrder;
    }

	@Override
	public Map<String, Object> getOrderCountByMemberId(Long memberId) {
		return this.orderDao.getOrderCountByMemberId(memberId);
	}

	@ServiceLog("导出订单数据到Excel")
    @Override
    public ExcelData getExportData(Map<String, Object> map,HttpServletResponse response) throws Exception {
       /* ExcelData data = new ExcelData();
        try{
            data.setName("订单数据");
            List<String> titles = new ArrayList();
            titles.add("序号");
            titles.add("订单号");
            titles.add("下单时间");
            titles.add("订单状态");
            titles.add("商品名称");
            titles.add("商品货号");
            titles.add("单价");
            titles.add("供应商");
            titles.add("购买数量");
            titles.add("商品总价");
            titles.add("邮费");
            titles.add("总重量/g");
            titles.add("快递方式");
            titles.add("赠送云积分");
            titles.add("订单总价");
            titles.add("红积分");
            titles.add("黄积分");
            titles.add("云积分抵扣");
            titles.add("优惠券抵扣");
            titles.add("优惠金额");
            titles.add("实付金额");
            titles.add("付款状态");
            titles.add("支付方式");
            titles.add("付款时间");
            titles.add("买家昵称");
            titles.add("用户ID");
            titles.add("收货人姓名");
            titles.add("联系方式");
            titles.add("收货地址");
            titles.add("邮编");
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();
            List<Map<String, Object>> orderList = orderDao.queryExportOrderData(map);
            int i = 1;

            Map orderStatusMap = DictionaryUtil.getDataGroupMap("order_status");
            Map payStatusMap=DictionaryUtil.getDataGroupMap("pay_status");

            for (Map m: orderList) {
                List<Object> row = new ArrayList();
                row.add(i);
                //判断当前orderList有几个orderSn
                //int mergeCount = this.count(orderList, m.get("orderSn").toString());
                //订单号
                row.add(m.get("orderSn")==null?"":m.get("orderSn").toString());
                //下单时间
                row.add(m.get("createTime")==null?"":m.get("createTime").toString());
                //订单状态
                if(m.get("orderStatus").toString().equals("-2"))
                    row.add("维权中");
                else
                    row.add(m.get("orderStatus")==null?"":DataDictUtil.getDataItemLabelByValue(orderStatusMap,m.get("orderStatus").toString(),"N"));
                //商品名称
                row.add(m.get("goodsName")==null?"":m.get("goodsName").toString());
                //商品货号
                row.add(m.get("productSn")==null?"":m.get("productSn").toString());
                //单价
                row.add(m.get("goodsPrice")==null?"":m.get("goodsPrice").toString());
                //供应商
                row.add(m.get("comName")==null?"":m.get("comName").toString());
                //购买数量
                row.add(m.get("buyCount")==null?"":m.get("buyCount").toString());
                //商品总价
                row.add(m.get("goodsAmount")==null?"":m.get("goodsAmount").toString());
                //邮费
                row.add(m.get("shipAmount")==null?"":m.get("shipAmount").toString());
                //总重量
                row.add(m.get("weight")==null?"":m.get("weight").toString());
                //快递方式
                row.add(m.get("shipType")==null?"":m.get("shipType").toString());
                //赠送云积分
                row.add("0");
                //订单总价
                row.add(m.get("orderAmount")==null?"":m.get("orderAmount").toString());
                //红积分抵扣
                row.add("0");
                //黄积分抵扣
                row.add("0");
                //云积分抵扣
                row.add("0");
                //优惠券抵扣
                row.add("0");
                //优惠金额
                row.add(m.get("discount")==null?"":m.get("discount").toString());
                //实付金额
                row.add(m.get("payMoney")==null?"":m.get("payMoney").toString());
                //付款状态
                row.add(m.get("payStatus")==null?"":DataDictUtil.getDataItemLabelByValue(payStatusMap,m.get("payStatus").toString(),"N"));
                //支付方式
                row.add(m.get("paymentName")==null?"":m.get("paymentName").toString());
                //付款时间
                row.add(m.get("paymentTime")==null?"":m.get("paymentTime").toString());
                //买家昵称
                row.add(m.get("nickname")==null?"":m.get("nickname").toString());
                //收货人ID
                row.add(m.get("memberId")==null?"":m.get("memberId").toString());
                //收货人姓名
                row.add(m.get("shipName")==null?"":m.get("shipName").toString());
                //联系方式
                String tel = m.get("shipTel")==null?"":m.get("shipTel").toString();
                row.add(m.get("shipMobile")==null?tel:m.get("shipMobile").toString());
                //收货地址
                row.add(m.get("shipAddress")==null?"":m.get("shipAddress").toString());
                //邮编
                row.add(m.get("shipZip")==null?"":m.get("shipZip").toString());
                rows.add(row);
                i++;
            }
            data.setRows(rows);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }*/
		
		List<Map<String,Object>> orderList = orderDao.queryExportOrderData(map);;//必须按合并列排序
        
        List<OrderForExcel> list = new ArrayList<OrderForExcel>();
        for(Map data:orderList) {
            //OrderForExcel orderForExcel = new OrderForExcel();
            //BeanUtils.copyProperties(data2,data);
            //BeanUtils.copyProperties(data,orderForExcel);
        	data.put("orderStatus", DictionaryUtil.getDataLabelByValue("order_status", data.get("orderStatus").toString()));
        	data.put("payStatus", DictionaryUtil.getDataLabelByValue("pay_status", data.get("payStatus").toString()));
            OrderForExcel orderForExcel = EntityReflectUtil.toBean(OrderForExcel.class, data);
            list.add(orderForExcel);
        }
        
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("title", "订单信息表");
        String xlsType="订单";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        ExcelUtil.getInstance().exportObj2ExcelByTemplate(response,datas, "template.xls", null,
                list, OrderForExcel.class, true,2,xlsType);
		
		
		return null;
    }

    private int count(List<Map<String, Object>> orderList, String orderSn) {
        int i = 0;
        for (Map map:orderList) {
            if(map.get("orderSn").toString().equals(orderSn)){
                i++;
            }
        }
        return i;
    }

	@Override
	public List<Map<String, Object>> queryOrderProfit(Order order,Member member) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		if(order.getOrderStatus() == Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete"))){
			//返回已分润的结果
			returnList = memberPointDao.getOrderProfit(order.getOrderId());
		}else if(order.getOrderStatus() >= 0 && order.getOrderStatus() != 8){
			//预分润
			ProfitParam profitParam = profitParamDao.selectAll().get(0);
			BigDecimal total=profitParam.getTax().add(profitParam.getManagement());
	        total=total.add(profitParam.getOperation());
	        total=total.add(profitParam.getGain());
	        total=total.add(profitParam.getPresenter());
	        
	        if(total.compareTo(new BigDecimal(100))<0){
	        	BigDecimal goodsCost=new BigDecimal(0);//成本总金额
	            BigDecimal goodsPrice=new BigDecimal(0);//销售总金额
	            List<OrderItems> orderItemsList=this.orderItemsDao.selectByOrderId(order.getOrderId());
	            if(orderItemsList!=null && orderItemsList.size()>0){
	                for (OrderItems orderItems : orderItemsList) {
	                    //只有销售价大于成本价的才进行分润
	                    if(!StringUtil.isEmpty(orderItems.getCost()) &&
	                            orderItems.getPrice().compareTo(orderItems.getCost())>0){
	                        goodsCost=goodsCost.add(orderItems.getCost().multiply(new BigDecimal(orderItems.getBuyCount())));
	                        goodsPrice=goodsPrice.add(orderItems.getPrice().multiply(new BigDecimal(orderItems.getBuyCount())));
	                    }
	                }
	            }
	            
	          //如果销售总金额大于成本总金额,则进行分润
	            if(goodsPrice.compareTo(goodsCost)>0){
	                BigDecimal profitAmount=goodsPrice.subtract(goodsCost);//毛利润
	                //分润金额(毛利润2)
	                profitAmount=profitAmount.subtract(profitAmount.multiply(total).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN));
	                if(member.getIsProfit() == 0){//首购满200 分润 毛利润2的30%
		        		if(order.getOrderAmount().compareTo(new BigDecimal(200))>0){
		        			profitAmount=profitAmount.multiply(profitParam.getProfitMemberFirst()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
		        			BigDecimal cloudPoint = order.getPayMoney();
		        			BigDecimal yellowCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
		        			BigDecimal redCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
		        			//拼装返回对象
		        			Map<String,Object> map = new HashMap<String, Object>();
		        			map.put("memberId", member.getMemberId());
		        			map.put("nickname", member.getNickname());
		        			map.put("pointType", 0);
		        			map.put("point", cloudPoint);
		        			map.put("getType", 0);
		        			returnList.add(map);
		        			map = new HashMap<String, Object>();
		        			map.put("memberId", member.getMemberId());
		        			map.put("nickname", member.getNickname());
		        			map.put("pointType", 1);
		        			map.put("point", yellowCloud);
		        			map.put("getType", 0);
		        			returnList.add(map);
		        			map = new HashMap<String, Object>();
		        			map.put("memberId", member.getMemberId());
		        			map.put("nickname", member.getNickname());
		        			map.put("pointType", 2);
		        			map.put("point", redCloud);
		        			map.put("getType", 0);
		        			returnList.add(map);
		        		}
		        	}else{//重复分润 毛利润2的20%
		        		profitAmount=profitAmount.multiply(profitParam.getProfitMemberSec()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal cloudPoint = order.getPayMoney();
	        			BigDecimal yellowCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal redCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			//拼装返回对象
	        			Map<String,Object> map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 0);
	        			map.put("point", cloudPoint);
	        			map.put("getType", 1);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 1);
	        			map.put("point", yellowCloud);
	        			map.put("getType", 1);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getMemberId());
	        			map.put("nickname", member.getNickname());
	        			map.put("pointType", 2);
	        			map.put("point", redCloud);
	        			map.put("getType", 1);
	        			returnList.add(map);
		        	}
		        	
		        	Integer parentId = member.getParentId();
		        	if(parentId > 0 ){//给上级分润
		        		profitAmount=profitAmount.multiply(profitParam.getProfitShopownerSub()).divide(new BigDecimal(100), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal cloudPoint = order.getPayMoney();
	        			BigDecimal yellowCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			BigDecimal redCloud = profitAmount.divide(new BigDecimal(2), 2,BigDecimal.ROUND_DOWN);
	        			//拼装返回对象
	        			Member parentMember = memberDao.findMemerById(member.getParentId().longValue());
	        			Map<String,Object> map = new HashMap<String, Object>();
	        			map.put("memberId", member.getParentId());
	        			map.put("nickname", parentMember==null?"":parentMember.getNickname());
	        			map.put("pointType", 0);
	        			map.put("point", cloudPoint);
	        			map.put("getType", 2);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getParentId());
	        			map.put("nickname", parentMember==null?"":parentMember.getNickname());
	        			map.put("pointType", 1);
	        			map.put("getType", 2);
	        			map.put("point", yellowCloud);
	        			returnList.add(map);
	        			map = new HashMap<String, Object>();
	        			map.put("memberId", member.getParentId());
	        			map.put("nickname", parentMember==null?"":parentMember.getNickname());
	        			map.put("pointType", 2);
	        			map.put("point", redCloud);
	        			map.put("getType", 2);
	        			returnList.add(map);
		        	}
	            }
	        }
		}
		
		return returnList;
	}

    @Override
    @Transactional
    public void autoCloseOrder(Order order ,BigDecimal hourOrderAutoClose) {
        /**
         *  ORDER_STATUS = - 9,
          DELETE_FLAG = 1
         */
        Order updateOrder = new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_cancel")));
        updateOrder.setDeleteFlag(1);
        orderDao.updateByPrimaryKeySelective(updateOrder);
        
        //orderlog
        //添加日志
        OrderLog orderLog=new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderSn(order.getOrderSn());
        orderLog.setMessage("系统自动对订单"+order.getOrderSn()+"进行了关闭操作,该订单"+hourOrderAutoClose+"小时未支付");
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        orderLog.setCreationTime(TimestampUtil.getNowTime());
        this.orderLogDao.insertSelective(orderLog);
        
    }

    @Override
    @Transactional
    public void autoConfirmCommonOrder(Order order, BigDecimal dayAutoConfirm) {
        Order updateOrder=new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status","order_rog")));
        updateOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status","ship_receipt")));
        updateOrder.setSigningTime(new Timestamp(System.currentTimeMillis()));
        this.update(updateOrder);
        
        //添加日志
        OrderLog orderLog=new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderSn(order.getOrderSn());
        orderLog.setMessage("系统自动对订单"+order.getOrderSn()+"进行了确认收货操作,该订单发货后"+dayAutoConfirm+"天内买家未操作，系统自动确认收货");
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        orderLog.setCreationTime(TimestampUtil.getNowTime());
        this.orderLogDao.insertSelective(orderLog);
    }

    @Override
    @Transactional
    public void autoCloseOrderLegal(Order order, BigDecimal dayLegalAutoClose) {
        Order updateOrder=new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setRightCloseStatus(1);//TODO 不可维权
        this.update(updateOrder);
        
        //添加日志
        OrderLog orderLog=new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderSn(order.getOrderSn());
        orderLog.setMessage("系统自动对订单"+order.getOrderSn()+"进行了自动关闭维权功能操作,该订单已收货订单"+dayLegalAutoClose+"天内不发起维权，系统自动关闭维权功能"); 
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        orderLog.setCreationTime(TimestampUtil.getNowTime());
        this.orderLogDao.insertSelective(orderLog);
        
    }
}
