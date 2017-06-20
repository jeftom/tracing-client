package com.bdfint.bdtrace.util;

import com.github.kristofa.brave.Sampler;
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

    /**
     * Method: init()
     */
    @Test
    public void testInit() throws Exception {

        Map<String, Sampler> init = SamplerInitilizer.init();
        System.out.println(init);
        init = SamplerInitilizer.init(SamplerInitilizer.SamplerType.METHOD_SAMPLER_PATH);
        System.out.println(init);
        init = SamplerInitilizer.init(SamplerInitilizer.SamplerType.GROUP_SAMPLER_PATH);
        System.out.println(init);
        init = SamplerInitilizer.init(SamplerInitilizer.SamplerType.APPLICATION_SAMPLER_PATH);
        System.out.println(init);
        init = SamplerInitilizer.init(SamplerInitilizer.SamplerType.GLOBAL_SAMPLER_PATH);
        System.out.println(init);


    }


} 
