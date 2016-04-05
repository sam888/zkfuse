package org.zkfuse.dao.i18n.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.KeyValueDao;
import org.zkfuse.dao.i18n.LocaleDao;
import org.zkfuse.dao.i18n.ModuleDao;
import org.zkfuse.dao.i18n.ResourceBundleDao;
import org.zkfuse.model.i18n.KeyValue;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"classpath:spring/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class KeyValueDaoImplTest {
    
    @Autowired
    private KeyValueDao keyValueDao;
    
    @Autowired
    private LocaleDao localeDao;

    @Autowired
    private ModuleDao moduleDao;
    
    @Autowired
    private ResourceBundleDao resourceBundleDao;
    
    // assumption: this record exists in DB already
    private Locale existingLocale;
    private Module existingModule;
    private ResourceBundle existingResourceBundle; 
    
    // the name of ResourceBundle that has no KeyValue
    String noKeyValueResourceBundleName = "TestModule_ZK";
    
    // key of existing KeyValue
    String keyOfExistingKeyValue = "hello";

    // value of existing KeyValue
    String valueOfExistingKeyValue = "Hello";
    
    @Before
    public void setUp() throws Exception {
        
        if ( existingResourceBundle != null && existingResourceBundle.getResourceBundleId() > 0 ) { 
            return;
        }
        
        existingLocale = new Locale("English","en", "US");
        existingLocale = localeDao.getLocale( existingLocale.getLanguageCode(), existingLocale.getCountryCode(), "" );
        if (existingLocale == Locale.EMPTY_LOCALE) {
            throw new IllegalStateException("Failed to load test data for Locale");
        }
        
        existingModule = new Module( "TestModule" );
        existingModule = moduleDao.getModuleByName( existingModule.getName() );
        if (existingModule == Module.EMPTY_MODULE) {
            throw new IllegalStateException("Failed to load test data for Locale");
        }
        
        existingResourceBundle = new ResourceBundle( existingLocale, existingModule, "TestModule_en_US" );
        existingResourceBundle = resourceBundleDao.getResourceBundle( existingResourceBundle );
        
        if (existingResourceBundle.equals(ResourceBundle.EMPTY_RESOURCE_BUNDLE)) {
            throw new IllegalStateException("Failed to load test data for ResourceBundle");
        }
        
        /**
         * existingResourceBundle should not be changed from this point onward as it will be used
         * by all tests
         */
    }
    
    @Test
    public void testGetKeyValuesByResourceBundleSuccess() throws Exception {
        List<KeyValue> keyValueList = keyValueDao.getKeyValuesByResourceBundle( existingResourceBundle );
        assertTrue( keyValueList.size() > 0 );
    }

    @Test
    public void testGetKeyValueSuccess() throws Exception {
        
        KeyValue existingKeyValue = keyValueDao.getKeyValue(existingResourceBundle, keyOfExistingKeyValue);
        assertNotNull( existingKeyValue );
        assertTrue( existingKeyValue.getValue().equals( valueOfExistingKeyValue ) );
    }

    @Test
    public void testGetKeyValue_returnEmptyKeyValueForNonExistingKey() throws Exception {
        
        String nonExistingKey = "zk";
        KeyValue nonExistingKeyValue = keyValueDao.getKeyValue(existingResourceBundle, nonExistingKey);
        assertNotNull( nonExistingKeyValue );
        assertTrue( nonExistingKeyValue == KeyValue.EMPTY_KEY_VALUE );
    }

    @Test
    public void testDeleteKeyValueSuccess() throws Exception {
        keyValueDao.deleteKeyValue(existingResourceBundle, keyOfExistingKeyValue);
        KeyValue existingKeyValue = keyValueDao.getKeyValue(existingResourceBundle, keyOfExistingKeyValue);
        System.out.println("existingKeyValue Id: " + existingKeyValue.getKeyValueId());
        assertNotNull( existingKeyValue );
        assertTrue( existingKeyValue == KeyValue.EMPTY_KEY_VALUE );
    }

    @Test(expected=EntityNotFoundException.class)
    public void testDeleteKey_forNonExistingKey() throws Exception {
        
        String nonExistingKey = "zk";
        keyValueDao.deleteKeyValue(existingResourceBundle, nonExistingKey);
        
        KeyValue existingKeyValue = keyValueDao.getKeyValue(existingResourceBundle, keyOfExistingKeyValue);
    }
    
    @Test
    public void testSaveKey_forNonExistingKey() throws Exception {
        
        KeyValue newKeyValue = new KeyValue();
        newKeyValue.setResourceBundle( existingResourceBundle );
        
        String nonExistingKey = "zk";
        newKeyValue.setProperty( nonExistingKey );
        newKeyValue.setValue("ZK");

        keyValueDao.persist( newKeyValue );
        KeyValue retrievedKeyValue = keyValueDao.getKeyValue(existingResourceBundle, nonExistingKey);
        
        assertNotNull( retrievedKeyValue );
        assertTrue( retrievedKeyValue.getProperty().equals( nonExistingKey ));
    }

}
