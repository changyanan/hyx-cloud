package com.hyx.core.utils;

/**
 * 数据太小时不建议压缩，因为越压缩可能越大，建议压缩限制最小 200Byte
 * @author lenovo
 *
 */
public interface Zip {
	/**
	 * 压缩
	 * @param d
	 * @return
	 */
	byte[] z(byte[] data);
	/**
	 * 解压缩
	 * @param d
	 * @return
	 */
	byte[] un(byte[] data);
	
	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

}
