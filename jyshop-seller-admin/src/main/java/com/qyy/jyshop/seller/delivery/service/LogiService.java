package com.qyy.jyshop.seller.delivery.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Logi;

public interface LogiService {

    /**
     * 查询物流配送信息
     * @author hwc
     * @created 2018年1月10日 下午3:15:55
     * @return
     */
    public Map<String,Object> queryLogiDistributionInfo(Long orderId,Integer orderType);
   
    /**
     * 获取所有的物流公司
     * @author hwc
     * @created 2017年12月22日 上午9:54:06
     * @return
     */
    public List<Logi> queryLogi();
}
