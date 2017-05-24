package com.bdfint.bdtrace.functionable;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin 需要上下文环境中调用
 */
public interface ConsumerInfoProvidable {

    /**
     * 获取应用名
     *
     * @return
     */
    String applicationName();

    /**
     * 获取服务名
     *
     * @return
     */
    String serviceName();

    /**
     * 获取客户端地址，需要上下文环境中调用
     *
     * @return
     */
    String clientAddress();

}
