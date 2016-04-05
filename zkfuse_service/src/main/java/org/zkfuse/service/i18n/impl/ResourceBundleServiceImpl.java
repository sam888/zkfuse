package org.zkfuse.service.i18n.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.ResourceBundleDao;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.i18n.ResourceBundleService;
import org.zkfuse.service.impl.GenericService;

@Service
public class ResourceBundleServiceImpl extends GenericService<ResourceBundle, Long> implements ResourceBundleService{

    @Autowired
    private ResourceBundleDao resourceBundleDao;
    
    public ResourceBundleServiceImpl() {
        super(ResourceBundle.class);
    }
    
    public ResourceBundle getResourceBundle(ResourceBundle resourceBundle) {
        return resourceBundleDao.getResourceBundle( resourceBundle );
    }

    public ResourceBundle getResourceBundle(Locale locale, Module module, String resourceBundleName) {
        return resourceBundleDao.getResourceBundle(locale, module, resourceBundleName);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteResourceBundle(ResourceBundle resourceBundle) throws EntityNotFoundException {
        resourceBundleDao.deleteResourceBundle( resourceBundle );
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteResourceBundle(Locale locale, Module module, String resourceBundleName) throws EntityNotFoundException {
        resourceBundleDao.deleteResourceBundle(locale, module, resourceBundleName);
    }

    public List<ResourceBundle> getResourceBundleByWildCardName( String resourceBundleName) {
        if ( StringUtils.isNotBlank(resourceBundleName) && resourceBundleName.length() > 1 ) {
            return resourceBundleDao.getResourceBundleByWildCardName( resourceBundleName );
        }
        return new ArrayList<ResourceBundle>();
    }
}
