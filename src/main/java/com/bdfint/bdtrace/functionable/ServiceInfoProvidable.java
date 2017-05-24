package com.bdfint.bdtrace.functionable;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin 需要上下文环境中调用
 */
public interface ServiceInfoProvidable extends SpanNameProvidable {

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
    String consumerAddress();

    /**
     * 获取provider 地址
     *
     * @return
     */
    String providerAddress();

}
