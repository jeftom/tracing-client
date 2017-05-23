package com.bdfint.bdtrace.support;

import java.io.IOException;
import java.util.Properties;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public class Configuration {
    static String zipkinUrlHead = "http://";
    static String zipkinHost = "127.0.0.1";
    static String zipkinPort = ":9411";
    static String zipkinUrlTail = "/api/v1/spans/";
    static String zipkinUrl = "http://" + zipkinHost + "/api/v1/spans/";


    static final String HOST = "domain";

    /**
     * get url from client
     *
     * @return zipkin url where to send tracing data
     */
    public static String getZipkinUrl() {
        Properties prop = new Properties();
        try {
            prop.load(Configuration.class.getClassLoader().getResourceAsStream("zipkin.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        zipkinHost = prop.getProperty(HOST);
        return zipkinUrl();
    }

    private static String zipkinUrl() {
        return zipkinUrlHead + zipkinHost + zipkinPort + zipkinUrlTail;
    }

    public static void main(String[] args) throws IOException {

        System.out.println(getZipkinUrl());
    }

    /**
     * get sample percentage
     *
     * @return
     */
    public static float getSampler() {
        //TODO

        return 1F;
    }
}
