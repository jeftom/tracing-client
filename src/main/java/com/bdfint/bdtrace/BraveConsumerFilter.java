package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.function.AbstractDubboFilter;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

@Activate(group = {Constants.CONSUMER})
public class BraveConsumerFilter extends AbstractDubboFilter {
    private static final Logger logger = LoggerFactory.getLogger(BraveConsumerFilter.class);
    SpanId spanId = null;

    /**
     * 反射获取tracer并new spanId
     *
     * @param clientRequestAdapter
     * @return
     */
    private SpanId newNullableSpanId(DubboClientRequestAdapter clientRequestAdapter) {
        SpanId spanId;
        try {
            Field field = ClientRequestInterceptor.class.getDeclaredField("clientTracer");
            field.setAccessible(true);
            ClientTracer clientTracer = (ClientTracer) field.get(clientRequestInterceptor);
            spanId = clientTracer.startNewSpan(clientRequestAdapter.getSpanName());//may be null
            return spanId;
        } catch (NoSuchFieldException e) {
            logger.info("异常信息：", e);
        } catch (IllegalAccessException e) {
            logger.info("异常信息：", e);
        }
        return null;
    }

    @Override
    public boolean preHandle(Invoker<?> invoker, Invocation invocation) {
        serviceName = serviceInfoProvidable.serviceName();
        setInterceptors(serviceName);
        logger.debug(serviceName + " consumer execute");

        DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(invocation.getAttachments(), serviceName);
        spanId = newNullableSpanId(clientRequestAdapter);
        if (spanId == null)
            return true;
        getParentServiceNameAndSetBrave(serviceName, spanId);
        clientRequestInterceptor.handle(clientRequestAdapter);

        annotated.clientSent(serviceName, clientRequestAdapter);

        return false;
    }

    @Override
    public void afterHandle() {
        final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter(status, errMsg, System.currentTimeMillis());
        clientResponseInterceptor.handle(clientResponseAdapter);
    }
}