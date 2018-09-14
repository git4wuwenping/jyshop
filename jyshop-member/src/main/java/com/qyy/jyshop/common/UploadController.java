/*
 * Copyright (c) 2005, 2017, CHEERTEA Technology Co.,Ltd. All rights reserved.
 * CHEERTEA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.qyy.jyshop.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qyy.jyshop.controller.AppBaseController;
import com.qyy.jyshop.pojo.AjaxResult;
import com.qyy.jyshop.util.JsonConvertUtil;
import com.qyy.jyshop.util.UploadUtils;


/**
 * 图片上传
 * @author hwc
 * @created 2017年11月27日 下午2:25:21
 */
@Controller
public class UploadController extends AppBaseController {

    //访问地址
    @Value("${upload.path}")
    private String path;
    //存放地址
    @Value("${upload.docBase}")
    private String docBase;
    @Autowired
    private UploadUtils uploadUtils;

    @RequestMapping(value = "jqUploadImg")
    @ResponseBody
    public String jqUploadImg(HttpServletRequest request, HttpServletResponse response) {
        //必须加上这句，否则插件不调用done回调
        response.setContentType("text/html");
        String filepath = uploadUtils.upload(request, docBase, path);
        return filepath;
    }
    
    @PostMapping(value = "/anon/base64UploadImg")
    @ResponseBody
    public AjaxResult base64UploadImg(HttpServletRequest request) {
        String filePath = uploadUtils.createBase64Img(request,"data", docBase, path);
        Map<String,Object> returnMap = new HashMap<String, Object>();
        if(StringUtils.isEmpty(filePath)){
            return new AjaxResult(1, "上传失败", null);
        }
        returnMap.put("filePath", filePath);
        return new AjaxResult(0, "上传成功", returnMap);
    }
    
    
    @PostMapping(value = "/anon/batchUploadBASE64Image")
    @ResponseBody
    public String batchUploadBASE64Image(@RequestBody List<String> strList) {
        List<String> returnList = new ArrayList<String>();
        for(String str : strList){
            String filePath = uploadUtils.createBase64Img2(str, docBase, path);
            returnList.add(filePath);
        }
        
        return StringUtils.join(returnList.toArray(), ",");
    }

    /**
     * 上传图片
     * @author hwc
     * @created 2017年11月27日 下午2:37:15
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadImg.do")
    @ResponseBody
    public AjaxResult uploadImg(HttpServletRequest request) {
        final Map<String, Object> result = new HashMap<String, Object>();
        String filepath = uploadUtils.upload(request, docBase, path);
        result.put("filepath", filepath);
        return new AjaxResult(1, "上传成功", result);
    }


    /**
     * 上传文件
     * @author hwc
     * @created 2017年11月27日 下午2:21:36
     * @param files
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadFile.do")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile[] files, HttpServletRequest request) {
        final Map<String, Object> result = new HashMap<String, Object>();
        // 判断file数组不能为空并且长度大于0
        if (files != null && files.length > 0) {
            // 循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // 判断文件是否为空
                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    // 文件后缀
                    String suffixes = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    // 重命名文件
                    String nowName = uploadUtils.getNewName(suffixes);

                    final ByteArrayOutputStream fileStream = new ByteArrayOutputStream();
                    // 存放位置
                    String savepath = uploadUtils.getFilePath(docBase, nowName);
                    try {
                        // 复制文件内容
                        IOUtils.copy(file.getInputStream(), fileStream);
                        // 复制文件
                        FileUtils.copyInputStreamToFile(new ByteArrayInputStream(fileStream.toByteArray()), new File(savepath));
                    } catch (Exception e) {
                        return new AjaxResult(0, "上传失败", null);
                    }
                    result.put("nowname", nowName);
                    result.put("filetype", suffixes);
                    result.put("filesize", file.getSize());
                    result.put("savepath", savepath);
                    result.put("url", uploadUtils.getFilePath(path, nowName));
                } else {
                    return new AjaxResult(0, "请选择文件上传", result);
                }
            }
        }
        return new AjaxResult(1, "上传成功", result);
    }


    /**
     * 百度编辑器加载配置表/上传图片
     * @author hwc
     * @created 2017年11月27日 下午2:48:49
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("ueditoUpload")
    public void ueditoUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html");
        String action = request.getParameter("action");
        String jsonStr = null;
        if ("config".equals(action)) {
            InputStream jsonInStream = JsonConvertUtil.class.getClassLoader().getResourceAsStream("config.json");
            jsonStr = JsonConvertUtil.convertStream2Json(jsonInStream);
        } else if ("uploadimage".equals(action)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("state", "SUCCESS");
            String url = uploadUtils.upload(request, docBase, path);
            map.put("url", url);
            map.put("title", "");
            map.put("original", "");
            jsonStr = new ObjectMapper().writeValueAsString(map);
        }
        response.getWriter().write(jsonStr);
    }
    
   
}
