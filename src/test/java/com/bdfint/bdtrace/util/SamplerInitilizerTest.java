package com.bdfint.bdtrace.util;

import com.github.kristofa.brave.Sampler;
import org.junit.Test;

import java.util.Map;

/**
 * SamplerInitializer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre></pre>
 */
public class SamplerInitilizerTest {

    /**
     * Method: init()
     */
    @Test
    public void testInit() throws Exception {

        Map<String, Sampler> init = SamplerInitializer.init();
        System.out.println(init);
        init = SamplerInitializer.init(SamplerInitializer.SamplerType.METHOD_SAMPLER_PATH);
        System.out.println(init);
        init = SamplerInitializer.init(SamplerInitializer.SamplerType.GROUP_SAMPLER_PATH);
        System.out.println(init);
        init = SamplerInitializer.init(SamplerInitializer.SamplerType.APPLICATION_SAMPLER_PATH);
        System.out.println(init);
        init = SamplerInitializer.init(SamplerInitializer.SamplerType.GLOBAL_SAMPLER_PATH);
        System.out.println(init);


    }


} 
