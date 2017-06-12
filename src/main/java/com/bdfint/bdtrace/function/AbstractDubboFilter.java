package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.functionable.FilterTemplate;
import com.bdfint.bdtrace.functionable.NoneTraceBehaviors;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
import com.bdfint.bdtrace.functionable.ServiceInfoProvidable;
import com.bdfint.bdtrace.test.Test;
import com.github.kristofa.brave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public abstract class AbstractDubboFilter implements Filter, FilterTemplate {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDubboFilter.class);
    private static AtomicLong threadName = new AtomicLong(0);
    // brave and interceptors
    protected Brave brave = null;
    protected ClientRequestInterceptor clientRequestInterceptor;
    protected ClientResponseInterceptor clientResponseInterceptor;
    protected ServerRequestInterceptor serverRequestInterceptor;
    protected ServerResponseInterceptor serverResponseInterceptor;

    // interface dependencies and impl.
    protected AnnotatedImpl annotated = new AnnotatedImpl();
    protected NoneTraceBehaviors noneTraceBehaviors = new NoneTraceBehaviorsImpl();
    protected ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
    protected ParentServiceNameCacheProcessing cacheProcessor = new ParentServiceNameCacheProcessor();

    //field
    protected StatusEnum status = StatusEnum.OK;
    protected String serviceName;
    protected String spanName;
    //    protected String errType = null;
//    protected String errMsg = null;
    protected Throwable exception;

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

        //invoke
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            exception = handleAndGetException(result, serviceName); //template method
        } catch (RpcException e) {
            status = StatusEnum.ERROR;
            exception = new Throwable("dubbo RPC调用异常", e);
            throw new RuntimeException(e.getCause());
        } finally {
            afterHandle(invocation);//template method
            Test.testServiceName(serviceName);
            return result;
        }
    }

    protected void setParentServiceName(String serviceName, SpanId spanId) {
        cacheProcessor.setParentServiceName(serviceName, spanId);
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

    protected void getParentServiceNameAndSetBrave(String serviceName, SpanId spanId) {
        LocalSpanId localSpanId = cacheProcessor.getParentLocalSpanId(spanId);

        //if there is no CACHE
        if (localSpanId != null) {
            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
            Test.testForParentChildrenRelationship(parentSpanServiceName, serviceName, logger);
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
    public Throwable handleAndGetException(Result result, String serviceName) {
        if (result.hasException()) {
            logger.error("======================TRACING CLIENT Exception=====================");
//                result.getException().printStackTrace();
            logger.error("serviceName: {}, class: {}", serviceName, this);
            logger.error("Exception info {}", result.getException());
            status = StatusEnum.ERROR;
            return new Throwable("远程方法系统异常", result.getException());
        }
        return null;
    }
}
