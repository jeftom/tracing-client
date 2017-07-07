package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.BraveFactory;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Activate(group = {Constants.CONSUMER})
public class BraveConsumerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(BraveConsumerFilter.class);
    protected Brave brave = null;
    protected ClientRequestInterceptor clientRequestInterceptor;
    protected ClientResponseInterceptor clientResponseInterceptor;

    protected String serviceName;
    protected String spanName;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        serviceName = invoker.getInterface().getCanonicalName();
        spanName = invocation.getMethodName();
        setInterceptors(serviceName);

        DubboClientRequestAdapter clientRequestAdapter = new DubboClientRequestAdapter(invocation.getAttachments(), spanName, serviceName);
        clientRequestInterceptor.handle(clientRequestAdapter);

        Result result = null;
        result = invoker.invoke(invocation);
        afterHandle(invocation);//template method
        return result;
    }

    protected void setInterceptors(String serviceName) {
        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
            return;
        }
        this.clientRequestInterceptor = brave.clientRequestInterceptor();
        this.clientResponseInterceptor = brave.clientResponseInterceptor();
    }

    public void afterHandle(Invocation invocation) {
        final DubboClientResponseAdapter clientResponseAdapter = new DubboClientResponseAdapter(StatusEnum.OK, null, 0);
        clientResponseInterceptor.handle(clientResponseAdapter);
    }
}