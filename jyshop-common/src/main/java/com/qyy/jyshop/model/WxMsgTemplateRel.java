package com.qyy.jyshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信消息模版发送会员关联表
 */
@Entity
@Table(name = "shop_wx_msg_template_rel")
public class WxMsgTemplateRel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // id

    @Column(name = "tpl_id")
    private Long tplId; // 模版id

    @Column(name = "member_id")
    private Long memberId; // 会员id

    /**
     * @author jiangbin
     * @created 2018年4月12日 下午3:27:19
     * @return type
     */
    public Long getId() {
        return id;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 下午3:27:19
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 下午3:27:19
     * @return type
     */
    public Long getTplId() {
        return tplId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 下午3:27:19
     * @param tplId
     */
    public void setTplId(Long tplId) {
        this.tplId = tplId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 下午3:27:19
     * @return type
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * @author jiangbin
     * @created 2018年4月12日 下午3:27:19
     * @param memberId
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

}
