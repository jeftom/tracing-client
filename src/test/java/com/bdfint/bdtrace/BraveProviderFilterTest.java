package com.bdfint.bdtrace;

import com.alibaba.dubbo.rpc.Invocation;
import com.bdfint.bdtrace.bean.BravePack;
import com.bdfint.bdtrace.bean.StatusEnum;
import com.bdfint.bdtrace.function.AbstractDubboFilter;
import com.github.kristofa.brave.Brave;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * {@link BraveProviderFilter} Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre></pre>
 */
public class BraveProviderFilterTest {

    AbstractDubboFilter filter = new BraveProviderFilter();

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: {@link BraveProviderFilter#invoke () } invoke(Invoker<?> invoker, Invocation invocation)
     */
    @Test
    public void testInvoke() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link BraveProviderFilter#preHandle () } preHandle(Invoker<?> invoker, Invocation invocation, String serviceName, String spanName, BravePack bravePack)
     */
    @Test
    public void testPreHandle() throws Exception {
        BravePack bravePack = new BravePack();
        System.out.println(bravePack);
        set(bravePack);
        System.out.println(bravePack.brave);
        System.out.println(bravePack);
    }

    public void set(BravePack bravePack) {
        bravePack.brave = new Brave.Builder().build();
        System.out.println(bravePack.brave);
        set2(bravePack);
        bravePack = new BravePack();
    }

    public void set2(BravePack bravePack) {
        bravePack.brave = new Brave.Builder().build();
        System.out.println(bravePack.brave);
    }


    /**
     * Method: {@link BraveProviderFilter#afterHandle () } afterHandle(Invocation invocation, StatusEnum status, Throwable exception, Brave brave)
     */
    @Test
    public void testAfterHandle() throws Exception {
        Brave brave = org.mockito.Mockito.mock(Brave.class);
        Invocation invocation = mock(Invocation.class);
        filter.afterHandle(invocation, StatusEnum.OK, null, brave);
    }
}
