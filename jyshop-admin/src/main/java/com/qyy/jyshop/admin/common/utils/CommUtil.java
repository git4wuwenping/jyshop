/*
 * Copyright (c) 2005, 2018, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.admin.common.utils;

import javax.servlet.http.HttpServletRequest;

import com.qyy.jyshop.model.Goods;
import com.qyy.jyshop.pojo.PageAjax;

/**
 * 描述
 * 
 * @author jiangbin
 * @created 2018年3月27日 下午2:47:58
 */
public class CommUtil {
    public static String getURL(HttpServletRequest request) {

        String contextPath = request.getContextPath().equals("/") ? "" : request.getContextPath();

        String url = "http://" + request.getServerName();
        if (null2Int(Integer.valueOf(request.getServerPort())) != 80)
            url = url + ":" + null2Int(Integer.valueOf(request.getServerPort())) + contextPath;
        else {
            url = url + contextPath;
        }
        return url;

    }

    public static int null2Int(Object s) {
        int v = 0;
        if (s != null)
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception localException) {
            }
        return v;
    }

    public static String getPageAjaxDom(String url, String staticURL, String params, PageAjax<Goods> pageGoods) {
        StringBuffer resultDom = new StringBuffer();
        if (pageGoods.getRows() != null && pageGoods.getRows().size() > 0) {
            resultDom.append("<ul class='floor_sear_pro'>");
            for (Goods goods : pageGoods.getRows()) {
                resultDom
                        .append("<li>")
                        .append("<a href='javascript:void(0);' onclick='goods_list_set(this);' img_uri='"
                                + goods.getImage() + "' goods_name='" + goods.getName() + "' goods_id='"
                                + goods.getGoodsId() + "' class='floor_sear_a'> ")
                        .append("<span class='floor_sear_img'>")
                        .append("<img src='" + goods.getImage() + "' width='70' height='70' />")
                        .append(" </span> <span class='floor_sear_name'>" + goods.getName() + "</span> </a>")
                        .append("</li>");
            }
            resultDom.append("</ul>");
            resultDom.append("<div class='floor_page'><span>");
            resultDom.append(showPageAjaxHtml(url, params, pageGoods.getPageNo(), pageGoods.getPages()));
            resultDom.append("</span>");
        }
        return resultDom.toString();
    }

    public static String showPageAjaxHtml(String url, String params, int currentPage, int pages) {
        String s = "";
        if (pages > 0) {
            String address = url + "?1=1" + params;
            if (currentPage >= 1) {
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" + address + "\",1,this)'>首页</a> ";
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" + address + "\","
                        + (currentPage - 1) + ",this)'>上一页</a> ";
            }

            int beginPage = currentPage - 3 < 1 ? 1 : currentPage - 3;
            if (beginPage <= pages) {
                s = s + "第　";
                int i = beginPage;
                for (int j = 0; (i <= pages) && (j < 6); j++) {
                    if (i == currentPage)
                        s = s + "<a class='this' href='javascript:void(0);' onclick='return ajaxPage(\"" + address
                                + "\"," + i + ",this)'>" + i + "</a> ";
                    else
                        s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" + address + "\"," + i
                                + ",this)'>" + i + "</a> ";
                    i++;
                }

                s = s + "页　";
            }
            if (currentPage <= pages) {
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" + address + "\","
                        + (currentPage + 1) + ",this)'>下一页</a> ";
                s = s + "<a href='javascript:void(0);' onclick='return ajaxPage(\"" + address + "\"," + pages
                        + ",this)'>末页</a> ";
            }
        }

        return s;
    }
}
