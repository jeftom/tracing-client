package com.bdfint.bdtrace.support;

import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import zipkin.Annotation;

/**
 * @author heyb
 * @date 2017/5/22.
 * @desriptioin
 */
public interface Annotated {

    /**
     * 获取CS annotation
     *
     * @param interfaceName        服务名称
     * @param clientRequestAdapter adapter for csInterceptor
     * @return Annotation
     */
    Annotation clientSent(String interfaceName, DubboClientRequestAdapter clientRequestAdapter);

    Annotation serverReceived(String interfaceName, DubboServerRequestAdapter serverRequestAdapter);

    Annotation serverSent(String interfaceName, DubboServerResponseAdapter serverResponseAdapter);

    Annotation clientReceived(String interfaceName, DubboClientResponseAdapter clientResponseAdapter);
}
