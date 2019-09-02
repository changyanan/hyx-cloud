package com.hyx.core.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hyx.core.exception.GlobalException;

public abstract class AesUtlis {

	private static Logger logger = LoggerFactory.getLogger(AesUtlis.class);

	/**
	 * 算法
	 */
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	/**
	 * AES加密
	 * 
	 * @param content待加密的内容
	 * @param secretKey加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception 解密失败将抛出解密失败的异常
	 */
	public static byte[] encrypt(String content, String secretKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			cipher.init(Cipher.ENCRYPT_MODE, getAesKey(secretKey));
			return cipher.doFinal(content.getBytes("utf-8"));
		} catch (Exception e) {
			logger.warn("加密内容失败 :{}", e.getMessage());
			GlobalException.warn( "内容加密失败");
			return null;
		}
	}

	/**
	 * AES解密
	 * 
	 * @param encryptBytes待解密的byte[]
	 * @param secretKey解密密钥
	 * @return 解密后的String
	 * @throws Exception 解密失败将抛出解密失败的异常
	 */
	public static final String decrypt(byte[] encryptBytes, String secretKey) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			cipher.init(Cipher.DECRYPT_MODE, getAesKey(secretKey));
			byte[] decryptBytes = cipher.doFinal(encryptBytes);
			return new String(decryptBytes);
		} catch ( Exception e) {
			logger.warn("解密内容失败:{}", e.getMessage());
			GlobalException.warn( "内容解密失败");
			return null;
		}
	}
	
	/**
	 * 生成AesKey
	 * @param secretKey
	 * @return
	 */
	private static final Key getAesKey(String secretKey) {
		return new SecretKeySpec(secretKey.getBytes(), "AES");
	}
}
