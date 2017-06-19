package com.bdfint.bdtrace.util;

import com.github.kristofa.brave.Sampler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class SamplerInitilizer {

    private static final String SAMPLER_PATH = "sampler.properties";
    private final static Map<String, Sampler> SAMPLER_CONFIG = new ConcurrentHashMap<>();

    /**
     * 实现必须为线程安全的，在dubbo的多线程中使用
     *
     * @return
     */
    public static Map<String, Sampler> init() {
        Set<Map.Entry<Object, Object>> entries = Configuration.listProperties(SAMPLER_PATH);
        Sampler sampler;

        for (Map.Entry<Object, Object> entry : entries) {
            String key = entry.getKey().toString();
            String ptg = entry.getValue().toString();
            sampler = Sampler.create(Float.parseFloat(ptg));
            SAMPLER_CONFIG.put(key, sampler);
        }

        return SAMPLER_CONFIG;
    }
}
