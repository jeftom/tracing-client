package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.bean.BravePack;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.AbstractDubboFilter;
import com.github.kristofa.brave.Brave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Activate(group = Constants.PROVIDER)
public class BraveProviderFilter extends AbstractDubboFilter {
    private static final Logger logger = LoggerFactory.getLogger(BraveProviderFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        try {
            result = super.invoke(invoker, invocation);
        } catch (RpcException e) {
            logger.error("RPC Provider 端异常，忽略本次追踪。", e);
        }
        return result;
    }

    @Override
    public boolean preHandle(Invoker<?> invoker, Invocation invocation, String serviceName, String spanName, BravePack bravePack) {
        logger.debug(serviceName + " provider execute");

        DubboServerRequestAdapter dubboServerRequestAdapter = new DubboServerRequestAdapter(invocation.getAttachments(), spanName);

        annotated.serverReceived(serviceName, dubboServerRequestAdapter);
//        SpanId spanId = dubboServerRequestAdapter.getTraceData().getSpanId();
//        if (spanId == null)// sample is 0 or null
//            return true;
        setParentServiceName(serviceName, null, bravePack.brave);
        bravePack.brave.serverRequestInterceptor().handle(dubboServerRequestAdapter);

        return false;
    }

    @Override
    public void afterHandle(Invocation invocation, StatusEnum status, Throwable exception, Brave brave) {
        brave.serverResponseInterceptor().handle(new DubboServerResponseAdapter(status, exception, annotated.sr()));
        CACHE_PROCESSOR.clearCache();
    }
}