package com.hyx.core.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.hyx.core.exception.GlobalException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

/**
 * 二维码编码，解码
 * 
 * @author leige
 *
 */
public class QRCodeUtils {

	private static final Logger log = LoggerFactory.getLogger(QRCodeUtils.class);

	private static final int DEFAULT_HEIGHT = 320;
	private static final int DEFAULT_WIDTH = 320;

	/**
	 * 生成二维码
	 * 
	 * @param content
	 * @return
	 */
	public static String encodeToBase64(String content) {
		return encodeToBase64(content, null, null);
	}

	/**
	 * 生成二维码转换成base64字符串
	 * 
	 * @param content
	 * @param width
	 * @param height
	 * @return
	 */
	public static String encodeToBase64(String content, Integer width, Integer height) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		encode(stream, content, width, height, "jpeg");
		byte[] bytes = stream.toByteArray();
		return Base64Utils.encodeToString(bytes);
	}

	/**
	 * 生成二维码
	 * 
	 * @param stream
	 * @param content
	 * @param width
	 * @param height
	 * @param fileType
	 * @param uid
	 * @return
	 */
	public static void encode(OutputStream stream, String content, Integer width, Integer height, String fileType) {
		Assert.hasText(content, "二维码内容不可以为空");
		if (width == null || width == 0) {
			width = DEFAULT_WIDTH;
		}
		if (height == null || height == 0) {
			height = DEFAULT_HEIGHT;
		}
		Map<EncodeHintType, String> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter.writeToStream(bitMatrix, fileType, stream);
		} catch (IOException | WriterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解码base64的二维码
	 * 
	 * @param base64
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public static String decodeBase64(String base64) {
		byte[] bytes = Base64Utils.decodeFromString(base64);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		return decode(inputStream);
	}

	/**
	 * 解码
	 * 
	 * @param filePath
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public static String decode(InputStream inputStream) {
		try {
			BufferedImage image = ImageIO.read(inputStream);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Hashtable<DecodeHintType, String> hints = new Hashtable<>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			return new MultiFormatReader().decode(bitmap, hints).getText();
		} catch (IOException | NotFoundException e) {
			log.warn("解码二维码失败", e);
			GlobalException.warn("解码二维码失败");
			return null;
		}
	}
}
