package org.zkfuse.service.security.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkfuse.dao.security.SecurityPermissionDao;
import org.zkfuse.model.security.SecurityPermission;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Samuel Huang
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityPermissionServiceImplMockitoTest {
    
    @Mock 
    private SecurityPermissionDao securityPermissionDao;

    @InjectMocks
    private SecurityPermissionServiceImpl securityPermissionService;

    private SecurityPermission existingSecurityPermission = null;
    private String permissionName = "SELF_DESTRUCT";

    @Before
    public void setUp() {
        existingSecurityPermission = new SecurityPermission();
        existingSecurityPermission.setPermissionName(permissionName);
    }

    /** Verify getSecurityPermissionByPermissionName(..) delegates call to securityPermissionDao.getSecurityPermissionByPermissionName(..) */
    @Test
    public void test_getSecurityPermissionByPermissionName_success() throws Exception {

        when( securityPermissionDao.getSecurityPermissionByPermissionName( permissionName ) ).thenReturn( existingSecurityPermission );

        SecurityPermission retrievedSecurityPermission = securityPermissionService.getSecurityPermissionByPermissionName( permissionName );
        assertTrue( retrievedSecurityPermission == existingSecurityPermission );

        verify( securityPermissionDao, times(1)).getSecurityPermissionByPermissionName( permissionName );
    }

    /** Verify deleteSecurityPermissionByPermissionName(..) delegates call to securityPermissionDao.deleteSecurityPermissionByPermissionName(..) */
    @Test
    public void test_deleteSecurityPermissionByPermissionName_success() throws Exception {

        securityPermissionService.deleteSecurityPermissionByPermissionName( permissionName );
        verify( securityPermissionDao, times(1)).deleteSecurityPermissionByPermissionName( permissionName );
    }

}
