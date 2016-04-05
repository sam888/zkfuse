package org.zkfuse.model.security;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * The primary key class for the security_permission_role database table.
 *
 * @author Samuel Huang
 */
@Embeddable
@EqualsAndHashCode
@ToString( exclude={"securityRole","securityPermission"}) // avoid java.lang.StackOverflowError caused by endless loop. Side effect of using @Data
public class SecurityPermissionRolePK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    // Don't even think about removing lazy fetch, exception will happen
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityRole securityRole;

    // Don't even think about removing lazy fetch, exception will happen
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityPermission securityPermission;

    public SecurityPermissionRolePK() {}

    public SecurityRole getSecurityRole() {
        return securityRole;
    }

    public void setSecurityRole(SecurityRole securityRole) {
        this.securityRole = securityRole;
    }

    public SecurityPermission getSecurityPermission() {
        return securityPermission;
    }

    public void setSecurityPermission(SecurityPermission securityPermission) {
        this.securityPermission = securityPermission;
    }
}