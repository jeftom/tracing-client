package com.bdfint.bdtrace;

import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.support.Annotated;
import zipkin.Annotation;
import zipkin.Endpoint;

/**
 * @author heyb
 * @date 2017/5/22.
 * @desriptioin
 */
public class AnnotatedImpl implements Annotated {
    Annotation cs;
    Annotation sr;
    Annotation ss;
    Annotation cr;

    long c2s = 0L;
    long remote = 0L;
    long s2c = 0L;
    long whole = 0L;


    public long getC2s() {
        c2s = sr.timestamp - cs.timestamp;
        return c2s;
    }

    public long getRemote() {
        remote = ss.timestamp - sr.timestamp;
        return remote;
    }

    public long getS2c() {
        s2c = cr.timestamp - ss.timestamp;
        return s2c;
    }

    public long getWhole() {
        whole = cr.timestamp - cs.timestamp;
        return whole;
    }

    public Annotation clientSent(String interfaceName, DubboClientRequestAdapter clientRequestAdapter) {
        Annotation annotation = Annotation.create(System.currentTimeMillis(), zipkin.Constants.CLIENT_SEND, Endpoint.create(interfaceName, clientRequestAdapter.serverAddress().ipv4));
        this.cs = annotation;
        return annotation;
    }

    public Annotation serverReceived(String interfaceName, DubboServerRequestAdapter serverRequestAdapter) {
        return null;
    }

    public Annotation serverSent(String interfaceName, DubboServerResponseAdapter serverResponseAdapter) {
        return null;
    }

    public Annotation clientReceived(String interfaceName, DubboClientResponseAdapter clientResponseAdapter) {
        return null;
    }
}
