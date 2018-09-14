package com.qyy.jyshop.alipay.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jyshop-goods"
)
public interface ProductStoreFeign {

    /**
     * 修改货品库存
     * @author hwc
     * @created 2018年4月3日 上午10:52:30
     * @param orderId
     */
    @RequestMapping(value = "editProductStore")
    public void editProductStore(@RequestParam("orderId")Long orderId);
}
