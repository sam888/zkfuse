package org.zkfuse.webservice.service.ut.hashedpassword;

import org.example.contract.usernametoken_hashed_password.doubleit.DoubleItPortType;

import javax.jws.WebService;

@WebService(targetNamespace = "http://www.example.org/contract/usernametoken_hashed_password/DoubleIt",
            portName="DoubleItPort",
            serviceName="DoubleItService", 
            endpointInterface="org.example.contract.usernametoken_hashed_password.doubleit.DoubleItPortType")
public class DoubleItPortTypeImpl implements DoubleItPortType {

    public int doubleIt(int numberToDouble) {
        return numberToDouble * 2;
    }
}
