package com.qyy.jyshop.comment.service;

import java.util.Map;

public interface CommentService {

    Map<String, Object> initOrderComment(String token, String orderSn);

    Map<String, Object> doOrderComment(String token, String orderSn, String commentData, Integer anonFlag);

    Map<String, Object> queryOrderComment(String token, String orderSn);

    Map<String, Object> queryGoodsComment(Integer goodsId);

    Map<String, Object> doOrderCommentAndroid(String token, String orderSn, String commentData, Integer anonFlag);

}
