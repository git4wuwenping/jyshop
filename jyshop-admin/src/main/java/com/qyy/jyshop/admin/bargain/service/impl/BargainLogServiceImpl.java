package com.qyy.jyshop.admin.bargain.service.impl;

import com.qyy.jyshop.admin.bargain.service.BargainLogService;
import com.qyy.jyshop.admin.order.service.OrderLogService;
import com.qyy.jyshop.dao.BargainLogDao;
import com.qyy.jyshop.dao.OrderLogDao;
import com.qyy.jyshop.model.BargainLog;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

@Service
public class BargainLogServiceImpl extends AbstratService<BargainLog> implements BargainLogService {

    @Autowired
    private BargainLogDao logDao;
    
    @Override
    public List<BargainLog> queryByOrderId(Long orderId) {
        if(StringUtil.isEmpty(orderId))
            return null;
        Example example = new Example(BargainLog.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        return logDao.selectByExample(example);
    }

}
