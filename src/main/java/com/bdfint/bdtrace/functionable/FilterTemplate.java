package com.bdfint.bdtrace.functionable;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;

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
     * @return whether to return #Result
     */
    boolean preHandle(Invoker<?> invoker, Invocation invocation);

//    void invoke();

    String handleException(Result result, String serviceName);

    void afterHandle(Invocation invocation);
}
