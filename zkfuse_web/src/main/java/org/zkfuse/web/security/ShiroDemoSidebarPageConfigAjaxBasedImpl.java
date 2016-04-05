/* 
	Adapted from
		ZK Essentials

*/
package org.zkfuse.web.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.zkoss.essentials.services.SidebarPage;
import org.zkoss.essentials.services.SidebarPageConfig;

public class ShiroDemoSidebarPageConfigAjaxBasedImpl implements SidebarPageConfig{

	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();

	public ShiroDemoSidebarPageConfigAjaxBasedImpl(){

        pageMap.put("home",new SidebarPage("home","Home","/imgs/fn.png","/security/shiro_home.zul"));
        pageMap.put("marketing",new SidebarPage("marketing","Marketing Admin","/imgs/fn.png","/security/shiro_demo/marketing.zul"));
        pageMap.put("sales",new SidebarPage("sales","Sales Admin","/imgs/fn.png","/security/shiro_demo/sales.zul"));
        pageMap.put("products",new SidebarPage("products","Products Admin","/imgs/fn.png","/security/shiro_demo/products.zul"));
        //pageMap.put("access_denied",new SidebarPage("access_denied","Access Denied","/imgs/access_denied_2.jpg","/security/accessdenied.zul"));
    }

	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
	
}
