package com.bdfint.bdtrace.chain;

import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public interface ConfigReader_Config {

    ConfigReader get();

    void setConfigReader(ConfigReader reader);

    Map<String, Sampler> getSamplerConfig();
}
