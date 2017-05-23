package com.bdfint.bdtrace.support;

import zipkin.Annotation;

/**
 * @author heyb
 * @date 2017/5/21.
 * @desriptioin
 */
//TODO
public interface TraceFunctionable {

    /**
     * 计算RPC调用的网络消耗时间
     */
    void calculateNetworkCost();

    /**
     * 获取RPC调用的异常并选择异步写日志
     */
    Throwable getExceptionAndFlush();

    /**
     * 获取RPC调用的异常,返回Throwable 或者 String 或者 StackTrace[] ;并选择异步写日志
     *
     * @param clz   @param T的类对象
     * @param flush 是否选择写日志，(maybe async)
     * @param <T>   返回的异常的包装类型
     * @return 异常
     */
    <T> T getExceptionAndFlush(Class<T> clz, boolean flush);

    /**
     * 展示Call Tree
     */
    void formatTree();

    /**
     * 获取其他的annotation
     */
    void getKvAnnotations();

    Annotation getAnotation();

}
