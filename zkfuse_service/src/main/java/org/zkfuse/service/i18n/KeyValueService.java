package org.zkfuse.service.i18n;

import java.util.List;

import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.IGenericService;

/**
 * @author Samuel Huang
 */
public interface KeyValueService  extends IGenericService<KeyValue, Long> {

    List<KeyValue> getKeyValuesByResourceBundle(ResourceBundle resourceBundle);

    KeyValue getKeyValue(ResourceBundle resourceBundle, String key);

    void deleteKeyValue(ResourceBundle resourceBundle, String key);

    void deleteKeyValue(ResourceBundle resourceBundle, KeyValue keyValue);
    
    int deleteKeyValueById(Long keyValudId);
}
