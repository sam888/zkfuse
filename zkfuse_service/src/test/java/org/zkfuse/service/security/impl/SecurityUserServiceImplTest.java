package org.zkfuse.service.security.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.model.security.SecurityRoleUser;
import org.zkfuse.model.security.SecurityUser;
import org.zkfuse.service.security.SecurityUserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Integration test for SecurityUserServiceImpl.java. Hence accessing DB directly.
 *
 * @author Samuel Huang
 */
@ContextConfiguration(locations = {"classpath:spring/applicationContext-service.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true )
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SecurityUserServiceImplTest {

    /** Used to persist SecurityRole. Note SecurityRoleDao not used since securityUserDao is our focus */
    @PersistenceContext(unitName="zkfuse")
    private EntityManager entityManager;

    enum TestDataKey {
        SECURITY_USER, SECURITY_ROLE, SECURITY_ROLE_USER
    }

    private Map<Object, Object> testDataMap;

    @Autowired
    private SecurityUserService securityUserService;

    private SecurityUser testSecurityUser;

    @Before
    public void init() {
        testDataMap = new HashMap<Object, Object>();
    }

    @After
    public void cleanup() {
        testDataMap = null;
    }

    @Test
    public void test() throws Exception {


    }

}
