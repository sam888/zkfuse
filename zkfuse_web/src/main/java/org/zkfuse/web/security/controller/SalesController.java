package org.zkfuse.web.security.controller;

/**
 * @author Samuel Huang
 */
public class SalesController extends ShiroBaseController {

	private static final long serialVersionUID = 1L;

    private static final String PAGE_ID = "SALES_PAGE";

    @Override
    public String getPageId() {
        return PAGE_ID;
    }

}
