package com.qyy.jyshop.basics.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.basics.service.WxService;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.util.StringUtil;

@RestController
public class WxController extends AppBaseController{

    @Autowired
    private WxService wxService;
    
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    
    @ModelAttribute
    public void setRes(HttpServletResponse response,HttpServletRequest request){
        this.response = response;
        this.request = request;
    }
    
    @RequestMapping("wxCode")
    public Map<String,Object> wxCode(String code)throws Exception{
        
        System.out.println("进来了"+code);
        
        if(StringUtil.isEmpty(code)){
            String req_url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb4868f50223328db&redirect_uri=http%3a%2f%2ftest.cheertea.com%2fbasics%2fwxCode&response_type=code&scope=snsapi_userinfo#wechat_redirect";
            response.sendRedirect(req_url);
        }
        return null;
    }
    
    @RequestMapping("wxLogin")
    public Map<String,Object> wxLogin(String code,Integer reqType, Integer parentId)throws Exception{
              
//        code="0817r63c1gOp1v00El3c1l9M2c17r632";
//        reqType=0;
        System.out.println("code:"+code);
        Map<String,Object> memberMap=this.wxService.wxLogin(code,reqType,parentId);
        if(memberMap!=null && !StringUtil.isEmpty(memberMap.get("token"))){
            return this.outMessage(0, "授权登陆成功...", memberMap);
        }else{
            return this.outMessage(1, "授权失败 ...", null);
        }   
    }
    
    @RequestMapping("anon/wxSign")
    public Map<String,Object> wxSign(String url)throws Exception{
      if(StringUtils.isEmpty(url)){
          return this.outMessage(1, "url不能为空...", null);
      }
      try{
          System.out.println(url);
          //url = "http%3a%2f%2fht.cheertea.com";
          return this.outMessage(0, "获取成功...", this.wxService.wxSign(url));
      }catch(Exception ex){
          ex.printStackTrace();
          return this.outMessage(1, "获取成功 ...", null);
      }
         
  }
    
}
