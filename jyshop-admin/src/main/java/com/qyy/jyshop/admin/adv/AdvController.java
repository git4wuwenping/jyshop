/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.adv;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.adv.service.AdvService;
import com.qyy.jyshop.admin.adv.service.AdvSpaceService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.model.Adv;
import com.qyy.jyshop.model.AdvSpace;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

/**
 * 描述
 * @author jiangbin
 * @created 2018年3月15日 下午2:55:42
 */

@Controller
@RequestMapping("/admin/adv")
public class AdvController extends BaseController{
    @Autowired
    private AdvService advService;
    @Autowired
    private AdvSpaceService advSpaceService;
    
    

    @RequestMapping("advSpaceMain")
    @Authority(opCode = "140201", opName = "广告位管理界面")
    public String advSpaceMain() {
        return "adv/adv_space_main";
    }

    @ControllerLog("广告位列表")
    @RequestMapping("pageAdvSpaceAjax")
    @ResponseBody
    @Authority(opCode = "14020101", opName = "广告位列表")
    public PageAjax<AdvSpace> pageAdvSpaceAjax(PageAjax<AdvSpace> page, AdvSpace advSpace) {
        return advSpaceService.pageAdvSpace(page,advSpace);
    }
    
    @RequestMapping("preAddAdvSpace")
    @Authority(opCode = "14020102", opName = "广告位新增页面")
    public String preAddAdvSpace() {
        return "adv/add_adv_space";
    }

    @ControllerLog("新增广告位")
    @ResponseBody
    @RequestMapping(value="addAdvSpace",method=RequestMethod.POST)
    @Authority(opCode = "14020103", opName = "新增广告位")
    public AjaxResult addAdvSpace(AdvSpace advSpace) {
        try {
            return AppUtil.returnObj(advSpaceService.addAdvSpace(advSpace));
        } catch (RuntimeException e) {
            this.logger.error("添加广告位失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @RequestMapping("preEditAdvSpace/{id}")
    @Authority(opCode = "14020104", opName = "广告位修改页面")
    public String preEditAdvSpace(@PathVariable("id") Long id, Map<String, Object> map) {
        map.put("advSpace", this.advSpaceService.queryAdvSpaceById(id));
        return "adv/edit_adv_space";
    }
    
    @ControllerLog("修改广告位")
    @RequestMapping("editAdvSpace")
    @ResponseBody
    @Authority(opCode = "14020105", opName = "修改广告位")
    public AjaxResult editAdvSpace(AdvSpace advSpace) {
        try {
            return AppUtil.returnObj(advSpaceService.doEditAdvSpace(advSpace));
        } catch (RuntimeException e) {
            this.logger.error("修改广告位失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @ControllerLog("修改广告位状态")
    @RequestMapping("updateAdvSpaceUsed/{id}")
    @ResponseBody
    @Authority(opCode = "14020106", opName = "修改广告位状态")
    public AjaxResult updateAdvSpaceUsed(@PathVariable("id") Long id) {
        try {
            return advSpaceService.updateAdvSpaceUsed(id);
        } catch (RuntimeException e) {
            this.logger.error("修改广告位失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @ControllerLog("删除广告位")
    @RequestMapping("delAdvSpace/{id}")
    @ResponseBody
    @Authority(opCode = "14020107", opName = "删除广告位")
    public AjaxResult delAdvSpace(@PathVariable("id") Long id) {
        try {
            return AppUtil.returnObj(advSpaceService.doDelAdvSpace(id));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @RequestMapping("prePageAdv")
    @Authority(opCode = "14020108", opName = "广告位广告页面")
    public String prePageAdv(Long advSpaceId,Map<String,Object> map) {
        map.put("advSpaceId", advSpaceId);
        return "adv/adv_main";
    }
    
    @ControllerLog("广告列表")
    @RequestMapping("pageAdvAjax/{advSpaceId}")
    @ResponseBody
    @Authority(opCode = "14020109", opName = "广告列表")
    public PageAjax<Map<String,Object>> pageAdvAjax(PageAjax<Map<String,Object>> page,@PathVariable("advSpaceId") Long advSpaceId) {
        return advService.pageAdv(page,advSpaceId);
    }
    
    @RequestMapping("preAddAdv")
    @Authority(opCode = "14020110", opName = "广告新增页面")
    public String preAddAdv(Long advSpaceId,Map<String, Object> map) {
        map.put("advSpaceId", advSpaceId);
        return "adv/add_adv";
    }

    @ControllerLog("新增广告")
    @ResponseBody
    @RequestMapping(value="addAdv",method=RequestMethod.POST)
    @Authority(opCode = "14020111", opName = "新增广告")
    public AjaxResult addAdv(Adv adv) {
        try {
            return AppUtil.returnObj(advService.doAddAdv(adv));
        } catch (RuntimeException e) {
            this.logger.error("添加广告失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    
    @RequestMapping("preEditAdv/{id}")
    @Authority(opCode = "14020112", opName = "广告修改页面")
    public String preEditAdv(@PathVariable("id") Long id,Map<String, Object> map) {
        map.put("adv", this.advService.queryAdvById(id));
        return "adv/edit_adv";
    }
    
    @ControllerLog("修改广告信息")
    @RequestMapping("editAdv")
    @ResponseBody
    @Authority(opCode = "14020113", opName = "修改广告信息")
    public AjaxResult editAdv(Adv adv) {
        try {
            return AppUtil.returnObj(advService.doEditAdv(adv));
        } catch (RuntimeException e) {
            this.logger.error("修改广告失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @ControllerLog("修改广告状态")
    @RequestMapping("updateAdvUsed/{id}")
    @ResponseBody
    @Authority(opCode = "14020114", opName = "修改广告位状态")
    public AjaxResult updateAdvUsed(@PathVariable("id") Long id) {
        try {
            return advService.updateAdvUsed(id);
        } catch (RuntimeException e) {
            this.logger.error("修改广告位失败", e);
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @RequestMapping("delAdv/{id}")
    @ResponseBody
    @Authority(opCode = "14020115", opName = "删除广告")
    public AjaxResult delAdv(@PathVariable("id") Long id) {
        try {
            return AppUtil.returnObj(advService.delAdById(id));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    
}
