package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.functionable.NoneTraceBehaviors;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public class NoneTraceBehaviorsImpl implements NoneTraceBehaviors {
    @Override
    public boolean ignoreTrace(Invoker<?> invoker, Invocation invocation) {
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
            return true;
        }
//        status = StatusEnum.OK;
        if ("0".equals(invocation.getAttachment(DubboTraceConst.SAMPLED))
                || "false".equalsIgnoreCase(invocation.getAttachment(DubboTraceConst.SAMPLED))
                || "null".equalsIgnoreCase(invocation.getAttachment(DubboTraceConst.SAMPLED))) {
            return true;
        }
        return false;
    }
}
