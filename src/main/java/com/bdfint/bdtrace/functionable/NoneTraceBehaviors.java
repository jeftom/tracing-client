package com.bdfint.bdtrace.functionable;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public interface NoneTraceBehaviors {

    /**
     * 决定当前调用是否被忽略
     * @param invoker
     * @param invocation
     * @return
     */
    boolean ignoreTrace(Invoker<?> invoker, Invocation invocation);
}
