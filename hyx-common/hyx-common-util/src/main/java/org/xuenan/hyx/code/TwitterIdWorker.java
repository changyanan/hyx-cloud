package org.xuenan.hyx.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 雪花算法生成ID
 * 每个节点最多每毫秒生成32768 个
 * 最多128个节点一起生成
 *
 * @author hulei
 * @date 2018/05/23
 */
public class TwitterIdWorker implements IdWorker {

    private static final Logger log = LoggerFactory.getLogger(TwitterIdWorker.class);

    private final static long TWEPOCH = 1288711299999L;
    private final static int WORKER_ID_BITS = 7;
    private final static int MAX_WORKER_ID = -1 ^ -1 << WORKER_ID_BITS;
    private final static int SEQUENCE_BITS = 15;
    private final static int WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static int TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static int SEQUENCE_MASK = -1 ^ -1 << SEQUENCE_BITS;

    private int workerId = -1;

    private volatile long sequence = 0L;
    private volatile long lastTimestamp = -1L;

    public TwitterIdWorker() {
        super();
    }

    public TwitterIdWorker(int workerId) {
        super();
        this.workerId = workerId;
    }

    public void setWorkerId(int workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.workerId = workerId;
    }

    @Override
    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if (this.sequence == 0) {
                if (log.isDebugEnabled()) {
                    log.debug("########### sequenceMask={}", SEQUENCE_MASK);
                }
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            throw new IllegalArgumentException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        }

        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT) | (this.workerId << WORKER_ID_SHIFT) | (this.sequence);
        if (log.isTraceEnabled()) {
            log.trace("timestamp:{},timestampLeftShift:{},nextId:{},workerId:{},sequence:{}", timestamp, TIMESTAMP_LEFT_SHIFT, nextId, workerId, sequence);
        }
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}