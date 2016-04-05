package org.zkfuse.web.vm.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.i18n.ResourceBundleService;
import org.zkfuse.service.i18n.util.ResourceBundleUtil;
import org.zkfuse.util.context.AppContext;
import org.zkfuse.web.util.i18n.KeyValueComparator;
import org.zkfuse.web.util.i18n.LabelsUtil;
import org.zkfuse.web.vm.GenericVM;
import org.zkfuse.web.vm.i18n.status.KeyValueStatus;
import org.zkoss.bind.Form;
import org.zkoss.bind.SimpleForm;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

/**
 * @author Samuel Huang
 */
@Slf4j
public class ResourceBundleVM extends GenericVM<ResourceBundle> {
    
    private ResourceBundle newResourceBundle;
    
    public static final String SET_LOCALE_COMMAND = "set_locale";
    public static final String SET_MODULE_COMMAND = "set_module";
    
    // @Autowired
    ResourceBundleService resourceBundleService;
    
    // list to display on zul
    ListModelList<ResourceBundle> resourceBundleList;
    
    Form myForm = new SimpleForm();
    
    public Form getMyForm() {
        return myForm;
    }
 
    // action commands for CRUD
    
    @Override
    @NotifyChange({"selected","resourceBundleList"})
    @Command
    public void newEntity() {
        newResourceBundle();
    }

    public void newResourceBundle(){
        if ( newResourceBundle == null ) {
            newResourceBundle = new ResourceBundle();
            getResourceBundleList().add( newResourceBundle );
        }
        setSelected( newResourceBundle ); //select the new one
    }
    
    @Override
    public ListModelList<ResourceBundle> getEntityList() {
        return getResourceBundleList();
    }
    
    public ListModelList<ResourceBundle> getResourceBundleList() {
        if (resourceBundleList == null) {
            // init the list
            resourceBundleList = new ListModelList<ResourceBundle>(getResourceBundleService().findAll());
        }
        return resourceBundleList;
    }
    
    @Override
    @Command
    @NotifyChange({"selected", "errorMessage"})
    public void saveEntity() {
        saveResourceBundle(); 
    }
    
    @NotifyChange({"selected", "errorMessage"})
    public void saveResourceBundle() {
        
        if ( getSelected() == null ) {
            return; // nothing to save
        }
        
        if ( getSelected().getResourceBundleId() == null ) {
            
            ResourceBundle existingResourceBundle = getResourceBundleService().getResourceBundle( getSelected() );
            if ( !existingResourceBundle.equals( ResourceBundle.EMPTY_RESOURCE_BUNDLE ) ) {
                setErrorMessage( "There exists a resource bundle with the same name, locale and module in database already.");
                return;
            }
            
            getResourceBundleService().persist(getSelected());  // create new ResourceBundle
            newResourceBundle = null;
            
            // Map<String,Object> args = new HashMap<String,Object>();
            // args.put("resourceBundle", getSelected());
            // BindUtils.postGlobalCommand(null, null, "initResourceBundle", args);
            
        } else {
            getResourceBundleService().merge(getSelected()); // update existing ResourceBundle
        }
    }
    
    
    // command for search & clear buttons
    
    @Command
    public void clearButton(@BindingParam("localeListbox")Listbox localeListbox, @BindingParam("resourceBundleNameTextbox") Textbox  resourceBundleNameTextbox) {
        resourceBundleNameTextbox.setValue("");
    }

    @Command
    @NotifyChange({"selected","resourceBundleList"})
    public void searchResourceBundleByWildCardName(@BindingParam("resourceBundleNameTextbox") Textbox  resourceBundleNameTextbox) {
        String resourceBundleName = resourceBundleNameTextbox.getValue();
        List<ResourceBundle> rbList = getResourceBundleService().getResourceBundleByWildCardName(resourceBundleName);
        resourceBundleList.clear();
        resourceBundleList.addAll( rbList );
        setSelected(null);
    }
    
    @Command
    @NotifyChange({"selected","resourceBundleList"})
    public void getAllResourceBundle() {
        resourceBundleList.clear();
        resourceBundleList.addAll( getResourceBundleService().findAll() );
        setSelected( null );
    }

    // called by messageDialogs.zul
    @Override
    @Command
    @NotifyChange({"selected","resourceBundleList","deleteMessage","errorMessage"})
    public void deleteEntity() {
        try {
            deleteResourceBundle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteResourceBundle(){
        
        ResourceBundle selectedResourceBundle = getSelected();
        boolean isDeleteSuccess = false;
        
        Long selectedResourceBundleId = selectedResourceBundle.getResourceBundleId(); 
        if (  selectedResourceBundleId != null ) {
            isDeleteSuccess = deleteResourceBundleWithErrorHandler( selectedResourceBundle );
        }
        
        setDeleteMessage( null );
        if ( !isDeleteSuccess && selectedResourceBundleId != null) {
            return;  // exit to show error message box for failing to delete
        }

        getResourceBundleList().remove( selectedResourceBundle );
        if ( selectedResourceBundle == newResourceBundle ) {
            newResourceBundle = null;
        }
        setSelected(null); //clean the selected
    }

    @Command
    public void reloadResourceBundle() {
        LabelsUtil.reloadResourceBundle( getSelected() );
    }

    @Command
    public void reloadAllResourceBundle() {
        LabelsUtil.reloadAllResourceBundle();
    }
    
    /** For use by deleteResourceBundle() only.  Return true if delete successfully, false otherwise **/
    private boolean deleteResourceBundleWithErrorHandler(ResourceBundle selectedResourceBundle) {
        try {
            getResourceBundleService().deleteResourceBundle( selectedResourceBundle );
            return true;
        } catch (Exception ex) {
            setErrorMessage( "Unexpectted error: " + ex.getMessage() );
            // log it...
        } 
        return false;
    }

    // confirm delete message

    @NotifyChange("deleteMessage")
    @Command
    public void confirmDelete(){
        //set the message to show user
        setDeleteMessage("Do you want to delete "+ getSelected().getResourceBundleId()+" ?");
    }
    
    // listeners
    
    // getter for service class

    private ResourceBundleService getResourceBundleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (ResourceBundleService)ctx.getBean("resourceBundleServiceImpl");
    }
    
    // global commands
    
    @GlobalCommand
    public void onSelectLocaleListboxListener(@BindingParam("localeListbox") Listbox localeListbox, @BindingParam("namebox") Textbox namebox) {
        
        // close bandbox after selection
        Bandbox localeBandbox = (Bandbox)localeListbox.getParent().getParent();
        localeBandbox.close();
        
        // set display value of selected bandbox
        Locale locale = (Locale)localeListbox.getSelectedItem().getValue();
        localeBandbox.setValue( locale.getLabel()  );

        // set selected module of resource bundle being changed
        String command = (String)localeBandbox.getAttribute("command");
        if (SET_LOCALE_COMMAND.equals(command)) {
            myForm.setField("locale", locale);
            generateNameForResourceBundle( namebox );
        }
    }

    @GlobalCommand
    public void onSelectModuleListboxListener(@BindingParam("moduleListbox") Listbox moduleListbox, @BindingParam("namebox") Textbox namebox) {
        
        // close bandbox after selection
        Bandbox moduleBandbox = (Bandbox)moduleListbox.getParent().getParent();
        moduleBandbox.close();
        
        // set display value of selected bandbox
        Module module = (Module)moduleListbox.getSelectedItem().getValue();
        moduleBandbox.setValue( module.getName() );

        // set selected module of resource bundle being changed
        String command = (String)moduleBandbox.getAttribute("command");
        if (SET_MODULE_COMMAND.equals(command)) {
            myForm.setField("module", module);
            generateNameForResourceBundle( namebox );
        }
    }
    
    @Command
    public void downloadResourceBundleAsPropertiesFile() {
        String fileName = generateFileName();
        try {
            Filedownload.save( convertKeyValuesToInputStream(), "text/html", fileName);
        } catch (IOException io) {
           // log.debug("Failed to download file " + fileName, io );
        }
    }
    
    @NotifyChange( "selected" )
    public void generateNameForResourceBundle(Textbox namebox) {

        Module module = (Module) myForm.getField("module");
        Locale locale = (Locale) myForm.getField("locale");
        String resourceBundleName = ResourceBundleUtil.generateResourceBundleName(locale, module);
        myForm.setField("resourceBundleName", resourceBundleName);
        namebox.setValue(resourceBundleName);
    }
    
    //      validators

    public  org.zkoss.bind.Validator getResourceBundleValidator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                
                // validate module
                String moduleMsgKey = "moduleMsgKey";
                Module module = (Module)myForm.getField("module");
                if( module == null ){
                    addInvalidMessage(ctx, moduleMsgKey, "Module must not be empty");
                } else {
                    clearValidationErrorMessage(ctx, moduleMsgKey);
                    getSelected().setModule(module);
                }
                
                // validate locale
                String localeMsgKey = "localeMsgKey";
                Locale locale = (Locale)myForm.getField("locale");
                if(  locale == null  ){
                    addInvalidMessage(ctx, localeMsgKey, "Locale must not be empty");
                } else {
                    clearValidationErrorMessage(ctx, localeMsgKey);
                    getSelected().setLocale(locale);
                }
            }
        };
    }
    
    // getter & setter for instance variables
    
    //      methods for key value

    public List<KeyValueStatus> getKeyValueStatusList() {
        List<KeyValueStatus> keyValueStatusList = new ArrayList<KeyValueStatus>();
        if ( getSelected() == null ) {
            return keyValueStatusList;
        }
        for ( KeyValue keyValue: getSelected().getKeyValues() ) {
            keyValueStatusList.add( new KeyValueStatus( keyValue, false) );
        }
        return keyValueStatusList;
    }
    
    public InputStream convertKeyValuesToInputStream() throws IOException {
        
        ResourceBundle resourceBundle = getSelected();
        if ( resourceBundle == null ) return null;

        List<KeyValue> keyValueList = resourceBundle.getKeyValues();
        Collections.sort( keyValueList, new KeyValueComparator() );
        
        StringBuffer buffer = new StringBuffer();
        for (KeyValue keyValue: keyValueList) {
            buffer.append(keyValue.getProperty() + "=" + keyValue.getValue() + "\n");
        }
        return IOUtils.toInputStream( buffer.toString(), "utf8" );
    }
    
    public String generateFileName() {
        ResourceBundle resourceBundle = getSelected();
        if ( resourceBundle == null ) return "";
        
        String moduleName = resourceBundle.getModule().getName(); 
        String localeName = resourceBundle.getLocale().getLocaleValue();
        return moduleName + "_" + localeName + ".properties";
    }

}
