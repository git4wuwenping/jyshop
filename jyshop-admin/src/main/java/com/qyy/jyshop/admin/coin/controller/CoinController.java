package com.qyy.jyshop.admin.coin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.coin.service.CoinDtlService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.pojo.PageAjax;

@Controller
@RequestMapping("/admin/coin")
public class CoinController extends BaseController {
	
	@Autowired
	private CoinDtlService coinDtlService;


	@Authority(opCode = "110101", opName = "金币设置")
	@RequestMapping("coinParamMain")
	public String coinParamMain() {
		return "coin/param/coin_param_main";
	}
	@Authority(opCode = "110102", opName = "金币明细")
	@RequestMapping("coinDtlMain")
	public String coinDtlMain() {
		return "coin/dtl/coin_dtl_main";
	}

	@RequestMapping("pageCoinDtlAjax")
	@ResponseBody
	@Authority(opCode = "11010201", opName = "金币明细列表")
	public PageAjax<Map<String,Object>> pageCoinDtlAjax(PageAjax<Map<String,Object>> page) {
		return coinDtlService.pageCoinDtl(page);
	}


	

	@Authority(opCode = "11010202", opName = "金币明细详情")
	@RequestMapping("detailCoinDtl/{id}")
	public String detailCoinDtl(@PathVariable Long id, Map<String, Object> map) {
		map.put("info", coinDtlService.queryDtlByMemberId(id));
		return "coin/dtl/coin_dtl_detail";
	}

	

	
}
