package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.function.DefaultGlobalSampler;
import com.bdfint.bdtrace.functionable.GlobalSampler;
import com.bdfint.bdtrace.util.SamplerInitilizer;
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
        return !config.values().iterator().next().isSampled(0);
    }

    /**
     * 决定当前的分类类型
     *
     * @return
     */
    @Override
    public SamplerInitilizer.SamplerType type() {
        return SamplerInitilizer.SamplerType.GLOBAL_SAMPLER_PATH;
    }
}
