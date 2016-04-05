package org.zkfuse.model.i18n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the ResourceBundle database table.
 *
 * @author Samuel Huang
 */
@Entity
@ToString
@Table(name="ResourceBundle", uniqueConstraints = @UniqueConstraint(columnNames = {"LocaleId", "ModuleId" }))
@NamedQueries({
    @NamedQuery(name = "ResourceBundle.getResourceBundle", 
        query = "from ResourceBundle where locale.localeId = ?1 and module.moduleId = ?2 and resourceBundleName = ?3"  ),
   @NamedQuery(name = "ResourceBundle.getResourceBundleByWildCardName", 
        query = "from ResourceBundle where resourceBundleName like ?1"  )
})
public class ResourceBundle implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
	public static final String NAMED_QUERY_GET_RESOURCE_BUNDLE = "ResourceBundle.getResourceBundle";
	
	public static final String NAMED_QUERY_GET_RESOURCE_BUNDLE_BY_WILD_CARD_NAME = "ResourceBundle.getResourceBundleByWildCardName";

	/** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
    public static final ResourceBundle EMPTY_RESOURCE_BUNDLE = new ResourceBundle();

    static {
        EMPTY_RESOURCE_BUNDLE.setResourceBundleId( new Long(-1) );
    }

    public ResourceBundle() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long resourceBundleId;

	@Column(nullable=false, length=255)
    @NotEmpty(message="Name is mandatory")
	private String resourceBundleName;

	@Column(length=255)
	private String description;

    /* need to use eager fetch here else will get Lazy LazyInitializationException in frontend */
    // bi-directional one-to-many association to KeyValue
	@OneToMany(mappedBy="resourceBundle",  cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
	private List<KeyValue> keyValues = new ArrayList<KeyValue>();

	// bi-directional many-to-one association to Locale
    @ManyToOne( fetch=FetchType.EAGER  )
	@JoinColumn(name="LocaleId", nullable=false)
    @NotNull(message="Locale is mandatory")
	private Locale locale;

	// bi-directional many-to-one association to Module
    @ManyToOne( fetch=FetchType.EAGER )
	@JoinColumn(name="ModuleId", nullable=false)
    @NotNull(message="Module is mandatory")
	private Module module;
    

    /** No argument constructor. Need this or will get: javax.persistence.PersistenceException: org.hibernate.InstantiationException: No default constructor for entity: ... **/
    // public ResourceBundle() {}

    // No argument constructor will be genrated by @NoArgsConstructor

    /** Constructor **/
    public ResourceBundle( Locale locale, Module module, String resourceBundleName ) {
        this.locale = locale;
        this.module = module;
        this.resourceBundleName = resourceBundleName;
    }

  // override this rather than letting lombok generate it for you else will get java.lang.StackOverflowError
  @Override
  public String toString() {
      return "ResourceBundle [resourceBundleId=" + resourceBundleId
              + ", resourceBundleName=" + resourceBundleName
              + ", locale=" + locale + ",\n module=" + module
              + ",\n description=" + description + "]";
  }

   // Getters/setters below

    public Long getResourceBundleId() {
        return resourceBundleId;
    }

    public void setResourceBundleId(Long resourceBundleId) {
        this.resourceBundleId = resourceBundleId;
    }

    public String getResourceBundleName() {
        return resourceBundleName;
    }

    public void setResourceBundleName(String resourceBundleName) {
        this.resourceBundleName = resourceBundleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<KeyValue> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(List<KeyValue> keyValues) {
        this.keyValues = keyValues;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}