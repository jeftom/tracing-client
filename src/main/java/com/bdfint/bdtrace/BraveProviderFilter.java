package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.function.AbstractDubboFilter;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Activate(group = {Constants.PROVIDER})
public class BraveProviderFilter extends AbstractDubboFilter {
    private static final Logger logger = LoggerFactory.getLogger(BraveProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        try {
            result = super.invoke(invoker, invocation);
        } catch (RpcException e) {
            result = invoker.invoke(invocation);
            logger.error("RPC Provider 端异常，忽略本次追踪。", e);
        }
        return result;
    }

    @Override
    public boolean preHandle(Invoker<?> invoker, Invocation invocation) {
        logger.debug(serviceName + " provider execute");

        DubboServerRequestAdapter dubboServerRequestAdapter = new DubboServerRequestAdapter(invocation.getAttachments(), spanName);

        annotated.serverReceived(serviceName, dubboServerRequestAdapter);
        SpanId spanId = dubboServerRequestAdapter.getTraceData().getSpanId();
        if (spanId == null)// sample is 0 or null
            return true;
        setParentServiceName(serviceName, spanId);
        serverRequestInterceptor.handle(dubboServerRequestAdapter);

        return false;
    }

    @Override
    public void afterHandle(Invocation invocation) {
        serverResponseInterceptor.handle(new DubboServerResponseAdapter(status, exception, annotated.sr()));
    }
}