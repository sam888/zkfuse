package org.zkfuse.service.i18n.util;

import org.apache.commons.lang.StringUtils;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.i18n.ServiceLocator;

public final class ResourceBundleUtil {
    
    public static ResourceBundle getResourceBundle( java.util.Locale locale, String moduleName ) {
        
        Module module = ServiceLocator.getModuleService().getModuleByName( moduleName );
        
        if ( module == Module.EMPTY_MODULE ) 
            return ResourceBundle.EMPTY_RESOURCE_BUNDLE;
        
        if (locale==null) return null;
        
        Locale customLocale = ServiceLocator.getLocaleService().getLocale( locale.getLanguage(), locale.getCountry(), 
                StringUtils.trimToEmpty( locale.getVariant()) );
        
        if ( customLocale == Locale.EMPTY_LOCALE ) return ResourceBundle.EMPTY_RESOURCE_BUNDLE;
        
        String resourceBundleName = generateResourceBundleName(customLocale, module);
        return ServiceLocator.getResourceBundleService().getResourceBundle(customLocale, module, resourceBundleName);
    }
    
    public static String generateResourceBundleName(Locale locale, Module module) {
        if ( locale != null  &&  module != null ) {
            return module.getName() + "_" + locale.getLocaleValue();
        }
        return null;
    }
    
    public static boolean doesResourceBundleExist(java.util.Locale locale, String moduleName) {
        ResourceBundle resourceBundle = ResourceBundleUtil.getResourceBundle(locale, moduleName);
        return ( resourceBundle != ResourceBundle.EMPTY_RESOURCE_BUNDLE );
    }
  
}
