package bdtrace;

import com.bdfint.bdtrace.bean.SamplerResult;
import com.bdfint.bdtrace.chain.ReaderChain;
import com.bdfint.bdtrace.chain.sampler.*;
import com.bdfint.bdtrace.function.ServiceInfoProvider;
import com.bdfint.bdtrace.functionable.ServiceInfoProvidable;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author heyb
 * @date 2017/6/26.
 * @desriptioin
 */
public class SamplerChainTest {

    @Test
    public void test() {
        //在给定的采样次数下（100 倍数）,根据每次的服务名称，断定是否走了正确的链路

        //采样处理
        ServiceSamplerConfigReader serviceSamplerConfigReader = new ServiceSamplerConfigReader();
        AbstractSamplerConfigReader[] readers = {
                new MethodSamplerConfigReader(),
                new ServiceSamplerConfigReader(),
                new GroupSamplerConfigReader(),
                new ApplicationSamplerConfigReader(),
                new GlobalSamplerConfigReader()
        };
        SamplerResult samplerResult = new SamplerResult();
        AbstractSamplerConfigReader reader = new GlobalSamplerConfigReader();
        ReaderChain chain = new SamplerConfigReaderChain();
        chain.addReaders(readers);
        ServiceInfoProvidable serviceInfoProvidable = new ServiceInfoProvider();
        String method = "logistic.x.com.bdfint.bdtrace.service.Buy.buy";
        String service = "logistic.y.com.bdfint.bdtrace.service.BuyAA";
        String group = "logistic.y";
        String application = "logistic";
        boolean sample = true;
        int count = 0;

        long start = System.currentTimeMillis();
        for (int i = 10000; i > 0; i--) {
            chain = new SamplerConfigReaderChain();
            chain.addReaders(readers);
            readers[0].setInterface(method);
            service = "";
            readers[1].setInterface(service);
            group = "";
            readers[2].setInterface(group);
            application = "";
            readers[3].setInterface(application);
            readers[4].setInterface("sampler");

            chain.readForAll(samplerResult);
            System.out.println(i);
            if (samplerResult.isSampled)
                count++;
        }
        System.out.println(System.currentTimeMillis() - start);
        Assert.assertEquals((int) (10000 * 0.1), count);
    }
}
