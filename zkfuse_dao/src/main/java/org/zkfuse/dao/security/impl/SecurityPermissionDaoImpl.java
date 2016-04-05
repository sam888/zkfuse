package org.zkfuse.dao.security.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.dao.security.SecurityPermissionDao;
import org.zkfuse.model.security.SecurityPermission;

import javax.persistence.NoResultException;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
@Service
public class SecurityPermissionDaoImpl extends BasicDao<SecurityPermission, Long> implements SecurityPermissionDao {

    public SecurityPermissionDaoImpl() {
        super( SecurityPermission.class );
    }

    @Override
    public void deleteSecurityPermissionByPermissionName(String loginName) {
        SecurityPermission securityPermission = getSecurityPermissionByPermissionName( loginName );
        if ( securityPermission != SecurityPermission.EMPTY_SECURITY_PERMISSION ) {
            remove( SecurityPermission.class, securityPermission.getPermissionId() );
        }
    }

    @Override
    public SecurityPermission getSecurityPermissionByPermissionName(String permissionName) {
        Object obj = null;
        Object[] namedQueryData = new Object[] { permissionName };

        try {
            obj = findNamedQuerySingle( SecurityPermission.NAMED_QUERY_GET_PERMISSION_BY_PERMISSION_NAME, namedQueryData );
        } catch (NoResultException ex) {
            return SecurityPermission.EMPTY_SECURITY_PERMISSION;
        }

        return ( obj != null ? (SecurityPermission)obj : SecurityPermission.EMPTY_SECURITY_PERMISSION );
    }
}
