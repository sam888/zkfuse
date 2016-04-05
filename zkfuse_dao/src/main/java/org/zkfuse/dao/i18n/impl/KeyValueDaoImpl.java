package org.zkfuse.dao.i18n.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkfuse.dao.i18n.KeyValueDao;
import org.zkfuse.dao.i18n.ResourceBundleDao;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.ResourceBundle;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author Samuel Huang
 */
@Service
public class KeyValueDaoImpl extends BasicDao<KeyValue, Long> implements KeyValueDao {

    @Autowired
    ResourceBundleDao resourceBundleDao;
    
    /** Constructor that sets the entity to Module.class */
    public KeyValueDaoImpl() {
        super(KeyValue.class);
    }
    
    public List<KeyValue> getKeyValuesByResourceBundle(ResourceBundle resourceBundle) {
        ResourceBundle retrievedResourceBundle = resourceBundleDao.getResourceBundle( resourceBundle );
        return retrievedResourceBundle.getKeyValues();
    }

    public KeyValue getKeyValue(ResourceBundle resourceBundle, String key) {
        
        if (resourceBundle == null || key == null || key.equals("")
                || resourceBundle.getResourceBundleId() <= 0) {
            return KeyValue.EMPTY_KEY_VALUE;
        }
        
        Object obj = null;
        Object[] paramsArray = new Object[] { resourceBundle.getResourceBundleId(), key};
        
        try {
            obj = findNamedQuerySingle( KeyValue.NAMED_QUERY_GET_KEY_VALUE, paramsArray );
        } catch (NoResultException ex) {
            return KeyValue.EMPTY_KEY_VALUE;
        }
        
        return ( obj != null ? (KeyValue)obj : KeyValue.EMPTY_KEY_VALUE);
    }

    public void deleteKeyValue(ResourceBundle resourceBundle, String key) throws EntityNotFoundException {
        
//        validateResourceBundleIsNotNull( resourceBundle );

        KeyValue keyValue = getKeyValue(resourceBundle, key);
        
        // have to call this to manage the one side of the many-to-one relationship or else remove(..) below
        // will simply do nothing without exception
        resourceBundle.getKeyValues().remove(keyValue); 
        
        remove( KeyValue.class, keyValue.getKeyValueId() );
    }

    public void deleteKeyValue(ResourceBundle resourceBundle, KeyValue keyValue) throws EntityNotFoundException {
        
//        validateResourceBundleIsNotNull( resourceBundle );
        
        Long keyValueId = keyValue.getKeyValueId();
        if ( keyValueId == null || keyValueId.equals( KeyValue.EMPTY_KEY_VALUE.getKeyValueId() )) {
            throw new EntityNotFoundException("Can not delete a KeyValue record that doesn't exist in database");
        }

        // have to call this to manage the one side of the many-to-one relationship or else remove(..) below
        // will simply do nothing without exception
        resourceBundle.getKeyValues().remove(keyValue); 

        remove( KeyValue.class, keyValue.getKeyValueId() );
    }
    
    private void validateResourceBundleIsNotNull(ResourceBundle resourceBundle ) {
        if ( resourceBundle == null ) {
            throw new IllegalStateException("resourceBundle can not be null!");
        }
    }

    public int deleteKeyValueById(Long keyValueId) {
        Object[] paramsArray = new Object[] { keyValueId };
        return executeSingleNamedQuery( KeyValue.NAMED_QUERY_DELETE_KEY_VALUE, paramsArray );
    }

}
