package com.hyx.core.utils;

import com.reger.l2cache.pipeline.ops.RedisStringPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 17:17:00
 */
public class DistributedLock implements Lock {
    private static Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private final RedisStringPipeline<String, String> stringPipeline;
    private final String lockKey;
    private final Long timeoutSecond;
    private final int intervalMilliSeconds=20;
    private final static String lockId= UUID.randomUUID().toString().replaceAll("-", "");

    public DistributedLock(RedisStringPipeline<String, String> stringPipeline, String lockKey, Long timeoutSecond) {
        super();
        this.stringPipeline = stringPipeline;
        this.lockKey = "lock:"+ lockKey;
        this.timeoutSecond = timeoutSecond;
    }

    @Override
    public synchronized void lock() {
        try {
            lockInterruptibly();
        } catch (InterruptedException e) {
            logger.warn("获取锁中断",e);
            Thread.interrupted();
            throw new RuntimeException("获取锁中断",e);
        }
    }

    @Override
    public synchronized void lockInterruptibly() throws InterruptedException {
        while(true) {
            if(this.tryLock()) {
                return;
            }
            TimeUnit.MILLISECONDS.sleep(intervalMilliSeconds);
        }
    }
    private String getLockThreadId() {
        return String.format("%s.%s" ,lockId ,Thread.currentThread().getId());
    }
    @Override
    public synchronized boolean tryLock() {
        String lockThreadId=getLockThreadId();
        if(stringPipeline.setNxEx(lockKey, timeoutSecond, lockThreadId)  ) {
            logger.debug("获取锁成功");
            return true;
        }else {
            String  curLockThreadId=stringPipeline.get(lockKey);
            return lockThreadId.equals(curLockThreadId);
        }
    }

    @Override
    public synchronized boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        int size=(int) (unit.toMillis(time)/intervalMilliSeconds);
        for (int i = 0; i <= size; i++) {
            if(this.tryLock()) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(intervalMilliSeconds);
        }
        return false;
    }
    @Override
    public void unlock() {
        String lockThreadId=getLockThreadId();
        if(lockThreadId.equals(stringPipeline.get(lockKey))) {
            logger.debug("移除锁 {}", lockThreadId);
            stringPipeline.del(lockKey);
        }
    }

    @Override
    public Condition newCondition() {
        throw new RuntimeException("该方法未实现");
    }

}
