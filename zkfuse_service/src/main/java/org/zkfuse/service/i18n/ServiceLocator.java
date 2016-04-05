package org.zkfuse.service.i18n;

import org.springframework.context.ApplicationContext;
import org.zkfuse.util.context.AppContext;

/**
 * @author Samuel Huang
 */
public class ServiceLocator {

    public static ResourceBundleService getResourceBundleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (ResourceBundleService)ctx.getBean("resourceBundleServiceImpl");
    }

    public static LocaleService getLocaleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (LocaleService)ctx.getBean("localeServiceImpl");
    }

    public static ModuleService getModuleService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (ModuleService)ctx.getBean("moduleServiceImpl");
    }
    
    public static KeyValueService getKeyValueService() {
        ApplicationContext ctx = AppContext.getApplicationContext();
        return (KeyValueService)ctx.getBean("keyValueServiceImpl");
    }

}
