package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.BraveFactory;
import com.github.kristofa.brave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Activate(group = {Constants.PROVIDER})
public class BraveProviderFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(BraveProviderFilter.class);
    protected Brave brave = null;
    protected ClientRequestInterceptor clientRequestInterceptor;
    protected ClientResponseInterceptor clientResponseInterceptor;
    protected ServerRequestInterceptor serverRequestInterceptor;
    protected ServerResponseInterceptor serverResponseInterceptor;
    protected String serviceName;
    protected String spanName;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        serviceName = invoker.getInterface().getCanonicalName();
        spanName = invocation.getMethodName();
        setInterceptors(serviceName);

        DubboServerRequestAdapter dubboServerRequestAdapter = new DubboServerRequestAdapter(invocation.getAttachments(), spanName);
        serverRequestInterceptor.handle(dubboServerRequestAdapter);

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
        this.serverRequestInterceptor = brave.serverRequestInterceptor();
        this.serverResponseInterceptor = brave.serverResponseInterceptor();
    }

    public void afterHandle(Invocation invocation) {
        serverResponseInterceptor.handle(new DubboServerResponseAdapter(StatusEnum.OK, null, 0));
    }
}