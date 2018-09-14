package com.qyy.jyshop.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DlyTypeService {

    /**
     * 根据商家运费模板计算邮费
     * @author hwc
     * @created 2018年2月5日 下午2:46:11
     * @param comId
     * @param weight
     * @param provinceId
     * @param token
     * @return
     */
    public BigDecimal computeFreight(List<Map<String,Object>> goodsList,String provinceId,String token);
}
