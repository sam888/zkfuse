package org.zkfuse.web.security.enums;

/**
 * @author Samuel Huang
 */
public enum PageIdEnum {

    HOME_PAGE("HOME_PAGE"),
    MARKETING_PAGE("MARKETING_PAGE"),
    SALES_PAGE("SALES_PAGE"),
    PRODUCTS_PAGE("PRODUCTS_PAGE");

    private String pageId;

    private PageIdEnum(String pageId) {
        this.pageId = pageId;
    }

    public String value() {
        return pageId;
    }

    public static boolean contains( String pageId) {
        for ( PageIdEnum pageIdEnum: values() ) {
           if ( pageIdEnum.value().equals( pageId ) ) {
               return true;
           }
        }
        return false;
    }
}
