package org.zkfuse.web.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 26/08/13
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public final class PropertiesUtil {

    @Value("${webapp.port}")
    private String webAppPort;

    public String getWebAppPort() {
        return webAppPort;
    }

}
