package com.bdfint.bdtrace.function;

import com.bdfint.bdtrace.adapter.DubboClientRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboClientResponseAdapter;
import com.bdfint.bdtrace.adapter.DubboServerRequestAdapter;
import com.bdfint.bdtrace.adapter.DubboServerResponseAdapter;
import com.bdfint.bdtrace.functionable.Annotated;
import com.bdfint.bdtrace.functionable.AnnotationTimestamp;
import zipkin.Annotation;
import zipkin.Constants;
import zipkin.Endpoint;

/**
 * @author heyb
 * @date 2017/5/22.
 * @desriptioin
 */
public class AnnotatedImpl implements Annotated, AnnotationTimestamp {
    Annotation cs;
    Annotation sr;
    Annotation ss;
    Annotation cr;

    long c2s = 0L;
    long remote = 0L;
    long s2c = 0L;
    long whole = 0L;


    public long c2sElapse() {
        c2s = sr.timestamp - cs.timestamp;
        return c2s;
    }

    public long remoteElapse() {
        remote = ss.timestamp - sr.timestamp;
        return remote;
    }

    /**
     * MUST call after <B>cr!</B>
     *
     * @return
     */
    public long s2cElapse() {
//        s2c = cr.timestamp - ss.timestamp;
        s2c = whole - c2s - remote;
        return s2c;
    }

    public long wholeElapse() {
        whole = cr.timestamp - cs.timestamp;
        return whole;
    }

    public Annotation clientSent(String interfaceName, DubboClientRequestAdapter clientRequestAdapter) {
        Annotation annotation = Annotation.create(System.currentTimeMillis(), zipkin.Constants.CLIENT_SEND, Endpoint.create(interfaceName, clientRequestAdapter.serverAddress().ipv4));
        this.cs = annotation;
        return annotation;
    }

    public Annotation serverReceived(String interfaceName, DubboServerRequestAdapter serverRequestAdapter) {
        Annotation annotation = Annotation.create(System.currentTimeMillis(), zipkin.Constants.SERVER_RECV, Endpoint.create(interfaceName, 0));
        this.sr = annotation;
//        c2sElapse();
        return annotation;
    }

    /**
     * lazy load?how?
     *
     * @param interfaceName
     * @param serverResponseAdapter
     * @return
     */
    public Annotation serverSent(String interfaceName, DubboServerResponseAdapter serverResponseAdapter) {
        Annotation annotation = Annotation.create(System.currentTimeMillis(), Constants.CLIENT_RECV, Endpoint.create(interfaceName, 0));
        this.sr = annotation;
        remoteElapse();
        return annotation;
    }

    public Annotation clientReceived(String interfaceName, DubboClientResponseAdapter clientResponseAdapter) {
        Annotation annotation = Annotation.create(System.currentTimeMillis(), Constants.CLIENT_RECV, Endpoint.create(interfaceName, 0));
        this.sr = annotation;
        wholeElapse();
        s2cElapse();
        return annotation;
    }

    @Override
    public long cs() {
        return cs.timestamp;
    }

    @Override
    public long sr() {
        return sr.timestamp;
    }

    @Override
    public long ss() {
        return ss.timestamp;
    }

    @Override
    public long cr() {
        return cr.timestamp;
    }
}
