package com.qyy.jyshop.conf;

/**
 * 消息接收实体
 */
public class InMessage {

    private String outId;
    private String inId;
    private String username;
    private String name;
    private String sendMessage;
    
    public String getOutId() {
        return outId;
    }
    public void setOutId(String outId) {
        this.outId = outId;
    }
    public String getInId() {
        return inId;
    }
    public void setInId(String inId) {
        this.inId = inId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSendMessage() {
        return sendMessage;
    }
    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }
       
}
