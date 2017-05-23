package com.bdfint.bdtrace.adapter;

import com.alibaba.dubbo.rpc.RpcContext;
import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.util.IPConversionUtils;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.twitter.zipkin.gen.Endpoint;
import zipkin.Constants;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public class DubboClientRequestAdapter implements ClientRequestAdapter {
    public Map<String, String> getHeaders() {
        return headers;
    }

    private Map<String, String> headers;
    private String spanName;

    public SpanId getSpanId() {
        return spanId;
    }

    private SpanId spanId;

    public DubboClientRequestAdapter(Map<String, String> headers, String spanName) {
        this.headers = headers;
        this.spanName = spanName;
    }

    public String getSpanName() {
        return this.spanName;
    }

    public void addSpanIdToRequest(SpanId spanId) {
        this.spanId = spanId;
        if (spanId == null) {
            headers.put(DubboTraceConst.SAMPLED, "0");
        } else {
            headers.put(DubboTraceConst.SAMPLED, "1");
            headers.put(DubboTraceConst.TRACE_ID, IdConversion.convertToString(spanId.traceId));
            headers.put(DubboTraceConst.SPAN_ID, IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                headers.put(DubboTraceConst.PARENT_SPAN_ID, IdConversion.convertToString(spanId.parentId));
            }
            headers.put(DubboTraceConst.SPAN_NAME, spanName);
            //TODO
            headers.put(Constants.CLIENT_SEND, String.valueOf(System.currentTimeMillis()));
        }
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {
        return Collections.emptyList();
    }

    public Endpoint serverAddress() {
        InetSocketAddress inetSocketAddress = RpcContext.getContext().getRemoteAddress();
        String ipAddr = RpcContext.getContext().getUrl().getIp();
        return Endpoint.create(spanName, IPConversionUtils.convertToInt(ipAddr), inetSocketAddress.getPort());

    }
}
