package bdtrace;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.chain.ReaderChain;
import com.bdfint.bdtrace.chain.sampler.*;
import com.bdfint.bdtrace.function.ServiceInfoProvider;
import com.bdfint.bdtrace.functionable.ServiceInfoProvidable;
import com.bdfint.bdtrace.util.SamplerInitilizer;
import com.github.kristofa.brave.Sampler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heyb
 * @date 2017/6/26.
 * @desriptioin
 */
public class SamplerChainTest {
    final static Map<String, Map<String, Sampler>> CONFIG;
    static AbstractSamplerConfigReader reader = new GlobalSamplerConfigReader();

    static {
        CONFIG = new ConcurrentHashMap<>();
        for (SamplerInitilizer.SamplerType type : SamplerInitilizer.SamplerType.values()) {
            CONFIG.put(type.toString(), SamplerInitilizer.init(type));
        }
    }

    AbstractSamplerConfigReader[] readers = {
            new MethodSamplerConfigReader(),
            new ServiceSamplerConfigReader(),
            new GroupSamplerConfigReader(),
            new ApplicationSamplerConfigReader(),
            new GlobalSamplerConfigReader()
    };

    @Test
    public void test() {
        //在给定的采样次数下（100 倍数）,根据每次的服务名称，断定是否走了正确的链路

        //采样处理
        ServiceSamplerConfigReader serviceSamplerConfigReader = new ServiceSamplerConfigReader();
        ReaderChain chain = new SamplerConfigReaderChain();
        chain.addReaders(readers);
        ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
        String method = "logistic.x.com.bdfint.bdtrace.service.Buy.buy";
        String service = "logistic.y.com.bdfint.bdtrace.service.BuyAA";
        String group = "logistic.y";
        String application = "logistic";
        boolean sample = true;

        //一层
        testNTimes(method, service, group, application, 0.1F);
        //两层
        testNTimes("", service, group, application, 0.4F);
        testNTimes("", "logistic.y.com.bdfint.bdtrace.service.BuyABA", group, application, 0.6F);
        //三层
        testNTimes("", "logistic.y.com.bdfint.bdtrace.service.uyABA", "logistic.x", application, 0.05F);
        //四层
        testNTimes("", "logistic.y.com.bdfint.bdtrace.service.uyABA", "logistic.z", "logistic", 0.07F);
        // 5
        testNTimes("", "logistic.y.com.bdfint.bdtrace.service.uyABA", "logistic.", "xx", 0.08F);

    }

    public void testNTimes(String method, String service, String group, String application, float sampler) {
        SamplerResult samplerResult = new SamplerResult();
        ReaderChain chain = new SamplerConfigReaderChain();
        int count = 0;
        long start = System.currentTimeMillis();
        for (int i = 10000; i > 0; i--) {
            chain = new SamplerConfigReaderChain();
            chain.addReaders(readers);
            readers[0].setInterface(method);
            readers[1].setInterface(service);
            readers[2].setInterface(group);
            readers[3].setInterface(application);
            readers[4].setInterface("");

            chain.readForAll(samplerResult);
            if (samplerResult.isSampled)
                count++;
        }
        System.out.println("花费" + (System.currentTimeMillis() - start) + "毫秒");
        Assert.assertEquals((int) (10000 * sampler), count);
    }
}
