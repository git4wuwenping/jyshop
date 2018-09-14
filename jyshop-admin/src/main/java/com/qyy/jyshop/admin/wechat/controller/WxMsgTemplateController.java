package com.qyy.jyshop.admin.wechat.controller;

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
import com.qyy.jyshop.admin.wechat.service.WxMsgTemplateLogService;
import com.qyy.jyshop.admin.wechat.service.WxMsgTemplateRelService;
import com.qyy.jyshop.admin.wechat.service.WxMsgTemplateService;
import com.qyy.jyshop.model.WxMsgTemplateDetail;
import com.qyy.jyshop.model.WxMsgTemplateInfo;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/wechat/msgTemplate")
public class WxMsgTemplateController extends BaseController {

    @Autowired
    private WxMsgTemplateService wxMsgTemplateService;
    @Autowired
    private WxMsgTemplateRelService wxMsgTemplateRelService;
    @Autowired
    private WxMsgTemplateLogService wxMsgTemplateLogService;

    @Authority(opCode = "160101", opName = "微信消息模版")
    @RequestMapping(value = "wxMsgTemplateMain", method = { RequestMethod.POST, RequestMethod.GET })
    public String wxMsgTemplateMain() {
        return "wechat/msg_template/template_main";
    }

    @ControllerLog("查询微信消息模版列表")
    @RequestMapping(value = "pageMsgTemplateAjax", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "16010101", opName = "查询微信消息模版列表")
    public PageAjax<WxMsgTemplateInfo> pageMsgTemplateAjax(PageAjax<WxMsgTemplateInfo> page) {
        return wxMsgTemplateService.pageTemplate(page);
    }

    

    @ControllerLog("微信消息模版详情页面")
    @Authority(opCode = "16010102", opName = "微信消息模版详情页面")
    @RequestMapping(value = "detailMsgTemplate/{tplId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String detailMsgTemplate(@PathVariable("tplId") Long tplId, Map<String, Object> map) {
        WxMsgTemplateInfo info = wxMsgTemplateService.queryInfoById(tplId);
        map.put("info", info);
        List<WxMsgTemplateDetail> detail = wxMsgTemplateService.queryDetailByTplId(tplId);
        map.put("detail", detail);
        return "wechat/msg_template/template_detail";
    }
    
    
    @ControllerLog("微信消息模版发送页面")
    @Authority(opCode = "16010103", opName = "微信消息模版发送页面")
    @RequestMapping(value = "preSendMsg/{tplId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String preSendMsg(@PathVariable("tplId") Long tplId, Map<String, Object> map) {
        List<WxMsgTemplateDetail> detail = wxMsgTemplateService.queryDetailByTplId(tplId);
        map.put("detail", detail);
        map.put("tplId", tplId);
        WxMsgTemplateInfo info = wxMsgTemplateService.queryInfoById(tplId);
        map.put("templateId", info.getTemplateId());
        return "wechat/msg_template/template_send";
    }

    
    @ControllerLog("选择发送会员页面")
    @Authority(opCode = "16010104", opName = "选择发送会员页面")
    @RequestMapping(value = "selectMember/{tplId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String selectMember(@PathVariable("tplId") Long tplId, Map<String, Object> map) {
        map.put("tplId", tplId);
        
        return "wechat/msg_template/member_select";
    }
    
    @ControllerLog("添加关联会员")
    @RequestMapping("addRel")
    @ResponseBody
    @Authority(opCode = "16010105", opName = "添加商品标签关联商品")
    public AjaxResult addRel(@RequestParam(name="memberIdArr[]") Long[] memberIdArr,Long tplId) {
        try {
            return AppUtil.returnObj(wxMsgTemplateRelService.addRel(memberIdArr,tplId));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @ControllerLog("清除所有关联会员")
    @RequestMapping("delAllRel/{tplId}")
    @ResponseBody
    @Authority(opCode = "16010106", opName = "清除所有关联会员")
    public AjaxResult delAllRel(@PathVariable Long tplId) {
        try {
            return AppUtil.returnObj(wxMsgTemplateRelService.delRelByTplId(tplId));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    @ControllerLog("查询微信消息模版发送用户列表")
    @RequestMapping(value = "pageMemberRelAjax/{tplId}", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "16010107", opName = "查询微信消息模版发送用户列表")
    public PageAjax<Map<String,Object>> pageMemberRelAjax(@PathVariable("tplId") Long tplId,PageAjax<Map<String,Object>> page) {
        return wxMsgTemplateRelService.pageMemberRel(tplId,page);
    }
    
    @ControllerLog("删除微信消息模版发送用户")
    @RequestMapping("delMemberRel/{id}")
    @ResponseBody
    @Authority(opCode = "16010108", opName = "删除微信消息模版发送用户")
    public AjaxResult delMemberRel(@PathVariable("id") Long id) {
        try {
            return AppUtil.returnObj(wxMsgTemplateRelService.delMemberRel(id));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    
    
    @ControllerLog("发送微信消息模版给指定会员")
    @RequestMapping("sendMsg")
    @ResponseBody
    @Authority(opCode = "16010109", opName = "发送微信消息模版给指定会员")
    public AjaxResult sendMsg(@RequestParam Map<String,Object> map) {
        try {
            return AppUtil.returnObj(wxMsgTemplateService.sendMsg(map));
        } catch (Exception e) {
            return AppUtil.returnObj(2, e.getMessage());
        }
    }
    //viewSendMsgLog
    @ControllerLog("查看消息发送日志页面")
    @Authority(opCode = "16010110", opName = "查看消息发送日志页面")
    @RequestMapping(value = "viewSendMsgLog/{tplId}", method = { RequestMethod.POST, RequestMethod.GET })
    public String viewSendMsgLog(@PathVariable("tplId") Long tplId,Map<String,Object> map) {
        map.put("tplId", tplId);
        return "wechat/msg_template/template_log";
    }
    
    @ControllerLog("查询微信消息模版列表")
    @RequestMapping(value = "pageMsgLogAjax/{tplId}", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @Authority(opCode = "16010111", opName = "查询微信消息模版列表")
    public PageAjax<Map<String,Object>> pageMsgLogAjax(@PathVariable("tplId") Long tplId,PageAjax<Map<String,Object>> page) {
        return wxMsgTemplateLogService.pageMsgLog(tplId,page);
    }
}
