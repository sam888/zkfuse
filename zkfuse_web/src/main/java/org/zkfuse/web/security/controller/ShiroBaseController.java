package org.zkfuse.web.security.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.zkfuse.web.security.util.ShiroAuthorizationUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Label;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 23/07/13
 */
public abstract class ShiroBaseController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;

    private String loggedInUsername;
    private String accessDenyUrl = "/security/accessdenied.zul";
    private String ajaxAccessDenyUrl = "/security/ajax_accessdenied.zul";

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {

        Session session = Sessions.getCurrent();
        Subject subject = SecurityUtils.getSubject();


        // Redirect to access deny page if not authenticated
        if ( !subject.isAuthenticated() ) {
            Executions.getCurrent().sendRedirect( accessDenyUrl );
            return null;
        }

        // Initialize logged in username
        setLoggedInUsername( getLoggedInUsername( subject ) );

        // Redirect to access deny page if not authorized
        if ( !ShiroAuthorizationUtil.isAuthorized( getPageId(), getLoggedInUsername() ) ) {
            gotoPage( ajaxAccessDenyUrl );
            return null;
        }

        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        if ( loggedInUsername != null ) {

                Component bannerComponent =  getPage().getFellowIfAny("shiro-banner");
                if ( bannerComponent != null ) {

                    // Sets username label in banner
                    Label usernameLabel = (Label)bannerComponent.getFellowIfAny("usernameLabel");
                    if ( usernameLabel != null ) {
                        usernameLabel.setValue("Welcome - " + loggedInUsername);
                    }

                    // Sets logout label in banner and logout action when label is clicked
                    Label logoutLabel = (Label)bannerComponent.getFellowIfAny("logoutLabel");
                    logoutLabel.setValue( "Logout");
                    logoutLabel.addEventListener(Events.ON_CLICK, new EventListener() {
                        public void onEvent(Event event) {
                            SecurityUtils.getSubject().logout();
                            Executions.getCurrent().sendRedirect("/security/shiro_logout.zul");
                        }
                    });
                }
        }
    }

    public String getLoggedInUsername(Subject subject) {

        String username = "";
        if ( subject != null ) {

            PrincipalCollection principalCollection =  subject.getPrincipals();
            if ( principalCollection != null ) {
                username = (String)principalCollection.getPrimaryPrincipal();
            }

        }
        return username;
    }

    // Getters & setters below

    public String getLoggedInUsername() {
        return loggedInUsername;
    }
    public void setLoggedInUsername(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    public String getAccessDenyUrl() {
        return accessDenyUrl;
    }

    public void setAccessDenyUrl(String accessDenyUrl) {
        this.accessDenyUrl = accessDenyUrl;
    }

    protected Component getMainContent() {
        return getPage().getFellowIfAny("mainInclude");
    }

    // Abstract methods below

    abstract String getPageId();

    //   Methods for ajax-based navigation below

    /**
     * Go to page. @param uri the uri
     */
    protected void gotoPage(String uri) {
        gotoPage(uri, null, null);
    }

    /**
     * Go to page.
     *
     * @param uri  the uri
     * @param parent the parent
     */
    protected void gotoPage(String uri, Component parent) {
        gotoPage(uri, parent, null);
    }

    /**
     * Go to page.
     *
     * @param uri the uri
     * @param parent the parent
     * @param parameters the parameters
     */
    protected void gotoPage(String uri, Component parent, Map parameters) {

        if (parent != null) {
            try {
                parent.getChildren().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            parent = getMainContent();
            if ( parent != null ) {
                try {
                    parent.getChildren().clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // For production, uri could be used as key to retrieve actual url to load page
        Executions.getCurrent().createComponents( uri, parent, parameters);

        // getPage().getDesktop().setBookmark( uri );
    }

}
