package com.bdfint.bdtrace.adapter;

import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.github.kristofa.brave.KeyValueAnnotation;
import com.github.kristofa.brave.ServerResponseAdapter;
import com.github.kristofa.brave.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public class DubboServerResponseAdapter implements ServerResponseAdapter {
    private StatusEnum status;
    private String msg;
    private long sr;

    public DubboServerResponseAdapter(@Nullable StatusEnum status, String msg, long sr) {
        this.status = status;
        this.msg = msg;
        this.sr = sr;
    }

    public Collection<KeyValueAnnotation> responseAnnotations() {
        Collection<KeyValueAnnotation> annotations = new ArrayList<KeyValueAnnotation>();
        String elapse = String.valueOf(System.currentTimeMillis() - sr) + "ms";
        annotations.add(KeyValueAnnotation.create(DubboTraceConst.REMOTE_METHOD_CALL_ELAPSE, elapse));
        annotations.add(KeyValueAnnotation.create(DubboTraceConst.SERVER_RESPONSE_STATUS_CODE, status.getDesc()));
        if (msg != null)
            annotations.add(KeyValueAnnotation.create(DubboTraceConst.EXCEPTION_MESSAGE, msg));
//        return Collections.singleton(annotations);
        return annotations;
    }

}
