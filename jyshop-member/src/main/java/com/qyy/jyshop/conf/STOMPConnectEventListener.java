package com.qyy.jyshop.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.qyy.jyshop.exception.AppLoginException;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.redis.RedisDao;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;

public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {
    
    @Autowired
    private RedisDao redisDao;
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String token = sha.getNativeHeader("login").get(0);
        String outId = sha.getNativeHeader("outId").get(0);
        String sessionId = sha.getSessionId();
        Member member=(Member)this.redisDao.getObject(token);
        if((member==null || StringUtil.isEmpty(member.getMemberId()) ) && 
               (!DictionaryUtil.getDataNameByValue("user_type", "1").equals(token) &&
                !DictionaryUtil.getDataNameByValue("user_type", "2").equals(token) )){
            throw new AppLoginException("请先登陆....");
        }else{
            if(member!=null)
                webAgentSessionRegistry.registerSessionId(token,sessionId,outId,member.getNickname());
            else if(DictionaryUtil.getDataNameByValue("user_type", "1").equals(token))
                webAgentSessionRegistry.registerSessionId(outId,sessionId,null,"客服");
            else if(DictionaryUtil.getDataNameByValue("user_type", "2").equals(token))
                webAgentSessionRegistry.registerSessionId(outId,sessionId,null,"商家客服");
        }
   }   
}
