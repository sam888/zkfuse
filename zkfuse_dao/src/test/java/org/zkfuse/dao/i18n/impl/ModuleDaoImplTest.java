package org.zkfuse.dao.i18n.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.ModuleDao;
import org.zkfuse.model.i18n.Module;

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
public class ModuleDaoImplTest {

    /*
     * MySQL sample data:
     *   INSERT INTO Module (Name,Description) VALUES("TestModule", "For testing");
     *   INSERT INTO Module (Name,Description) VALUES("Payment", "Payment module");
     */
    
    @Autowired
    private ModuleDao moduleDao;
    
    private Module existentModuleWithChildren = null;
    private Module existentModuleWithoutChildren = null;
    private Module nonExistentModule = null;
    
    
    @Before
    public void setUp() {
        existentModuleWithChildren = new Module( "TestModule" ); // assumption: this record exists in DB already
        existentModuleWithoutChildren = new Module( "Payment" ); // assumption: this record exists in DB already
        nonExistentModule = new Module( "ZkModule");
    }
    
    
    @Test
    public void testCountSuccess() throws Exception {
        assertTrue( moduleDao.count() > 0);
    }
    
    
    @Test
    public void testPersist_insertNonExistentModuleSuccess() throws Exception {
        long count = moduleDao.count();
        assertNull( nonExistentModule.getModuleId() ); // before persist
        
        moduleDao.persist( nonExistentModule );
        
        assertTrue( (moduleDao.count() - count) == 1 ); // after persist
        assertNotNull( nonExistentModule.getModuleId() ); // confirm PK populated
    }
    
    
    @Test
    public void testFindAllModuleSuccess() throws Exception {
        List<Module> moduleList = moduleDao.findAll(Module.class);
        assertNotNull( moduleList );
        assertTrue(  moduleList.size() > 0 );
//        for ( Module module: moduleList ) {
//            System.out.println( module.toString() );
//        }
    }

    
    /** Verify PersistenceException is thrown when persisting Module that breaks unique constraint defined in POJO */
    @Test(expected=PersistenceException.class)
    public void testPersist_throwExceptionByBreakingUniqueConstraint() throws Exception {
        moduleDao.persist( existentModuleWithChildren );
    }
    
    
    /** Tests to see getLocale(..) can return a non-empty Module successfully if searched record exists in database */
    @Test
    public void testGetModuleByName_forExistentDataSuccess() throws Exception {
        Module module = moduleDao.getModuleByName( existentModuleWithChildren.getName() );
        assertFalse( module.getModuleId().equals( Module.EMPTY_MODULE.getModuleId() )); // verify non-empty module returned
        assertTrue( module.getName().equals( existentModuleWithChildren.getName() ));
    }

    
    /** Tests to see getLocale(..) can return an empty Module successfully if searched record doesn't exist in database */
    @Test
    public void testGetModuleByName_returnEmptyModuleForNonExistentDataSuccess() throws Exception {
        Module module = moduleDao.getModuleByName( "nonExistentModule" );
        assertTrue( module == Module.EMPTY_MODULE );
    }
    

    /** Tests to see merge(..) updates existing Module record successfully */
    @Test
    public void testMerge_updateExistingModuleSuccess() throws Exception {
        String data = "testing...";
        Module module = moduleDao.getModuleByName( existentModuleWithChildren.getName() );
        
        assertFalse( Module.EMPTY_MODULE == module ); // yes, existentModule exists in DB
        assertFalse( data.equals( module.getDescription() ) ); // before merge
        
        module.setDescription( data );
        moduleDao.merge( module );
        module = null;
        
        Module updatedModule = moduleDao.getModuleByName( existentModuleWithChildren.getName() );
        assertTrue( data.equals( updatedModule.getDescription() ) ); // after merge
    }
    

    /** Tests to see merge(..) inserts new record for non-existent Module */
    @Test
    public void testMerge_insertNonExistentModuleSuccess() throws Exception {
        Module module = moduleDao.getModuleByName( nonExistentModule.getName() );
        assertTrue( Module.EMPTY_MODULE == module ); // confirm non-existent Module
        
        moduleDao.merge( nonExistentModule );
        moduleDao.flush();
        
        System.out.println("Module before merge: " + nonExistentModule );
        Module insertedModule = moduleDao.getModuleByName( nonExistentModule.getName() );
        System.out.println( "Module after merge: " + insertedModule );
        
        assertFalse( Module.EMPTY_MODULE == insertedModule ); // after merge
        assertNotNull( insertedModule.getModuleId() ); // confirm PK populated
    }
    
    
    /** Tests to see deleteModule(..) can delete existing Module without children successfully **/
    @Test
    public void testDeleteModule_forExistentRecordSuccess() throws Exception {
        
        assertFalse( Module.EMPTY_MODULE == moduleDao.getModuleByName(existentModuleWithoutChildren.getName()) );
        
        moduleDao.deleteModule( existentModuleWithoutChildren.getName() );
        
        assertTrue( Module.EMPTY_MODULE == moduleDao.getModuleByName(existentModuleWithoutChildren.getName()) );
    }

    
    /** Tests to see deleteModule(..) throws the expected Exception when input non-existent module **/
    @Test(expected=EntityNotFoundException.class)
    public void testDeleteModule_throwExceptionForNonExistentRecord() throws Exception {
       moduleDao.deleteModule( nonExistentModule.getName() );
    }
    
    
    /** Tests to see deleteModule(..) will throw Exception when deleting an existing module with 
     *  children row (i.e. ResourceBundle) to violate foreign key constraint. **/
    @Test(expected=ConstraintViolationException.class)
    public void testDeleteMoule_throwsExceptionForExistentRecordWithChildren() throws Exception {
        try {
            moduleDao.deleteModule( existentModuleWithChildren.getName() );
            moduleDao.flush();
            fail("Should not get here");
        } catch (PersistenceException e) {
            Throwable c = e.getCause();
            assertTrue( c instanceof ConstraintViolationException );
            throw (ConstraintViolationException)c;
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception occured");
        }
    }
    
    
}
