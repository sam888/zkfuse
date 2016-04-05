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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The persistent class for the Module database table.
 *
 * @author Samuel Huang
 */
@Entity
@ToString
@EqualsAndHashCode
@Table(name="Module", uniqueConstraints = @UniqueConstraint(columnNames = "Name"))
@NamedQueries({ @NamedQuery(name="Module.getModuleByName", query = "from Module where name = ?1") })
public class Module implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	public static final String NAMED_QUERY_GET_MODULE_BY_NAME = "Module.getModuleByName";
	
	/** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
    public static final Module EMPTY_MODULE = new Module("");

    static {
        EMPTY_MODULE.setModuleId( new Long(-1) );
    }

    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long moduleId;

	@Column(length=255)
	private String description;

    @Column(nullable=false, length=255)
    @NotEmpty(message="Name is mandatory")
	private String name;

	/** if not FetchType.EAGER, LazyInitialization can occur if access ResourceBundle
		list from Module after session closed. Commented out as ResourceBundle shudn't
		be accessed from Locale **/ 
	//  bi-directional many-to-one association to ResourceBundle
	// @OneToMany(mappedBy="module", fetch= FetchType.EAGER)
	// private List<ResourceBundle> resourceBundles;

	
	/** No argument constructor. Need this or will get: javax.persistence.PersistenceException: org.hibernate.InstantiationException: No default constructor for entity: ... **/
    // public Module() {}

    // No argument constructor will be genrated by @NoArgsConstructor

    public Module(String name) {
       this.name = name;
    }

    public Module() {}

    // Getters & setters below

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}