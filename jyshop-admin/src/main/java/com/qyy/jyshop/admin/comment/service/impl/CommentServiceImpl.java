package com.qyy.jyshop.admin.comment.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.qyy.jyshop.admin.comment.service.CommentService;
import com.qyy.jyshop.dao.CommentDao;
import com.qyy.jyshop.dao.GiftOrderDao;
import com.qyy.jyshop.dao.GiftOrderItemsDao;
import com.qyy.jyshop.dao.GiftOrderLogDao;
import com.qyy.jyshop.dao.OrderAutocommentConfigDao;
import com.qyy.jyshop.dao.OrderDao;
import com.qyy.jyshop.dao.OrderItemsDao;
import com.qyy.jyshop.dao.OrderLogDao;
import com.qyy.jyshop.exception.AppErrorException;
import com.qyy.jyshop.model.Comment;
import com.qyy.jyshop.model.GiftOrder;
import com.qyy.jyshop.model.GiftOrderItems;
import com.qyy.jyshop.model.GiftOrderLog;
import com.qyy.jyshop.model.Order;
import com.qyy.jyshop.model.OrderAutocommentConfig;
import com.qyy.jyshop.model.OrderItems;
import com.qyy.jyshop.model.OrderLog;
import com.qyy.jyshop.pojo.ExcelData;
import com.qyy.jyshop.pojo.PageAjax;
import com.qyy.jyshop.pojo.ParamData;
import com.qyy.jyshop.supple.AbstratService;
import com.qyy.jyshop.util.DictionaryUtil;
import com.qyy.jyshop.util.EntityReflectUtil;
import com.qyy.jyshop.util.IPUtil;
import com.qyy.jyshop.util.TimestampUtil;
import com.qyy.jyshop.util.excel.ExcelUtil;
import com.qyy.jyshop.util.excel.dto.CommentForExcel;
import com.qyy.jyshop.util.excel.dto.OrderForExcel;

@Service
public class CommentServiceImpl extends AbstratService<Comment> implements CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private GiftOrderDao giftOrderDao;
	@Autowired
    private OrderItemsDao orderItemsDao;
    @Autowired
    private GiftOrderItemsDao giftOrderItemsDao;
	@Autowired
	private OrderLogDao orderLogDao;
	@Autowired
	private GiftOrderLogDao giftOrderLogDao;
	@Autowired
	private OrderAutocommentConfigDao orderAutocommentConfigDao;

	@Override
	public PageAjax<Map<String, Object>> pageComment(PageAjax<Map<String, Object>> page) {
		ParamData params = this.getParamData(page.getPageNo(), page.getPageSize());
		return this.pageQuery("CommentDao.queryCommentByParam", params);
	}

	@Override
	@Transactional
	public String updateCommentStatus(Long id, Short updateStatus) {
		Comment comment = new Comment();
		comment.setCommentId(id);
		comment.setStatus(updateStatus);
		commentDao.updateByPrimaryKeySelective(comment);
		return null;
	}

	@Override
	public Map<String,Object> queryCommentById(Long id) {
		 Map<String,Object> returnMap = new HashMap<String, Object>();
		List<Map<String,Object>> list = commentDao.queryCommentById(id);
		if(CollectionUtils.isNotEmpty(list)){
			returnMap = list.get(0);
		}
		return returnMap;
	}

	@Override
	@Transactional
	public String delCommentById(Long id) {
		this.delById(id);
		return null;
	}

    @Override
    @Transactional
    public void autoOrderComment(Order order, BigDecimal dayAutoComment) {
        //查询随机一条自动好评内容
        OrderAutocommentConfig randomComment = orderAutocommentConfigDao.selectRandomOne();
        String randomContent = randomComment == null ? "":randomComment.getContent();
        //新增订单评价
        List<OrderItems> orderItemsList = orderItemsDao.selectByOrderId(order.getOrderId());
        for(OrderItems orderItems : orderItemsList){
            // 添加评价表记录
            Comment comment = new Comment();
            comment.setGoodsId(orderItems.getGoodsId());
            comment.setOrderId(orderItems.getOrderId());
            comment.setMemberId(order.getMemberId());
            comment.setCommentTime(TimestampUtil.getNowTime());
//            comment.setIp(IPUtil.getIpAdd());
            comment.setItemId(orderItems.getItemId());
            comment.setContent(randomContent);
            comment.setScore(0);//TODO  0 - 好评
            comment.setStatus((short) 1);//TODO 设置评价显示
            comment.setAutoFlag((short) 1);//TODO 设置评价为自动评价
            comment.setAnonFlag((short) 0); //TODO 设置评价为不匿名
            commentDao.insert(comment);
        }
        //修改订单状态
        Order updateOrder=new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")));
        updateOrder.setEvaluateStatus(1); //TODO 设置为已评价
        this.orderDao.updateByPrimaryKeySelective(updateOrder);
        
        //添加订单日志
        OrderLog orderLog=new OrderLog();
        orderLog.setOrderId(order.getOrderId());
        orderLog.setOrderSn(order.getOrderSn());
        orderLog.setMessage("系统自动对订单"+order.getOrderSn()+"进行了【收货订单"+dayAutoComment+"天后自动评价，默认为好评，随机上传某条好评内容】操作");
        orderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        orderLog.setCreationTime(TimestampUtil.getNowTime());
        this.orderLogDao.insertSelective(orderLog);
    }
    
    @Override
    @Transactional
    public void autoGiftOrderComment(GiftOrder giftOrder, BigDecimal dayAutoComment) {
        //查询随机一条自动好评内容
        OrderAutocommentConfig randomComment = orderAutocommentConfigDao.selectRandomOne();
        String randomContent = randomComment == null ? "" : randomComment.getContent();
        //新增订单评价
        List<GiftOrderItems> giftOrderItemsList = giftOrderItemsDao.selectByOrderId(giftOrder.getOrderId());
        for(GiftOrderItems giftOrderItems : giftOrderItemsList){
            // 添加评价表记录
            Comment comment = new Comment();
            comment.setGoodsId(giftOrderItems.getGoodsId());
            comment.setOrderId(giftOrderItems.getOrderId());
            comment.setMemberId(giftOrder.getMemberId());
            comment.setCommentTime(TimestampUtil.getNowTime());
//            comment.setIp(IPUtil.getIpAdd());
            comment.setItemId(giftOrderItems.getItemId());
            comment.setContent(randomContent);
            comment.setScore(0);//TODO  0 - 好评
            comment.setStatus((short) 1);//TODO 设置评价显示
            comment.setAutoFlag((short) 1);//TODO 设置评价为自动评价
            comment.setAnonFlag((short) 0); //TODO 设置评价为不匿名
            commentDao.insert(comment);
        }
      //修改订单状态
        GiftOrder updateGiftOrder=new GiftOrder();
        updateGiftOrder.setOrderId(giftOrder.getOrderId());
        updateGiftOrder.setOrderStatus(Integer.valueOf(DictionaryUtil.getDataValueByName("order_status", "order_complete")));
        updateGiftOrder.setEvaluateStatus(1); //TODO 设置为已评价
        this.giftOrderDao.updateByPrimaryKeySelective(updateGiftOrder);
        //添加日志
        GiftOrderLog giftOrderLog=new GiftOrderLog();
        giftOrderLog.setOrderId(giftOrder.getOrderId());
        giftOrderLog.setOrderSn(giftOrder.getOrderSn());
        giftOrderLog.setMessage("系统自动对订单"+giftOrder.getOrderSn()+"进行了【收货订单"+dayAutoComment+"天后自动评价，默认为好评，随机上传某条好评内容】操作");
        giftOrderLog.setUserType(Integer.valueOf(DictionaryUtil.getDataValueByName("user_type", "system")));
        giftOrderLog.setCreationTime(TimestampUtil.getNowTime());
        this.giftOrderLogDao.insertSelective(giftOrderLog);
        
    }

	@Override
	public ExcelData getExportData(Map<String, Object> map, HttpServletResponse response) throws Exception {
		List<Map<String,Object>> commentList = commentDao.queryCommentByParam(map);;//必须按合并列排序
        
        List<CommentForExcel> list = new ArrayList<CommentForExcel>();
        for(Map data:commentList) {
            //OrderForExcel orderForExcel = new OrderForExcel();
            //BeanUtils.copyProperties(data2,data);
            //BeanUtils.copyProperties(data,orderForExcel);
        	String score = null;
        	if((int)data.get("score")==0){
        		score="好评";
        	}else if((int)data.get("score")==1){
        		score="中评";
        	}else{
        		score="差评";
        	}
        	data.put("score",score);
        	CommentForExcel commentForExcel = EntityReflectUtil.toBean(CommentForExcel.class, data);
            list.add(commentForExcel);
        }
        
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("title", "评价信息表");
        String xlsType= "评价信息";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
        ExcelUtil.getInstance().exportObj2ExcelByTemplate(response,datas, "template.xls", null,
                list, CommentForExcel.class, true,2,xlsType);
		return null;
	}

}
