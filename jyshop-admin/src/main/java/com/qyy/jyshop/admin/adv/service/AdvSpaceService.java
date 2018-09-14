package com.qyy.jyshop.admin.adv.service;

import com.qyy.jyshop.model.AdvSpace;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.pojo.PageAjax;

public interface AdvSpaceService {

    PageAjax<AdvSpace> pageAdvSpace(PageAjax<AdvSpace> page, AdvSpace advSpace);

    String addAdvSpace(AdvSpace advSpace);

    AdvSpace queryAdvSpaceById(Long id);

    String doEditAdvSpace(AdvSpace advSpace);

    String doDelAdvSpace(Long id);

    AjaxResult updateAdvSpaceUsed(Long id);

}
