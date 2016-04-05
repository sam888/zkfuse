package org.zkfuse.dao.i18n;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 *
 * @author Samuel Huang
 */
public interface ResourceBundleDao extends IBasicDao<ResourceBundle, Long>  {

    ResourceBundle getResourceBundle( ResourceBundle resourceBundle );

    ResourceBundle getResourceBundle( Locale locale, Module module, String resourceBundleName );

    List<ResourceBundle> getResourceBundleByWildCardName(String resourceBundleName);

    void deleteResourceBundle( ResourceBundle nonExistingResourceBundle ) throws EntityNotFoundException ;

    void deleteResourceBundle( Locale locale, Module module, String resourceBundleName ) throws EntityNotFoundException;
    
}
