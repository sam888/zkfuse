package org.zkfuse.web.util.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.zkfuse.model.i18n.ResourceBundle;

/**
 * @author Samuel Huang
 */
public class LabelsUtil {

    /** 
     * A map of (Locale l, Map(String moduleName, DBResourceBundleLocator dbResourceBundleLocator)). ConcurrentHashMap is used
     * since it is a static field providing concurrent access to multiple threads and we want it to be thread safe.
     */
    private static Map<Locale, Map<String, DBResourceBundleLocator>> dbResourceBundleLocatorMap = new ConcurrentHashMap<Locale, Map<String, DBResourceBundleLocator>>();

    
    public static void registerNonExistingDBResourceBundleLocator(Locale locale, String moduleName) {
        
        if ( locale == null || StringUtils.isBlank( moduleName )) {
            // throw new IllegalArgumentException("locale and moduleName argument can not be null");
            return; // can't proceed, do nothing
        }
        
        boolean isDBResourceBundleLocatorRegistered = isDBResourceBundleLocatorRegistered(locale, moduleName);
        if ( isDBResourceBundleLocatorRegistered ) {
            return; // no need to register the same DBResourceBundleLocator twice
        }

        Map<String, DBResourceBundleLocator> moduleNameToDBresourceBundleLocatorMap = dbResourceBundleLocatorMap.get( locale );
        boolean needToAdd_moduleNameToDBresourceBundleLocatorMap = false;
        if ( moduleNameToDBresourceBundleLocatorMap == null ) {
            needToAdd_moduleNameToDBresourceBundleLocatorMap = true;
            moduleNameToDBresourceBundleLocatorMap = new HashMap<String, DBResourceBundleLocator>();
        }

        DBResourceBundleLocator dbResourceBundleLocator = new DBResourceBundleLocator(locale, moduleName);
        dbResourceBundleLocator.populateKeyValueMap();

        moduleNameToDBresourceBundleLocatorMap.put( moduleName, dbResourceBundleLocator);

        if ( needToAdd_moduleNameToDBresourceBundleLocatorMap ) {
            dbResourceBundleLocatorMap.put( locale, moduleNameToDBresourceBundleLocatorMap );
        }
        // Labels.register( dbResourceBundleLocator );
    }
    
    public static boolean isDBResourceBundleLocatorRegistered(Locale locale, String moduleName) {
        Map<String, DBResourceBundleLocator> map = dbResourceBundleLocatorMap.get( locale );
        if ( map == null ) {
            return false;
        }
        DBResourceBundleLocator  dbResourceBundleLocator = map.get( moduleName );
        return (dbResourceBundleLocator != null);
    }
    
    public static DBResourceBundleLocator getDBResourceBundleLocator(Locale locale, String moduleName) {
        if ( locale == null || moduleName == null) {
            return null;
        }
        Map<String, DBResourceBundleLocator> map = dbResourceBundleLocatorMap.get( locale );
        if ( map == null ) {
            return null;
        }
        return map.get( moduleName );
    }
    
    public static java.util.Locale convertJpaLocaleToJavaLocale(org.zkfuse.model.i18n.Locale locale) {
        if (locale == null) return null;
        return new Locale( locale.getLanguageCode(), locale.getCountryCode(), locale.getVariantCode() );
    }
    
    /**
     * ZK 6.0.2:
     * 
     * org.zkoss.util.resource.Labels.getLabel(..) not used since ZK implementation of retrieving
     * labels from LabelLoader has many hard to debug problems using the dbResourceBundleLocatorMap 
     * map approach to manage resource bundle loaded from DB in this class, e.g. wrong label for
     * different locale can get loaded after changing current locale in session but sometimes it
     * will work --> unpredictable behaviour. And the functionalities/complexity provided by ZK's 
     * Labels & LabelsLoader are not really required if we can simply load & manage labels 
     * from backend.
     * 
     * Debugging in Labels.getLabel(..) will lead to LabelLoader which maintains multiple copies
     * of the same registerd DBResourceBundleLocator(i.e. unique locale and moduleName) in _locators variable 
     * even though each DBResourceBundleLocator is only registered once in dbResourceBundleLocatorMap.
     *  
     * To really use ZK's Labels class to load labels using current locale in session through 
     * dbResourceBundleLocatorMap will require extending ZK's Labels/LabelsLoader to manage it's internal
     * states --> not worth the effort. 
     * 
     * ZK's Labels & LabelsLoader classes seemed optimized and designed to load labels from property files, not DB.
     * 
     * @param key
     * @return
     */
    public static String getLabel(String key) {
        
        if ( StringUtils.isBlank( key ) ) return  curlyBracketLabelKey("");
        
        Locale locale = SessionUtil.getLocaleInSession();
        String moduleName = SessionUtil.getModuleNameInSession();
        DBResourceBundleLocator dbResourceBundleLocator = getDBResourceBundleLocator(locale, moduleName);
        
        if ( dbResourceBundleLocator == null ) return curlyBracketLabelKey( key );
        
        String label = dbResourceBundleLocator.getLabel( key );
        if ( StringUtils.isBlank( label ) ) {
            return curlyBracketLabelKey( key );
        }
        return label;
    }
    
    private static String curlyBracketLabelKey(String key) {
        return "{" + key + "}";
    }

    public static void reloadResourceBundle(ResourceBundle resourceBundle) {
        if ( resourceBundle == null ) return;
        
        Locale locale = convertJpaLocaleToJavaLocale( resourceBundle.getLocale() );
        String moduleName = resourceBundle.getModule().getName();
        
        DBResourceBundleLocator dbResourceBundleLocator = getDBResourceBundleLocator( locale, moduleName );
        reloadKeyValuesOfDBResourceBundleLocator( dbResourceBundleLocator );
    }
    
    public static void reloadAllResourceBundle() {
        //        for ( Map<String, DBResourceBundleLocator> moduleNameToDBResourceBundleLocatorMap: dbResourceBundleLocatorMap.values() ) {
        //            for ( DBResourceBundleLocator dbResourceBundleLocator: moduleNameToDBResourceBundleLocatorMap.values() ) {
        //                reloadKeyValuesOfDBResourceBundleLocator( dbResourceBundleLocator );
        //            }
        //        }
        dbResourceBundleLocatorMap.clear();
    }
    
    public static void reloadKeyValuesOfDBResourceBundleLocator(DBResourceBundleLocator dbResourceBundleLocator) {
        if ( dbResourceBundleLocator != null ) {
            dbResourceBundleLocator.reloadKeyValues();
        }
    }
    
}
