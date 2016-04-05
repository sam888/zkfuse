
package org.zkfuse.web.ui.macro.i18n;

import java.util.Iterator;

import org.zkfuse.model.i18n.Locale;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * @author Samuel Huang
 */
public class LocaleBandbox extends HtmlMacroComponent {
    
    private static final long serialVersionUID = 1L;
    
    public static final String LOCALE_LISTBOX_ID = "localeListbox";
    public static final String LOCALE_BANDBOX_ID = "localeBandbox";

    public void setLocale(Locale locale) {
        
        final Listbox listbox = (Listbox)getFellow( LOCALE_LISTBOX_ID );
        Bandbox localeBandbox = (Bandbox)listbox.getFellow( LOCALE_BANDBOX_ID );
        if (locale==null) {
            localeBandbox.setValue( "" );
            return;
        }

        Long localeId = locale.getLocaleId();
        setDynamicProperty("locale", locale);
        
        
        if (listbox != null) {
            
            listbox.getListModel();
            for (Iterator<Listitem> it = listbox.getItems().iterator(); it.hasNext();) {
                
                Listitem li = (Listitem) it.next();
                Locale _locale = li.getValue();
                
                if ( localeId.equals( _locale.getLocaleId() ) ) {
                  listbox.setSelectedItem(li);
                  localeBandbox.setValue( _locale.getLabel() );
                  break;
                }
            }
        }
    }
    
    public Long getlocaleId() {
        return (Long)getDynamicProperty("localeId");
    }

}
