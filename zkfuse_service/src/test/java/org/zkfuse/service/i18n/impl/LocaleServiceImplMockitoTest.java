package org.zkfuse.service.i18n.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.zkfuse.dao.i18n.LocaleDao;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.model.i18n.Locale;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Samuel Huang
 */
@RunWith(MockitoJUnitRunner.class)
public class LocaleServiceImplMockitoTest {
    
    @Mock 
    private LocaleDao localeDao;

    @Mock 
    private BasicDao<Locale, Long> basicDao;
    
    @InjectMocks
    private LocaleServiceImpl localeService;
    
    private Locale existentLocale = null;
    
    @Before
    public void setUp() {
        existentLocale = new Locale("English", "en", "US");
    }
    
    /** Verify localeService.count() delegates call to basicDao.count(Locale.class) */
    @Test
    public void testCountSuccess() throws Exception {
        Long testCount = new Long(777);
        when(basicDao.count(Locale.class)).thenReturn( testCount );
        assertTrue( localeService.count().equals( testCount ) ); 
        verify(basicDao, times(1)).count( Locale.class );
    }

    /** Verify localeService.getLocale(Locale) delegates call to localeDao.getLocale(Locale) */
    @Test
    public void testGetLocaleByTypeSuccess() throws Exception {
        when(localeDao.getLocale( Locale.EMPTY_LOCALE )).thenReturn( existentLocale );
        assertTrue( localeService.getLocale( Locale.EMPTY_LOCALE ).equals( existentLocale )); 
        verify(localeDao, times(1)).getLocale( Locale.EMPTY_LOCALE );
    }
    
    /** Verify localeService.getLocale(languageCode, countryCode, variantCode) delegates call to 
     *             localeDao.getLocale(languageCode, countryCode, variantCode) **/
    @Test
    public void testGetLocaleByStringSuccess() throws Exception {

        when( localeDao.getLocale(Locale.EMPTY_LOCALE.getLanguageCode(), Locale.EMPTY_LOCALE.getCountryCode(),
                Locale.EMPTY_LOCALE.getVariantCode() )).thenReturn( existentLocale );

        Locale retrievedLocale = localeService.getLocale(Locale.EMPTY_LOCALE.getLanguageCode(), Locale.EMPTY_LOCALE.getCountryCode(),
                Locale.EMPTY_LOCALE.getVariantCode());

        assertTrue( retrievedLocale == existentLocale );
        verify(localeDao, times(1)).getLocale(Locale.EMPTY_LOCALE.getLanguageCode(), Locale.EMPTY_LOCALE.getCountryCode(), Locale.EMPTY_LOCALE.
                getVariantCode() );
    }

    /** Verify localeService.deleteLocale(languageCode, countryCode, variantCode) delegates call to 
     *             localeDao.deleteLocale(languageCode, countryCode, variantCode) **/
    @Test
    public void testDeleteLocaleByStringSuccess() throws Exception {

        localeService.deleteLocale(Locale.EMPTY_LOCALE.getLanguageCode(), Locale.EMPTY_LOCALE.getCountryCode(),
                Locale.EMPTY_LOCALE.getVariantCode());

        verify(localeDao, times(1)).deleteLocale(Locale.EMPTY_LOCALE.getLanguageCode(), Locale.EMPTY_LOCALE.getCountryCode(),
                Locale.EMPTY_LOCALE.getVariantCode() );
    }

    /** Verify localeService.deleteLocale(Locale) delegates call to localeDao.deleteLocale(Locale) **/
    @Test
    public void testDeleteLocaleByLocaleSuccess() throws Exception {
        localeService.deleteLocale( existentLocale );
        verify(localeDao, times(1)).deleteLocale( existentLocale );
    }

}
