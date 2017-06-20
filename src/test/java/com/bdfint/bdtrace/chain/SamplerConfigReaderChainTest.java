package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.util.Configuration;
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
        int[] counts = new int[entries.size()];
        int[] global = new int[entries.size()];

        int[] actual = new int[counts.length];
        int idx = 0;

        for (Map.Entry<Object, Object> entry : entries) {
            String key = entry.getKey().toString();
            String ptg = entry.getValue().toString();
            actual[idx] = (int) (Float.parseFloat(ptg) * 100);
            global[idx] = (int) (Configuration.getSampler() * 100);

            for (int i = 0; i < 100; i++) {

                //采样处理
//                AbstractSamplerConfigReader reader = new ServiceSamplerConfigReader();
                AbstractSamplerConfigReader reader = new GlobalSamplerConfigReader();
                SamplerResult samplerResult = new SamplerResult();
                reader.setInterface(key);
                ReaderChain chain = new SamplerConfigReaderChain();
                chain.addReader(reader);
                chain.readForAll(samplerResult);

                if (samplerResult.isSampled()) {
                    counts[idx]++;
                }
            }
            idx++;
        }
//        Assert.assertArrayEquals(counts, actual);
        Assert.assertArrayEquals(counts, global);
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
    }


}
