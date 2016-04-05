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
import org.zkfuse.dao.security.SecurityUserDao;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
@ContextConfiguration(locations = {"classpath:spring/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true )
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityUserDaoImplTest {

    /** Used to persist SecurityRole. Note SecurityRoleDao not used since securityUserDao is our focus */
    @PersistenceContext(unitName="zkfuse")
    private EntityManager entityManager;

    enum TestDataKey {
        SECURITY_USER, SECURITY_ROLE, SECURITY_ROLE_USER
    }

    private Map<Object, Object> testDataMap;

    @Autowired
    private SecurityUserDao securityUserDao;

    private SecurityUser testSecurityUser;

    @Before
    public void init() {
        testDataMap = new HashMap<Object, Object>();
    }

    @After
    public void cleanup() {
        testDataMap = null;
    }

    /**
     * Verify persisting a SecurityUser with a SecurityRoleUser child will persist both records(i.e. cascade persist)  to
     * DB and persisted SecurityRoleUser record will establish relationships to both SecurityUser & SecurityRole records in DB
     *
     * @throws Exception
     */
    @Test
    public void test_create_securityUser_with_child_securityRoleUser_cause_cascade_persist() throws Exception {

        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewUserWithRoleUserAndRoleRecords( roleName, userLoginName, userPassword, createdBy );
    }

    /**
     * Verify delete a SecurityUser record will cause cascade delete to an attached child SecurityRoleUser record in DB
     *
     * @throws Exception
     */
    @Test
    public void test_delete_securityUser_with_child_securityRoleUser_cause_cascade_delete() throws Exception {

        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewUserWithRoleUserAndRoleRecords( roleName, userLoginName, userPassword, createdBy );

        SecurityRoleUser persitedSecurityRoleUser = (SecurityRoleUser)testDataMap.get(TestDataKey.SECURITY_ROLE_USER );
        SecurityRoleUser retrievedSecurityRoleUser = entityManager.find(SecurityRoleUser.class, persitedSecurityRoleUser.getId());

        assertNotNull( retrievedSecurityRoleUser ); // child record still exists

        SecurityUser securityUser = (SecurityUser)testDataMap.get( TestDataKey.SECURITY_USER );
        securityUserDao.remove( SecurityUser.class, securityUser.getUserId() );
        try {
            SecurityUser securityUserAfterDelete = securityUserDao.get( securityUser.getUserId() );
            fail( "securityUser not deleted, what the..." );
        } catch (Exception ex) {
            assertTrue( ex instanceof EntityNotFoundException );  // yup, parent record deleted
        }

        retrievedSecurityRoleUser = entityManager.find(SecurityRoleUser.class, persitedSecurityRoleUser.getId());
        assertTrue( retrievedSecurityRoleUser == null ); // yup, child record deleted
    }

    @Test
    public void test_getUserByLoginName_success() throws Exception {

        // insert test data to DB
        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewUserWithRoleUserAndRoleRecords( roleName, userLoginName, userPassword, createdBy ); // verified to be working by tests  above

        SecurityUser securityUser = securityUserDao.getUserByLoginName( userLoginName );

        assertNotNull( securityUser );
        assertTrue(securityUser != SecurityUser.EMPTY_SECURITY_USER);
        assertTrue( securityUser.getUserLoginName().equals( userLoginName ) );
        System.out.println("sec user: " + securityUser);
    }

    @Test
    public void test_merge_success() throws Exception {

        // insert test data to DB
//        String roleName = "SUPER_USER";
        String userLoginName = "Tester123";
//        String userPassword = "12345AAA";
//        String createdBy = "Sam";
//        addNewUserWithRoleUserAndRoleRecords( roleName, userLoginName, userPassword, createdBy ); // verified to be working by tests  above

        SecurityUser securityUser = securityUserDao.getUserByLoginName( userLoginName );

        assertNotNull( securityUser );
        assertTrue(securityUser != SecurityUser.EMPTY_SECURITY_USER);
        assertTrue( securityUser.getUserLoginName().equals( userLoginName ) );

        securityUser.setUserPassword("ABC");
        securityUserDao.merge( securityUser );
        System.out.println("sec user: " + securityUser);
    }

    @Test
    public void test_deleteUserByLoginName_success() throws Exception {

        // insert test data to DB
        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewUserWithRoleUserAndRoleRecords( roleName, userLoginName, userPassword, createdBy ); // verified to be working by tests  above

        // verify securityUser not deleted yet
        SecurityUser securityUser = securityUserDao.getUserByLoginName( userLoginName );
        assertTrue( securityUser.getUserLoginName().equals( userLoginName ) );

        securityUserDao.deleteUserByLoginName( userLoginName );

        securityUser = securityUserDao.getUserByLoginName( userLoginName );
        assertTrue( securityUser == SecurityUser.EMPTY_SECURITY_USER ); // yup, securityUser deleted
    }

    @Test( expected = PersistenceException.class )
    public void test_save_securityUser_with_nonUnique_userLoginName_throws_persistenceException() throws Exception {

        // insert test data to DB
        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewUserWithRoleUserAndRoleRecords( roleName, userLoginName, userPassword, createdBy ); // verified to be working by tests  above

        // save another user with the same login name
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUserLoginName( userLoginName );
        securityUser.setUserPassword( "test" );
        securityUserDao.persist( securityUser );
    }

    //  Util methods for inserting test data to DB below

    /** Persist SecurityUser, SecurityRoleUser and SecurityRole records linked to each other with 1-to-many (i.e. SecurityUser
     *  to SecurityRoleUser) and many-to-1(i.e. SecurityRoleUser to SecurityRole) relationships to DB **/
    private void addNewUserWithRoleUserAndRoleRecords(String roleName, String loginName, String userPassword, String createdBy) {

        // Persist a new role
        SecurityRole securityRole = new SecurityRole();
        securityRole.setRoleName( roleName );
        entityManager.persist( securityRole );
        assertNotNull(securityRole.getRoleId()); // role persisted
        System.out.println(" role id: " + securityRole.getRoleId());

        testDataMap.put(TestDataKey.SECURITY_ROLE, securityRole); // store test data

        // Persist a new user with the role above by adding a child SecurityRoleUser object
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUserLoginName( loginName );
        securityUser.setUserPassword( userPassword );

        SecurityRoleUser securityRoleUser = new SecurityRoleUser(); // SecurityRoleUser record
        securityRoleUser.setSecurityRole(securityRole);
        securityRoleUser.setSecurityUser(securityUser);
        securityRoleUser.setCreatedDate(new Date());
        securityRoleUser.setCreatedBy(createdBy);

        securityUser.addSecurityRoleUser( securityRoleUser );
        securityUserDao.persist( securityUser );
        assertNotNull( securityUser.getUserId() ); // verify SecurityUser persisted

        // store test data
        testDataMap.put( TestDataKey.SECURITY_USER, securityUser);
        testDataMap.put( TestDataKey.SECURITY_ROLE_USER, securityRoleUser);

        // verify SecurityRoleUser record persisted with links to both SecurityRole and user SecurityUser
        assertTrue(securityRoleUser.getId().getSecurityRole().getRoleId() == securityRole.getRoleId());
        assertTrue( securityRoleUser.getId().getSecurityUser().getUserId() == securityUser.getUserId() );

        System.out.println("user: " + securityUser);
        System.out.println("role user: " + securityRoleUser);
        System.out.println("role: " + securityRole);
        System.out.println();
    }


    @Test
    public void test() throws Exception {
        SecurityUser securityUser = securityUserDao.getUserByLoginName( "Tester124" );
        assertTrue( securityUser.getSecurityRoleUsers().size() == 5 );

        List<SecurityRoleUser> list = new ArrayList<SecurityRoleUser>( securityUser.getSecurityRoleUsers() );
        securityUser.removeSecurityRoleUser( list.get(0) );
        securityUser.removeSecurityRoleUser( list.get(1) );
        securityUserDao.merge( securityUser );

        securityUserDao.flush();

        securityUser = securityUserDao.getUserByLoginName( "Tester124" );
        assertTrue( securityUser.getSecurityRoleUsers().size() == 3 );

    }

    @Test
    public void test2() throws Exception {
        SecurityUser securityUser = securityUserDao.getUserByLoginName( "Tester124" );
        assertTrue( securityUser.getSecurityRoleUsers().size() == 5 );


        securityUserDao.deleteUserByLoginName( "Tester124" );
        securityUserDao.flush();

        securityUser = securityUserDao.getUserByLoginName( "Tester124" );
        assertTrue( securityUser == SecurityUser.EMPTY_SECURITY_USER );

    }

    @Test
    public void test_deleteUserByLoginName() throws Exception {

        String userLoginName = "OKIDOKI";

        // verify securityUser not deleted yet
        SecurityUser securityUser = securityUserDao.getUserByLoginName( userLoginName );
        assertTrue( securityUser.getUserLoginName().equals( userLoginName ) );

        securityUserDao.deleteUserByLoginName( userLoginName );

        securityUser = securityUserDao.getUserByLoginName( userLoginName );
        assertTrue( securityUser == SecurityUser.EMPTY_SECURITY_USER ); // yup, securityUser deleted
    }
}
