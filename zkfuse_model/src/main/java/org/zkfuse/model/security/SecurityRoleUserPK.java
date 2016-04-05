package org.zkfuse.model.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the security_role_user database table.
 *
 * @author Samuel Huang
 */
@Embeddable
@EqualsAndHashCode
@ToString( exclude={"securityRole","securityUser"}) // avoid java.lang.StackOverflowError caused by endless loop. Side effect of using @Data
public class SecurityRoleUserPK implements Serializable {

	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    // Don't even think about removing lazy fetch, exception will happen
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityRole securityRole;

    // Don't even think about removing lazy fetch, exception will happen
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityUser securityUser;

	public SecurityRoleUserPK() {}

    public SecurityRole getSecurityRole() {
        return securityRole;
    }

    public void setSecurityRole(SecurityRole securityRole) {
        this.securityRole = securityRole;
    }

    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }
}