package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class SamplerConfigReaderChain implements ReaderChain {

    ConfigReader_Config[] readers = new ConfigReader_Config[1];
    int pos = 0;
    int n = 0;

    /**
     * 读取下一个Reader的内容
     *
     * @param result
     */
    @Override
    public void readForAll(SamplerResult result) {
        if (pos < n) {
            ConfigReader_Config readerConfig = readers[pos++];
            readerConfig.get().read(readerConfig.getSamplerConfig(), result, this);
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
        if (n >= readers.length) {
            ConfigReader_Config[] tmp = readers;
            readers = new ConfigReader_Config[tmp.length + 1];
            System.arraycopy(tmp, 0, readers, 0, tmp.length);
        }
        readers[n] = new SamplerConfigReader_Config();
        readers[n++].setConfigReader(reader);
    }

    /**
     * 添加readers
     *
     * @param readers
     */
    @Override
    public void addReaders(ConfigReader[] readers) {
        for (ConfigReader reader : readers)
            addReader(reader);
    }
}
