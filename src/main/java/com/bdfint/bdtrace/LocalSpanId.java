package com.bdfint.bdtrace;

import com.github.kristofa.brave.SpanId;

/**
 * @author heyb
 * @date 2017/5/19.
 * @desriptioin
 */
public class LocalSpanId {

    private SpanId parentSpanId;
    private String parentSpanName;
    private String parentSpanServiceName;
    private long time;
    private Thread currentThread;

    public LocalSpanId(SpanId parentSpanId, String parentSpanName, String parentSpanServiceName, Thread currentThread) {
        this.parentSpanId = parentSpanId;
        this.parentSpanName = parentSpanName;
        this.parentSpanServiceName = parentSpanServiceName;
        this.currentThread = currentThread;
    }

    public Thread getCurrentThread() {
        return currentThread;
    }

    public void setCurrentThread(Thread currentThread) {
        this.currentThread = currentThread;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public SpanId getParentSpanId() {
        return parentSpanId;
    }

    public void asParentSpanId(SpanId parentSpanId) {
        this.parentSpanId = parentSpanId;
    }

    public String getParentSpanName() {
        return parentSpanName;
    }

    public void setParentSpanName(String parentSpanName) {
        this.parentSpanName = parentSpanName;
    }

    public String getParentSpanServiceName() {
        return parentSpanServiceName;
    }

    public void setParentSpanServiceName(String parentSpanServiceName) {
        this.parentSpanServiceName = parentSpanServiceName;
    }
}
