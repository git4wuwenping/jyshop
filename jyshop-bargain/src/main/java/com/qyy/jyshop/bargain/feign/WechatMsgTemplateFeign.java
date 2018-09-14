package com.qyy.jyshop.bargain.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jyshop-basics"
)
public interface WechatMsgTemplateFeign {
    
    @RequestMapping("/anon/sendWechatMsgToUser")
    public Map<String, Object> sendWechatMsgToUser(
            @RequestParam("memberIds")String memberIds,
            @RequestParam("templatId")String templatId, 
            @RequestParam("callUrl")String callUrl,
            @RequestParam("bgColor")String bgColor, 
            @RequestParam("jsonObjectStr")String jsonObjectStr, 
            @RequestParam("token")String token) throws Exception;
}
