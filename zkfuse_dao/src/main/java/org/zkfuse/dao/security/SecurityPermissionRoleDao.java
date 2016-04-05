package org.zkfuse.dao.security;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.security.SecurityPermissionRole;
import org.zkfuse.model.security.SecurityPermissionRolePK;

/**
 * Created with IntelliJ IDEA.
 * Author: Sam
 */
public interface SecurityPermissionRoleDao extends IBasicDao<SecurityPermissionRole, SecurityPermissionRolePK> {

    /**
     * Delete a record form the link table security_permission_role by role Id and permission Id
     *
     * @param roleId
     * @param permissionId
     */
    void delete(Long roleId, Long permissionId);
}
