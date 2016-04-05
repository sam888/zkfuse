package org.zkfuse.web.util.i18n;

import java.util.Locale;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

/**
 * @author Samuel Huang
 */
public class SessionUtil {
    
    public static final String LOCALE_ATTRIBUTE = "locale";
    public static String MODULE_NAME_ATTRIBUTE = "moduleName";
    
    public static String WINDOW_LABEL_KEY_ATTRIBUTE = "windowLabelKey";
    
    public static void setLocaleInSession(Locale locale) {
        Session session = Sessions.getCurrent();
        session.setAttribute( LOCALE_ATTRIBUTE,  locale );
        //Locales.setThreadLocal( locale );
    }
    public static Locale getLocaleInSession() {
        Session session = Sessions.getCurrent();
        return (Locale)session.getAttribute( LOCALE_ATTRIBUTE );
    }
    
    public static String getModuleNameInSession() {
        Session session = Sessions.getCurrent();
        return (String)session.getAttribute( MODULE_NAME_ATTRIBUTE );
    }
    public static void setModuleNameInSession(String moduleName) {
        Session session = Sessions.getCurrent();
        session.setAttribute( MODULE_NAME_ATTRIBUTE,  moduleName );
    }

    public static String getWindowLabelKeyInSession() {
        Session session = Sessions.getCurrent();
        return (String)session.getAttribute( WINDOW_LABEL_KEY_ATTRIBUTE );
    }
    public static void setWindowLabelKeyInSession(String windowLabelKey) {
        Session session = Sessions.getCurrent();
        session.setAttribute( WINDOW_LABEL_KEY_ATTRIBUTE,  windowLabelKey );
    }
    
}
