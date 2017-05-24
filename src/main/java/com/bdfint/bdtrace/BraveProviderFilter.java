package com.bdfint.bdtrace;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.AbstractDubboFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Activate(group = {Constants.PROVIDER})
public class BraveProviderFilter extends AbstractDubboFilter {
    private static final Logger logger = LoggerFactory.getLogger(BraveProviderFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        IgnoreFilter ingoreFilter = new IgnoreFilter(invoker, invocation).invoke();
        if (ingoreFilter.is()) return invoker.invoke(invocation);
        String interfaceName = ingoreFilter.getInterfaceName();
        status = ingoreFilter.getStatus();

        setInterceptors(interfaceName);

        logger.debug(interfaceName + " provider execute");

        DubboServerRequestAdapter dubboServerRequestAdapter = new DubboServerRequestAdapter(invocation.getAttachments(), interfaceName);

        setParentServiceName(interfaceName, dubboServerRequestAdapter);
        serverRequestInterceptor.handle(dubboServerRequestAdapter);
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
            serverResponseInterceptor.handle(new DubboServerResponseAdapter(status, msg, System.currentTimeMillis()));
            return result;
        }
    }


}