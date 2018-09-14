package com.qyy.jyshop.admin.profit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.profit.service.ProfitParamService;
import com.qyy.jyshop.model.ProfitParam;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/profit")
public class ProfitParamController extends BaseController{

	@Autowired
	private ProfitParamService profitParamService;

	
	/**
	 * 获取分润参数用于回显
	 */
	@ControllerLog("分润参数")
	@RequestMapping("queryProfitParam")
	@Authority(opCode = "060103", opName = "分润参数")
	public String queryProfitParam(Map<String, Object> map) {
	    ProfitParam profitParam = profitParamService.queryProfitParam();
		map.put("profitParam", profitParam);
		return "profit/profit_param_main";
	}
	
	/**
	 * 更新分润参数
	 */
	@ControllerLog("更新分润参数")
    @ResponseBody
    @RequestMapping("editProfitParam")
    @Authority(opCode = "06010301", opName = "保存分润设置")
    public AjaxResult editProfitParam(@RequestParam Map<String, Object> map) {
        try {
            return AppUtil.returnObj(profitParamService.doEditProfitParam(map));
        } catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
	
}
