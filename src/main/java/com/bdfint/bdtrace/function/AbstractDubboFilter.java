package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.bean.BravePack;
import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.chain.ReaderChain;
import com.bdfint.bdtrace.chain.sampler.*;
import com.bdfint.bdtrace.functionable.FilterTemplate;
import com.bdfint.bdtrace.functionable.ParentServiceNameCacheProcessing;
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
    protected final static ParentServiceNameCacheProcessing CACHE_PROCESSOR = new ParentServiceNameThreadLocalCacheProcessor();
    static final Exception EXCEPTION = new IllegalStateException("不知道发生了什么");
    //for sampler
    private final static AbstractSamplerConfigReader[] READERS = {
            new MethodSamplerConfigReader(),
            new ServiceSamplerConfigReader(),
            new GroupSamplerConfigReader(),
            new ApplicationSamplerConfigReader(),
            new GlobalSamplerConfigReader()
    };
    private final static ReaderChain CHAIN = new SamplerConfigReaderChain();
    private static final Logger logger = LoggerFactory.getLogger(AbstractDubboFilter.class);

    static {
        CHAIN.addReaders(READERS);
    }

    //不依赖于初始化参数，是无状态的类
    protected AnnotatedImpl annotated = new AnnotatedImpl();
    protected ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
    protected ServerTraceIgnoredBehaviors noneTraceBehaviors = new NoneTraceBehaviorsImpl();
    protected ServiceInfoProvidable samplerInfoProvider = new SamplerInfoProvider();

    @Override
    public void initField(Invoker<?> invoker, Invocation invocation) {
//        String serviceName = serviceInfoProvidable.serviceName(invoker, invocation);
//        String spanName = serviceInfoProvidable.spanName(invoker, invocation);
//        brave(serviceName);
    }

    /**
     * 事实证明 在同一application下的dubbo环境中，filter是<b>单例</b>的
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
//        CHAIN.reset();
//        READERS[0].setInterface(samplerInfoProvider.methodName(invoker, invocation));
//        READERS[1].setInterface(samplerInfoProvider.uniqueInterfaceKey(invoker, invocation));
//        READERS[2].setInterface(samplerInfoProvider.group());
//        READERS[3].setInterface(samplerInfoProvider.applicationName());
//        READERS[4].setInterface(DubboTraceConst.GLOBAL_SAMPLER_KEY);
//
//        CHAIN.readForAll(samplerResult);
//        if (!samplerResult.isSampled()) {
//            return invoker.invoke(invocation);
//        }

        //ignore this trace when sample is 0 or null
//        if (noneTraceBehaviors.ignore(invoker, invocation))
//            return invoker.invoke(invocation);

        BravePack bravePack = new BravePack();
        Result result = new RpcResult(EXCEPTION);
        Throwable exception = null;
        StatusEnum status = StatusEnum.OK;
        String serviceName;
        String spanName;

        //template method
        //initField(invoker, invocation);
        serviceName = serviceInfoProvidable.serviceName(invoker, invocation);
        spanName = serviceInfoProvidable.spanName(invoker, invocation);
        brave(serviceName, bravePack);//set brave in bravePack

        //template method
        if (bravePack.brave == null
                || preHandle(invoker, invocation, serviceName, spanName, bravePack)) {
            return invoker.invoke(invocation);
        }
//        if (!CACHE_PROCESSOR.outOfSpace()) {
//            logger.warn("缓存容量已达1000,停止tracing.");
//            return invoker.invoke(invocation);
//        }

        //invoke
        try {
            result = invoker.invoke(invocation);
            exception = handleAndGetException(result, serviceName, status); //template method
        } catch (RpcException e) {
            status = StatusEnum.ERROR;
            exception = e;
            throw e;
        } finally {
            afterHandle(invocation, status, exception, bravePack.brave);//template method
            return result;
        }
    }

    protected void setParentServiceName(String serviceName, SpanId spanId, Brave brave) {
        CACHE_PROCESSOR.setParentServiceName(serviceName, spanId, brave);
    }

    protected void getParentServiceNameAndSetBrave(String serviceName, SpanId spanId, BravePack bravePack) {
        LocalSpanId localSpanId = CACHE_PROCESSOR.getParentLocalSpanId(spanId, serviceName);

        //if there is no CACHE
        if (localSpanId != null) {
            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
            brave(parentSpanServiceName, bravePack);
//            bravePack.brave = localSpanId.getBrave();
        }
    }

    protected Brave brave(String serviceName, BravePack bravePack) {
        if ((bravePack.brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
//
        }
        return bravePack.brave;
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
            logger.error("======================dubbo远程方法异常=====================");
            logger.error("serviceName: {}, class: {}", serviceName, this);
            logger.error("Exception info {}", result.getException());
            logger.error("======================dubbo远程方法异常=====================");
            logger.error("");
            return result.getException();
        }
        return null;
    }

}
