package com.qyy.jyshop.basics.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.basics.service.WechatMsgTemplateService;
import com.qyy.jyshop.basics.util.HttpsUtil;
import com.qyy.jyshop.basics.util.WeixinUrlUtil;
import com.qyy.jyshop.dao.MemberDao;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.redis.RedisDao;

@Service
public class WechatMsgTemplateServiceImpl implements WechatMsgTemplateService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private RedisDao redisDao;

    /**
     * 发送模版信息
     * 
     * @author jiangbin
     * @created 2018年4月11日 下午4:05:19
     * @param memberId
     * @param callUrl
     * @param bgColor
     * @param jsonObject
     */
    @Override
    public String sendMsgs(List<Long> memberIdList, String templatId, String clickurl, String topcolor,
            List<JSONObject> jsonObject) {
        int successNum = 0;
        String accessToken = (String) redisDao.getObject("WEIXIN_ACCESS_TOKEN");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = WeixinUrlUtil.getAccessToken();
            redisDao.saveObject("WEIXIN_ACCESS_TOKEN", accessToken, 7200);
        }

        for (int i = 0; i < memberIdList.size(); i++) {
            Member member = memberDao.selectByPrimaryKey(memberIdList.get(i));
            if (member == null || StringUtils.isBlank(member.getOpenId())) {
                continue;
            }
            String openId = member.getOpenId();
            Integer state = WechatMsgTemplateServiceImpl.subscribeState(openId, accessToken);
            if (state == 1) {
                String result = WechatMsgTemplateServiceImpl.sendWechatMsgToUser(openId, templatId, clickurl, topcolor,
                        jsonObject.get(i), accessToken);
                if(result.endsWith("success")){
                    successNum ++ ;
                }
            }
        }
        StringBuilder ret = new StringBuilder();
        ret.append("成功发送").append(successNum).append("条").append(",发送失败").append(memberIdList.size()-successNum).append("条.");
        return ret.toString();

    }

    @Override
    public String sendMsg(List<Long> memberIdList, String templatId, String clickurl, String topcolor,
            JSONObject jsonObject) {
        int successNum = 0;
        String accessToken = (String) redisDao.getObject("WEIXIN_ACCESS_TOKEN");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = WeixinUrlUtil.getAccessToken();
            redisDao.saveObject("WEIXIN_ACCESS_TOKEN", accessToken, 7200);
        }
        System.out.println(accessToken);
        for (int i = 0; i < memberIdList.size(); i++) {
            Member member = memberDao.selectByPrimaryKey(memberIdList.get(i));
            if (member == null || StringUtils.isBlank(member.getOpenId())) {
                continue;
            }
            String openId = member.getOpenId();
            Integer state = WechatMsgTemplateServiceImpl.subscribeState(openId, accessToken);
            if (state == 1) {
                String result = WechatMsgTemplateServiceImpl.sendWechatMsgToUser(openId, templatId, clickurl, topcolor,
                        jsonObject, accessToken);
                if(result.endsWith("success")){
                    successNum ++ ;
                }
            }
        }
        StringBuilder ret = new StringBuilder();
        ret.append("成功发送").append(successNum).append("条").append(",发送失败").append(memberIdList.size()-successNum).append("条.");
        return ret.toString();
    }

    private static Integer subscribeState(String openId, String accessToken) {

        JSONObject jsonObject = WeixinUrlUtil.getUnionID(accessToken, openId);
        Map result = (Map) JSONObject.toBean(JSONObject.fromObject(jsonObject), Map.class);
        if (result.get("errmsg") != null || result.get("errcode") != null) {
            return -1;
        }
        if (jsonObject != null) {
            // 用户是否订阅该公众号标识（0代表此用户没有关注该公众号 1表示关注了该公众号）。
            Integer subscribe = (Integer) result.get("subscribe");
            return subscribe;
        }
        return -1;
    }

    private static String sendWechatMsgToUser(String touser, String templatId, String clickurl, String topcolor,
            JSONObject data, String accessToken) {
        String tmpurl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        JSONObject json = new JSONObject();
        json.put("touser", touser);
        json.put("template_id", templatId);
        json.put("url", clickurl);
        json.put("topcolor", topcolor);
        json.put("data", data);
        try {
            JSONObject jsonObject = HttpsUtil.httpsRequest(tmpurl, "POST", json.toString());
            Map result = (Map) JSONObject.toBean(JSONObject.fromObject(jsonObject), Map.class);
            // {errcode=0, errmsg=ok, msgid=232297049100484610}
            if (result.get("errmsg") != null && !"ok".equals((String) result.get("errmsg"))
                    && result.get("errcode") != null && (Integer) result.get("errcode") != 0) {
                return "error";
            }
            if (result.get("errmsg") == null || result.get("errcode") == null) {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }
}
