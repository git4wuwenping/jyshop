package com.qyy.jyshop.admin.point.controller;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qyy.jyshop.admin.common.annotation.Authority;
import com.qyy.jyshop.admin.common.annotation.ControllerLog;
import com.qyy.jyshop.admin.common.controller.BaseController;
import com.qyy.jyshop.admin.point.service.PointParamService;
import com.qyy.jyshop.model.PointParam;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.AppUtil;

@Controller
@RequestMapping("/admin/pointParam")
public class PointParamController extends BaseController {

	@Autowired
	private PointParamService pointParamService;

	/**
	 * 红积分列表
	 * 
	 * @return
	 */
	@Authority(opCode = "080201", opName = "积分设置")
	@RequestMapping("pointParamMain")
	public String pointParamMain(Map<String, Object> map) {
		map.put("pointParam", pointParamService.getPointParam());
		return "point/pointParam/pointParam_main";
	}

	@ControllerLog("修改积分配置")
	@RequestMapping("editPointParam")
	@ResponseBody
	@Authority(opCode = "08020101", opName = "修改积分配置")
	public AjaxResult editPointParam(PointParam pointParam,String yellowConvertBeginStr,String yellowConvertEndStr) {
		try {
			if(yellowConvertBeginStr.length() == 16) {
				pointParam.setYellowConvertBegin(Timestamp.valueOf(yellowConvertBeginStr + ":00"));
			}else {
				pointParam.setYellowConvertBegin(Timestamp.valueOf(yellowConvertBeginStr));
			}
			if(yellowConvertBeginStr.length() == 16) {
				pointParam.setYellowConvertEnd(Timestamp.valueOf(yellowConvertBeginStr + ":00"));
			}else {
				pointParam.setYellowConvertEnd(Timestamp.valueOf(yellowConvertBeginStr));
			}
			
			return AppUtil.returnObj(pointParamService.editPointParam(pointParam));
		} catch (RuntimeException e) {
			this.logger.error("修改积分配置失败", e);
			return AppUtil.returnObj(2, e.getMessage());
		}
	}

}
