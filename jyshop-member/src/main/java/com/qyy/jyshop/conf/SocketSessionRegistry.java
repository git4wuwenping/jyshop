package com.qyy.jyshop.conf;

import com.qyy.jyshop.util.StringUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SocketSessionRegistry {

    private final ConcurrentMap<String, String> userSessionIds = new ConcurrentHashMap<String, String>();
    private final ConcurrentMap<String, Object> userNickNames = new ConcurrentHashMap<String, Object>();
    private final ConcurrentMap<String, ConcurrentMap<String,Object>> comUserMap = new ConcurrentHashMap<String, ConcurrentMap<String,Object>>();
    private final Object lock = new Object();

    public SocketSessionRegistry() {
    }

    public String getSessionIds(String user) {
        return this.userSessionIds.get(user);
    }

    public ConcurrentMap<String, String> getAllSessionIds() {
        return this.userSessionIds;
    }

    public ConcurrentMap<String, Object> getAllNickNames() {
        return this.userNickNames;
    }
    
    public ConcurrentMap<String, Object> getUserNickNames(String outId) {
        return this.comUserMap.get(outId);
    }

    public void registerSessionId(String token, String sessionId,String outId,String nickname) {
        synchronized (this.lock) {
            this.userSessionIds.put(token, sessionId);
            this.userNickNames.put(token, nickname);
            if(!StringUtil.isEmpty(outId)){
                ConcurrentMap<String,Object> userMap=comUserMap.get(outId);
                System.out.println("userMap:"+userMap);
                if(userMap==null){
                    userMap=new ConcurrentHashMap<String,Object>();   
                }
                
                userMap.put(token, nickname);
                comUserMap.put(outId, userMap);
            }
            
        }
    }

    public void unregisterSessionId(String token, String sessionId) {
        synchronized (this.lock) {
            String session = this.userSessionIds.get(token);
            if (!StringUtil.isEmpty(session)) {
                this.userSessionIds.remove(token);
                this.userNickNames.remove(token);
            }

        }
    }
}
