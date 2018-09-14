package com.qyy.jyshop.spell.service.impl;

import com.qyy.jyshop.dao.*;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.*;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.spell.service.SpellService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class SpellServiceImpl extends AbstratService<Spell> implements SpellService {

    @Autowired
    private SpellActivityDao spellActivityDao;

    @Autowired
    private SpellDao spellDao;

    @Autowired
    private SpellOrderDao spellOrderDao;

    @Autowired
    private SpellOrderItemsDao spellOrderItemsDao;

    @Autowired
    private SpellDeliveryDao spellDeliveryDao;

    @Autowired
    private SpellRecomGoodsDao spellRecomGoodsDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private MemberAddressDao memberAddressDao;

    @Autowired
    private DlyTypeDao dlyTypeDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private GoodsSpecDao goodsSpecDao;

    @Override
    public Map<String, Object> getSpellActivityPage(Integer pageNo, Integer pageSize, Integer sortType, Integer sortWay){
        Map<String, Object> pageMap = new HashMap<String, Object>();
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        if(sortType == null){
            sortType = 0;
        }
        if(sortWay == null) {
            sortWay = 0;
        }
        List<Map<String, Object>> recommendedList = spellActivityDao.getSpellActivityByRecommended();
        pageMap.put("recommendedList", recommendedList);
        ParamData params = this.getParamData(pageNo, pageSize);
        params.put("sortType", sortType);
        params.put("sortWay", sortWay);
        PageAjax<Map<String,Object>> pageAjax = this.pageQuery("SpellActivityDao.getSpellActivityList", params);
        pageMap.put("spellActivityList", pageAjax.getRows());
        if(pageAjax.isIsLastPage()){
            pageMap.put("nextPage", 0);
        }else{
            pageMap.put("nextPage", pageAjax.getNextPage());
        }
        return pageMap;
    }

    @Override
    public Map<String,Object> getSpellActivityByActivityId(Long activityId, String token) {
        Map<String,Object> activityMap = spellActivityDao.getSpellActivityByActivityId(activityId);
        if(activityMap == null){
            throw new AppErrorException("没有该拼团活动");
        }
        List<Map<String,Object>> spellList = spellDao.getOngoingByActivityId(activityId);
        for(Map<String, Object> map : spellList){
            List<Map<String, Object>> list = getParticipateDetails(map.get("participateDetails").toString());
            map.put("participateDetails", list);
        }
        activityMap.put("Ongoing",spellList);
        Boolean isParticipate = true;
        if(token != null){
            Member member = this.getMember(token);
            Integer count = spellDao.countMySpellByActivityId(member.getMemberId(), activityId);
            Integer limited = Integer.valueOf(activityMap.get("limited").toString());
            if(count >= limited){
                isParticipate = false;
            }
        }
        activityMap.put("isParticipate", isParticipate);
        return activityMap;
    }

    @Override
    public Map<String,Object> getOrderCheckout(Long activityId, Long productId, String token) {
        Map<String, Object> model = new HashMap<String, Object>();
        Member member = this.getMember(token);
        SpellActivity activity = spellActivityDao.findByActivityId(activityId);
        Integer count = spellDao.countMySpellByActivityId(member.getMemberId(), activityId);
        if(activity.getLimited() <= count){
            throw new AppErrorException("不能重复参加拼团");
        }
        Long nowDate = System.currentTimeMillis();
        Long startDate = activity.getStartDate().getTime();
        Long endDate = activity.getEndDate().getTime();
        if(nowDate < startDate || activity.getStatus() == 0){
            throw new AppErrorException("拼团活动未开始");
        }
        if(nowDate >= endDate || activity.getStatus() == 2){
            throw new AppErrorException("拼团活动已经结束");
        }
        if(activity.getStore() < 1){
            throw new AppErrorException("活动商品库存不足");
        }
//        Goods goods = goodsDao.selectByGoodsId(activity.getGoodsId());
//        Map<String, Object> goodsMap = productDao.selectByProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
//        Map<String, Object> comMap = new HashMap<String, Object>();
        Product product = productDao.selectByPrimaryKey(productId);

        List<MemberAddress> memberAdddressList=this.memberAddressDao.selectByMemberId(member.getMemberId());
        model.put("memberAddressList", memberAdddressList);

        String comName = null;
        if(!StringUtil.isEmpty(product.getComId()) &&
                product.getComId().toString().equals("0")){
            comName = this.companyDao.selectStoreName(0);
        }else{
            if(!StringUtil.isEmpty(product.getComId())){
                comName = this.companyDao.selectStoreName(
                        Integer.valueOf(product.getComId().toString()));
            }
        }
        Map<String, Object> spellMap = new HashMap<String, Object>();
        spellMap.put("comId", product.getComId());
        spellMap.put("comName", comName);
        spellMap.put("name", activity.getName());
        spellMap.put("productId", productId);
        spellMap.put("goodsId", product.getGoodsId());
        spellMap.put("goodsName", activity.getGoodsName());
        spellMap.put("goodsTitle", activity.getGoodsTitle());
        spellMap.put("productSn", product.getSn());
        spellMap.put("store", activity.getStore());
        spellMap.put("specs", product.getSpecs());
        spellMap.put("goodsPrice", activity.getGoodsPrice());
        spellMap.put("spellPrice", activity.getSpellPrice());
        spellMap.put("image", activity.getImage2());

//        goodsMap.put("comName", comName);
//        goodsMap.put("activityId",activityId);
//        model.put("com", comMap);
        model.put("spell", spellMap);

        BigDecimal weight = product.getWeight();
        String provinceId = this.memberAddressDao.selectDefOfProvinceId(this.getMemberId(token));
        BigDecimal shipAmount = calculateFreight(activity.getDlyTypeId(), weight, provinceId);
        model.put("shipAmount", shipAmount);
        return model;
    }

    @Override
    public BigDecimal getShipAmount(Long activityId, Long memberAddressId, String token){
        SpellActivity activity = spellActivityDao.findByActivityId(activityId);
        Goods goods = goodsDao.selectByGoodsId(activity.getGoodsId());
        Member member = this.getMember(token);
        MemberAddress memberAddress =this.memberAddressDao.selectByAddrIdAndMemberId(memberAddressId,member.getMemberId());
        return calculateFreight(activity.getDlyTypeId(), goods.getWeight(), memberAddress.getProvinceId());
    }

    public BigDecimal calculateFreight(Long dlyTypeId, BigDecimal weight, String provinceId){
        BigDecimal freight = new BigDecimal(0);

        if(!dlyTypeId.equals("0")){
            weight = weight == null ? new BigDecimal(0) : weight;
        }else{
            return freight;
        }

//        String provinceId = this.memberAddressDao.selectDefOfProvinceId(this.getMemberId(token));

        if(StringUtil.isEmpty(provinceId))
            return freight;

        DlyType dlyType=this.dlyTypeDao.selectByTypeId(dlyTypeId);
        if(dlyType!=null){
            //判断是否指定地区配置
            if(dlyType.getIsSame().equals(0)){

                List<DlyTypeArea> dlyTypeAreaList=dlyType.getDlyTypeAreaList();

                if(dlyTypeAreaList!=null && dlyTypeAreaList.size()>0){

                    endFreight:for (DlyTypeArea dlyTypeArea : dlyTypeAreaList) {
                        if(!StringUtil.isEmpty(dlyTypeArea.getAreaIdGroup())){
                            if(Arrays.asList(dlyTypeArea.getAreaIdGroup().split(",")).contains(provinceId)){
                                //是否包邮
                                if(dlyTypeArea.getFreeDly().equals(1)){
                                    return freight;
                                }else{
                                    dlyType.setFirstWeight(dlyTypeArea.getFirstWeight());
                                    dlyType.setFirstWeightPrice(dlyTypeArea.getFirstWeightPrice());
                                    dlyType.setAdditionalWeight(dlyTypeArea.getAdditionalWeight());
                                    dlyType.setAdditionalWeightPrice(dlyTypeArea.getAdditionalWeightPrice());
                                    break endFreight;
                                }
                            }
                        }
                    }
                }
            }

            freight=freight.add(dlyType.getFirstWeightPrice());

            dlyType.setFirstWeight(dlyType.getFirstWeight().multiply(new BigDecimal(1000)).setScale(0, BigDecimal.ROUND_DOWN));
            dlyType.setAdditionalWeight(dlyType.getAdditionalWeight().multiply(new BigDecimal(1000)).setScale(0, BigDecimal.ROUND_DOWN));
            //如果大于首重,则计算续重费用
            if(weight.compareTo(dlyType.getFirstWeight())>0){

                //续重
                weight=weight.subtract(dlyType.getFirstWeight());

                weight=weight.divide(dlyType.getAdditionalWeight());
                weight=weight.setScale(0,BigDecimal.ROUND_UP);
                weight=weight.multiply(dlyType.getAdditionalWeightPrice());
                freight=freight.add(weight);
            }
        }
        return freight.setScale(2, BigDecimal.ROUND_DOWN);
    }

    @Override
    @Transactional
    public Map<String, Object> createSpellOrder(Long activityId, Long productId, Long memberAddressId, String remark, String token){
        Map<String, Object> orderMap = new HashMap<String, Object>();
        Member member = this.getMember(token);
        MemberAddress memberAddress =this.memberAddressDao.selectByAddrIdAndMemberId(memberAddressId,member.getMemberId());
        SpellActivity activity = spellActivityDao.findByActivityId(activityId);
        if(activity == null){
            throw new AppErrorException("拼团活动不存在");
        }
        Integer count = spellDao.countMySpellByActivityId(member.getMemberId(), activityId);
        if(activity.getLimited() <= count){
            throw new AppErrorException("不能重复参加拼团");
        }
        Long nowDate = System.currentTimeMillis();
        Long startDate = activity.getStartDate().getTime();
        Long endDate = activity.getEndDate().getTime();
        if(nowDate < startDate || activity.getStatus() == 0){
            throw new AppErrorException("拼团活动未开始");
        }
        if(nowDate >= endDate || activity.getStatus() == 3){
            throw new AppErrorException("拼团活动已经结束");
        }
        if(activity.getStore() < 1){
            throw new AppErrorException("活动商品库存不足");
        }
//        Map<String, Object> goodsMap = productDao.selectByProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));

        Product product = productDao.selectByPrimaryKey(productId);
//        String endDateStr = DateUtil.getDateStringOfHour(activity.getCycle(),DateUtil.yyyy_MM_dd_HH_mm_ss_EN);
        String participateDetails = "memberId=" + member.getMemberId() + ",nickname=" + member.getNickname() + ",face=" + member.getFace();

        Integer store = activity.getStore() - 1;
        Integer realNum = activity.getRealNum() + 1;

        synchronized (store) {
            activity.setStore(store);
            activity.setRealNum(realNum);
            spellActivityDao.updateByPrimaryKey(activity);
        }

        Spell spell = new Spell();
        spell.setCreateDate(new Timestamp(nowDate));
        spell.setActivityId(activityId);
        spell.setImage2(activity.getImage2());
        spell.setOriginatorId(member.getMemberId());
        spell.setOriginatorName(member.getNickname());
        spell.setOriginatorFace(member.getFace());
        spell.setComId(activity.getComId());
        spell.setShopStoreId(activity.getShopStoreId());
//        spell.setStartDate(new Timestamp(System.currentTimeMillis()));
//        spell.setEndDate(Timestamp.valueOf(endDateStr));
        spell.setSpellType(activity.getSpellType());
        spell.setName(activity.getName());
        spell.setGoodsId(activity.getGoodsId());
        spell.setGoodsName(activity.getGoodsName());
        spell.setGoodsTitle(activity.getGoodsTitle());
        spell.setGoodsPrice(activity.getGoodsPrice());
        spell.setSpellPrice(activity.getSpellPrice());
//        spell.setProductId(productId);
//        spell.setProductSpecs(goodsMap.get("specs").toString());
        spell.setCompleteNum(activity.getNum());
        spell.setParticipateNum(1);
        spell.setParticipateDetails(participateDetails);
        spell.setStatus(0);
        spellDao.insertSelective(spell);
        if(spell.getSpellId() == null){
            throw new AppErrorException("创建拼团失败");
        }

        BigDecimal weight = product.getWeight();
        BigDecimal shipAmount = calculateFreight(activity.getDlyTypeId(), weight, memberAddress.getProvinceId());
        SpellOrder order = new SpellOrder();
        order.setCreateDate(new Timestamp(nowDate));
        order.setLastModify(new Timestamp(nowDate));
        synchronized (nowDate) {
            order.setOrderSn(String.valueOf(nowDate));
        }
        order.setSpellId(spell.getSpellId());
        order.setSpellType(spell.getSpellType());
        order.setMemberId(member.getMemberId());
        order.setOrderStatus(0);
        order.setPayStatus(0);
        order.setShipStatus(0);
//        order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("spell_order_status", "order_not_pay")));
//        order.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")));
//        order.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_no")));
        order.setWeight(weight);
        order.setShipAmount(shipAmount);
        order.setGoodsAmount(activity.getSpellPrice());
        order.setOrderAmount(activity.getSpellPrice().add(shipAmount));
        order.setPayAmount(new BigDecimal(0));
        order.setIsFree(false);
        order.setShipName(memberAddress.getName());
        order.setShipAddr(memberAddress.getAddressDetail());
        order.setShipZip(memberAddress.getZip());
        order.setShipMobile(memberAddress.getMobile());
        order.setShipTel(memberAddress.getTel());
        order.setShipProvinceId(memberAddress.getProvinceId());
        order.setShipCityId(memberAddress.getCityId());
        order.setShipDistrictId(memberAddress.getDistrictId());
        order.setShipProvinceName(memberAddress.getProvinceName());
        order.setShipCityName(memberAddress.getCityName());
        order.setShipDistrictName(memberAddress.getDistrictName());
        order.setRemark(remark);
        spellOrderDao.insertSelective(order);

        SpellOrderItems orderItems = new SpellOrderItems();
        orderItems.setCreateDate(new Timestamp(nowDate));
        orderItems.setLastModify(new Timestamp(nowDate));
        orderItems.setOrderId(order.getOrderId());
        orderItems.setOrderSn(order.getOrderSn());
        orderItems.setGoodsId(activity.getGoodsId());
        orderItems.setGoodsName(activity.getGoodsName());
        orderItems.setGoodsImage(activity.getImage2());
        orderItems.setProductId(productId);
        orderItems.setProductSn(product.getSn());
        orderItems.setProductSpecs(product.getSpecs());
        orderItems.setGoodsPrice(activity.getSpellPrice());
        orderItems.setComId(activity.getComId());
        orderItems.setShopStoreId(activity.getShopStoreId());
        orderItems.setWeight(weight);
        spellOrderItemsDao.insert(orderItems);
        orderMap.put("spellId", spell.getSpellId());
        orderMap.put("orderId", order.getOrderId());
        return orderMap;
    }

    @Override
    public List<Map<String, Object>> getSpellActivityByGoodsId(Long goodsId){
        Goods goods = goodsDao.selectByGoodsId(goodsId);
        if(goods == null){
            throw new AppErrorException("商品不存在");
        }
        return spellActivityDao.getSpellActivityByShopStoreId(Long.valueOf(goods.getShopStoreId()));
    }

    @Override
    public Map<String, Object> getMyInitiateSpell(Integer pageNo, Integer pageSize, String token){
        Map<String, Object> pageMap = new HashMap<String, Object>();
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Member member = this.getMember(token);
        ParamData params = this.getParamData(pageNo, pageSize);
        params.put("memberId", member.getMemberId());
        PageAjax<Map<String,Object>> pageAjax = this.pageQuery("SpellDao.getMyInitiateSpell", params);
        pageMap.put("spellList", pageAjax.getRows());
        if(pageAjax.isIsLastPage()){
            pageMap.put("nextPage", 0);
        }else{
            pageMap.put("nextPage", pageAjax.getNextPage());
        }
        return pageMap;
//        return pageAjax.getRows();
    }

    @Override
    public Map<String, Object> getMyParticipateSpell(Integer pageNo, Integer pageSize, String token){
        Map<String, Object> pageMap = new HashMap<String, Object>();
        if(pageNo == null){
            pageNo = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Member member = this.getMember(token);
        ParamData params = this.getParamData(pageNo, pageSize);
        String memberStr = ";memberId=" + member.getMemberId()+",";
        params.put("memberStr", memberStr);
        params.put("memberId", member.getMemberId());
        PageAjax<Map<String,Object>> pageAjax = this.pageQuery("SpellDao.getMyParticipateSpell", params);
        pageMap.put("spellList", pageAjax.getRows());
        if(pageAjax.isIsLastPage()){
            pageMap.put("nextPage", 0);
        }else{
            pageMap.put("nextPage", pageAjax.getNextPage());
        }
        return pageMap;
//        return pageAjax.getRows();
    }

    @Override
    public Map<String, Object> getSellDetail(Long spellId){
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> spellMap = spellDao.getSpellDetail(spellId);
        List<Map<String, Object>> list = getParticipateDetails(spellMap.get("participateDetails").toString());
        spellMap.put("participateDetails", list);
        map.put("spell", spellMap);
        List<Map<String, Object>> recommendedList = spellRecomGoodsDao.getGoodsList();
        map.put("recomendedList", recommendedList);
        map.put("goodsSpec",getGoodsSpec(Long.valueOf(spellMap.get("goodsId").toString())));
        return map;
    }

    @Override
    public Map<String, Object> getOrderDetail(Long spellId, String token){
        Member member = this.getMember(token);
        Map<String, Object> orderMap = spellOrderDao.getOrderDetail(spellId, member.getMemberId());
        List<Map<String, Object>> participateDetailsList = getParticipateDetails(orderMap.get("participateDetails").toString());
        orderMap.put("participateDetails", participateDetailsList);
//        SpellDelivery delivery = spellDeliveryDao.selectLogiByOrderId((Long)orderMap.get("orderId"));
//        Map<String, Object> map = new HashMap<String, Object>();
//        String logiCode = delivery.getLogiCode();
//        String logiNo = delivery.getLogiNo();
//        String logiName = delivery.getLogiName();
//        String param ="{\"com\":\""+logiCode+"\",\"num\":\""+logiNo+"\"}";
//        //String param ="{\"com\":\"zhongtong\",\"num\":\"474311891677\"}";
//        String customer ="065EE38CB55838D4C58BF447FA9C9F68";
//        String key = "gFHvkFcD1808";
//        String sign = MD5.encode(param+key+customer);
//        HashMap params = new HashMap();
//        params.put("param",param);
//        params.put("sign",sign);
//        params.put("customer",customer);
//        String resp;
//        map.put("logiNo", logiNo);
//        map.put("logiName", logiName);
//        try {
//            resp = new HttpRequest().postData("http://poll.kuaidi100.com/poll/query.do", params, "utf-8").toString();
////            if(returnMap!=null && !StringUtil.isEmpty(returnMap.get("resp"))){
//                JSONObject goodsJson= JSONObject.fromObject(resp);
//
//                if(goodsJson.getInt("status")==200){
//                    map.put("logiInfo", goodsJson.getJSONArray("data"));
////                    returnMap.put("logiInfo", goodsJson.getJSONArray("data"));
////                    return this.outMessage(0, "获取成功", returnMap);
//                }
////            }
////            map.put("resp", resp);
//            System.out.println(resp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return orderMap;
    }

    @Override
    @Transactional
    public Map<String, Object> participateSpell(Long spellId, Long productId, Long memberAddressId, String remark, String token){
        Map<String, Object> map = new HashMap<String, Object>();
        Member member = this.getMember(token);
        MemberAddress memberAddress =this.memberAddressDao.selectByAddrIdAndMemberId(memberAddressId,member.getMemberId());
        Spell spell = spellDao.selectByPrimaryKey(spellId);
        Long nowDate = System.currentTimeMillis();
        if(spell == null){
            throw new AppErrorException("该拼团不存在");
        }
        if(spell.getStatus() == 0 || nowDate < spell.getStartDate().getTime()){
            throw new AppErrorException("拼团未开始");
        }
        if(spell.getStatus() != 1 || nowDate > spell.getEndDate().getTime()){
            throw new AppErrorException("拼团已结束");
        }
        if(spell.getCompleteNum() <= spell.getParticipateNum()){
            throw new AppErrorException("拼团人数已满");
        }
        SpellActivity activity = spellActivityDao.findByActivityId(spell.getActivityId());
        if(activity == null){
            throw new AppErrorException("拼团活动不存在");
        }
        Integer count = spellDao.countMySpellByActivityId(member.getMemberId(), spell.getActivityId());
        if(activity.getLimited() <= count){
            throw new AppErrorException("不能重复参加拼团");
        }

//        Map<String, Object> goodsMap = productDao.selectByProductId(productId, Integer.valueOf(DictionaryUtil.getDataValueByName("goods_market_enable", "sales_yes")));
        Product product = productDao.selectByPrimaryKey(productId);

        BigDecimal weight = product.getWeight();
        BigDecimal shipAmount = calculateFreight(activity.getDlyTypeId(), weight, memberAddress.getProvinceId());
        SpellOrder order = new SpellOrder();
        order.setCreateDate(new Timestamp(nowDate));
        order.setLastModify(new Timestamp(nowDate));
        synchronized (nowDate) {
            order.setOrderSn(String.valueOf(nowDate));
        }
        order.setSpellId(spell.getSpellId());
        order.setSpellType(spell.getSpellType());
        order.setMemberId(member.getMemberId());
        order.setOrderStatus(0);
        order.setPayStatus(0);
        order.setShipStatus(0);
//        order.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("spell_order_status", "order_not_pay")));
//        order.setPayStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("pay_status", "pay_no")));
//        order.setShipStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("ship_status", "ship_allocation_no")));
        order.setWeight(weight);
        order.setShipAmount(shipAmount);
        order.setGoodsAmount(activity.getSpellPrice());
        order.setOrderAmount(activity.getSpellPrice().add(shipAmount));
        order.setPayAmount(new BigDecimal(0));
        order.setIsFree(false);
        order.setShipName(memberAddress.getName());
        order.setShipAddr(memberAddress.getAddressDetail());
        order.setShipZip(memberAddress.getZip());
        order.setShipMobile(memberAddress.getMobile());
        order.setShipTel(memberAddress.getTel());
        order.setShipProvinceId(memberAddress.getProvinceId());
        order.setShipCityId(memberAddress.getCityId());
        order.setShipDistrictId(memberAddress.getDistrictId());
        order.setShipProvinceName(memberAddress.getProvinceName());
        order.setShipCityName(memberAddress.getCityName());
        order.setShipDistrictName(memberAddress.getDistrictName());
        order.setRemark(remark);
        spellOrderDao.insertSelective(order);

        SpellOrderItems orderItems = new SpellOrderItems();
        orderItems.setCreateDate(new Timestamp(nowDate));
        orderItems.setLastModify(new Timestamp(nowDate));
        orderItems.setOrderId(order.getOrderId());
        orderItems.setOrderSn(order.getOrderSn());
        orderItems.setGoodsId(activity.getGoodsId());
        orderItems.setGoodsName(activity.getGoodsName());
        orderItems.setGoodsImage(activity.getImage2());
        orderItems.setProductId(productId);
        orderItems.setProductSn(product.getSn());
        orderItems.setProductSpecs(product.getSpecs());
        orderItems.setGoodsPrice(activity.getSpellPrice());
        orderItems.setComId(activity.getComId());
        orderItems.setShopStoreId(activity.getShopStoreId());
        orderItems.setWeight(weight);
        spellOrderItemsDao.insert(orderItems);
        map.put("orderId", order.getOrderId());
        return map;
    }

    /**
     * 获取商品规格
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> getGoodsSpec(Long goodsId) {
        if(StringUtil.isEmpty(goodsId))
            throw new AppErrorException("商品ID不能为空...");
        List<Map<String, Object>> mapList = goodsSpecDao.querySepcValueByGoodsId(goodsId);
        if(CollectionUtils.isEmpty(mapList)) {
            return null;
        }
        String specId = null;
        List<Map<String, Object>> resultList1 = null;
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map map2 = new HashedMap();
        Map map1 = null;
        for (Map map : mapList) {
            String newSpecId = map.get("specId").toString();
            if (MapUtils.isEmpty(map2) || !newSpecId.equals(map2.get("specId").toString())) {
                if (CollectionUtils.isNotEmpty(resultList1) && MapUtils.isNotEmpty(map2)) {
                    map2.put("detail", resultList1);
                    resultList.add(map2);
                    map2 = new HashedMap();
                }
                map2.put("specId", map.get("specId"));
                map2.put("specName", map.get("specName"));
                resultList1 = new ArrayList<>();
            }
            map1 = new HashedMap();
            map1.put("specValueId", map.get("specValueId"));
            map1.put("specValue", map.get("specValue"));
            resultList1.add(map1);
        }

        map2.put("detail", resultList1);
        resultList.add(map2);
        return resultList;
    }

    public List<Map<String, Object>> getParticipateDetails(String participateDetails){
        String[] par = participateDetails.split(";");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(String member:par){
            String[] members = member.split(",");
            Map<String, Object> map = new HashMap<String, Object>();
            for (String s:members){
                String[] ss = s.split("=");
                map.put(ss[0],ss[1]);
            }
            list.add(map);
        }
        return list;
    }
}