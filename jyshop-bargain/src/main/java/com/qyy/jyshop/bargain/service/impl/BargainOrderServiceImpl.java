package com.qyy.jyshop.bargain.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.bargain.feign.WechatMsgTemplateFeign;
import com.qyy.jyshop.bargain.service.BargainOrderService;
import com.qyy.jyshop.dao.BargainGoodsDao;
import com.qyy.jyshop.dao.BargainOrderDao;
import com.qyy.jyshop.dao.BargainOrderItemsDao;
import com.qyy.jyshop.dao.BargainRecomGoodsDao;
import com.qyy.jyshop.dao.MemberAddressDao;
import com.qyy.jyshop.dao.MemberBargainDao;
import com.qyy.jyshop.dao.ProductDao;
import com.qyy.jyshop.dao.WxMsgTemplateInfoDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.BargainGoods;
import com.qyy.jyshop.model.BargainOrder;
import com.qyy.jyshop.model.BargainOrderItems;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberAddress;
import com.qyy.jyshop.model.MemberBargain;
import com.qyy.jyshop.model.WxMsgTemplateInfo;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.BigDecimalUtil;
import com.qyy.jyshop.util.DateUtil;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.vo.WechatMsgTemplateData;

import net.sf.json.JSONObject;

@Service
public class BargainOrderServiceImpl extends AbstratService<BargainOrder> implements BargainOrderService{

    @Autowired
    private BargainOrderDao bargainOrderDao;
    @Autowired
    private BargainGoodsDao bargainGoodsDao;
    @Autowired
    private BargainOrderItemsDao bargainOrderItemsDao;
    @Autowired
    private MemberBargainDao memberBargainDao;
    @Autowired
    private BargainRecomGoodsDao bargainRecomGoodsDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MemberAddressDao memberAddressDao;
    @Autowired
    private WxMsgTemplateInfoDao wxMsgTemplateInfoDao;
    @Autowired
    private WechatMsgTemplateFeign wechatMsgTemplateFeign;
    
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public BigDecimal doMemberBargain(Long orderId,String token){

        BargainOrder bargainOrder=this.bargainOrderDao.selectByOrderId(orderId);
        if(bargainOrder!=null){
            
            if(!bargainOrder.getOrderStatus().equals(0)){
                throw new AppErrorException("该砍价订单己完成...");
            }
   
            Member member=this.getMember(token);
            
            if(bargainOrder.getMemberId().equals(member.getMemberId()))
                throw new AppErrorException("自己不能帮自己砍价...");
            
            synchronized (orderId) {
                Long nowDate=System.currentTimeMillis();  
                
                synchronized (member.getMemberId()) {
                    BargainOrderItems bargainOrderItems=this.bargainOrderItemsDao.selectByOrderId(orderId);
                    if(bargainOrderItems!=null){
    
                        if(bargainOrderItems.getGoodsPrice().compareTo(
                                bargainOrderItems.getTargetPrice().add(bargainOrderItems.getBargainPrices()))<=0){
                            throw new AppErrorException("该砍价任务己完成...");
                        }
                        System.out.println("砍价过期时间:"+bargainOrderItems.getTakeDate()+":"+
                                bargainOrderItems.getTakeDate().getTime());
                        if(bargainOrderItems.getTakeDate().getTime()<nowDate){
                            throw new AppErrorException("己过砍价任务时间...");
                        }
                        
                        if(!StringUtil.isEmpty(bargainOrderItems.getMemberIds())){
                            if(StringUtil.useLoop(bargainOrderItems.getMemberIds().split(","), member.getMemberId().toString())){
                                throw new AppErrorException("你已经帮该砍价任务砍过价,不能重复砍价...");
                            }    
                        }
                        MemberBargain memberBargain=new MemberBargain();
                        memberBargain.setBargainId(bargainOrderItems.getBargainId());
                        memberBargain.setGoodsId(bargainOrderItems.getGoodsId());
                        memberBargain.setOrderId(bargainOrderItems.getOrderId());
                        memberBargain.setOrderSn(bargainOrderItems.getOrderSn());
                        memberBargain.setMemberId(member.getMemberId());
                        memberBargain.setNickname(member.getNickname());
                        memberBargain.setFace(member.getWeixinFace());
                        memberBargain.setCreateDate(new Timestamp(nowDate));
                        
                        BigDecimal bargainPrice=bargainOrderItems.getPriceMin();
                        if(bargainOrderItems.getBargainMode().equals(0)){
                            
                        }else if(bargainOrderItems.getBargainMode().equals(1)){
                            bargainPrice=BigDecimalUtil.nextBigDecimal(bargainOrderItems.getPriceMin(), bargainOrderItems.getPriceMax(), 2);
                        }else{
                            throw new AppErrorException("错误的砍价方式...");
                        }
                        if(StringUtil.isEmpty(bargainOrderItems.getMemberIds()))
                            bargainOrderItems.setMemberIds(member.getMemberId()+",");
                        else
                            bargainOrderItems.setMemberIds(bargainOrderItems.getMemberIds()+member.getMemberId()+",");
                        
                        BigDecimal surplusPrices=bargainOrderItems.getBargainPrices().add(bargainOrderItems.getTargetPrice());
                        surplusPrices=bargainOrderItems.getGoodsPrice().subtract(surplusPrices);
                        if(bargainPrice.compareTo(surplusPrices)>0)
                            bargainPrice=surplusPrices;
                        
                        //设置砍价金额
                        memberBargain.setBargainPrice(bargainPrice);
                        bargainOrderItems.setBargainPrices(bargainOrderItems.getBargainPrices().add(memberBargain.getBargainPrice()));
                        bargainOrderItems.setBargainMoneys(bargainOrderItems.getBargainMoneys()+memberBargain.getBargainPrice()+",");
                        bargainOrderItems.setBargainNum(bargainOrderItems.getBargainNum()+1);
                        
                        memberBargain.setNum(bargainOrderItems.getBargainNum());
                        this.bargainOrderItemsDao.updateByPrimaryKeySelective(bargainOrderItems);
                        this.memberBargainDao.insertSelective(memberBargain);
                        
                        if(bargainOrderItems.getGoodsPrice().compareTo(
                                bargainOrderItems.getTargetPrice().add(bargainOrderItems.getBargainPrices()))<=0){
                            //修改砍价订单为己砍价完成状态
                            int i=this.bargainOrderDao.updateOfOrderStatus(orderId, 
                                    bargainOrder.getMemberId(),
                                    Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_not_pay")),
                                    bargainOrderItems.getTargetPrice());
                            if(i!=1){
                                throw new AppErrorException("修改砍价完成状态失败...");
                            }else{
                                
                                this.sendWxInfo(bargainOrder.getMemberId(), bargainOrderItems.getGoodsName(), bargainOrderItems.getTargetPrice());
                                if(bargainOrderItems.getBargainType().equals(1) || bargainOrderItems.getBargainType().equals(2)){
                                    if(!StringUtil.isEmpty(bargainOrderItems.getMemberIds())){
                                        
                                        String[] memberIdArray=bargainOrderItems.getMemberIds().split(",");
                                        for(String memberId:memberIdArray){
                                            this.sendWxInfo(Long.valueOf(memberId), bargainOrderItems.getGoodsName(), bargainOrderItems.getTargetPrice());
                                        }
                                    }
                                }
                            }
                        }
                        
                        return bargainPrice;
                    }else{
                        throw new AppErrorException("获取砍价订单砍价详情失败...");
                    }
                }
            }
            
        }else{
            throw new AppErrorException("获取砍价订单失败...");
        }
    }
    
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Map<String,Object> doCreateBargainOrder(Long bargainId, String token) {
        
//        token="0RNrMoQqjqndFtAweXWWyA==";
//        bargainId=12L;
        Member member=this.getMember(token);
        Map<String,Object> returnMap=new HashMap<String,Object>();
        
        BargainGoods bargainGoods=this.bargainGoodsDao.selectByBargainId(bargainId);

        if(bargainGoods!=null){
            Long nowDate=System.currentTimeMillis();
            if(bargainGoods.getStartDate().getTime()>nowDate)
                throw new AppErrorException("该砍价商品还未到开启时间...");
            
            if(bargainGoods.getEndDate().getTime()<nowDate)
                throw new AppErrorException("该砍价商品活动己结束...");
            
            synchronized(member.getMemberId()){
                
                if(this.bargainOrderItemsDao.selectCountByMemberIdAndBargainId(bargainId, member.getMemberId(),null)>0){
                    returnMap.put("res_info","该商品正在砍价中,快去邀请好友帮忙砍价吧...");
                    return returnMap;
                }
                if(bargainGoods.getBargainType().equals(0)){
                }else if(bargainGoods.getBargainType().equals(1) || bargainGoods.getBargainType().equals(2)){
                    if(this.bargainOrderItemsDao.selectCountByMemberIdAndBargainId(bargainId, null,member.getMemberId())>0){
                        if(bargainGoods.getBargainType().equals(1))
                            returnMap.put("res_info","您己参与了该任务的砍价,该任务是共享砍价,快去邀请好友帮忙砍价吧...");
                        else
                            returnMap.put("res_info","您己参与了该任务的砍价,该任务是免单砍价,快去邀请好友帮忙砍价吧...");
                        return returnMap;
                    }
                }else{
                    throw new AppErrorException("错误的砍价活动类型...");
                }
                
                BargainOrder bargainOrder=new BargainOrder();
                bargainOrder.setOrderSn("bg"+nowDate);
                bargainOrder.setMemberId(member.getMemberId());
                bargainOrder.setGoodsAmount(bargainGoods.getGoodsPrice());
                bargainOrder.setWeight(new BigDecimal(0));
                bargainOrder.setShipAmount(new BigDecimal(0));
                bargainOrder.setCreateDate(new Timestamp(nowDate));
                this.bargainOrderDao.insertSelective(bargainOrder);
                
                BargainOrderItems bargainOrderItems=new BargainOrderItems();
                bargainOrderItems.setOrderId(bargainOrder.getOrderId());
                bargainOrderItems.setOrderSn(bargainOrder.getOrderSn());
                bargainOrderItems.setBargainId(bargainGoods.getBargainId());
                bargainOrderItems.setGoodsId(bargainGoods.getGoodsId());
                bargainOrderItems.setGoodsName(bargainGoods.getTitle());
                bargainOrderItems.setGoodsImage(bargainGoods.getImage());
                bargainOrderItems.setBargainType(bargainGoods.getBargainType());
                bargainOrderItems.setGoodsPrice(bargainGoods.getGoodsPrice());
                bargainOrderItems.setTargetPrice(bargainGoods.getTargetPrice());
                bargainOrderItems.setIsFirst(bargainGoods.getIsFirst());
                bargainOrderItems.setFirstPriceMin(bargainGoods.getFirstPriceMin());
                bargainOrderItems.setFirstPriceMax(bargainGoods.getFirstPriceMax());
                bargainOrderItems.setBargainMode(bargainGoods.getBargainMode());
                bargainOrderItems.setPriceMin(bargainGoods.getPriceMin());
                bargainOrderItems.setPriceMax(bargainGoods.getPriceMax());
                bargainOrderItems.setIsFree(bargainGoods.getIsFree());
                bargainOrderItems.setTakeDate(new Timestamp(nowDate+bargainGoods.getTakeDate()*3600*1000));
                if(bargainOrderItems.getTakeDate().getTime()>bargainGoods.getEndDate().getTime())
                    bargainOrderItems.setTakeDate(bargainGoods.getEndDate());
                List<MemberBargain> memberBargainList=new ArrayList<MemberBargain>();
                if(bargainOrderItems.getIsFirst().equals(1)){
                    bargainOrderItems.setBargainPrices(BigDecimalUtil.nextBigDecimal(bargainOrderItems.getFirstPriceMin(), 
                            bargainOrderItems.getFirstPriceMax(), 2));
                    bargainOrderItems.setBargainMoneys(bargainOrderItems.getBargainPrices()+",");
                    bargainOrderItems.setBargainNum(1);
                }
                bargainOrderItems.setFreePrice(bargainGoods.getFreePrice());
                this.bargainOrderItemsDao.insertSelective(bargainOrderItems);
                
                if(bargainOrderItems.getIsFirst().equals(1)){
                    MemberBargain memberBargain=new MemberBargain();
                    memberBargain.setBargainId(bargainId);
                    memberBargain.setGoodsId(bargainOrderItems.getGoodsId());
                    memberBargain.setOrderId(bargainOrderItems.getOrderId());
                    memberBargain.setOrderSn(bargainOrderItems.getOrderSn());
                    memberBargain.setMemberId(member.getMemberId());
                    memberBargain.setNickname(member.getNickname());
                    memberBargain.setFace(member.getFace());
                    memberBargain.setBargainPrice(bargainOrderItems.getBargainPrices());
                    memberBargain.setNum(1);
                    memberBargain.setCreateDate(new Timestamp(nowDate));
                    
                    this.memberBargainDao.insertSelective(memberBargain);
                    memberBargainList.add(memberBargain);
                }
                
                returnMap.put("member", member);
                bargainOrderItems.setBuyNum(this.bargainOrderItemsDao.selectCountByCompleteAndBargainId(bargainId));
                bargainOrderItems.setDetails(bargainGoods.getDetails());
                bargainOrderItems.setShareTitle(bargainGoods.getShareTitle());
                bargainOrderItems.setShareDescribe(bargainGoods.getShareDescribe());
                bargainOrderItems.setRule(bargainGoods.getRule());
                returnMap.put("bargainGoods", bargainOrderItems);
                returnMap.put("memberBargainList", memberBargainList);
                returnMap.put("goodsList", bargainRecomGoodsDao.selectGoodsList());
                returnMap.put("type", 1);
                return returnMap;
            }
        }else{
            throw new AppErrorException("获取砍价活动商品失败,创建砍价团失败..."); 
        }
    }
    
    @Transactional
    @Override
    public BigDecimal doEditOrderAddress(String token,Long addrId,Long orderId){
        
//        token="d3yGIsAUyByVe9FhBV2rmw==";
//        orderId=343L;
//        addrId=192L;
        Long memberId=this.getMemberId(token);
        
        System.out.println("orderId:"+orderId+",addrId:"+addrId+",memberId:"+memberId);
        BargainOrder bargainOrder=this.bargainOrderDao.selectMemberOrder(orderId, memberId);
        
        if(bargainOrder==null || StringUtil.isEmpty(bargainOrder.getOrderStatus()))
            throw new AppErrorException("获取砍价任务失败...");
        
        if(bargainOrder.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_new"))))
            throw new AppErrorException("该砍价任务还没示完砍价标靶,快去邀请好友帮助砍价吧...");
        
        if(!bargainOrder.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_not_pay"))))
            throw new AppErrorException("该砍价任务己完成,不能下单...");
        BigDecimal orderAmount=bargainOrder.getOrderAmount();
        Integer bargainType=bargainOrder.getBargainType();
        if(StringUtil.isEmpty(orderAmount))
            throw new AppErrorException("获取订单金额失败...");
        Long parentId=bargainOrder.getParentId();
        BigDecimal freePrice=bargainOrder.getFreePrice();
        bargainOrder=new BargainOrder();
        bargainOrder.setOrderId(orderId);
        MemberAddress memberAddress =this.memberAddressDao.selectByAddrIdAndMemberId(addrId,memberId);
        if(memberAddress!=null){
            bargainOrder.setShipId(1);
            bargainOrder.setShipType("快递");
            bargainOrder.setShipAddress(memberAddress.getProvinceName()+memberAddress.getCityName()+
                    memberAddress.getDistrictName()+memberAddress.getAddressDetail());
            bargainOrder.setShipProvinceId(memberAddress.getProvinceId());
            bargainOrder.setShipProvinceName(memberAddress.getProvinceName());
            bargainOrder.setShipCityId(memberAddress.getCityId());
            bargainOrder.setShipCityName(memberAddress.getCityName());
            bargainOrder.setShipDistrictId(memberAddress.getDistrictId());
            bargainOrder.setShipDistrictName(memberAddress.getDistrictName());
            bargainOrder.setShipName(memberAddress.getName());
            bargainOrder.setShipAddr(memberAddress.getAddressDetail());
            bargainOrder.setShipZip(memberAddress.getZip());
            bargainOrder.setShipMobile(memberAddress.getMobile());
            bargainOrder.setShipTel(memberAddress.getTel());
                
        }else
            throw new AppErrorException("获取收货地址失败...");
        //判断是否免单砍价
        if(bargainType.equals(2)){

            synchronized (parentId){
                BigDecimal payAmount=this.bargainOrderDao.selectPayAmount(orderId);
                if(payAmount.compareTo(freePrice)>=0){
                    bargainOrder.setPayAmount(orderAmount);
                    bargainOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_confirm")));
                    bargainOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                    bargainOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                    bargainOrder.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                    this.bargainOrderDao.updateByPrimaryKeySelective(bargainOrder);
                    return new BigDecimal(0);
                }
            }
            
        }else{
            if(orderAmount.compareTo(new BigDecimal(0))==0){
                bargainOrder.setPayAmount(bargainOrder.getOrderAmount());
                bargainOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_allocation")));
                bargainOrder.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_yes")));
                bargainOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_yes")));
                bargainOrder.setPaymentDate(new Timestamp(System.currentTimeMillis()));
                this.bargainOrderDao.updateByPrimaryKeySelective(bargainOrder);
                return bargainOrder.getPayAmount();
            }
        }
        this.bargainOrderDao.updateByPrimaryKeySelective(bargainOrder);
        return orderAmount;
    }
    
    @Override
    public Map<String,Object> queryBargainDetail(Long orderId,String token){
//        orderId=196L;
//        token="0rYO6RIRkOegZyNQsV1g/w==";
        Member member =this.getMember(token);
        
        System.out.println("orderId:"+orderId+",token:"+token);
        
        Map<String,Object> returnMap=new HashMap<String,Object>();
        
        Map<String,Object> bargainDetail=this.bargainOrderDao.selectBargainDetail(orderId);
        if(bargainDetail!=null && !StringUtil.isEmpty(bargainDetail.get("orderId"))){
            bargainDetail.put("buyNum", 
                    this.bargainOrderItemsDao.selectCountByCompleteAndBargainId(Long.valueOf(bargainDetail.get("bargainId").toString())));
            returnMap.put("member", member);
            if(member.getMemberId().equals(Long.valueOf(bargainDetail.get("memberId").toString())))
                returnMap.put("type", 1);
            else
                returnMap.put("type", 2);
            returnMap.put("bargainGoods", bargainDetail);
            returnMap.put("memberBargainList", this.memberBargainDao.selectByOrderId(Long.valueOf(bargainDetail.get("orderId").toString())));
            returnMap.put("goodsList", bargainRecomGoodsDao.selectGoodsList());
        }else{
            throw new AppErrorException("获取订单详情失败...");
        }
        return returnMap;
    }
    
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    @Override
    public Map<String,Object> doBargainOrderCheckout(Long orderId,Long productId,String token){
        
//        orderId=343L;
//        token="X0CSvvQIGUZ0Po8uI0IiFA==";
//        productId=4626L;
        Long memberId=this.getMemberId(token);
        synchronized(memberId){
            
            System.out.println("orderId:"+orderId+",productId:"+productId+",memberId:"+memberId);
            BargainOrder bargainOrder=this.bargainOrderDao.selectByOrderId(orderId);
            
            if(bargainOrder==null)
                throw new AppErrorException("砍价任务不存在...");
            
            if(bargainOrder.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_new"))))
                throw new AppErrorException("该砍价任务还没示完砍价标靶,快去邀请好友帮助砍价吧...");
            
            //获取砍价任务详情
            BargainOrderItems bargainOrderItems=this.bargainOrderItemsDao.selectByOrderId(orderId);
            
            if(bargainOrderItems==null)
                throw new AppErrorException("获取砍价任务详情失败,下单失败...");
            
            //根据砍价任务类型做判断
            if(bargainOrderItems.getBargainType().equals(0)){
                
                if(!bargainOrder.getMemberId().equals(memberId))
                    throw new AppErrorException("该砍价任务不是您发起的,下单失败...");
                
                if(!bargainOrder.getOrderStatus().equals(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_not_pay"))))
                    throw new AppErrorException("该砍价任务己完成,不能重复领取...");
                
            }else if(bargainOrderItems.getBargainType().equals(1) || bargainOrderItems.getBargainType().equals(2)){
            
                if(!bargainOrder.getMemberId().equals(memberId)){
                    BargainOrder childBargainOrder=this.bargainOrderDao.selectByParentIdAndMemberId(orderId, memberId);
                    
                    if(childBargainOrder==null){
                        Long nowData=0L;
                        synchronized(this){
                            nowData=System.currentTimeMillis();
                        }
                        bargainOrder.setParentId(bargainOrder.getOrderId());
                        bargainOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status", "order_not_pay")));
                        bargainOrder.setPayStatus(0);
                        bargainOrder.setShipStatus(0);
                        bargainOrder.setOrderSn("bg"+nowData);
                        bargainOrder.setMemberId(memberId);
                        bargainOrder.setCreateDate(new Timestamp(nowData));
                        bargainOrder.setOrderId(null);
                        this.bargainOrderDao.insertSelective(bargainOrder);
                        
                        bargainOrderItems.setOrderId(bargainOrder.getOrderId());
                        bargainOrderItems.setOrderSn(bargainOrder.getOrderSn());
                        bargainOrderItems.setMemberIds(null);
                        bargainOrderItems.setItemId(null);
                        this.bargainOrderItemsDao.insertSelective(bargainOrderItems);
                    }else{
                        bargainOrder=childBargainOrder;
                    }
                }
            }else{
                throw new AppErrorException("错误的砍价活动类型...");
            }
            
            //获取砍价商品详情
            Map<String,Object> goodsMap=this.productDao.selectByProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
            
            if(goodsMap==null)
                throw new AppErrorException("获取砍价商品出错,无法下单,可能该商品己下架…………");
            
    //        Integer store=(Integer)goodsMap.get("store");
    //     
    //        if(StringUtil.isEmpty(store) || store<=0)
    //            throw new AppErrorException("砍价商品"+goodsMap.get("name")+"库存不足…………");
    
            Map<String,Object> bargainDetail=this.bargainOrderDao.selectBargainDetail(orderId);
            
            if(bargainDetail==null || StringUtil.isEmpty(bargainDetail.get("goodsId")))
                throw new AppErrorException("获取砍价信息失败…………");
            
            if(!bargainDetail.get("goodsId").toString().equals(goodsMap.get("goodsId").toString()))
                throw new AppErrorException("下单货品不是砍价商品对应货品,下单失败…………");
            
            bargainOrder.setOrderAmount(new BigDecimal(bargainDetail.get("targetPrice").toString()));
            goodsMap.put("price", bargainDetail.get("goodsPrice"));
            goodsMap.put("buyCount", 1);
            Map<String,Object> returnMap=new HashMap<String, Object>();
            returnMap.put("memberAddressList", this.memberAddressDao.selectByMemberId(memberId));
            returnMap.put("goods", goodsMap);
            returnMap.put("bargainOrder", bargainOrder);
            return returnMap;
        }
    }
    
	@Override
	public Map<String, Object> selectBargainOrderList(String token, Integer pageNo, Integer pageSize) {
		Map<String, Object> map = new HashMap<>();
		Member member=this.getMember(token);
        ParamData params = this.getParamData(pageNo,pageSize);
        params.put("memberId",  member.getMemberId());
        map.put("bargainOrderList",this.pageQuery("BargainOrderItemsDao.selectBargainOrderList", params));
        //map.put("participateBargainOrderList", this.pageQuery("BargainOrderItemsDao.selectParticipateBargainOrderList", params));
        return map;
	}
	
	@Transactional
	@Override
	public void doBargainOrderRog(String token,Long orderId){
//	    orderId=359L;
//        token="Qvo8ol8N1VZEozCHVwIDrg==";
	    if(StringUtil.isEmpty(orderId))
            throw new AppErrorException("订单Id不能为空...");
	    
        BargainOrder bargainOrder=this.bargainOrderDao.selectMemberOrder(orderId, this.getMemberId(token));
        
        if(bargainOrder!=null){
            
            if(DictionaryUtil.getDataValueByName("bargain_order_status","order_ship").
                    equals(String.valueOf(bargainOrder.getOrderStatus())) &&
               DictionaryUtil.getDataValueByName("pay_status","pay_yes").
                    equals(String.valueOf(bargainOrder.getPayStatus())) &&
               DictionaryUtil.getDataValueByName("ship_status","ship_yes").
                    equals(String.valueOf(bargainOrder.getShipStatus()))){
                
                BargainOrder updateOrder=new BargainOrder();
                updateOrder.setOrderId(bargainOrder.getOrderId());
                updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("bargain_order_status","order_rog")));
                updateOrder.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status","ship_receipt")));
                updateOrder.setSigningDate(new Timestamp(System.currentTimeMillis()));
                this.update(updateOrder);
                System.out.println("订单确认收货...");
            }else
                throw new AppErrorException("订单状态错误,收货失败..."); 
            
        }else
            throw new AppErrorException("该订单不存在...");
	}
	
	/**
	 * 微信消息推送
	 * @author hwc
	 * @created 2018年4月25日 上午9:31:41
	 * @param memberId
	 * @param goodsName
	 * @param targetPrice
	 */
	private void sendWxInfo(Long memberId,String goodsName,BigDecimal targetPrice){
	    WxMsgTemplateInfo wmti = this.wxMsgTemplateInfoDao.selectByPrimaryKey(3L);
        
        
        Map<String, WechatMsgTemplateData> param = new HashMap<>();
        param.put("first", new WechatMsgTemplateData("您的砍价已完成。", "#696969"));
        param.put("keyword1", new WechatMsgTemplateData(goodsName, "#696969"));
        param.put("keyword2", new WechatMsgTemplateData(targetPrice.toString(), "#696969"));
        param.put("remark", new WechatMsgTemplateData("您的砍价已于"+DateUtil.getCurDate()+"完成，请及时领取！", "#696969"));
        
        
        JSONObject fromObject = JSONObject.fromObject(param);
        
        try {
            this.wechatMsgTemplateFeign.sendWechatMsgToUser(
                    memberId.toString(),
                    wmti.getTemplateId(), 
                    wmti.getCallUrl(),
                    "#000000", 
                    fromObject.toString(), 
                    "CHEERTEA");
        } catch (Exception e) {
            
        }
	}
	/*public PageAjax<Map<String, Object>> selectParticipateBargainOrderList(String token, Integer pageNo,
			Integer pageSize) {
		Member member=this.getMember(token);
        ParamData params = this.getParamData(pageNo,pageSize);
        params.put("memberId",  member.getMemberId());
        return this.pageQuery("BargainOrderItemsDao.selectParticipateBargainOrderList", params);
	}*/


}
