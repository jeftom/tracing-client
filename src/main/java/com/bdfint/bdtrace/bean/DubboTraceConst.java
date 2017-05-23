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

    String SERVER_RESPONSE_STATUS_CODE = "ser_resp.stat";

    String CLIENT_RESPONSE_STATUS_CODE = "cli_resp.stat";

    String EXCEPTION_MESSAGE = "excp_msg";

    String SPAN_NAME = "dubbo.trace.span_name";


    String CLIENT_TO_SERVER_ELAPSE = "c2s_elapse";
    String REMOTE_METHOD_CALL_ELAPSE = "method_elapse";
    String SERVER_TO_CLIENT_ELAPSE = "s2c_elapse";
    String WHOLE_ELAPSE = "whole_elapse";
}
