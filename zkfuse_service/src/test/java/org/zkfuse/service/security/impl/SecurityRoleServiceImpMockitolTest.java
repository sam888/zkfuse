package org.zkfuse.service.security.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkfuse.dao.security.SecurityRoleDao;
import org.zkfuse.model.security.SecurityRole;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Samuel Huang
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityRoleServiceImpMockitolTest {
    
    @Mock 
    private SecurityRoleDao securityRoleDao;

    @InjectMocks
    private SecurityRoleServiceImpl securityRoleService;
    
    private SecurityRole existingSecurityRole = null;
    private String superUserRoleName = "SUPER_USER";

    @Before
    public void setUp() {
        existingSecurityRole = new SecurityRole();
        existingSecurityRole.setRoleName( superUserRoleName );
    }

    /** Verify getRoleByRoleName(..) delegates call to securityRoleDao.getRoleByRoleName(..) */
    @Test
    public void test_getRoleByRoleName_success() throws Exception {

        when( securityRoleDao.getRoleByRoleName( superUserRoleName ) ).thenReturn( existingSecurityRole );

        SecurityRole retrievedSecurityRole = securityRoleService.getRoleByRoleName( superUserRoleName );
        assertTrue( retrievedSecurityRole == existingSecurityRole );

        verify( securityRoleDao, times(1)).getRoleByRoleName( superUserRoleName );
    }

    /** Verify deleteRoleByRoleName(..) delegates call to securityRoleDao.deleteRoleByRoleName(..) */
    @Test
    public void test_deleteRoleByRoleName_success() throws Exception {

        securityRoleService.deleteRoleByRoleName( superUserRoleName );
        verify( securityRoleDao, times(1)).deleteRoleByRoleName( superUserRoleName );
    }
}
