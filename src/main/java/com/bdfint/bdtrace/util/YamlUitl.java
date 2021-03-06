package com.bdfint.bdtrace.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/21.
 * @desriptioin
 */
public class YamlUitl {

    private static final Map YAML_MAP;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlUitl.class.getClassLoader().getResourceAsStream("sampler.yaml");
        YAML_MAP = (Map) yaml.load(inputStream);
    }

    public static Object get(SamplerInitializer.SamplerType type) {
        return YAML_MAP.get(type.toString());
    }

    public static void main(String[] args) {
        Object o = get(SamplerInitializer.SamplerType.METHOD_SAMPLER_PATH);
        System.out.println(o);
        for (SamplerInitializer.SamplerType type : SamplerInitializer.SamplerType.values()) {
            System.out.println(type.getConfig());
        }
    }
}
