package org.zkfuse.model.i18n;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the Locale database table.
 *
 * @author Samuel Huang
 */
//@Data

@ToString
@EqualsAndHashCode
@Entity
@Table(name="Locale", uniqueConstraints = {
      @UniqueConstraint(columnNames = {"LanguageCode", "CountryCode", "VariantCode"}),
      @UniqueConstraint(columnNames = {"Name"})
})
@NamedQueries({
    @NamedQuery(name = "Locale.getLocaleByParameters",
        query = "from Locale where languageCode = ?1 and countryCode = ?2 and variantCode = ?3"  )
})
public class Locale implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String NAMED_QUERY_GET_LOCALE_BY_PARAMETERS = "Locale.getLocaleByParameters";

    /** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
	public static final Locale EMPTY_LOCALE = new Locale("","","");

	static {
	    EMPTY_LOCALE.setLocaleId( new Long(-1));
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long localeId;

    @Column(nullable=false, unique=true, length=45)
	@NotEmpty(message="Name is mandatory and unique")
	private String name;

    @Column(nullable=false, length=45)
    @NotEmpty(message="Language Code is mandatory")
    private String languageCode;


    //	@NotEmpty(message="Country Code is mandatory")
    @Column(nullable=false, length=45)
    private String countryCode = "";

    @Column(nullable=true, length=45)
    private String variantCode = "";

	@Column(length=255)
	private String description;

	/**
	 *  // if not FetchType.EAGER, LazyInitialization can occur if access ResourceBundle
    	// list from Locale after session closed. Commented out as ResourceBundle shudn't
    	// be accessed from Locale

   	    //bi-directional many-to-one association to ResourceBundle
	    @OneToMany(mappedBy="locale", fetch= FetchType.EAGER)
	    private List<ResourceBundle> resourceBundles;
	*/

	/** Constructor. Need this or will get: javax.persistence.PersistenceException: org.hibernate.InstantiationException: No default constructor for entity: ... **/
	// public Locale() {}

    // A no argument constructor will be genrated by @NoArgsConstructor

	/** Constructor **/
	public Locale(java.util.Locale locale) {
	    setLocale(locale);
	}

    /** Constructor. Note variantCode is rarely used except maybe for the same language with different dialects **/
    public Locale(String name, String languageCode, String countryCode) {
        this(name, languageCode, countryCode, ""); // variantCode is NOT NULL in database
    }

    /** Constructor **/
    public Locale(String name, String languageCode, String countryCode, String variantCode) {
        this.name = name;
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.variantCode = variantCode;
    }

    public Locale(){}

    // For use in frontend
    public String getLabel() {
        String label = getName() + ", " + getLocaleValue();
        return label;
    }

    // used to compose resource bundle name
    public String getLocaleValue() {
        String localeValue = getLanguageCode();
        if ( StringUtils.isNotBlank( getCountryCode() ) ) {
            localeValue = localeValue + "_" + getCountryCode();
        }
        if ( StringUtils.isNotBlank( variantCode ) ) {
            localeValue = "_" + variantCode;
        }
        return localeValue;
    }

    // 	Getters & setters below

    public void setLocale( String languageCode, String countryCode, String variantCode ) {
        this.languageCode = languageCode;
        this.countryCode = countryCode;
        this.variantCode = variantCode;
    }

    /** a convenient method to set Locale using java.util.Locale **/
    public void setLocale(java.util.Locale locale) {
        this.countryCode = locale.getCountry();
        this.languageCode = locale.getLanguage();
        this.variantCode = locale.getVariant();
    }

    public Long getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Long localeId) {
        this.localeId = localeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getVariantCode() {
        return variantCode;
    }

    public void setVariantCode(String variantCode) {
        this.variantCode = variantCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}