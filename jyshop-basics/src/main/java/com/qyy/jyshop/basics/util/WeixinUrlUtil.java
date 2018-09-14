package com.qyy.jyshop.basics.util;

import com.qyy.jyshop.basics.conf.WeixinAuthToken;
import com.qyy.jyshop.basics.conf.WeixinConfig;
import com.qyy.jyshop.util.StringUtil;
import net.sf.json.JSONObject;

import java.util.Map;

public class WeixinUrlUtil {

	//微信用户授权回调地址(获取access_token和openid)
	public static WeixinAuthToken getWeixinOpenidJson(String code,String appId,String appSecret){
	    WeixinAuthToken wat = null;
		String code_url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
				+ "appid="+appId
				+ "&secret="+appSecret	//公众号的appsecret
				+ "&code="+code		//填写code参数
				+ "&grant_type=authorization_code";
		System.out.println("请求地址:"+code_url);
		// 获取网页授权凭证
        JSONObject jsonObject = HttpsUtil.httpsRequest(code_url, HttpsUtil.GET, null);
        System.out.println("返回数据:"+jsonObject.toString());
        if (null != jsonObject) {
            try {
                wat = new WeixinAuthToken();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
                if(!StringUtil.isEmpty(jsonObject.get("unionid"))){
                    wat.setUnionId(jsonObject.getString("unionid"));
                }
                
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
            }
        }
        
        return wat;
	}
	
	//H5获取unionID
	public static JSONObject getUnionID(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        //String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        url = url.replace("ACCESS_TOKEN", access_token);
        url = url.replace("OPENID", openid);
        JSONObject jsonObject = HttpsUtil.httpsRequest(url, HttpsUtil.GET, null);
        return jsonObject;
    }
	
	// ios获取个人信息unionID
    public static JSONObject getUnionIDForApp(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        url = url.replace("ACCESS_TOKEN", access_token);
        url = url.replace("OPENID", openid);
        JSONObject jsonObject = HttpsUtil.httpsRequest(url, HttpsUtil.GET, null);
        return jsonObject;
    }

    // 检验access_token是否有效
    public static boolean validateAccessToken(String token, String openid) {
        String token_validate_url = "https://api.weixin.qq.com/sns/auth?access_token="
                + token + "&openid=" + openid;
        JSONObject jsonObject = HttpsUtil.httpsRequest(token_validate_url, HttpsUtil.GET, null);
        Map result = (Map) JSONObject.toBean(jsonObject, Map.class);
        if (result.get("errcode").toString().equals("0")) {
            return true;
        }
        return false;
    }

    //获取通用accessToken
    public static String getAccessToken() {
	    String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        access_token_url = access_token_url.replace("APPID", WeixinConfig.APP_ID);
        access_token_url = access_token_url.replace("APPSECRET", WeixinConfig.APP_SECRET);
        JSONObject jsonObject = HttpsUtil.httpsRequest(access_token_url, HttpsUtil.GET, null);
        System.out.println("getAccessToken jsonObject:" + jsonObject);
        Map result = (Map) JSONObject.toBean(jsonObject, Map.class);
        if (result.get("errcode")!=null) {
            return null;
        }
        return jsonObject.getString("access_token");
    }
    
    
    //获取通用accessToken
    public static String getJsapiTicket(String accessToken) {
        String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        jsapi_ticket_url = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = HttpsUtil.httpsRequest(jsapi_ticket_url, HttpsUtil.GET, null);
        Map result = (Map) JSONObject.toBean(jsonObject, Map.class);
        if (result.get("errcode")!=null && result.get("errmsg")!=null && (Integer)result.get("errcode")==0 && 
                ((String)result.get("errmsg")).equalsIgnoreCase("ok")) {
            return jsonObject.getString("ticket");
        }
        return null;
    }
    
}
