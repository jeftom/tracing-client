package com.bdfint.bdtrace.functionable;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.bdfint.bdtrace.bean.BravePack;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.github.kristofa.brave.Brave;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public interface FilterTemplate {

//    void ignoreFilt();

    void initField(Invoker<?> invoker, Invocation invocation);


    /**
     * MUST check return type
     *
     * @param invoker
     * @param invocation
     * @param serviceName
     * @param spanName
     * @param bravePack
     * @return whether to return #Result
     */
    boolean preHandle(Invoker<?> invoker, Invocation invocation, String serviceName, String spanName, BravePack bravePack);

//    void invoke();

    Throwable handleAndGetException(Result result, String serviceName, StatusEnum status);

    void afterHandle(Invocation invocation, StatusEnum statusEnum, Throwable exception, Brave brave);
}
