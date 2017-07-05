package com.bdfint.bdtrace;

import com.bdfint.bdtrace.util.Configuration;
import org.springframework.context.ApplicationContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/7/5.
 * @desriptioin
 */
public class SamplerConfigGenerator {
    static final String GLOBAL_KEY = "";
    static final String APPLICATION_TYPE = "com.alibaba.dubbo.config.ApplicationConfig";
    static final String GROUP_KEY = "??";
    static final String SERVICE_TYPE = "com.alibaba.dubbo.config.spring.ServiceBean";
    static final String REFERENCE_TYPE_REG_EX = "com.alibaba.dubbo.common.bytecode.proxy\\d+";
    static final String METHOD_KEY = "";
    static final Float defaultSampler = Configuration.getSampler();
    static final String WHITESPACE = "  ";
    static ApplicationContext context;//= Context.getContext();
    static List<String> application = new ArrayList<>();
    static List<String> service = new ArrayList<>();
    static List<String> method = new ArrayList<>();
    static StringBuilder configSb = new StringBuilder();
    static Map<String, Map<String, Float>> map = new HashMap<>();
    static Map<String, Float> conf = new HashMap<>();
    static Map<String, Float> conf1 = new HashMap<>();
    static Map<String, Float> conf2 = new HashMap<>();

    public static void get() {
        for (String beanName : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(beanName);
            if (bean.getClass().getCanonicalName().equals(APPLICATION_TYPE)) {
                application.add(beanName);
                conf.put(beanName, defaultSampler);
            }
            if (bean.getClass().getCanonicalName().equals(SERVICE_TYPE)) {
                service.add(beanName);
                conf1.put(beanName, defaultSampler);
            }
            Method[] methods = bean.getClass().getDeclaredMethods();
            System.out.println(beanName + "{");
            for (Method m : methods) {
                if (!m.getName().equals("$echo")) {
                    conf2.put(m.getName(), defaultSampler);
                    method.add(m.getName());
                }
                System.out.println("    " + m.getName() + ", ");
            }
            System.out.println("}");
            System.out.println(beanName + "->" + bean.getClass());
        }

        appendModule("application", application);
        appendModule("service", service);
        appendModule("method", method);

        map.put("application", conf);
        map.put("service", conf1);
        map.put("method", conf2);
    }

    public static void appendModule(String key, List<String> configModule) {
        configSb.append(key).append(":");
        configSb.append(System.lineSeparator());
        for (String mod : configModule)
            configSb.append(WHITESPACE).append(mod).append(":").append(defaultSampler).append(System.lineSeparator());
        configSb.append(System.lineSeparator());
        configSb.append(System.lineSeparator());
    }

    public static void writeFile() {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("./samplerx.yaml"))) {
            writer.write(configSb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        get();
//        System.out.println(configSb);
        writeFile();
    }
}
