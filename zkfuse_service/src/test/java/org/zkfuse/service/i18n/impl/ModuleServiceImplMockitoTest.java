package org.zkfuse.service.i18n.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkfuse.dao.i18n.ModuleDao;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.model.i18n.Module;

/**
 * @author Samuel Huang
 */
@RunWith(MockitoJUnitRunner.class)
public class ModuleServiceImplMockitoTest {
    
    @Mock 
    private ModuleDao moduleDao;

    @Mock
    private BasicDao<Module, Long> basicDao;

    @InjectMocks
    private ModuleServiceImpl moduleService;
    
    private Module existentModule = null;
    
    @Before
    public void setUp() {
        existentModule = new Module("TestModule");   // assumption: this record exists in DB already
    }

    /** Verify moduleService.count() delegates call to basicDao.count(Module.class) **/
    @Test
    public void testCountSuccess() throws Exception {
        Long testCount = new Long(777);
        when(basicDao.count(Module.class)).thenReturn( testCount );
        assertTrue( moduleService.count().equals( testCount ) ); 
        verify(basicDao, times(1)).count( Module.class );
    }
    
    /** Verify moduleService.getModuleByName(moduleName) delegates call to moduleDao.getModuleByName(moduleName) **/
    @Test
    public void testGetModuleByNameSuccess() throws Exception {
        when( moduleDao.getModuleByName( existentModule.getName() ) ).thenReturn( existentModule );
        assertTrue( moduleService.getModuleByName( existentModule.getName() ).equals( existentModule )); 
        verify(moduleDao, times(1)).getModuleByName( existentModule.getName() );
    }


    /** Verify moduleService.deleteModule(moduleName) delegates call to moduleDao.deleteLocale(moduleName) **/
    @Test
    public void testDeleteModuleSuccess() throws Exception {
        String message = "success";
        doThrow( new RuntimeException( message ) ).when(moduleDao).deleteModule( existentModule.getName() );
        try {
            moduleService.deleteModule( existentModule.getName() );
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().equals( message ));
        } 
    }
    
}
