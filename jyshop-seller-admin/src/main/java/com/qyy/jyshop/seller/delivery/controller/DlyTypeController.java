package com.qyy.jyshop.seller.delivery.controller;

import com.alibaba.fastjson.JSONObject;
import com.qyy.jyshop.model.Address;
import com.qyy.jyshop.model.DlyType;
import com.qyy.jyshop.model.DlyTypeArea;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.seller.common.annotation.Authority;
import com.qyy.jyshop.seller.common.annotation.ControllerLog;
import com.qyy.jyshop.seller.common.controller.BaseController;
import com.qyy.jyshop.seller.delivery.service.AddressService;
import com.qyy.jyshop.seller.delivery.service.DlyTypeAreaService;
import com.qyy.jyshop.seller.delivery.service.DlyTypeService;
import com.qyy.jyshop.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/dlyType")
public class DlyTypeController extends BaseController {

    @Autowired
    private DlyTypeService dlyTypeService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private DlyTypeAreaService dlyTypeAreaService;

    @Authority(opCode = "070102", opName = "配送方式")
    @RequestMapping("dlyTypeMain")
    public String dlyTypeMain(){
        return "dlytype/dlytype_main";
    }

    @ControllerLog("分页查询配送方式列表")
    @RequestMapping("pageDlyTypeAjax")
    @ResponseBody
    @Authority(opCode = "07010201", opName = "分页查询配送方式列表")
    public PageAjax<Map<String, Object>> pageDlyTypeAjax(PageAjax<Map<String, Object>> page) {
        return dlyTypeService.pageDlyType(page);
    }

    @ControllerLog("添加配送方式页面")
    @Authority(opCode = "07010202", opName = "添加配送方式页面")
    @RequestMapping("preAddDlyType")
    public String preAddDlyType() {
        return "dlytype/add_dlytype";
    }

    @ControllerLog("添加配送方式")
    @RequestMapping("addDlyType")
    @ResponseBody
    @Authority(opCode = "07010203", opName = "添加配送方式")
    public AjaxResult addDlyType(@RequestParam Map<String, Object> map) {
        try{
            return AppUtil.returnObj(dlyTypeService.addDlyType(map));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("更新配送方式页面")
    @Authority(opCode = "07010204", opName = "更新配送方式页面")
    @RequestMapping("preEditDlyType/{typeId}")
    public String preEditDlyType(@PathVariable("typeId") Long typeId, Map<String, Object> map) {
        DlyType dlyType = dlyTypeService.queryByTypeId(typeId);
        map.put("dlyType", dlyType);
        List<DlyTypeArea> dlyTypeAreaList = dlyTypeAreaService.listDlyTypeArea(typeId);
        map.put("dlyTypeAreaList", dlyTypeAreaList);
        map.put("dlyTypeAreaJson", JSONObject.toJSONString(dlyTypeAreaList));
        return "dlytype/edit_dlytype";
    }

    @ControllerLog("修改配送方式")
    @RequestMapping("editDlyType")
    @ResponseBody
    @Authority(opCode = "07010205", opName = "修改配送方式")
    public AjaxResult editDlyType(@RequestParam Map<String, Object> map) {
        try{
            return AppUtil.returnObj(dlyTypeService.editDlyType(map));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("删除配送方式")
    @RequestMapping("delDlyType/{typeId}")
    @ResponseBody
    @Authority(opCode = "07010206", opName = "删除配送方式")
    public AjaxResult delDlyType(@PathVariable("typeId") Long typeId) {
        try{
            return AppUtil.returnObj(dlyTypeService.delByTypeId(typeId));
        }catch (Exception e) {
            return AppUtil.returnObj(2,e.getMessage());
        }
    }

    @ControllerLog("省份选择页面")
    @Authority(opCode = "07010207", opName = "省份选择页面")
    @RequestMapping("preSelectProvinces")
    public String preSelectProvinces(Map<String, Object> map) {
        List<Address> provinceList = addressService.listProvince();
        map.put("provinceList", provinceList);
        return "dlytype/select_province";
    }
}
