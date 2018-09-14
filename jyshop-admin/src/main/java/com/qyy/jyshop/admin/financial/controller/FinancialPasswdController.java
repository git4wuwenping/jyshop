package com.qyy.jyshop.admin.financial.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.financial.service.FinancialPasswdService;
import com.qyy.jyshop.pojo.AjaxResult;

@Controller
@RequestMapping("/admin/financialMgr/passwd")
public class FinancialPasswdController extends BaseController {

    @Autowired
    private FinancialPasswdService financialPasswdService;

    @Authority(opCode = "090301", opName = "财务密码页面")
    @RequestMapping("passwdMain")
    public String passwdMain(@RequestParam(required = false, defaultValue = "%2Fadmin%2FhomePage") String path,
            HttpServletRequest request) {

        String url = URLDecoder.decode(path);
        request.setAttribute("url", url);
        Boolean isExpire = false;
        if (url.equals("/admin/homePage")) {
            // 检查密码是否过期
            isExpire = this.financialPasswdService.checkExpire(request); // 返回True表示没有过期，返回false
        }
        request.setAttribute("isExpire", isExpire);
        return "financial/passwdMgr/passwd_input";
    }

    @RequestMapping("checkFinancialPasswd")
    @ResponseBody
    @Authority(opCode = "09030101", opName = "验证财务密码")
    public AjaxResult checkFinancialPasswd(String password, HttpServletRequest request) {
        return financialPasswdService.checkFinancialPasswd(password, request);
    }

}
