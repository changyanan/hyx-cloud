package com.hyx.core.code;

import com.hyx.core.utils.Assert;

public class TransforCode {

	private static final int BYTE_SIZE=64;

	private final byte[] bytes;
	private final int length;

	public TransforCode(final byte[] bytes) {
		this.length = bytes.length;
		this.bytes = bytes;
	}

	public TransforCode(final short length) {
		Assert.isTrue(length>0, "序列化字符串不可以小于0");
		Assert.isTrue(length<=256, "序列化字符串不可以大于256");
		byte[] bytes = new byte[length];
		short len = (short) (length / 2 );
		for (short i = 0; i < length; i++) {
			bytes[i] = (byte) (i - len);
		}
		this.length = bytes.length;
		this.bytes = bytes;
	}

	public byte[] serialize(Long len) {
		if (len == null)
			return null;
		byte[] data=new byte[BYTE_SIZE];
		int i = 1;
		for (; i <= BYTE_SIZE; i++) {
			data[BYTE_SIZE-i]=bytes[(int) (len % length)];
			if((len = len / length) == 0) {
				break;
			}
		}
		byte[] ret=new byte[i];
		System.arraycopy(data, BYTE_SIZE-i, ret, 0, i);
		return ret;
	}
	
	public Long deserialize(final byte[] _bytes) {
		if (_bytes == null)
			return null;
		long lval = 0;
		for (byte _byte : _bytes) {
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] == _byte) {
					lval = lval * length + i;
					break;
				}
			}
		}
		return lval;
	}

	public int getLength() {
		return length;
	}
}
