package org.xuenan.hyx.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 根据64进制生产序列字符串（64进制包括大写小写字母和数字，区分大小写）
 * 该序列合理使用理论上可以永远不重复
 * @author leige
 *
 */
public final class Sequence {
	private final static Logger LOGGER = LoggerFactory.getLogger(Sequence.class);
	private final static TransforCode CODE_STRING = TransforCode64String.CODE_STRING;
	private final static short LENGTH = (short) CODE_STRING.getLength();
	private final static byte EMPTY = CODE_STRING.serialize(0L)[0];
	private final static int TIMESTAMP_SHIFT = 10;
	private final static int SEQUENCE_SHIFT = 3;
	private final static int SERVERID_SHIFT = 2;
	private final static int SEQUENCE_MAX = (int) Math.pow(LENGTH, SEQUENCE_SHIFT) - 1;
	private final static int SERVERID_MAX = (int) Math.pow(LENGTH, SERVERID_SHIFT) - 1;

	private static String lastTimestampString = "";
	private volatile static long lastTimestamp = -1L;
	private static AtomicInteger serverid = new AtomicInteger(0);
	private static String serveridStr = new String(new byte[] { EMPTY, EMPTY });
	private static AtomicInteger atomicSequence = new AtomicInteger(0);

	/**
	 * 产生一个序列串,如 00jknqqia6-vp-000, 00jknqqia6 以毫秒为单位的时间戳标识， vp服务器标识， 0uz序列标识
	 * 
	 * @return
	 */
	public final static SequenceOnce generate() {
		validServerid();
		SequenceOnce sequenceOnce = new SequenceOnce();
		sequenceOnce.serverIdString=serveridStr;
		nextSequence(sequenceOnce);
		return sequenceOnce;
	}

	private final static void nextSequence(SequenceOnce sequenceOnce) {
		int sequence = atomicSequence.updateAndGet((int operand) -> {
			long runTime = System.currentTimeMillis();
			if (runTime == lastTimestamp&&operand < SEQUENCE_MAX) {
				sequenceOnce.timestampString = lastTimestampString;
				return operand + 1;
			} else if (runTime >= lastTimestamp ) {
				if (operand >= SEQUENCE_MAX) {
					while(runTime == System.currentTimeMillis());
					runTime = System.currentTimeMillis();
				}
				byte[] tc = CODE_STRING.serialize(runTime);
				lastTimestampString = addFormat(tc,TIMESTAMP_SHIFT);
				sequenceOnce.timestampString = lastTimestampString;
				lastTimestamp = runTime;
				return 0;
			} else if (runTime < lastTimestamp) {
				throw new RuntimeException("时间不可以向前调整");
			}
			return 0;
		});
		byte[] tc = CODE_STRING.serialize((long) sequence);
		sequenceOnce.sequenceString=addFormat(tc, SEQUENCE_SHIFT);
	}

	private static void validServerid() {
		if (serverid.compareAndSet(0, 1)) {
			short serverid = (short) (Math.random() * SERVERID_MAX);
			setServerid(serverid);
			LOGGER.warn("请注意,Sequence序列未设置服务器id,多台服务器有可能会生成相同的序列,随机产生的服务器id {}", serverid);
		}
	}

	private static String addFormat( byte[] tc, int length) {
		byte[] buffer = new byte[length];
		for (int i = 0; i < (length - tc.length); i++) {
			buffer[i] = EMPTY;
		}
		System.arraycopy(tc, 0, buffer, length - tc.length, tc.length);
		return new String(buffer);
	}

	/**
	 * 获取服务器标识
	 * 
	 * @return
	 */
	public static short getServerid() {
		long serverid = CODE_STRING.deserialize(serveridStr.getBytes());
		return (short) serverid;
	}

	/**
	 * 设置服务器编号，必须大于0
	 * 
	 * @param i
	 */
	public synchronized static void setServerid(int i) {
		if (i < 0 || i > SERVERID_MAX) {
			throw new RuntimeException("serverid必须大于0且小于" + SERVERID_MAX);
		}
		serverid.set(i);
		byte[] st = CODE_STRING.serialize((long) i);
		serveridStr = addFormat( st, SERVERID_SHIFT);
	}
}
