package org.zkfuse.service.i18n.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.KeyValueDao;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.i18n.KeyValueService;
import org.zkfuse.service.impl.GenericService;

/**
 *
 * @author Samuel Huang
 */
@Service
public class KeyValueServiceImpl extends GenericService<KeyValue, Long> implements KeyValueService {

    @Autowired
    private KeyValueDao keyValueDao;
    
    public KeyValueServiceImpl() {
        super(KeyValue.class);
    }
    
    public List<KeyValue> getKeyValuesByResourceBundle(ResourceBundle resourceBundle) {
        return keyValueDao.getKeyValuesByResourceBundle( resourceBundle );
    }

    public KeyValue getKeyValue(ResourceBundle resourceBundle, String key) {
        return keyValueDao.getKeyValue(resourceBundle, key);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteKeyValue(ResourceBundle resourceBundle, String key) {
        keyValueDao.deleteKeyValue(resourceBundle, key);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED )
    public void deleteKeyValue(ResourceBundle resourceBundle, KeyValue keyValue) {
        keyValueDao.deleteKeyValue(resourceBundle, keyValue);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED )
    public int deleteKeyValueById(Long keyValueId) {
        return keyValueDao.deleteKeyValueById( keyValueId );
    }

}
