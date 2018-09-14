package com.qyy.jyshop.admin.financial.service;

import java.util.Map;

import com.qyy.jyshop.pojo.PageAjax;

public interface RefundDtlService {

    PageAjax<Map<String, Object>> pageAjaxRefundDtl(PageAjax<Map<String, Object>> page);

}
