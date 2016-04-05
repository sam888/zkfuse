package org.zkfuse.model.i18n;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * The persistent class for the KeyValue database table.
 *
 * @author Samuel Huang
 */
//@Data
@Entity
@ToString
@EqualsAndHashCode
@Table(name="KeyValue", uniqueConstraints = @UniqueConstraint(columnNames = {"Property", "ResourceBundleId"}))
@NamedQueries({
    @NamedQuery(name = "KeyValue.getKeyValue", query = "from KeyValue where resourceBundle.resourceBundleId = ?1 and Property = ?2" ),
    @NamedQuery(name = "KeyValue.deleteKeyValue", query = "delete from KeyValue where keyValueId = ?1"  )
})
public class KeyValue implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_GET_KEY_VALUE = "KeyValue.getKeyValue";
    public static final String NAMED_QUERY_DELETE_KEY_VALUE = "KeyValue.deleteKeyValue";

    /** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
    public static final KeyValue EMPTY_KEY_VALUE = new KeyValue();

    static {
        EMPTY_KEY_VALUE.setKeyValueId( new Long(-1) );
    }

    public KeyValue() {} // no arg constructor

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long keyValueId;

    @Column(name="Description", length=255, nullable=true)
	private String description;

	@Column(name="Property", nullable=false, length=45 )
	private String property;

	@Column(name="Value", nullable=false)
	private String value;

    //bi-directional many-to-one association to ResourceBundle
    @ManyToOne( fetch=FetchType.EAGER )
	@JoinColumn(name="ResourceBundleId", nullable=false)
	private ResourceBundle resourceBundle;

    // Getters and setters below

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Long getKeyValueId() {
        return keyValueId;
    }

    public void setKeyValueId(Long keyValueId) {
        this.keyValueId = keyValueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}