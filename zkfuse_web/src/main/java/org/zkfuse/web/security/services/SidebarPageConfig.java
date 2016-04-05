/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package org.zkfuse.web.security.services;

import java.util.List;

/**
 * Adapted from sample projects of ZK Essentials
 */
public interface SidebarPageConfig {
	/** get pages of this application **/
	public List<SidebarPage> getPages();

	/** get page **/
	public SidebarPage getPage(String name);
}
