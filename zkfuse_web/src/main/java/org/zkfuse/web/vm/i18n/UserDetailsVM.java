package org.zkfuse.web.vm.i18n;

import org.zkfuse.model.i18n.Locale;
import org.zkfuse.web.util.i18n.SessionUtil;
import org.zkfuse.web.vm.GenericVM;

/**
 * @author Samuel Huang
 */
public class UserDetailsVM extends GenericVM<Locale> {

    public static final String MODULE_NAME = "UserDetails";

    public UserDetailsVM() {
        SessionUtil.setModuleNameInSession( MODULE_NAME );
        SessionUtil.setWindowLabelKeyInSession( "user.details" );
    }
}
