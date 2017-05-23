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

import java.lang.reflect.Field;
import java.util.Arrays;

@Activate(group = {Constants.CONSUMER})
public class BraveConsumerFilter extends AbstractDubboFilter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        IgnoreFilter ingoreFilter = new IgnoreFilter(invoker, invocation).invoke();
        if (ingoreFilter.is())
            return invoker.invoke(invocation);
        String interfaceName = ingoreFilter.getInterfaceName();
        StatusEnum status = ingoreFilter.getStatus();
        SpanId spanId = null;

        setInterceptors(interfaceName);
        System.out.println(interfaceName + " consumer execute");
        DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(invocation.getAttachments(), interfaceName);
        spanId = newSpanId(interfaceName, clientRequestAdapter);
        getParentServiceNameAndSetBrave(interfaceName, spanId);
        clientRequestInterceptor.handle(clientRequestAdapter);

        //
        annotated.clientSent(interfaceName, clientRequestAdapter);
        Result result = null;
        String msg = null;
        try {
            result = invoker.invoke(invocation);
            if (result.hasException()) {
                System.out.println("======================Exception=====================");
                result.getException().printStackTrace();
                msg = Arrays.toString(result.getException().getStackTrace());
                System.out.println(interfaceName + "," + this + "," + System.currentTimeMillis());
                status = StatusEnum.ERROR;
            }
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

    private SpanId newSpanId(String interfaceName, DubboClientRequestAdapter clientRequestAdapter) {
        SpanId spanId;
        try {
            Field field = ClientRequestInterceptor.class.getDeclaredField("clientTracer");
            field.setAccessible(true);
            ClientTracer clientTracer = (ClientTracer) field.get(clientRequestInterceptor);
            spanId = clientTracer.startNewSpan(clientRequestAdapter.getSpanName());
            if (spanId == null)
                throw new NullPointerException("======spanId is null======");
            return spanId;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

}