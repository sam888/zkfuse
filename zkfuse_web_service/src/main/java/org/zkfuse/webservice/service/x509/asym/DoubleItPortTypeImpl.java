package org.zkfuse.webservice.service.x509.asym;

import org.example.contract.usernametoken_x509_asymmetric.doubleit.DoubleItPortType;

import javax.jws.WebService;

@WebService(targetNamespace = "http://www.example.org/contract/x509_asymmetric/DoubleIt",
            portName="DoubleItPort",
            serviceName="DoubleItService", 
            endpointInterface="org.example.contract.x509_asymmetric.doubleit.DoubleItPortType")
public class DoubleItPortTypeImpl implements DoubleItPortType {

    public int doubleIt(int numberToDouble) {
        return numberToDouble * 2;
    }
}
