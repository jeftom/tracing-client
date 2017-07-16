package com.bdfint.bdtrace.metrics;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heyb
 * @date 2017/7/16.
 * @desriptioin
 */
public class SampleStatImpl implements SampleStat {
    AtomicInteger sampledCount = new AtomicInteger(0);
    AtomicInteger calledCount = new AtomicInteger(0);
    float sampler;
    boolean expectSampler = false;
    long startFrom;
    volatile float errorRange;
    volatile String currentInterface;

    public SampleStatImpl(AtomicInteger sampledCount, AtomicInteger calledCount, float sampler, boolean expectSampler, long startFrom, float errorRange, String currentInterface) {
        this.sampledCount = sampledCount;
        this.calledCount = calledCount;
        this.sampler = sampler;
        this.expectSampler = expectSampler;
        this.startFrom = startFrom;
        this.errorRange = errorRange;
        this.currentInterface = currentInterface;
    }

    @Override
    public boolean isSamplingAsExpect() {
        return false;
    }

    @Override
    public float getErrorRange() {
        return 0;
    }

    @Override
    public void setErrorRange(float range) {

    }

    /**
     * 采样率
     *
     * @return
     */
    @Override
    public float getSampler() {
        return 0;
    }

    @Override
    public void reset() {

    }

    /**
     * 统计采样开始的时间
     *
     * @return ms
     */
    @Override
    public long statFrom() {
        return 0;
    }

    /**
     * 采样的次数
     *
     * @return
     */
    @Override
    public long sampledTimes() {
        return 0;
    }

    /**
     * 调用的总次数
     *
     * @return
     */
    @Override
    public long calledTimes() {
        return 0;
    }

    /**
     * 设置当前的调用上下文，包括interface,sampler,isSampled
     */
    @Override
    public void setCurrentInterfaceContext() {

    }
}
