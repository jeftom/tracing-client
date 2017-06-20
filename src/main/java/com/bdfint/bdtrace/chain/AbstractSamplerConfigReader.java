package com.bdfint.bdtrace.chain;

import com.bdfint.bdtrace.function.DefaultGlobalSampler;
import com.bdfint.bdtrace.functionable.CurrentInterface;
import com.bdfint.bdtrace.functionable.GlobalSampler;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public abstract class AbstractSamplerConfigReader implements ConfigReader, CurrentInterface {
    GlobalSampler globalSampler = new DefaultGlobalSampler();
    String name;

    @Override
    public String getInterface() {
        return name;
    }

    @Override
    public void setInterface(String itf) {
        name = itf;
    }
}
