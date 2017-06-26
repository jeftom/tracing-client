package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;
import com.bdfint.bdtrace.functionable.ServiceInfoProvidable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public class SamplerInfoProvider implements ServiceInfoProvidable {
    private static final Logger logger = LoggerFactory.getLogger(SamplerInfoProvider.class);

    @Override
    public String applicationName() {
        // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
        RpcContext context = RpcContext.getContext();
        String application = context.getUrl().getParameter("application");
        return application;
    }

    /**
     * 分组
     *
     * @return
     */
    @Override
    public String group() {
        String group = RpcContext.getContext().getUrl().getParameter("group", "0");
        return applicationName() + "." + group;
    }

    /**
     * 获取version 值
     *
     * @return
     */
    @Override
    public String version() {
        String version = RpcContext.getContext().getUrl().getParameter("version", "1.0");
        return version;

    }


    /**
     * 方法名
     *
     * @param invoker
     * @param invocation
     * @return
     */
    @Override
    public String methodName(Invoker<?> invoker, Invocation invocation) {
        RpcContext context = RpcContext.getContext();
        String methodName = context.getMethodName();
        return serviceName(invoker, invocation) + "." + methodName;
    }

    @Override
    public String serviceName(Invoker<?> invoker, Invocation invocation) {
        String s = applicationName() + "-" + invoker.getInterface().getSimpleName();
        return group() + "." + s;
    }

    /**
     * 获取具有唯一性的服务接口名称
     *
     * @param invoker
     * @param invocation
     * @return
     */
    @Override
    public String uniqueInterfaceKey(Invoker<?> invoker, Invocation invocation) {
        URL url = RpcContext.getContext().getUrl();
        String group = url.getParameter("group");
        String version = url.getParameter("version");
        String serviceInterface = url.getServiceInterface();
        String name = invoker.getInterface().getName();

        if (group != null && version != null) {
            String key = group + "/" + serviceInterface + ":" + version;
            logger.debug(key);
            RpcContext context = RpcContext.getContext();
            String methodName = context.getMethodName();
            key = name + "." + methodName;
        }

        return name;
    }

    @Override
    public String consumerAddress() {
        RpcContext context = RpcContext.getContext();
        String addr = context.isConsumerSide() ? context.getLocalAddressString() : context.getRemoteAddressString();
        return addr;
    }

    @Override
    public String providerAddress() {
        RpcContext context = RpcContext.getContext();
        String addr = context.isProviderSide() ? context.getLocalAddressString() : context.getRemoteAddressString();
        return addr;
    }

    @Override
    public String spanName(Invoker<?> invoker, Invocation invocation) {
        return methodName(invoker, invocation);
    }
}
