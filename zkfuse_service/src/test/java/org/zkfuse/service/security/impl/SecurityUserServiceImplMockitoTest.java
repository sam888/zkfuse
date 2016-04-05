package org.zkfuse.service.security.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkfuse.dao.security.SecurityUserDao;
import org.zkfuse.model.security.SecurityUser;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Samuel Huang
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityUserServiceImplMockitoTest {
    
    @Mock 
    private SecurityUserDao securityUserDao;

    @InjectMocks
    private SecurityUserServiceImpl securityUserService;

    private SecurityUser existingSecurityUser = null;
    private String loginName = "blabla";

    @Before
    public void setUp() {
        existingSecurityUser = new SecurityUser();
        existingSecurityUser.setUserLoginName( loginName );
    }

    /** Verify getUserByLoginName(..) delegates call to SecurityUserDao.getUserByLoginName(..) */
    @Test
    public void test_getUserByLoginName_success() throws Exception {

        when( securityUserDao.getUserByLoginName( loginName ) ).thenReturn( existingSecurityUser );

        SecurityUser retrievedSecurityUser = securityUserService.getUserByLoginName( loginName );
        assertTrue( retrievedSecurityUser == existingSecurityUser );

        verify( securityUserDao, times(1)).getUserByLoginName( loginName );
    }

    /** Verify deleteUserByLoginName(..) delegates call to SecurityUserDao.deleteUserByLoginName(..) */
    @Test
    public void test_deleteUserByLoginName_success() throws Exception {

        securityUserService.deleteUserByLoginName( loginName );
        verify( securityUserDao, times(1)).deleteUserByLoginName( loginName );
    }

}
