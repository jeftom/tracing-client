package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;
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
        if (!config.get(getInterface()).isSampled(0)) {//如果不需要采样，就读取下一个配置文件
            chain.readForAll(result);
        }
        //TODO result
        SamplerResult samplerResult = result instanceof SamplerResult ? ((SamplerResult) result) : null;
        if (samplerResult != null) {
            samplerResult.setSampled(true);
        }
    }
}
