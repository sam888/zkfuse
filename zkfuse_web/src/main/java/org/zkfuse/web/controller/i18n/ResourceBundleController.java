package org.zkfuse.web.controller.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 * @author Samuel Huang
 *
 * Not used for now
 */
@Slf4j
@Controller
@Scope("prototype")
public class ResourceBundleController extends SelectorComposer<Window>{

    private static final long serialVersionUID = 1L;
    
    @Wire
    private Listbox localeListbox; 

    @Wire
    private Listbox moduleListbox; 
    
    @Listen("onSelect=#localeListbox")
    public void onSelectLocaleListboxEventListener() {
        Locale selectedLocale = (Locale)localeListbox.getSelectedItem().getValue();
        if (selectedLocale==null) return;
        System.out.println("Locale " + selectedLocale.getLabel() + " is selected...");
    }

    @Listen("onSelect=#moduleListbox")
    public void onSelectModuleListboxEventListener() {
        Module selectedModule = (Module)moduleListbox.getSelectedItem().getValue();
        if (selectedModule==null) return;
        System.out.println("Module " + selectedModule.getName() + " is selected...");
    }

}
