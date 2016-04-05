package org.zkfuse.web.composer;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.zkfuse.web.util.i18n.LabelsUtil;
import org.zkfuse.web.util.i18n.SessionUtil;
import org.zkoss.bind.BindComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

/**
 * @author Samuel Huang
 *
 * Extend this class for any page using MVVM pattern(i.e. backed by view model) and needs to be
 * internationalized by using locale set in session.
 */
@SuppressWarnings("rawtypes")
public class BaseBindComposer extends BindComposer {
    
    private static final long serialVersionUID = 1L;
    
    public void doBeforeComposeChildren(Component comp) throws Exception {

        // this will call constructor of associated ViewModel to set moduleName in session
        super.doBeforeComposeChildren(comp); 

        registerNonExistingDBResourceBundleLocator();
        setWindowTitle( comp );
    }
    
    protected void registerNonExistingDBResourceBundleLocator() {
        Locale localeInSession = SessionUtil.getLocaleInSession();
        String moduleName = SessionUtil.getModuleNameInSession();
        LabelsUtil.registerNonExistingDBResourceBundleLocator(localeInSession, moduleName);
    }
    
    protected void setWindowTitle(Component component) {
        String windowlabelKey = SessionUtil.getWindowLabelKeyInSession();
        if ( component instanceof Window && StringUtils.isNotBlank( windowlabelKey )) {
            Window window = (Window)component;
            window.setTitle( LabelsUtil.getLabel( windowlabelKey ) );
        }
    }

    // Getters & setterd
}
