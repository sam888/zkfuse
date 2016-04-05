package org.zkfuse.web.vm.i18n;

import org.zkfuse.model.i18n.Locale;
import org.zkfuse.web.util.i18n.SessionUtil;
import org.zkfuse.web.vm.GenericVM;

/**
 * @author Samuel Huang
 */
public class HelloWorldVM extends GenericVM<Locale> {

    public static final String MODULE_NAME = "TestModule";

    public HelloWorldVM() {
        SessionUtil.setModuleNameInSession( MODULE_NAME );
    }
}
