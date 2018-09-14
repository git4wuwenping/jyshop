package com.qyy.jyshop.pay.service.impl;

import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pay.feign.*;
import com.qyy.jyshop.pay.service.OrderService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends AbstratService<Order> implements OrderService{

    @Autowired
    private AdvancePayFeign advancePayFeign;
    @Autowired
    private WxPayFeign wxPayFeign;
    @Autowired
    private AliPayFeign aliPayFeign;
    @Autowired
    private PayLogFeign payLogFeign;
    @Autowired
    private ProductStoreFeign productStoreFeign;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private GiftOrderDao giftOrderDao;
    @Autowired
    private BargainOrderDao bargainOrderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SpellOrderDao spellOrderDao;
    @Autowired
    private SpellDao spellDao;
    @Autowired
    private SpellActivityDao spellActivityDao;
    @Autowired
    private RightOrderDao rightOrderDao;
    @Autowired
    private OrderItemsDao itemsDao;
    @Autowired
    private RightRelDao relDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductStoreDao productStoreDao;
    @Autowired
    private RightProcessDao processDao;

    @Override
    @Transactional
    public Map<String,Object> doOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        System.out.println("//////////////////////////////////");     
        System.out.println("orderId:"+orderId+",memberId:"+this.getMemberId(token)+",token:"+token);
        synchronized (orderId) {
            System.out.println("==================================");        
            Order order=this.orderDao.selectMemberOrder(orderId, this.getMemberId(token));
            if(order!=null){
                if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")).equals(order.getOrderStatus()) && 
                        Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")).equals(order.getPayStatus())){
                    
                    if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
                        //预存款支付
                        if(this.advancePayFeign.pay(order.getOrderAmount(), orderId,0,payPwd,token)){
                            
                            this.orderDao.updateOrderPayStatus(order.getOrderId(), 
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")), //订单状态
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")), //支付状态
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")), //配送状态 
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")), //支付ID
                                    DictionaryUtil.getDataLabelByName("pay_type", "advancepay"),  //支付名称
                                    "advancepay", //支付类型
                                    order.getOrderAmount(), //支付金额 
                                    new Timestamp(System.currentTimeMillis())); //支付时间
                            
                            this.payLogFeign.addOrderPayLog(order);
                            
                            //减少货品库存
                            this.productStoreFeign.editProductStore(order.getOrderId());
                        }else{
                            throw new AppErrorException("支付失败...");
                        }
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")))){
                        
                        //IOS APP 微信支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.wxPayFeign.getAppPayInfo(order.getOrderAmount(), order.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")),
                                    null, 
                                    token);
                        //H5 微信支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return this.wxPayFeign.getH5PayInfo(order.getOrderAmount(), order.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")),
                                    null, 
                                    token);
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
                        //IOS APP 支付宝支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.aliPayFeign.getAppPayInfo(order.getOrderAmount(), order.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "order")),
                                    null, 
                                    token);
                        //H5 支付宝支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return null;
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                    }else{
                        throw new AppErrorException("错误的支付方式...");
                    }
                    
                }else{
                    throw new AppErrorException("该订单己支付过...");
                }
            }else{
                throw new AppErrorException("订单不存在...");
            }
            return null;
        }

    }
    
    
    @Override
    @Transactional
    public Map<String,Object> doBargainOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        System.out.println("//////////////////////////////////");     
        System.out.println("orderId:"+orderId+",memberId:"+this.getMemberId(token)+",token:"+token);
        synchronized (orderId) {
            System.out.println("==================================");        
            BargainOrder bargainOrder=this.bargainOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
            if(bargainOrder!=null){
                if(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_not_pay")).equals(bargainOrder.getOrderStatus()) && 
                        Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")).equals(bargainOrder.getPayStatus())){
                    
                    if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
                        //预存款支付
                        if(this.advancePayFeign.pay(bargainOrder.getOrderAmount(), orderId,0,payPwd,token)){
                            
//                            this.giftOrderDao.updateOrderStatus(giftOrder.getOrderId(), 
//                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")), 
//                                    Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")), 
//                                    Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                            BargainOrder updateBargainOrder=new BargainOrder();
                            updateBargainOrder.setOrderId(bargainOrder.getOrderId());
                            updateBargainOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                            updateBargainOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                            updateBargainOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                            updateBargainOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")));
                            updateBargainOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "advancepay"));
                            updateBargainOrder.setPaymentType("advancepay");
                            updateBargainOrder.setPayAmount(bargainOrder.getOrderAmount());
                            updateBargainOrder.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            
                            this.bargainOrderDao.updateByPrimaryKeySelective(updateBargainOrder);
                            
//                            if(bargainOrder.getBargainType().equals(3)){
//                                
//                                synchronized (bargainOrder.getParentId()){
//                                    BigDecimal payAmount=this.bargainOrderDao.selectPayAmount(bargainOrder.getParentId());
//                                    if(payAmount.compareTo(bargainOrder.getFreePrice())>=0){
//                                        
//                                    }
//                                }
//                            }
                        }else{
                            throw new AppErrorException("支付失败...");
                        }
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")))){
                        
                        //IOS APP 微信支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.wxPayFeign.getAppPayInfo(bargainOrder.getOrderAmount(), bargainOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "bargain_order")),
                                    null, 
                                    token);
                        //H5 微信支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return this.wxPayFeign.getH5PayInfo(bargainOrder.getOrderAmount(), bargainOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "bargain_order")),
                                    null, 
                                    token);
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
                        //IOS APP 支付宝支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.aliPayFeign.getAppPayInfo(bargainOrder.getOrderAmount(), bargainOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "bargain_order")),
                                    null, 
                                    token);
                        //H5 支付宝支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return null;
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                    }else{
                        throw new AppErrorException("错误的支付方式...");
                    }

                }else{
                    throw new AppErrorException("该订单己支付过...");
                }
            }else{
                throw new AppErrorException("订单不存在...");
            }
            return null;
        }

    }
    
    
    @Override
    @Transactional
    public Map<String,Object> doGiftOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        System.out.println("//////////////////////////////////");     
        System.out.println("orderId:"+orderId+",memberId:"+this.getMemberId(token)+",token:"+token);
        synchronized (orderId) {
            System.out.println("==================================");        
            GiftOrder giftOrder=this.giftOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
            if(giftOrder!=null){
                if(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_not_pay")).equals(giftOrder.getOrderStatus()) && 
                        Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")).equals(giftOrder.getPayStatus())){
                    
                    if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
                        //预存款支付
                        if(this.advancePayFeign.pay(giftOrder.getOrderAmount(), orderId,0,payPwd,token)){
                            
//                            this.giftOrderDao.updateOrderStatus(giftOrder.getOrderId(), 
//                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")), 
//                                    Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")), 
//                                    Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                            GiftOrder updateGiftOrder=new GiftOrder();
                            updateGiftOrder.setOrderId(giftOrder.getOrderId());
                            updateGiftOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_allocation")));
                            updateGiftOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                            updateGiftOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                            updateGiftOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")));
                            updateGiftOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "advancepay"));
                            updateGiftOrder.setPaymentType("advancepay");
                            updateGiftOrder.setPayMoney(giftOrder.getOrderAmount());
                            updateGiftOrder.setPaymentTime(new Timestamp(System.currentTimeMillis()));
                            
                            this.giftOrderDao.updateByPrimaryKeySelective(updateGiftOrder);
                            
                            this.memberDao.updateMemberOfShopOwner(giftOrder.getMemberId(), 
                                    new Timestamp(System.currentTimeMillis()));
                        }else{
                            throw new AppErrorException("支付失败...");
                        }
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")))){
                        
                        //IOS APP 微信支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.wxPayFeign.getAppPayInfo(giftOrder.getOrderAmount(), giftOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")),
                                    null, 
                                    token);
                        //H5 微信支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return this.wxPayFeign.getH5PayInfo(giftOrder.getOrderAmount(), giftOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")),
                                    null, 
                                    token);
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                        
                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
                        //IOS APP 支付宝支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.aliPayFeign.getAppPayInfo(giftOrder.getOrderAmount(), giftOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "gift_order")),
                                    null, 
                                    token);
                        //H5 支付宝支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return null;
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                    }else{
                        throw new AppErrorException("错误的支付方式...");
                    }
                }else{
                    throw new AppErrorException("该订单己支付过...");
                }
            }else{
                throw new AppErrorException("订单不存在...");
            }
            return null;
        }

    }

    @Override
    @Transactional
    public Map<String,Object> doSpellOrderPay(Long orderId,Integer payType,Integer reqType,String payPwd,String token){
        synchronized (orderId) {
            Member member = this.getMember(token);
            SpellOrder spellOrder = spellOrderDao.selectByPrimaryKey(orderId);
            Spell spell = spellDao.selectByPrimaryKey(spellOrder.getSpellId());
            SpellActivity spellActivity = spellActivityDao.selectByPrimaryKey(spell.getActivityId());
            if(spellOrder != null){
                if(Integer.valueOf(0).equals(spellOrder.getOrderStatus()) &&
                        Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")).equals(spellOrder.getPayStatus())){
                    if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")))){
                        //预存款支付
                        if(this.advancePayFeign.pay(spellOrder.getOrderAmount(), orderId,0,payPwd,token)){
                            Long nowDate = System.currentTimeMillis();
                            if(spell.getOriginatorId().equals(member.getMemberId())){
                                if(spellActivity.getStartDate().getTime() > nowDate || spellActivity.getStatus() == 0){
                                    throw new AppErrorException("活动未开始");
                                }
                                if(spellActivity.getEndDate().getTime() <= nowDate || spellActivity.getStatus() == 2){
                                    throw new AppErrorException("活动已结束");
                                }
                                spell.setStartDate(new Timestamp(nowDate));
                                Long endDate = nowDate + (spellActivity.getCycle() * 3600 * 1000);
                                if(endDate < spellActivity.getEndDate().getTime()){
                                    spell.setEndDate(new Timestamp(endDate));
                                }else{
                                    spell.setEndDate(spellActivity.getEndDate());
                                }
                                spell.setStatus(1);
                            }else{
                                if(spell.getStartDate() == null || spell.getStartDate().getTime() > nowDate || spell.getStatus() != 1){
                                    throw new AppErrorException("活动未开始");
                                }
                                if(spell.getEndDate().getTime() <= nowDate){
                                    throw new AppErrorException("活动已结束");
                                }
                                String memberStr = ";memberId=" + member.getMemberId() + ",nickname=" + member.getNickname() + ",face=" + member.getFace();
                                String participateDetails = spell.getParticipateDetails() + memberStr;
                                spell.setParticipateDetails(participateDetails);
                                spell.setParticipateNum(spell.getParticipateNum() + 1);
                            }
                            if(spell.getCompleteNum() <= spell.getParticipateNum()){
                                spell.setStatus(2);
                                spellActivity.setCompleteNum(spellActivity.getCompleteNum() + 1);
                                spellActivityDao.updateByPrimaryKeySelective(spellActivity);
                            }

                            spellDao.updateByPrimaryKeySelective(spell);
                            spellOrder.setOrderStatus(Integer.valueOf(1));
                            spellOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                            spellOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                            spellOrder.setPaymentId(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "advancepay")));
                            spellOrder.setPaymentName(DictionaryUtil.getDataLabelByName("pay_type", "advancepay"));
                            spellOrder.setPaymentType("advancepay");
                            spellOrder.setPayAmount(spellOrder.getOrderAmount());
                            spellOrder.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                            this.spellOrderDao.updateByPrimaryKeySelective(spellOrder);

                        }else{
                            throw new AppErrorException("支付失败...");
                        }

                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "wxpay")))){

                        //IOS APP 微信支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.wxPayFeign.getAppPayInfo(spellOrder.getOrderAmount(), spellOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "spell_order")),
                                    null,
                                    token);
                            //H5 微信支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return this.wxPayFeign.getH5PayInfo(spellOrder.getOrderAmount(), spellOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "spell_order")),
                                    null,
                                    token);
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }

                    }else if(payType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_type", "alipay")))){
                        //IOS APP 支付宝支付
                        if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "ios_app")))){
                            return this.aliPayFeign.getAppPayInfo(spellOrder.getOrderAmount(), spellOrder.getOrderSn(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("order_type", "spell_order")),
                                    null,
                                    token);
                            //H5 支付宝支付
                        }else if(reqType.equals(Integer.valueOf(DictionaryUtil.getDataValueByName("req_type", "h5")))){
                            return null;
                        }else{
                            throw new AppErrorException("错误的请求类型...");
                        }
                    }else{
                        throw new AppErrorException("错误的支付方式...");
                    }
                }else{
                    throw new AppErrorException("该订单己支付过...");
                }
            }else{
                throw new AppErrorException("订单不存在...");
            }
            return null;
        }
    }

    @Override
    @Transactional
    public Map<String, String> refund(Long id, String type, String outRefundNo, Integer refundFee, Integer totalFee) {
        RightOrder rightOrder = rightOrderDao.selectByPrimaryKey(id);
        if(rightOrder==null){
            throw new AppErrorException("订单不存在...");
        }
        String seller = rightOrder.getSellerStatus();
        String dataSeller;
        String rightType = DictionaryUtil.getDataValueByName("right_type", "tuihuotuikuan");
        if(rightType.equals(rightOrder.getType())){
            dataSeller = DictionaryUtil.getDataValueByName("order_items_status", "items_daishouhuo");
        }else {
            dataSeller = DictionaryUtil.getDataValueByName("order_items_status", "items_daituikuan");
        }
        if(!seller.equals(dataSeller)){
            throw new AppErrorException("订单状态不符合...");
        }
        RightRel rightRel = new RightRel();
        rightRel.setRightId(id);
        rightRel = relDao.selectOne(rightRel);
        Long itemsId = rightRel.getItemsId();
        OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
        Order order = orderDao.selectByPrimaryKey(items.getOrderId());
        if(order==null){
            throw new AppErrorException("订单不存在...");
        }
        String dataPay = DictionaryUtil.getDataValueByName("pay_type", order.getPaymentType());
        if("0".equals(dataPay)){
            //预付款
            BigDecimal payAmount = rightOrder.getPrice();
            Long memberId = order.getMemberId();
            memberDao.updateMemberAdvanceOfAdd(memberId, payAmount);
            insertReceive(rightOrder);
            //订单状态，库存操作
            updateRightStatus(rightOrder);
        } else if("1".equals(dataPay)){
            //微信
            if("0".equals(type)){
                //H5
                Map<String,String> map = wxPayFeign.refundH5(outRefundNo,order.getOrderSn(),refundFee,totalFee);
                if("1".equals(String.valueOf(map.get("res_code")))||"FAIL".equals(map.get("return_code"))){
                    throw new AppErrorException("退款失败...");
                }
                //点击退款维权单状态改为退款中
                insertReceive(rightOrder);
                updateRightStatus(rightOrder);
                return map;
            }else{
                //app
            }
        }
        return null;
    }

    /**
     * 插入确认收货信息
     * @param rightOrder
     */
    private void insertReceive(RightOrder rightOrder){
        String tuikuan = DictionaryUtil.getDataValueByName("right_type", "tuihuotuikuan");
        if(tuikuan.equals(rightOrder.getType())){
            RightProcess process = new RightProcess();
            process.setCreateTime(new Timestamp(System.currentTimeMillis()));
            process.setRightStatusId(4L);
            process.setRightOrderId(rightOrder.getId());
            processDao.insert(process);
        }
    }

    /**
     * 修改维权单前后台状态
     * @param entity
     */
    @Transactional
    public void updateRightStatus(RightOrder entity) {
        //订单状态，库存操作
        Long statusIds;
        String orderType = entity.getType();
        String tuikuan = DictionaryUtil.getDataValueByName("right_type", "tuikuan");
        if(orderType.equals(tuikuan)){
            //仅退款
            statusIds = 10L;
        }else {
            //退货退款
            statusIds = 5L;
        }
        String status = DictionaryUtil.getDataValueByName("order_items_status", "items_tuikuansuccess");
        String sellerStatus = DictionaryUtil.getDataValueByName("order_items_status", "items_tuikuansuccess");
        entity.setStatus(status);
        entity.setSellerStatus(sellerStatus);
        RightProcess process = new RightProcess();
        process.setCreateTime(new Timestamp(System.currentTimeMillis()));
        process.setRightStatusId(statusIds);
        process.setRightOrderId(entity.getId());
        rightOrderDao.updateByPrimaryKey(entity);
        processDao.insert(process);
        //修改库存
        RightRel rel = new RightRel();
        rel.setRightId(entity.getId());
        List<RightRel> rightRelList = relDao.select(rel);
        List<OrderItems> orderItemsList = new ArrayList<>();
        for(RightRel rightRel : rightRelList){
            Long itemsId = rightRel.getItemsId();
            OrderItems items = itemsDao.selectByPrimaryKey(itemsId);
            orderItemsList.add(items);
        }
        for (OrderItems orderItems : orderItemsList) {
            productDao.updateStoreOfCut(orderItems.getProductId(), orderItems.getBuyCount()*-1);
            productStoreDao.updateStoreOfCut(orderItems.getProductId(), orderItems.getBuyCount()*-1);
        }
    }
}
