package org.zkfuse.dao.security;

import org.zkfuse.dao.IBasicDao;
import org.zkfuse.model.security.SecurityRole;

/**
 * Created with IntelliJ IDEA.
 * @author Samuel Huang
 */
public interface SecurityRoleDao extends IBasicDao<SecurityRole, Long> {

    SecurityRole getRoleByRoleName(String roleName);

    void deleteRoleByRoleName(String roleName);
}
