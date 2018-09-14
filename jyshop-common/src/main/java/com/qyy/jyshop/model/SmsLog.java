package com.qyy.jyshop.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "shop_sms_log")
public class SmsLog {
    /**
     * 主键
     */
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    /**
     * 请求ip地址
     */
    @Column(name = "request_ip")
    private String requestIp;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

    /**
     * 1:注册；2认证
     */
    @Column(name = "log_type")
    private Integer logType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取主键
     *
     * @return log_id - 主键
     */
    public Long getLogId() {
        return logId;
    }

    /**
     * 设置主键
     *
     * @param logId 主键
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    /**
     * 获取请求ip地址
     *
     * @return request_ip - 请求ip地址
     */
    public String getRequestIp() {
        return requestIp;
    }

    /**
     * 设置请求ip地址
     *
     * @param requestIp 请求ip地址
     */
    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取验证码
     *
     * @return code - 验证码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置验证码
     *
     * @param code 验证码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取1:注册；2认证
     *
     * @return log_type - 1:注册；2认证
     */
    public Integer getLogType() {
        return logType;
    }

    /**
     * 设置1:注册；2认证
     *
     * @param logType 1:注册；2认证
     */
    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}