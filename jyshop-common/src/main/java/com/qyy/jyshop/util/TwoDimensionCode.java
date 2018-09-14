package com.qyy.jyshop.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.util.ResourceUtils;

import com.swetake.util.Qrcode;

public class TwoDimensionCode {

	public void encoderQRCode(String content,String icon,String imgType, String imagePath) {
		BufferedImage bufImg = null;
		Integer width=140;
		Integer height=140;
		//icon = "https://himg.bdimg.com/sys/portrait/item/c6e8c4c7bad5b6fbc9e3d3b08d5e.jpg";
		try {
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('M');
			qrcodeHandler.setQrcodeEncodeMode('B');
			qrcodeHandler.setQrcodeVersion(7);

			byte[] contentBytes = content.getBytes("utf-8");
			
			if(contentBytes.length>=120){
			    qrcodeHandler.setQrcodeVersion(20);
			    width=300;
		        height=300;
			}
			bufImg = new BufferedImage(width,height,
                    BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, width, height);

			gs.setColor(Color.BLACK);
			int pixoff = 2;
			if (contentBytes.length > 0 && contentBytes.length < 300) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				throw new Exception("QRCode content bytes length = "
						+ contentBytes.length + " not in [0, 300].");
			}
			//File file = ResourceUtils.getFile(imagePath);
			//System.out.println(file.exists());
			
			//Image img = ImageIO.read(file);//实例化一个Image对象。
	        //gs.drawImage(img, 60, 60, 60, 60, null); 
			//gs.dispose();
			//bufImg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File imgFile = new File(imagePath);
			System.out.println(imgFile);
			ImageIO.write(bufImg, imgType, imgFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}