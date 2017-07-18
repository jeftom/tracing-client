package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public class ParentServiceNameMapCacheProcessor implements ParentServiceNameCacheProcessing {
    private static final Logger logger = LoggerFactory.getLogger(ParentServiceNameMapCacheProcessor.class);
    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    /**
     * CACHE for parent service
     */
    protected static volatile Map<Long, LocalSpanId> CACHE = new ConcurrentHashMap<>();
    private static int ttl = 3 * 60 * 1000; // unit is ms
    private static final int CLEAR_INTERNAL = 1000; // unit is ms

    public ParentServiceNameMapCacheProcessor() {
        clearTask();
    }

    public static Map<Long, LocalSpanId> getCache() {
        return CACHE;
    }

    public void setParentServiceName(String serviceName, SpanId spanId) {
        CACHE.put(spanId.spanId, new LocalSpanId(spanId, serviceName, serviceName, Thread.currentThread()));
        logger.debug("缓存设置成功，spanId is {},serviceName is {},缓存容量 {}", spanId.spanId, serviceName, CACHE.size());
    }

    public LocalSpanId getParentLocalSpanId(SpanId spanId) {
        LocalSpanId localSpanId = CACHE.get(spanId.parentId);//获取父节点缓存，为了使当前节点接收父节点的依赖
        if (localSpanId == null) {
            if (spanId.nullableParentId() != null) // 当不是根节点时，consumer端应该获取到父节点的缓存
                logger.error("ERROR when get parent service name");
            return null;
        }

        if (System.currentTimeMillis() - localSpanId.getTime() > ttl) {
            logger.warn("设置的缓存清理时间过小，请重新设置");
            ttl += 1000;
        }

//            Test.testForParentChildrenRelationship(parentSpanServiceName, serviceName);
        if (localSpanId == null)
            logger.error("ERROR CACHE get null object. which means no CACHE set but try to get.");

        logger.debug("获取缓存 {}", spanId.parentId);
        return localSpanId;

    }

    @Override
    public void setParentServiceName(String serviceName, SpanId spanId, Brave brave) {

    }

    /**
     * use spanId.spanId as key to get CACHE
     *
     * @param spanId
     * @param currServiceName
     * @return
     */
    @Override
    public LocalSpanId getParentLocalSpanId(SpanId spanId, String currServiceName) {
        return null;
    }

    public boolean clearCache() {
        boolean hasEntryRemoved = false;
        Map<Long, LocalSpanId> spanIdMap = CACHE;
        for (Map.Entry<Long, LocalSpanId> entry : spanIdMap.entrySet()) {
            LocalSpanId value = entry.getValue();
            long elapse = System.currentTimeMillis() - value.getTime();
            logger.debug("缓存@{} 已生存 {} ms", value.getParentSpanId(), elapse);
            if (elapse >= ttl) {
                spanIdMap.remove(entry.getKey());
                logger.debug("缓存 {} has been clear,缓存容量 {}", value.getParentSpanId(), CACHE.size());
                hasEntryRemoved = true;
            }
        }

        return hasEntryRemoved;
    }

    @Override
    public boolean outOfSpace() {
        return false;
    }


    /**
     * 定时清理,一旦new一个当前对象则有一个单线程的线程池被修改，重新执行任务
     */
    public void clearTask() {
        SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logger.debug("清理线程："+Thread.currentThread().toString());
                clearCache();
            }
        }, ttl, CLEAR_INTERNAL, TimeUnit.MILLISECONDS);
    }
}
