package com.qyy.jyshop.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.qyy.jyshop.model.Company;
import com.qyy.jyshop.supple.MyMapper;

public interface CompanyDao extends MyMapper<Company>{
    
	/**
     * 根据供应商ID获取供应商名称
     * @author hwc
     * @created 2017年12月12日 下午4:44:58
     * @param comId
     * @return
     */
    public String selectComName(@Param("comId")Integer comId);
    
    
    /**
     * 根据供应商ID获取商铺名称
     * @author hwc
     * @created 2018年2月9日 下午2:21:14
     * @param comId
     * @return
     */
    public String selectStoreName(@Param("comId")Integer comId);
    
    /**
     * 根据供应商Id获取供应商编码
     * @author hwc
     * @created 2018年1月23日 下午5:25:06
     * @param comId
     * @return
     */
    public String selectOfComCode(@Param("comId")Integer comId);
    
    Company queryComByParentId(@Param("parentId") Integer parentId,@Param("comId")Integer comId);
    
    public List<Map<String, Object>> queryComById(@Param("comId")int comId);

    public int selectComByName(@Param("comName")String comName);
    
    List<Map<String, Object>> selectComsByParams(Map<String,Object> params);
    
    List<Map<String, Object>> selectDataTableCom(Map<String,Object> params);
	
	void isDisabledExamine(@Param("comId")int comId, @Param("useable")Integer useable);

	public int insertCompany(Company company);
	

}
