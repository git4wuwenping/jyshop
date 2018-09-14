package com.qyy.jyshop.order.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.dao.DlyTypeAreaDao;
import com.qyy.jyshop.dao.DlyTypeDao;
import com.qyy.jyshop.dao.MemberAddressDao;
import com.qyy.jyshop.model.DlyType;
import com.qyy.jyshop.model.DlyTypeArea;
import com.qyy.jyshop.order.service.DlyTypeService;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;

@Service
public class DlyTypeServiceImpl extends AbstratService<DlyType> implements DlyTypeService{

    @Autowired
    private DlyTypeDao dlyTypeDao;
    @Autowired
    private DlyTypeAreaDao dlyTypeAreaDao;
    @Autowired
    private MemberAddressDao memberAddressDao;
    
    @Override
    public BigDecimal computeFreight(List<Map<String,Object>> goodsList,String cityId,String token) {
      
        //运费
        BigDecimal freight=new BigDecimal(0);
        
        if(goodsList!=null && goodsList.size()>0){
            
            Map<Long,BigDecimal> freightMap=new HashMap<Long, BigDecimal>();
            
            
            for (Map<String, Object> goodsMap : goodsList) {
                
                Long dlyTypeId=Long.valueOf(goodsMap.get("dlyTypeId").toString());
                
                if(!dlyTypeId.equals("0")){
                    
                    BigDecimal weight=StringUtil.isEmpty(goodsMap.get("weight"))?new BigDecimal(0):
                        new BigDecimal(goodsMap.get("weight").toString());
                    weight=weight.multiply(new BigDecimal(goodsMap.get("buyCount").toString()))
                            .setScale(0, BigDecimal.ROUND_DOWN);
                    if(freightMap.get(dlyTypeId)!=null){    
                        freightMap.put(dlyTypeId, freightMap.get(dlyTypeId).add(weight));
                    }else{
                        freightMap.put(dlyTypeId, weight);
                    }
                }
            }
            
            if(StringUtil.isEmpty(cityId))
                cityId=this.memberAddressDao.selectDefOfCityId(this.getMemberId(token));
            
            if(StringUtil.isEmpty(cityId))
                return freight;
            
            endComputeFreight:for (Map.Entry<Long,BigDecimal> freightEntry : freightMap.entrySet()) { 
                DlyType dlyType=this.dlyTypeDao.selectByTypeId(freightEntry.getKey());
                if(dlyType!=null){
                    //判断是否指定地区配置
                    if(dlyType.getIsSame().equals(0)){
                        
                        List<DlyTypeArea> dlyTypeAreaList=dlyType.getDlyTypeAreaList();
                        
                        if(dlyTypeAreaList!=null && dlyTypeAreaList.size()>0){
                            

                            endFreight:for (DlyTypeArea dlyTypeArea : dlyTypeAreaList) {
                                if(!StringUtil.isEmpty(dlyTypeArea.getAreaIdGroup())){
                                    if(Arrays.asList(dlyTypeArea.getAreaIdGroup().split(",")).contains(cityId)){
                                        //是否包邮
                                        if(dlyTypeArea.getFreeDly().equals(1)){
                                            break endComputeFreight;
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
                    BigDecimal weight=freightEntry.getValue();
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
            
            }
        
        }
        
        return freight.setScale(2, BigDecimal.ROUND_DOWN);
    }

}
