package org.zkfuse.web.ui.macro.i18n;

import java.util.Iterator;

import org.zkfuse.model.i18n.Module;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * @author Samuel Huang
 */
public class ModuleBandbox extends HtmlMacroComponent {
    
    private static final long serialVersionUID = 1L;
    public static final String MODULE_LISTBOX_ID = "moduleListbox";
    public static final String MODULE_BANDBOX_ID = "moduleBandbox";
    
    public void setModule(Module module) {
        
        final Listbox listbox = (Listbox)getFellow( MODULE_LISTBOX_ID );
        Bandbox moduleBandbox = (Bandbox)listbox.getFellow( MODULE_BANDBOX_ID );
        if ( module == null ) {
            moduleBandbox.setValue( "" );
            return;
        }
        
        Long moduleId = module.getModuleId();
        setDynamicProperty("module", module);

        if (listbox != null) {
            
            listbox.getListModel();
            for (Iterator<Listitem> it = listbox.getItems().iterator(); it.hasNext();) {
                
                Listitem li = (Listitem) it.next();
                Module _module = li.getValue();
                
                if ( moduleId.equals( _module.getModuleId() ) ) {
                  listbox.setSelectedItem(li);
                  moduleBandbox.setValue( _module.getName() );
                  break;
                }
            }
        }
    }
    
    public Module getModule() {
        return (Module)getDynamicProperty("module");
    }

}
