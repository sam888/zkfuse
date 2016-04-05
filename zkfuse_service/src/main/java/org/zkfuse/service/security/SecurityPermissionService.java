package org.zkfuse.service.security;

import org.zkfuse.model.security.SecurityPermission;
import org.zkfuse.service.IGenericService;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 * Date: 13/07/13
 */
public interface SecurityPermissionService extends IGenericService<SecurityPermission, Long> {

    SecurityPermission getSecurityPermissionByPermissionName(String permissionName);

    void deleteSecurityPermissionByPermissionName(String permissionName);
}
