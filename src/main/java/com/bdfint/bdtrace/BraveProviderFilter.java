package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.BraveFactory;
import com.bdfint.bdtrace.function.ParentServiceNameMapCacheProcessor;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ServerRequestInterceptor;
import com.github.kristofa.brave.ServerResponseInterceptor;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Activate(group = {Constants.PROVIDER})
public class BraveProviderFilter implements Filter {
    protected static final ParentServiceNameMapCacheProcessor cacheProcessor = new ParentServiceNameMapCacheProcessor();
    private static final Logger logger = LoggerFactory.getLogger(BraveProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        ServerRequestInterceptor serverRequestInterceptor;
        ServerResponseInterceptor serverResponseInterceptor;
        Brave brave = null;
        String spanName;
        String serviceName;
        serviceName = invoker.getInterface().getCanonicalName();
        spanName = invocation.getMethodName();
        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
            return invoker.invoke(invocation);
        }
        serverRequestInterceptor = brave.serverRequestInterceptor();
        serverResponseInterceptor = brave.serverResponseInterceptor();

        DubboServerRequestAdapter dubboServerRequestAdapter = new DubboServerRequestAdapter(invocation.getAttachments(), spanName);
        SpanId spanId = dubboServerRequestAdapter.getTraceData().getSpanId();
        if (spanId == null)// sample is 0 or null
            return invoker.invoke(invocation);
        setParentServiceName(serviceName, spanId);
        serverRequestInterceptor.handle(dubboServerRequestAdapter);

        Result result = null;
        result = invoker.invoke(invocation);
        serverResponseInterceptor.handle(new DubboServerResponseAdapter(StatusEnum.OK, null, 0));
        return result;
    }

    protected void setParentServiceName(String serviceName, SpanId spanId) {
        cacheProcessor.setParentServiceName(serviceName, spanId);
    }
}