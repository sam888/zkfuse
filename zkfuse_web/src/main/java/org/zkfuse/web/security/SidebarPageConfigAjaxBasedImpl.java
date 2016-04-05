/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package org.zkfuse.web.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.zkoss.essentials.services.SidebarPage;
import org.zkoss.essentials.services.SidebarPageConfig;

public class SidebarPageConfigAjaxBasedImpl implements SidebarPageConfig{
	
	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();

	public SidebarPageConfigAjaxBasedImpl(){		

        pageMap.put("fn0",new SidebarPage("fn0","Home","/imgs/fn.png","/security/home.zul"));
        pageMap.put("fn1",new SidebarPage("fn1","User Admin","/imgs/fn.png","/security/admin/user-admin.zul"));
        pageMap.put("fn2",new SidebarPage("fn2","Role Admin","/imgs/fn.png","/security/admin/role-admin.zul"));
        pageMap.put("fn3",new SidebarPage("fn3","Permission Admin","/imgs/fn.png","/security/admin/permission-admin.zul"));
    }

	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
	
}
