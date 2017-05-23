package com.bdfint.bdtrace.adapter;

import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;
import com.github.kristofa.brave.internal.Nullable;
import zipkin.Constants;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.github.kristofa.brave.IdConversion.convertToLong;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public class DubboServerRequestAdapter implements ServerRequestAdapter {
    private Map<String, String> headers;
    private String spanName;
    long cs;

    public DubboServerRequestAdapter(@Nullable Map<String, String> headers, @Nullable String spanName) {
        this.headers = headers;
        this.spanName = spanName;
    }

    public TraceData getTraceData() {
        final String sampled = headers.get(DubboTraceConst.SAMPLED);
        if (sampled != null) {
            if (sampled.equals("0") || sampled.toLowerCase().equals("false")) {
                return TraceData.builder().sample(false).build();
            } else {
                final String parentSpanId = headers.get(DubboTraceConst.PARENT_SPAN_ID);
                final String traceId = headers.get(DubboTraceConst.TRACE_ID);
                final String spanId = headers.get(DubboTraceConst.SPAN_ID);
                String s = headers.get(Constants.CLIENT_SEND);
                //TODO
                cs = Long.valueOf(s);
                if (traceId != null && spanId != null) {
                    SpanId span = getSpanId(traceId, spanId, parentSpanId);
                    return TraceData.builder().sample(true).spanId(span).build();
                }

            }
        }
        return TraceData.builder().build();
    }

    public String getSpanName() {
        return this.spanName;
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {

        String elapse = String.valueOf((System.currentTimeMillis() - cs) / 1000) + "ms";
        return Collections.singleton(KeyValueAnnotation.create(DubboTraceConst.CLIENT_TO_SERVER_ELAPSE, elapse));
    }

    static SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder().traceId(convertToLong(traceId)).spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }
}
