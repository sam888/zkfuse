package org.zkfuse.dao.security.impl;

import org.springframework.stereotype.Service;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.dao.security.SecurityPermissionRoleDao;
import org.zkfuse.model.security.SecurityPermissionRole;
import org.zkfuse.model.security.SecurityPermissionRolePK;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
@Service
public class SecurityPermissionRoleDaoImpl extends BasicDao<SecurityPermissionRole, SecurityPermissionRolePK> implements SecurityPermissionRoleDao {

    public SecurityPermissionRoleDaoImpl() {
        super( SecurityPermissionRole.class );
    }

    @Override
    public void delete(Long roleId, Long permissionId) {
        Object[] paramsArray = new Object[] { roleId, permissionId };
        executeSingleNamedQuery( SecurityPermissionRole.NAMED_QUERY_DELETE_BY_ROLE_ID_AND_PERMISSION_ID, paramsArray );
    }

}
