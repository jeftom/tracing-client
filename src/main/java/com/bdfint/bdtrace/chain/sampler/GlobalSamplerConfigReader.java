package com.bdfint.bdtrace.chain.sampler;

import com.bdfint.bdtrace.util.SamplerInitializer;
import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class GlobalSamplerConfigReader extends AbstractSamplerConfigReader {

    @Override
    protected boolean conditionOnNextSampling(Map<String, Sampler> config) {
//        return !config.values().iterator().next().isSampled(0);
        return false;
    }

    /**
     * 决定当前的分类类型
     *
     * @return
     */
    @Override
    public SamplerInitializer.SamplerType type() {
        return SamplerInitializer.SamplerType.GLOBAL_SAMPLER_PATH;
    }
}
