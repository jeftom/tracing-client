package com.bdfint.bdtrace.test;

/**
 * @author heyb
 * @date 2017/7/10.
 * @desriptioin
 */
public class TestRpcConsumer {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            HelloService refer = RpcFramework.refer(HelloService.class, "127.0.0.1", 1234);

            String hello = refer.hello("rick " + i);
            System.out.println(hello);
        }
    }
}
