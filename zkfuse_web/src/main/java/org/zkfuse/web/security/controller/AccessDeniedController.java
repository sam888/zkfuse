package org.zkfuse.web.security.controller;

/**
 * @author Samuel Huang
 */
public class AccessDeniedController extends ShiroBaseController {

	private static final long serialVersionUID = 1L;

    private static final String PAGE_ID = "PRODUCTS_PAGE";

    @Override
    public String getPageId() {
        return PAGE_ID;
    }

}
