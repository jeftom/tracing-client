package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public interface ConfigReader {

    /**
     * 读取配置并处理返回,SamplerResult在读取到NOT采样时一直为false并处理链条的下一个,直到被告知采样则设置为true并返回
     *
     * @param config 配置
     * @param result
     * @param chain  @return
     */
    void read(Map<String, Sampler> config, SamplerResult result, ReaderChain chain);
}
