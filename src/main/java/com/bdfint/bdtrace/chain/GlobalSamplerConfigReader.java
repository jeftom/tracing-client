package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.function.DefaultGlobalSampler;
import com.bdfint.bdtrace.functionable.GlobalSampler;
import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class GlobalSamplerConfigReader extends AbstractSamplerConfigReader {

    private static final GlobalSampler GLOBAL_SAMPLER = new DefaultGlobalSampler();

    @Override
    protected boolean conditionOnNotSampling(Map<String, Sampler> config) {
        return !GLOBAL_SAMPLER.defaultSampler().isSampled(0L);
    }
}
