package org.zkfuse.dao.i18n.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.i18n.LocaleDao;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.model.i18n.Locale;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 *
 * @author Samuel Huang
 */
@Service
public class LocaleDaoImpl extends BasicDao<Locale, Long> implements LocaleDao {
    
    // private static final Logger logger = LoggerFactory.getLogger(LocaleDaoImpl.class);
    
    /** Constructor that sets the entity to Locale.class */
    public LocaleDaoImpl() {
        super(Locale.class);
    }
    
    public Locale getLocale(String languageCode, String countryCode, String variantCode) {
        Object obj = null;
        Object[] paramsArray = new Object[] { languageCode, countryCode, variantCode};
        try {
            obj = findNamedQuerySingle( Locale.NAMED_QUERY_GET_LOCALE_BY_PARAMETERS, paramsArray );
        } catch (NoResultException ex) {
            return Locale.EMPTY_LOCALE;
        }
        return ( obj != null ? (Locale)obj : Locale.EMPTY_LOCALE );
    }

    public Locale getLocale(Locale locale) {

        if (locale==null) return Locale.EMPTY_LOCALE;

        return getLocale(locale.getLanguageCode(), locale.getCountryCode(), locale.getVariantCode());
    }

    public void deleteLocale(Locale locale) throws EntityNotFoundException {
        deleteLocale(locale.getLanguageCode(), locale.getCountryCode(), locale.getVariantCode());
    }

    public void deleteLocale(String languageCode, String countryCode, String variantCode) throws EntityNotFoundException {
       Locale locale = getLocale(languageCode, countryCode, variantCode);
       remove(Locale.class, locale.getLocaleId());
    }
}
