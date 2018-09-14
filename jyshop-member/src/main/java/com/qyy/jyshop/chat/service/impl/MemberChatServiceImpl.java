package com.qyy.jyshop.chat.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qyy.jyshop.chat.service.MemberChatService;
import com.qyy.jyshop.conf.InMessage;
import com.qyy.jyshop.conf.OutMessage;
import com.qyy.jyshop.conf.SocketSessionRegistry;
import com.qyy.jyshop.dao.MemberChatDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.MemberChat;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.StringUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class MemberChatServiceImpl extends AbstratService<MemberChat>  implements MemberChatService {
    
    @Autowired
    public SocketSessionRegistry socketSessionRegistry;
    @Autowired
    public MemberChatDao memberChatDao;
    
    @Override
    public List<MemberChat> queryByUnreadInfo(String token){
        return this.memberChatDao.selectByUnreadInfo(this.getMemberId(token));
    }
    
    @Override
    public OutMessage doSendInfo(InMessage inMessage, String token,Integer comId) {
        
        if(StringUtil.isEmpty(inMessage.getOutId()))
            throw new AppErrorException("发送用户不能为空……");
        
        OutMessage outMessage=new OutMessage();
        outMessage.setNickName(inMessage.getName());
        MemberChat memberChat=new MemberChat();
        memberChat.setSendType(0);
        //判断是否是客服发送给用户
        if(!StringUtil.isEmpty(inMessage.getInId())){
            memberChat.setCsId(Integer.valueOf(inMessage.getInId()));
            memberChat.setCsUname(inMessage.getUsername());
            memberChat.setCsName(inMessage.getName());
            token=inMessage.getOutId();
            memberChat.setSendType(1);
            memberChat.setComId(comId);
        }else{
            memberChat.setComId(Integer.valueOf(inMessage.getOutId()));
        }
        
        String memberId=inMessage.getOutId();

        System.out.println("发送用户:"+token);
        Member member=this.getMember(token);
        
        if(StringUtil.isEmpty(outMessage.getNickName()))
            outMessage.setNickName(member.getNickname());
        
        Timestamp nowDate=new Timestamp(System.currentTimeMillis());
        memberChat.setMemberId(member.getMemberId());
        memberChat.setNickname(member.getNickname());
        memberChat.setMemberFace(member.getWeixinFace());
        memberChat.setMessage(inMessage.getSendMessage());
        memberChat.setCreateTime(nowDate);
        memberChat.setSendTime(nowDate);
        String sessionId=socketSessionRegistry.getSessionIds(memberId);
        if(StringUtil.isEmpty(sessionId)){
            //判断是否是发送给客服
            if(!StringUtil.isEmpty(inMessage.getInId())){
                memberChat.setCsSee(1);
            }else{
                memberChat.setMemberSee(1);
            }
            outMessage=null;
        }else{
            outMessage.setSessionId(sessionId);
            outMessage.setFace(memberChat.getMemberFace());
            outMessage.setContent(inMessage.getSendMessage());
            outMessage.setSendDate(TimestampUtil.getTimestampToStr(memberChat.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        
        this.memberChatDao.insertSelective(memberChat);
        return outMessage;
    }
  
}
