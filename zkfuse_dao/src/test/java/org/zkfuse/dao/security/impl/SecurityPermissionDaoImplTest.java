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
import org.zkfuse.dao.security.SecurityPermissionDao;
import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.model.security.SecurityPermissionRole;
import org.zkfuse.model.security.SecurityRole;

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
public class SecurityPermissionDaoImplTest {

    /** Used to persist SecurityRole. Note SecurityRoleDao not used since securityPermissionDao is our focus */
    @PersistenceContext(unitName="zkfuse")
    private EntityManager entityManager;

    enum TestDataKey {
        SECURITY_PERMISSION, SECURITY_ROLE, SECURITY_PERMISSION_ROLE
    }

    private Map<Object, Object> testDataMap;

    @Autowired
    private SecurityPermissionDao securityPermissionDao;

    private SecurityPermission testSecurityPermission;

    @Before
    public void init() {
        testDataMap = new HashMap<Object, Object>();
    }

    @After
    public void cleanup() {
        testDataMap = null;
    }

    /**
     * Verify persisting a SecurityPermission with a SecurityPermissionRole child will persist both records(i.e. cascade persist)  to
     * DB and persisted SecurityPermissionRole record will establish relationships to both SecurityPermission & SecurityRole records
     * in DB.
     *
     * @throws Exception
     */
    @Test
    public void test_create_securityPermission_with_child_securityRoleUser_cause_cascade_persist() throws Exception {

        String roleName = "SUPER_ADMIN";
        String permissionName = "EDIT_CLIENT_DETAILS";
        String createdBy = "Sam";
        addNewPermissionWithPermissionRoleAndRoleRecords( permissionName, roleName, createdBy );
    }

    /**
     * Verify delete a SecurityPermission record will cause cascade delete to an attached child SecurityPermissionRole record in DB
     *
     * @throws Exception
     */
    @Test
    public void test_delete_securityPermission_with_child_SecurityPermissionRole_cause_cascade_delete() throws Exception {

        // add test data into DB
        String roleName = "SUPER_ADMIN";
        String permissionName = "EDIT_CLIENT_DETAILS";
        String createdBy = "Sam";
        addNewPermissionWithPermissionRoleAndRoleRecords( permissionName, roleName, createdBy );

        SecurityPermissionRole persitedSecurityPermissionRole = (SecurityPermissionRole)testDataMap.get( TestDataKey.SECURITY_PERMISSION_ROLE );
        SecurityPermissionRole retrievedSecurityPermissionRole = entityManager.find(SecurityPermissionRole.class, persitedSecurityPermissionRole.getId());

        assertNotNull( retrievedSecurityPermissionRole ); // verify child record not deleted yet

        SecurityPermission securityPermission = (SecurityPermission)testDataMap.get( TestDataKey.SECURITY_PERMISSION );
        securityPermissionDao.remove(SecurityPermission.class, securityPermission.getPermissionId());
        try {
            SecurityPermission securityPermissionAfterDelete = securityPermissionDao.get( securityPermission.getPermissionId() );
            fail( "securityPermission not deleted, what the..." );
        } catch (Exception ex) {
            assertTrue( ex instanceof EntityNotFoundException );  // yup, parent record deleted
        }

        retrievedSecurityPermissionRole = entityManager.find(SecurityPermissionRole.class, persitedSecurityPermissionRole.getId());
        assertTrue( retrievedSecurityPermissionRole == null ); // yup, child record deleted
    }

    /**
     * Verify getSecurityPermissionByPermissionName(..) can return an existing SecurityPermission record by permission name from DB successfully
     *
     * @throws Exception
     */
    @Test
    public void test_getSecurityPermissionByPermissionName_success() throws Exception {

        // add test data into DB
        String roleName = "SUPER_ADMIN";
        String permissionName = "EDIT_CLIENT_DETAILS";
        String createdBy = "Sam";
        addNewPermissionWithPermissionRoleAndRoleRecords( permissionName, roleName, createdBy );

        SecurityPermission securityPermission = securityPermissionDao.getSecurityPermissionByPermissionName( permissionName );

        assertNotNull( securityPermission );
        assertTrue( securityPermission != SecurityPermission.EMPTY_SECURITY_PERMISSION );
        assertTrue( securityPermission.getPermissionName().equals( permissionName ) );
        System.out.println("sec permission: " + securityPermission);
    }

    /**
     * Verify deleteSecurityPermissionByPermissionName(..) can delete an existing SecurityPermission record by permission name from DB successfully.
     *
     * @throws Exception
     */
    @Test
    public void test_deleteUserByLoginName_success() throws Exception {

         // insert test data to DB
         String roleName = "SUPER_ADMIN";
         String permissionName = "EDIT_CLIENT_DETAILS";
         String createdBy = "Sam";
         addNewPermissionWithPermissionRoleAndRoleRecords( permissionName, roleName, createdBy ); // verified to be working by tests  above

         // verify securityPermission not deleted yet
        SecurityPermission securityPermission = securityPermissionDao.getSecurityPermissionByPermissionName( permissionName );
        assertTrue( securityPermission.getPermissionName().equals( permissionName ) );

        securityPermissionDao.deleteSecurityPermissionByPermissionName( permissionName );
        securityPermission = securityPermissionDao.getSecurityPermissionByPermissionName( permissionName );
        assertTrue( securityPermission == SecurityPermission.EMPTY_SECURITY_PERMISSION ); // yup, securityUser deleted
     }


    //  Util methods for inserting test data to DB below

    /** Persist SecurityPermission, SecurityPermissionRole and SecurityRole records linked to each other with 1-to-many
     * (i.e. SecurityPermission to SecurityPermissionRole) and many-to-1(i.e. SecurityPermissionRole to SecurityRole)
     * relationships to DB **/
    private void addNewPermissionWithPermissionRoleAndRoleRecords(String permissionName, String roleName, String createdBy) {

        // Persist a new role
        SecurityRole securityRole = new SecurityRole();
        securityRole.setRoleName( roleName );
        entityManager.persist( securityRole );
        assertNotNull(securityRole.getRoleId()); // role persisted
        System.out.println(" role id: " + securityRole.getRoleId());

        testDataMap.put(TestDataKey.SECURITY_ROLE, securityRole); // store test data

        // Persist a new permission with the role above by adding a child SecurityPermissionRole object
        SecurityPermission securityPermission = new SecurityPermission();
        securityPermission.setPermissionName(permissionName);

        SecurityPermissionRole securityPermissionRole = new SecurityPermissionRole(); // SecurityPermissionRole record
        securityPermissionRole.setSecurityRole(securityRole);
        securityPermissionRole.setSecurityPermission(securityPermission);
        securityPermissionRole.setCreatedDate(new Date());
        securityPermissionRole.setCreatedBy(createdBy);

        securityPermission.addSecurityPermissionRole(securityPermissionRole);
        securityPermissionDao.persist(securityPermission);
        assertNotNull(securityPermission.getPermissionId()); // verify SecurityPermission persisted

        // store test data
        testDataMap.put( TestDataKey.SECURITY_PERMISSION, securityPermission );
        testDataMap.put( TestDataKey.SECURITY_PERMISSION_ROLE, securityPermissionRole );

        // verify SecurityPermissionRole record persisted with links to both SecurityPermission and user SecurityRole
        assertTrue( securityPermissionRole.getId().getSecurityRole().getRoleId() == securityRole.getRoleId());
        assertTrue(securityPermissionRole.getId().getSecurityPermission().getPermissionId() == securityPermission.getPermissionId());

        System.out.println("permission: " + securityPermission);
        System.out.println("permission role: " + securityPermissionRole);
        System.out.println("role: " + securityRole);
        System.out.println();
    }
}
