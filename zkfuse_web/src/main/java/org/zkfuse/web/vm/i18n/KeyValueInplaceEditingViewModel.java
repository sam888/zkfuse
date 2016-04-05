package org.zkfuse.web.vm.i18n;

import java.util.ArrayList;
import java.util.List;

import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.i18n.KeyValueService;
import org.zkfuse.service.i18n.ServiceLocator;
import org.zkfuse.web.vm.GenericVM;
import org.zkfuse.web.vm.i18n.status.KeyValueStatus;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

/**
 * @author Samuel Huang
 */
public class KeyValueInplaceEditingViewModel extends GenericVM<KeyValue> {

    private boolean displayEdit = true;
    private ResourceBundle resourceBundle;
    
    private KeyValueStatus selectedKeyValueStatus;
    
    private List<KeyValueStatus> keyValueStatusList = new ArrayList<KeyValueStatus>();
    

    public void initResourceBundle( ResourceBundle resourceBundle ) {
        this.resourceBundle = resourceBundle;
    }
    
    public List<KeyValueStatus> getKeyValueStatusList( ) {
        return keyValueStatusList;
    }

    @NotifyChange({"keyValueStatusList"})
    public List<KeyValueStatus> loadKeyValueStatusList(ResourceBundle resourceBundle) {
        
        keyValueStatusList = new ArrayList<KeyValueStatus>();
        if ( resourceBundle == null ) {
            return keyValueStatusList;
        }
        
        this.resourceBundle = resourceBundle;
        List<KeyValue> keyValueList = resourceBundle.getKeyValues();
        for ( KeyValue keyValue: keyValueList ) {
            keyValueStatusList.add( new KeyValueStatus( keyValue, false) );
        }
        return keyValueStatusList;
    }

    @Override
    public void newEntity() {
        
    }
    
    @Override
    @Command
    @NotifyChange("deleteMessage")
    public void cancelDelete() {
        
        resetSelectedKeyValueAndStatusToNull();
        setDeleteMessage(null);
    }

    @Override
    @Command
    @NotifyChange({"deleteMessage","errorMessage"})
    public void deleteEntity() {
        
        deleteKeyValue( getSelected() );
        changeEditableStatus( getSelectedKeyValueStatus() );
        keyValueStatusList.remove( getSelectedKeyValueStatus() );
        
        resetSelectedKeyValueAndStatusToNull();
        setDeleteMessage(null);
    }

    @Override
    public void saveEntity() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ListModelList<KeyValue> getEntityList() {
        return null;
    }
    
    public boolean isDisplayEdit() {
        return displayEdit;
    }
    
    @NotifyChange({"keyValueStatusList", "displayEdit"})
    public void setDisplayEdit(boolean displayEdit) {
        this.displayEdit = displayEdit;
    }
    
    @Command
    public void confirmSave(@BindingParam("keyValueStatus") KeyValueStatus keyValueStatus) {
        saveOrUpdateKeyValue( keyValueStatus.getKeyValue() );
        changeEditableStatus( keyValueStatus );
    }

    @Command
    @NotifyChange("deleteMessage")
    public void confirmDelete(@BindingParam("keyValueStatus") KeyValueStatus keyValueStatus) {
        
        KeyValue keyValue = keyValueStatus.getKeyValue();
        setDeleteMessage("Are you sure you want to delete KeyValue with Id=" +  keyValue.getKeyValueId() + 
            ", property=" + keyValue.getProperty() + ", value=" + keyValue.getValue() + " ?");
    }
    
    private void resetSelectedKeyValueAndStatusToNull() {
        setSelected( null );
        setSelectedKeyValueStatus( null );
    }
    
    public void saveOrUpdateKeyValue( KeyValue keyValue ) {
        
        if ( keyValue == null ) {
            return;
        }

        if ( resourceBundle == null ) {
            throw new IllegalStateException("ResourceBundle is null when saving KeyValue...");
        }
          
        KeyValueService keyValueService = ServiceLocator.getKeyValueService();
        
        if ( keyValue.getKeyValueId() != null ) {  // update
            keyValueService.merge( keyValue );
        } else {  // save
            keyValue.setResourceBundle( resourceBundle );
            resourceBundle.getKeyValues().add( keyValue );
            keyValueService.persist( keyValue );
            if ( keyValue.getKeyValueId() != null ) {
                resourceBundle.getKeyValues().add( keyValue );
            }
        }
    }

    public void deleteKeyValue( KeyValue keyValue ) {
        
        if ( keyValue == null ) {
            return;
        }
        
        ResourceBundle resourceBundle = keyValue.getResourceBundle();
        if ( keyValue.getKeyValueId() != null ) {  // delete from DB

            // KeyValueService.deleteKeyValue(..) not working even though unit test works so use deleteKeyValueById() below 
            ServiceLocator.getKeyValueService().deleteKeyValueById( keyValue.getKeyValueId() );
            resourceBundle.getKeyValues().remove(keyValue);
        }
    }
    
    @Command
    public void changeEditableStatus(@BindingParam("keyValueStatus") KeyValueStatus keyValueStatus) {
        setSelectedKeyValueStatus( keyValueStatus );
        setSelected( keyValueStatus.getKeyValue() );
        keyValueStatus.setEditingStatus( !keyValueStatus.getEditingStatus() );
        refreshRowTemplate( keyValueStatus );
    }
    
    public void refreshRowTemplate(KeyValueStatus keyValueStatus) {
        /*
         * This code is special and notifies ZK that the bean's value
         * has changed as it is used in the template mechanism.
         * This stops the entire Grid's data from being refreshed
         */
        BindUtils.postNotifyChange(null, null, keyValueStatus, "editingStatus");
    }
    
    @Command
    @NotifyChange({"keyValueStatusList"})
    public void newKeyValue() {
     
        KeyValue newKeyValue = new KeyValue();
        keyValueStatusList.add( new KeyValueStatus(newKeyValue, false));
    }
    
    // getter for service class

    // getter & setter for instance variables
    
    public KeyValueStatus getSelectedKeyValueStatus() {
        return selectedKeyValueStatus;
    }

    public void setSelectedKeyValueStatus(KeyValueStatus selectedKeyValueStatus) {
        this.selectedKeyValueStatus = selectedKeyValueStatus;
    }
    
    // dialog messages
    
    // global commands
    
    @GlobalCommand @NotifyChange({"keyValueStatusList"})
    public void initResourceBundleInKeyValueVM(@BindingParam("resourceBundle") ResourceBundle resourceBundle) {
        loadKeyValueStatusList( resourceBundle );
    }
}
