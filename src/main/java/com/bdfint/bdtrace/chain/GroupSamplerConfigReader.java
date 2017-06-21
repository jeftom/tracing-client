package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.util.SamplerInitilizer;
import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class GroupSamplerConfigReader extends AbstractSamplerConfigReader {

    protected boolean conditionOnNotSampling(Map<String, Sampler> config) {
        return config.containsKey(getInterface()) && !config.get(getInterface()).isSampled(0);//如果不需要采样，就读取下一个配置文件
    }

    /**
     * 决定当前的分类类型
     *
     * @return
     */
    @Override
    public SamplerInitilizer.SamplerType type() {
        return SamplerInitilizer.SamplerType.GROUP_SAMPLER_PATH;
    }
}
