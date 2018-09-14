package com.qyy.jyshop.seller.goodstype.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.pojo.PageAjax;

public interface SpecService {

    /**
     * 获取规格信息
     * @author hwc
     * @created 2018年1月3日 上午9:52:09
     * @param specId
     * @return
     */
    public Spec queryBySpecId(Long specId);
    
    /**
     * 获取当前管理员管理的所有的规格
     * @author hwc
     * @created 2018年1月3日 下午4:06:20
     * @return
     */
    public List<Spec> queryByComId();
    
    /**
     * 根据规格Ids获取规格列表
     * @author hwc
     * @created 2018年1月3日 下午4:06:11
     * @param specIds
     * @return
     */
    public List<Spec> queryBySpecIds(Long[] specIds);
    
    /**
     * 获取规格列表(分页)
     * @author hwc
     * @created 2018年1月2日 上午11:25:57
     * @param page
     * @param spec
     * @return
     */
    public PageAjax<Spec> pageSpec(PageAjax<Spec> page,Spec spec);
    
    /**
     * 添加规格
     * @author hwc
     * @created 2018年1月2日 上午10:33:37
     * @param specMap
     */
    public String doAddSpec(Map<String,Object> specMap);
    
    /**
     * 修改规格
     * @author hwc
     * @created 2018年1月3日 上午10:43:58
     * @param specMap
     * @return
     */
    public String doEditSpec(Map<String,Object> specMap);
    
    
}
