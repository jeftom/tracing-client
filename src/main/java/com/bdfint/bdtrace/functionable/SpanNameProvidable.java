package com.bdfint.bdtrace.functionable;

/**
 * @author heyb
 * @date 2017/5/24.
 * @desriptioin
 */
public interface SpanNameProvidable {

    /**
     * 以xxxService.xxxMethod()作为spanName
     *
     * @return
     */
    String spanName();
}
