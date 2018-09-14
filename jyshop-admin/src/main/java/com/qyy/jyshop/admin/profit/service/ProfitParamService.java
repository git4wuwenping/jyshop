package com.qyy.jyshop.admin.profit.service;

import java.util.Map;

import com.qyy.jyshop.model.ProfitParam;

public interface ProfitParamService {

    /**
     * 查询分润设置
     * @author hwc
     * @created 2018年1月12日 下午3:57:02
     * @return
     */
    ProfitParam queryProfitParam();
    
    /**
     * 修改分润设置
     * @author hwc
     * @created 2018年1月12日 下午3:57:34
     * @param map
     * @return
     */
	String doEditProfitParam(Map<String, Object> map);
}
