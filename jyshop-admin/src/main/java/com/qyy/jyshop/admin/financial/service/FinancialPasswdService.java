package com.qyy.jyshop.admin.financial.service;

import javax.servlet.http.HttpServletRequest;

import com.qyy.jyshop.pojo.AjaxResult;

public interface FinancialPasswdService {

    AjaxResult checkFinancialPasswd(String password, HttpServletRequest request);

    Boolean checkExpire(HttpServletRequest request);

}
