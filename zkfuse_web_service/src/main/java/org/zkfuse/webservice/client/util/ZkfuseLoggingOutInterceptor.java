package org.zkfuse.webservice.client.util;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 23/08/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.io.OutputStream;

public class ZkfuseLoggingOutInterceptor extends LoggingOutInterceptor {

    private String soapXml;

    public ZkfuseLoggingOutInterceptor() {
        super(Phase.PRE_STREAM);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        OutputStream out = message.getContent(OutputStream.class);
        final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(out);
        message.setContent(OutputStream.class, newOut);
        newOut.registerCallback(new LoggingCallback());
    }

    public class LoggingCallback implements CachedOutputStreamCallback {
        public void onFlush(CachedOutputStream cos) {
        }

        public void onClose(CachedOutputStream cos) {
            if ( cos == null ) return;
            try {
                StringBuilder builder = new StringBuilder();

                cos.writeCacheTo(builder, limit);
                // here comes my xml:
                soapXml = builder.toString();
                System.out.println("MyLogOutInterceptor: soapXml: " + soapXml);
            } catch (Exception e) {
            }
        }
    }

    public String getSoapXml() {
        return soapXml;
    }
}