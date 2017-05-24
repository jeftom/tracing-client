package com.bdfint.bdtrace.functionable;

import com.alibaba.dubbo.rpc.Invocation;

import java.util.Map;

/**
 * @author heyb
 * @date 2017/5/18.
 * @desriptioin
 */
public interface IAttachmentTransmittable {

    Map<String, String> getAttachments();

    void putAttachmentsFrom(Invocation invocation);
}
