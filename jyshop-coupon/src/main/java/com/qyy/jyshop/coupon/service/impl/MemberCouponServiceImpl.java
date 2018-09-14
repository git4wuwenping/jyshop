package com.qyy.jyshop.coupon.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.coupon.service.MemberCouponService;
import com.qyy.jyshop.dao.CouponDao;
import com.qyy.jyshop.dao.MemberCouponDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Coupon;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberCoupon;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class MemberCouponServiceImpl extends AbstratService<MemberCoupon> implements MemberCouponService {

    @Autowired
    private MemberCouponDao memberCouponDao;
    @Autowired
    private CouponDao couponDao;

    @Override
    @Transactional
    public void getCoupon(String token, Long cpnId) {
        Member member = this.getMember(token);
        if (member == null) {
            throw new AppErrorException("请重新登录");
        }
        Coupon coupon = couponDao.selectByPrimaryKey(cpnId);
        if (coupon == null || coupon.getGrantNum() <= 0) {
            throw new AppErrorException("优惠券不存在");
        }
        synchronized (cpnId) {
            // 会员限领
            Integer limitGet = coupon.getLimitGet();
            Example example = new Example(MemberCoupon.class);
            Criteria createCriteria = example.createCriteria();
            createCriteria.andEqualTo("memberId", member.getMemberId());
            createCriteria.andEqualTo("cpnId", cpnId);
            List<MemberCoupon> memberCouponList = memberCouponDao.selectByExample(example);
            if (memberCouponList != null && memberCouponList.size() >= limitGet) {
                throw new AppErrorException("已经达到领取该惠券上限");
            }
            // 是否达到上限
            example.clear();
            Criteria createCriteria1 = example.createCriteria();
            createCriteria1.andEqualTo("cpnId", cpnId);
            List<MemberCoupon> memberCouponList1 = memberCouponDao.selectByExample(example);
            if (memberCouponList1 != null && memberCouponList1.size() >= coupon.getGrantNum()) {
                throw new AppErrorException("优惠券已经领取光啦~");
            }
            // 新增会员优惠券记录
            MemberCoupon memberCoupon = new MemberCoupon();
            BeanUtils.copyProperties(coupon, memberCoupon);
            memberCoupon.setSn(UUID.randomUUID().toString());
            memberCoupon.setIsGet(1);
            memberCoupon.setIsUse(0);
            memberCoupon.setMemberId(member.getMemberId());
            memberCoupon.setUseTime(null);
            Date curDate = new Date();
            memberCoupon.setCreateTime(new Timestamp(curDate.getTime()));
            memberCoupon.setGetTime(new Timestamp(curDate.getTime()));
            if(coupon.getUseType() == 1){ //使用类型： 0按日期  1按领取日期有效天数    
                Timestamp useEndTime = TimestampUtil.getAfterNDays(curDate, coupon.getEffectiveDate());
                memberCoupon.setUseBeginTime(new Timestamp(curDate.getTime()));
                memberCoupon.setUseEndTime(useEndTime);
            }
            memberCouponDao.insert(memberCoupon);
        }
    }

    @Override
    public Map<String, Object> memberCouponCenter(String token) {
        Map<String,Object> retMap = new HashMap<String, Object>();
        
        Member member = this.getMember(token);
        if (member == null) {
            throw new AppErrorException("请重新登录");
        }
        
        //最近领取的优惠券 1 天领取的优惠券
        List<Map<String,Object>> couponListRecently = memberCouponDao.getUsableCouponRecently(member.getMemberId());
        retMap.put("couponListRecently", couponListRecently);
        /* //优惠券类型
        Map<String,Object> catMap = new HashMap<String, Object>();
        catMap.put("0", "分类优惠券");
        catMap.put("1", "商品优惠券");
        catMap.put("0", "网店优惠券");
        
        retMap.put("cat", catMap);*/
        return retMap;
    }

    @Override
    public PageAjax<Map<String, Object>> memberCouponList(String token, Integer getFlag, Integer pageNo, Integer pageSize) {
        Member member = this.getMember(token);
        if (member == null) {
            throw new AppErrorException("请重新登录");
        }
        ParamData params = this.getParamData(pageNo, pageSize);
        params.put("memberId", member.getMemberId());
        params.put("getFlag", getFlag); //0未领取 1-已领取
        return this.pageQuery("MemberCouponDao.getMemberCouponList", params);
    }

    @Override
    @Transactional
    public void memberGetUnreceivedCoupon(String token, String sn) {
        Member member = this.getMember(token);
        if (member == null) {
            throw new AppErrorException("请重新登录");
        }
        synchronized (sn) {
            MemberCoupon record = new MemberCoupon();
            record.setSn(sn);
            record.setMemberId(member.getMemberId());
            MemberCoupon memberCoupon = memberCouponDao.selectOne(record);
            memberCoupon.setIsGet(1);
            Long nowTime = new Date().getTime();
            memberCoupon.setCreateTime(new Timestamp(nowTime));
            if(memberCoupon.getUseType() == 1){ //使用类型： 0按日期  1按领取日期有效天数    
                Timestamp useEndTime = new Timestamp(nowTime + 86400 * memberCoupon.getEffectiveDate());
                memberCoupon.setUseBeginTime(new Timestamp(nowTime));
                memberCoupon.setUseEndTime(useEndTime);
            }
            memberCoupon.setGetTime(new Timestamp(nowTime));
            memberCouponDao.updateByPrimaryKeySelective(memberCoupon);
        }
        
        
    }

    

}
