package com.bdfint.bdtrace.support;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public class BraveFactory {
    private static final Logger logger = LoggerFactory.getLogger(BraveFactory.class);
    private static AsyncReporter<zipkin.Span> sReporter = new BraveFactory().reporter;
    private static Sampler sSampler = new BraveFactory().sampler;
    private static Map<String, Brave> cache = new HashMap<String, Brave>();
    private final Sender sender = OkHttpSender.create(Configuration.getZipkinUrl());
    private final AsyncReporter<zipkin.Span> reporter = AsyncReporter.builder(sender).build();
    private final Sampler sampler = Sampler.create(Configuration.getSampler());

    public static Brave nullableInstance(String serviceName) {

        Brave brave = null;
        try {
            if (cache.containsKey(serviceName)) {
                brave = cache.get(serviceName);
            }
            brave = new Brave.Builder(serviceName).reporter(sReporter).traceSampler(sSampler).build();
            if (brave != null)
                cache.put(serviceName, brave);
        } catch (Exception e) {
            logger.info("异常信息：", e);
        } finally {
            return brave;
        }
    }
}
