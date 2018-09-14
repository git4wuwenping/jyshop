package com.qyy.jyshop.coupon.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.coupon.service.CouponService;
import com.qyy.jyshop.coupon.service.MemberCouponService;
import com.qyy.jyshop.pojo.PageAjax;

@RestController
public class MemeberCouponController extends AppBaseController {
    
    @Autowired
    private CouponService couponService;
    
    @Autowired
    private MemberCouponService memberCouponService;
    
    /**
     *  优惠券-接口-优惠券中心~会员优惠券领取 
     * @author jiangbin
     * @created 2018年4月20日 下午7:09:54
     * @param token
     * @param cpnId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "memberGetCoupon", method = { RequestMethod.POST, RequestMethod.GET})
    public Map<String,Object> memberGetCoupon(String token,Long cpnId) throws Exception {
        try{
            memberCouponService.getCoupon(token,cpnId);
        }catch(Exception ex){
            return this.outMessage(1, ex.getMessage(), null);
        }
        return this.outMessage(0, "获取成功", null);
    }

    /**
     *  优惠券-接口-会员卡券包~会员优惠券中心首页
     * @author jiangbin
     * @created 2018年4月20日 下午7:10:06
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "memberCouponCenter", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String,Object> memberCouponCenter(String token) throws Exception {
        Map<String, Object> couponList =this.memberCouponService.memberCouponCenter(token);
        return this.outMessage(0, "获取成功", couponList);
    }
    
    /**
     *  优惠券-接口-会员卡券包~会员优惠券列表 已领取 未领取
     * @author jiangbin
     * @created 2018年4月20日 下午7:41:19
     * @param token
     * @param getFlag
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "memberCouponList", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String,Object> memberCouponList(String token,Integer getFlag, Integer pageNo,Integer pageSize) throws Exception {
        PageAjax<Map<String,Object>> couponList = this.memberCouponService.memberCouponList(token,getFlag,pageNo, pageSize);
        return this.outMessage(0, "获取成功", this.getPageMap(couponList));
    }
    
    /**
     *  优惠券-接口-会员卡券包~会员领取 已发放未领取的优惠券
     * @author jiangbin
     * @created 2018年4月20日 下午7:42:21
     * @param token
     * @param sn
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "memberGetPrivateCoupon", method = { RequestMethod.POST, RequestMethod.GET})
    public Map<String,Object> memberGetPrivateCoupon(String token,String sn) throws Exception {
        try{
            memberCouponService.memberGetUnreceivedCoupon(token,sn);
        }catch(Exception ex){
            return this.outMessage(1, ex.getMessage(), null);
        }
        return this.outMessage(0, "获取成功", null);
    }
}
