package com.bdfint.bdtrace.util;

import com.github.kristofa.brave.Sampler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * SamplerInitilizer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre></pre>
 */
public class SamplerInitilizerTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: init()
     */
    @Test
    public void testInit() throws Exception {

        Map<String, Sampler> init = SamplerInitilizer.init();
        System.out.println(init);


    }


} 
