package org.zkfuse.model.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the security_permission database table.
 *
 * @author Samuel Huang
 */
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode( exclude = {"securityPermissionRoles"})
@Table(name="security_permission", uniqueConstraints = @UniqueConstraint(columnNames = {"permission_name"}))
@NamedQueries({
    @NamedQuery(name = "SecurityPermission.getByPermissionName", query = "from SecurityPermission where permissionName = ?1"  ),
    @NamedQuery(name = "SecurityPermission.deleteByPermissionName", query = "delete from SecurityPermission where permissionName = ?1"  )
})
public class SecurityPermission implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_GET_PERMISSION_BY_PERMISSION_NAME = "SecurityPermission.getByPermissionName";

    /** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
    public static final SecurityPermission EMPTY_SECURITY_PERMISSION = new SecurityPermission();

    static {
        EMPTY_SECURITY_PERMISSION.setPermissionId( new Long(-1) );
    }

    @Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="permission_id", unique=true, nullable = false)
	private Long permissionId;

	@Column(name="permission_name", nullable = false)
    @NotEmpty(message="Permission Name is mandatory and unique")
	private String permissionName;

    @Column(name="description", nullable = true)
    private String description;

	//bi-directional one-to-many association to SecurityPermissionRole
	@OneToMany(fetch = FetchType.EAGER, mappedBy="id.securityPermission", cascade= CascadeType.ALL, orphanRemoval=true)
	private Set<SecurityPermissionRole> securityPermissionRoles = new HashSet<SecurityPermissionRole>();

	public SecurityPermissionRole addSecurityPermissionRole(SecurityPermissionRole securityPermissionRole) {
		getSecurityPermissionRoles().add(securityPermissionRole);
		securityPermissionRole.setSecurityPermission(this);

		return securityPermissionRole;
	}

	public SecurityPermissionRole removeSecurityPermissionRole(SecurityPermissionRole securityPermissionRole) {
		getSecurityPermissionRoles().remove(securityPermissionRole);
		securityPermissionRole.setSecurityPermission(null);

        SecurityRole securityRole = securityPermissionRole.getSecurityRole();
        if ( securityRole != null ) {
            securityRole.removeSecurityPermissionRole( securityPermissionRole );
        }
		return securityPermissionRole;
	}

    // Getters & setters below

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SecurityPermissionRole> getSecurityPermissionRoles() {
        return securityPermissionRoles;
    }

    public void setSecurityPermissionRoles(Set<SecurityPermissionRole> securityPermissionRoles) {
        this.securityPermissionRoles = securityPermissionRoles;
    }
}