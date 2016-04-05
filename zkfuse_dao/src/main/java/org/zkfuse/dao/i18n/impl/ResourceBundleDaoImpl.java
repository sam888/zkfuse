package org.zkfuse.dao.i18n.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.i18n.ResourceBundleDao;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Samuel Huang
 */
@Service
public class ResourceBundleDaoImpl extends BasicDao<ResourceBundle, Long> implements ResourceBundleDao {

    
    /** Constructor that sets the entity to Module.class */
    public ResourceBundleDaoImpl() {
        super(ResourceBundle.class);
    }
    

    public ResourceBundle getResourceBundle(Locale locale, Module module, String resourceBundleName) {
        
        Object obj = null;
        Object[] namedQueryData = new Object[] { locale.getLocaleId(), module.getModuleId(), resourceBundleName };
        
        try {
            obj = findNamedQuerySingle( ResourceBundle.NAMED_QUERY_GET_RESOURCE_BUNDLE, namedQueryData );
        } catch (NoResultException ex) {
            return ResourceBundle.EMPTY_RESOURCE_BUNDLE;
        }
        
        return ( obj != null ? (ResourceBundle)obj : ResourceBundle.EMPTY_RESOURCE_BUNDLE );
        
    }

    
    public ResourceBundle getResourceBundle(ResourceBundle resourceBundle) {
        
        if (resourceBundle == null) {
            return  ResourceBundle.EMPTY_RESOURCE_BUNDLE;
        }
        
        return getResourceBundle(resourceBundle.getLocale(), resourceBundle.getModule(), resourceBundle.getResourceBundleName());
    }

    public List<ResourceBundle> getResourceBundleByWildCardName(String resourceBundleName) {
        
        List<ResourceBundle> resourceBundleList = new ArrayList<ResourceBundle>();
        
        try {
            Query query = entityManager.createNamedQuery( ResourceBundle.NAMED_QUERY_GET_RESOURCE_BUNDLE_BY_WILD_CARD_NAME );
            query.setParameter(1, "%" + resourceBundleName + "%");
            resourceBundleList = query.getResultList();
            
        } catch (NoResultException ex) {
            return new ArrayList<ResourceBundle>();
        }
        
        return resourceBundleList;
        
    }


    public void deleteResourceBundle(ResourceBundle resourceBundle) throws EntityNotFoundException {
        deleteResourceBundle(resourceBundle.getLocale(), resourceBundle.getModule(), resourceBundle.getResourceBundleName());
    }


    public void deleteResourceBundle(Locale locale, Module module, String resourceBundleName) throws EntityNotFoundException {
        ResourceBundle resourceBundle = getResourceBundle(locale, module, resourceBundleName );
        remove( ResourceBundle.class, resourceBundle.getResourceBundleId() );
    }
}
