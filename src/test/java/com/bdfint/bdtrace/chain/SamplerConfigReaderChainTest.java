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

        for (Map.Entry<Object, Object> entry : entries) {
            String key = entry.getKey().toString();
            String ptg = entry.getValue().toString();

            int count = 0;
            for (int i = 0; i < 100; i++) {

                //采样处理
                ServiceSamplerConfigReader serviceSamplerConfigReader = new ServiceSamplerConfigReader();
                SamplerResult samplerResult = new SamplerResult();
                serviceSamplerConfigReader.setInterface(key);
                ReaderChain chain = new SamplerConfigReaderChain();
                chain.addReader(serviceSamplerConfigReader);
                chain.readForAll(samplerResult);
                if (samplerResult.isSampled()) {
                    count++;
                }
            }
            Assert.assertEquals((int) (Float.parseFloat(ptg) * 100), count);
        }
    }

    /**
     * Method: addReader(ConfigReader reader)
     */
    @Test
    public void testAddReader() throws Exception {
//TODO: Test goes here... 
    }


} 
