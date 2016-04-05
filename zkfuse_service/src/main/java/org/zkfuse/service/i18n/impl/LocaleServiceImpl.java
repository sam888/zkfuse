package org.zkfuse.service.i18n.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.LocaleDao;
import org.zkfuse.model.i18n.Locale;
import org.zkfuse.service.i18n.LocaleService;
import org.zkfuse.service.impl.GenericService;

/**
 *
 * @author Samuel Huang
 */
@Service
public class LocaleServiceImpl extends GenericService<Locale, Long> implements LocaleService {
    
    @Autowired
    private LocaleDao localeDao;
    
    public LocaleServiceImpl() {
        super(Locale.class);
    }

    public Locale getLocale(Locale locale) {
        return localeDao.getLocale( locale );
    }

    public Locale getLocale(String languageCode, String countryCode, String variantCode) {
        return localeDao.getLocale(languageCode, countryCode, variantCode);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteLocale(String languageCode, String countryCode, String variantCode) throws EntityNotFoundException {
        localeDao.deleteLocale(languageCode, countryCode, variantCode);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteLocale(Locale locale) throws EntityNotFoundException {
        localeDao.deleteLocale(locale);
    }


}
