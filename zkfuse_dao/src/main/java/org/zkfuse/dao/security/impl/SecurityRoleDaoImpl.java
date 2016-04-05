package org.zkfuse.dao.security.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.dao.security.SecurityRoleDao;
import org.zkfuse.model.security.SecurityRole;

import javax.persistence.NoResultException;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
@Service
public class SecurityRoleDaoImpl extends BasicDao<SecurityRole, Long> implements SecurityRoleDao {

    public SecurityRoleDaoImpl() {
        super( SecurityRole.class );
    }

    @Override
    public void deleteRoleByRoleName(String roleName) {
        SecurityRole securityRole = getRoleByRoleName( roleName );
        if ( securityRole != SecurityRole.EMPTY_SECURITY_ROLE ) {
            remove(SecurityRole.class, securityRole.getRoleId());
        }
    }

    @Override
    public SecurityRole getRoleByRoleName(String roleName) {
        Object obj = null;
        Object[] namedQueryData = new Object[] { roleName };

        try {
            obj = findNamedQuerySingle( SecurityRole.NAMED_QUERY_GET_ROLE_BY_ROLE_NAME, namedQueryData );
        } catch (NoResultException ex) {
            return SecurityRole.EMPTY_SECURITY_ROLE;
        }

        return ( obj != null ? (SecurityRole)obj : SecurityRole.EMPTY_SECURITY_ROLE);
    }
}
