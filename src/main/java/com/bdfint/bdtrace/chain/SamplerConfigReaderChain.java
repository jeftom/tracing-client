package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.util.SamplerInitilizer;
import com.github.kristofa.brave.Sampler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class SamplerConfigReaderChain implements ReaderChain {

    ConfigReader_Config[] readers = new ConfigReader_Config[1];
    int pos = 0;
    int n = 0;

    List<ConfigReader> readerList = new ArrayList<>();

    /**
     * 读取下一个Reader的内容
     *
     * @param result
     */
    @Override
    public <T> void readForAll(T result) {
        if (pos < n) {
            ConfigReader_Config readerConfig = readers[pos++];
            readerConfig.get().read(readerConfig.getSamperConfig(), result, this);
            return;
        }

//        throw new IllegalStateException("wtf 数组越界");
    }

    /**
     * 添加
     *
     * @param reader
     */
    @Override
    public void addReader(ConfigReader reader) {
        readers[n] = new ConfigReader_Config() {
            ConfigReader reader;

            @Override
            public ConfigReader get() {
                return this.reader;
            }

            @Override
            public void setConfigReader(ConfigReader reader) {
                this.reader = reader;
            }

            @Override
            public Map<String, Sampler> getSamperConfig() {
                return SamplerInitilizer.init();
            }
        };
        readers[n++].setConfigReader(reader);
        readerList.add(reader);
    }
}
