package com.hyx.core.code;

public class SequenceOnce {
	String timestampString;
	String serverIdString;
	String sequenceString;
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(timestampString);
		buffer.append('-');
		buffer.append(serverIdString);
		buffer.append('-');
		buffer.append(sequenceString);
		return buffer.toString();
	}
}