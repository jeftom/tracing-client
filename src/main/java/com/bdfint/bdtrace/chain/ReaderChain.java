package com.bdfint.bdtrace.chain;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public interface ReaderChain {

    /**
     * 读取下一个Reader的内容
     */
    <T> void readNext(T result);

    /**
     * 添加
     */
    void addReader(ConfigReader reader);
}
