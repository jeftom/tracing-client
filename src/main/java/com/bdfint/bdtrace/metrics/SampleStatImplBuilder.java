package com.bdfint.bdtrace.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class SampleStatImplBuilder {
    private AtomicInteger sampledCount;
    private AtomicInteger calledCount;
    private float sampler;
    private boolean expectSampler;
    private long startFrom;
    private float errorRange;
    private String currentInterface;

    public SampleStatImplBuilder setSampledCount(AtomicInteger sampledCount) {
        this.sampledCount = sampledCount;
        return this;
    }

    public SampleStatImplBuilder setCalledCount(AtomicInteger calledCount) {
        this.calledCount = calledCount;
        return this;
    }

    public SampleStatImplBuilder setSampler(float sampler) {
        this.sampler = sampler;
        return this;
    }

    public SampleStatImplBuilder setExpectSampler(boolean expectSampler) {
        this.expectSampler = expectSampler;
        return this;
    }

    public SampleStatImplBuilder setStartFrom(long startFrom) {
        this.startFrom = startFrom;
        return this;
    }

    public SampleStatImplBuilder setErrorRange(float errorRange) {
        this.errorRange = errorRange;
        return this;
    }

    public SampleStatImplBuilder setCurrentInterface(String currentInterface) {
        this.currentInterface = currentInterface;
        return this;
    }

    public SampleStatImpl createSampleStatImpl() {
        return new SampleStatImpl(sampledCount, calledCount, sampler, expectSampler, startFrom, errorRange, currentInterface);
    }
}