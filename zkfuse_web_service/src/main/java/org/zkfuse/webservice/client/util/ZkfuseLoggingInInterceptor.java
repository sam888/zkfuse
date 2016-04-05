package org.zkfuse.webservice.client.util;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 23/08/13
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.io.InputStream;


public class ZkfuseLoggingInInterceptor extends LoggingInInterceptor {

    private String soapXml;

    public ZkfuseLoggingInInterceptor() {
        super(Phase.RECEIVE);
    }

    // Credits: http://cxf.547215.n5.nabble.com/CXF-Spring-client-getting-the-message-s-XML-payload-td556049.html
    @Override
    public void handleMessage(Message message) throws Fault {

        InputStream is = message.getContent(InputStream.class);
        if (is != null) {
            CachedOutputStream bos = new CachedOutputStream();
            try {
                IOUtils.copy(is, bos);

                bos.flush();// you get the soap xml now
                is.close();

                // don't forget to write inputstream back, the inputstream is still needed for next invoked interceptors
                message.setContent(InputStream.class, bos.getInputStream());

                StringBuilder builder = new StringBuilder();
                bos.writeCacheTo(builder, limit);

                // here comes my xml:
                soapXml = builder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getSoapXml() {
        return soapXml;
    }
}