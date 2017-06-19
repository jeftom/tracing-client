package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.functionable.CurrentInterface;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class CurrentInterfaceTranfer implements CurrentInterface {
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
