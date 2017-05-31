package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public class ParentServiceNameCacheProcessor implements ParentServiceNameCacheProcessing {
    /**
     * cache for parent service
     */
    protected static final ThreadLocal<Map<Long, LocalSpanId>> cache = new ThreadLocal<Map<Long, LocalSpanId>>() {
        @Override
        protected Map<Long, LocalSpanId> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };
    private static final Logger logger = LoggerFactory.getLogger(ParentServiceNameCacheProcessor.class);

    public static ThreadLocal<Map<Long, LocalSpanId>> getCache() {
        return cache;
    }

    @Override
    public void setParentServiceName(String serviceName, SpanId spanId) {
        cache.get().put(spanId.spanId, new LocalSpanId(spanId, serviceName, serviceName, Thread.currentThread()));
    }

    @Override
    public LocalSpanId getParentLocalSpanId(SpanId spanId) {
        if (cache.get().size() == 0) {
            if (spanId.nullableParentId() != null) // 当不是根节点时，consumer端应该获取到父节点的缓存
                logger.error("ERROR when get parent service name");
            return null;
        } else {
            LocalSpanId localSpanId = cache.get().get(spanId.parentId);//获取父节点缓存，为了使当前节点接收父节点的依赖

//            Test.testForParentChildrenRelationship(parentSpanServiceName, serviceName);
            if (localSpanId == null)
                logger.error("ERROR cache get null object. which means no cache set but try to get.");
            return localSpanId;

        }
    }

}
