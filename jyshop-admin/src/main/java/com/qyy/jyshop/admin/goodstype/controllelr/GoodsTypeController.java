package com.qyy.jyshop.admin.goodstype.controllelr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.goodstype.service.GoodsTypeService;
import com.qyy.jyshop.admin.goodstype.service.SpecService;
import com.qyy.jyshop.model.GoodsType;
import com.qyy.jyshop.model.Spec;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;
import com.qyy.jyshop.util.LongUtil;
import com.qyy.jyshop.util.StringUtil;


@Controller
@RequestMapping("/admin/goodstype")
public class GoodsTypeController  extends BaseController{

    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private SpecService specService;
    
    @RequestMapping("goodsTypeMain")
    @Authority(opCode = "040201", opName = "类型列表")
    public String goodsTypeMain(Map<String, Object> map) {
        return "goodstype/goods_type_main";
    }
    
    
    @ControllerLog("查询类型列表")
    @RequestMapping("pageGoodsTypeAjax")
    @ResponseBody
    @Authority(opCode = "04020101", opName = "查询类型列表")
    public PageAjax<GoodsType> pageGoodsTypeAjax(PageAjax<GoodsType> page,GoodsType goodsType) {
        return this.goodsTypeService.pageGoodsType(page, goodsType);
    }
    
    @ControllerLog("添加类型页面")
    @Authority(opCode = "04020102", opName = "添加类型页面")
    @RequestMapping("preAddGoodsType")
    public String preAddGoodsType() {
        return "goodstype/add_goods_type";
    }

    @ControllerLog("添加类型")
    @RequestMapping("addGoodsType")
    @ResponseBody
    @Authority(opCode = "04020103", opName = "添加类型")
    public AjaxResult addGoodsType(GoodsType goodsType) {
        try{
            return AppUtil.returnObj(this.goodsTypeService.doAddGoodsType(goodsType));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("修改类型页面")
    @Authority(opCode = "04020104", opName = "修改类型页面")
    @RequestMapping("preEditGoodsType/{typeId}")
    public String preEditGoodsType(@PathVariable("typeId") Long typeId, Map<String, Object> map) {
        map.put("goodsType", this.goodsTypeService.queryByTypeId(typeId));
        return "goodstype/edit_goods_type";
    }
    
    @ControllerLog("修改类型")
    @RequestMapping("editGoodsType")
    @ResponseBody
    @Authority(opCode = "04020105", opName = "修改类型")
    public AjaxResult editGoodsType(GoodsType goodsType) {
        try{
            return AppUtil.returnObj(this.goodsTypeService.doEditGoodsType(goodsType));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("删除类型")
    @RequestMapping("delGoodsType/{typeId}")
    @ResponseBody
    @Authority(opCode = "04020106", opName = "删除类型")
    public AjaxResult delGoodsType(@PathVariable("typeId")Long typeId) {
        try{
            return AppUtil.returnObj(this.goodsTypeService.doDelByTypeId(typeId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("设置规格页面")
    @Authority(opCode = "04020107", opName = "设置规格页面")
    @RequestMapping("prePutSpec/{typeId}")
    public String prePutSpec(@PathVariable("typeId") Long typeId, Map<String, Object> map) {
        map.put("goodsType", this.goodsTypeService.queryByTypeId(typeId));
        
        GoodsType goodsType = this.goodsTypeService.queryByTypeId(typeId);
        map.put("goodsType", goodsType);
        
        List<Spec> specList = this.specService.queryByComId();
        
        List<Spec> hasSpecList = new ArrayList<Spec>();
        if(goodsType!=null && !StringUtil.isEmpty(goodsType.getSpecIds())){
            Long[] specIds=LongUtil.stringArrayToLongArray(goodsType.getSpecIds().split(","));
            hasSpecList=this.specService.queryBySpecIds(specIds);
        }

        List<Long> specIdList = new ArrayList<Long>();
        for(Spec spec: hasSpecList){
            specIdList.add(spec.getSpecId());
        }
        
        for (Spec spec : specList) {
            if (specIdList.contains(spec.getSpecId())) {
                spec.setStatus(1);
            }
        }
        
        map.put("specList", specList);

        return "goodstype/goods_type_spec";
    }
    
    @ControllerLog("设置规格")
    @RequestMapping("putSpec")
    @ResponseBody
    @Authority(opCode = "04020108", opName = "设置规格")
    public AjaxResult putSpec(Long typeId, String specIds) {
        try{
//            GoodsType goodsType = this.goodsTypeService.queryByTypeId(typeId);
            return AppUtil.returnObj(this.goodsTypeService.doPutSpc(typeId, specIds));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }
    
    @ControllerLog("获取类型规格")
    @RequestMapping("queryTypeSpec/{typeId}")
    @ResponseBody
    @Authority(opCode = "04020111", opName = "获取类型规格")
    public AjaxResult queryTypeSpec(@PathVariable("typeId") Long typeId) {
        try{
            GoodsType goodsType = this.goodsTypeService.queryByTypeId(typeId);
            List<Spec> hasSpecList = new ArrayList<Spec>();
            if(goodsType!=null && !StringUtil.isEmpty(goodsType.getSpecIds())){
                Long[] specIds=LongUtil.stringArrayToLongArray(goodsType.getSpecIds().split(","));
                hasSpecList=this.specService.queryBySpecIds(specIds);
            }
            return AppUtil.returnObj(null, hasSpecList); 
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    } 
}
