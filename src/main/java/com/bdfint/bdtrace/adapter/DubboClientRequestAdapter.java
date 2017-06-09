package com.bdfint.bdtrace.adapter;

import com.alibaba.dubbo.rpc.RpcContext;
import com.bdfint.bdtrace.function.AttachmentTransmission;
import com.bdfint.bdtrace.functionable.AttachmentTransmittable;
import com.bdfint.bdtrace.util.IPConversionUtils;
import com.github.kristofa.brave.ClientRequestAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.SpanId;
import com.twitter.zipkin.gen.Endpoint;

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
    AttachmentTransmittable transmittable = new AttachmentTransmission();
    private Map<String, String> headers;
    private String serviceName;
    private SpanId spanId;

    public DubboClientRequestAdapter(Map<String, String> headers, String serviceName) {
        this.headers = headers;
        this.serviceName = serviceName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public SpanId getSpanId() {
        return spanId;
    }

    public String getSpanName() {
        return this.serviceName;
    }

    public void addSpanIdToRequest(SpanId spanId) {
        transmittable.putAttachments(headers, spanId, serviceName);
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {
        return Collections.emptyList();
    }

    public Endpoint serverAddress() {
        InetSocketAddress inetSocketAddress = RpcContext.getContext().getRemoteAddress();
        String ipAddr = RpcContext.getContext().getUrl().getIp();
        return Endpoint.create(serviceName, IPConversionUtils.convertToInt(ipAddr), inetSocketAddress.getPort());

    }
}
