package com.bdfint.bdtrace;

import brave.Span;
import brave.Tracer;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.okhttp3.OkHttpSender;


@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class ZipkinFilter implements Filter {

    public ZipkinFilter() {
        System.out.println("ZipkinFilter Init");
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();


        String serviceName = rpcContext.getMethodName();
        Tracer tracer = Tracer.newBuilder()
                .localServiceName(serviceName)
                .reporter(reporter)
                .build();

        TraceContextOrSamplingFlags contextOrFlags = extractor.extract(rpcContext);
        Span span = contextOrFlags.context() != null
                ? tracer.joinSpan(contextOrFlags.context())
                : tracer.newTrace(contextOrFlags.samplingFlags());

        injector.inject(span.context(), rpcContext);
        span.start();

        Result result = invoker.invoke(invocation);

        span.finish();

        return result;
    }


    private static final AsyncReporter<zipkin.Span> reporter = AsyncReporter.builder(OkHttpSender.create("192.168.8.174:9410")).build();


    private static final TraceContext.Extractor<RpcContext> extractor = Propagation.B3_STRING.extractor(new Propagation.Getter<RpcContext, String>() {
        public String get(RpcContext carrier, String key) {
            return carrier.getAttachment(key);
        }
    });

    private static final TraceContext.Injector<RpcContext> injector = Propagation.B3_STRING.injector(new Propagation.Setter<RpcContext, String>() {
        public void put(RpcContext carrier, String key, String value) {
            carrier.setAttachment(key, value);
        }
    });
}
