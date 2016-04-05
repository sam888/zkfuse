package org.zkfuse.web.vm.i18n;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.service.i18n.LocaleService;
import org.zkfuse.util.context.AppContext;
import org.zkfuse.web.vm.GenericVM;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

/**
 * @author Samuel Huang
 */
public class LocaleVM extends GenericVM<Locale> {
    
    // an entity that hasn't been saved to DB
    private Locale newLocale;

    // @Autowired
    LocaleService localeService;

    // list to display on zul
    ListModelList<Locale> localeList;
    
    Listbox localeListbox;

    // action commands for CRUD
    
    public Listbox getLocaleListbox() {
        return localeListbox;
    }

    public void setLocaleListbox(Listbox localeListbox) {
        this.localeListbox = localeListbox;
    }

    @Override
    @Command
    @NotifyChange({"selected","localeList"})
    public void newEntity() {
        newLocale();
    }
    
    public void newLocale(){
        if ( newLocale == null ) {
            newLocale = new Locale();
            getLocaleList().add( newLocale );
        }
        setSelected( newLocale );//select the new one
    }
    
    @Override
    public ListModelList<Locale> getEntityList() {
        return getLocaleList();
    }

    public ListModelList<Locale> getLocaleList() {
        if (localeList == null) {
            // init the list
            localeList = new ListModelList<Locale>(getLocaleService().findAll());
        }
        return localeList;
    }
    
    @Override
    @Command
    @NotifyChange({"selected", "errorMessage"})
    public void saveEntity() {
        saveLocale(); 
    }
    
    public void saveLocale( ){
        if ( getSelected().getLocaleId() == null ) {

            List<Locale> _localeList = localeList.getInnerList();
            for (Locale locale: _localeList) {
                
                if ( locale == getSelected() ) continue;

                if ( locale.getName().equalsIgnoreCase( getSelected().getName() ) ) {
                    setErrorMessage( "There exists a Locale with the same Name already. Name must be unique");
                    return;
                }
                if ( locale.getCountryCode().equalsIgnoreCase( getSelected().getCountryCode() )  && 
                     locale.getLanguageCode().equalsIgnoreCase( getSelected().getLanguageCode() ) &&
                     locale.getVariantCode().equalsIgnoreCase( getSelected().getVariantCode() ) ) {
                    setErrorMessage( "There exists a Locale with the same Language, Country and Variant codes already. They must be unique");
                    return;
                }
                
            }

            getLocaleService().persist( getSelected() );
            newLocale = null;

        } else {
            getLocaleService().merge( getSelected() );
        }
    }
    
    // called by messageDialogs.zul
    @Override
    @Command
    @NotifyChange({"selected","localeList","deleteMessage","errorMessage"})
    public void deleteEntity() {
        deleteLocale();
    }
    
    public void deleteLocale(){
        
        Locale selectedLocale = getSelected();
        boolean isDeleteSuccess = false;
        
        Long selectedLocaleId = selectedLocale.getLocaleId();
        if ( selectedLocaleId != null ) {
            isDeleteSuccess = deleteLocaleWithErrorHandler(selectedLocale);
        }
        
        setDeleteMessage( null );
        if ( !isDeleteSuccess && selectedLocaleId != null ) {
            return;  // exit to show error message box for failing to delete
        }
      
        getLocaleList().remove( selectedLocale );
        if ( selectedLocale == newLocale ) {
            newLocale = null;
        }
        setSelected( null );
    }
    
    /** For use by deleteLocale() only. Return true if delete successfully, false otherwise **/
    private boolean deleteLocaleWithErrorHandler(Locale selectedLocale) {
        try {
            getLocaleService().deleteLocale( selectedLocale );
            return true;
        } catch (DataIntegrityViolationException ex) {
            setErrorMessage( "Can not delete locale as there exists child ResourceBundle record. Try delete child record first." );
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
        setDeleteMessage( "Are you sure you want to delete " + getSelected().toString() + " ?" );
    }
    
    // getter for service class

    private LocaleService getLocaleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (LocaleService)ctx.getBean("localeServiceImpl");
    }
    
    // listener
    
    @Listen("onSelect=#localeListbox")
    public void onSelectLocaleListboxEventListener() {
        Locale selectedLocale = (Locale)localeListbox.getSelectedItem().getValue();
        if (selectedLocale==null) {
            return;
        }
    }

}
