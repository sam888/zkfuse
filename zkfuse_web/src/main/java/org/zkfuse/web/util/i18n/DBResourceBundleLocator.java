package org.zkfuse.web.util.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.i18n.util.ResourceBundleUtil;

/**
 * @author Samuel Huang
 */
public class DBResourceBundleLocator {
    
    private String moduleName;
    
    private Locale locale;

    /** 
     * ConcurrentHashMap used since it's designed for concurrent access and not supposed to be changed by multiple threads,
     * i.e. only admin can reload resource bundle and result in updating the map  */
    private Map<String, String> keyValueMap = new ConcurrentHashMap<String, String>();
    
    
    public DBResourceBundleLocator(Locale locale, String moduleName) {
        this.locale = locale;
        this.moduleName = moduleName;
        validateStates();
    }

    private void validateStates() {

        if ( moduleName == null || StringUtils.isBlank(moduleName) ) {
           throw new IllegalStateException("Module name is null or empty string!"); 
        }

        if (locale == null) {
            throw new IllegalStateException("Locale is null!"); 
        }
    }
    
    public void populateKeyValueMap() {
        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(locale, moduleName);
        if (resourceBundle == ResourceBundle.EMPTY_RESOURCE_BUNDLE) {
           return;
        }
        
        List<KeyValue> keyValueList = resourceBundle.getKeyValues();
        for (KeyValue keyValue: keyValueList) {
            keyValueMap.put( keyValue.getProperty(), keyValue.getValue() );
        }
    }
    
    public String getLabel( String key ) {
        return keyValueMap.get( key );
    }

    public void reloadKeyValues() {
        keyValueMap.clear();
        populateKeyValueMap();
    }
    
    // Getter &  setters for instance variables
    
}
