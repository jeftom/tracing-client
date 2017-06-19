package com.bdfint.bdtrace.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public class Configuration {
    final static String zipkinUrlHead = "http://";
    final static String zipkinPort = ":9411";
    final static String zipkinUrlTail = "/api/v1/spans/";
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static final String SAMPLER = "sampler";
    private static final String HOST = "domain";
    static String zipkinHost = "127.0.0.1";
    final static String zipkinUrl = "http://" + zipkinHost + "/api/v1/spans/";

    /**
     * get url from client
     *
     * @return zipkin url where to send tracing data
     */
    public static String getZipkinUrl() {
        zipkinHost = getProperty(HOST);
        return zipkinUrl();
    }

    /**
     * get property from file named zipkin.properties under <b>resources</b>
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        Properties prop = getProperties();
        return prop.getProperty(key);
    }

    private static String zipkinUrl() {
        return zipkinUrlHead + zipkinHost + zipkinPort + zipkinUrlTail;
    }

    public static void main(String[] args) throws IOException {

        logger.info(getZipkinUrl());
//        logger.info(getSampler());
    }

    /**
     * get sample percentage
     *
     * @return
     */
    public static float getSampler() {
        String p = getProperty(SAMPLER);
        float sampler = Float.valueOf(p);
        logger.info("tracing-client的采样率为：{}", p);

//        return 1F;
        return sampler;
    }

    public static long timeout() {
        String t = getProperty("timeout");
        return Long.valueOf(t);
    }

    public static Properties getProperties() {
        return getProperties("zipkin.properties");
    }

    public static Properties getProperties(String path) {
        Properties prop = new Properties();
        try {
            prop.load(Configuration.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            logger.info("异常信息：", e);
        }
        return prop;
    }

    /**
     * 获取配置文件的所有k-v
     *
     * @return
     */
    public static Set<Map.Entry<Object, Object>> listProperties(String path) {
        Properties prop = getProperties(path);
        return prop.entrySet();
    }
}
