package org.zkfuse.model.security;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The persistent class for the security_role database table.
 *
 * @author Samuel Huang
 */
@Entity
@ToString
@EqualsAndHashCode( exclude = {"securityPermissionRoles","securityRoleUsers"})
@NoArgsConstructor
@Table(name="security_role", uniqueConstraints = @UniqueConstraint(columnNames = {"role_name"}))
@NamedQueries({
    @NamedQuery(name = "SecurityRole.getRoleByRoleName", query = "from SecurityRole where roleName = ?1"  ),
    @NamedQuery(name = "SecurityRole.deleteRoleByRoleName", query = "delete from SecurityRole where roleName = ?1"  )
})
public class SecurityRole implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_GET_ROLE_BY_ROLE_NAME = "SecurityRole.getRoleByRoleName";
    public static final String NAMED_QUERY_DELETE_ROLE_BY_ROLE_NAME = "SecurityRole.deleteRoleByRoleName";

    /** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
    public static final SecurityRole EMPTY_SECURITY_ROLE = new SecurityRole();

    static {
        EMPTY_SECURITY_ROLE.setRoleId(new Long(-1));
    }

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="role_id", unique=true, nullable = false)
	private Long roleId;

	@Column(name="role_name", unique=true, nullable = false)
    @NotEmpty(message="Role Name is mandatory and unique")
	private String roleName;

    @Column(name="description", nullable = true)
    private String description;

	// bi-directional one-to-many association to SecurityPermissionRole
	@OneToMany(fetch = FetchType.EAGER, mappedBy="id.securityRole", cascade=CascadeType.ALL, orphanRemoval=true)
	private Set<SecurityPermissionRole> securityPermissionRoles = new HashSet<SecurityPermissionRole>();

    // bi-directional one-to-many association to SecurityRoleUser
    //    @OneToMany(fetch = FetchType.EAGER, mappedBy="id.securityRole", cascade=CascadeType.ALL, orphanRemoval=true)
    //    private Set<SecurityRoleUser> securityRoleUsers = new HashSet<SecurityRoleUser>();

    // add & remove methods for maintaining a collection of SecurityPermissionRole

    public SecurityPermissionRole addSecurityPermissionRole(SecurityPermissionRole securityPermissionRole) {
		getSecurityPermissionRoles().add(securityPermissionRole);
		securityPermissionRole.setSecurityRole(this);
		return securityPermissionRole;
	}

	public SecurityPermissionRole removeSecurityPermissionRole(SecurityPermissionRole securityPermissionRole) {
		getSecurityPermissionRoles().remove(securityPermissionRole);
		securityPermissionRole.setSecurityRole(null);

        SecurityPermission securityPermission = securityPermissionRole.getSecurityPermission();
        if ( securityPermission != null ) {
            securityPermission.removeSecurityPermissionRole( securityPermissionRole );
        }

		return securityPermissionRole;
	}

    // add & remove methods for maintaining a collection of SecurityRoleUser

    //    public SecurityRoleUser addSecurityRoleUser( SecurityRoleUser securityRoleUser ) {
    //        getSecurityRoleUsers().add( securityRoleUser );
    //        securityRoleUser.setSecurityRole(this);
    //        return securityRoleUser;
    //    }
    //
    //    public SecurityRoleUser removeSecurityRoleUser(SecurityRoleUser securityRoleUser) {
    //        getSecurityRoleUsers().remove(securityRoleUser);
    //        securityRoleUser.setSecurityRole(null);
    //
    //        SecurityUser securityUser = securityRoleUser.getSecurityUser();
    //        if ( securityUser != null ) {
    //            securityUser.removeSecurityRoleUser( securityRoleUser );
    //        }
    //
    //        return securityRoleUser;
    //    }

    /** A convenient method that returns the list of permissions assigned to this role**/
    public List<SecurityPermission> getSecurityPermissions() {

        List<SecurityPermissionRole> securityPermissionRoleList = new ArrayList<SecurityPermissionRole>( getSecurityPermissionRoles() );
        List<SecurityPermission> securityPermissionList = new ArrayList<SecurityPermission>();

        for ( SecurityPermissionRole securityPermissionRole: securityPermissionRoleList ) {
            SecurityPermission securityPermission = securityPermissionRole.getSecurityPermission();
            if ( securityPermission != null ) {
                securityPermissionList.add( securityPermission );
            }
        }
        return securityPermissionList;
    }

    // Getters & setters below

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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