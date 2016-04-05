package org.zkfuse.dao.i18n;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.ResourceBundle;

import java.util.List;

/**
 *
 * @author Samuel Huang
 */
public interface KeyValueDao  extends IBasicDao<KeyValue, Long> {

    List<KeyValue> getKeyValuesByResourceBundle(ResourceBundle resourceBundle);

    KeyValue getKeyValue(ResourceBundle resourceBundle, String key);

    void deleteKeyValue(ResourceBundle resourceBundle, String key);

    void deleteKeyValue(ResourceBundle resourceBundle, KeyValue keyValue);

    int deleteKeyValueById(Long keyValueId);
    

}
