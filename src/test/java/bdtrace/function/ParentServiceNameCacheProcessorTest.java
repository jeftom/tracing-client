package bdtrace.function;

import com.bdfint.bdtrace.bean.LocalSpanId;
import com.bdfint.bdtrace.function.ParentServiceNameMapCacheProcessor;
import com.bdfint.bdtrace.function.ParentServiceNameThreadLocalCacheProcessor;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.SpanId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.ofNullable;

/**
 * ParentServiceNameThreadLocalCacheProcessor Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>$yyyy/$mm/$dd</pre>
 */
public class ParentServiceNameCacheProcessorTest {
    private static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    static AtomicLong uuid = new AtomicLong(0);
    static ParentServiceNameMapCacheProcessor processor = new ParentServiceNameMapCacheProcessor();
    private static int sInternal = 1 * 1000; // unit is ms

    //    @Test
//    public void testtt() throws Exception {
    public static void main(String[] args) throws Exception {
        ParentServiceNameMapCacheProcessor processor = new ParentServiceNameMapCacheProcessor();
//        ParentServiceNameCacheProcessorTest test = new ParentServiceNameCacheProcessorTest();
//        test.testSetParentServiceName();
//        Thread.sleep(5000);
//        SERVICE.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(System.currentTimeMillis());
//            }
//        }, sInternal, sInternal, TimeUnit.MILLISECONDS);
//        test.testGetParentLocalSpanId();

        Brave brave = null;
        new Thread(() -> {
            processor.setParentServiceName("1", SpanId.builder().traceId(1).spanId(1).build(), brave);
        }).start();
        new Thread(() -> {
            processor.setParentServiceName("5", SpanId.builder().traceId(1).spanId(5).parentId(3L).build(), brave);
        }).start();
        new Thread(() -> {
            processor.setParentServiceName("2", SpanId.builder().traceId(1).spanId(2).parentId(1L).build(), brave);
        }).start();
        new Thread(() -> {
            processor.setParentServiceName("3", SpanId.builder().traceId(1).spanId(3).parentId(1L).build(), brave);
        }).start();
        new Thread(() -> {
            processor.setParentServiceName("4", SpanId.builder().traceId(1).spanId(4).parentId(2L).build(), brave);
        }).start();
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
                    Brave brave = null;
                    processor.setParentServiceName(uuid.toString(), SpanId.builder().traceId(uuid.get()).spanId(uuid.get()).build(), brave);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ParentServiceNameThreadLocalCacheProcessor.getCache().get());
//                    System.out.println(ParentServiceNameThreadLocalCacheProcessor.getCache().get().get(uuid.get()));
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
//            System.out.println(ParentServiceNameThreadLocalCacheProcessor.getCache().get().get(i));
            LocalSpanId spanId = processor.getParentLocalSpanId(SpanId.builder().spanId(Long.valueOf(i)).parentId(Long.valueOf(i)).build(), "");
            if (spanId == null) continue;
            Assert.assertNull((spanId.getParentSpanId().parentId));
            Assert.assertEquals(ofNullable(spanId.getParentSpanId().spanId), Long.valueOf(i));
            Assert.assertEquals(ofNullable(spanId.getParentSpanId().traceId), Long.valueOf(i));
            Assert.assertEquals(spanId.getParentSpanServiceName(), String.valueOf(i));
        }
    }

    @Test
    public void testClearCache() {
        Brave brave = null;
        processor.setParentServiceName("", SpanId.builder().spanId(1).traceId(1).build(), brave);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean b = processor.clearCache();
        System.out.println(b);
    }

}
