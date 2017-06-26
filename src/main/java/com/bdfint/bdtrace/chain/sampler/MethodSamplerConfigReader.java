package com.bdfint.bdtrace.chain.sampler;

import com.bdfint.bdtrace.util.SamplerInitilizer;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class MethodSamplerConfigReader extends AbstractSamplerConfigReader {

    /**
     * 决定当前的分类类型
     *
     * @return
     */
    @Override
    public SamplerInitilizer.SamplerType type() {
        return SamplerInitilizer.SamplerType.METHOD_SAMPLER_PATH;
    }
}
