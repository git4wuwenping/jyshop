package com.qyy.jyshop.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.chat.service.MemberChatService;
import com.qyy.jyshop.conf.InMessage;
import com.qyy.jyshop.conf.OutMessage;
import com.qyy.jyshop.conf.SocketSessionRegistry;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.model.MemberChat;
import com.qyy.jyshop.util.StringUtil;


@RestController
public class ChatController extends AppBaseController{

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public SocketSessionRegistry socketSessionRegistry;
    @Autowired
    private MemberChatService memberChatService;
    
    /**
     * 发送信息
     * @author hwc
     * @created 2018年4月3日 上午9:40:23
     * @param message
     * @param token
     * @throws Exception
     */
    @RequestMapping(value ="sendInfo")
    public void sendInfo(InMessage message,String token,Integer comId) throws Exception {
        
        System.out.println("发送信息...");
        OutMessage outMessage=this.memberChatService.doSendInfo(message, token,comId);
        if(!StringUtil.isEmpty(outMessage))
            simpMessagingTemplate.convertAndSendToUser(outMessage.getSessionId(),"/topic/greetings",outMessage,createHeaders(outMessage.getSessionId()));
    
    }
    
    /**
     * 获取在线用户
     * @author hwc
     * @created 2018年4月3日 上午9:40:35
     * @return
     */
    @RequestMapping(value ="queryAllUser")
    public Map<String,Object> queryAllUser(){
        Map<String,Object> returnMap =  (Map<String,Object>)this.socketSessionRegistry.getAllNickNames();
        
        if(returnMap!=null){
            for (String key : returnMap.keySet()) {
                System.out.println(key + " :" + returnMap.get(key));
            }
        }
        return this.outMessage(0, "获取成功", returnMap);
    }
    
    /**
     * 获取的咨询用户 
     * @author hwc
     * @created 2018年4月26日 上午10:27:04
     * @return
     */
    @RequestMapping(value ="anon/queryChatUser")
    public Map<String,Object> queryChatUser(String outId){
        Map<String,Object> returnMap =  (Map<String,Object>)this.socketSessionRegistry.getUserNickNames(outId);
        
        if(returnMap!=null){
            for (String key : returnMap.keySet()) {
                System.out.println(key + " :" + returnMap.get(key));
            }
        }
        return this.outMessage(0, "获取成功", returnMap);
    }
    
    /**
     * 获取用户未读信息
     * @author hwc
     * @created 2018年4月3日 上午9:42:20
     * @param token
     * @return
     */
    public Map<String,Object> queryByUnreadInfo(String token){
        
        List<MemberChat> memberChatList=this.memberChatService.queryByUnreadInfo(token);
        Map<String,Object> returnMap=new HashMap<String,Object>();
        returnMap.put("memberChatList", memberChatList);
        return this.outMessage(0, "获取成功", returnMap);
    }
    
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
