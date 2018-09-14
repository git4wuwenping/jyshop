package com.qyy.jyshop.admin.financial.service;

import java.util.List;

import com.qyy.jyshop.model.PayLog;
import com.qyy.jyshop.pojo.AjaxResult;

public interface MemberAdvanceService {

    List<PayLog> queryPayLogListByMemberId(Long memberId);

    AjaxResult recharge(String memberId, String rmb,String adminRemark);

}
