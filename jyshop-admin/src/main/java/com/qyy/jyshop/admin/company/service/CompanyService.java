package com.qyy.jyshop.admin.company.service;

import java.util.List;
import java.util.Map;

import com.qyy.jyshop.model.Company;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;

public interface CompanyService {


	PageAjax<Map<String, Object>> pageCompanyAjax(PageAjax<Map<String, Object>> page);
	
	/**
	 * 根据id查询供应商信息
	 * @param comId
	 * @return
	 */
	 Map<String, Object> queryComById(int comId);

	String selectComByName(String comName);

	String editCompany(Map<String, Object> map) throws Exception;

	String doAddCom(Map<String, Object> map) throws Exception;

	String isDisabledExamine(int comId, Integer integer);

	List<Company> queryAllCom();

	Company queryComByParentId(Integer parentId, Integer comId);

    DataTablePage<Map<String, Object>> pageDataTableCom(Map<String, Object> map);


}
