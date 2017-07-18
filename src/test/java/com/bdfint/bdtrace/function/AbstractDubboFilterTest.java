package com.bdfint.bdtrace.function;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link AbstractDubboFilter} Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre></pre>
 */
public class AbstractDubboFilterTest {


    public static void main(String[] args) {

        for (; ; ) {

            AbstractDubboFilter filter;
            filter = new AbstractDubboFilter() {
                @Override
                public boolean preHandle(Invoker<?> invoker, Invocation invocation) {
                    return false;
                }

                @Override
                public void afterHandle(Invocation invocation) {

                }
            };
        }
    }

    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: {@link AbstractDubboFilter#initField () } initField(Invoker<?> invoker, Invocation invocation)
     */
    @Test
    public void testInitField() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#invoke () } invoke(Invoker<?> invoker, Invocation invocation)
     */
    @Test
    public void testInvoke() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#setParentServiceName () } setParentServiceName(String serviceName, SpanId spanId)
     */
    @Test
    public void testSetParentServiceName() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#getParentServiceNameAndSetBrave () } getParentServiceNameAndSetBrave(String serviceName, SpanId spanId)
     */
    @Test
    public void testGetParentServiceNameAndSetBrave() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#setInterceptors () } setInterceptors(String serviceName)
     */
    @Test
    public void testSetInterceptors() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#handleAndGetException () } handleAndGetException(Result result, String serviceName)
     */
    @Test
    public void testHandleAndGetException() throws Exception {
        //TODO: Test goes here...
    }


    /**
     * Method: setSampled(DubboClientRequestAdapter clientRequestAdapter, boolean sampled)
     */
    @Test
    public void testSetSampled() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = AbstractDubboFilter.getClass().getMethod("setSampled", DubboClientRequestAdapter.class, boolean.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
