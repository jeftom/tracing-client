package com.bdfint.bdtrace.adapter;

import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.github.kristofa.brave.ClientResponseAdapter;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.internal.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public class DubboClientResponseAdapter implements ClientResponseAdapter {
    private StatusEnum status;
    private Throwable throwable;
    private long cs;

    public DubboClientResponseAdapter(@Nullable StatusEnum status, Throwable throwable, long cs) {
        this.status = status;
        this.throwable = throwable;
        this.cs = cs;
    }

    public Collection<KeyValueAnnotation> responseAnnotations() {
        Collection<KeyValueAnnotation> annotations = new ArrayList<KeyValueAnnotation>();
        String elapse = String.valueOf(System.currentTimeMillis() - cs) + "ms";
        annotations.add(KeyValueAnnotation.create(DubboTraceConst.WHOLE_ELAPSE, elapse));
        annotations.add(KeyValueAnnotation.create(DubboTraceConst.CLIENT_RESPONSE_STATUS_CODE, status.getDesc()));
        if (throwable != null) {
            annotations.add(KeyValueAnnotation.create(DubboTraceConst.EXCEPTION_STACK_MESSAGE, Arrays.toString(throwable.getStackTrace())));
            annotations.add(KeyValueAnnotation.create(DubboTraceConst.EXCEPTION_MESSAGE, throwable.getCause().toString()));
        }
//        return Collections.singleton(annotations);
        return annotations;
    }
}
