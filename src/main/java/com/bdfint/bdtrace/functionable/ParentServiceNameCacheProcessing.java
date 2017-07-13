package com.bdfint.bdtrace.functionable;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.SpanId;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public interface ParentServiceNameCacheProcessing {

    void setParentServiceName(String serviceName, SpanId spanId, Brave brave);

    /**
     * use spanId.spanId as key to get CACHE
     *
     * @param spanId
     * @param currServiceName
     * @return
     */
    LocalSpanId getParentLocalSpanId(SpanId spanId, String currServiceName);


    /**
     * 清理缓存
     *
     * @return 是否有清理过缓存
     */
//    static boolean clearCache();

    boolean outOfSpace();
}
