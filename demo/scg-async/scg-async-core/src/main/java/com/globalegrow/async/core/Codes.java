package com.globalegrow.async.core;


/**
 * 简单编解码协议
 * @author hulei
 *
 */
public class Codes {
	
	/**
	 * 将序列化好的byte[]数组编码为byte数组
	 * @param bytess
	 * @return
	 */
	public final static byte[] encode(byte[][] bytess) {
		String lenStr="";
		int destPos =6;
		int len=destPos;
		for (int i = 0; i < bytess.length; i++) {
			byte[] bs = bytess[i];
			if(bs==null) {
				lenStr+= "-1,";
				continue;
			}
			lenStr+=bs.length+",";
			len+=bs.length;
		}
		byte[] lenBytes=lenStr.getBytes();
		len+=lenBytes.length;
		byte[] ret=new byte[len];
		byte[] indexBytes=(lenBytes.length+"").getBytes();
		System.arraycopy(indexBytes, 0, ret, 0, indexBytes.length);
		System.arraycopy(lenBytes, 0, ret, destPos, lenBytes.length);
		destPos+=lenBytes.length;
		for (int i = 0; i < bytess.length; i++) {
			byte[] bs = bytess[i];
			if(bs==null) {
				continue;
			}
			if(bs.length>0) {
				System.arraycopy(bs, 0, ret, destPos, bs.length);
			}
			destPos+=bs.length;
		}
		return ret;
	}
	
	/**
	 * 将一个报问体解码为多个byte数组
	 * @param bytess
	 * @return
	 */
	public final static byte[][] decode(byte[] bytess) {
		int len=6;
		int srcPos =0;
		byte[] indexBytes=new byte[len];
		System.arraycopy(bytess, srcPos, indexBytes, 0, indexBytes.length);
		srcPos+=indexBytes.length;
		String indexStr=new String(indexBytes);
		int index=Integer.parseInt(indexStr.trim());
		byte[] lenBytes=new byte[index-1];
		System.arraycopy(bytess, srcPos, lenBytes, 0, lenBytes.length);
		srcPos+=index;
		String lenStr=new String(lenBytes);
		String[] lenStrs= lenStr.split(",");
		byte[][] ret=new byte[lenStrs.length][];
		for (int i = 0; i < lenStrs.length; i++) {
			int dataLen =Integer.parseInt(lenStrs[i]);
			if(dataLen==-1) {
				continue;
			}
			ret[i]=new byte[dataLen];
			if(dataLen>0) {
				System.arraycopy(bytess, srcPos, ret[i], 0, ret[i].length);
			}
			srcPos+=ret[i].length;
		}
		return ret;
	}
 
	public static void main(String[] args) {
		byte[] bytes = Codes.encode(new byte[][] {"434343,".getBytes(),null,"34444444444444,".getBytes(),"".getBytes(),"4344444444444444".getBytes(),null,"34444444444444,".getBytes(),"".getBytes(),"4344444444444444".getBytes()});
		System.err.println(new String(bytes));
		byte[][] bytess = Codes.decode(bytes);
		for (int i = 0; i < bytess.length; i++) {
			byte[] bs = bytess[i];
			if(bs!=null) {
				System.err.println(new String(bs));
			}else {
				System.err.println(bs);
			}
		}
	}
}
