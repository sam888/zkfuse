package org.zkfuse.model.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
 * The persistent class for the security_role_user database table.
 *
 * @author Samuel Huang
 */
@Entity
@ToString
@EqualsAndHashCode(exclude={"created_date", "created_by"})
@Table(name="security_role_user")
@AssociationOverrides(value = {
        @AssociationOverride(name = "id.securityRole",
                joinColumns = @JoinColumn(name = "role_id")),
        @AssociationOverride(name = "id.securityUser",
                joinColumns = @JoinColumn(name = "user_id"))})
@NamedQueries({
        @NamedQuery(name = "deleteByUserIdAndRoleId", query = "delete from SecurityRoleUser role_user where role_user.id.securityUser.userId = ?1 and role_user.id.securityRole.roleId = ?2"  )
})
public class SecurityRoleUser implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_DELETE_BY_USER_ID_AND_ROLE_ID = "deleteByUserIdAndRoleId";

    @EmbeddedId
    private SecurityRoleUserPK id = new SecurityRoleUserPK();

    public SecurityRoleUserPK getId() {
        return id;
    }

    public void setId(SecurityRoleUserPK id) {
        this.id = id;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date", nullable = false, length = 10)
    private Date createdDate;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

	public SecurityRoleUser() {
	}

    @Transient
    public SecurityRole getSecurityRole() {
        return getId().getSecurityRole();
    }

    public void setSecurityRole(SecurityRole securityRole) {
        getId().setSecurityRole( securityRole );
    }

    @Transient
    public SecurityUser getSecurityUser() {
        return getId().getSecurityUser();
    }

    public void setSecurityUser(SecurityUser securityUser) {
        getId().setSecurityUser(securityUser);
    }

    // Getters & setters

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