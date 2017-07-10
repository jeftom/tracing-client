package com.bdfint.bdtrace.test;

interface HelloService {
    String hello(String name);
}

class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}

/**
 * @author heyb
 * @date 2017/7/10.
 * @desriptioin
 */
public class TestRpc {
    public static void main(String[] args) throws Exception {

        HelloService helloService = new HelloServiceImpl();
        RpcFramework.export(helloService, 1234);
    }
}
