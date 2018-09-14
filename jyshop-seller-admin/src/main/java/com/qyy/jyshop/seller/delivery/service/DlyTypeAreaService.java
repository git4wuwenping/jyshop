package com.qyy.jyshop.seller.delivery.service;

import com.qyy.jyshop.model.DlyTypeArea;

import java.util.List;

public interface DlyTypeAreaService {
    /**
     * 获取配送方式的特殊配置列表
     * @param typeId
     * @return
     */
    List<DlyTypeArea> listDlyTypeArea(Long typeId);
}
