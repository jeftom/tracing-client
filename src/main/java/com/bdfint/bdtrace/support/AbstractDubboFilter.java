package com.bdfint.bdtrace.support;

import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.AnnotatedImpl;
import com.bdfint.bdtrace.LocalSpanId;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.bean.DubboTraceConst;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.test.Test;
import com.github.kristofa.brave.*;
import com.github.kristofa.brave.SpanId;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public abstract class AbstractDubboFilter implements Filter {
    protected static final ThreadLocal<Map<Long, LocalSpanId>> callTreeCache = new ThreadLocal<Map<Long, LocalSpanId>>() {
        @Override
        protected Map<Long, LocalSpanId> initialValue() {
            return new HashMap<Long, LocalSpanId>();
        }
    };

    protected ClientRequestInterceptor clientRequestInterceptor;
    protected ClientResponseInterceptor clientResponseInterceptor;
    protected ServerRequestInterceptor serverRequestInterceptor;
    protected ServerResponseInterceptor serverResponseInterceptor;

    protected Annotated annotated = new AnnotatedImpl();

    public abstract Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;

    @Autowired
    protected IAttachmentTransmittable transmitter;

    static AtomicLong threadName = new AtomicLong(0);
    LocalSpanThreadBinder spanThreadBinder;

    protected void setParentServiceName(String interfaceName, DubboServerRequestAdapter dubboServerRequestAdapter) {
        SpanId spanId = dubboServerRequestAdapter.getTraceData().getSpanId();
        callTreeCache.get().put(spanId.spanId, new LocalSpanId(spanId, interfaceName, interfaceName, Thread.currentThread()));
//        if (callTreeCache.get().size() == 0) {
//            long andIncrement = threadName.getAndIncrement();
//            System.out.println(andIncrement);
//            Thread.currentThread().setName(String.valueOf(andIncrement));
//            System.out.println("set in " + Thread.currentThread());
//        } else {
//            if (spanId.nullableParentId() != null)
//                System.out.println("WARNING-WARNING when set parent service name in provider");
//        }
    }

    protected void getParentServiceNameAndSetBrave(String interfaceName, SpanId spanId) {
        if (callTreeCache.get().size() == 0) {
            System.out.println("WARNING when get parent service name");
        } else {
            LocalSpanId localSpanId = callTreeCache.get().get(spanId.parentId);//获取父节点缓存，为了使当前节点接收父节点的依赖
//            System.out.println(Thread.currentThread() + "<=>" + localSpanId.getCurrentThread());

            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
            Test.testForParentChildrenRelationship(parentSpanServiceName, interfaceName);
            setInterceptors(parentSpanServiceName);

        }
    }

    protected void setInterceptors(String serviceName) {
        Brave brave = BraveFactory.nullableInstance(serviceName);
        this.clientRequestInterceptor = brave.clientRequestInterceptor();
        this.clientResponseInterceptor = brave.clientResponseInterceptor();
        this.serverRequestInterceptor = brave.serverRequestInterceptor();
        this.serverResponseInterceptor = brave.serverResponseInterceptor();
    }


    protected class IgnoreFilter {
        private boolean myResult;
        private Invoker<?> invoker;
        private Invocation invocation;
        private String interfaceName;
        private StatusEnum status;

        public IgnoreFilter(Invoker<?> invoker, Invocation invocation) {
            this.invoker = invoker;
            this.invocation = invocation;
        }

        public boolean is() {
            return myResult;
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public StatusEnum getStatus() {
            return status;
        }

        public IgnoreFilter invoke() {
    /*
     * 监控的 dubbo 服务，不纳入跟踪范围
     */
            if ("com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName())) {
                myResult = true;
                return this;
            }
        /*
         * 调用的方法名 以此作为 span name
         */
            interfaceName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
//        interfaceName = interfaceName + (context.isConsumerSide() ? ".consumer" : ".provider");
        /*
         * provider 应用相关信息
         */
            status = StatusEnum.OK;
            if ("0".equals(invocation.getAttachment(DubboTraceConst.SAMPLED))
                    || "false".equals(invocation.getAttachment(DubboTraceConst.SAMPLED))) {
                myResult = true;
                return this;
            }
            myResult = false;
            return this;
        }
    }
}
