package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.RpcContext;
import com.bdfint.bdtrace.functionable.ConsumerInfoProvidable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public class ConsumerInfoProvider implements ConsumerInfoProvidable {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerInfoProvider.class);

    @Override
    public String applicationName() {
        // 获取当前服务配置信息，所有配置信息都将转换为URL的参数
        RpcContext context = RpcContext.getContext();
        if (!context.isConsumerSide()) {// maybe to do
            logger.error("IllegalStatementException: 需要consumer上下文");
            return "";
        }
        String application = context.getUrl().getParameter("application");
        return application;
    }

    @Override
    public String serviceName() {
        RpcContext context = RpcContext.getContext();
        String serviceInterface = context.getUrl().getServiceInterface();
        return serviceInterface;
    }

    @Override
    public String clientAddress() {
        RpcContext context = RpcContext.getContext();
        String addr = context.isConsumerSide() ? context.getLocalAddressString() : context.getRemoteAddressString();
        return addr;
    }
}
