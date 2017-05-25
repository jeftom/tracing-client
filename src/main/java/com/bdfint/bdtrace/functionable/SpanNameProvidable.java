package com.bdfint.bdtrace.functionable;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public interface SpanNameProvidable {

    /**
     * 以xxxService.xxxMethod()作为spanName
     *
     * @return
     */
    String spanName(Invoker<?> invoker, Invocation invocation);
}
