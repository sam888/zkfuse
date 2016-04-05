/*
	Adapted from
		ZK Essentials
*/
package org.zkfuse.web.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zkfuse.web.security.ShiroDemoSidebarPageConfigAjaxBasedImpl;
import org.zkoss.essentials.services.SidebarPage;
import org.zkoss.essentials.services.SidebarPageConfig;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Include;

public class ShiroDemoBookmarkChangeController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	SidebarPageConfig pageConfig = new ShiroDemoSidebarPageConfigAjaxBasedImpl();

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {

        Session session = Sessions.getCurrent();
        Subject subject = SecurityUtils.getSubject();

        // Check if user is authenticated. If not, return null so banner, sidebar and footer won't be displayed at all.
        if ( !subject.isAuthenticated() ) {
            return null;
        }

        return super.doBeforeCompose(page, parent, compInfo);
    }
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (comp.getParent() != null) {
			throw new RuntimeException("A bookmark change listener can only apply on the root comp");
		}

		comp.addEventListener("onBookmarkChange", new SerializableEventListener<BookmarkEvent>() {
			private static final long serialVersionUID = 1L;

			public void onEvent(BookmarkEvent event) throws Exception {
				String bookmark = event.getBookmark();
				if(bookmark.startsWith("p_")){
					String p = bookmark.substring("p_".length());
					SidebarPage page = pageConfig.getPage(p);
					
					if(page!=null){
						//use iterable to find the first include only
						Include include = (Include) Selectors.iterable(getPage(), "#mainInclude").iterator().next();
						include.setSrc(page.getUri());
					}
				}
			}
		});
	}
}
