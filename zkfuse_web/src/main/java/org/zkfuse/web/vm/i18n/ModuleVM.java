package org.zkfuse.web.vm.i18n;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.service.i18n.ModuleService;
import org.zkfuse.util.context.AppContext;
import org.zkfuse.web.vm.GenericVM;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

/**
 * @author Samuel Huang
 */
@Slf4j
public class ModuleVM extends GenericVM<Module> {
    
    private Module newModule;

    // @Autowired
    ModuleService moduleService;

    // list to display on zul
    ListModelList<Module> moduleList;

    // action commands
    
    @Override
    @Command
    @NotifyChange({"selected","moduleList"})
    public void newEntity() {
        newModule();
    }
    
    public void newModule(){
        if ( newModule == null ) {
            newModule = new Module();
            getModuleList().add( newModule );
        }
        setSelected( newModule );//select the new one
    }
    
    @Override
    public ListModelList<Module> getEntityList() {
        return getModuleList();
    }
    
    public ListModelList<Module> getModuleList() {
        if (moduleList == null) {
            // init the list
            moduleList = new ListModelList<Module>(getModuleService().findAll());
        }
        return moduleList;
    }

    @Override
    @Command
    @NotifyChange({"selected", "errorMessage"})
    public void saveEntity() {
        saveModule(); 
    }
    
    public void saveModule( ){
        if ( getSelected().getModuleId() == null ) {
            
            List<Module> _moduleList = moduleList.getInnerList();
            for (Module module: _moduleList) {
                
                if ( module == getSelected() ) continue;

                if ( module.getName().equalsIgnoreCase( getSelected().getName() ) ) {
                    setErrorMessage( "There exists a Module with the same Name already. Name must be unique");
                    return;
                }
            }
            
            getModuleService().persist(getSelected());
            newModule = null;
        } else {
            getModuleService().merge(getSelected());
        }
    }
    
    // called by messageDialogs.zul
    @Command
    @NotifyChange({"selected","localeList","deleteMessage","errorMessage"})
    public void deleteEntity() {
        deleteModule();
    }
    
    public void deleteModule(){
        
        Module selectedModule = getSelected();
        boolean isDeleteSuccess = false;
        
        Long selectedModuleId = selectedModule.getModuleId(); 
        if ( selectedModuleId != null ) {
            isDeleteSuccess = deleteModuleWithErrorHandler(selectedModule);
        }
        
        setDeleteMessage( null );
        if ( !isDeleteSuccess && selectedModuleId != null) {
            return;  // exit to show error message box for failing to delete
        }
        
        getModuleList().remove(selectedModule);
        if ( newModule == selectedModule ) {
            newModule = null;
        }
        setSelected( null );
    }
    
    /** For use by deleteModule() only.  Return true if delete successfully, false otherwise **/
    private boolean deleteModuleWithErrorHandler(Module selectedModule) {
        try {
            getModuleService().deleteModule( selectedModule );
            return true;
        } catch (DataIntegrityViolationException ex) {
            setErrorMessage( "Can not delete module as there exists child ResourceBundle record. Try delete child record first." );
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
        // set the message to show user
        setDeleteMessage( "Are you sure you want to delete "+ getSelected() + " ?" );
    }
    
    // getter for service class
    
    private ModuleService getModuleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (ModuleService)ctx.getBean("moduleServiceImpl");
    }

}
