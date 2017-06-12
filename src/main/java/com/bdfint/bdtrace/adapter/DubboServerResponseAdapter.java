package com.bdfint.bdtrace.adapter;

import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;
import com.github.kristofa.brave.internal.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public class DubboServerResponseAdapter implements ServerResponseAdapter {
    private StatusEnum status;
    private Throwable throwable;
    private long sr;

    public DubboServerResponseAdapter(@Nullable StatusEnum status, Throwable throwable, long sr) {
        this.status = status;
        this.throwable = throwable;
        this.sr = sr;
    }

    public Collection<KeyValueAnnotation> responseAnnotations() {
        Collection<KeyValueAnnotation> annotations = new ArrayList<KeyValueAnnotation>();
        String elapse = String.valueOf(System.currentTimeMillis() - sr) + "ms";
        annotations.add(KeyValueAnnotation.create(DubboTraceConst.REMOTE_METHOD_CALL_ELAPSE, elapse));
        annotations.add(KeyValueAnnotation.create(DubboTraceConst.SERVER_RESPONSE_STATUS_CODE, status.getDesc()));
        if (throwable != null) {
            annotations.add(KeyValueAnnotation.create(DubboTraceConst.EXCEPTION_STACK_MESSAGE, Arrays.toString(throwable.getStackTrace())));
            annotations.add(KeyValueAnnotation.create(DubboTraceConst.EXCEPTION_MESSAGE, throwable.getCause().toString()));
        }
//        return Collections.singleton(annotations);
        return annotations;
    }

}
