package com.qyy.jyshop.admin.coin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.coin.service.CoinParamConfigService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.model.CoinParamConfig;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/coin")
public class CoinConfigController  extends BaseController{
    
	@Autowired
	private CoinParamConfigService coinParamConfigService;
      
    @Authority(opCode = "110101", opName = "金币设置")
    @RequestMapping("coinConfigMain")
    public String coinConfigMain(Map<String, Object> map) {  
        map.put("config", coinParamConfigService.querycoinParamConfig());
        return "coin/param/coin_config_main";
    }
    
    @ControllerLog("修改金币设置")
    @RequestMapping("editCoinConfig")
    @ResponseBody
    @Authority(opCode = "11010101", opName = "修改金币设置")
    public AjaxResult editCoinConfig(CoinParamConfig config) {
        try{
            return AppUtil.returnObj(coinParamConfigService.edit(config));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    
    
}
