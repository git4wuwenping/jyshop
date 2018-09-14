package com.qyy.jyshop.admin.comment.service;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;

public interface CommentService {

    PageAjax<Map<String, Object>> pageComment(PageAjax<Map<String, Object>> page);

    String updateCommentStatus(Long id, Short updateStatus);

    Map<String, Object> queryCommentById(Long id);

    String delCommentById(Long id);

    /**
     * 已收货订单若干天后自动评价，默认为好评，随机上传某条好评内容
     * @author jiangbin
     * @created 2018年3月22日 上午10:57:04
     * @param order
     * @param dayAutoComment 
     */
    void autoOrderComment(Order order, BigDecimal dayAutoComment);

    /**
     * 已收货订单若干天后自动评价，默认为好评，随机上传某条好评内容
     * @author jiangbin
     * @created 2018年3月22日 上午10:57:15
     * @param giftOrder
     */
    void autoGiftOrderComment(GiftOrder giftOrder, BigDecimal dayAutoComment);
    /**
     * 导出excel
     * @author Tonny
     * @date 2018年4月16日
     */
	ExcelData getExportData(Map<String, Object> map, HttpServletResponse response) throws Exception;

}
