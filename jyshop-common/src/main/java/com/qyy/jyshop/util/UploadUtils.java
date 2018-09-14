package com.qyy.jyshop.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component
public class UploadUtils {

    public static String uploadLocal(MultipartFile file, String subFolder) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件的扩展名
        String ext = FileUtils.getFileExt(fileName);
        if(!FileUtils.isAllowUpImg(fileName)){
            throw new IllegalArgumentException("不被允许的上传文件类型");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String path = request.getSession().getServletContext().getRealPath( "static/upload");
        System.out.println(path);
        if(StringUtils.isNotBlank(subFolder)){
            path += File.separator + subFolder + File.separator;
        }
        if(!new File(path).exists())   {
            new File(path).mkdirs();
        }
        fileName = DateUtil.dateToDateFullString(new Date()) + CommonUtils.getRandStr(4) + "." + ext;
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String filePath = "http://"+ IPUtil.getServerIp() + ":" + request.getLocalPort() + request.getContextPath() + "/static/upload/";
        if(StringUtils.isNotBlank(subFolder)){
            filePath =  filePath + subFolder + "/";
        }
        filePath += fileName;
        return filePath;
    }
    
    
    /**
     * 上传
     * @param request
     * @return
     * @throws Exception
     */
    public String upload(HttpServletRequest request, String docBase, String path) {
        String filepath = "";
         //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    // 重命名上传后的文件名
                    String nowName = getNewName(".jpg");
                    final ByteArrayOutputStream srcImageData = new ByteArrayOutputStream();
                    try {
                        IOUtils.copy(file.getInputStream(), srcImageData);
                        // 上传图片
                        org.apache.commons.io.FileUtils.copyInputStreamToFile(new ByteArrayInputStream(srcImageData.toByteArray()), new File(getFilePath(docBase, nowName)));
                    } catch (IOException e) {
                        return filepath;
                    }
                    filepath = getFilePath(path, nowName);
                }
            }
        }
        return filepath;
    }

    /**
     * 获取文件绝对路径
     * @param request
     * @param host
     * @return
     */
    public static String getFileUrl(HttpServletRequest request, String host) {
        String path = request.getScheme() + "://" + host + request.getContextPath() + "/";
        int port = request.getServerPort();
        if (80 != port) {
            path = request.getScheme() + "://" + host + ":" + request.getServerPort() + request.getContextPath() + "/";
        }
        return path;
    }

    /**
     * 文件路径
     * @param type
     * @return
     */
    public String getFilePath(String filepath, String filename) {
        StringBuilder sb = new StringBuilder(filepath).append(DateUtil.getCurDate("yyyyMMdd")).append("/").append(filename);
        return sb.toString();
    }
    
    /**
     * 文件重命名
     * @param suffixes
     * @return
     */
    public String getNewName(String suffixes){
        return UUID.randomUUID().toString() + suffixes;
    }

    /**
     * 删除文件
     * @param path
     */
    public static void delFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
    
    
    
    /**
     * 解析前台上传的base64图片并保存到服务器静态路径
     * 
     * @param para
     * @param subFolder
     * @return
     */
    public String createBase64Img(HttpServletRequest request, String para, String docBase, String path) {
        //upload.path=http://192.168.2.250:8082/static/upload/img/
        //upload.docBase=/mnt/img_tomcat8/webapps/static/upload/img/
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // new一个文件对象用来保存图片，默认保存当前工程根目录
        String resultStr = request.getParameter("data").toString();// base64编码后的字符串
        if (!resultStr.isEmpty()) {
            String nowName = getNewName(".png");
            String filepath = getFilePath(path, nowName);
            File imageFile = new File(new StringBuilder(docBase).append(DateUtil.getCurDate("yyyyMMdd")).append("/").append(nowName).toString());
            File file =  new File(new StringBuilder(docBase).append(DateUtil.getCurDate("yyyyMMdd")).toString());
            if (!file.exists()) {
                file.mkdir();
            }
            resultStr = resultStr.substring(resultStr.indexOf(",") + 1);// 需要去掉头部信息，这很重要
            // 创建输出流
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(imageFile);
                // 获得一个图片文件流
                BASE64Decoder base64Decoder = new BASE64Decoder();
                byte[] result = null;
                result = base64Decoder.decodeBuffer(resultStr);
                for (int i = 0; i < result.length; ++i) {
                    if (result[i] < 0) {// 调整异常数据
                        result[i] += 256;
                    }
                }
                outputStream.write(result);
                outputStream.flush();
                outputStream.close();
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filepath;
        }
        return "";
    }
    
    
    /**
     * 解析前台上传的base64图片并保存到服务器静态路径
     * 
     * @param para
     * @param subFolder
     * @return
     */
    public String createBase64Img2(String resultStr, String docBase, String path) {
        //upload.path=http://192.168.2.250:8082/static/upload/img/
        //upload.docBase=/mnt/img_tomcat8/webapps/static/upload/img/
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // new一个文件对象用来保存图片，默认保存当前工程根目录
        if (!resultStr.isEmpty()) {
            String nowName = getNewName(".png");
            String filepath = getFilePath(path, nowName);
            File imageFile = new File(new StringBuilder(docBase).append(DateUtil.getCurDate("yyyyMMdd")).append("/").append(nowName).toString());
            File file =  new File(new StringBuilder(docBase).append(DateUtil.getCurDate("yyyyMMdd")).toString());
            if (!file.exists()) {
                file.mkdir();
            }
            resultStr = resultStr.substring(resultStr.indexOf(",") + 1);// 需要去掉头部信息，这很重要
            // 创建输出流
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(imageFile);
                // 获得一个图片文件流
                BASE64Decoder base64Decoder = new BASE64Decoder();
                byte[] result = null;
                result = base64Decoder.decodeBuffer(resultStr);
                for (int i = 0; i < result.length; ++i) {
                    if (result[i] < 0) {// 调整异常数据
                        result[i] += 256;
                    }
                }
                outputStream.write(result);
                outputStream.flush();
                outputStream.close();
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filepath;
        }
        return "";
    }

}
