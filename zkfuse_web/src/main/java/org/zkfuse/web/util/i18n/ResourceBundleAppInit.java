package org.zkfuse.web.util.i18n;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

/**
 * Not used for now. Only requried if use org.zkoss.util.resource.Labels.register(...)
 *
 * @author Samuel Huang
 */
public class ResourceBundleAppInit implements WebAppInit {

    @Override   
    public void init(WebApp wapp) throws Exception {
        // Labels.register( new DBResourceBundleLocator() );
    }

}
