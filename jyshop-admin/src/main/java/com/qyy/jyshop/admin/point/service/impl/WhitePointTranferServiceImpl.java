/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.point.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qyy.jyshop.admin.point.service.WhitePointTranferService;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.dao.MemberPointDao;
import com.qyy.jyshop.dao.OldMemberPointDao;
import com.qyy.jyshop.dao.PointLogDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberPoint;
import com.qyy.jyshop.model.OldMemberPoint;
import com.qyy.jyshop.model.PointLog;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.TimestampUtil;

/**
 * 描述
 * @author jiangbin
 * @created 2018年3月19日 下午4:35:42
 */
@Service
public class WhitePointTranferServiceImpl extends AbstratService<OldMemberPoint> implements WhitePointTranferService {
    
    @Autowired
    private OldMemberPointDao oldMemberPointDao;
    @Autowired
    private MemberPointDao memberPointDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private PointLogDao pointLogDao;
    /**
     * 描述
     * @author jiangbin
     * @created 2018年3月19日 下午4:35:43
     * @param oldMemberPoint
     * @see com.qyy.jyshop.admin.point.service.WhitePointTranferService#transferPoint(com.qyy.jyshop.model.OldMemberPoint)
     */
    @Override
    @Transactional
    public void transferPoint(OldMemberPoint oldMemberPoint) {
        if(StringUtils.isBlank(oldMemberPoint.getMemberId().toString())){
            return;
        }
        BigDecimal changePoint = oldMemberPoint.getPoint().divide(new BigDecimal(2000)).setScale(2, RoundingMode.HALF_DOWN);
        //1 修改oldMemberPoint 记录
        oldMemberPoint.setAlready(oldMemberPoint.getAlready().add(changePoint));
        oldMemberPoint.setResidue(oldMemberPoint.getResidue().subtract(changePoint));
        oldMemberPointDao.updateByPrimaryKeySelective(oldMemberPoint);
        //2 新增会员红积分明细记录
        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setOrderId((long) 0);
        memberPoint.setOrderSn("");
        memberPoint.setOrderType(0);
        memberPoint.setType(0);
        memberPoint.setPoint(changePoint);
        memberPoint.setPointType(2);
        memberPoint.setMemberId(oldMemberPoint.getMemberId());
        memberPoint.setGetType(3);
        memberPoint.setPointStatus(1);
        memberPoint.setCreateTime(TimestampUtil.getNowTime());
        memberPointDao.insert(memberPoint);
        //3 插入积分日志
        PointLog pointLog= new PointLog();
        pointLog.setMemberId(oldMemberPoint.getMemberId());
        pointLog.setOrderId((long) 0);
        pointLog.setOrderSn("WhitePointTranfer");
        pointLog.setCloudPoint(BigDecimal.ZERO);
        pointLog.setYellowPoint(BigDecimal.ZERO);
        pointLog.setRedPoint(changePoint);
        pointLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pointLogDao.insert(pointLog);
        //4 修改会员红积分
        memberDao.updateMemberPointOfAdd(oldMemberPoint.getMemberId(),BigDecimal.ZERO,BigDecimal.ZERO,changePoint);
        //4 修改redis中会员信息
    }

}
