package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.functionable.GlobalSampler;
import com.bdfint.bdtrace.util.Configuration;
import com.github.kristofa.brave.Sampler;

/**
 * @author heyb
 * @date 2017/6/20.
 * @desriptioin
 */
public class DefaultGlobalSampler implements GlobalSampler {
    private static final Sampler SAMPLER = Sampler.create(Configuration.getSampler());

    /**
     * 默认的的全局Sampler
     *
     * @return
     */
    @Override
    public Sampler defaultSampler() {
        return SAMPLER;
    }
}
