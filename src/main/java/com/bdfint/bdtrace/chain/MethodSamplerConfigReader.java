package com.bdfint.bdtrace.chain;

import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class MethodSamplerConfigReader extends AbstractSamplerConfigReader {

    protected boolean conditionOnNotSampling(Map<String, Sampler> config) {
        return config.containsKey(getInterface()) && !config.get(getInterface()).isSampled(0);//如果不需要采样，就读取下一个配置文件
    }
}