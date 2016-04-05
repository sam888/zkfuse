package org.zkfuse.web.vm.i18n.status;

import org.zkfuse.model.i18n.KeyValue;

/**
 * Adapted from ZK demo site
 *
 */
public class KeyValueStatus {
    
    private KeyValue keyValue;
    private boolean editingStatus;

    public KeyValueStatus(KeyValue keyValue, boolean editingStatus) {
        this.keyValue = keyValue;
        this.editingStatus = editingStatus;
    }
    
    public KeyValue getKeyValue() {
        return keyValue;
    }
    public void setKeyValue(KeyValue keyValue) {
        this.keyValue = keyValue;
    }
     
    public boolean getEditingStatus() {
        return editingStatus;
    }
    public void setEditingStatus(boolean editingStatus) {
        this.editingStatus = editingStatus;
    }

}
