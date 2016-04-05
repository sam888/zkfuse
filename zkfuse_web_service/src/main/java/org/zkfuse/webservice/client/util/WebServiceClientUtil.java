package org.zkfuse.webservice.client.util;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.springframework.stereotype.Component;
import org.zkfuse.webservice.WebServiceNameEnum;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@Component
public class WebServiceClientUtil {

    public SoapRequestResponse callWebService(int numToDouble, String webServiceId) throws Exception {

        SoapRequestResponse soapRequestResponse = null;
        if (WebServiceNameEnum.USERNAME_TOKEN_X509_SYMMETRIC_WS.getId().equals( webServiceId ) ) {

            soapRequestResponse = callUsernameTokenX509SymmetricDoubleItWS( numToDouble );

        } else if (WebServiceNameEnum.USERNAME_TOKEN_HASHED_PASSWORD_WS.getId().equals( webServiceId ) ) {

            soapRequestResponse = callUsernameTokenHashedPasswordDoubleItWS(numToDouble);

        } else if (WebServiceNameEnum.USERNAME_TOKEN_X509_ASYMMETRIC_WS.getId().equals( webServiceId ) ) {

            soapRequestResponse = callUsernameTokenX509AsymmetricDoubleItWS(numToDouble);

        } else if (WebServiceNameEnum.X509_ASYMMETRIC_WS.getId().equals( webServiceId ) ) {

            soapRequestResponse = callX509AsymmetricDoubleItWS(numToDouble);
        }

        return soapRequestResponse;
    }

    private SoapRequestResponse callX509AsymmetricDoubleItWS(int numToDouble) {
        org.example.contract.x509_asymmetric.doubleit.DoubleItService service = new
                org.example.contract.x509_asymmetric.doubleit.DoubleItService();
        org.example.contract.x509_asymmetric.doubleit.DoubleItPortType port = service.getDoubleItPort();

        ZkfuseLoggingInInterceptor zkfuseLoggingInInterceptor = new ZkfuseLoggingInInterceptor();
        ZkfuseLoggingOutInterceptor zkfuseLoggingOutInterceptor = new ZkfuseLoggingOutInterceptor();

        Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(zkfuseLoggingInInterceptor);
        client.getOutInterceptors().add(zkfuseLoggingOutInterceptor);

        int resp = port.doubleIt(numToDouble);
        System.out.println("The number " + numToDouble + " doubled is " + resp);
        return getProcessedSoapRequestResponse(zkfuseLoggingInInterceptor, zkfuseLoggingOutInterceptor);
    }

    private SoapRequestResponse callUsernameTokenX509AsymmetricDoubleItWS(int numToDouble) {
        org.example.contract.usernametoken_x509_asymmetric.doubleit.DoubleItService service = new
                org.example.contract.usernametoken_x509_asymmetric.doubleit.DoubleItService();
        org.example.contract.usernametoken_x509_asymmetric.doubleit.DoubleItPortType port = service.getDoubleItPort();

        ZkfuseLoggingInInterceptor zkfuseLoggingInInterceptor = new ZkfuseLoggingInInterceptor();
        ZkfuseLoggingOutInterceptor zkfuseLoggingOutInterceptor = new ZkfuseLoggingOutInterceptor();

        Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(zkfuseLoggingInInterceptor);
        client.getOutInterceptors().add(zkfuseLoggingOutInterceptor);

        int resp = port.doubleIt(numToDouble);
        System.out.println("The number " + numToDouble + " doubled is " + resp);
        return getProcessedSoapRequestResponse(zkfuseLoggingInInterceptor, zkfuseLoggingOutInterceptor);
    }

    private SoapRequestResponse callUsernameTokenX509SymmetricDoubleItWS(int numToDouble) {

        org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItService service = new
                org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItService();
        org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItPortType port = service.getDoubleItPort();

        ZkfuseLoggingInInterceptor zkfuseLoggingInInterceptor = new ZkfuseLoggingInInterceptor();
        ZkfuseLoggingOutInterceptor zkfuseLoggingOutInterceptor = new ZkfuseLoggingOutInterceptor();

        Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(zkfuseLoggingInInterceptor);
        client.getOutInterceptors().add(zkfuseLoggingOutInterceptor);

        // client.getInInterceptors().add(new LoggingInInterceptor());
        // client.getOutInterceptors().add(new LoggingOutInterceptor());

        int resp = port.doubleIt(numToDouble);
        System.out.println("The number " + numToDouble + " doubled is " + resp);
        return getProcessedSoapRequestResponse(zkfuseLoggingInInterceptor, zkfuseLoggingOutInterceptor);
    }

   private SoapRequestResponse callUsernameTokenHashedPasswordDoubleItWS(int numToDouble) {

        org.example.contract.usernametoken_hashed_password.doubleit.DoubleItService service = new
                org.example.contract.usernametoken_hashed_password.doubleit.DoubleItService();
        org.example.contract.usernametoken_hashed_password.doubleit.DoubleItPortType port = service.getDoubleItPort();

        ZkfuseLoggingInInterceptor zkfuseLoggingInInterceptor = new ZkfuseLoggingInInterceptor();
        ZkfuseLoggingOutInterceptor zkfuseLoggingOutInterceptor = new ZkfuseLoggingOutInterceptor();

        Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(zkfuseLoggingInInterceptor);
        client.getOutInterceptors().add(zkfuseLoggingOutInterceptor);

        int resp = port.doubleIt(numToDouble);
        System.out.println("The number " + numToDouble + " doubled is " + resp);
        return getProcessedSoapRequestResponse(zkfuseLoggingInInterceptor, zkfuseLoggingOutInterceptor);
    }

    private SoapRequestResponse getProcessedSoapRequestResponse(ZkfuseLoggingInInterceptor zkfuseLoggingInInterceptor,
                                                                ZkfuseLoggingOutInterceptor zkfuseLoggingOutInterceptor) {
        SoapRequestResponse soapRequestResponse = new SoapRequestResponse();
        soapRequestResponse.setSoapRequest( prettyFormat(zkfuseLoggingOutInterceptor.getSoapXml(), 3) ); // request
        soapRequestResponse.setSoapResponse( prettyFormat(zkfuseLoggingInInterceptor.getSoapXml(), 3) ); // response
        return soapRequestResponse;
    }

    public String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

}
