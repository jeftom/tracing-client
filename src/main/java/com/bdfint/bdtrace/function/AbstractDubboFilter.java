package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.functionable.*;
import com.bdfint.bdtrace.test.Test;
import com.github.kristofa.brave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public abstract class AbstractDubboFilter implements Filter, FilterTemplate {
    /**
     * cache for parent service
     */
    protected static final ThreadLocal<Map<Long, LocalSpanId>> callTreeCache = new ThreadLocal<Map<Long, LocalSpanId>>() {
        @Override
        protected Map<Long, LocalSpanId> initialValue() {
            return new HashMap<>();
        }
    };
    private static final Logger logger = LoggerFactory.getLogger(AbstractDubboFilter.class);
    private static AtomicLong threadName = new AtomicLong(0);
    // brave and interceptors
    protected Brave brave = null;
    protected ClientRequestInterceptor clientRequestInterceptor;
    protected ClientResponseInterceptor clientResponseInterceptor;
    protected ServerRequestInterceptor serverRequestInterceptor;
    protected ServerResponseInterceptor serverResponseInterceptor;

    // interface dependencies and impl.
    protected Annotated annotated = new AnnotatedImpl();
    protected NoneTraceBehaviors noneTraceBehaviors = new NoneTraceBehaviorsImpl();
    protected ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
    protected IAttachmentTransmittable transmitter;

    //field
    protected StatusEnum status = StatusEnum.OK;
    protected String serviceName;
    protected String spanName;
    protected String errMsg = null;

    @Override
    public void initField(Invoker<?> invoker, Invocation invocation) {
        serviceName = serviceInfoProvidable.serviceName(invoker, invocation);
        spanName = serviceInfoProvidable.spanName(invoker, invocation);
        setInterceptors(serviceName);
        Test.testServiceName(serviceName);
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {//build template
        //ignore this trace when sample is 0 or null
        if (noneTraceBehaviors.ignoreTrace(invoker, invocation))
            return invoker.invoke(invocation);

        //template method
        initField(invoker, invocation);

        //template method
        if (preHandle(invoker, invocation)) {
            return invoker.invoke(invocation);
        }
        Test.testServiceName(serviceName);
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            errMsg = handleException(result, serviceName); //template method
        } catch (RpcException e) {
            status = StatusEnum.ERROR;
            errMsg = Arrays.toString(e.getStackTrace());
            throw new RuntimeException(e.getCause());
        } finally {
            afterHandle(invocation);//template method
            Test.testServiceName(serviceName);
            return result;
        }
    }

    protected void setParentServiceName(String interfaceName, SpanId spanId) {
        callTreeCache.get().put(spanId.spanId, new LocalSpanId(spanId, interfaceName, interfaceName, Thread.currentThread()));
//        if (callTreeCache.get().size() == 0) {
//            long andIncrement = threadName.getAndIncrement();
//            logger.info(andIncrement);
//            Thread.currentThread().setName(String.valueOf(andIncrement));
//            logger.info("set in " + Thread.currentThread());
//        } else {
//            if (spanId.nullableParentId() != null)
//                logger.info("WARNING-WARNING when set parent service name in provider");
//        }
    }

    protected void getParentServiceNameAndSetBrave(String interfaceName, SpanId spanId) {
        if (callTreeCache.get().size() == 0) {
            if (spanId.nullableParentId() != null) // 当不是根节点时，consumer端应该获取到父节点的缓存
                logger.warn("WARNING when get parent service name");
        } else {
            LocalSpanId localSpanId = callTreeCache.get().get(spanId.parentId);//获取父节点缓存，为了使当前节点接收父节点的依赖
//            logger.info(Thread.currentThread() + "<=>" + localSpanId.getCurrentThread());

            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
//            Test.testForParentChildrenRelationship(parentSpanServiceName, interfaceName);
            setInterceptors(parentSpanServiceName);

        }
    }

    protected void setInterceptors(String serviceName) {
        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
            logger.error("brave 初始化失败，serviceName:{}", serviceName);
            return;
        }

//        Brave brave = BraveFactory.nullableInstance(serviceName);
        this.clientRequestInterceptor = brave.clientRequestInterceptor();
        this.clientResponseInterceptor = brave.clientResponseInterceptor();
        this.serverRequestInterceptor = brave.serverRequestInterceptor();
        this.serverResponseInterceptor = brave.serverResponseInterceptor();
    }

    /**
     * 处理remote method内部异常
     *
     * @param result
     * @return
     */
    public String handleException(Result result, String serviceName) {
        String msg = null;
        if (result.hasException()) {
            logger.warn("======================TRACING CLIENT Exception=====================");
//                result.getException().printStackTrace();
            msg = Arrays.toString(result.getException().getStackTrace());
            logger.warn("serviceName: {}, class: {}", serviceName, this);
            logger.warn("Exception info {}", result.getException());
            status = StatusEnum.ERROR;
        }
        return msg;
    }

}