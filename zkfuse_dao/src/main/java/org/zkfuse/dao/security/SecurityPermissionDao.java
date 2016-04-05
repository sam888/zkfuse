package org.zkfuse.dao.security;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.security.SecurityPermission;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
public interface SecurityPermissionDao extends IBasicDao<SecurityPermission, Long> {

    SecurityPermission getSecurityPermissionByPermissionName(String permissionName);

    void deleteSecurityPermissionByPermissionName(String permissionName);
}
