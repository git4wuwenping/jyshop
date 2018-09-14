package com.qyy.jyshop.comment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qyy.jyshop.comment.service.CommentService;
import com.qyy.jyshop.controller.AppBaseController;

/**
 * 评价接口 描述
 * 
 * @author jiangbin
 * @created 2018年3月13日 上午9:07:26
 */
@RestController
public class CommentController extends AppBaseController {

    @Autowired
    private CommentService commentService;

    /**
     * 订单评价初始化数据
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午9:16:14
     * @param token
     * @param orderSn
     * @return
     */
    @RequestMapping(value = "initOrderComment", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String, Object> initOrderComment(String token, String orderSn) {
        Map<String, Object> map = this.commentService.initOrderComment(token, orderSn);
        if (map.get("orderItemsList") == null) {
            return this.outMessage(1, "获取订单错误", null);
        }
        return this.outMessage(0, "获取成功", map);
    }

    /**
     * 订单评价
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午9:33:45
     * @param token
     * @param orderSn
     * @param commentData
     * @return
     */
    @RequestMapping(value = "doOrderComment", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String, Object> doOrderComment(String token, String orderSn, String commentData, Integer anonFlag) {
        return this.outMessage(0, "操作成功", this.commentService.doOrderComment(token, orderSn, commentData, anonFlag));
    }
    
    /**
     * 订单评价
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午9:33:45
     * @param token
     * @param orderSn
     * @param commentData
     * @return
     */
    @RequestMapping(value = "doOrderCommentAndroid", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String, Object> doOrderCommentAndroid(String token, String orderSn, String commentData, Integer anonFlag) {
        return this.outMessage(0, "操作成功", this.commentService.doOrderCommentAndroid(token, orderSn, commentData, anonFlag));
    }

    /**
     * 查看订单评价
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午10:02:50
     * @param token
     * @param orderSn
     * @return
     */
    @RequestMapping(value = "queryOrderComment", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String, Object> queryOrderComment(String token, String orderSn) {
        Map<String, Object> map = this.commentService.queryOrderComment(token, orderSn);
        if (map.get("commentList") == null) {
            return this.outMessage(1, "获取订单评价错误", null);
        }
        return this.outMessage(0, "操作成功", map);
    }
    
    /**
     * 查看商品评价列表
     * @author jiangbin
     * @created 2018年3月13日 上午10:22:33
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "queryGoodsComment", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String,Object> queryGoodsComment(Integer goodsId){
        Map<String,Object> returnMap = this.commentService.queryGoodsComment(goodsId);
        if (returnMap.get("commentList") == null) {
            return this.outMessage(1, "获取评价错误", null);
        }
        return this.outMessage(0, "操作成功", returnMap);
    }
}
