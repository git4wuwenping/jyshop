package com.qyy.jyshop.admin.chat.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.qyy.jyshop.admin.chat.service.MemberChatService;
import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.member.service.MemberService;
import com.qyy.jyshop.model.AuthUser;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.util.TimestampUtil;


@Controller
@RequestMapping("/admin/chat")
public class ChatController {
    
    @Value("${interface-url}")
    private String interfaceUrl;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberChatService memberChatService;
    
    @ControllerLog("咨询界面")
    @Authority(opCode = "100102", opName = "咨询界面")
    @RequestMapping("preChatInterface")
    public String preSendInfo(Map<String, Object> returnMap) {
        RestTemplate restTemplate=new RestTemplate();
        interfaceUrl="http://192.168.146.128:5555";
        String strUser=restTemplate.getForEntity(interfaceUrl+"/member/anon/queryChatUser?outId=0", String.class).getBody();
        JSONObject jsonUser=JSONObject.fromObject(strUser); 
        returnMap.put("chatUrl", interfaceUrl);
        
        if(jsonUser.getInt("res_code")==0){
            returnMap.put("userList", jsonUser.getJSONObject("res_data"));
        }
        AuthUser authUser=(AuthUser) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getAttribute("loginUser");
        if(authUser!=null){
            returnMap.put("loginId", authUser.getId());
            returnMap.put("loginUname", authUser.getUsername());
            returnMap.put("loginName", authUser.getName());
        }
        return "chat/send_info"; 
    }
    
    @ControllerLog("咨询记录")
    @Authority(opCode = "100101", opName = "咨询记录")
    @RequestMapping("chatMain")
    public String chatMain(Map<String, Object> map) {
    	return "chat/member_chat_main";
    }
    
    @ControllerLog("查询用户最后咨询列表")
    @RequestMapping("pageChatAjax")
    @ResponseBody
    @Authority(opCode = "10010101", opName = "查询用户最后咨询列表")
    public PageAjax<Map<String,Object>> pageChatAjax(PageAjax<Map<String,Object>> page) {
        return this.memberChatService.pageByLatest(page);
    }
    
    @ControllerLog("获取用户咨询列表")
    @RequestMapping("queryMemberChatAjax")
    @ResponseBody
    @Authority(opCode = "10010204", opName = "获取用户咨询列表")
    public List<Map<String,Object>> queryMemberChatAjax(@RequestParam Map<String,Object> params) {
        return this.memberChatService.queryTodayChatList(params);
    }
    
    @ControllerLog("查询用户咨询列表(分页)")
    @RequestMapping("pageMemberChatAjax")
    @ResponseBody
    @Authority(opCode = "10010104", opName = "查询用户咨询列表(分页)")
    public PageAjax<Map<String,Object>> pageMemberChatAjax(PageAjax<Map<String,Object>> page) {
        return this.memberChatService.pageMemberChatByParams(page);
    }
    
    @ControllerLog("查询咨询详情")
    @RequestMapping("preMemberChatDetails/{memberId}")
    @Authority(opCode = "10010102", opName = "查询咨询详情")
    public ModelAndView preMemberChatDetails(@PathVariable("memberId")Long memberId,ModelMap model) {
        model.put("member", this.memberService.preDetaileCustomer(memberId));
        return new ModelAndView("chat/member_chat_detail",model);
    }
    @ControllerLog("导出咨询信息到excel")
    @RequestMapping("exportOrderToExcel")
    @Authority(opCode = "03010104", opName = "导出咨询信息到excel")
    public void exportChatToExcel(HttpServletResponse response, @RequestParam Map<String, Object> map) throws Exception{
        ExcelData data = memberChatService.getExportData(map,response);
        //ExportExcelUtils.exportExcel(response,"订单数据" + DateUtil.dateToDateFullString(new Date())+ ".xlsx",data);
        //logger.info("controller结束------" + TimestampUtil.getNowTime());
    }
    
}
