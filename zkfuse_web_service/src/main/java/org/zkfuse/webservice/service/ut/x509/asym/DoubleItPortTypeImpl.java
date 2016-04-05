package org.zkfuse.webservice.service.ut.x509.asym;

import org.example.contract.usernametoken_x509_asymmetric.doubleit.DoubleItPortType;

import javax.jws.WebService;

@WebService(targetNamespace = "http://www.example.org/contract/usernametoken_x509_asymmetric/DoubleIt",
            portName="DoubleItPort",
            serviceName="DoubleItService", 
            endpointInterface="org.example.contract.usernametoken_x509_asymmetric.doubleit.DoubleItPortType")
public class DoubleItPortTypeImpl implements DoubleItPortType {

    public int doubleIt(int numberToDouble) {
        return numberToDouble * 2;
    }
}
