package com.bdfint.bdtrace.bean;

/**
 * @author heyb
 * @date 2017/5/15.
 * @desriptioin
 */
public interface DubboTraceConst {
    String SAMPLED = "dubbo.trace.sampled";

    String PARENT_SPAN_ID = "dubbo.trace.parentSpanId";

    String SPAN_ID = "dubbo.trace.spanId";

    String TRACE_ID = "dubbo.trace.traceId";

    String SERVER_RESPONSE_STATUS_CODE = "provider响应状态";

    String CLIENT_RESPONSE_STATUS_CODE = "consumer响应状态";

    String EXCEPTION_MESSAGE = "异常信息";

    String SPAN_NAME = "dubbo.trace.span_name";

    String CLIENT_TO_SERVER_ELAPSE = "c-p网络耗时";

    String REMOTE_METHOD_CALL_ELAPSE = "远程方法耗时";

    String SERVER_TO_CLIENT_ELAPSE = "p-c网络耗时";

    String WHOLE_ELAPSE = "总耗时";

    String UN_SAMPLE_STATUS = "0";

    String SAMPLE_STATUS = "1";
}
