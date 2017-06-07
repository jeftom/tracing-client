package test.com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.function.ParentServiceNameCacheProcessor;
import com.github.kristofa.brave.SpanId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ParentServiceNameCacheProcessor Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>$yyyy/$mm/$dd</pre>
 */
public class ParentServiceNameCacheProcessorTest {
    static AtomicLong uuid = new AtomicLong(0);
    ParentServiceNameCacheProcessor processor = new ParentServiceNameCacheProcessor();

    //    @Test
//    public void testtt() throws Exception {
    public static void main(String[] args) throws Exception {
        ParentServiceNameCacheProcessorTest test = new ParentServiceNameCacheProcessorTest();
        test.testSetParentServiceName();
        Thread.sleep(5000);
//        test.testGetParentLocalSpanId();
    }

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: setParentServiceName(String serviceName, SpanId spanId)
     */
    @Test
    public void testSetParentServiceName() throws Exception {

        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(uuid.get());
                    processor.setParentServiceName(uuid.toString(), SpanId.builder().traceId(uuid.get()).spanId(uuid.get()).build());
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ParentServiceNameCacheProcessor.getCache().get());
                    System.out.println(ParentServiceNameCacheProcessor.getCache().get().get(uuid.get()));
                    uuid.incrementAndGet();
                }
            }).start();
        }
    }

    /**
     * Method: getParentLocalSpanId(String serviceName, SpanId spanId)
     */
    @Test
    public void testGetParentLocalSpanId() throws Exception {
        for (int i = 0; i < 1000; i++) {

            System.out.println(uuid.incrementAndGet());
            System.out.println(uuid.get());
            System.out.println(ParentServiceNameCacheProcessor.getCache().get().get(i));
            LocalSpanId spanId = processor.getParentLocalSpanId(SpanId.builder().spanId(Long.valueOf(i)).parentId(Long.valueOf(i)).build());
            if (spanId == null) continue;
            Assert.assertNull((spanId.getParentSpanId().parentId));
            Assert.assertEquals(java.util.Optional.ofNullable(spanId.getParentSpanId().spanId), Long.valueOf(i));
            Assert.assertEquals(java.util.Optional.ofNullable(spanId.getParentSpanId().traceId), Long.valueOf(i));
            Assert.assertEquals(spanId.getParentSpanServiceName(), String.valueOf(i));
        }
    }

    @Test
    public void testClearCache() {
        processor.setParentServiceName("", SpanId.builder().spanId(1).traceId(1).build());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean b = processor.clearCache();
        System.out.println(b);
    }
} 
