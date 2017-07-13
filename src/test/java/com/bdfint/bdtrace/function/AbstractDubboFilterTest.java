package com.bdfint.bdtrace.function;

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
        ClassLoader classLoader = AbstractDubboFilterTest.class.getClassLoader();
        try {
            AbstractDubboFilter bClass = (AbstractDubboFilter) Class.forName("com.bdfint.bdtrace.BraveProviderFilter", true, classLoader).newInstance();
            Class<?> aClass = Class.forName("com.bdfint.bdtrace.BraveConsumerFilter", true, classLoader);
            System.out.println(aClass);
            System.out.println(bClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
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
     * Method: {@link AbstractDubboFilter#setParentServiceName () } setParentServiceName(String serviceName, SpanId spanId, Brave brave)
     */
    @Test
    public void testSetParentServiceName() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#getParentServiceNameAndSetBrave () } getParentServiceNameAndSetBrave(String serviceName, SpanId spanId, BravePack bravePack)
     */
    @Test
    public void testGetParentServiceNameAndSetBrave() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#brave () } brave(String serviceName, BravePack bravePack)
     */
    @Test
    public void testBrave() throws Exception {
        //TODO: Test goes here...
    }

    /**
     * Method: {@link AbstractDubboFilter#handleAndGetException () } handleAndGetException(Result result, String serviceName, StatusEnum status)
     */
    @Test
    public void testHandleAndGetException() throws Exception {
        //TODO: Test goes here...
    }
} 
