package com.qyy.jyshop.admin.goodstype.controllelr;

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
import com.qyy.jyshop.admin.goodstype.service.SpecService;
import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/spec")
public class SpecController extends BaseController{

    @Autowired
    private SpecService specService;
    
    @RequestMapping("specMain")
    @Authority(opCode = "040202", opName = "规格列表")
    public String specMain(Map<String, Object> map) {
        return "goodstype/spec/spec_main";
    }
    
    
    @ControllerLog("查询规格列表")
    @RequestMapping("pageSpecAjax")
    @ResponseBody
    @Authority(opCode = "04020201", opName = "查询规格列表")
    public PageAjax<Spec> pageSpecAjax(PageAjax<Spec> page,Spec spec) {
        return this.specService.pageSpec(page, spec);
    }
    
    @ControllerLog("添加规格页面")
    @Authority(opCode = "04020202", opName = "添加规格页面")
    @RequestMapping("preAddSpec")
    public String preAddSpec() {
        return "goodstype/spec/add_spec";
    }

    @ControllerLog("添加规格")
    @RequestMapping("addSpec")
    @ResponseBody
    @Authority(opCode = "04020203", opName = "添加规格")
    public AjaxResult addSpec(@RequestParam Map<String,Object> specMap) {
        try{
            return AppUtil.returnObj(this.specService.doAddSpec(specMap));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("修改规格页面")
    @Authority(opCode = "04020204", opName = "修改规格页面")
    @RequestMapping("preEditSpec/{specId}")
    public String preEditSpec(@PathVariable("specId") Long specId, Map<String, Object> map) {
        map.put("spec", this.specService.queryBySpecId(specId));
        return "goodstype/spec/edit_spec";
    }
    
    @ControllerLog("修改规格")
    @RequestMapping("editSpec")
    @ResponseBody
    @Authority(opCode = "04020205", opName = "修改规格")
    public AjaxResult editSpec(@RequestParam Map<String,Object> specMap) {
        try{
            return AppUtil.returnObj(this.specService.doEditSpec(specMap));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("删除规格")
    @Authority(opCode = "04020206", opName = "删除规格")
    @ResponseBody
    @RequestMapping("delSpec/{specId}")
    public AjaxResult delSpec(@PathVariable("specId") Long specId) {
        try{
            return AppUtil.returnObj(this.specService.doDelSpec(specId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("根据商品规格值查询商品")
	@RequestMapping(value="findGoodBySpecValueId",method=RequestMethod.POST)
	@ResponseBody
	@Authority(opCode = "04020207", opName = "根据商品规格值查询商品")
    public String findGoodBySpecValueId(@RequestParam Long specValueId ){
    	return specService.selectGoodBySpecValueId(specValueId);
    }
    
}
