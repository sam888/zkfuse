package org.zkfuse.web.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;

/**
 *
 * @author Samuel Huang
 */
public class ShiroLoginController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

    private static final String PAGE_ID = "LOGIN_PAGE";

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {

        Session session = Sessions.getCurrent();
        Subject subject = SecurityUtils.getSubject();

        // Redirect to home page if logged in already. No need to login again.
        //
        // This will also fix the Shiro bug:
        // After user logs in, open a new browser tab to visist login page again, users won't be able to login in
        // some browsers. Even if user is able to login, we will have the complications of managing 2 logged in users
        // (different or the same user), each of which occupies a browser tab.
        if ( subject.isAuthenticated() ) {
            Executions.getCurrent().sendRedirect("/security/shiro_index.zul");
            return null;
        }

        return super.doBeforeCompose(page, parent, compInfo);
    }

}
