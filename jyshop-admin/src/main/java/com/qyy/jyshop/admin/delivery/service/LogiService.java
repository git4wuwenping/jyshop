package com.qyy.jyshop.admin.delivery.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Delivery;
import com.qyy.jyshop.model.Logi;
import com.qyy.jyshop.pojo.PageAjax;

public interface LogiService {

    /**
     * 查询物流公司详情
     * @author hwc
     * @created 2017年12月21日 上午10:57:28
     * @param logiId
     * @return
     */
    public Logi queryByLogiId(Integer logiId);
    
    
    /**
     * 查询物流配送信息
     * @author hwc
     * @created 2018年1月10日 下午3:15:55
     * @return
     */
    public Map<String,Object> queryLogiDistributionInfo(String logiName,String logiCode,String logiNo);
    
    /**
     * 获取所有的物流公司
     * @author hwc
     * @created 2017年12月22日 上午9:54:06
     * @return
     */
    public List<Logi> queryLogi();
    
    /**
     * 获取物流列表(分页)
     * @author hwc
     * @created 2017年12月21日 上午10:28:45
     * @param page
     * @param Logi
     * @return
     */
    public PageAjax<Logi> pageLogi(PageAjax<Logi> page, Logi Logi);
    
    /**
     * 添加物流公司
     * @author hwc
     * @created 2017年12月21日 上午10:57:49
     * @param logi
     * @return
     */
    public String doAddLogi(Logi logi);
    
    /**
     * 修改物流公司
     * @author hwc
     * @created 2017年12月21日 上午10:58:01
     * @param logi
     * @return
     */
    public String doEditLogi(Logi logi);
    
    /**
     * 删除物流信息
     * @author hwc
     * @created 2017年12月21日 上午10:58:40
     * @param logiId
     * @return
     */
    public String doDelLogi(Integer logiId);
}
