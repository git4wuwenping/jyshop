package com.qyy.jyshop.dao;

import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.supple.MyMapper;

public interface LogiDao extends MyMapper<Logi>{

    /**
     * 根据物流编号获取物流公司信息
     * @author hwc
     * @created 2017年12月21日 上午11:22:00
     * @param logiCode
     * @return
     */
    Logi selectByLogiCode(String logiCode);
    
    /**
     * 根据物流名称获取物流公司信息
     * @author hwc
     * @created 2017年12月21日 上午11:21:21
     * @param logiName
     * @return
     */
    Logi selectByLogiName(String logiName);
}
