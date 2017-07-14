package com.bdfint.bdtrace.metrics;

/**
 * @author heyb
 * @date 2017/7/14.
 * @desriptioin 针对每一个类型的统计
 */
public interface Stat {

    boolean isSamplingAsExpect();

    float getErrorRange();

    void setErrorRange(float range);

    /**
     * 采样率
     *
     * @return
     */
    Float getSampler();

    void reset();

    /**
     * 统计采样开始的时间
     *
     * @return ms
     */
    long statFrom();

    /**
     * 采样的次数
     *
     * @return
     */
    long sampledTimes();

    /**
     * 调用的总次数
     *
     * @return
     */
    long calledTimes();
}
