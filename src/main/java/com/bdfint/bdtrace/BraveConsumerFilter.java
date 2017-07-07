package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
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

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        try {
            result = super.invoke(invoker, invocation);
        } catch (RpcException e) {
            result = invoker.invoke(invocation);
            logger.error("RPC Consumer 端异常，忽略本次追踪。", e);
        }
        return result;
    }

    /**
     * 反射获取tracer并new spanId,null if sample is 0 or null
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
        logger.debug(serviceName + " consumer execute");

        DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(invocation.getAttachments(), spanName, serviceName);
        // 获取到
//        SpanId spanId = newNullableSpanId(clientRequestAdapter);
//        if (spanId == null)
//            return true;
//        if (spanId.nullableParentId() != null)
//            getParentServiceNameAndSetBrave(serviceName, spanId);

        //clientRequestInterceptor has changed to parent service name brave.clientRequestInterceptor
        clientRequestInterceptor.handle(clientRequestAdapter);
//        new ClientRequestInterceptorProxy().handle(spanId, clientRequestInterceptor, clientRequestAdapter);
        annotated.clientSent(serviceName, clientRequestAdapter);

        return false;
    }

    @Override
    public void afterHandle(Invocation invocation) {
        final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter(status, exception, annotated.cs());
        clientResponseInterceptor.handle(clientResponseAdapter);
    }
}