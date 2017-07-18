package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.*;
import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.chain.ReaderChain;
import com.bdfint.bdtrace.chain.sampler.*;
import com.bdfint.bdtrace.functionable.FilterTemplate;
import com.bdfint.bdtrace.functionable.ServerTraceIgnoredBehaviors;
import com.bdfint.bdtrace.functionable.ServiceInfoProvidable;
import com.github.kristofa.brave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public abstract class AbstractDubboFilter implements Filter, FilterTemplate {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDubboFilter.class);
    //    protected ParentServiceNameCacheProcessing cacheProcessor = new ParentServiceNameThreadLocalCacheProcessor();
    protected static ParentServiceNameThreadLocalCacheProcessor cacheProcessor = new ParentServiceNameThreadLocalCacheProcessor();
    //for sampler
    static AbstractSamplerConfigReader[] readers = {
            new MethodSamplerConfigReader(),
            new ServiceSamplerConfigReader(),
            new GroupSamplerConfigReader(),
            new ApplicationSamplerConfigReader(),
            new GlobalSamplerConfigReader()
    };
    static ReaderChain chain = new SamplerConfigReaderChain();
    private static AtomicLong threadName = new AtomicLong(0);
    // brave and interceptors
    protected Brave brave = null;
    protected ClientRequestInterceptor clientRequestInterceptor;
    protected ClientResponseInterceptor clientResponseInterceptor;
    protected ServerRequestInterceptor serverRequestInterceptor;
    protected ServerResponseInterceptor serverResponseInterceptor;
    // interface dependencies and impl.
    protected AnnotatedImpl annotated = new AnnotatedImpl();
    protected ServerTraceIgnoredBehaviors noneTraceBehaviors = new NoneTraceBehaviorsImpl();
    protected ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
    protected ServiceInfoProvidable samplerInfoProvider = new SamplerInfoProvider();
    //field
    protected StatusEnum status = StatusEnum.OK;
    protected String serviceName;
    protected String spanName;
    protected Throwable exception;
    SamplerResult samplerResult = new SamplerResult();

    public AbstractDubboFilter() {
        chain.addReaders(readers);
    }

    @Override
    public void initField(Invoker<?> invoker, Invocation invocation) {
        serviceName = serviceInfoProvidable.serviceName(invoker, invocation);
        spanName = serviceInfoProvidable.spanName(invoker, invocation);
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
        initField(invoker, invocation);

        //template method
        if (preHandle(invoker, invocation)) {
            return invoker.invoke(invocation);
        }

        //invoke
        Result result = null;
        try {
            result = invoker.invoke(invocation);
            exception = handleAndGetException(result, serviceName); //template method
        } catch (RpcException e) {
            status = StatusEnum.ERROR;
//            exception = new Throwable("dubbo RPC调用异常", e);
            exception = e;
        } finally {
            afterHandle(invocation);//template method
            return result;
        }
    }

    protected void setParentServiceName(String serviceName, SpanId spanId) {
        cacheProcessor.setParentServiceName(serviceName, spanId, null);
    }

    protected void getParentServiceNameAndSetBrave(String serviceName, SpanId spanId) {
        LocalSpanId localSpanId = cacheProcessor.getParentLocalSpanId(spanId, null);

        //if there is no CACHE
        if (localSpanId != null) {
            String parentSpanServiceName = localSpanId.getParentSpanServiceName();
            setInterceptors(parentSpanServiceName);
        }
    }

    protected void setInterceptors(String serviceName) {
        if ((brave = BraveFactory.nullableInstance(serviceName)) == null) {//理论上不会为空
            return;
        }

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
            status = StatusEnum.ERROR;
            logger.error("======================TRACING CLIENT Exception=====================");
            logger.error("serviceName: {}, class: {}", serviceName, this);
            logger.error("Exception info {}", result.getException());
            logger.error("远程方法系统异常");
            logger.error("======================TRACING CLIENT Exception=====================");
            logger.error("");
//            return new Throwable("远程方法系统异常", result.getException());
//                result.getException().printStackTrace();
            return result.getException();
        }
        return null;
    }

    private void setSampled(DubboClientRequestAdapter clientRequestAdapter, boolean sampled) {
        try {
            Field field = ClientRequestInterceptor.class.getDeclaredField("clientTracer");
            field.setAccessible(true);
            ClientTracer clientTracer = (ClientTracer) field.get(clientRequestInterceptor);
            try {
                Method serverSpan = ClientTracer.class.getDeclaredMethod("currentServerSpan", ServerSpanThreadBinder.class);
                serverSpan.setAccessible(true);
                ServerSpanThreadBinder invoke = ((ServerSpanThreadBinder) serverSpan.invoke(clientTracer));
                ServerSpan currentServerSpan = invoke.getCurrentServerSpan();
                Method spanId = ServerSpan.class.getDeclaredMethod("spanId");
                spanId.setAccessible(true);
                SpanId Id = ((SpanId) spanId.invoke(currentServerSpan));


            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            logger.info("异常信息：", e);
        } catch (IllegalAccessException e) {
            logger.info("异常信息：", e);
        }
    }
}
