package org.zkfuse.webservice.client.un.x509.sym;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class ClientPasswordCallback implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException, 
            UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        System.out.println("testing...");
        if ("alice".equals(pc.getIdentifier())) {
            pc.setPassword("clarinet");
        } // else {...} - can add more users, access DB, etc.
    }
}

