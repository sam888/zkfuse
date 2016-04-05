package org.zkfuse.web.security.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.zkfuse.model.security.SecurityUser;
import org.zkfuse.service.security.SecurityUserService;
import org.zkfuse.util.context.AppContext;
import org.zkfuse.web.security.enums.PageIdEnum;
import org.zkfuse.web.security.enums.RoleEnum;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 26/07/13
 * Last update: 3/08/13
 *
 * A util class for authorization
 */
public class ShiroAuthorizationUtil {

    /**
     * A map whose key is page ID and value being a list of roles. In real life, data for this map will be read in from a
     * properties file, xml file or DB during server start up and initialized by a spring bean. For demo, let's just
     * hard code its' data.
     */
    private static Map<String, List<String>> pageIdToRoleListMap = new ConcurrentHashMap<String, List<String>>();

    private final static String PROPERTIES_FILE_NAME = "/pagId-roles.properties";
    static {
        loadPageIdRolesByPropertiesFile();
    }

    /**
     * Only call this method after user has been authenticated.
     *
     * @param pageId
     * @param username
     * @return
     */
    public static boolean isAuthorized(String pageId, String username) {

        // All authenticated user can see home page
        if ( PageIdEnum.HOME_PAGE.value().equals( pageId ) ) {
            return true;
        }

        SecurityUser securityUser = getSecurityUserService().getUserByLoginName( username );
        if ( securityUser != SecurityUser.EMPTY_SECURITY_USER ) {
            List<String> userRoleList = securityUser.getRoleList();

            List<String> requiredRoleList = pageIdToRoleListMap.get( pageId );
            if ( requiredRoleList != null ) {
                return CollectionUtils.containsAny( requiredRoleList, userRoleList );
            }
        }
        return false;
    }

    public static void loadPageIdRolesByPropertiesFile( ) {
        Properties configProp = new Properties();
        InputStream in = ShiroAuthorizationUtil.class.getResourceAsStream( PROPERTIES_FILE_NAME );

        if ( in == null ) {
            throw new IllegalStateException("Error reading pagId-roles.properties");
        }

        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            // log it
            throw new IllegalStateException("Error reading pagId-roles.properties");
        }

        for(String key : configProp.stringPropertyNames()) {

            if ( !PageIdEnum.contains( key ) ) continue;

            String value = configProp.getProperty(key);

            List<String> unfilteredRoleList = Arrays.asList( value.split("\\s*,\\s*") );
            List<String> roleList = new ArrayList<String>();
            for ( String role: unfilteredRoleList ) {
                if ( RoleEnum.contains( role ) ) {
                    roleList.add( role );
                }
            }
            pageIdToRoleListMap.put(key, roleList);
        }
    }

    private static SecurityUserService getSecurityUserService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (SecurityUserService)ctx.getBean("securityUserServiceImpl");
    }


   /* public static void main(String args[]) throws Exception {

        loadPageIdRolesByPropertiesFile();
        for (Map.Entry<String, List<String>> entry : pageIdToRoleListMap.entrySet()) {
            String pageId = entry.getKey();

            System.out.print("pagId: " + pageId + " = ");
            List<String> roleList = entry.getValue();
            for ( String role : roleList) {
                System.out.print(role + ",");
            }
            System.out.println();
        }
        System.out.println("end");
    }*/
}
