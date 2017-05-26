package com.bdfint.bdtrace.adapter;

import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.function.AttachmentTransmission;
import com.bdfint.bdtrace.functionable.IAttachmentTransmittable;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerRequestAdapter;
import com.github.kristofa.brave.TraceData;
import com.github.kristofa.brave.internal.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public class DubboServerRequestAdapter implements ServerRequestAdapter {
    long cs;
    IAttachmentTransmittable transmittable = new AttachmentTransmission();
    private Map<String, String> headers;
    private String spanName;

    public DubboServerRequestAdapter(@Nullable Map<String, String> headers, @Nullable String spanName) {
        this.headers = headers;
        this.spanName = spanName;
    }

    public TraceData getTraceData() {
        return transmittable.getAttachmentsAndBuildTraceData(headers, new long[]{cs});
    }

    public String getSpanName() {
        return this.spanName;
    }

    public Collection<KeyValueAnnotation> requestAnnotations() {

        String elapse = String.valueOf((System.currentTimeMillis() - cs) / 1000.0) + "ms";
        return Collections.singleton(KeyValueAnnotation.create(DubboTraceConst.CLIENT_TO_SERVER_ELAPSE, elapse));
    }
}
