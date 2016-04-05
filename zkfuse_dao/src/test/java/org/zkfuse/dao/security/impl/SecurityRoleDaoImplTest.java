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
import org.zkfuse.dao.security.SecurityRoleDao;
import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.model.security.SecurityPermissionRole;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.HashMap;
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
public class SecurityRoleDaoImplTest {

    /** Used to persist SecurityUser. Note SecurityUserDao not used since securityRoleDao is our focus */
    @PersistenceContext(unitName="zkfuse")
    private EntityManager entityManager;

    enum TestDataKey {
        SECURITY_USER, SECURITY_ROLE, SECURITY_ROLE_USER, SECURITY_PERMISSION, SECURITY_PERMISSION_ROLE
    }

    private Map<Object, Object> testDataMap;

    @Autowired
    private SecurityRoleDao securityRoleDao;

    @Before
    public void init() {
        testDataMap = new HashMap<Object, Object>();
    }

    @After
    public void cleanup() {
        testDataMap = null;
    }

    /**
     * Verify persisting a SecurityRole with a SecurityRoleUser child will persist both records(i.e. cascade persist)  to
     * DB and persisted SecurityRoleUser record will establish relationships to both SecurityUser & SecurityRole records in DB
     *
     * @throws Exception
     */
    @Test
    public void test_create_securityRole_with_child_securityRoleUser_cause_cascade_persist() throws Exception {

        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewRoleWithRoleUserAndUserRecords( roleName, userLoginName, userPassword, createdBy );
    }

    /**
     * Verify delete a SecurityRole record will cause cascade delete to an attached child SecurityRoleUser record in DB
     *
     * @throws Exception
     */
    @Test
    public void test_delete_securityRole_with_child_securityRoleUser_cause_cascade_delete() throws Exception {

        // insert test data to be deleted
        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewRoleWithRoleUserAndUserRecords( roleName, userLoginName, userPassword, createdBy );

        SecurityRoleUser persitedSecurityRoleUser = (SecurityRoleUser)testDataMap.get(TestDataKey.SECURITY_ROLE_USER );
        SecurityRoleUser retrievedSecurityRoleUser = entityManager.find(SecurityRoleUser.class, persitedSecurityRoleUser.getId());

        assertNotNull( retrievedSecurityRoleUser ); // verify child record still exists

        SecurityRole securityRole = (SecurityRole)testDataMap.get( TestDataKey.SECURITY_ROLE );
        securityRoleDao.remove( SecurityRole.class, securityRole.getRoleId() );
        try {
            SecurityRole securityRoleAfterDelete = securityRoleDao.get( securityRole.getRoleId() );
            fail( "securityRole not deleted, what the..." );
        } catch (Exception ex) {
            assertTrue( ex instanceof EntityNotFoundException );  // yup, parent record deleted
        }

        retrievedSecurityRoleUser = entityManager.find(SecurityRoleUser.class, persitedSecurityRoleUser.getId());
        assertTrue( retrievedSecurityRoleUser == null ); // yup, child record deleted
    }

    @Test
    public void test_getRoleByRoleName_success() throws Exception {

        // insert test data to DB
        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewRoleWithRoleUserAndUserRecords( roleName, userLoginName, userPassword, createdBy ); // verified to be working by tests  above

        SecurityRole securityRole = securityRoleDao.getRoleByRoleName( roleName );

        assertNotNull( securityRole );
        assertTrue(securityRole != SecurityRole.EMPTY_SECURITY_ROLE);
        assertTrue( securityRole.getRoleName().equals( roleName ) );
        System.out.println("sec role: " + securityRole);
    }

    @Test
    public void test_deleteRoleByLoginName_success() throws Exception {

        // insert test data to DB
        String roleName = "SUPER_ADMIN";
        String userLoginName = "Tester123";
        String userPassword = "12345AAA";
        String createdBy = "Sam";
        addNewRoleWithRoleUserAndUserRecords( roleName, userLoginName, userPassword, createdBy ); // verified to be working by tests  above

        // verify securityRole not deleted yet
        SecurityRole securityRole = securityRoleDao.getRoleByRoleName( roleName );
        assertTrue( securityRole.getRoleName().equals( roleName ) );

        securityRoleDao.deleteRoleByRoleName( roleName );

        securityRole = securityRoleDao.getRoleByRoleName( userLoginName );
        assertTrue( securityRole == SecurityRole.EMPTY_SECURITY_ROLE); // yup, securityRole deleted
    }



    //  Util methods for inserting test data to DB below

    /** Persist SecurityUser, SecurityRoleUser and SecurityRole records linked to each other with 1-to-many (i.e. SecurityUser
     *  to SecurityRoleUser) and many-to-1(i.e. SecurityRoleUser to SecurityRole) relationships to DB **/
    private void addNewRoleWithRoleUserAndUserRecords(String roleName, String loginName, String userPassword, String createdBy) {

        // Persist a new user
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUserLoginName(  loginName );
        securityUser.setUserPassword( userPassword );

        entityManager.persist( securityUser );

        assertNotNull( securityUser ); // user persisted
        System.out.println(" user id: " + securityUser.getUserId());

        testDataMap.put(TestDataKey.SECURITY_USER, securityUser); // store test data

        // Persist a new role with the user above by adding a child SecurityRoleUser object
        SecurityRole securityRole = new SecurityRole();
        securityRole.setRoleName( roleName );

        SecurityRoleUser securityRoleUser = new SecurityRoleUser(); // SecurityRoleUser record
        securityRoleUser.setSecurityRole(securityRole);
        securityRoleUser.setSecurityUser(securityUser);
        securityRoleUser.setCreatedDate(new Date());
        securityRoleUser.setCreatedBy(createdBy);

        // securityRole.addSecurityRoleUser( securityRoleUser );
        securityRoleDao.persist( securityRole );
        assertNotNull( securityUser.getUserId() ); // verify SecurityUser persisted

        // store test data
        testDataMap.put( TestDataKey.SECURITY_ROLE, securityRole);
        testDataMap.put( TestDataKey.SECURITY_ROLE_USER, securityRoleUser);

        // verify SecurityRoleUser record persisted with links to both SecurityRole and user SecurityUser
        assertTrue(securityRoleUser.getId().getSecurityRole().getRoleId() == securityRole.getRoleId());
        assertTrue( securityRoleUser.getId().getSecurityUser().getUserId() == securityUser.getUserId() );

        System.out.println("role: " + securityRole);
        System.out.println("role user: " + securityRoleUser);
        System.out.println("user: " + securityUser);
        System.out.println();
    }

    /**
     * Verify persisting a SecurityRole with a SecurityPermissionRole child will persist both records(i.e. cascade persist)  to
     * DB and persisted SecurityPermissionRole record will establish relationships to both SecurityPermission & SecurityRole records in DB
     *
     * @throws Exception
     */
    @Test
    public void test_create_securityRole_with_child_securityPermissionRole_cause_cascade_persist() throws Exception {

        // insert test data to DB
        String roleName = "SUPER_ADMIN";
        String permissionName = "EDIT_CLIENT_DETAILS";
        String createdBy = "Sam";
        addNewRoleWithRolePermissioAndPermissioRecords( permissionName, roleName, createdBy );
    }


    //  Testing cascade persist and delete from SecurityRole to SecurityPermissionRole below

    /**
     * Verify delete a SecurityRole record will cause cascade delete to an attached child SecurityPermissionRole record in DB
     *
     * @throws Exception
     */
    @Test
    public void test_delete_securityRole_with_child_securityPermissionRole_cause_cascade_delete() throws Exception {

        // insert test data to be deleted
        String roleName = "SUPER_ADMIN";
        String permissionName = "EDIT_CLIENT_DETAILS";
        String createdBy = "Sam";
        addNewRoleWithRolePermissioAndPermissioRecords( permissionName, roleName, createdBy );

        SecurityPermissionRole persitedSecurityPermissionRole = (SecurityPermissionRole)testDataMap.get( TestDataKey.SECURITY_PERMISSION_ROLE );
        SecurityPermissionRole retrievedSecurityPermissionRole = entityManager.find(SecurityPermissionRole.class, persitedSecurityPermissionRole.getId());

        assertNotNull( retrievedSecurityPermissionRole ); // verify child record still exists

        SecurityRole securityRole = (SecurityRole)testDataMap.get( TestDataKey.SECURITY_ROLE );
        securityRoleDao.remove( SecurityRole.class, securityRole.getRoleId() );
        try {
            SecurityRole securityRoleAfterDelete = securityRoleDao.get( securityRole.getRoleId() );
            fail( "securityRole not deleted, what the..." );
        } catch (Exception ex) {
            assertTrue( ex instanceof EntityNotFoundException );  // yup, parent record deleted
        }

        retrievedSecurityPermissionRole = entityManager.find(SecurityPermissionRole.class, persitedSecurityPermissionRole.getId());
        assertTrue( retrievedSecurityPermissionRole == null ); // yup, child record deleted
    }

    /** Persist SecurityRole, SecurityPermissionRole and SecurityPermission records linked to each other with 1-to-many
     * (i.e. SecurityRole to SecurityPermissionRole) and many-to-1(i.e. SecurityPermissionRole to SecurityPermission)
     * relationships to DB **/
    private void addNewRoleWithRolePermissioAndPermissioRecords(String permissionName, String roleName, String createdBy) {

        // Persist a new permission
        SecurityPermission securityPermission = new SecurityPermission();
        securityPermission.setPermissionName(permissionName);
        entityManager.persist( securityPermission );
        assertNotNull(securityPermission.getPermissionId()); // permission persisted
        System.out.println("Permission id: " + securityPermission.getPermissionId());

        testDataMap.put(TestDataKey.SECURITY_PERMISSION, securityPermission); // store persisted test data

        // Persist a role with the permission above by adding a child SecurityPermissionRole object
        SecurityRole securityRole = new SecurityRole();
        securityRole.setRoleName(roleName);

        SecurityPermissionRole securityPermissionRole = new SecurityPermissionRole(); // SecurityPermissionRole record
        securityPermissionRole.setSecurityRole( securityRole );
        securityPermissionRole.setSecurityPermission( securityPermission );
        securityPermissionRole.setCreatedDate(new Date());
        securityPermissionRole.setCreatedBy(createdBy);

        securityRole.addSecurityPermissionRole(securityPermissionRole);
        securityRoleDao.persist( securityRole );
        assertNotNull(securityRole.getRoleId()); // verify SecurityRole persisted

        // store persisted test data
        testDataMap.put( TestDataKey.SECURITY_ROLE, securityRole );
        testDataMap.put( TestDataKey.SECURITY_PERMISSION_ROLE, securityPermissionRole );

        // verify SecurityPermissionRole record persisted with links to both SecurityPermission and user SecurityRole
        assertTrue( securityPermissionRole.getId().getSecurityRole().getRoleId().equals( securityRole.getRoleId() ));
        assertTrue( securityPermissionRole.getId().getSecurityPermission().getPermissionId().equals( securityPermission.getPermissionId() ) );

        System.out.println("role: " + securityRole);
        System.out.println("permission role: " + securityPermissionRole);
        System.out.println("permission: " + securityPermission);
        System.out.println();
    }
}
