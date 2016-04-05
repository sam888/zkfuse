package org.zkfuse.dao.i18n.impl;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@ContextConfiguration(locations = {"classpath:spring/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceBundleDaoImplTest {

    /** 
     * Assumptions: 
     *  - Both localeDao and moduleDao are working and will be used to load test data in loadTestData()
     */

    /**
     * Test data:
     * - existentResourceBundle can be created by the following SQL for MySQL
     * 
     *      INSERT INTO ResourceBundle (ResourceBundleName, LocaleId, ModuleId, Description ) VALUES ( "TestModule_en_US", 4, 1, "A testing resource bundle" );
     *    
     *    where localeId = (select LocaleId from Locale where LanguageCode = "en" AND CountryCode = "US") and 
     *          moduleId = (select moduleId from Module where Name = "TestModule")
     */

    @Autowired
    private ResourceBundleDao resourceBundleDao;

    @Autowired
    private KeyValueDao keyValueDao;

    private static LocaleDao localeDao;
    private static ModuleDao moduleDao;

    private ResourceBundle existingResourceBundle;
    private ResourceBundle nonExistingResourceBundle;
    
    private static Locale existingLocale;
    private static Locale nonExistingLocale;
    
    private static Module existingModule;
    private static Module existingModuleWithoutChildren;
    
    private static Module nonExistingModule;
    
    private static String existingResourceBundleName;
    
    @BeforeClass
    public static void loadTestData() throws Exception {
        
        /** since localeDao and moduleDao will be null here if we use @Autowired to get them **/
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        localeDao = context.getBean(LocaleDao.class);
        moduleDao = context.getBean(ModuleDao.class);
        
        existingLocale = new Locale("English", "en", "US");
        existingLocale = localeDao.getLocale( existingLocale.getLanguageCode(), existingLocale.getCountryCode(), "" );
        if (existingLocale == Locale.EMPTY_LOCALE) {
            throw new IllegalStateException("Failed to load test data for Locale");
        }
        
        existingModule = new Module( "TestModule" );
        existingModule = moduleDao.getModuleByName( existingModule.getName() );
        if (existingModule == Module.EMPTY_MODULE) {
            throw new IllegalStateException("Failed to load test data for Module with name " + existingModule.getName());
        }
        
        existingModuleWithoutChildren = new Module("Payment");
        existingModuleWithoutChildren = moduleDao.getModuleByName( existingModuleWithoutChildren.getName() );
        if (existingModuleWithoutChildren == Module.EMPTY_MODULE) {
            throw new IllegalStateException("Failed to load test data for Module with name " + existingModuleWithoutChildren.getName());
        }
        
        nonExistingLocale = new Locale("zk", "ZK", "");
        nonExistingModule = new Module("ZkModule");
        
        existingResourceBundleName = "TestModule_en_US";
    }
    
    
    @Before
    public void setUp() {
        
        existingResourceBundle = new ResourceBundle( existingLocale, existingModule, "TestModule_en_US" ); // assumption: this record exists in DB already
        nonExistingResourceBundle = new ResourceBundle( existingLocale, existingModuleWithoutChildren, "zkModule");
    }
    
    @Test
    public void testCountSuccess() throws Exception {
        assertTrue( resourceBundleDao.count() > 0);
    }
    
    @Test
    public void testFindAllResourceBundleSuccess() throws Exception {
        
        List<ResourceBundle> resourceBundleList = resourceBundleDao.findAll(ResourceBundle.class);
        System.out.println("resourceBundleList size: " + resourceBundleList.size());
        for (int i = 0; i < resourceBundleList.size(); i++) {
            System.out.println( resourceBundleList.get(i));
        }
        assertNotNull( resourceBundleList );
        assertTrue( resourceBundleList.size() > 0 );
    }
    
    
    //      Test resourceBundleDao.getResource(..) starts
    
    /** 
     * Tests to see resourceBundleDao.getResourceBundle(..) can return a non-empty ResourceBundle successfully if 
     * searched record exists in database 
     */
    @Test
    public void testGetResourceBundle_forExistentDataSuccess() throws Exception  {
        
        ResourceBundle resourceBundle = resourceBundleDao.getResourceBundle( existingLocale, existingModule, existingResourceBundleName );
        assertNotNull( resourceBundle );
        assertFalse( resourceBundle.equals(ResourceBundle.EMPTY_RESOURCE_BUNDLE) ); // not empty ResourceBundle
    }
    

    /** 
     * Tests to see resourceBundleDao.getResourceBundle(..) will return an empty ResourceBundle (i.e. 
     * ResourceBundle.EMPTY_RESOURCE_BUNDLE) if searched record doesn't exist in database 
     */
    @Test
    public void testGetResourceBundle_returnEmptyResourceBundleForNonExistentDataSuccess() throws Exception  {
        
        ResourceBundle resourceBundle = resourceBundleDao.getResourceBundle( nonExistingLocale, nonExistingModule, existingResourceBundleName  );
        assertTrue( resourceBundle.equals( ResourceBundle.EMPTY_RESOURCE_BUNDLE ) );
    }

    //      Test resourceBundleDao.persist() starts
    
    @Test
    public void testPersist_insertNonExistentResourceBundleSuccess() throws Exception {
        
        long count = resourceBundleDao.count();
        assertNull( nonExistingResourceBundle.getResourceBundleId() ); // before persist
        
        resourceBundleDao.persist( nonExistingResourceBundle );
        
        assertTrue( (resourceBundleDao.count() - count) == 1 ); // after persist
        assertNotNull( nonExistingResourceBundle.getResourceBundleId() ); // confirm PK populated
    }
    
    /** Verify PersistenceException is thrown when persisting ResourceBundle that breaks unique constraint defined in ResourceBundle POJO */
    @Test(expected=PersistenceException.class)
    public void testPersist_throwExceptionByBreakingUniqueConstraint() throws Exception {
        resourceBundleDao.persist( existingResourceBundle );
    }

    /** Verify PersistenceException is thrown when persisting ResourceBundle with non-existing locale */
    @Test(expected=IllegalStateException.class)
    public void testPersist_throwExceptionWithNonExistingLocale() throws Exception {
        
        existingResourceBundle.setLocale(nonExistingLocale);
        resourceBundleDao.persist(existingResourceBundle);
    }

    /** Verify PersistenceException is thrown when persisting ResourceBundle with non-existing module */
    @Test(expected=IllegalStateException.class)
    public void testPersist_throwExceptionWithNonExistingModule() throws Exception {
        
        existingResourceBundle.setModule(nonExistingModule);
        resourceBundleDao.persist(existingResourceBundle);
    }
    
    
    //      Test resourceBundleDao.merge(..) starts
    
    /** Tests to see merge(..) updates existing ResourceBundle record successfully */
    @Test
    public void testMerge_updateExistingResourceBundleSuccess() throws Exception {
        
        String data = "Testing...";
        ResourceBundle resourceBundle = resourceBundleDao.getResourceBundle( existingResourceBundle.getLocale(), 
                existingResourceBundle.getModule(), existingResourceBundleName );
        
        assertFalse( ResourceBundle.EMPTY_RESOURCE_BUNDLE.equals(resourceBundle) ); // yes, existingResourceBundle exists in DB
        assertFalse( data.equals( resourceBundle.getDescription() ) ); // before merge

        resourceBundle.setDescription( data );
        resourceBundleDao.merge( resourceBundle ); // merge

        Long resourceBundleId = resourceBundle.getResourceBundleId();
        resourceBundle = null; // in case resourceBundle and updatedResourceBundle refer to the same instance below 
        
        ResourceBundle updatedResourceBundle = resourceBundleDao.get( resourceBundleId ); // get updated ResourceBundle from DB
        assertNotNull( updatedResourceBundle );
        assertTrue( data.equals( updatedResourceBundle.getDescription() ) ); // after merge
    }
    
    
    /** Tests to see merge(..) inserts new record for non-existent ResourceBundle */
    @Test
    public void testMerge_insertNonExistingResourceBundleSuccess() throws Exception {
        
        ResourceBundle resourceBundle = resourceBundleDao.getResourceBundle( nonExistingResourceBundle );
        assertTrue( ResourceBundle.EMPTY_RESOURCE_BUNDLE.equals( resourceBundle ) ); // confirm non-existent resourceBundle
        
        resourceBundleDao.merge( nonExistingResourceBundle );
        resourceBundleDao.flush();
        
        System.out.println("resourceBundle before merge: " + nonExistingResourceBundle);
        ResourceBundle insertedResourceBundle = resourceBundleDao.getResourceBundle( nonExistingResourceBundle );
        System.out.println( "resourceBundle after merge: " + insertedResourceBundle + "\n");
        
        assertFalse( ResourceBundle.EMPTY_RESOURCE_BUNDLE == insertedResourceBundle ); // after merge
        assertNotNull( insertedResourceBundle.getResourceBundleId() ); // confirm PK populated
    }
    
    
    //  Test resourceBundleDao.deleteResourceBundle(..) starts
    
    /** Tests to see deleteResourceBundle(..) can delete an existing resourceBundle successfully **/
    @Test
    public void testDeleteResourceBundleForExistingRecordSuccess() throws Exception {
        
        resourceBundleDao.deleteResourceBundle( existingResourceBundle.getLocale(), existingResourceBundle.getModule(), 
                existingResourceBundle.getResourceBundleName() );
        
        // getResourceBundle() should return empty ResourceBundle after successful delete
        assertTrue( ResourceBundle.EMPTY_RESOURCE_BUNDLE.equals( resourceBundleDao.getResourceBundle(existingResourceBundle) ) );
    }
    

    /** Tests to see deleteResourceBundle(..) throws the expected Exception when input non-existent resourceBundle **/
    @Test(expected=EntityNotFoundException.class)
    public void testDeleteResourceBundle_throwExceptionForNonExistentRecord() throws Exception {
        resourceBundleDao.deleteResourceBundle( nonExistingResourceBundle );
    }

    
    /** Tests to see deleteResourceBundle(..) can delete an existing resourceBundle and its' children objects (KeyValue)
     *  successfully **/
    @Test
    public void testDeleteResourceBundle_cascadeDeleteToKeyValueSuccess() throws Exception {
        
        /** record exists in DB **/
        ResourceBundle resourceBundle = resourceBundleDao.getResourceBundle( existingResourceBundle );
        assertTrue( resourceBundle != ResourceBundle.EMPTY_RESOURCE_BUNDLE && resourceBundle.getResourceBundleId() > 0 );
        
        /** record has children object KeyValue **/
        assertTrue( resourceBundle.getKeyValues().size() > 0 );
        
        /* keyValue exists in DB */
        Long keyValueId = resourceBundle.getKeyValues().get(0).getKeyValueId();
        assertTrue( keyValueDao.get( keyValueId ) != KeyValue.EMPTY_KEY_VALUE );
        
        /* delete existingResourceBundle */
        resourceBundleDao.deleteResourceBundle( existingResourceBundle.getLocale(), existingResourceBundle.getModule(), existingResourceBundleName );
        resourceBundleDao.flush();
        
        /* keyValue doesn't exit in DB now */
        try {
            KeyValue deletedKeyValue = keyValueDao.get( keyValueId );
            fail( "Should not get here!" );
        } catch (Exception e) {
            assertTrue( e instanceof EntityNotFoundException );
        }
        
    }
    
    @Test
    public void testGetResourceBundleByWildCardName_success() throws Exception {
        List<ResourceBundle> resourceBundleList = resourceBundleDao.getResourceBundleByWildCardName( "Module" );
        printResourceBundleList( resourceBundleList );
        assertNotNull( resourceBundleList );
        assertTrue( resourceBundleList.size() > 0 );
    }

    public void printResourceBundleList(List<ResourceBundle> resourceBundleList) {
        System.out.println("list size: " + resourceBundleList.size());
        for (int i = 0; i < resourceBundleList.size(); i++) {
            System.out.println( resourceBundleList.get(i));
        }
    }

}
