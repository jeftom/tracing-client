package com.bdfint.bdtrace.chain.sampler;

import com.bdfint.bdtrace.chain.ConfigReader;
import com.bdfint.bdtrace.chain.ConfigReader_Config;
import com.bdfint.bdtrace.util.SamplerInitilizer;
import com.github.kristofa.brave.Sampler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heyb
 * @date 2017/6/20.
 * @desriptioin
 */
public class SamplerConfigReader_Config implements ConfigReader_Config {
//    final static Map<String, Sampler> METHOD_CONFIG = SamplerInitilizer.init(SamplerInitilizer.SamplerType.METHOD_SAMPLER_PATH);
//    final static Map<String, Sampler> SERVICE_CONFIG = SamplerInitilizer.init(SamplerInitilizer.SamplerType.SERVICE_SAMPLER_PATH);
//    final static Map<String, Sampler> GROUP_CONFIG = SamplerInitilizer.init(SamplerInitilizer.SamplerType.GROUP_SAMPLER_PATH);
//    final static Map<String, Sampler> APPLICATION_CONFIG = SamplerInitilizer.init(SamplerInitilizer.SamplerType.APPLICATION_SAMPLER_PATH);
//    final static Map<String, Sampler> GLOBAL_CONFIG = SamplerInitilizer.init(SamplerInitilizer.SamplerType.GLOBAL_SAMPLER_PATH);

    final static Map<String, Map<String, Sampler>> CONFIG;

    static {
        CONFIG = new ConcurrentHashMap<>();
        for (SamplerInitilizer.SamplerType type : SamplerInitilizer.SamplerType.values()) {
            CONFIG.put(type.toString(), SamplerInitilizer.init(type));
        }
    }

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
        return CONFIG.get(reader.type().toString());
    }

    public static void main(String[] args) {
        System.out.println(CONFIG);
        System.out.println(CONFIG.get(SamplerInitilizer.SamplerType.APPLICATION_SAMPLER_PATH.toString()));
    }
}
