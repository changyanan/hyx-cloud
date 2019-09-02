package com.hyx.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description TODO
 * @date 2019年09月02日 17:21:00
 */
public class LoadUtils {
    private static final Logger log = LoggerFactory.getLogger(LoadUtils.class);

    public static <V> void load(FindByIdAndTime<V> idAndTime, SyncCache<V> cache) throws InterruptedException{
        LoadUtils.<V>load(idAndTime, cache, 10000);
    }

    public static <V> void load(FindByIdAndTime<V> idAndTime, SyncCache<V> cache,int pageSize) throws InterruptedException {
        Long startTime=System.currentTimeMillis();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger id = new AtomicInteger(0);
        AtomicReference<Date> date = new AtomicReference<Date>(new Date(0));
        AtomicReference<Throwable> throwable = new AtomicReference<Throwable>();
        List<V> pages;
        Semaphore semaphore = new Semaphore(36);
        AtomicInteger ths = new AtomicInteger(0);
        while (ListUtils.isNotEmpty(pages = idAndTime.findByIdAndTime(date, id, pageSize))) {
            List<V> ds = pages;
            semaphore.acquire();
            ths.incrementAndGet();
            AsyncUtils.execute(() -> {
                try {
                    cache.syncCache(ds);
                    log.info("成功加载{}个到缓存,已加载{}个到缓存", ds.size(), count.addAndGet(ds.size()));
                }catch (Throwable e) {
                    throwable.set(e);
                    log.warn("缓存数据异常",e);
                } finally {
                    semaphore.release();
                    ths.decrementAndGet();
                }
            });
        }
        while (ths.get() > 0) {
            Thread.sleep(50);
        }
        Long endTime=System.currentTimeMillis();
        log.info("缓存完毕， 总共已缓存{}条数据 ,总共耗时{}秒",count.get(),(endTime-startTime)/1000.0);
        if(throwable.get()!=null) {
            throw new RuntimeException(throwable.get());
        }
    }

    @FunctionalInterface
    public interface SyncCache<V> {
        void syncCache(List<V> ds);
    }

    @FunctionalInterface
    public interface FindByIdAndTime<V> {
        List<V> findByIdAndTime(AtomicReference<Date> date, AtomicInteger id, Integer pageSize);
    }

}
