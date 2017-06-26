package com.bdfint.bdtrace.chain.sampler;

import com.bdfint.bdtrace.util.SamplerInitializer;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class GroupSamplerConfigReader extends AbstractSamplerConfigReader {

    /**
     * 决定当前的分类类型
     *
     * @return
     */
    @Override
    public SamplerInitializer.SamplerType type() {
        return SamplerInitializer.SamplerType.GROUP_SAMPLER_PATH;
    }
}
