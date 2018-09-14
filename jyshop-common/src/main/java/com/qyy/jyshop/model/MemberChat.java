package com.qyy.jyshop.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shop_member_chat")
public class MemberChat implements Serializable {

    /**
     * 描述
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //主键ID
    
    @Column(name = "member_id")
    private Long memberId; //用户ID
    
    private String nickname; //用户昵你
    
    @Column(name = "member_face")
    private String memberFace; //用户头像
    private String message; //发送信息
    
    @Column(name = "send_type")
    private Integer sendType; //发送类型 0:咨询1:回复
    
    @Column(name = "create_time")
    private Timestamp createTime; //创建时间
    
    @Column(name = "send_time")
    private Timestamp sendTime; //发送时间
    
    @Column(name = "cs_id")
    private Integer csId; //客服ID
    
    @Column(name = "cs_name")
    private String csName; //客服昵称
    
    @Column(name = "cs_uname")
    private String csUname; //客服帐号
    
    @Column(name = "member_see")
    private Integer memberSee; //用户查看状态 0:己查看 1:未查看
    
    @Column(name = "cs_see")
    private Integer csSee; //客服查看状态 0:己查看 1:未查看

    @Column(name = "com_id")
    private Integer comId; 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemberFace() {
        return memberFace;
    }

    public void setMemberFace(String memberFace) {
        this.memberFace = memberFace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getCsId() {
        return csId;
    }

    public void setCsId(Integer csId) {
        this.csId = csId;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public String getCsUname() {
        return csUname;
    }

    public void setCsUname(String csUname) {
        this.csUname = csUname;
    }

    public Integer getMemberSee() {
        return memberSee;
    }

    public void setMemberSee(Integer memberSee) {
        this.memberSee = memberSee;
    }

    public Integer getCsSee() {
        return csSee;
    }

    public void setCsSee(Integer csSee) {
        this.csSee = csSee;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

}
