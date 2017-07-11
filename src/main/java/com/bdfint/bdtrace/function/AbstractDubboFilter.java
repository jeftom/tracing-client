package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.chain.ReaderChain;
import com.bdfint.bdtrace.chain.sampler.*;
import com.bdfint.bdtrace.functionable.FilterTemplate;
import com.bdfint.bdtrace.functionable.ServerTraceIgnoredBehaviors;
import com.bdfint.bdtrace.functionable.ServiceInfoProvidable;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.SpanId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public abstract class AbstractDubboFilter implements Filter, FilterTemplate {
    protected final static ParentServiceNameMapCacheProcessor cacheProcessor = new ParentServiceNameMapCacheProcessor();
    //for sampler
    final static AbstractSamplerConfigReader[] readers = {
            new MethodSamplerConfigReader(),
            new ServiceSamplerConfigReader(),
            new GroupSamplerConfigReader(),
            new ApplicationSamplerConfigReader(),
            new GlobalSamplerConfigReader()
    };
    final static ReaderChain chain = new SamplerConfigReaderChain();
    private static final Logger logger = LoggerFactory.getLogger(AbstractDubboFilter.class);

    static {
        chain.addReaders(readers);
    }

    // brave and interceptors
//    protected ClientRequestInterceptor clientRequestInterceptor;
//    protected ClientResponseInterceptor clientResponseInterceptor;
//    protected ServerRequestInterceptor serverRequestInterceptor;
//    protected ServerResponseInterceptor serverResponseInterceptor;
    // interface dependencies and impl.

    //不依赖于初始化参数，是无状态的类
    protected AnnotatedImpl annotated = new AnnotatedImpl();
    protected ServerTraceIgnoredBehaviors noneTraceBehaviors = new NoneTraceBehaviorsImpl();
    protected ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
    //field
//    protected StatusEnum status = StatusEnum.OK;
//    protected String spanName;
//    protected Throwable exception;
//    SamplerResult samplerResult = new SamplerResult();
    protected ServiceInfoProvidable samplerInfoProvider = new SamplerInfoProvider();

    @Override
    public void initField(Invoker<?> invoker, Invocation invocation) {
        String serviceName = serviceInfoProvidable.serviceName(invoker, invocation);
        String spanName = serviceInfoProvidable.spanName(invoker, invocation);
        setInterceptors(serviceName);
//        Test.testServiceName(serviceName);
    }

    /**
     * 事实证明 在同一application下的dubbo环境中，filter是<b>单例</b>的，至少在只有一对一的情况下是这样子的。。
     *
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {//build template
        logger.debug("当前的dubbo filter hashcode={}", this);
//
//        //采样处理
//        chain.reset();
//        readers[0].setInterface(samplerInfoProvider.methodName(invoker, invocation));
//        readers[1].setInterface(samplerInfoProvider.uniqueInterfaceKey(invoker, invocation));
//        readers[2].setInterface(samplerInfoProvider.group());
//        readers[3].setInterface(samplerInfoProvider.applicationName());
//        readers[4].setInterface(DubboTraceConst.GLOBAL_SAMPLER_KEY);
//
//        chain.readForAll(samplerResult);
//        if (!samplerResult.isSampled()) {
//            return invoker.invoke(invocation);
//        }

        //ignore this trace when sample is 0 or null
//        if (noneTraceBehaviors.ignore(invoker, invocation))
//            return invoker.invoke(invocation);

        //template method
//        initField(invoker, invocation);
        Brave brave = null;
        String serviceName = serviceInfoProvidable.serviceName(invoker, invocation);
        String spanName = serviceInfoProvidable.spanName(invoker, invocation);
        brave = setInterceptors(serviceName);


        //template method
        if (preHandle(invoker, invocation, serviceName, spanName, brave)) {
            return invoker.invoke(invocation);
        }

        //invoke
        Result result = null;
        Throwable exception = null;
        StatusEnum status = StatusEnum.OK;
        try {
            result = invoker.invoke(invocation);
            exception = handleAndGetException(result, serviceName, status); //template method
        } catch (RpcException e) {
            status = StatusEnum.ERROR;
            exception = e;
        } finally {
            afterHandle(invocation, status, exception, brave);//template method
            return result;
        }
    }

    protected void setParentServiceName(String serviceName, SpanId spanId) {
        cacheProcessor.setParentServiceName(serviceName, spanId);
    }

    protected void getParentServiceNameAndSetBrave(String serviceName, SpanId spanId) {
        LocalSpanId localSpanId = cacheProcessor.getParentLocalSpanId(spanId);

        //if there is no CACHE
        if (localSpanId != null) {
            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
            setInterceptors(parentSpanServiceName);
        }

    }

    protected Brave setInterceptors(String serviceName) {
        Brave brave = null;
        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
            return brave;
        }
        return brave;
    }

    /**
     * 处理remote method内部异常
     *
     * @param result
     * @param status
     * @return
     */
    public Throwable handleAndGetException(Result result, String serviceName, StatusEnum status) {
        if (result.hasException()) {
            status = StatusEnum.ERROR;
            logger.error("======================远程方法系统异常=====================");
            logger.error("serviceName: {}, class: {}", serviceName, this);
            logger.error("Exception info {}", result.getException());
            logger.error("======================远程方法系统异常=====================");
            logger.error("");
            return result.getException();
        }
        return null;
    }

}
