package org.zkfuse.web.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.util.Contracts.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Sam
 * Date: 26/08/13
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true )
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class PropertiesUtilTest {

    @Autowired
    private PropertiesUtil propertiesUtil;

    @Test
    public void test_getWebAppPort_success() throws Exception {
        assertNotNull( propertiesUtil );
        String webAppPort = propertiesUtil.getWebAppPort();
        assertNotNull( webAppPort );
        System.out.println("webAppPort: " + webAppPort);
    }
}
