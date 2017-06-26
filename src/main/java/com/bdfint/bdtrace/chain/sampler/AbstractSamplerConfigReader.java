package com.bdfint.bdtrace.chain.sampler;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.chain.ConfigReader;
import com.bdfint.bdtrace.chain.ReaderChain;
import com.bdfint.bdtrace.functionable.CurrentInterface;
import com.github.kristofa.brave.Sampler;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public abstract class AbstractSamplerConfigReader implements ConfigReader, CurrentInterface {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSamplerConfigReader.class);
    String name;

    @Override
    public String getInterface() {
        return name;
    }

    @Override
    public void setInterface(String itf) {
        name = itf;
    }


    @Override
    public void read(Map<String, Sampler> config, SamplerResult samplerResult, ReaderChain chain) {
        Assert.assertNotNull(samplerResult);

        readForThis(config, samplerResult, chain);
        if (conditionOnNextSampling(config)) {//如果不需要采样，就读取下一个配置文件
            samplerResult.setSampled(false);
            chain.readForAll(samplerResult);
        }
    }

    protected boolean conditionOnNextSampling(Map<String, Sampler> config) {
        return !config.containsKey(getInterface());//如果不需要采样，就读取下一个配置文件
    }

//    protected abstract boolean conditionOnNextSampling(Map<String, Sampler> config);
//    protected abstract boolean readForThis(Map<String, Sampler> config, SamplerResult samplerResult, ReaderChain chain);

    protected boolean readForThis(Map<String, Sampler> config, SamplerResult samplerResult, ReaderChain chain) {
        if (!conditionOnNextSampling(config)) {
            boolean sampled = config.get(getInterface()).isSampled(0);
            samplerResult.setSampled(sampled);
            logger.debug("当前服务接口名称：{}, 采样：{}", getInterface(), sampled);
            return sampled;
        }

        logger.debug("当前服务接口名称：{}, 采样：{}", getInterface(), true);
        return false;
    }
}
