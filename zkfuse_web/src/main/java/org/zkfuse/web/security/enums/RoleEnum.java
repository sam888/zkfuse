
package org.zkfuse.web.security.enums;

/**
 * @author Samuel Huang
 */
public enum RoleEnum {

    ADMIN_ROLE("ADMIN_ROLE"), MARKETING_ROLE("MARKETING_ROLE"), SALES_ROLE("SALES_ROLE"), PRODUCTS_ROLE("PRODUCTS_ROLE");

    private String roleName;

    private RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public static boolean contains( String role) {
        for ( RoleEnum roleEnum: values() ) {
            if ( roleEnum.value().equals( role ) ) {
                return true;
            }
        }
        return false;
    }

    public String value() {
        return roleName;
    }
}
