package com.qyy.jyshop.platform.util;

public class FileUtils {

    /**
     * 是否是允许上传的图片
     * @param imgFileName
     * @return
     */
    public static boolean isAllowUpImg(String imgFileName){
        imgFileName = imgFileName.toLowerCase();
        String allowTYpe = "gif,jpg,bmp,png,jpeg,swf";
        if (!imgFileName.trim().equals("") && imgFileName.length() > 0) {
            String ex = imgFileName.substring(imgFileName.lastIndexOf(".") + 1, imgFileName.length());
            return allowTYpe.toUpperCase().indexOf(ex.toUpperCase()) >= 0;
        } else {
            return false;
        }
    }

    /**
     * 得到文件的扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName) {
        int potPos = fileName.lastIndexOf('.') + 1;
        String type = fileName.substring(potPos, fileName.length());
        return type;
    }

}
