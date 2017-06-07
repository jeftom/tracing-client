package com.bdfint.bdtrace.functionable;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.github.kristofa.brave.SpanId;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public interface ParentServiceNameCacheProcessing {

    void setParentServiceName(String serviceName, SpanId spanId);

    /**
     * use spanId.spanId as key to get CACHE
     *
     * @param spanId
     * @return
     */
    LocalSpanId getParentLocalSpanId(SpanId spanId);


    /**
     * 清理缓存
     *
     * @return 是否有清理过缓存
     */
    boolean clearCache();
}
