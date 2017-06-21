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
public class SamplerInitilizer {

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

        METHOD_SAMPLER_PATH("method", new ConcurrentHashMap<>()),
        SERVICE_SAMPLER_PATH("service", new ConcurrentHashMap<>()),
        GROUP_SAMPLER_PATH("group", new ConcurrentHashMap<>()),
        APPLICATION_SAMPLER_PATH("application", new ConcurrentHashMap<>()),
        GLOBAL_SAMPLER_PATH("global", new ConcurrentHashMap<>());

        static {
            Pattern pattern = Pattern.compile("^(0|1|1.0|(0\\.\\d+))$");

            for (SamplerInitilizer.SamplerType type : SamplerInitilizer.SamplerType.values()) {
                String t = type.toString();
                System.out.println(t);
                Map<String, Sampler> samplerConfig = type.config;
                Object obj = YamlUitl.get(type);

                Sampler sampler;
                if (obj instanceof Map) {
                    Set<Map.Entry<String, Object>> entries = ((Map) obj).entrySet();
                    for (Map.Entry<String, Object> entry : entries) {
                        String key = entry.getKey();
                        String ptg = String.valueOf(entry.getValue());
                        try {
                            sampler = Sampler.create(Float.parseFloat(ptg));
                            samplerConfig.put(key, sampler);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (obj instanceof Integer) {
                    Integer iVal = (Integer) obj;
                    sampler = Sampler.create(Float.parseFloat(String.valueOf(iVal)));
                    samplerConfig.put("", sampler);

                } else if (obj instanceof Double) {
                    Double dVal = (Double) obj;
                    sampler = Sampler.create(Float.parseFloat(String.valueOf(dVal)));
                    samplerConfig.put("", sampler);
                }
            }
        }

        String path;
        Map<String, Sampler> config;

        SamplerType(String s, Map<String, Sampler> map) {
            path = s;
            config = map;
        }

        @Override
        public String toString() {
            return path;
        }

        public Map<String, Sampler> getConfig() {
            return config;
        }
    }
}
