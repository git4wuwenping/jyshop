package com.qyy.jyshop.admin.delivery.service.impl;

import com.qyy.jyshop.admin.common.annotation.ServiceLog;
import com.qyy.jyshop.admin.delivery.service.DlyTypeAreaService;
import com.qyy.jyshop.model.DlyTypeArea;
import com.qyy.jyshop.supple.AbstratService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DlyTypeAreaServiceImpl extends AbstratService<DlyTypeArea> implements DlyTypeAreaService {

    @ServiceLog("查询配送方式的配置列表")
    @Override
    public List<DlyTypeArea> listDlyTypeArea(Long typeId) {
        DlyTypeArea dlyTypeArea = new DlyTypeArea();
        dlyTypeArea.setTypeId(typeId);
        return this.queryList(dlyTypeArea);
    }
}