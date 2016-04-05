package org.zkfuse.dao.i18n.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.i18n.LocaleDao;
import org.zkfuse.model.i18n.Locale;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@ContextConfiguration(locations = {"classpath:spring/applicationContext-dao.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class LocaleDaoImplTest {
	
	@Autowired
	private LocaleDao localeDao;

	private Locale existentLocaleWithChildren = null;
	private Locale existentLocaleWithNoChildren = null;
	private Locale nonExistentLocale = null;
	
	
	/**
	 * Test data for MySQL 
	 * 
	 * INSERT INTO Locale( `Name`, `LanguageCode`,`CountryCode`,`VariantCode`,`Description`) VALUES('English','en', 'US', '', null);
	 * INSERT INTO Locale( `Name`, `LanguageCode`,`CountryCode`,`VariantCode`,`Description`) VALUES('Chinese','zh', 'CN', '', null);
	 */
	@Before
	public void setUp() {
	    
	    existentLocaleWithChildren = new Locale("English", "en", "US"); // assumption: this record exists in DB already and has children (i.e. ResourceBundle)
	    existentLocaleWithNoChildren = new Locale("Chinese", "zh", "CN"); // assumption: this record exists in DB already
	    nonExistentLocale = new Locale("ZK", "zk", "ZK");
	}
	
	/* uncomment if using persistence.xml to define EntityManager 

        	private static final String PERSISTENCE_UNIT_NAME = "zkfuse";
        	private static EntityManagerFactory factory;
        	
        	@Test
        	public void testGetLocaleByEntityManagerQuerySuccess() {
        		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        		EntityManager em = factory.createEntityManager();
        		// Read the existing entries and write to console
        		Query q = (Query) em.createQuery("select l from Locale l");
        		List<Locale> localeList = q.getResultList();
        		for (Locale loc : localeList ) {
        			System.out.println( loc );
        		}
        		System.out.println("Size: " + localeList.size());
        		em.close();
        	}
	*/
	
	
	@Test
	public void testCountSuccess() throws Exception {
		assertTrue( localeDao.count() > 0);
	}
	
	
	@Test
	public void testFindAllLocaleSuccess() throws Exception {
		List<Locale> localeList = localeDao.findAll(Locale.class);
		assertNotNull( localeList );
		assertTrue(  localeList.size() > 0 );
		/*for ( Locale locale: localeList ) {
			System.out.println( locale.toString() );
		}*/
	}

	
	@Test
	public void testPersist_insertNonExistentLocaleSuccess() throws Exception {
		long count = localeDao.count();
		assertNull( nonExistentLocale.getLocaleId() ); // before persist
		
		localeDao.persist( nonExistentLocale );
		
		assertTrue( (localeDao.count() - count) == 1 ); // after persist
		assertNotNull( nonExistentLocale.getLocaleId() ); // confirm PK populated
 	}

	
	/** Verify PersistenceException is thrown when persisting Locale that breaks unique constraint defined in Locale POJO */
	@Test(expected=PersistenceException.class)
	public void testPersist_throwExceptionByBreakingUniqueConstraint() throws Exception {
		localeDao.persist( existentLocaleWithChildren );
	}

	
	/** Tests to see getLocale(..) can return a non-empty Locale successfully if searched record exists in database */
	@Test
	public void testGetLocale_forExistentDataSuccess() throws Exception  {
	    Locale locale = localeDao.getLocale(existentLocaleWithChildren.getLanguageCode(), existentLocaleWithChildren.getCountryCode(), "");
	    assertNotNull( locale );
	    assertFalse( locale == Locale.EMPTY_LOCALE ); // not empty locale
	}

	
	/** Tests to see getLocale(..) will return an empty Locale(i.e. Locale.EMPTY_LOCALE) if searched record doesn't exist in database */
	@Test
	public void testGetLocale_returnEmptyLocalForNonExistentDataSuccess() throws Exception  {
	    Locale locale = localeDao.getLocale( "aka", "Wadiyan", "");
	    assertTrue( locale == Locale.EMPTY_LOCALE );
	}

	
	/** Tests to see merge(..) updates existing Locale record successfully */
	@Test
	public void testMerge_updateExistingLocaleSuccess() throws Exception {
	    String data = "testing...";
	    Locale locale = localeDao.getLocale( existentLocaleWithChildren );
	    
	    assertFalse( Locale.EMPTY_LOCALE == locale ); // yes, existentLocale exists in DB
	    assertFalse( data.equals( locale.getDescription() ) ); // before merge
	    
	    locale.setDescription( data );
	    localeDao.merge(locale);
	    
	    Locale updatedLocale = localeDao.getLocale( locale );
	    assertTrue( data.equals( updatedLocale.getDescription() ) ); // after merge
	}
	

	/** Tests to see merge(..) inserts new record for non-existent Locale */
	@Test
	public void testMerge_insertNonExistentLocaleSuccess() throws Exception {
	    Locale locale = localeDao.getLocale( nonExistentLocale );
	    assertTrue( Locale.EMPTY_LOCALE == locale ); // confirm non-existent Locale
	    
	    localeDao.merge( nonExistentLocale );
	    localeDao.flush();
	    
	    System.out.println("Locale before merge: " + nonExistentLocale);
	    Locale insertedLocale = localeDao.getLocale( nonExistentLocale );
	    System.out.println( "Locale after merge: " + insertedLocale + "\n");
	    
	    assertFalse( Locale.EMPTY_LOCALE == insertedLocale ); // after merge
	    assertNotNull( insertedLocale.getLocaleId() ); // confirm PK populated
	}
	
	
	/** Tests to see deleteLocale(..) can delete an existing Locale that has no children row (i.e. ResourceBundle) **/
	@Test
	@Ignore
	public void testDeleteLocal_forExistentRecordWithNoChildrenSuccess() throws Exception {
	    
	    assertFalse( Locale.EMPTY_LOCALE == localeDao.getLocale(existentLocaleWithNoChildren) );
	    
	    localeDao.deleteLocale( existentLocaleWithNoChildren.getLanguageCode(), existentLocaleWithNoChildren.getCountryCode(), 
	            existentLocaleWithNoChildren.getVariantCode() );
	    
	    // getLocale() should return empty locale after successful delete
	    assertTrue( Locale.EMPTY_LOCALE == localeDao.getLocale(existentLocaleWithNoChildren) );
	}
	

	/** Tests to see deleteLocale(..) will throw ConstraintViolationException when deleting an existing Locale with 
	 *  children row (i.e. ResourceBundle) to violate foreign key constraint. 
	 * @throws Throwable **/
	@Test
	public void testDeleteLocal_throwsExceptionForExistentRecordWithChildren() throws Exception {
	    try {
	        localeDao.deleteLocale( existentLocaleWithChildren );
	        localeDao.flush();
	        fail("Should not get here");
	    } catch (PersistenceException e) {
	        Throwable c = e.getCause();
	        System.out.println("c " + c);
	        assertTrue( c instanceof ConstraintViolationException );
	    } catch (Exception e) {
	        e.printStackTrace();
	        fail("Unexpected exception occured");
	    }
	}
	

	/** Tests to see deleteLocale(..) throws the expected Exception when input non-existent locale **/
	@Test(expected=EntityNotFoundException.class)
	public void testDeleteLocale_throwExceptionForNonExistentRecord() throws Exception {
	   localeDao.deleteLocale( nonExistentLocale.getLanguageCode(), nonExistentLocale.getCountryCode(), nonExistentLocale.getVariantCode() );
	}

	
	/** Verify getLocale(locale) delegates work to getLocale(locale.getLanguageCode(), locale.getCountryCode(), locale.getVariantCode())  **/
	@Test
	public void testGetLocaleWithLocaleParameterSuccess() throws Exception {
	    localeDao = mock(LocaleDaoImpl.class);
	    
	    // stub localeDao.getLocale(existentLocale.getLanguageCode(), existentLocale.getCountryCode(), "")
	    stub(localeDao.getLocale(existentLocaleWithChildren.getLanguageCode(), existentLocaleWithChildren.getCountryCode(), 
	            existentLocaleWithChildren.getVariantCode()))
	        .toReturn(nonExistentLocale);
	    
	    // localeDao.getLocale(existentLocale) will call real method with stub method above
	    doCallRealMethod().when(localeDao).getLocale(existentLocaleWithChildren);
	    
	    assertEquals( localeDao.getLocale(existentLocaleWithChildren), nonExistentLocale );
	}

	
	/** Verify deleteLocale(locale) delegates work to 
	 *  deleteLocale(locale.getLanguageCode(), locale.getCountryCode(), locale.getVariantCode())  **/
	@Test
	public void testDeleteLocaleByTypeSuccess()  {
	    localeDao = mock(LocaleDaoImpl.class);
	    String message = "success";

	    doThrow( new RuntimeException( message ) ).when(localeDao).deleteLocale(existentLocaleWithChildren.getLanguageCode(), 
	            existentLocaleWithChildren.getCountryCode(), existentLocaleWithChildren.getVariantCode());
	    
	    // localeDao.deleteLocale(existentLocale) will call real method stubbed above
	    doCallRealMethod().when(localeDao).deleteLocale(existentLocaleWithChildren);
	    
	    try {
	        localeDao.deleteLocale(existentLocaleWithChildren);
	        fail();
	    } catch (RuntimeException e) {
	        assertTrue(e.getMessage().equals( message ));
	    }
	    
	    
	}
	
}
