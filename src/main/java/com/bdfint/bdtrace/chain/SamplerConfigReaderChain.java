package com.bdfint.bdtrace.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class SamplerConfigReaderChain implements ReaderChain {

    ConfigReader_Config[] readers = new ConfigReader_Config[0];
    int pos = 0;
    int n = 0;

    List<ConfigReader> readerList = new ArrayList<>();

    /**
     * 读取下一个Reader的内容
     * @param result
     */
    @Override
    public <T> void readNext(T result) {
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
        readers[n++].setConfigReader(reader);
        readerList.add(reader);
    }
}
