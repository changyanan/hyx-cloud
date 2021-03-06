package org.xuenan.hyx.code;

public class TransforCode36String {

	final static TransforCode CODE_STRING = new TransforCode("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes());

	public static TransforCode getCodeString() {
		return CODE_STRING;
	}
	public final static Long deserialize(String codes) {
		if (codes == null) {
			return null;
		}
		return CODE_STRING.deserialize(codes.getBytes());
	}

	public final static String serialize(Long _long) {
		if (_long == null) {
			return null;
		}
		return new String(CODE_STRING.serialize(_long));
	}
	public final static byte getLength() {
		return (byte) CODE_STRING.getLength();
	}
}
