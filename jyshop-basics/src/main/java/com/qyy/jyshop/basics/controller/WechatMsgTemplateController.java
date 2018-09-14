package com.qyy.jyshop.basics.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.basics.service.WechatMsgTemplateService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.vo.WechatMsgTemplateData;

@RestController
public class WechatMsgTemplateController extends AppBaseController {

    @Autowired
    private WechatMsgTemplateService wechatMsgTemplateService;

    protected HttpServletResponse response;
    protected HttpServletRequest request;

    @ModelAttribute
    public void setRes(HttpServletResponse response, HttpServletRequest request) {
        this.response = response;
        this.request = request;
    }

    /**
     * 
     * 微信消息模版发送
     * 
     * @author jiangbin
     * @created 2018年4月12日 上午9:09:34
     * @param memberIdList
     *            用户id列表
     * @param templatId
     *            模版id
     * @param callUrl
     *            点击url
     * @param bgColor
     *            背景颜色
     * @param jsonObjectList
     *            内容
     * @return
     * @throws Exception
     */
    @RequestMapping("/anon/sendWechatMsgsToUser")
    public Map<String, Object> sendWechatMsgsToUser(List<Long> memberIdList, String templatId, String callUrl,
            String bgColor, List<JSONObject> jsonObjectList, String token) throws Exception {
        // List<Long> memberIdList = new ArrayList<Long>();
        // memberIdList.add(209623064L);
        // String templatId = "iIF4rLsr2daTqPHkFOMObH4mCx4pwjZctY5qKIHHOMM";
        // String bgColor = "#000000";
        // String callUrl = "http://www.baidu.com";
        // List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        //
        // Map<String, WechatMsgTemplateData> param = new HashMap<>();
        // param.put("first", new WechatMsgTemplateData("恭喜！", "#696969"));
        // param.put("keyword1", new WechatMsgTemplateData("12345345456",
        // "#696969"));
        // param.put("keyword2", new WechatMsgTemplateData("2018年", "#696969"));
        // param.put("remark", new WechatMsgTemplateData("祝愉快！", "#696969"));
        //
        // JSONObject fromObject = JSONObject.fromObject(param);
        // jsonObjectList.add(fromObject);

        if (token == null || !token.equals("CHEERTEA")) {
            return this.outMessage(-1, "校验码不正确", null);
        }
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            String ret = this.wechatMsgTemplateService.sendMsgs(memberIdList, templatId, callUrl, bgColor,
                    jsonObjectList);
            retMap.put("ret", ret);
        } catch (Exception ex) {
            ex.printStackTrace();
            return this.outMessage(-2, "出错了", null);
        }

        return this.outMessage(0, "发送成功", retMap);
    }

    @RequestMapping("/anon/sendWechatMsgToUser")
    public Map<String, Object> sendWechatMsgToUser(String memberIds, String templatId, String callUrl,
            String bgColor, String jsonObjectStr, String token) throws Exception {
        if (token == null || !token.equals("CHEERTEA")) {
            return this.outMessage(-1, "校验码不正确", null);
        }
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            List<Long> memberIdList = new ArrayList<Long>();
            Arrays.asList(memberIds.split(",")).forEach(e -> {
                memberIdList.add(Long.parseLong(e));
            });
            
            JSONObject jsonObject = JSONObject.fromObject(jsonObjectStr);
            System.out.println(jsonObject);
            String ret = this.wechatMsgTemplateService.sendMsg(memberIdList, templatId, callUrl, bgColor, jsonObject );
            retMap.put("ret", ret);
        } catch (Exception ex) {
            ex.printStackTrace();
            return this.outMessage(-2, "出错了", null);
        }

        return this.outMessage(0, "发送成功", retMap);
    }

}
