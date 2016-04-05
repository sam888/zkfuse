package org.zkfuse.webservice.service.ut.x509.sym;

import org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItPortType;

import javax.jws.WebService;

@WebService(targetNamespace = "http://www.example.org/contract/usernametoken_x509_symmetric/DoubleIt",
            portName="DoubleItPort",
            serviceName="DoubleItService", 
            endpointInterface="org.example.contract.usernametoken_x509_symmetric.doubleit.DoubleItPortType")
public class DoubleItPortTypeImpl implements DoubleItPortType {

    public int doubleIt(int numberToDouble) {
        return numberToDouble * 2;
    }
}
