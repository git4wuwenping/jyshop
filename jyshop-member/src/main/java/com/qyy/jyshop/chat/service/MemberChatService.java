package com.qyy.jyshop.chat.service;

import java.util.List;

import com.qyy.jyshop.conf.InMessage;
import com.qyy.jyshop.conf.OutMessage;
import com.qyy.jyshop.model.MemberChat;

public interface MemberChatService {

    /**
     * 获取用户未读取的信息
     * @author hwc
     * @created 2018年4月3日 上午9:53:03
     * @param token
     * @return
     */
    public List<MemberChat> queryByUnreadInfo(String token);
    
    /**
     * 发送信息
     * @author hwc
     * @created 2018年3月13日 上午10:34:15
     * @param inMessage
     * @param token
     * @return
     */
    public OutMessage doSendInfo(InMessage inMessage,String token,Integer comId);
}
