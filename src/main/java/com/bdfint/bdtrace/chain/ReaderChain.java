package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.bean.SamplerResult;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public interface ReaderChain {

    /**
     * 读取下一个Reader的内容
     *
     * @param result
     */
    void readForAll(SamplerResult result);

    /**
     * 添加
     */
    void addReader(ConfigReader reader);
}
