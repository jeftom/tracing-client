package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.functionable.CurrentInterface;
import com.github.kristofa.brave.Sampler;
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

        if (samplerResult != null) {
            if (conditionOnNotSampling(config)) {//如果不需要采样，就读取下一个配置文件
                logger.debug("当前服务接口名称：{}, 采样：{}", getInterface(), false);
                samplerResult.setSampled(false);
                chain.readForAll(samplerResult);
                return;
            }

            logger.debug("当前服务接口名称：{}, 采样：{}", getInterface(), true);
            samplerResult.setSampled(true);
        }
    }

    protected abstract boolean conditionOnNotSampling(Map<String, Sampler> config);

}
