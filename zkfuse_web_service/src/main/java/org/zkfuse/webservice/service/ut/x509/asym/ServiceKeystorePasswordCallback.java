package org.zkfuse.webservice.service.ut.x509.asym;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServiceKeystorePasswordCallback implements CallbackHandler {
    
    private Map<String, String> passwords = 
        new HashMap<String, String>();
    
    public ServiceKeystorePasswordCallback() {
        passwords.put("myservicekey", "skpass"); // for private key with alias 'myservicekey'
        passwords.put("joe", "joepassword"); // for username token
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];

            String pass = passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }
    }   
}

