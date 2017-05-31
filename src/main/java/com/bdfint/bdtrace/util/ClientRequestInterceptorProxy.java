package com.bdfint.bdtrace.util;

import com.bdfint.bdtrace.BraveConsumerFilter;
import com.github.kristofa.brave.*;
import com.twitter.zipkin.gen.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author heyb
 * @date 2017/5/31.
 * @desriptioin
 */
public class ClientRequestInterceptorProxy {
    private static final Logger logger = LoggerFactory.getLogger(BraveConsumerFilter.class);
    private ClientTracer clientTracer;
    private ClientRequestInterceptor clientRequestInterceptor;
    private SpanId spanId;

    public ClientRequestInterceptorProxy() {
    }

    public void setClientTracer(ClientRequestInterceptor clientRequestInterceptor) {
        this.clientRequestInterceptor = clientRequestInterceptor;
        try {
            Field field = ClientRequestInterceptor.class.getDeclaredField("clientTracer");
            field.setAccessible(true);
            clientTracer = (ClientTracer) field.get(clientRequestInterceptor);
        } catch (NoSuchFieldException e) {
            logger.info("异常信息：", e);
        } catch (IllegalAccessException e) {
            logger.info("异常信息：", e);
        }
    }

    public void setSpanId(SpanId spanId) {
        this.spanId = spanId;
    }

    public void handle(ClientRequestAdapter adapter) {

        SpanId context = spanId;
        if (context == null) {
            // We will not trace this request.
            adapter.addSpanIdToRequest(null);
        } else {
            adapter.addSpanIdToRequest(context);
            for (KeyValueAnnotation annotation : adapter.requestAnnotations()) {
                clientTracer.submitBinaryAnnotation(annotation.getKey(), annotation.getValue());
            }
            recordClientSentAnnotations(adapter.serverAddress());
        }
    }

    public void handle(SpanId spanId, ClientRequestInterceptor clientRequestInterceptor, ClientRequestAdapter adapter) {
        this.setClientTracer(clientRequestInterceptor);
        this.setSpanId(spanId);
        this.handle(adapter);
    }

    private void recordClientSentAnnotations(Endpoint serverAddress) {
        if (serverAddress == null) {
            clientTracer.setClientSent();
        } else {
            clientTracer.setClientSent(serverAddress);
        }
    }
}
