package org.zkfuse.dao.security.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.security.SecurityRoleUserDao;
import org.zkfuse.dao.security.SecurityUserDao;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityRoleUserPK;
import org.zkfuse.model.security.SecurityUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
@ContextConfiguration(locations = {"classpath:spring/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false )
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityRoleUserDaoImplTest {

    /** Used to persist SecurityUser. Note SecurityUserDao not used since securityRoleDao is our focus */
    @PersistenceContext(unitName="zkfuse")
    private EntityManager entityManager;

    enum TestDataKey {
        SECURITY_USER, SECURITY_ROLE, SECURITY_ROLE_USER, SECURITY_PERMISSION, SECURITY_PERMISSION_ROLE
    }

    private Map<Object, Object> testDataMap;

    @Autowired
    private SecurityRoleUserDao securityRolUsereDao;

    @Autowired
    private SecurityUserDao securityUsereDao;

    @Before
    public void init() {
        testDataMap = new HashMap<Object, Object>();
    }

    @After
    public void cleanup() {
        testDataMap = null;
    }

    @Test
    public void test() {

        SecurityUser securityUser = securityUsereDao.getUserByLoginName( "AAA" );
        List<SecurityRoleUser> securityRoleUserList = new ArrayList<SecurityRoleUser>( securityUser.getSecurityRoleUsers() );
        SecurityRoleUser test =  securityRolUsereDao.find( SecurityRoleUser.class, securityRoleUserList.get(0).getId() );

        System.out.println("test before delete: " + test );

       // securityRolUsereDao.clear();
       // securityRolUsereDao.refresh( securityRoleUserList.get(0).getId() );

        //securityRolUsereDao.remove( SecurityRoleUser.class, securityRoleUserList.get(0).getId() );

        SecurityRoleUserPK pk = securityRoleUserList.get(0).getId();
        securityRolUsereDao.delete( pk.getSecurityUser().getUserId(), pk.getSecurityRole().getRoleId() );
        //test =  securityRolUsereDao.find( SecurityRoleUser.class, securityRoleUserList.get(0).getId() );

        System.out.println("test after delete: " + test );


    }
}
