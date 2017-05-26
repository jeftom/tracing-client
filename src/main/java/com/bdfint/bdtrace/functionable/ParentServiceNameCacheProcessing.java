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

    LocalSpanId getParentLocalSpanId(String interfaceName, SpanId spanId);
}
