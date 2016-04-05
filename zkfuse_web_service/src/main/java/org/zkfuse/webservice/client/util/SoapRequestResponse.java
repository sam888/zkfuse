package org.zkfuse.webservice.client.util;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 1/09/13
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class SoapRequestResponse {

    private String soapRequest;
    private String soapResponse;

    public String getSoapRequest() {
        return soapRequest;
    }

    public void setSoapRequest(String soapRequest) {
        this.soapRequest = soapRequest;
    }

    public String getSoapResponse() {
        return soapResponse;
    }

    public void setSoapResponse(String soapResponse) {
        this.soapResponse = soapResponse;
    }
}
