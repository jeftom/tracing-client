package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
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
    /**
     * CACHE for parent service
     */
    protected static final Map<Long, LocalSpanId> CACHE = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ParentServiceNameMapCacheProcessor.class);
    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private static int sInternal = 3 * 1000; // unit is ms

    public ParentServiceNameMapCacheProcessor() {
        clearTask();
    }

    public static Map<Long, LocalSpanId> getCache() {
        return CACHE;
    }

    @Override
    public void setParentServiceName(String serviceName, SpanId spanId) {
        CACHE.put(spanId.spanId, new LocalSpanId(spanId, serviceName, serviceName, Thread.currentThread()));
    }

    @Override
    public LocalSpanId getParentLocalSpanId(SpanId spanId) {
        LocalSpanId localSpanId = CACHE.get(spanId.parentId);//获取父节点缓存，为了使当前节点接收父节点的依赖
        if (localSpanId == null) {
            if (spanId.nullableParentId() != null) // 当不是根节点时，consumer端应该获取到父节点的缓存
                logger.error("ERROR when get parent service name");
            return null;
        }

        if (System.currentTimeMillis() - localSpanId.getTime() > sInternal) {
            logger.warn("设置的缓存清理时间过小，请重新设置");
            sInternal += 1000;
        }

//            Test.testForParentChildrenRelationship(parentSpanServiceName, serviceName);
        if (localSpanId == null)
            logger.error("ERROR CACHE get null object. which means no CACHE set but try to get.");
        return localSpanId;


    }

    public boolean clearCache() {
        boolean hasEntryRemoved = false;
        Map<Long, LocalSpanId> spanIdMap = CACHE;
        for (Map.Entry<Long, LocalSpanId> entry : spanIdMap.entrySet()) {
            LocalSpanId value = entry.getValue();
            long elapse = System.currentTimeMillis() - value.getTime();
            logger.debug("已生存" + elapse + "ms");
            if (elapse >= sInternal) {
                spanIdMap.remove(entry.getKey());
                logger.debug("cache {} has been clear", entry.getValue().getParentSpanServiceName());
                hasEntryRemoved = true;
            }
        }

        return hasEntryRemoved;
    }


    /**
     * 定时清理,一旦new一个当前对象则有一个单线程的线程池被修改，重新执行任务
     */
    public void clearTask() {
        SERVICE.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                clearCache();
            }
        }, sInternal, sInternal, TimeUnit.MILLISECONDS);
    }
}
