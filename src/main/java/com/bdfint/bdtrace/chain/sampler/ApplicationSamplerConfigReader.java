package com.bdfint.bdtrace.chain.sampler;

import com.bdfint.bdtrace.util.SamplerInitializer;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class ApplicationSamplerConfigReader extends AbstractSamplerConfigReader {

    /**
     * 决定当前的分类类型
     *
     * @return
     */
    @Override
    public SamplerInitializer.SamplerType type() {
        return SamplerInitializer.SamplerType.APPLICATION_SAMPLER_PATH;
    }
}
