package org.zkfuse.model.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
//import org.eclipse.persistence.annotations.PrivateOwned;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
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
 * The persistent class for the security_user database table.
 *
 * @author Samuel Huang
 */
@Entity
@ToString
@EqualsAndHashCode( exclude = {"securityRoleUsers"})
@Table(name="security_user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_login_name"}))
@NamedQueries({
    @NamedQuery(name = "SecurityUser.getUserByLoginName", query = "from SecurityUser where userLoginName = ?1"  ),
    @NamedQuery(name = "SecurityUser.deleteUserByLoginName", query = "delete from SecurityUser where userLoginName = ?1"  )
})
public class SecurityUser implements Serializable {

	private static final long serialVersionUID = 1L;

    public static final String NAMED_QUERY_GET_USER_BY_LOGIN_NAME = "SecurityUser.getUserByLoginName";
    public static final String NAMED_QUERY_DELETE_USER_BY_LOGIN_NAME = "SecurityUser.deleteUserByLoginName";

    /** Returned by Dao/Service layer to stick to "Don't return Null' practice" **/
    public static final SecurityUser EMPTY_SECURITY_USER = new SecurityUser();

    static {
        EMPTY_SECURITY_USER.setUserId(new Long(-1));
    }

    public SecurityUser() {}

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="user_id", unique=true, nullable=false)
	private Long userId;

	@Column(name="user_login_name", unique=true, nullable=false)
    @NotEmpty(message="Login Name is mandatory and unique")
	private String userLoginName;

	@Column(name="user_password", nullable=false)
    @NotEmpty(message="Password can not be empty")
	private String userPassword;

	@Column(name="user_salt")
	private String userSalt;

    // @PrivateOwned // dependency on eclipse persistence, tests show it doesn't help with removing SecurityRoleUser when call merge(SecurityUSer) at runtime
    // @NotFound(action= NotFoundAction.IGNORE) // hide EntityNotFoundException when call merge(..)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id.securityUser", cascade= CascadeType.ALL, orphanRemoval=true )
    private Set<SecurityRoleUser> securityRoleUsers = new HashSet<SecurityRoleUser>();

    public SecurityRoleUser addSecurityRoleUser(SecurityRoleUser securityRoleUser) {
        getSecurityRoleUsers().add(securityRoleUser);
        securityRoleUser.setSecurityUser(this);
        return securityRoleUser;
    }

    public SecurityRoleUser removeSecurityRoleUser(SecurityRoleUser securityRoleUser) {
        getSecurityRoleUsers().remove(securityRoleUser);
        securityRoleUser.setSecurityUser(null);

        return securityRoleUser;
    }

    public List<String> getRoleList() {

        List<SecurityRole> securityRoleList = getSecurityRoles();
        List<String> roleList = new ArrayList<String>();

        for ( SecurityRole securityRole: securityRoleList ) {
            roleList.add( securityRole.getRoleName() );
        }
        return roleList;
    }

    /** A convenient method that returns the list of roles belonging to the current user **/
    public List<SecurityRole> getSecurityRoles() {

        List<SecurityRoleUser> securityRoleUserList = new ArrayList<SecurityRoleUser>( getSecurityRoleUsers() );
        List<SecurityRole> securityRoleList = new ArrayList<SecurityRole>();

        for ( SecurityRoleUser securityRoleUser: securityRoleUserList ) {
            SecurityRole securityRole = securityRoleUser.getSecurityRole();
            if ( securityRole != null ) {
                securityRoleList.add( securityRole );
            }
        }
        return securityRoleList;
    }

    // Getters & setters below

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public Set<SecurityRoleUser> getSecurityRoleUsers() {
        return securityRoleUsers;
    }

    public void setSecurityRoleUsers(Set<SecurityRoleUser> securityRoleUsers) {
        this.securityRoleUsers = securityRoleUsers;
    }
}