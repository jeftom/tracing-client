package com.bdfint.bdtrace.chain;

import com.github.kristofa.brave.Sampler;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class ServiceSamplerConfigReader extends AbstractSamplerConfigReader {
    @Override
    public <T> void read(Map<String, Sampler> config, T result, ReaderChain chain) {
        if (!config.get(getInterface()).isSampled(0)) {
            chain.readNext(result);
        }
        //TODO result
        return;
    }
}
