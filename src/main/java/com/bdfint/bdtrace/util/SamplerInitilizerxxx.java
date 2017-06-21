package com.bdfint.bdtrace.util;

import com.github.kristofa.brave.Sampler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class SamplerInitilizerxxx {

//    private static final String METHOD_SAMPLER_PATH = "sampler.method.properties";
//    private static final String SERVICE_SAMPLER_PATH = "sampler.service.properties";
//    private static final String GROUP_SAMPLER_PATH = "sampler.group.properties";
//    private static final String APPLICATON_SAMPLER_PATH = "sampler.application.properties";
//    private static final String GLOBAL_SAMPLER_PATH = "zipkin.properties";
//    private final static Map<String, Sampler> SAMPLER_CONFIG = new ConcurrentHashMap<>();
//    private final static Map<String, Map<String, Sampler>> TYPES_SAMPLER_CONFIG = new ConcurrentHashMap<>();

    /**
     * 实现必须为线程安全的，在dubbo的多线程中使用
     *
     * @return
     */
    public static Map<String, Sampler> init() {
        return init(SamplerType.SERVICE_SAMPLER_PATH);
    }

    public static Map<String, Sampler> init(SamplerType type) {
        return type.config;
    }

    enum SamplerType {
        METHOD_SAMPLER_PATH("sampler.method.properties", new ConcurrentHashMap<>()),
        SERVICE_SAMPLER_PATH("sampler.service.properties", new ConcurrentHashMap<>()),
        GROUP_SAMPLER_PATH("sampler.group.properties", new ConcurrentHashMap<>()),
        APPLICATION_SAMPLER_PATH("sampler.application.properties", new ConcurrentHashMap<>()),
        GLOBAL_SAMPLER_PATH("sampler.global.properties", new ConcurrentHashMap<>());

        static {

            Pattern pattern = Pattern.compile("^(0|1|1.0|(0\\.\\d+))$");
            for (SamplerType type : SamplerType.values()) {

                Map<String, Sampler> samplerConfig = type.config;
                Set<Map.Entry<Object, Object>> entries = Configuration.listProperties(type.path);
                Sampler sampler;

                for (Map.Entry<Object, Object> entry : entries) {
                    String key = entry.getKey().toString();
                    String ptg = entry.getValue().toString();
//                    Matcher matcher = pattern.matcher(ptg);
//                    if (matcher.find()) {
                    try {
                        sampler = Sampler.create(Float.parseFloat(ptg));
                        samplerConfig.put(key, sampler);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
//                    }
                }

            }
        }

        String path;
        Map<String, Sampler> config;

        SamplerType(String s, Map<String, Sampler> map) {
            path = s;
            config = map;
        }
    }
}
