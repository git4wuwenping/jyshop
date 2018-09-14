package com.qyy.jyshop.comment.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.qyy.jyshop.comment.feign.UploadFeign;
import com.qyy.jyshop.comment.service.CommentService;
import com.qyy.jyshop.dao.CommentDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Comment;
import com.qyy.jyshop.model.Member;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.IPUtil;
import com.qyy.jyshop.util.TimestampUtil;

@Service
public class CommentServiceImpl extends AbstratService<Comment> implements CommentService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemsDao orderItemsDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UploadFeign uploadFeign;

    /**
     * 订单评价初始化数据
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午9:16:30
     * @param token
     * @param orderSn
     * @return
     * @see com.qyy.jyshop.comment.service.CommentService#initOrderComment(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Map<String, Object> initOrderComment(String token, String orderSn) {
        Map<String, Object> reurnMap = new HashMap<String, Object>();
        Member member = this.getMember(token);
        Order o = new Order();
        o.setOrderSn(orderSn);
        Order order = orderDao.selectOne(o);
        
        if (member == null) {
            throw new AppErrorException("请先登录");
        }
        if (order == null) {
            throw new AppErrorException("此订单不存在");
        }
        if (!order.getOrderStatus().toString().equals(DictionaryUtil.getDataValueByName("order_status", "order_rog"))) {
            throw new AppErrorException("订单状态错误");
        }
        if (!order.getMemberId().equals(member.getMemberId())) {
            throw new AppErrorException("您没有操作权限");
        }
        // 货物列表
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderSn(orderSn);
        List<OrderItems> orderItemsList = orderItemsDao.select(orderItems);

        reurnMap.put("orderItemsList", orderItemsList);
        return reurnMap;
    }

    /**
     * 订单评价
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午9:33:39
     * @param token
     * @param orderSn
     * @param commentData
     * @return
     * @see com.qyy.jyshop.comment.service.CommentService#doOrderComment(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public Map<String, Object> doOrderComment(String token, String orderSn, String commentData,Integer anonFlag) {
        Member member = this.getMember(token);
        Order order = new Order();
        order.setOrderSn(orderSn);
        Order o = orderDao.selectOne(order);
        if (o == null) {
            throw new AppErrorException("订单不存在");
        }
        if (commentData != null && !commentData.equals("[]")) {//
            JSONArray jsonArray = JSONArray.fromObject(commentData);
            if (jsonArray != null && jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Long itemId = Long.parseLong(jsonObject.get("itemId").toString());
                    Comment mc = new Comment();
                    mc.setItemId(itemId);
                    List list = commentDao.select(mc);
                    if (CollectionUtils.isEmpty(list)) {
                        OrderItems orderItems = orderItemsDao.selectByPrimaryKey(itemId);
                        // 添加评价表记录
                        Comment comment = new Comment();
                        comment.setGoodsId(orderItems.getGoodsId());
                        comment.setOrderId(orderItems.getOrderId());
                        comment.setMemberId(member.getMemberId());
                        comment.setCommentTime(TimestampUtil.getNowTime());
                        comment.setIp(IPUtil.getIpAdd());
                        comment.setItemId(itemId);
                        comment.setContent(jsonObject.get("content").toString());
                        if (jsonObject.get("score") == null || StringUtils.isBlank(jsonObject.get("score").toString())) {
                            comment.setScore(0);//TODO
                        } else {
                            comment.setScore(Integer.parseInt(jsonObject.get("score").toString()));
                        }
                        comment.setStatus((short) 0);//TODO 评价是否显示
                        comment.setAutoFlag((short)0);//TODO 评价是否为自动评价
                        comment.setAnonFlag(anonFlag.shortValue()); //评价是否匿名
                        comment.setImage(jsonObject.get("image").toString());
                        commentDao.insert(comment);
                        // 更改订单货物表为已评价
                        orderItems.setEvaluateStatus(1);//TODO
                        orderItemsDao.updateByPrimaryKeySelective(orderItems);
                    } else {
                        throw new AppErrorException("订单已评价");
                    }
                }
                o.setOrderStatus(Integer.parseInt(DictionaryUtil.getDataValueByName("order_status", "order_complete"))); // 修改订单状态为已完成 //TODO
                o.setEvaluateStatus(1); // 修改订单评价状态为已评价
                orderDao.updateByPrimaryKeySelective(o);
            }
        } else {
            throw new AppErrorException("请求数据有误");
        }
        return null;
    }
    
    /**
     * 订单评价 android
     * 
     * @author jiangbin
     * @created 2018年3月13日 上午9:33:39
     * @param token
     * @param orderSn
     * @param commentData
     * @return
     * @see com.qyy.jyshop.comment.service.CommentService#doOrderComment(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public Map<String, Object> doOrderCommentAndroid(String token, String orderSn, String commentData,Integer anonFlag) {
        Member member = this.getMember(token);
        Order order = new Order();
        order.setOrderSn(orderSn);
        Order o = orderDao.selectOne(order);
        if (o == null) {
            throw new AppErrorException("订单不存在");
        }
        if (commentData != null && !commentData.equals("{}")) {//
            Map parseObject = JSON.parseObject(commentData, Map.class);
            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(parseObject.get("dataList").toString());
            if (jsonArray != null && jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Long itemId = Long.parseLong(jsonObject.get("itemId").toString());
                    Comment mc = new Comment();
                    mc.setItemId(itemId);
                    List list = commentDao.select(mc);
                    if (CollectionUtils.isEmpty(list)) {
                        OrderItems orderItems = orderItemsDao.selectByPrimaryKey(itemId);
                        // 添加评价表记录
                        Comment comment = new Comment();
                        comment.setGoodsId(orderItems.getGoodsId());
                        comment.setOrderId(orderItems.getOrderId());
                        comment.setMemberId(member.getMemberId());
                        comment.setCommentTime(TimestampUtil.getNowTime());
                        comment.setIp(IPUtil.getIpAdd());
                        comment.setItemId(itemId);
                        
                        if (jsonObject.get("content") == null) {
                            comment.setContent(null);//TODO
                        } else {
                            comment.setContent(jsonObject.get("content").toString());
                        }
                        if (jsonObject.get("score") == null || StringUtils.isBlank(jsonObject.get("score").toString())) {
                            comment.setScore(0);//TODO
                        } else {
                            comment.setScore(Integer.parseInt(jsonObject.get("score").toString()));
                        }
                        comment.setStatus((short) 0);//TODO 评价是否显示
                        comment.setAutoFlag((short)0);//TODO 评价是否为自动评价
                        comment.setAnonFlag(anonFlag.shortValue()); //评价是否匿名
                        List<String> imageList = (List<String>) jsonObject.get("imageList");
                        if(imageList.size() > 0){
                            String imagePath = uploadFeign.uploadBASE64Image(imageList);
                            comment.setImage(imagePath);
                        }
                        commentDao.insert(comment);
                        // 更改订单货物表为已评价
                        orderItems.setEvaluateStatus(1);//TODO
                        orderItemsDao.updateByPrimaryKeySelective(orderItems);
                    } else {
                        throw new AppErrorException("订单已评价");
                    }
                }
                o.setOrderStatus(Integer.parseInt(DictionaryUtil.getDataValueByName("order_status", "order_complete"))); // 修改订单状态为已完成 //TODO
                o.setEvaluateStatus(1); // 修改订单评价状态为已评价
                orderDao.updateByPrimaryKeySelective(o);
            }
        } else {
            throw new AppErrorException("请求数据有误");
        }
        return null;
    }

    /**
     * 查看订单评价
     * @author jiangbin
     * @created 2018年3月13日 上午10:14:58
     * @param token
     * @param orderSn
     * @return
     * @see com.qyy.jyshop.comment.service.CommentService#queryOrderComment(java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, Object> queryOrderComment(String token, String orderSn) {
        Order order = new Order();
        order.setOrderSn(orderSn);
        Order o = orderDao.selectOne(order);
        if(o == null){
            throw new AppErrorException("订单不存在");
        }
        if(!o.getEvaluateStatus().equals(1)){
            throw new AppErrorException("订单还未评价");
        }
        Member member=this.getMember(token);
        if(!o.getMemberId().equals(member.getMemberId())){
            throw new AppErrorException("您没有操作权限");
        }
        List<Map> commentList = commentDao.queryOrderComment(orderSn);
        Map<String,Object> returnMap = new HashMap<String, Object>();
        returnMap.put("commentList", commentList);
        return returnMap;
    }

    @Override
    public Map<String, Object> queryGoodsComment(Integer goodsId) {
        List<Map> commentList = commentDao.queryGoodsComment(goodsId);
        Map<String,Object> returnMap = new HashMap<String, Object>();
        returnMap.put("commentList", commentList);
        return returnMap;
    }

}
