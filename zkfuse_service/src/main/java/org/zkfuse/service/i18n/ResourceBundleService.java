package org.zkfuse.service.i18n;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.zkfuse.model.i18n.Locale;
import org.zkfuse.model.i18n.Module;
import org.zkfuse.model.i18n.ResourceBundle;
import org.zkfuse.service.IGenericService;

/**
 * @author Samuel Huang
 */
public interface ResourceBundleService extends IGenericService<ResourceBundle, Long> {

    ResourceBundle getResourceBundle( ResourceBundle resourceBundle );

    ResourceBundle getResourceBundle( Locale locale, Module module, String resourceBundleName );
    
    List<ResourceBundle> getResourceBundleByWildCardName(String resourceBundleName);

    void deleteResourceBundle( ResourceBundle resourceBundle ) throws EntityNotFoundException ;

    void deleteResourceBundle( Locale locale, Module module, String resourceBundleName ) throws EntityNotFoundException;
}
