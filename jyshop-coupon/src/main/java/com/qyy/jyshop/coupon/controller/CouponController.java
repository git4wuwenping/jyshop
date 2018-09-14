package com.qyy.jyshop.coupon.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.coupon.service.CouponService;
import com.qyy.jyshop.pojo.PageAjax;

@RestController
public class CouponController extends AppBaseController {
    
    @Autowired
    private CouponService couponService;
    
    /**
     * 优惠券-接口-优惠券中心~优惠券列表 
     * @author jiangbin
     * @created 2018年4月20日 下午7:08:55
     * @param token
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "couponList", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String,Object> getCouponList(String token,Integer pageNo,Integer pageSize) throws Exception {
        PageAjax<Map<String,Object>> couponList =this.couponService.getCouponList(pageNo, pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(couponList));
    }
    
    /**
     * 优惠券-接口-优惠券中心~优惠券详情 
     * @author jiangbin
     * @created 2018年4月20日 下午7:09:23
     * @param token
     * @param cpnId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "couponDetail", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String,Object> couponDetail(String token,Long cpnId,Integer pageNo, Integer pageSize) throws Exception {
        Map<String, Object> couponList =this.couponService.couponDetail(cpnId,pageNo,pageSize);
        return this.outMessage(0, "获取成功", couponList);
    }
    
    

}
