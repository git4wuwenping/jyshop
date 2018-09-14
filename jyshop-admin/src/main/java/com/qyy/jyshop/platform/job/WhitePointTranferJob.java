/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.platform.job;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qyy.jyshop.admin.point.service.WhitePointTranferService;
import com.qyy.jyshop.dao.OldMemberPointDao;
import com.qyy.jyshop.model.OldMemberPoint;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月19日 下午4:25:26
 */
@Component
public class WhitePointTranferJob {
    private final Log logger = LogFactory.getLog(ProfitJob.class);

    @Autowired
    private WhitePointTranferService whitePointTranferService;

    @Autowired
    private OldMemberPointDao oldMemberPointDao;

    //每日6点执行
    @Scheduled(cron = "0 0 6 * * ?")
    public void autoTransfer() {
        logger.info("处理白积分转换开始......");
        List<OldMemberPoint> list = this.oldMemberPointDao.selectAll();
        if (list != null && list.size() > 0) {
            for (OldMemberPoint oldMemberPoint : list) {
                if (oldMemberPoint.getResidue().compareTo(BigDecimal.ZERO) > 0 && oldMemberPoint.getPoint().compareTo(new BigDecimal(20)) >= 0)
                    this.whitePointTranferService.transferPoint(oldMemberPoint);
            }
        }
        logger.info("处理白积分转换完成......");

    }

}
