package org.zkfuse.service.i18n;

import javax.persistence.EntityNotFoundException;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.service.IGenericService;


public interface LocaleService extends IGenericService<Locale, Long> { 

    Locale getLocale(Locale locale);
    
    Locale getLocale(String languageCode, String countryCode, String variantCode);
    
    void deleteLocale(String languageCode, String countryCode, String variantCode) throws EntityNotFoundException;
    
    void deleteLocale(Locale locale) throws EntityNotFoundException;
    
}
