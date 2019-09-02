package com.example.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

public class Application {

	public static void main(String[] args) throws TesseractException, IOException {
		
		File imageFile = new File("src/main/resources/eurotext.tif");  
		// 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分  
		BufferedImage textImage = ImageHelper.convertImageToGrayscale(ImageIO.read(imageFile));  
		// 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率  
		// textImage = ImageHelper.convertImageToBinary(textImage);  
		// 图片放大5倍,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)  
		textImage = ImageHelper.getScaledInstance(textImage, textImage.getWidth() * 1, textImage.getHeight() * 1);  
		Tesseract instance = new Tesseract();  // JNA Interface Mapping  
		instance.setDatapath("src/main/resources/");
		String result = instance.doOCR(imageFile);  
		System.err.println(result);
	}
}
