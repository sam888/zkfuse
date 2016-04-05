package org.zkfuse.model.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the security_permission_role database table.
 *
 * @author Samuel Huang
 */
@Entity
@EqualsAndHashCode( exclude={"created_date", "created_by"} )
@Table(name="security_permission_role")
@AssociationOverrides({
        @AssociationOverride(name = "id.securityPermission",
                joinColumns = @JoinColumn(name = "permission_id")),
        @AssociationOverride(name = "id.securityRole",
                joinColumns = @JoinColumn(name = "role_id")) })
@NamedQueries({
        @NamedQuery(name = "deleteByRoleIdAndPermissionId", query = "delete from SecurityPermissionRole permission_role where permission_role.id.securityRole.roleId=?1 and permission_role.id.securityPermission.permissionId = ?2"  )
})
public class SecurityPermissionRole implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_DELETE_BY_ROLE_ID_AND_PERMISSION_ID = "deleteByRoleIdAndPermissionId";

    @EmbeddedId
	private SecurityPermissionRolePK id = new SecurityPermissionRolePK();

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", nullable = false, length = 10)
    private Date createdDate;

    public SecurityPermissionRolePK getId() {
        return id;
    }

    public void setId(SecurityPermissionRolePK id) {
        this.id = id;
    }

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Transient
	public SecurityRole getSecurityRole() {
       return getId().getSecurityRole();
    }
    public void setSecurityRole(SecurityRole securityRole) {
        getId().setSecurityRole( securityRole );
    }

    @Transient
    public SecurityPermission getSecurityPermission() {
        return getId().getSecurityPermission();
    }
    public void setSecurityPermission( SecurityPermission securityPermission ) {
        getId().setSecurityPermission( securityPermission );
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}