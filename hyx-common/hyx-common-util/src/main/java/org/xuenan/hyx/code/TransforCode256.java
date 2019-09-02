package org.xuenan.hyx.code;

public class TransforCode256{

	final static TransforCode CODE_STRING = new TransforCode((short)256);

	public final static Long deserialize(byte[] codes) {
		if (codes == null) {
			return null;
		}
		return CODE_STRING.deserialize(codes);
	}

	public final static byte[] serialize(Long _long) {
		if (_long == null) {
			return null;
		}
		return CODE_STRING.serialize(_long);
	}
	public final static short getLength() {
		return (short) CODE_STRING.getLength();
	}
}
