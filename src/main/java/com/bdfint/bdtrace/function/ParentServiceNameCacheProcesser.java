package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public class ParentServiceNameCacheProcesser implements ParentServiceNameCacheProcessing {
    /**
     * cache for parent service
     */
    protected static final ThreadLocal<Map<Long, LocalSpanId>> cache = new ThreadLocal<Map<Long, LocalSpanId>>() {
        @Override
        protected Map<Long, LocalSpanId> initialValue() {
            return new HashMap<>();
        }
    };
    private static final Logger logger = LoggerFactory.getLogger(ParentServiceNameCacheProcesser.class);

    @Override
    public void setParentServiceName(String serviceName, SpanId spanId) {
        cache.get().put(spanId.spanId, new LocalSpanId(spanId, serviceName, serviceName, Thread.currentThread()));
    }

    @Override
    public LocalSpanId getParentLocalSpanId(String interfaceName, SpanId spanId) {
        if (cache.get().size() == 0) {
            if (spanId.nullableParentId() != null) // 当不是根节点时，consumer端应该获取到父节点的缓存
                logger.warn("WARNING when get parent service name");
        } else {
            LocalSpanId localSpanId = cache.get().get(spanId.parentId);//获取父节点缓存，为了使当前节点接收父节点的依赖
//            logger.info(Thread.currentThread() + "<=>" + localSpanId.getCurrentThread());

//            Test.testForParentChildrenRelationship(parentSpanServiceName, interfaceName);
            return localSpanId;

        }
        return null;
    }
}
