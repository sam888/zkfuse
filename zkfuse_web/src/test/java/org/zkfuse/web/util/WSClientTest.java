package org.zkfuse.web.util;

//import org.apache.cxf.endpoint.Client;
//import org.apache.cxf.frontend.ClientProxy;
//import org.apache.cxf.interceptor.LoggingInInterceptor;
//import org.apache.cxf.interceptor.LoggingOutInterceptor;
//import org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItPortType;
//import org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItService;
//import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:spring/applicationContext-ws.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class WSClientTest {

   /* @Test
    public void testDoubleIt() throws Exception {
        DoubleItService service = new DoubleItService();
        DoubleItPortType port = service.getDoubleItPort();

        // next three lines optional, they output the SOAP request/response XML
        Client client = ClientProxy.getClient(port);
        client.getInInterceptors().add(new LoggingInInterceptor());
        client.getOutInterceptors().add(new LoggingOutInterceptor()); 

        doubleIt(port, 10);
        doubleIt(port, -10);
    } */
    
    /*public void doubleIt(DoubleItPortType port, int numToDouble) {
        int resp = port.doubleIt(numToDouble);
        System.out.println("The number " + numToDouble + " doubled is " + resp);
    }*/
}

