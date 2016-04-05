package org.zkfuse.web.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.Executions;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 25/08/13
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class HttpClientUtil {

//    @Autowired
//    private static PropertiesUtil propertiesUtil;

    public String getWsdlForWebService(String serviceName, String port) throws IOException {

        HttpClient httpclient = new DefaultHttpClient();
        String responseBody = null;
        try {

            String wsdlURL = getAppUrl() +  "/wsservice/" + serviceName + "?wsdl";
            HttpGet httpget = new HttpGet( wsdlURL );

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler. The use of an HTTP response handler guarantees that the underlying HTTP connection
            // will be released back to the connection manager automatically in all cases.
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httpget, responseHandler);

            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return responseBody;
    }

   /* public final static void main(String[] args) throws Exception {

        System.out.println("web app port: "  + propertiesUtil.getWebAppPort());
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet("http://localhost:7070/wsservice/doubleit?wsdl");

            System.out.println("executing request " + httpget.getURI());

            // Create a response handler. The use of an HTTP response handler guarantees that the underlying HTTP connection
            // will be released back to the connection manager automatically in all cases.
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }*/

    public String getAppUrl() {
        String port = ( Executions.getCurrent().getServerPort() == 80 ) ? "" : (":" + Executions.getCurrent().getServerPort());

        return Executions.getCurrent().getScheme() + "://" + Executions.getCurrent().getServerName() + port +
                Executions.getCurrent().getContextPath() ;
    }
}
