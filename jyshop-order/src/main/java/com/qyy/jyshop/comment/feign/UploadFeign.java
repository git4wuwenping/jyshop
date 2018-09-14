package com.qyy.jyshop.comment.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jyshop-member")
public interface UploadFeign {

    /**
     * 获取App支付参数
     * @author hwc
     * @created 2018年1月8日 下午3:58:04
     * @param payAmount
     * @param orderSn
     * @param createIp
     * @param token
     * @return
     */
    @RequestMapping("anon/batchUploadBASE64Image")
    public String uploadBASE64Image(List<String> strList);
    
    
    
}
