package com.qyy.jyshop.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.util.ResourceUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;

public class LogoQRCode {
	/**
     * 生成带logo的二维码图片
	 * @param docBase 
	 * @param logoPath 
     *
     * @param qrPic
     * @param logoPic
	 * @throws FileNotFoundException 
     */
    public static String getLogoQRCode(String qrUrl,String productName, String docBase, String logoPath) throws FileNotFoundException
    {
        //String filePath = (javax.servlet.http.HttpServletRequest)request.getSession().getServletContext().getRealPath("/") + "resources/images/logoImages/llhlogo.png";
        //filePath是二维码logo的路径，但是实际中我们是放在项目的某个路径下面的，所以路径用上面的，把下面的注释就好
        //File file = ResourceUtils.getFile(logoPath);
        File file = new File(logoPath);
        String content = qrUrl;
        try
        {  
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 400, 400, zp.getDecodeHintType());
            return zp.addLogo_QRCode(bim, file, new LogoConfig(), productName,docBase);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    


    /**
     * 构建初始化二维码
     *
     * @param bm
     * @return
     */
    public BufferedImage fileToBufferedImage(BitMatrix bm)
    {
        BufferedImage image = null;
        try
        {
            int w = bm.getWidth(), h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                {
                    image.setRGB(x, y, bm.get(x, y) ? 0xFF000000 : 0xFFCCDDEE);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return image;
    }

   
}
