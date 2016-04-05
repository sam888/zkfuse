package org.zkfuse.service.security;

import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.model.security.SecurityPermissionRole;
import org.zkfuse.model.security.SecurityRole;
import org.zkfuse.service.IGenericService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 13/07/13
 */
public interface SecurityRoleService extends IGenericService<SecurityRole, Long> {

    SecurityRole getRoleByRoleName(String roleName);

    void deleteRoleByRoleName(String roleName);

    void updateAssignedPermissions(List<SecurityPermission> assignedSecurityPermissionList, SecurityRole securityRole);
}
