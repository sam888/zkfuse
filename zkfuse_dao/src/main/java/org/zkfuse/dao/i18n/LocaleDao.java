package org.zkfuse.dao.i18n;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.i18n.Locale;

import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Samuel Huang
 */
public interface LocaleDao extends IBasicDao<Locale, Long> {

    /**
     * Returns a locale based on unique combination of language code, country
     * code and variant code.
     * 
     * @param languageCode
     * @param countryCode
     * @param variantCode
     * @return
     * @throws EntityNotFoundException
     */
    Locale getLocale(String languageCode, String countryCode, String variantCode);

    Locale getLocale(Locale locale);

    // Locale getLocaleByName(String name);

    /**
     * 
     * @return
     * @throws EntityNotFoundException
     */
    void deleteLocale(String languageCode, String countryCode, String variantCode) throws EntityNotFoundException;

    void deleteLocale(Locale locale) throws EntityNotFoundException;

    // void deleteLocaleByName(String name) throws EntityNotFoundException;
}
