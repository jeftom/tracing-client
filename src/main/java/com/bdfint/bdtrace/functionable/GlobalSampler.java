package com.bdfint.bdtrace.functionable;

import com.github.kristofa.brave.Sampler;

/**
 * @author heyb
 * @date 2017/6/20.
 * @desriptioin
 */
public interface GlobalSampler {

    /**
     * 默认的的全局Sampler
     * @return
     */
    Sampler defaultSampler();
}
