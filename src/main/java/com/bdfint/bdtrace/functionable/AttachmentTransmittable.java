package com.bdfint.bdtrace.functionable;

import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin dubbo 参数传递 c to s
 */
public interface AttachmentTransmittable {

    /**
     * get SpanId From invocation
     *
     * @param headers
     * @param clientSent
     * @return
     */
    TraceData getAttachmentsAndBuildTraceData(Map<String, String> headers, long[] clientSent);

    /**
     * put spanId in invocation
     *
     * @param headers
     * @param spanId
     * @param spanName
     */
    void putAttachments(Map<String, String> headers, SpanId spanId, String spanName);
}
