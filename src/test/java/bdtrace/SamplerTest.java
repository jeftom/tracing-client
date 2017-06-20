package bdtrace;

import com.github.kristofa.brave.Sampler;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * @author heyb
 * @date 2017/6/19.
 * @desriptioin
 */
public class SamplerTest {

    @Test
    public void test() {
        for (int j = 0; j < 10; j++) {

            float percentage = 0.11F;
            Sampler sampler = Sampler.create(percentage);
            boolean[] rs = new boolean[100];
            int count = 0;
            for (int i = 0; i < 100; i++) {
                boolean sampled = sampler.isSampled(new Random().nextLong());
                rs[i] = sampled;
                if (sampled)
                    count++;
            }
            Assert.assertEquals((int) (percentage * 100), count);

            count = 0;
            for (int i = 0; i < 100; i++) {
                boolean sampled = sampler.isSampled(0);
                rs[i] = sampled;
                if (sampled)
                    count++;
            }
            Assert.assertEquals((int) (percentage * 100), count);
        }

    }
}
