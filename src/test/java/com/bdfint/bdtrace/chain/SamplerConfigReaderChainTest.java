package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.util.Configuration;
import com.github.kristofa.brave.Sampler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * SamplerConfigReaderChain Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre></pre>
 */
public class SamplerConfigReaderChainTest {

    private static final String SAMPLER_PATH = "sampler.properties";

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: readForAll(T result)
     */
    @Test
    public void testReadForAll() throws Exception {

        Set<Map.Entry<Object, Object>> entries = Configuration.listProperties(SAMPLER_PATH);
        Sampler sampler;
        int[] counts = new int[entries.size()];
        int[] actual = new int[counts.length];
        int idx = 0;

        for (Map.Entry<Object, Object> entry : entries) {
            String key = entry.getKey().toString();
            String ptg = entry.getValue().toString();
            actual[idx] = (int) (Float.parseFloat(ptg) * 100);

            for (int i = 0; i < 100; i++) {

                //采样处理
                ServiceSamplerConfigReader serviceSamplerConfigReader = new ServiceSamplerConfigReader();
                SamplerResult samplerResult = new SamplerResult();
                serviceSamplerConfigReader.setInterface(key);
                ReaderChain chain = new SamplerConfigReaderChain();
                chain.addReader(serviceSamplerConfigReader);
                chain.addReader(serviceSamplerConfigReader);
                chain.addReader(serviceSamplerConfigReader);
                chain.addReader(serviceSamplerConfigReader);
                chain.readForAll(samplerResult);
                if (samplerResult.isSampled()) {
                    counts[idx]++;
                }
            }
            idx++;
//            Assert.assertEquals((int) (Float.parseFloat(ptg) * 100), idx);
        }
        Assert.assertArrayEquals(counts, actual);
    }

    /**
     * Method: addReader(ConfigReader reader)
     */
    @Test
    public void testAddReader() throws Exception {
        ServiceSamplerConfigReader serviceSamplerConfigReader = new ServiceSamplerConfigReader();
        serviceSamplerConfigReader.setInterface("aaa");
        ReaderChain chain = new SamplerConfigReaderChain();
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(serviceSamplerConfigReader);
        chain.addReader(new ConfigReader() {
            /**
             * 读取配置并处理返回,SamplerResult在读取到NOT采样时一直为false并处理链条的下一个,直到被告知采样则设置为true并返回
             *
             * @param config 配置
             * @param result
             * @param chain  @return
             */
            @Override
            public <T> void read(Map<String, Sampler> config, T result, ReaderChain chain) {

            }
        });
    }


} 
