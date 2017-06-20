package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.util.SamplerInitilizer;
import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/20.
 * @desriptioin
 */
public class SamplerConfigReader_Config implements ConfigReader_Config {
    final static Map<String, Sampler> CONFIG = SamplerInitilizer.init();
    ConfigReader reader;

    @Override
    public ConfigReader get() {
        return reader;
    }

    @Override
    public void setConfigReader(ConfigReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<String, Sampler> getSamplerConfig() {
        return CONFIG;
    }
}
