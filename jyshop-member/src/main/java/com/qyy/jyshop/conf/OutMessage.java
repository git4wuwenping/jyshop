package com.qyy.jyshop.conf;

/**
 * 消息推送实体
 */
public class OutMessage {

    private String sessionId;
    private String nickName;
    private String face;
    private String content;
    private String sendDate;
    
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getFace() {
        return face;
    }
    public void setFace(String face) {
        this.face = face;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSendDate() {
        return sendDate;
    }
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

}
