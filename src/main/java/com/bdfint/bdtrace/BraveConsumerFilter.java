package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.BraveFactory;
import com.bdfint.bdtrace.function.ParentServiceNameMapCacheProcessor;
import com.github.kristofa.brave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

@Activate(group = {Constants.CONSUMER})
public class BraveConsumerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(BraveConsumerFilter.class);
    protected static volatile ParentServiceNameMapCacheProcessor cacheProcessor = new ParentServiceNameMapCacheProcessor();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String serviceName;
        serviceName = invoker.getInterface().getCanonicalName();
        Brave brave = null;
        String spanName;
        spanName = invocation.getMethodName();

        ClientRequestInterceptor clientRequestInterceptor;
        ClientResponseInterceptor clientResponseInterceptor;
        ServerRequestInterceptor serverRequestInterceptor;
        ServerResponseInterceptor serverResponseInterceptor;
        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
            return invoker.invoke(invocation);
        }
        clientRequestInterceptor = brave.clientRequestInterceptor();
        clientResponseInterceptor = brave.clientResponseInterceptor();
        serverRequestInterceptor = brave.serverRequestInterceptor();
        serverResponseInterceptor = brave.serverResponseInterceptor();

        DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(invocation.getAttachments(), spanName, serviceName);

        SpanId spanId = newNullableSpanId(clientRequestAdapter, clientRequestInterceptor);
        if (spanId == null)
            return invoker.invoke(invocation);
        if (spanId.nullableParentId() != null) {
            LocalSpanId localSpanId = cacheProcessor.getParentLocalSpanId(spanId);

            //if there is no CACHE
            if (localSpanId != null) {
                String parentSpanServiceName = localSpanId.getParentSpanServiceName();
                brave = BraveFactory.nullableInstance(parentSpanServiceName);
                clientRequestInterceptor = brave.clientRequestInterceptor();
                clientResponseInterceptor = brave.clientResponseInterceptor();
                serverRequestInterceptor = brave.serverRequestInterceptor();
                serverResponseInterceptor = brave.serverResponseInterceptor();
            }
        }

        clientRequestInterceptor.handle(clientRequestAdapter);

        Result result = null;
        result = invoker.invoke(invocation);
        final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter(StatusEnum.OK, null, 0);
        clientResponseInterceptor.handle(clientResponseAdapter);
        return result;
    }

    protected void getParentServiceNameAndSetBrave(String serviceName, SpanId spanId) {
        LocalSpanId localSpanId = cacheProcessor.getParentLocalSpanId(spanId);

        //if there is no CACHE
        if (localSpanId != null) {
            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
            setInterceptors(parentSpanServiceName);
        }
    }

    protected void setInterceptors(String serviceName) {
//        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
//            return;
//        }
//        this.clientRequestInterceptor = brave.clientRequestInterceptor();
//        this.clientResponseInterceptor = brave.clientResponseInterceptor();
//        this.serverRequestInterceptor = brave.serverRequestInterceptor();
//        this.serverResponseInterceptor = brave.serverResponseInterceptor();
    }

    public void afterHandle(Invocation invocation) {
//        final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter(StatusEnum.OK, null, 0);
//        clientResponseInterceptor.handle(clientResponseAdapter);
    }

    /**
     * 反射获取tracer并new spanId,null if sample is 0 or null
     *
     * @param clientRequestAdapter
     * @return
     */
    private SpanId newNullableSpanId(DubboClientRequestAdapter clientRequestAdapter, ClientRequestInterceptor clientRequestInterceptor) {
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
}