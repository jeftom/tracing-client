package com.bdfint.bdtrace.chain;

import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public interface ConfigReader {

    /**
     * 读取配置并处理返回
     *
     * @param config 配置
     * @param chain
     * @return
     */
    <T> void read(Map<String, Sampler> config, T result, ReaderChain chain);
}
