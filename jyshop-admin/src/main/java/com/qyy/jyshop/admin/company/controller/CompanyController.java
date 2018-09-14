package com.qyy.jyshop.admin.company.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.company.service.CompanyService;
import com.qyy.jyshop.model.Company;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.DataTablePage;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.DictionaryUtil;
@Controller
@RequestMapping("/admin/company")
public class CompanyController extends BaseController{
	
	@Autowired
	private CompanyService companyService;

	/**
	 * 供应商管理
	 * @return
	 */
	@Authority(opCode = "050102", opName = "供应商管理")
	@RequestMapping("companyMain")
	public String comMain(Map<String, Object> map){
		return "company/company_main";
	}
	
	/**
	 * 分页查询供应商列表
	 */
	@ControllerLog("查询供应商")
	@RequestMapping("pageCompanyAjax")
	@ResponseBody
	@Authority(opCode = "05010201", opName = "查询供应商")
	public PageAjax<Map<String, Object>> pageCompanyAjax(PageAjax<Map<String,Object>> page) {
		
		return companyService.pageCompanyAjax(page);
	}
	
	/**
	 * 供应商编辑页面
	 */
	@ControllerLog("供应商编辑页面")
	@RequestMapping("preEditCom/{comId}")
	@Authority(opCode = "05010202", opName = "供应商编辑页面")
	public String preEditCom(@PathVariable("comId") int comId,Map<String, Object> map) {
		Map<String, Object> comMap = companyService.queryComById(comId);
		List<Company> companys = companyService.queryAllCom();		//查询所有供应商
		map.put("companys", companys);
		if(comMap.get("parentId") != null){
			/*int parentId = (int) comMap.get("parentId");
			Company ParentCompany = companyService.queryComByParentId(parentId,comId);
			String referee = ParentCompany.getComName();
			Integer refereeId = ParentCompany.getComId();*/
			map.put("refereeId", comMap.get("parentId"));	//推荐人
		}else{
			map.put("refereeId", "");	//推荐人
		}
		map.put("com", comMap);
		return "company/company_edit";
	}
	
	/**
	 * 更新供应商信息
	 * @throws Exception 
	 */
	
	@ControllerLog("更新供应商")
	@RequestMapping("editCompany")
	@ResponseBody
	@Authority(opCode = "05010203", opName = "更新供应商")
	public AjaxResult editCompany(@RequestParam Map<String, Object> map) throws Exception {
		return  AppUtil.returnObj(companyService.editCompany(map));
	}
	
	/**
	 * 添加供应商页面
	 */
	@ControllerLog("添加供应商页面")
	@RequestMapping("preAddCom")
	@Authority(opCode = "05010204", opName = "添加供应商页面")
	public String preAddUser(Map<String, Object> map) {
		List<Company> companys = companyService.queryAllCom();
		map.put("companys", companys);
		return "company/company_add";
	}
	
	@ControllerLog("添加供应商")
	@RequestMapping("addCompany")
	@ResponseBody
	@Authority(opCode = "05010205", opName = "添加供应商")
	public AjaxResult addCom(@RequestParam Map<String, Object> map) {
		try{
			return AppUtil.returnObj(companyService.doAddCom(map));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("停用")
	@RequestMapping("disabledNo/{comId}")
	@ResponseBody
	@Authority(opCode = "05010206", opName = "停用")
	public AjaxResult disabledNo(@PathVariable("comId") int comId) {
		try{
			return AppUtil.returnObj(companyService.isDisabledExamine(comId,Integer.valueOf(DictionaryUtil.getDataValueByName("is_usable", "yes"))));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	@ControllerLog("启用")
	@RequestMapping("disabledYes/{comId}")
	@ResponseBody
	@Authority(opCode = "05010207", opName = "启用")
	public AjaxResult disabledYes(@PathVariable("comId") int comId) {
		try{
			return AppUtil.returnObj(companyService.isDisabledExamine(comId,Integer.valueOf(DictionaryUtil.getDataValueByName("is_usable", "no"))));
		}catch (Exception e) {
			return AppUtil.returnObj(2,e.getMessage());
		}
	}
	@ControllerLog("校验用户名")
	@RequestMapping(value="checkComName",method=RequestMethod.POST)
	@ResponseBody
	@Authority(opCode = "05010208", opName = "校验供应商名称")
	public AjaxResult checkComName(@RequestParam("comName") String comName){
		try {
			return AppUtil.returnObj(companyService.selectComByName(comName));
		} catch (Exception e) {
			return  AppUtil.returnObj(2,e.getMessage());
		}
	}
	
	@ControllerLog("获取dataTable供应商数据")
    @RequestMapping("pageDataTableCom")
    @ResponseBody
    @Authority(opCode = "05010209", opName = "获取dataTable供应商数据")
    public DataTablePage<Map<String,Object>> pageDataTableCom(@RequestParam Map<String, Object> map) {
        DataTablePage<Map<String,Object>> page = companyService.pageDataTableCom(map);
        return page;
    }
}
