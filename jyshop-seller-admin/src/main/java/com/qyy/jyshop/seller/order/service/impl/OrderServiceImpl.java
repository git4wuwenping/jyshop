package com.qyy.jyshop.seller.order.service.impl;

import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.seller.common.annotation.ServiceLog;
import com.qyy.jyshop.seller.order.service.OrderService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DataDictUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    
    @Override
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
    @ServiceLog("获取订单列表(分页)")
    public PageAjax<Map<String,Object>> pageOrder(PageAjax<Map<String,Object>> page) {
        
        ParamData pd = this.getParamData(page.getPageNo(),page.getPageSize());
        pd.put("comId", this.getLoginUserComId());
        return this.pageQuery("OrderDao.selectOrderByParams", pd);

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

            if(!order.getComId().equals(this.getLoginUser().getComId())){
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

//            this.orderDao.updateOrderStatus(order.getOrderId(), orderStatus, null, shipStatus);
            
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
    @ServiceLog("订单作废")
    public String doCancleOrder(Long orderId){
        
        Order order=this.queryByID(orderId);
        if(order==null)
            return "获取订单信息失败...";
        if(this.getLoginUserComId().equals(order.getComId())){
            
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

    @ServiceLog("导出订单数据到Excel")
    @Override
    public ExcelData getExportData(Map<String, Object> map) {
        ExcelData data = new ExcelData();
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
            titles.add("收货人ID");
            titles.add("收货人姓名");
            titles.add("联系方式");
            titles.add("收货地址");
            titles.add("邮编");
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();
            map.put("comId", this.getLoginUserComId());
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
                row.add(m.get("payStatus")==null?"": DataDictUtil.getDataItemLabelByValue(payStatusMap,m.get("payStatus").toString(),"N"));
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
        }
        return data;
    }
}
