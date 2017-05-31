package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.functionable.AttachmentTransmittable;
import com.github.kristofa.brave.IdConversion;
import com.github.kristofa.brave.SpanId;
import com.github.kristofa.brave.TraceData;
import zipkin.Constants;

import java.util.Map;

import static com.github.kristofa.brave.IdConversion.convertToLong;

/**
 * @author heyb
 * @date 2017/5/26.
 * @desriptioin
 */
public class AttachmentTransmission implements AttachmentTransmittable {
    static SpanId getSpanId(String traceId, String spanId, String parentSpanId) {
        return SpanId.builder().traceId(convertToLong(traceId)).spanId(convertToLong(spanId))
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }

    @Override
    public TraceData getAttachmentsAndBuildTraceData(Map<String, String> headers, long[] clientSent) {
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
                clientSent[0] = Long.valueOf(s);
                if (traceId != null && spanId != null) {
                    SpanId span = getSpanId(traceId, spanId, parentSpanId);
                    return TraceData.builder().sample(true).spanId(span).build();
                }

            }
        }
        return TraceData.builder().build();
    }

    @Override
    public void putAttachments(Map<String, String> headers, SpanId spanId, String spanName) {
        if (spanId == null) {
            headers.put(DubboTraceConst.SAMPLED, DubboTraceConst.UN_SAMPLE_STATUS);
        } else {
            headers.put(DubboTraceConst.SAMPLED, DubboTraceConst.SAMPLE_STATUS);
            headers.put(DubboTraceConst.TRACE_ID, IdConversion.convertToString(spanId.traceId));
            headers.put(DubboTraceConst.SPAN_ID, IdConversion.convertToString(spanId.spanId));
            if (spanId.nullableParentId() != null) {
                headers.put(DubboTraceConst.PARENT_SPAN_ID, IdConversion.convertToString(spanId.parentId));
            }
            headers.put(DubboTraceConst.SPAN_NAME, spanName);
            headers.put(Constants.CLIENT_SEND, String.valueOf(System.currentTimeMillis()));
        }
    }
}
