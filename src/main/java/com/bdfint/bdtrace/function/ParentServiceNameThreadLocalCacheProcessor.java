package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public class ParentServiceNameThreadLocalCacheProcessor implements ParentServiceNameCacheProcessing {
    /**
     * CACHE for parent service
     */
    protected static final ThreadLocal<String> CACHE = new ThreadLocal<String>();
    private static final Logger logger = LoggerFactory.getLogger(ParentServiceNameThreadLocalCacheProcessor.class);

    public ParentServiceNameThreadLocalCacheProcessor() {
        clearTask();
    }

    public static ThreadLocal<String> getCache() {
        return CACHE;
    }

    @Override
    public void setParentServiceName(String serviceName, SpanId spanId, Brave brave) {
        CACHE.set(serviceName);
    }

    @Override
    public LocalSpanId getParentLocalSpanId(SpanId spanId, String currServiceName) {
        if (CACHE.get() == null) {
//            if (spanId.nullableParentId() != null) // 当不是根节点时，consumer端应该获取到父节点的缓存
//                logger.error("ERROR when get parent service name");
            return new LocalSpanId(null, null, "unknown", null);
        } else {
            String pServiceName = CACHE.get();//获取父节点缓存，为了使当前节点接收父节点的依赖
            return new LocalSpanId(null, null, pServiceName, null);
        }
    }

    /**
     * 清理缓存
     *
     * @return 是否有清理过缓存
     */
    @Override
    public boolean outOfSpace() {
        return false;
    }

    public boolean clearCache() {
        CACHE.remove();
        return true;
    }


    /**
     * 定时清理,一旦new一个当前对象则有一个单线程的线程池被修改，重新执行任务
     */
    public void clearTask() {
    }
}
