package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.support.AbstractDubboFilter;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientTracer;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

@Activate(group = {Constants.CONSUMER})
public class BraveConsumerFilter extends AbstractDubboFilter {
    private static final Logger logger = LoggerFactory.getLogger(BraveConsumerFilter.class);
    String interfaceName;
    SpanId spanId = null;

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        IgnoreFilter ignoreFilter = new IgnoreFilter(invoker, invocation).invoke();
        if (ignoreFilter.is())
            return invoker.invoke(invocation);
        interfaceName = ignoreFilter.getInterfaceName();
        status = ignoreFilter.getStatus();

        setInterceptors(interfaceName);
        logger.debug(interfaceName + " consumer execute");
        DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(invocation.getAttachments(), interfaceName);
        spanId = newNullableSpanId(clientRequestAdapter);
        if (spanId == null)
            return invoker.invoke(invocation);
        getParentServiceNameAndSetBrave(interfaceName, spanId);
        clientRequestInterceptor.handle(clientRequestAdapter);

        annotated.clientSent(interfaceName, clientRequestAdapter);
        Result result = null;
        String msg = null;
        try {
            result = invoker.invoke(invocation);
            msg = handleException(result, interfaceName);
        } catch (RpcException e) {
            status = StatusEnum.ERROR;
            msg = Arrays.toString(e.getStackTrace());
            throw new RuntimeException(e.getCause());
        } finally {
            final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter(status, msg, System.currentTimeMillis());
            clientResponseInterceptor.handle(clientResponseAdapter);
            return result;
        }
    }


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

}