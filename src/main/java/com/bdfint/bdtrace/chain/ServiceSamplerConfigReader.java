package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.github.kristofa.brave.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class ServiceSamplerConfigReader extends AbstractSamplerConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ServiceSamplerConfigReader.class);

    @Override
    public <T> void read(Map<String, Sampler> config, T result, ReaderChain chain) {
        SamplerResult samplerResult = result instanceof SamplerResult ? ((SamplerResult) result) : null;

        if (samplerResult != null) {
            if (
//                    (!config.containsKey(getInterface()) && !globalSampler.defaultSampler().isSampled(0L)) ||
                    config.containsKey(getInterface()) && !config.get(getInterface()).isSampled(0)) {//如果不需要采样，就读取下一个配置文件
                logger.debug("当前服务接口名称：{}, 采样：{}", getInterface(), false);
                samplerResult.setSampled(false);
                chain.readForAll(result);
                return;
            }

            logger.debug("当前服务接口名称：{}, 采样：{}", getInterface(), true);
            samplerResult.setSampled(true);
        }
    }
}
