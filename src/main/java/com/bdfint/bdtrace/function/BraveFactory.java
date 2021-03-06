package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.functionable.GlobalSampler;
import com.bdfint.bdtrace.util.Configuration;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ClientRequestInterceptor;
import com.github.kristofa.brave.ClientResponseInterceptor;
import com.github.kristofa.brave.Sampler;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public class BraveFactory {
    private static final Logger logger = LoggerFactory.getLogger(BraveFactory.class);
    //const
    private final static Sender SENDER = OkHttpSender.create(Configuration.getZipkinUrl());
    private final static AsyncReporter<zipkin.Span> REPORTER = AsyncReporter.builder(SENDER).build();
    private final static GlobalSampler GLOBAL_SAMPLER = new DefaultGlobalSampler();
    private final static Sampler SAMPLER = GLOBAL_SAMPLER.defaultSampler();
    //static
//    private static AsyncReporter<zipkin.Span> sReporter = new BraveFactory().reporter;
//    private static Sampler sSampler = new BraveFactory().sampler;
    private final static LinkedHashMap<String, Brave> CACHE = new LinkedHashMap<>();
    private final static int MAX_SIZE = 20;
    //field
//    private final Sender sender = OkHttpSender.create(Configuration.getZipkinUrl());
//    private final AsyncReporter<zipkin.Span> reporter = AsyncReporter.builder(sender).build();
//    private final Sampler sampler = Sampler.create(Configuration.getSampler());

    /**
     *  serviceName should be the same when using at the same endpoint
     * @param serviceName
     * @return
     */
    public static Brave nullableInstance(String serviceName) {

        Brave brave = null;
        try {
            if (CACHE.containsKey(serviceName)) {
                brave = CACHE.get(serviceName);
            } else {
                brave = new Brave.Builder(serviceName).reporter(REPORTER).traceSampler(SAMPLER).build();
                if (brave != null){
                    CACHE.put(serviceName, brave);
                    // TODO CACHE.size() == MAX_SIZE ? CACHE.
                }
            }
        } catch (Exception e) {
            logger.info("异常信息：", e);
        } finally {
            return brave;
        }
    }

    @Test
    public void test() {
        Brave brave = nullableInstance("hello-service");
        Brave brave2 = nullableInstance("hello-service");
        Assert.assertNotNull(brave);
        System.out.println(brave);
        System.out.println(brave2);
        ClientRequestInterceptor clientRequestInterceptor = brave.clientRequestInterceptor();
        ClientResponseInterceptor clientResponseInterceptor = brave.clientResponseInterceptor();
        Map<String, String> headers = new HashMap<>();
        clientRequestInterceptor.handle(new DubboClientRequestAdapter(headers, "api"));
        System.out.println("mock method calling...");
        clientResponseInterceptor.handle(new DubboClientResponseAdapter(StatusEnum.OK, null, System.currentTimeMillis()));

    }
}
