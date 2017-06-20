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
public class ServiceInfoProvider implements ServiceInfoProvidable {
    private static final Logger logger = LoggerFactory.getLogger(ServiceInfoProvider.class);

    @Override
    public String applicationName() {
        // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
        RpcContext context = RpcContext.getContext();
//        if (!context.isConsumerSide()) {// maybe to do
//            logger.error("IllegalStatementException: 需要consumer上下文");
//            return "";
//        }
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
        return RpcContext.getContext().getUrl().getParameter("group","0");
    }

    /**
     * 获取version 值
     *
     * @return
     */
    @Override
    public String version() {
        return RpcContext.getContext().getUrl().getParameter("version", "1.0");
    }

    /**
     * 方法名
     *
     * @return
     */
    @Override
    public String methodName() {
        RpcContext context = RpcContext.getContext();
        String methodName = context.getMethodName();
        return methodName;
    }

    @Override
    public String serviceName(Invoker<?> invoker, Invocation invocation) {
//
//
//        RpcContext context = RpcContext.getContext();
//        String serviceInterface = context.getUrl().getServiceKey();
//
//        int i;
//        if ((i = serviceInterface.lastIndexOf(".")) > 0) {
//            String simpleName = serviceInterface.substring(i, serviceInterface.length());
//            return applicationName() + "-" + simpleName;
//        }
//        return serviceInterface;
        return applicationName() + "-" + invoker.getInterface().getSimpleName();
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
        String key = group + "/" + serviceInterface + ":" + version;

        logger.debug(key);
        RpcContext context = RpcContext.getContext();
        String methodName = context.getMethodName();
        String name = invoker.getInterface().getName();
        key = name + "." + methodName;
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
        return methodName();
    }
}
